package com.williamngo.beans;

import javafx.beans.property.StringProperty;

/**
 *
 * @author William Ngo
 */
public class FolderBean {
    private StringProperty foldername;
    
    public FolderBean(){
        
    }
    
    public FolderBean(String name){
        foldername.set(name);
    }
    
    public String getFoldername(){
       return foldername.get();
    }
    
    public void setFoldername(String name){
        foldername.set(name);
    }
}
