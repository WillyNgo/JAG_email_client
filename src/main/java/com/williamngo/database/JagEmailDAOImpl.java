package com.williamngo.database;


import com.mysql.jdbc.log.Log;
import com.williamngo.beans.ConfigBean;
import com.williamngo.business.JagEmail;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import jodd.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 1435707
 */
public class JagEmailDAOImpl implements JagEmailDAO {
    public final Logger log = LoggerFactory.getLogger(getClass().getName());
    public ConfigBean cb;
    
    public JagEmailDAOImpl(ConfigBean cb)
    {
        this.cb = cb;
    }
    
    /**
     * This method adds a JagEmail object to the database in the email table.
     * It takes an email's to, from, cc, subject, message, html, typeflag, 
     * received date and folder fields and stores them in the database.
     * 
     * @param jagemail - JagEmail to be stored in the database
     */
    @Override
    public void addEmailToDatabase(JagEmail jagemail)
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        
        String query = "INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            //get values to be set in the prepare statement
            String receivers = "";
            MailAddress[] listOfReceiver = jagemail.getTo();
            //Getting string containing all email addresses receiving emails seperated by ','
            for(MailAddress recipient : listOfReceiver)
            {
                receivers = (receivers.equals("") ? recipient + "" : receivers + "," + recipient);
            }
            
            //Get email account id
            int email_account_id = jagemail.getUserId();
            log.debug("user of sending email" + email_account_id);
            
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
            List<String> message = new ArrayList();
            List<String> html = new ArrayList();
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
            if(jagemail.getReceiveDate() != null)
            {   
                date = jagemail.getReceiveDate(); 
            }
            Timestamp ts = new Timestamp(date.getTime());
            //Getting folder
            String folder = jagemail.getFolder();
            
            //Setting values to prepared statement
            stmt.setInt(1, email_account_id);
            stmt.setString(2, receivers);
            stmt.setString(3, sender);
            stmt.setString(4, cc);
            stmt.setString(5, subject);
            stmt.setString(6, String.join(",", message));
            stmt.setString(7, String.join(",", html));
            stmt.setBoolean(8, typeFlag);
            stmt.setTimestamp(9, ts);
            stmt.setString(10, folder);
            
            //Execute query
            int result = stmt.executeUpdate();
            log.info("Added " + result + " results in email table");
            
            //Add attachment into database
            addAttachmentToDatabase(jagemail, stmt.getGeneratedKeys());
        }catch(SQLException sqle)
        {
            log.error(sqle.getMessage());
        }
    }
    
    /**
     * Adds attachment to database by converting it to byte array
     * @param jagemail
     * @param rs 
     */
    @Override
    public void addAttachmentToDatabase(JagEmail jagemail, ResultSet rs)
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        
        String query = "INSERT INTO attachments (attach_messageNumber, attachmentName, attachmentByte) VALUES (?, ?, ?);";
        try(Connection conn = DriverManager.getConnection(url, user, password)){
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
                log.info("Added " + result + " results in email table");
            }
        }catch(SQLException sqle)
        {
            log.error(sqle.getMessage());
        }
    }
    
    /**
     * Adds a user account into the database
     * 
     * @param account_username - Username of the user
     * @param emailAddress - email address of the user
     * @param account_password - the password associated with its account
     */
    @Override
    public void addAccountToDatabase(String account_username, String emailAddress, String account_password)
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        
        String query = "INSERT INTO accounts (account_username, emailAddress, account_password) VALUES (?, ?, ?);";
         try(Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, account_username);
            stmt.setString(2, emailAddress);
            stmt.setString(3, account_password);
            
            stmt.executeQuery();
         }
         catch(SQLException sqle)
         {
             sqle.getMessage();
         }
    }
    
    @Override
    public void deleteEmailFromDatabase(JagEmail jagemail, int messageNumber)
    {
         String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        
        String query = "DELETE FROM emails WHERE messageNumber = ?;";
         try(Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setInt(1, messageNumber);
            
            stmt.executeQuery();
         }
         catch(SQLException sqle)
         {
             sqle.getMessage();
         }
    }
    
    /**
     * 
     * 
     * @param emailAddress
     * @param account_password 
     */
    @Override
    public void deleteAccountFromDatabase(String emailAddress, String account_password)
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        
        String query = "DELETE FROM accounts WHERE emailAddress = ? AND account_password = ?;";
         try(Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            stmt.setString(1, emailAddress);
            stmt.setString(2, account_password);
            
            stmt.executeQuery();
            log.info("Deleted account: " + emailAddress);
         }
         catch(SQLException sqle)
         {
             sqle.getMessage();
         }
    }
    
    @Override
    public List<JagEmail> searchEmail(String keyword)
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        List<JagEmail> emailFound = new ArrayList<JagEmail>();
        
        String query = "SELECT sender, cc, subject_text, message, html, receive_date, folder, attachmentByte FROM emails"
                + " INNER JOIN attachments ON emails.messageNumber = attachments.attach_messageNumber\n" +
                "WHERE sender LIKE \"%text%\" OR cc LIKE \"%text%\" OR subject_text LIKE \"%text%\" OR message LIKE \"%text%\";";
        
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            
           
        }
        catch(SQLException sqle)
        {
            sqle.getMessage();
        }
        
        return emailFound;
    }
    
    
    public int getUserIdFromDatabase()
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        int user_id = -1;
        String query = "SELECT account_id FROM accounts WHERE account_username = ?;";
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement stmt = conn.prepareStatement(query);
            
            String username = cb.getUserName();
            stmt.setString(1, username);
            
            try(ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                {
                    user_id = rs.getInt("account_id");
                    
                }
            }
            catch(SQLException sqle)
            {
                sqle.getMessage();
            }
        }
        catch(SQLException sqle)
        {
            sqle.getMessage();
        }
        
        return user_id;
    }
    
    
}
