package com.williamngo.database;


import com.mysql.jdbc.log.Log;
import com.williamngo.beans.ConfigBean;
import com.williamngo.business.JagEmail;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jodd.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 1435707
 */
public class jagemail_database {
    public final Logger log = LoggerFactory.getLogger(getClass().getName());
    public ConfigBean cb;
    
    public jagemail_database(ConfigBean cb)
    {
        this.cb = cb;
    }
    public void populateEmailTable(JagEmail jagemail)
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "CS1435707";
        String password = "tripermu";
        
        String query = "INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(url, user, password)){
            PreparedStatement stmt = conn.prepareStatement(query);
            
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
        }catch(SQLException sqle)
        {
            log.error(sqle.getMessage());
        }
    }
}
