package com.williamngo.beans;

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
    private String emailPassword; //sendanemail
    private int smtpPortNo;
    private int imapPortNo; 
    private String databaseURL;
    private String databaseUserName;
    private String databasePassword;
    
    public ConfigBean()
    {
        
    }
    
    public ConfigBean (String userName, String smtpServerName, String imapServerName,
            String emailAddress, String emailAddressPwd, int smtpPortNo, int imapPortNo, String databaseURL, String databaseUserName, String databasePassword)
    {
        this.userName = userName;
        this.smtpServerName = smtpServerName;
        this.imapServerName = imapServerName;
        this.emailAddress = emailAddress;
        this.emailPassword = emailAddressPwd;
        this.smtpPortNo = smtpPortNo;
        this.imapPortNo = imapPortNo;
        this.databaseURL = databaseURL;
        this.databaseUserName = databaseUserName;
        this.databasePassword = databasePassword;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public String getSmtpServerName()
    {
        return smtpServerName;
    }
    
    public String getImapServerName()
    {
        return imapServerName;
    }
    
    public String getEmailAddress() 
    {
        return emailAddress;
    }
    
    public String getEmailPassword() 
    {
        return emailPassword;
    }
    
    public int getSmtpPort()
    {
        return smtpPortNo;
    }
    
    public int getImapPort()
    {
        return imapPortNo;
    }
    
    public String getDatabaseURL()
    {
        return databaseURL;
    }
    
    public String getDatabaseUserName()
    {
        return databaseUserName;
    }
    
    public String getDatabasePassword()
    {
        return databasePassword;
    }
    
    public void setUserName(String username)
    {
        this.userName = username;
    }
    
    public void setSmtpServerName(String smtpServerName)
    {
        this.smtpServerName = smtpServerName;
    }
    
    public void setImapServerName(String imapServerName)
    {
        this.imapServerName = imapServerName;
    }
    
    public void setEmailAddress(String emailAddress) 
    {
        this.emailAddress = emailAddress;
    }
    
    public void setEmailPassword(String emailAddressPwd) 
    {
        this.emailPassword = emailAddressPwd;
    }
    
    public void setSmtpPort(int smtpPort) 
    {
            this.smtpPortNo = smtpPort;
    }
    
    public void setImapPort(int imapPort) 
    {
            this.imapPortNo = imapPort;
    }
    
    public void setDatabaseURL(String URL)
    {
        this.databaseURL = URL;
    }
    
    public void setDatabaseUserName(String name)
    {
        this.databaseUserName = name;
    }
    
    public void setDatabasePassword(String password)
    {
        this.databasePassword = password;
    }
}
