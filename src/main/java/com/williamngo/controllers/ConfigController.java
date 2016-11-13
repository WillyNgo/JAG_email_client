package com.williamngo.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.williamngo.beans.ConfigBean;
import com.williamngo.database.JagEmailDAO;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class ConfigController{
    private ConfigBean cb;
    private JagEmailDAO jagemailDAO;
    @FXML
    private TextField usernameTextField;
    
    @FXML
    private TextField SMTPServerNameTextField;
    
    @FXML
    private TextField IMAPServerNameTextField;
    
    @FXML
    private TextField emailAddressTextField;
    
    @FXML
    private PasswordField emailPasswordField;
    
    @FXML
    private TextField SMTPPortNoTextField;
    
    @FXML
    private TextField IMAPPortNoTextField;
    
    @FXML
    private TextField databaseURLTextField;
    
    @FXML
    private TextField databaseUserNameTextField;
    
    @FXML
    private PasswordField databasePasswordField;
    
    /**
     * Default constructor
     */
    public ConfigController(){
        super();
        cb = new ConfigBean();
    }
    
    @FXML
    private void initialize(){
        Bindings.bindBidirectional(usernameTextField.textProperty(), cb.userNameProperty());
        Bindings.bindBidirectional(SMTPServerNameTextField.textProperty(), cb.smtpServerNameProperty());
        Bindings.bindBidirectional(IMAPServerNameTextField.textProperty(), cb.imapServerNameProperty());
        Bindings.bindBidirectional(emailAddressTextField.textProperty(), cb.emailAddressProperty());
        Bindings.bindBidirectional(emailPasswordField.textProperty(), cb.emailPasswordProperty());
        Bindings.bindBidirectional(SMTPPortNoTextField.textProperty() , cb.smtpPortNoProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(IMAPPortNoTextField.textProperty() , cb.imapPortNoProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(databaseURLTextField.textProperty(), cb.databaseURLProperty());
        Bindings.bindBidirectional(databaseUserNameTextField.textProperty(), cb.databaseUserNameProperty());
        Bindings.bindBidirectional(databasePasswordField.textProperty(), cb.databasePasswordProperty());
    }
    
    @FXML
    void submitForm(ActionEvent event) throws SQLException
    {
        if(validateForm())
        {
            //show error message
        }
    }
    
    @FXML
    public void clearForm()
    {
        
    }
    
    private boolean validateForm()
    {
        boolean isValid = true;
        //Validate email address for correct format: check if there's a @ sign
        String email = cb.getEmailAddress();
        if(email.indexOf("@") == -1)
        {
            Text t = new Text();
            t.setText("Please input a valid email address");
            t.setFill(Color.RED);
            emailAddressTextField.textProperty().set(t.toString());
            isValid = false;
        }
        
        return isValid;
    }
    
    public void setJagEmailDAO(JagEmailDAO jag)
    {
        this.jagemailDAO = jag;
    }
}
