package com.williamngo.interfaces;

import com.williamngo.configurations.ConfigBean;
import com.williamngo.business.JagEmail;
import com.williamngo.interfaces.Mailer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.Flags;
import javax.mail.internet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.mail.*;
/**
 * MailerImpl implements the Mailer interface. Contains methods for sending 
 * and receiving mails.
 * 
 * @author 1435707
 * @version 2016/04/10
 */
public class MailerImpl implements Mailer {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private ConfigBean configBean;
    private boolean containsEmbed = false;
    
    public MailerImpl(ConfigBean c)
    {
        this.configBean = c;
    }
    
    @Override
    public JagEmail sendEmail(String listOfTo, Optional<String> listOfCc, Optional<String> listOfBcc, String subject, String message,
            String html, String embedded, String attachment)
    {       
        //Assign info of sender
        String smtpServerName = configBean.getSmtpServer();
        String emailSend = configBean.getEmailAddress();
        String emailSendPwd = configBean.getEmailAddressPwd();
        int smtpPortNo = configBean.getSmtpPort();

        //Creates server
        SmtpServer<SmtpSslServer> mySmtpServer = SmtpSslServer
                .create(smtpServerName, smtpPortNo)
                .authenticateWith(emailSend, emailSendPwd);
        log.info("Created SmtpServer");     
        
        /********** Validate required information ***************/
        JagEmail email = new JagEmail();
        email.from(emailSend);
                
        String[] toArray; //Array containing list of To
        //If there are no recipients, throw exception
        if(listOfTo == null || listOfTo.equals(""))
            throw new IllegalArgumentException("Error in sendEmail - Must have at least one recipient");
        else
        {
            //Enhanced for loop in case we have many To recipients
            for(String mail : listOfTo.split(","))
            {
                email.to(mail);
            }
        }
            
        //Check if there are no subject, defaults to "(no subject)"
        if(subject == null || subject.length() == 0){
            log.info("Subject not found, defaulting to '(no subject)'");
            subject = "(no subject)";
        }
        email.subject(subject);
        
        //If message is null, default to empty string
        if(message == null){
            log.info("Message found as null, defaulting to empty string");
            message = "";
        }
        email.addText(message);
        
        log.info("Created JagEmail object");
        
        /************ Validate optional information *********/
        
        //Add html
        if(html == null)
            html = "";
        email.addHtml(html);
        
        //Checks for attachments
        if(embedded != null)
        {
            containsEmbed = true;
            log.info("embedded found, setting embedded in email");
            String[] embedStrArray = embedded.split(",");
            for(String e : embedStrArray)
                email.embed(EmailAttachment.attachment().bytes(new File(e)));
            
            //email.addHtml("<img src=cid:C:\\Users\\1435707\\Pictures\\koala.jpg />");
        }
        
        if(attachment != null)
        {
            log.info("attachment found, setting attachment in email");
            String[] attachStrArray = attachment.split(",");
            for(String a : attachStrArray)
                email.attach(EmailAttachment.attachment().file(a));
        }

        //Check to see if there is any CC, add if there is
        if(listOfCc.isPresent())
        {
            //If there are CCs present, creates a new EmailAddress[] containing those emails
            String emailCCstr = listOfCc.get();
            log.info("Recipient CC found");
            String[] emailCC = emailCCstr.split(",");
            email.cc(emailCC);
        }
        
        if(listOfBcc.isPresent())
        {
            String emailBCCstr = listOfBcc.get();
            log.info("Recipient BCC found");
            String[] emailBCC = emailBCCstr.split(",");
            email.bcc(emailBCC);
        }
        
        //Set folder to sent
        email.setFolder("sent");
        
        //Open session and send mail
        SendMailSession session = mySmtpServer.createSession();
        
        session.open();
        session.sendMail(email);
        
        //Solution for embedded attachment
        //if there is embedded, set it back
        if(containsEmbed == true)
        {
            log.info("embedded found, setting embedded back to email");
            String[] embedStrArray = embedded.split(",");
            for(String e : embedStrArray)
                email.embed(EmailAttachment.attachment().bytes(new File(e)));
        }
        
        session.close();
        return email;
    }
    
    
    /**
     * Creates an imap server in order to receive messages
     * 
     * @return JagEmails - an array of JagEmail that contains all JagEmails received
     */
    @Override
    public JagEmail[] receiveEmail()
    {
        //Receiver information
        String imapServerName = configBean.getImapServer();
        String emailReceive = configBean.getEmailAddress(); //Receive the emails sent by the same account
        String emailReceivePwd = configBean.getEmailAddressPwd();
        int imapPortNo = configBean.getImapPort();
        
        //Instantiate server and session
        ImapSslServer myImapServer = new ImapSslServer(imapServerName, imapPortNo, emailReceive, emailReceivePwd);
        ReceiveMailSession session = myImapServer.createSession();
        session.open();
        //myImapServer.setProperty("mail.debug", "true");
        
        ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(
                EmailFilter.filter().flag(Flags.Flag.SEEN, false));
        
        JagEmail[] jagEmails;
        
        //Convert ReceivedEmail to JagEmail
        if(emails != null)
        {
            jagEmails = convertBean(emails);
        }
        else
        {
            jagEmails = null;
        }
        
        session.close();
        return jagEmails;
    }
    
    /**
     * This method creates an array of JagEmail. It then sets every JagEmail's 
     * field in the array to the information of the receivedEmails. 
     * Then it returns the array.
     * 
     * @param r
     * @return myEmailArray
     */
    private JagEmail[] convertBean(ReceivedEmail[] r)
    {
        JagEmail[] myEmailArray = new JagEmail[r.length];
        //int e = myEmailArray.length;
        for(int i = 0; i < r.length; i++)
        {
            myEmailArray[i] = new JagEmail();
            myEmailArray[i].setTo(r[i].getTo());
            myEmailArray[i].setFrom(r[i].getFrom());
            myEmailArray[i].setBcc(r[i].getBcc());
            myEmailArray[i].setCc(r[i].getCc());
            myEmailArray[i].setAttachedMessages(r[i].getAttachedMessages());
            myEmailArray[i].setSubject(r[i].getSubject());
            myEmailArray[i].setFlags(r[i].getFlags());
            myEmailArray[i].setReceiveDate(r[i].getReceiveDate());
            myEmailArray[i].setFolder("Inbox");
            //Message number not used currently
            //myEmailArray[i].setMessageNumber(r[i].getMessageNumber());
            
            //Get messages
            if(r[i].getAttachments() != null)
                myEmailArray[i].setEmailAttachment(new ArrayList<>(r[i].getAttachments()));
            
            //Get content of messages
            List<EmailMessage> content = r[i].getAllMessages();
            for(int j = 0; j < content.size(); j++)
            {
                //Check if message is plain text or html, then set correct type
                if(content.get(j).getMimeType().equalsIgnoreCase("TEXT/PLAIN"))
                    myEmailArray[i].addText(content.get(j).getContent().replace("\n", "").replace("\r", ""));
                else if(content.get(j).getMimeType().equalsIgnoreCase("TEXT/HTML"))
                    myEmailArray[i].addText(content.get(j).getContent().replace("\n", "").replace("\r", ""));
            } 
        }
        
        log.info("successfully converted ReceivedEmail to JagEmail");
        return myEmailArray;
    }
}
