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
 *
 * @author 1435707
 */
public interface JagEmailDAO {
    public void addAccountToDatabase(String username, String emailaddress, String password);
    public void addEmailToDatabase(JagEmail jagemail);
    public void addAttachmentToDatabase(JagEmail jagemail, ResultSet rs);
    public void deleteEmailFromDatabase(JagEmail jagemail, int messageNumber);
    public void deleteAccountFromDatabase(String emailAddress, String password);
    public List<JagEmail> searchEmail(String keyword);
}
