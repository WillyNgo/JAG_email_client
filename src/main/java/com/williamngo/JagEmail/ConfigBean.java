package com.williamngo.JagEmail;

import org.slf4j.*;

/**
 * Contains information of SMPT, IMAP servers and email addresses
 * identification information
 * 
 * @author William Ngo
 * @version 2016/04/10
 */
public class ConfigBean {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private String userName;
    private String smtpServerName; //"smtp.gmail.com";
    private String imapServerName; //"imap.gmail.com";
    private String emailAddress; //williamngosend@gmail.com
    private String emailAddressPwd; //sendanemail
    private int smtpPortNo;
    private int imapPortNo; 
    
    public ConfigBean (String userName, String smtpServerName, String imapServerName,
            String emailAddress, String emailAddressPwd, int smtpPortNo, int imapPortNo)
    {
        this.userName = userName;
        this.smtpServerName = smtpServerName;
        this.imapServerName = imapServerName;
        this.emailAddress = emailAddress;
        this.emailAddressPwd = emailAddressPwd;
        this.smtpPortNo = smtpPortNo;
        this.imapPortNo = imapPortNo;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public String getSmtpServer()
    {
        return smtpServerName;
    }
    
    public String getImapServer()
    {
        return imapServerName;
    }
    
    public String getEmailAddress() 
    {
        return emailAddress;
    }
    
    public String getEmailAddressPwd() 
    {
        return emailAddressPwd;
    }
    
    public int getSmtpPort()
    {
        return smtpPortNo;
    }
    
    public int getImapPort()
    {
        return imapPortNo;
    }
    
    public void setUserName(String username)
    {
        this.userName = username;
    }
    
    public void setSmtpServer(String smtpServerName)
    {
        this.smtpServerName = smtpServerName;
    }
    
    public void setImapServer(String imapServerName)
    {
        this.imapServerName = imapServerName;
    }
    
    public void setEmailAddress(String emailAddress) 
    {
        this.emailAddress = emailAddress;
    }
    
    public void setEmailAddressPwd(String emailAddressPwd) 
    {
        this.emailAddressPwd = emailAddressPwd;
    }
    
    public void setSmtpPort(int smtpPort) 
    {
            this.smtpPortNo = smtpPort;
    }
    
    public void setImapPort(int imapPort) 
    {
            this.imapPortNo = imapPort;
    }
}
