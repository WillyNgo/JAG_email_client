/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.business;

import javafx.beans.property.StringProperty;

/**
 * Folder objects
 * @author William Ngo
 */
public class Folder {
    private String folderName;
    
    public String getFolderName(){
        return folderName;
    }
    
    public void setFolderName(String name){
        this.folderName = name;
    }
}
