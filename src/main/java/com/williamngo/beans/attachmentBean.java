/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.beans;

/**
 *
 * @author 1435707
 */
public class attachmentBean {
    private int attach_id;
    private String filename;
    private byte[] attachment;
    
    public attachmentBean(int attach_id, String filename, byte[] attachment)
    {
        this.attach_id = attach_id;
        this.filename = filename;
        this.attachment = attachment;
    }
    
    public int getAttachId()
    {
        return this.attach_id;
    }
    
    public String getFileName()
    {
        return this.filename;
    }
    
    public byte[] getAttachmentByteArray()
    {
        return this.attachment;
    }
    
    public void setFileName(String filename)
    {
        this.filename = filename;
    }
    
    public void setAttachmentByteArray(byte[] attachment)
    {
        this.attachment = attachment;
    }
}
