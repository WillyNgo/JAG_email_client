package com.williamngo.test;

import com.williamngo.beans.ConfigBean;
import com.williamngo.business.JagEmail;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.database.JagEmailDAOImpl;
import com.williamngo.interfaces.MailerImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jodd.mail.EmailAttachment;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 1435707
 */

@RunWith(Parameterized.class)
public class TestDatabase {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    //private final String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
    //private final String user = "CS1435707";
    //private final String password = "tripermu";

    ConfigBean cb;
    String emailToBeReceived;
    Optional<String> emailCC;
    Optional<String> emailBCC;
    String subject;
    String text;
    String html;
    String embedded;
    String attachment;
    String searchKeyword;
    JagEmailDAOImpl jdb;
    String foldername;
    int msgNumber;

    public TestDatabase(
            ConfigBean cfg,
            String emailToBeReceived,
            Optional<String> emailCC,
            Optional<String> emailBCC,
            String subject,
            String text,
            String html,
            String embedded,
            String attachment,
            String keyword,
            JagEmailDAOImpl jdb,
            String foldername,
            int emailId) {
        this.cb = cfg;
        this.emailToBeReceived = emailToBeReceived;
        this.emailCC = emailCC;
        this.emailBCC = emailBCC;
        this.subject = subject;
        this.text = text;
        this.html = html;
        this.embedded = embedded;
        this.attachment = attachment;
        this.searchKeyword = keyword;
        this.jdb = jdb;
        this.foldername = foldername;
        this.msgNumber = emailId;
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addEmailToDatabase() {
        log.info("adding email to database...");
        int count = 0;
        // Creating email to be added to database
        JagEmail email = new JagEmail();
        //Setup the email
        email.from(cb.getEmailAddress());
        email.to(emailToBeReceived);
        email.cc(emailCC.get().split(","));
        email.bcc(emailBCC.get().split(","));
        email.subject(subject);
        email.addText(text);
        email.addHtml(html);
        for (String a : attachment.split(",")) {
            email.attach(EmailAttachment.attachment().file(a));
        }

        jdb.addEmail(email);

        //Demo database already has 4 emails stored. after adding, database should contain 5
        String query = "SELECT COUNT(messageNumber) as number FROM emails;";
        try (Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                count = rs.getInt("number");
            }
        } catch (SQLException sqle) {
            log.error("Error occured during AddEmail: email was not added");
            sqle.getMessage();
        }

        //current number of emails should be greater after sending email.
        assertEquals(5, count);
    }
    

    @Test
    public void deleteEmailFromDatabase() {
        log.info("deleting email from database...");
        int count = 0;
        
        //Deletes the email with associated id
        jdb.deleteEmail(msgNumber);
        
        String query = "SELECT COUNT(messageNumber) as number FROM emails;";
        try (Connection conn = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("number");
            }
        } catch (SQLException sqle) {
            log.error("Could not find specfied email: Email not deleted");
        }

        //current number of emails should be less after deleting which is from 4 to 3.
        assertEquals(3, count);
    }

    @Test
    public void findEmailByKeyword() {
        int count = 0;
        
        String[] k = searchKeyword.split(",");
        String keyword = k[0];
        int expectedCount = Integer.parseInt(k[1]);
        
        List<JagEmail> found = jdb.searchEmail(keyword);

        count = found.size();
        
        //There is a total of 4 emails in the database demo
        assertEquals(expectedCount, count);
    }
    
    @Test
    public void findEmailByFoldername()
    {
        int count = 0;
        
        String[] f = foldername.split(",");
        String myFoldername = f[0];
        int expectedCount = Integer.parseInt(f[1]);
        
        List<JagEmail> found = jdb.retrieveEmail(myFoldername);
        
        count = found.size();
        
        assertEquals(expectedCount, count);
    }

    @Parameters
    public static Collection<Object[]> data() {
        //Messaged used in the body of the email
        String msg = "Here is some text";
        ConfigBean cb;
        JagEmailDAOImpl jag;
        return Arrays.asList(new Object[][]{
            {
                cb = new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993, "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707", "CS1435707", "tripermu"),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - All values present",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg",           //Attachment
                "chicken,2",                        //SearchKeyword, ExpectedCount
                jag = new JagEmailDAOImpl(cb),   //JagEmailDao implementer
                "sent,2",                           //foldername, expectedValue
                1,                                  //MessageNumber
            },
            {
                cb = new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993, "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707", "CS1435707", "tripermu"),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - All values present",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg",           //Attachment
                "Pocket,0",                        //SearchKeyword, ExpectedCount
                jag = new JagEmailDAOImpl(cb),   //JagEmailDao implementer
                "NoFolder,0",                           //foldername, expectedValue
                1,                                  //MessageNumber  
            }
        }
        );
    }

    /**
     * Method to create database and teardown before each test
     */
    @Before
    public void createDatabase() {
        try (Connection con = DriverManager.getConnection(cb.getDatabaseURL(), cb.getDatabaseUserName(), cb.getDatabasePassword())) {
            PreparedStatement stmt;
            
            List<String> queryDrop = new ArrayList<String>();
            queryDrop.add("DROP TABLE IF EXISTS attachments;");
            queryDrop.add("DROP TABLE IF EXISTS emails;");
            //queryDrop.add("DROP TABLE IF EXISTS accounts;");
            
            for(String str : queryDrop)
            {
                stmt = con.prepareStatement(str);
                stmt.executeUpdate();
                log.info("dropped table with query: " + str);
            }

            String emailQuery = "create table emails (\n"
                    + "messageNumber INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n"
                    + "receiver VARCHAR(255) NOT NULL,\n"
                    + "sender VARCHAR(255) NOT NULL,\n"
                    + "cc varchar(255),\n"
                    + "subject_text VARCHAR(255),\n"
                    + "message VARCHAR(255),\n"
                    + "html VARCHAR(255),\n"
                    + "typeFlag boolean,\n"
                    + "receive_date date,\n"
                    + "folder VARCHAR(255)\n"
                    + ")ENGINE=InnoDB;";

            String attachmentQuery = "CREATE TABLE attachments(\n"
                    + "attachment_id int NOT NULL AUTO_INCREMENT,\n"
                    + "attach_messageNumber int,\n"
                    + "attachmentName varchar(255),\n"
                    + "attachmentByte mediumblob,\n"
                    + "PRIMARY KEY (attachment_id),\n"
                    + "CONSTRAINT fk_messagenumber FOREIGN KEY (attach_messageNumber) REFERENCES emails(messageNumber) ON DELETE CASCADE\n"
                    + ")ENGINE=InnoDB;";
            
            stmt = con.prepareStatement(emailQuery);
            stmt.executeUpdate();
            
            stmt = con.prepareStatement(attachmentQuery);
            stmt.executeUpdate();
            
            List<String> demoEmailsQuery = new ArrayList<String>();
            demoEmailsQuery.add("INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', true, NOW(), 'sent');");
            demoEmailsQuery.add("INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', false, NOW(), 'inbox');");
            demoEmailsQuery.add("INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', true, NOW(), 'sent');");
            demoEmailsQuery.add("INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', false, NOW(), 'inbox');");
            for(String str : demoEmailsQuery)
            {
                stmt = con.prepareStatement(str);
                stmt.executeUpdate();
                log.info("Inserted emails to database");
            }
            
            log.info("Database created!");
            
        } catch (SQLException sqle) {
            sqle.getMessage();
        }
    }
}
