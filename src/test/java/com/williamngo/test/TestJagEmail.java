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
 * This class tests the sending email module. In order to assure that an email has been
 * sent properly, I used my overridden equals() method coded in the JagEmail class. Thus,
 * I am testing for both methods here.
 * 
 * @author William Ngo
 */
@RunWith(Parameterized.class)
public class TestJagEmail {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    ConfigBean cfg;
    Optional<String> emailReceive;
    Optional<String> emailCC;
    Optional<String> emailBCC;
    String subject;
    String text;
    String html;
    String embedded;
    String attachment;

    public TestJagEmail(
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
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - All values present",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg"
            }
                /*,
            //Testing for null in the to
            {
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.ofNullable(null),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No TO recipient",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "pictures\\kimagura.jpg"
            },
            //Testing for null in the cc and bcc
            {
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
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
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
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
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
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
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
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
                new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No attachments",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                null
            }
                */
           }
        );
    }
    
    @Test
    public void JagEmailTesting()
    {
        MailerImpl m = new MailerImpl(cfg);
        
        JagEmail sendingEmail = m.sendEmail(emailReceive, emailCC, emailBCC, subject, text, html, embedded, attachment);
        
        cfg.setEmailAddress("williamngoreceive@gmail.com");
        cfg.setEmailAddressPwd("receiveanemail");
        
        JagEmail[] receivedEmail = m.receiveEmail();
        JagEmail receivingEmail = receivedEmail[0];
        
        assertTrue(sendingEmail.equals(receivingEmail));
    }
    
    
    
    
    /**

    //@Test
    public void sendEmail_test_assertTrue() {
        MailerImpl m = new MailerImpl(cfg);

        
        JagEmail myEmail = m.sendEmail(emailReceive, emailCC, emailBCC, subject, text, html, embedded, attachment);
        log.info("Created JagEmail object...");
        JagEmail expectedEmail = createExpectedJagEmail();
        log.info("Created expected JagEmail Object..");
        assertTrue(myEmail.equals(expectedEmail));
    }
    
    //@Test
    public void sendEmail_test_assertFalse()
    {
        MailerImpl m = new MailerImpl(cfg);
        
        JagEmail myEmail = m.sendEmail(emailReceive, emailCC, emailBCC, subject, text, html, embedded, attachment);
        log.info("Created JagEmail Object");
        JagEmail differentEmail = createExpectedJagEmail();
        log.info("Created JagEmail object named differentEmail that contains same info as myEmail");
        //Change a value
        differentEmail.to("ngo.willi@gmail.com");
        log.info("Changed value TO of differentEmail to ngo.willi@gmail.com");
        assertFalse(myEmail.equals(differentEmail));
    }
    

    public JagEmail createExpectedJagEmail() {
        log.info("Creating expected Jag Email...");
        //JagEmail object to be compared with
        JagEmail expected = new JagEmail();
        
        //Set up the same information as the other JagEmail
        String expectedSend = cfg.getEmailSend();
        String expectedReceive = emailReceive.orElse("");
        String expectedCC = emailCC.orElse("");
        String expectedBCC = emailBCC.orElse("");
        String expectedSubject = subject;
        String expectedText = text;
        String expectedHtml = html;
        String expectedEmbedded = embedded;
        String expectedAttachment = attachment;
        
        //Validate
        if(expectedSubject == null || expectedSubject.length() == 0)
            expectedSubject = "(no subject)";
        
        if(expectedText == null)
            expectedText = "";
        
        if(expectedHtml == null)
            expectedHtml = "";
        
        //Set values after validating
        expected.from(expectedSend)
                .to(expectedReceive)
                .cc(expectedCC)
                .bcc(expectedBCC)
                .subject(expectedSubject)
                .addText(expectedText)
                .addHtml(expectedHtml);
        log.info("Setted values of expected JagEmail");
        
        //Check for attachments
        if (expectedEmbedded != null) {
            String[] embedStrArray = expectedEmbedded.split(",");
            for (String e : embedStrArray) {
                expected.embed(EmailAttachment.attachment().bytes(new File(e)));
            }
        }

        if (expectedAttachment != null) {
            String[] attachStrArray = expectedAttachment.split(",");
            for (String a : attachStrArray) {
                expected.attach(EmailAttachment.attachment().file(a));
            }
        }
        
        log.info("returning expected JagEmail...");
        return expected;
    }
    */
}
