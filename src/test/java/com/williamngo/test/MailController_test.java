/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.test;

import com.mycompany.firstproject.ConfigBean;
import com.mycompany.firstproject.JagEmail;
import com.mycompany.firstproject.MailController;
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
 *
 * @author William Ngo
 */

@RunWith(Parameterized.class)
public class MailController_test {
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
    
    public MailController_test(
            ConfigBean cfg,
            Optional<String> emailReceive,
            Optional<String> emailCC,
            Optional<String> emailBCC,
            String subject,
            String text,
            String html,
            String embedded,
            String attachment) 
    {
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
    
    @Parameters
    public static Collection<Object[]> data() {
        //Messaged used in the body of the email
        String msg = "Here is some text";
        return Arrays.asList(new Object[][]{
           /*{
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - All values present",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                "C:\\Users\\1435707\\Pictures\\koala.jpg",
                "C:\\Users\\1435707\\Pictures\\koala.jpg"
           },
           //Testing for null in the to
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.ofNullable(null),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No TO recipient",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                "C:\\Users\\1435707\\Pictures\\koala.jpg",
                "C:\\Users\\1435707\\Pictures\\koala.jpg"
           },
           //Testing for null in the cc and bcc
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.ofNullable(null),
                Optional.ofNullable(null),
                "Subject Test - No CC or BCC recipient",
                "Here is some text",
                "<html><body><h1>" + msg + "</h1></body></html>",
                "C:\\Users\\1435707\\Pictures\\koala.jpg",
                "C:\\Users\\1435707\\Pictures\\koala.jpg"
           },
           //No subject
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                null,
                msg +  " - No subject",
                "<html><body><h1>" + msg + "</h1></body></html>",
                "C:\\Users\\1435707\\Pictures\\koala.jpg",
                "C:\\Users\\1435707\\Pictures\\koala.jpg"
           },
           //No text in body, has html
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No Text in body, does have HTML",
                null,
                "<html><body><h1>" + msg + "</h1></body></html>",
                "C:\\Users\\1435707\\Pictures\\koala.jpg",
                "C:\\Users\\1435707\\Pictures\\koala.jpg"
           },
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No Msg, No HTML
                null,
                null,
                "C:\\Users\\1435707\\Pictures\\koala.jpg",
                "C:\\Users\\1435707\\Pictures\\koala.jpg"
           },
           //No attachments
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No attachments",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                null
           }, 
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No embed, does have attachment",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                null,
                "C:\\Users\\1435707\\Pictures\\koala.jpg"
           },*/
           {
               new ConfigBean ("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                Optional.of("williamngoreceive@gmail.com"),
                Optional.of("shiftkun662@gmail.com"),
                Optional.of("devjlin1@gmail.com"),
                "Subject Test - No attach, does have embed",
                msg,
                "<html><body><h1>" + msg + "</h1></body></html>",
                "C:\\Users\\1435707\\Pictures\\koala.jpg",
                null
           }
        }
       );
    }
    
    
    @Test
    public void sendEmail_test()
    {
        MailController m = new MailController(cfg);
        
        boolean valid = false;
        JagEmail myEmail = m.sendEmail(emailReceive, emailCC, emailBCC, subject, text, html, embedded, attachment);
        JagEmail expectedEmail = createExpectedJagEmail();
        
        //assertTrue(myEmail.equals(expectedEmail));
    }
    
    /**
     * Creates a JagEmail object that contains the same information as the JagEmail
     * that is in the Collection<Object> Data.
     * 
     * @return expected - The JagEmail that is expected to be equal to
     */
    public JagEmail createExpectedJagEmail()
    {
        JagEmail expected = new JagEmail();
        
        String expectedSend = cfg.getEmailSend();
        String expectedReceive = emailReceive.get();
        String expectedCC = emailCC.get();
        String expectedBCC = emailBCC.get();
        String expectedSubject = subject;
        String expectedText = text;
        String expectedHtml = html;
        String expectedEmbedded = embedded;
        String expectedAttachment = attachment;
        
        expected.from(expectedSend)
                .to(expectedReceive)
                .cc(expectedCC)
                .bcc(expectedBCC)
                .subject(expectedSubject)
                .addText(expectedText)
                .addHtml(expectedHtml);
        
        if(embedded != null)
        {
            String[] embedStrArray = embedded.split(",");
            for(String e : embedStrArray)
                expected.embed(EmailAttachment.attachment().bytes(new File(e)));
        }
        
        if(attachment != null)
        {
            String[] attachStrArray = attachment.split(",");
            for(String a : attachStrArray)
                expected.attach(EmailAttachment.attachment().file(a));
        }
        
        return expected;
    }
}
