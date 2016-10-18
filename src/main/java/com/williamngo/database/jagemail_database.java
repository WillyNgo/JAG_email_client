package com.williamngo.database;


import com.williamngo.business.JagEmail;
import java.sql.*;
import java.util.List;
import jodd.mail.*;

/**
 *
 * @author 1435707
 */
public class jagemail_database {
    public void populateEmailTable(JagEmail jagemail)
    {
        String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
        String user = "cs1435707";
        String password = "tripermu";
        
        String query = "INSERT INTO emails (receiver, sender, cc, message, html, typeFlag, receive_date, folder) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            String cc = "";
            MailAddress[] listOfCc = jagemail.getCc();
            for(MailAddress recipient : listOfCc)
            {
                cc = (receivers.equals("") ? recipient + "" : receivers + "," + recipient);
            }
            //Getting sender
            String sender = jagemail.getFrom().getEmail();
            
            //Getting message content;
            String message = "";
            List<EmailMessage> msgList = jagemail.getAllMessages();
            for(int i = 0; i < msgList.size(); i++)
            {
                message = message.concat(msgList.get(i).getContent());
            }
            
            //Getting html
            //
            
            
            stmt.setString(1, receivers);
        }catch(Exception e)
        {
            
        }
    }
}
