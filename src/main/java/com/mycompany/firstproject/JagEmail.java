/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firstproject;

import java.util.*;
import jodd.mail.*;
import javax.mail.*;

/**
 *
 * @author William Ngo
 */
public class JagEmail extends Email{
    private String folder;
    private List<ReceivedEmail> attachedMessages;
    private Flags flags;
    private boolean typeFlag; //True = Sent || False = Received
    private int messageNumber;
    private Date receiveDate;
   
    public JagEmail() {
        super();
    }
    
    public List<ReceivedEmail> getAttachedMessages()
    {
        return attachedMessages;
    }
    
    public void setAttachedMessages(List<ReceivedEmail> attachedMessages)
    {
        this.attachedMessages = attachedMessages;
    }
    
    public Flags getFlags()
    {
        return this.flags;
    }
    
    public void setFlags(Flags flag)
    {
        this.flags = flag;
    }
    
    public boolean getTypeFlags()
    {
        return this.typeFlag;
    }
    
    public void setTypeFlags(boolean sentOrReceived)
    {
        this.typeFlag = sentOrReceived;
    }
    
    public int getMessageNumber()
    {
        return messageNumber;
    }
    
    public void setMessageNumber(int messageNumber)
    {
        this.messageNumber = messageNumber;
    }
    
    public Date getReceiveDate()
    {
        return this.receiveDate;
    }
    
    public void setReceiveDate(Date receiveDate)
    {
        this.receiveDate = receiveDate;
    }
    
    public String getFolder() {
        return folder;
    }
 
    public void setFolder(String folder) {
        this.folder = folder;
    }
    
    public ArrayList<EmailAttachment> getEmailAttachment()
    {
        return attachments;
    }
    
    public void setEmailAttachment(ArrayList<EmailAttachment> e)
    {
        this.attachments = e;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.folder);
        hash = 29 * hash + Objects.hashCode(this.attachedMessages);
        hash = 29 * hash + Objects.hashCode(this.flags);
        hash = 29 * hash + (this.typeFlag ? 1 : 0);
        hash = 29 * hash + this.messageNumber;
        hash = 29 * hash + Objects.hashCode(this.receiveDate);
        return hash;
    }
    
    //Checking for attachment, to, from, subject, text body
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        //JagEmail to be compared with
        final JagEmail other = (JagEmail) obj;
        
        //from
        if(!Objects.equals(this.from.getEmail(), other.from.getEmail()))
            return false;
        
        //to
        if(to != null)
        {
            for(int i = 0; i < to.length; i++)
            {
                if(!Objects.equals(this.to[i].getEmail(), other.to[i].getEmail()))
                return false;
            }
        }
        
        //cc
        if(cc != null)
        {
            for(int i = 0; i < cc.length; i++)
            {
                if(!Objects.equals(this.cc[i].getEmail(), other.cc[i].getEmail()))
                    return false;
            }
        }
        
        //subject
        if(!Objects.equals(this.subject, other.subject))
            return false;
        
        //Messages - checks content of messages
        if(messages != null)
        {
            for(int i = 0; i < messages.size(); i++)
            {
               if(!Objects.equals(this.messages.get(i).getContent(), other.messages.get(i).getContent()))
               return false; 
            }
        }
        
        
        //Compares attachment array - Checks for the name and sizes
        if(attachments != null)
        {
            if(!Objects.equals(this.attachments.size(), other.attachments.size()))
                    return false;
            
            for(int i = 0; i < attachments.size(); i++)
            {
                if(!Objects.equals(this.attachments.get(i).getName(), other.attachments.get(i).getName()))
                    return false;
                
                if(!Objects.equals(this.attachments.get(i).getSize(), other.attachments.get(i).getSize()))
                    return false;
            }

        //Checks actual attachment
            for(int i = 0; i < attachments.size(); i++)
            {
                EmailAttachment thisAttachment = this.attachments.get(i);
                EmailAttachment otherAttachment = other.attachments.get(i);
            
                if(!Objects.equals(thisAttachment.getName(), otherAttachment.getName()))
                    return false;
            
                if(!Objects.equals(thisAttachment.getSize(), otherAttachment.getSize()))
                    return false;
            }
        }
        
        
        
        return true;
    }
}
