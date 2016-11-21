/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.database;
import com.williamngo.business.JagEmail;
import java.sql.ResultSet;
import java.util.List;

/**
 * Interface for database utilities
 * @author 1435707
 */
public interface JagEmailDAO {
    public void addEmail(JagEmail jagemail);
    public void deleteEmail(int messageNumber);
    public void moveEmail(int messageNumber, String foldername);
    public List<JagEmail> retrieveEmail(String folder);
    public List<JagEmail> searchEmail(String keyword);
    public void addFolder(String foldername);
    public void deleteFolder(String foldername);
    public List<String> getAllFolders();
}
