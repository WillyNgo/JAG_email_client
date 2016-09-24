/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firstproject;

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
 *
 * @author 1435707
 */
public class MailController implements IMailer {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private ConfigBean configBean;
    
    public MailController(ConfigBean c)
    {
        this.configBean = c;
    }
    
    @Override
    public JagEmail sendEmail(Optional<String> listOfTo, Optional<String> listOfCc, Optional<String> listOfBcc, String subject, String message,
            String html, String embedded, String attachment)
    {       
        //Assign info of sender
        String smtpServerName = configBean.getSmtpServer();
        String emailSend = configBean.getEmailSend();
        String emailSendPwd = configBean.getEmailSendPwd();
        int smtpPortNo = configBean.getSmtpPort();

        //Creates server
        SmtpServer<SmtpSslServer> mySmtpServer = SmtpSslServer
                .create(smtpServerName, smtpPortNo)
                .authenticateWith(emailSend, emailSendPwd);
        log.info("Created SmtpServer");
        
        //Debug set to true to view conversation
        mySmtpServer.debug(true);      
        
        /********** Validate required information ***************/
        
        //Check if there are no subject, defaults to "(no subject)"
        if(subject == null || subject.length() == 0){
            log.info("Subject not found, defaulting to '(no subject)'");
            subject = "(no subject)";
        }
        
        //If message is null, default to empty string
        if(message == null){
            log.info("Message found as null, defaulting to empty string");
            message = "";
        }
        //If message and html is not null, concatenate both - Not used
        //if(!(html.equals("") && message.equals("")))
        
        //Email object to be sent
        JagEmail email = new JagEmail();
        email.from(emailSend)
                .subject(subject)
                .addText(message);
        
        log.info("Created JagEmail object");
        
        /************ Validate optional information *********/
        
        //Add html
        if(html == null)
            html = "";
        email.addHtml(html);
        
        //Checks for attachments
        if(embedded != null)
        {
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
        
        //An email may have either a recipient in To, Cc and/or Bcc,
        //So I check if an email has at least one of these.
        
        //Check to see if there is a recipient in the To field
        if(listOfTo.isPresent())
        {
            String emailTostr = listOfTo.get();
            log.info("Recipient TO found");
            String[] emailTo = emailTostr.split(",");
            email.to(emailTo);
        }
        else{
            email.to("");
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
        else
        {
            email.cc("");
        }
        
        if(listOfBcc.isPresent())
        {
            String emailBCCstr = listOfBcc.get();
            log.info("Recipient BCC found");
            String[] emailBCC = emailBCCstr.split(",");
            email.bcc(emailBCC);
        }
        else
        {
            email.bcc("");
        }
        
        
        //If there are no recipients, throw exception
        if(!listOfTo.isPresent() && !listOfCc.isPresent() && !listOfBcc.isPresent())
            throw new IllegalArgumentException("Error in sendEmail - Must have at least one recipient");
        
       
        //Set folder to sent
        email.setFolder("sent");
        
        //Open session and send mail
        SendMailSession session = mySmtpServer.createSession();
        
        session.open();
        session.sendMail(email);
        session.close();
        
        return email;
    }
    
    
    /**
     * Creates an imap server in order to receive messages
     * 
     * @return 
     */
    @Override
    public JagEmail[] receiveEmail()
    {
        //Receiver information
        String imapServerName = configBean.getImapServer();
        String emailReceive = configBean.getEmailSend();
        String emailReceivePwd = configBean.getEmailSendPwd();
        int imapPortNo = configBean.getImapPort();
        
        //Instantiate server and session
        ImapSslServer myImapServer = new ImapSslServer(imapServerName, imapPortNo,emailReceive, emailReceivePwd);
        ReceiveMailSession session = myImapServer.createSession();
        session.open();
        myImapServer.setProperty("mail.debug", "true");
        
        ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(
                EmailFilter.filter().flag(Flags.Flag.SEEN, false));
        
        JagEmail[] jagEmails;
        
        if(emails != null)
        {
            jagEmails = convertBean(emails);
        }
        else
        {
            jagEmails = null;
        }
        
        session.close();
        //Convert ReceivedEmail to JagEmail
        return jagEmails;
    }
    
    public void setConfigBean (ConfigBean cb)
    {
        this.configBean = cb;
    }
    
    public ConfigBean getConfigBean()
    {
        return this.configBean;
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
        int e = myEmailArray.length;
        for(int i = 0; i < r.length; i++)
        {
            myEmailArray[i] = new JagEmail();
            myEmailArray[i].setBcc(r[i].getBcc());
            myEmailArray[i].setCc(r[i].getCc());
            myEmailArray[i].setAttachedMessages(r[i].getAttachedMessages());
            
            if(r[i].getAttachments() != null)
                myEmailArray[i].setEmailAttachment(new ArrayList<>(r[i].getAttachments()));
            myEmailArray[i].setFlags(r[i].getFlags());
            //Message number not used currently
            //myEmailArray[i].setMessageNumber(r[i].getMessageNumber());
            myEmailArray[i].setReceiveDate(r[i].getReceiveDate());
            myEmailArray[i].setFolder("Inbox");
        }
        
        log.info("successfully converted ReceivedEmail to JagEmail");
        return myEmailArray;
    }
}
