package com.williamngo.test;

import com.williamngo.JagEmail.ConfigBean;
import com.williamngo.JagEmail.JagEmail;
import com.williamngo.JagEmail.MailerImpl;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import jodd.mail.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This classes tests for cases that should throw illegal argument exception.
 * The most obvious case is when an email does not have any recipient.
 * @author William Ngo
 */
@RunWith(Parameterized.class)
public class TestSendEmail_iae_test {

    //private final Logger log = LoggerFactory.getLogger(getClass().getName());
    ConfigBean cfg;
    Optional<String> emailReceive;
    Optional<String> emailCC;
    Optional<String> emailBCC;
    String subject;
    String text;
    String html;
    String embedded;
    String attachment;

    public TestSendEmail_iae_test(
            ConfigBean cfg,
            Optional<String> emailReceive,
            Optional<String> emailCC,
            Optional<String> emailBCC,
            String subject,
            String text,
            String html,
            String embedded,
            String attachment) {
        this.cfg = cfg;
        this.emailReceive = emailReceive;
        this.emailCC = emailCC;
        this.emailBCC = emailBCC;
        this.subject = subject;
        this.text = text;
        this.html = html;
        this.embedded = embedded;
        this.attachment = attachment;
    }

    /*
    @BeforeClass
    public static void setUpClass() 
    {
    
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() 
    {
    
    }
    
    @After
    public void tearDown() 
    {
    
    }
     */
    
    /**
     * Contains data for testing. Note: all the embed fields are null because it
     * causes problems with the equals method when comparing attachments sizes.
     * Additionally, embedded does a similar job to attachments and 
     * the attachment comparison doesn't give any problems.
     * 
     */
    @Parameters
    public static Collection<Object[]> data() {
        //Messaged used in the body of the email
        String msg = "Here is some text";
        return Arrays.asList(new Object[][]{
            //No recipients, should throw exception
            {
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.ofNullable(null),
                Optional.ofNullable(null),
                Optional.ofNullable(null),
                "Subject Test - No recipients",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg"
            }
        }
        );
    }

    
    /**
     * This method tests cases where it should throw an illegal argument
     * exception which is when there are no recipients in TO, CC and BCC
     */
    @Test(expected=IllegalArgumentException.class)
    public void sendEmail_test_noRecipient()
    {
        MailerImpl m = new MailerImpl(cfg);
        JagEmail myEmail = m.sendEmail(Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null), subject, text, html, embedded, attachment);
    }
}
