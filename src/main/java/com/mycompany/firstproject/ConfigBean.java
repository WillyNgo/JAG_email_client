/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firstproject;

/**
 * Contains information of SMPT, IMAP servers and email addresses
 * identification information
 * @author William Ngo
 * Version 09/18/2016
 */
public class ConfigBean {
    private String userName;
    private String smtpServerName; //"smtp.gmail.com";
    private String imapServerName; //"imap.gmail.com";
    private String emailSend; //williamngosend@gmail.com
    private String emailSendPwd; //sendanemail
    private int smtpPortNo;
    private int imapPortNo; 
    
    public ConfigBean (String userName, String smtpServerName, String imapServerName,
            String emailSend, String emailSendPwd, int smtpPortNo, int imapPortNo)
    {
        this.userName = userName;
        this.smtpServerName = smtpServerName;
        this.imapServerName = imapServerName;
        this.emailSend = emailSend;
        this.emailSendPwd = emailSendPwd;
        this.smtpPortNo = smtpPortNo;
        this.imapPortNo = imapPortNo;
    }
    
    /**
     * Returns username
     * 
     * @return userName
     */
    public String getUserName()
    {
        return userName;
    }
    
    /**
     * Returns name of the SMTP server
     * 
     * @return smptServerName
     */
    public String getSmtpServer()
    {
        return smtpServerName;
    }
    
    /**
     * Returns name of the IMAP server
     * 
     * @return imapServerName
     */
    public String getImapServer()
    {
        return imapServerName;
    }
    
    /**
     * Returns email address that sends email
     * 
     * @return emailSend
     */
    public String getEmailSend() {
        return emailSend;
    }
    
    /**
     * Returns sending email address password
     * 
     * @return emailSendPwd
     */
    public String getEmailSendPwd() {
        return emailSendPwd;
    }
    
    /**
     * Return Smtp Port number
     * 
     * @return smtpPortNo
     */
    public int getSmtpPort()
    {
        return smtpPortNo;
    }
    
    /**
     * Return imap port number
     * 
     * @return imapPortNo
     */
    public int getImapPort()
    {
        return imapPortNo;
    }
    
    /**
     * Sets username
     * @param username 
     */
    public void setUserName(String username)
    {
        if(username == null || username.length() == 0)
            throw new IllegalArgumentException("Invalid - Error in setUserName");
        
        this.userName = username;
    }
    
    /**
     * 
     * @param smtpServerName 
     */
    public void setSmtpServer(String smtpServerName)
    {
        if(smtpServerName == null || smtpServerName.length() == 0)
            throw new IllegalArgumentException("Invalid - Error in setSmtpServer");
        
        this.smtpServerName = smtpServerName;
    }
    
    /**
     * 
     * @param imapServerName 
     */
    public void setImapServer(String imapServerName)
    {
        if(imapServerName == null || imapServerName.length() == 0)
            throw new IllegalArgumentException("Invalid - Error in setimapServer");
        
        this.imapServerName = imapServerName;
    }
    
    /**
     * 
     * @param emailSend 
     */
    public void setEmailSend(String emailSend) {
        if(emailSend == null || emailSend.length() == 0)
            throw new IllegalArgumentException("Invalid - Error in SetEmailSend");
        
        this.emailSend = emailSend;
    }
    
    /**
     * 
     * @param emailSendPwd 
     */
    public void setEmailSendPwd(String emailSendPwd) {
        if(emailSendPwd == null || emailSendPwd.length() == 0)
            throw new IllegalArgumentException("Invalid - Error in SetEmailSendPwd");
        
        this.emailSendPwd = emailSendPwd;
    }
    
    /**
     * 
     * @param smtpPort 
     */
    public void setSmtpPort(int smtpPort) {
        if(smtpPort == -1)
            this.smtpPortNo = 465; //SSL ecrypted SMTP port defaults to 465
        else
            this.smtpPortNo = smtpPort;
    }
    
    /**
     * 
     * @param imapPort 
     */
    public void setImapPort(int imapPort) {
        if(imapPort == -1)
            this.imapPortNo = 993;//SSL ecrypted IMAP port defaults to 993
        else
            this.imapPortNo = imapPort;
    }
}
