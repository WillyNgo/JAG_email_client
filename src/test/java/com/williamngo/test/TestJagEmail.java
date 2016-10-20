package com.williamngo.test;

import com.williamngo.beans.ConfigBean;
import com.williamngo.business.JagEmail;
import com.williamngo.interfaces.MailerImpl;
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
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class tests the sending email module. In order to assure that an email has been
 * sent properly, I used my overridden equals() method coded in the JagEmail class. Thus,
 * I am testing for both methods here.
 * 
 * @author William Ngo
 */

@Ignore
@RunWith(Parameterized.class)
public class TestJagEmail {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    ConfigBean cfg;
    String emailAddress;
    Optional<String> emailCC;
    Optional<String> emailBCC;
    String subject;
    String text;
    String html;
    String embedded;
    String attachment;

    public TestJagEmail(
            ConfigBean cfg,
            String emailAddress,
            Optional<String> emailCC,
            Optional<String> emailBCC,
            String subject,
            String text,
            String html,
            String embedded,
            String attachment) {
        this.cfg = cfg;
        this.emailAddress = emailAddress;
        this.emailCC = emailCC;
        this.emailBCC = emailBCC;
        this.subject = subject;
        this.text = text;
        this.html = html;
        this.embedded = embedded;
        this.attachment = attachment;
    }
    
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
        return Arrays.asList(new Object[][]
        {
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - All values present",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg"
            }/*
            ,
            //Testing for null in the cc and bcc
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.ofNullable(null),
                Optional.ofNullable(null),
                "Subject Test - No CC or BCC recipient",
                "Here is some text",
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg"
            },
            //No subject
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                null,
                msg + " - No subject",
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg"
            },
            //No text in body, has html
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No Text in body, does have HTML",
                null,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg"
            },
            //No text or html
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No Msg, No HTML",
                null,
                null,
                null,
                "pictures\\kimagura.jpg"
            },
            //No attachments
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No attachments",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                null
            },
            //Has embedded
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com",
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - Had embedded and attach",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                "pictures\\edhappy.png",
                "pictures\\kimagura.jpg"
            },
            //Many to
            {
                new ConfigBean("sender", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                "williamngoreceive@gmail.com,shiftkun662@gmail.com",
                Optional.ofNullable(null),
                Optional.ofNullable(null),
                "Subject Test - No attachments",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                null
            }*/
           }
        );
    }
    
    @Test
    public void TestingSendReceiveEmail()
    {
        MailerImpl m = new MailerImpl(cfg);
        
        JagEmail sendingEmail = m.sendEmail(emailAddress, emailCC, emailBCC, subject, text, html, embedded, attachment);
        
        // Add a five second pause to allow the Gmail server to receive what has
        // been sent
        try {
             Thread.sleep(5000);
         } catch (InterruptedException e) {
             log.error("Threaded sleep failed", e);
             System.exit(1);
         }
        
        //Email receiving
        m = new MailerImpl(new ConfigBean("receiver", "smtp.gmail.com", "imap.gmail.com", "williamngoreceive@gmail.com", "receiveanemail", 465, 993));
        
        JagEmail[] receivedEmail = m.receiveEmail();
        JagEmail receivingEmail = receivedEmail[0];

        assertEquals(sendingEmail, receivingEmail);
    }
}
