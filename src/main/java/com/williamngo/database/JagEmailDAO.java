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
    public void addAccount(String username, String emailaddress, String password);
    public void addEmail(JagEmail jagemail);
    public void addAttachment(JagEmail jagemail, ResultSet rs);
    public void updateAccount(int account_id, String username, String emailaddress, String password);
    public void deleteEmail(JagEmail jagemail, int messageNumber);
    public void deleteAccount(String emailAddress, String password);
    public List<JagEmail> retrieveEmail(String folder);
    public List<JagEmail> searchEmail(String keyword);
}
