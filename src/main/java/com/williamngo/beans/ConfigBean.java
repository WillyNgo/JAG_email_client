package com.williamngo.beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    private StringProperty userName;
    private StringProperty smtpServerName; //"smtp.gmail.com";
    private StringProperty imapServerName; //"imap.gmail.com";
    private StringProperty emailAddress; //williamngosend@gmail.com
    private StringProperty emailPassword; //sendanemail
    private IntegerProperty smtpPortNo;
    private IntegerProperty imapPortNo; 
    private StringProperty databaseURL;
    private StringProperty databaseUserName;
    private StringProperty databasePassword;
    
    public ConfigBean()
    {
        this("", "", "", "", "", 465, 993, "", "", "");
    }
    
    public ConfigBean (String userName, final String smtpServerName, final String imapServerName,
            final String emailAddress, final String emailPassword, final int smtpPortNo, final int imapPortNo,
            final String databaseURL, final String databaseUserName, final String databasePassword)
    {
        this.userName = new SimpleStringProperty(userName);
        this.smtpServerName = new SimpleStringProperty(smtpServerName);
        this.imapServerName = new SimpleStringProperty(imapServerName);
        this.emailAddress = new SimpleStringProperty(emailAddress);
        this.emailPassword = new SimpleStringProperty(emailPassword);
        this.smtpPortNo = new SimpleIntegerProperty(smtpPortNo);
        this.imapPortNo = new SimpleIntegerProperty(imapPortNo);
        this.databaseURL = new SimpleStringProperty(databaseURL);
        this.databaseUserName = new SimpleStringProperty(databaseUserName);
        this.databasePassword = new SimpleStringProperty(databasePassword);
    }
    
    //Getters
    public String getUserName()
    {
        return userName.get();
    }
    
    public String getSmtpServerName()
    {
        return smtpServerName.get();
    }
    
    public String getImapServerName()
    {
        return imapServerName.get();
    }
    
    public String getEmailAddress() 
    {
        return emailAddress.get();
    }
    
    public String getEmailPassword() 
    {
        return emailPassword.get();
    }
    
    public int getSmtpPort()
    {
        return smtpPortNo.get();
    }
    
    public int getImapPort()
    {
        return imapPortNo.get();
    }
    
    public String getDatabaseURL()
    {
        return databaseURL.get();
    }
    
    public String getDatabaseUserName()
    {
        return databaseUserName.get();
    }
    
    public String getDatabasePassword()
    {
        return databasePassword.get();
    }
    
    //Setters
    public void setUserName(String username)
    {
        this.userName.set(username);
    }
    
    public void setSmtpServerName(String smtpServerName)
    {
        this.smtpServerName.set(smtpServerName);
    }
    
    public void setImapServerName(String imapServerName)
    {
        this.imapServerName.set(imapServerName);
    }
    
    public void setEmailAddress(String emailAddress) 
    {
        this.emailAddress.set(emailAddress);
    }
    
    public void setEmailPassword(String emailAddressPwd) 
    {
        this.emailPassword.set(emailAddressPwd);
    }
    
    public void setSmtpPort(int smtpPort) 
    {
            this.smtpPortNo.set(smtpPort);
    }
    
    public void setImapPort(int imapPort) 
    {
            this.imapPortNo.set(imapPort);
    }
    
    public void setDatabaseURL(String URL)
    {
        this.databaseURL.set(URL);
    }
    
    public void setDatabaseUserName(String name)
    {
        this.databaseUserName.set(name);
    }
    
    public void setDatabasePassword(String password)
    {
        this.databasePassword.set(password);
    }
    
    //Properties method
    public StringProperty userNameProperty()
    {
        return userName;
    }
    
    public StringProperty smtpServerNameProperty()
    {
        return smtpServerName;
    }
    
    public StringProperty imapServerNameProperty()
    {
        return imapServerName;
    }
    
    public StringProperty emailAddressProperty()
    {
        return emailAddress;
    }
    
    public StringProperty emailPasswordProperty()
    {
        return emailPassword;
    }
    
    public IntegerProperty smtpPortNoProperty()
    {
        return smtpPortNo;
    }
    public IntegerProperty imapPortNoProperty()
    {
        return imapPortNo;
    }
    public StringProperty databaseURLProperty()
    {
        return databaseURL;
    }
    public StringProperty databaseUserNameProperty()
    {
        return databaseUserName;
    }
    public StringProperty databasePasswordProperty()
    {
        return databasePassword;
    }
}
