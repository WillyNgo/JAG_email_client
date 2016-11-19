package com.williamngo.database;


import com.mysql.jdbc.log.Log;
import com.williamngo.beans.ConfigBean;
import com.williamngo.beans.FolderBean;
import com.williamngo.business.Folder;
import com.williamngo.business.JagEmail;

import java.sql.*;

import java.util.*;
import javafx.collections.FXCollections;
import jodd.mail.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of database interface
 * @author 1435707
 */
public class JagEmailDAOImpl implements JagEmailDAO {
    public final Logger log = LoggerFactory.getLogger(getClass().getName());
    public ConfigBean cb;
    public FolderBean fb;
    
    
    public JagEmailDAOImpl(ConfigBean cb, FolderBean fb){
        this.cb = cb;
        this.fb = fb;
    }
    
    public JagEmailDAOImpl(ConfigBean cb)
    {
        this.cb = cb;
        this.fb = new FolderBean();
    }
    
    public JagEmailDAOImpl(){
        super();
    }
    
    /**
     * This method adds a JagEmail object to the database in the email table.
     * It takes an email's to, from, cc, subject, message, html, typeflag, 
     * received date and folder fields and stores them in the database.
     * 
     * @param jagemail - JagEmail to be stored in the database
     */
    @Override
    public void addEmail(JagEmail jagemail)
    {        
        String query = "INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            //get values to be set in the prepare statement
            String receivers = "";
            MailAddress[] listOfReceiver = jagemail.getTo();
            //Getting string containing all email addresses receiving emails seperated by ','
            for(MailAddress recipient : listOfReceiver)
            {
                receivers = (receivers.equals("") ? recipient + "" : receivers + "," + recipient);
            }
            
            //Getting CCs
            String cc = "";
            MailAddress[] listOfCc = jagemail.getCc();
            for(MailAddress recipient : listOfCc)
            {
                cc = (receivers.equals("") ? recipient + "" : receivers + "," + recipient);
            }
            //Getting sender
            String sender = jagemail.getFrom().getEmail();
            
            //Getting subjects
            String subject = jagemail.getSubject();
            
            //Getting message and html content;
            List<String> message = new ArrayList<>();
            List<String> html = new ArrayList<>();
            List<EmailMessage> msgList = jagemail.getAllMessages();
            for(EmailMessage msg : msgList)
            {
                if(msg.getMimeType().equalsIgnoreCase("TEXT/PLAIN"))
                    message.add(msg.getContent());
                if(msg.getMimeType().equalsIgnoreCase("TEXT/HTML"))
                    html.add(msg.getContent());
            }
            
            //Getting typeflag
            boolean typeFlag = jagemail.getTypeFlags();
            
            //Getting receive date
            
            java.util.Date date = new java.util.Date();
            Timestamp ts;
            if(jagemail.getReceiveDate() != null)
            {   
                date = jagemail.getReceiveDate();
                ts = new Timestamp(date.getTime());
            } else
                ts = null;
            //Getting folder
            String folder = jagemail.getFolder();
            
            //Setting values to prepared statement
            stmt.setString(1, receivers);
            stmt.setString(2, sender);
            stmt.setString(3, cc);
            stmt.setString(4, subject);
            stmt.setString(5, String.join(",", message));
            stmt.setString(6, String.join(",", html));
            stmt.setBoolean(7, typeFlag);
            stmt.setTimestamp(8, ts);
            stmt.setString(9, folder);
            
            //Execute query
            int result = stmt.executeUpdate();
            log.info("Added " + result + " results in email table");
            
            //Add attachment into database
            if(jagemail.getAttachments() != null){
                addAttachment(jagemail, stmt.getGeneratedKeys());
            }
        }catch(SQLException sqle)
        {
            log.error(sqle.getMessage());
        }
    }
    
    @Override
    public void deleteEmail(int messageNumber)
    {
        String query = "DELETE FROM emails WHERE messageNumber = ?;";
         try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setInt(1, messageNumber);
            
            int result = stmt.executeUpdate();
            log.info("Deleted " + result + " email from database");
         }
         catch(SQLException sqle)
         {
             sqle.getMessage();
         }
    }
    
    public void deleteEmail(String foldername){
        String query = "DELETE FROM emails WHERE folder = ?;";
         try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, foldername);
            
            int result = stmt.executeUpdate();
            log.info("Deleted " + result + " email(s) from database");
         }
         catch(SQLException sqle)
         {
             sqle.getMessage();
         }
    }
    
    /**
     * Returns list of JagEmail based on folder name
     * @param foldername
     * @return 
     */
    @Override
    public List<JagEmail> retrieveEmail(String foldername)
    {
        List<JagEmail> emailFound = new ArrayList<JagEmail>();
        
        String query = "SELECT sender, receiver, cc, subject_text, message, html, receive_date, folder FROM emails"
                + " WHERE folder = ?;";
        
        try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, foldername);
            
            try(ResultSet rs = stmt.executeQuery())
            {
                while(rs.next())
                {
                    //Creates JagEmail to be put into the list if email was found
                    String sender = rs.getString("sender");
                    String receiver = rs.getString("receiver");
                    String cc = rs.getString("cc");
                    String subject = rs.getString("subject_text");
                    String message = rs.getString("message");
                    String html = rs.getString("html");
                    java.util.Date receiveDate = rs.getTimestamp("receive_date");
                    String folder = rs.getString("folder");
                    
                    //Create email to be added to list
                    JagEmail emailToBeAdded = new JagEmail();
                    emailToBeAdded.from(sender);
                    for(String mailAddressTo : receiver.split(","))
                    {
                        emailToBeAdded.to(mailAddressTo);
                    }
                    for(String mailAddressCc : cc.split(","))
                    {
                        emailToBeAdded.cc(mailAddressCc);
                    }
                    emailToBeAdded.setSubject(subject);
                    
                    emailToBeAdded.addText(message);
                    emailToBeAdded.addHtml(html);
                    emailToBeAdded.setReceiveDate(receiveDate);
                    emailToBeAdded.setFolder(folder);
                    getAttachmentAndSetToEmail(emailToBeAdded);
                    
                    emailFound.add(emailToBeAdded);
                }
            }
        }
        catch(SQLException sqle)
        {
            sqle.getMessage();
        }
        
         return emailFound;
    }
    
    
    /**
     * Returns an list of jag email whose subject, sender or content of email contains the specified keyword
     * 
     * @param keyword
     * @return emailFound - List of email that contains the keyword
     */
    @Override
    public List<JagEmail> searchEmail(String keyword)
    {

        List<JagEmail> emailFound = new ArrayList<JagEmail>();
        String query = "SELECT sender, receiver, cc, subject_text, message, html, receive_date, folder FROM emails"
               + " WHERE (sender LIKE ? OR cc LIKE ? OR subject_text LIKE ? OR message LIKE ?);";
        
        try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            //Setting user id so as to only search for emails that belongs to the current user
            
            stmt.setString(1, "%"+keyword+"%");
            stmt.setString(2, "%"+keyword+"%");
            stmt.setString(3, "%"+keyword+"%");
            stmt.setString(4, "%"+keyword+"%");
            
            try(ResultSet rs = stmt.executeQuery())
            {
                while(rs.next())
                {
                    //Creates JagEmail to be put into the list if email was found
                    String sender = rs.getString("sender");
                    String receiver = rs.getString("receiver");
                    String cc = rs.getString("cc");
                    String subject = rs.getString("subject_text");
                    String message = rs.getString("message");
                    String html = rs.getString("html");
                    java.util.Date receiveDate = rs.getTimestamp("receive_date");
                    String folder = rs.getString("folder");
                    
                    //Create email to be added to list
                    JagEmail emailToBeAdded = new JagEmail();
                    emailToBeAdded.from(sender);
                    for(String mailAddressTo : receiver.split(","))
                    {
                        emailToBeAdded.to(mailAddressTo);
                    }
                    for(String mailAddressCc : cc.split(","))
                    {
                        emailToBeAdded.cc(mailAddressCc);
                    }
                    emailToBeAdded.setSubject(subject);
                    emailToBeAdded.addText(message);
                    emailToBeAdded.addHtml(html);
                    emailToBeAdded.setReceiveDate(receiveDate);
                    emailToBeAdded.setFolder(folder);
                    getAttachmentAndSetToEmail(emailToBeAdded);
                    
                    emailFound.add(emailToBeAdded);
                }
            }
        }
        catch(SQLException sqle)
        {
            sqle.getMessage();
        }
        
        return emailFound;
    }
    
    public void addFolder(String foldername){
        String query = "INSERT INTO folders(foldername) VALUES (?);";
        try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, foldername);
            
            int result = stmt.executeUpdate();
            log.info("Added " + result + " folder into folders table.");
        }
        catch(SQLException sqle){
            sqle.getMessage();
        }
    }
    
    public void deleteFolder(String foldername){
        String query = "DELETE FROM folders WHERE foldername = ?;";
        try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, foldername);
            
            //Delete all emails associated with the folders
            deleteEmail(foldername);
            
            int result = stmt.executeUpdate();
            log.info("DELETED '" + foldername + "' folder from folders table.");
        }
        catch(SQLException sqle){
            sqle.getMessage();
        }
    }
    
    /**
     * Retrieves all folder name in a list
     * @return myList - List containing all folder names
     */
    @Override
    public List<String> getAllFolders() 
    {
        List<String> myList = FXCollections.observableArrayList();
        String query = "SELECT foldername FROM Folders";
        
        try(Connection con = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword()))
        {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                myList.add(rs.getString("foldername"));
            }
        }catch(SQLException sqle)
        {
            System.out.println(sqle.getMessage());
        }
        
        return myList;
    }
    
    /**
     * Gets attachment associated with the email and attach them to the email.
     * 
     * @param attachment_id
     * @param jagemail 
     */
    private void getAttachmentAndSetToEmail(JagEmail jagemail)
    {
        String query = "SELECT attachmentByte, attachmentName, from attachments WHERE attachment_messageNumber = ?;";
        try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, jagemail.getMessageNumber());
            
            try(ResultSet rs = stmt.executeQuery())
            {
                while(rs.next())
                {
                    Blob fileData = rs.getBlob("attachmentByte");
                    byte[] stream = fileData.getBytes(1, (int)fileData.length());
                    String attachmentName = rs.getString("attachmentName");
                    
                    jagemail.attach(EmailAttachment.attachment().bytes(stream).setName(attachmentName).create());
                }
                
            }
        }
        catch(SQLException sqle)
        {
            sqle.getMessage();
        }
    }
    
    /**
     * Adds attachment to database by converting it to byte array
     * 
     * @param jagemail
     * @param rs 
     */
    private void addAttachment(JagEmail jagemail, ResultSet rs)
    {        
        String query = "INSERT INTO attachments (attach_messageNumber, attachmentName, attachmentByte) VALUES (?, ?, ?);";
        try(Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            //Getting attachment name
            String attachmentName = "";
            byte[] attachmentByte;
            
            //rs contains primary key value of email sent. Use this key to set it to attachment
            int key = -1;
            if(rs.next())
            {
                key = rs.getInt(1);
                stmt.setInt(1, key);
            }
            
            //No set attachment name and byte array
            for(EmailAttachment attach : jagemail.getAttachments())
            {
                attachmentName = attach.getName();
                attachmentByte = attach.toByteArray();
                
                //Setting attachment
                stmt.setString(2, attachmentName);
                stmt.setBytes(3, attachmentByte);
                
                //Execute query
                int result = stmt.executeUpdate();
                log.info("Added " + result + " results in attachment table");
            }
        }catch(SQLException sqle)
        {
            log.error(sqle.getMessage());
        }
    }
}
