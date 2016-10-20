/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Ignore
@RunWith(Parameterized.class)
public class TestDatabase {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private final String url = "jdbc:mysql://waldo2.dawsoncollege.qc.ca:3306/cs1435707";
    private final String user = "CS1435707";
    private final String password = "tripermu";

    ConfigBean cfg;
    String emailAddress;
    Optional<String> emailCC;
    Optional<String> emailBCC;
    String subject;
    String text;
    String html;
    String embedded;
    String attachment;
    String keyword;
    JagEmailDAOImpl jdb;
    String account_username;
    String account_password;
    String foldername;

    public TestDatabase(
            ConfigBean cfg,
            String emailAddress,
            Optional<String> emailCC,
            Optional<String> emailBCC,
            String subject,
            String text,
            String html,
            String embedded,
            String attachment,
            String keyword,
            JagEmailDAOImpl jdb,
            String account_username,
            String account_password,
            String foldername) {
        this.cfg = cfg;
        this.emailAddress = emailAddress;
        this.emailCC = emailCC;
        this.emailBCC = emailBCC;
        this.subject = subject;
        this.text = text;
        this.html = html;
        this.embedded = embedded;
        this.attachment = attachment;
        this.keyword = keyword;
        this.jdb = jdb;
        this.account_username = account_username;
        this.account_password = account_password;
        this.foldername = foldername;
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

    /**
     *
     */
    @Test
    public void addEmailToDatabase() {
        int count = 0;
        // Sql queries demo already has 2 emails in table by default
        JagEmail email = new JagEmail();
        //Setup the email
        email.setUserId(jdb.getAccountIdFromDatabase());
        email.from(cfg.getEmailAddress());
        email.to(emailAddress);
        email.cc(emailCC.get().split(","));
        email.bcc(emailBCC.get().split(","));
        email.subject(subject);
        email.addText(text);
        email.addHtml(html);
        for (String a : attachment.split(",")) {
            email.attach(EmailAttachment.attachment().file(a));
        }

        jdb.addEmail(email);

        //Demo database already has 2 emails stored. after adding, database should contain 3
        String query = "SELECT COUNT(messageNumber) as number FROM emails;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                count = rs.getInt("number");
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be greater after sending email.
        assertTrue(count > 4);
    }

    @Test
    public void addAccountToDatabase() {
        int count = 0;
        // Sql queries demo already has 2 emails in table by default
        jdb.addAccount(account_username, emailAddress, account_password);

        String query = "SELECT COUNT(account_id) as number FROM accounts;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("number");
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be greater after sending email.
        assertTrue(count > 2);
    }

    @Test
    public void updateAccountToDatabase() {
        String myUsername = "";
        String myNewUsername = "";
        String query = "SELECT account_username FROM accounts WHERE account_id = ?;";
        int id = jdb.getAccountIdFromDatabase();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    myUsername = rs.getString("account_username");
                }
            } catch (SQLException sqle) {
                sqle.getMessage();
            }

