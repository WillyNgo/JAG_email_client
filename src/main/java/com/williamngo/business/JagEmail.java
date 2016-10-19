package com.williamngo.business;

import java.util.*;
import jodd.mail.*;
import javax.mail.*;

/**
 * JagEmail object
 * 
 * @author William Ngo
 * @version 2016/04/10
 */
public class JagEmail extends Email{
    private int user_id;
    private String folder;
    private List<ReceivedEmail> attachedMessages;
    private Flags flags;
    private boolean typeFlag; //True = Sent || False = Received
    private int messageNumber;
    private Date receiveDate;
   
    public JagEmail() {
        super();
    }
    
    public int getUserId()
    {
        return this.user_id;
    }
    
    public void setUserId(int user_id)
    {
        this.user_id = user_id;
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
            
            boolean present = false;
            //Checks actual attachment
            //Checks if other.attachments contains same attachments as those in this.attachment
            //Because when receiving an email, the attachments are not in the same order as the one sent
            //So instead of verifying each email by the same subscript, 2 forloop to check if it contains.
            for(int i = 0; i < this.attachments.size(); i++)
            {
                present = false;
                for(int j = 0; j < other.attachments.size(); j++)
                {
                    if(this.attachments.get(i).getName().contains(other.attachments.get(j).getName()))
                    {
                        present = true;
                        break;
                    }
                }
            }
            
            //If name of the attachment wasn't found, then return false;
            if(present == false)
            {
                return false;
            }
        }
        return true;
    }
}
