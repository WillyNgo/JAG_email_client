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
 * Email bean to display emails in the table of the user
 * 
 * @author William Ngo
 */
public class EmailBean {
    private StringProperty from;
    private StringProperty subject;
    private Date receiveDate;
    
    public EmailBean()
    {}
    
    public EmailBean(final String from, final String subject, final Date receiveDate)
    {
        this.from = new SimpleStringProperty(from);
        this.subject = new SimpleStringProperty(subject); 
        this.receiveDate = receiveDate;
    }
    
    //Getters
    public String getFrom()
    {
        return this.from.get();
    }
    
    public String getSubject()
    {
        return this.subject.get();
    }
    
    public Date getReceiveDate()
    {
        return this.receiveDate;
    }
    
    //Setters
    public void setFrom(String from)
    {
        this.from.set(from);
    }
    
    public void setSubject(String subject)
    {
        this.subject.set(subject);
    }
    
    public void setReceiveDate(Date date)
    {
        this.receiveDate = date;
    }
}
