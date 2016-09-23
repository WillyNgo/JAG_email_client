/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.test;

import com.mycompany.firstproject.ConfigBean;
import com.mycompany.firstproject.JagEmail;
import com.mycompany.firstproject.MailController;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.*;

/**
 *
 * @author 1435707
 */
@RunWith(Parameterized.class)
public class ReceiveEmail_test {

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

    public ReceiveEmail_test(
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

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        //Messaged used in the body of the email
        String msg = "Here is some text about receiving";
        return Arrays.asList(
                new Object[][]{
                    {
                        new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngosend@gmail.com", "sendanemail", 465, 993),
                        Optional.of("williamngoreceive@gmail.com"),
                        Optional.ofNullable(null),
                        Optional.ofNullable(null),
                        "Subject Test - Receive Test",
                        msg,
                        "<html><body><h1>" + msg + "</h1></body></html>",
                        null,
                        "C:\\Users\\1435707\\Pictures\\koala.jpg"
                    }
                }
        );
    }
    
    @Test
    public void receiveEmail_test_notNull()
    {
        MailController m = new MailController(cfg);
        JagEmail myEmail = m.sendEmail(emailReceive, emailCC, emailBCC, subject, text, html, embedded, attachment);
        
        // Add a five second pause to allow the Gmail server to receive what has
        // been sent
         try {
             Thread.sleep(5000);
         } catch (InterruptedException e) {
             log.error("Threaded sleep failed", e);
             System.exit(1);
         }
         
         //sets configBean to pass information of the receiving account
         m = new MailController(new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngoreceive@gmail.com", "receiveanemail", 465, 993));
         
        JagEmail[] receivedEmails = m.receiveEmail();
        assertNotNull(receivedEmails);
    }
    
    @Test
    public void receiveEmail_test_Null()
    {
        MailController m = new MailController(cfg);
        //JagEmail myEmail = m.sendEmail(emailReceive, emailCC, emailBCC, subject, text, html, embedded, attachment);
        
        // Add a five second pause to allow the Gmail server to receive what has
        // been sent
         try {
             Thread.sleep(5000);
         } catch (InterruptedException e) {
             log.error("Threaded sleep failed", e);
             System.exit(1);
         }
         
         //sets configBean to pass information of the receiving account
         m = new MailController(new ConfigBean("userName", "smtp.gmail.com", "imap.gmail.com", "williamngoreceive@gmail.com", "receiveanemail", 465, 993));
         
         JagEmail[] receivedNullEmails = m.receiveEmail();
         assertNull(receivedNullEmails);
    }
}
