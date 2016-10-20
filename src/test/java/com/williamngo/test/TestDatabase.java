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
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 1435707
 */
public class TestDatabase {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String user = "root";
    private final String password = "dawson";

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
        MailerImpl m = new MailerImpl(cfg);
        JagEmail sendingEmail = m.sendEmail(emailAddress, emailCC, emailBCC, subject, text, html, embedded, attachment);

        jdb.deleteEmail(sendingEmail);

        String query = "SELECT COUNT(messageNumber) FROM emails;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                count++;
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be greater after sending email.
        assertTrue(count > 2);
    }

    public void addAccountToDatabase() {
        int count = 0;
       // Sql queries demo already has 2 emails in table by default
        jdb.addAccount(account_username, emailAddress, account_password);
        
        String query = "SELECT COUNT(account_id) FROM accounts;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                count++;
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be greater after sending email.
        assertTrue(count > 2);
    }

    public void updateAccountToDatabase() 
    {
        String myUsername = "";
        String myNewUsername = "";
        String query = "SELECT account_username FROM accounts WHERE account_id = ?;";
        int id = jdb.getAccountIdFromDatabase();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            
            try(ResultSet rs = stmt.executeQuery())
            {
                if(rs.next()) {
                myUsername = rs.getString("account_username");
            }
            } catch (SQLException sqle) {
            sqle.getMessage();
            }

            //Update username with new name
            jdb.updateAccount(id, "newUserName", emailAddress, account_password);
            
            try(ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                {
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
        // Sql queries demo already has 2 emails in table by default
        MailerImpl m = new MailerImpl(cfg);
        
        JagEmail sendingEmail = m.sendEmail(emailAddress, emailCC, emailBCC, subject, text, html, embedded, attachment);
        jdb.deleteEmail(sendingEmail);
        
        String query = "SELECT COUNT(messageNumber) FROM emails;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                count++;
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be the same after sending then deleting.
        assertEquals(count, 2);
    }

    public void deleteAccountFromDatabase() 
    {
        int count = 0;
        
        jdb.deleteAccount(emailAddress, account_password);
        
        String query = "SELECT COUNT(account_id) FROM accounts;";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                count++;
            }
        } catch (SQLException sqle) {
            sqle.getMessage();
        }

        //current number of emails should be greater after sending email.
        assertTrue(count < 2);
    }

    public void findEmailByFolder() {
        int count = 0;
        // Sql queries demo already has 2 emails in table by default
        List<JagEmail> found = jdb.retrieveEmail(foldername);
        
        count = found.size();
        
        assertEquals(count, 2);
    }

    public void findEmailByKeyword() {
        int count = 0;
        int alreadyIn = 4;
        List<JagEmail> found = jdb.searchEmail(keyword);
        
        count = found.size();
        
        assertEquals(count, 2);
    }

    public static Collection<Object[]> data() {
        //Messaged used in the body of the email
        String msg = "Here is some text";
        ConfigBean cfgbn;
        return Arrays.asList(new Object[][]{
            {
                cfgbn = new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - All values present",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg",
                "text",
                new JagEmailDAOImpl(cfgbn),
                "sender",
                "accountpassword",
                "sent"
            }
        }
        );
    }

    /**
     * ********************* Utility methods provided by Ken Fogel
     * ***************
     */
    /**
     * This routine recreates the database for every test. This makes sure that
     * a destructive test will not interfere with any other test.
     *
     * This routine is courtesy of Bartosz Majsak, an Arquillian developer at
     * JBoss who helped me out last winter with an issue with Arquillian. Look
     * up Arquillian to learn what it is.
     */
    @Before
    public void seedDatabase() {
        final String seedDataScript = loadAsString("createDatabase.sql");
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
                conn.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed seeding database", e);
        }
    }

    /**
     * The following methods support the seedDatabse method
     */
    private String loadAsString(final String path) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                Scanner scanner = new Scanner(inputStream)) {
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close input stream.", e);
        }
    }

    private List<String> splitStatements(Reader reader, String statementDelimiter) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final StringBuilder sqlStatement = new StringBuilder();
        final List<String> statements = new LinkedList<String>();
        try {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || isComment(line)) {
                    continue;
                }
                sqlStatement.append(line);
                if (line.endsWith(statementDelimiter)) {
                    statements.add(sqlStatement.toString());
                    sqlStatement.setLength(0);
                }
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed parsing sql", e);
        }
    }

    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
    }
}
