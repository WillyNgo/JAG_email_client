/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.business.JagEmail;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.interfaces.MailerImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class EditorController implements Initializable {
    
    private JagEmailDAO jagemailDAO;
    
    @FXML
    private BorderPane editorPane;
    
    @FXML
    private HTMLEditor editor;
    
    
    private JagEmail email;
    private MailerImpl mailer;
    
    
    public EditorController(){
        super();
    }
    
    public JagEmail getEmail(){
        return this.email;
    }
    
    public void setEmail(JagEmail email){
        this.email = email;
    }
    
    /**
     * Displays the content of the email that a user clicked on the table.
     * @param email 
     */
    public void displayEmailContent(JagEmail email){
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
