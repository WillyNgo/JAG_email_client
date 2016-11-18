/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.business.JagEmail;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.interfaces.MailerImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import jodd.mail.EmailAddress;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.MailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class EditorController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private JagEmailDAO jagemailDAO;

    @FXML
    private BorderPane editorPane;

    @FXML
    private HTMLEditor editor;
    @FXML
    private TextField toTextField;
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextField ccTextField;
    @FXML
    private TextField bccTextField;
    @FXML
    private Button sendButton;
    @FXML
    private Button attachButton;
    
    //Path to the attachments
    private String attachPath;
    private String embedPath;

    private JagEmail email;
    private MailerImpl mailer;

    public EditorController() {
        super();
    }

    public JagEmail getEmail() {
        return this.email;
    }

    public void setEmail(JagEmail email) {
        this.email = email;
    }
    
    public void setMailer(MailerImpl mailer){
        this.mailer = mailer;
    }

    /**
     * Displays the content of the email that a user clicked on the table.
     *
     * @param email
     */
    public void displayEmailContent(JagEmail email) {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        sendButton.setVisible(false);
        attachButton.setVisible(false);
         */
    }

    /**
     * Displays content of email when user clicks on an email in the table
     */
    public void displayEmail() {
        toTextField.setText(email.getFrom().getEmail());
        ccTextField.setText(ccToString(email.getCc()));
        subjectTextField.setText(email.getSubject());
    }

    /**
     * Get Content messages
     *
     * @param msgList
     * @return
     */
    private String getMessageContent(List<EmailMessage> msgList) {
        String result = "";
        //return an empty string if there are no msgs
        if (msgList == null) {
            return result;
        }

        StringBuilder str = new StringBuilder("");

        //Creates a string containing all the contents
        for (EmailMessage em : msgList) {
            str.append(em.getContent());
        }

        result = str.toString();

        return result;
    }

    /**
     * Converts an array of Cc to a string. Each entry is seperated by a comma;
     *
     * @param ccList
     * @return
     */
    private String ccToString(MailAddress[] ccList) {
        String result = "";
        if (ccList == null) {
            return result;
        }

        StringBuilder str = new StringBuilder("");

        //Each email address is seperated by a comma
        for (MailAddress ma : ccList) {
            str.append(ma.getEmail() + ",");
        }

        result = str.toString();

        //Remove comma at the end of the list of Ccs
        if (!result.equals("")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    private void saveFile() {
        Stage stage = new Stage();

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select a directory to save");
        File selectedDirectory = chooser.showDialog(stage);

        if (selectedDirectory != null && selectedDirectory.exists()) {
            String path = selectedDirectory.getAbsolutePath();
            writeFile(path);
        }
    }

    private void writeFile(String path) {
        List<EmailAttachment> attachments = email.getAttachments();

        try {
            for (EmailAttachment ea : attachments) {
                try (FileOutputStream fos = new FileOutputStream(path + "\\" + ea.getName())) {
                    fos.write(ea.toByteArray());
                }
            }
        } catch (IOException ioe) {
            log.info("ERROR SAVING FILE:\n" + ioe.getMessage());
        }
    }
    
    @FXML
    public void sendMail(){
        String to = toTextField.getText();
        String subject = subjectTextField.getText();
        String cc = ccTextField.getText();
        String bcc = bccTextField.getText();
        
        String message = editor.getHtmlText();
        
        try{
            //Validate fields
            if(validateEmailFields(to, subject, cc, bcc)){
                mailer.sendEmail(to, Optional.of(cc), Optional.of(bcc), subject, message, "", embedPath, attachPath);
                log.info("SUCCESSFULLY SENT EMAIL");
            }
            else{
                log.info("ERROR, EMAIL NOT SENT");
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Returns true if all fields are properly inputted
     * @param to
     * @param subject
     * @param cc
     * @param bcc
     * @param message
     * @return 
     */
    private boolean validateEmailFields(String to, String subject, String cc, String bcc){
        boolean isValid = true;
        
        //Validate To field
        if(to.equals("")){
            isValid = false;
            throw new IllegalArgumentException("Please provide a recipient");
        }
        //Determines if the email inputted is a valid email address
        else{
            checkAddress(to);
        }
        
        //validate cc
        if(!cc.equals("")){
            if(checkAddress(cc)){
                isValid = false;
            }
        }
        
        if(!bcc.equals("")){
            if(checkAddress(bcc)){
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    /**
     * Returns true if the inputted addresses are valid.
     * @param myAddresses
     * @return 
     */
    private boolean checkAddress(String myAddresses){
        boolean isValid = true;
        String[] ccList = myAddresses.split(",");
            
        for(String CcAddress : ccList){
            EmailAddress addressToCheck = new EmailAddress(CcAddress);
            if(!addressToCheck.isValid()){
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    private void saveAttachmentFromEmail(){
        
    }
    
    /**
     * Enables fields to allow user to send.
     */
    public void sendEmailButtonClicked(){
        /*
        sendButton.setVisible(true);
        bccTextField.setEditable(true);
        attachButton.setVisible(true);
        */
    }
}