            //Update username with new name
            jdb.updateAccount(id, "newUserName", emailAddress, account_password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    myNewUsername = rs.getString("account_username");
                }
            } catch (SQLException sqle) {
                sqle.getMessage();
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        assertNotEquals(myUsername, myNewUsername);
    }

    @Test
    public void deleteEmailFromDatabase() {
        int count = 0;
        // Sql queries demo already has 4 emails in table by default
        
        jdb.deleteEmail(1);
        
        String query = "SELECT COUNT(messageNumber) as number FROM emails;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("number");
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be less after deleting.
        assertTrue(count < 4);
    }

    @Test
    public void deleteAccountFromDatabase() {
        int count = 0;

        jdb.deleteAccount(jdb.getAccountIdFromDatabase(), emailAddress, account_password);

        String query = "SELECT COUNT(account_id) as number FROM accounts;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                count = rs.getInt("number");
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be greater after sending email.
        assertTrue(count < 2);
    }

    //@Test
    public void findEmailByFolder() {
        int count = 0;
        // Sql queries demo already has 2 emails in table by default
        List<JagEmail> found = jdb.retrieveEmail(foldername);

        count = found.size();

        assertEquals(count, 2);
    }

    //@Test
    public void findEmailByKeyword() {
        int count = 0;
        int alreadyIn = 4;
        List<JagEmail> found = jdb.searchEmail(keyword);

        count = found.size();

        assertEquals(2, count);
    }

    @Parameters
    public static Collection<Object[]> data() {
        //Messaged used in the body of the email
        String msg = "Here is some text";
        ConfigBean cfgbn;
        return Arrays.asList(new Object[][]{
            {
                cfgbn = new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngosend@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - All values present",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg",
                "text",
                new JagEmailDAOImpl(cfgbn),
                "receiver",
                "sendanemail",
                "sent"
            }
        }
        );
    }

    /**
     * Method to create database and teardown before anything
     */
    @Before
    public void createDatabase() {
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt;
            
            List<String> queryDrop = new ArrayList<String>();
            queryDrop.add("DROP TABLE IF EXISTS attachments;");
            queryDrop.add("DROP TABLE IF EXISTS emails;");
            queryDrop.add("DROP TABLE IF EXISTS accounts;");
            
            for(String str : queryDrop)
            {
                stmt = con.prepareStatement(str);
                stmt.executeUpdate();
                log.info("dropped database");
            }
            
            String accountQuery = "CREATE TABLE accounts (\n"
                    + "account_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n"
                    + "account_username VARCHAR(255)NOT NULL,\n"
                    + "emailAddress VARCHAR(255) NOT NULL,\n"
                    + "account_password VARCHAR(255) NOT NULL\n"
                    + ")ENGINE=InnoDB;";
            String emailQuery = "create table emails (\n"
                    + "messageNumber INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n"
                    + "email_account_id INT,\n"
                    + "receiver VARCHAR(255) NOT NULL,\n"
                    + "sender VARCHAR(255) NOT NULL,\n"
                    + "cc varchar(255),\n"
                    + "subject_text VARCHAR(255),\n"
                    + "message VARCHAR(255),\n"
                    + "html VARCHAR(255),\n"
                    + "typeFlag boolean,\n"
                    + "receive_date date,\n"
                    + "folder VARCHAR(255),\n"
                    + "CONSTRAINT fk_account_id FOREIGN KEY (email_account_id) REFERENCES accounts(account_id)\n"
                    + ")ENGINE=InnoDB;";

            String attachmentQuery = "CREATE TABLE attachments(\n"
                    + "attachment_id int NOT NULL AUTO_INCREMENT,\n"
                    + "attach_messageNumber int,\n"
                    + "attachmentName varchar(255),\n"
                    + "attachmentByte mediumblob,\n"
                    + "PRIMARY KEY (attachment_id),\n"
                    + "CONSTRAINT fk_messagenumber FOREIGN KEY (attach_messageNumber) REFERENCES emails(messageNumber)\n"
                    + ")ENGINE=InnoDB;";
            
            stmt = con.prepareStatement(accountQuery);
            stmt.executeUpdate();
            
            stmt = con.prepareStatement(emailQuery);
            stmt.executeUpdate();
            
            stmt = con.prepareStatement(attachmentQuery);
            stmt.executeUpdate();
            
            List<String> demoAccountsQuery = new ArrayList<String>();
            demoAccountsQuery.add("INSERT INTO accounts (account_username, emailAddress, account_password) VALUES ('sender', 'williamngosend@gmail.com', 'sendanemail');\n");
            demoAccountsQuery.add("INSERT INTO accounts (account_username, emailAddress, account_password) VALUES ('receiver', 'williamngoreceive@gmail.com', 'receiveanemail');");
            for(String str : demoAccountsQuery)
            {
                stmt = con.prepareStatement(str);
                stmt.executeUpdate();
                log.info("Inserted accounts to database");
            }
            
            List<String> demoEmailsQuery = new ArrayList<String>();
            demoEmailsQuery.add("INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (1, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', true, NOW(), 'sent');");
            demoEmailsQuery.add("INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (2, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', false, NOW(), 'inbox');");
            demoEmailsQuery.add("INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (1, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', true, NOW(), 'sent');");
            demoEmailsQuery.add("INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (2, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', false, NOW(), 'inbox');");
            for(String str : demoEmailsQuery)
            {
                stmt = con.prepareStatement(str);
                stmt.executeUpdate();
                log.info("Inserted emails to databasE");
            }
            
            log.info("added Datanase");
            
        } catch (SQLException sqle) {
            sqle.getMessage();
        }
    }

    /**
     * ********************* Utility methods provided by Ken Fogel
     * ***************
     *
     * /**
     * This routine recreates the database for every test. This makes sure that
     * a destructive test will not interfere with any other test.
     *
     * This routine is courtesy of Bartosz Majsak, an Arquillian developer at
     * JBoss who helped me out last winter with an issue with Arquillian. Look
     * up Arquillian to learn what it is.
     *
     * @Before public void seedDatabase() { final String seedDataScript =
     * loadAsString("createDatabase.sql"); try (Connection conn =
     * DriverManager.getConnection(url, user, password)) { for (String statement
     * : splitStatements(new StringReader(seedDataScript), ";")) {
     * conn.prepareStatement(statement).execute(); } } catch (SQLException e) {
     * throw new RuntimeException("Failed seeding database", e); } }
     *
     * /**
     * The following methods support the seedDatabse method
     *
     * private String loadAsString(final String path) { try (InputStream
     * inputStream =
     * Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
     * Scanner scanner = new Scanner(inputStream)) { return
     * scanner.useDelimiter("\\A").next(); } catch (IOException e) { throw new
     * RuntimeException("Unable to close input stream.", e); } }
     *
     * private List<String> splitStatements(Reader reader, String
     * statementDelimiter) { final BufferedReader bufferedReader = new
     * BufferedReader(reader); final StringBuilder sqlStatement = new
     * StringBuilder(); final List<String> statements = new
     * LinkedList<String>(); try { String line = ""; while ((line =
     * bufferedReader.readLine()) != null) { line = line.trim(); if
     * (line.isEmpty() || isComment(line)) { continue; }
     * sqlStatement.append(line); if (line.endsWith(statementDelimiter)) {
     * statements.add(sqlStatement.toString()); sqlStatement.setLength(0); } }
     * return statements; } catch (IOException e) { throw new
     * RuntimeException("Failed parsing sql", e); } }
     *
     * private boolean isComment(final String line) { return
     * line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
     * }
     */
}
