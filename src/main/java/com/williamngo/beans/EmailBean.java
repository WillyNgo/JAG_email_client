/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.beans;

import java.util.Date;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.mail.Flags;
import jodd.mail.ReceivedEmail;

/**
 *
 * @author William Ngo
 */
public class EmailBean {
    private StringProperty from;
    private StringProperty subject;
    private StringProperty folder;
    private BooleanProperty typeFlag; //True = Sent || False = Received
    private IntegerProperty messageNumber;
    private Date receiveDate;
    
    public EmailBean()
    {}
    
    public EmailBean(final String from, final String subject, final String folder,
            final boolean typeFlag, final int messageNumber, final Date receiveDate)
    {
        this.from = new SimpleStringProperty(from);
        this.subject = new SimpleStringProperty(subject);
        this.folder = new SimpleStringProperty(folder);
        this.messageNumber = new SimpleIntegerProperty(messageNumber); 
        this.receiveDate = receiveDate;
    }
    
    public String getFrom()
    {
        return this.from.get();
    }
    
    public String getSubject()
    {
        return this.subject.get();
    }
    
    public String getFolder()
    {
        return this.folder.get();
    }
    
    public Boolean getTypeFlag()
    {
        return this.typeFlag.get();
    }
    
    public int getMessageNumber()
    {
        return this.messageNumber.get();
    }
}
