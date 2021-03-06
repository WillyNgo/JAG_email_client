/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.business.JagEmail;
import static com.williamngo.business.utility.displayAlert;
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
import javafx.stage.FileChooser;
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
    private RootController rootControl;

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
    @FXML
    private Button saveAttachButton;
    
    //Path to the attachments
    private String attachPath;
    private String embedPath;

    private JagEmail email;
    private MailerImpl mailer;

    public EditorController() {
        super();
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableEditorButtons();
    }
    
    /********* GETTERS AND SETTERS **********/
    public JagEmail getEmail() {
        return this.email;
    }
    public void setEmail(JagEmail email) {
        this.email = email;
    }
    public void setMailer(MailerImpl mailer){
        this.mailer = mailer;
    }
    public void setRootController(RootController rootControl){
        this.rootControl = rootControl;
    }

    /**
     * Displays the sender, the CC if any, the subject and the message
     * content of the email that a user clicked on the table.
     *
     * @param email
     */
    public void displayEmailContent(JagEmail email) {
        toTextField.setText(email.getFrom().getEmail());
        subjectTextField.setText(email.getSubject());
        ccTextField.setText(ccToString(email.getCc()));
        
        //Show messages
        editor.setHtmlText(getMessageContent(email.getAllMessages()));
        
        //TODO: Look around attachment
    }
    
    /**
     * Enables the send and attach button in the editor
     */
    public void enableEditorButtons(){
        sendButton.setDisable(false);
        attachButton.setDisable(false);
    }
    
    /**
     * Disables the send and attach buttons in the editor
     */
    public void disableEditorButtons(){
        sendButton.setDisable(true);
        attachButton.setDisable(true);
    }
    
    /**
     * Clear input fields for user to enter new information for a new message
     */
    public void clearInputFields(){
        toTextField.textProperty().set("");
        subjectTextField.textProperty().set("");
        ccTextField.textProperty().set("");
        bccTextField.textProperty().set("");
        editor.setHtmlText("");
    }
    
    public void checkForAttachment(){
        if(email.getAttachments() == null || email.getAttachments().isEmpty()){
            disableAttachButton();
            log.info("NO ATTACHMENT");
        }
        else{
            enableAttachButton();
        }
    }
    
    /**
     * Enables the button to save attachment if the selected email
     * contains an attachment. Else, it will disable the button
     */
    public void enableAttachButton(){
        saveAttachButton.setDisable(false);
    }
    
    public void disableAttachButton(){
        saveAttachButton.setDisable(true);
    }
    
    /**
     * Fill out the information needed to reply which is To, subject and the previous message
     * appended to the new message.
     */
    public void fillReplyInfo(){
        toTextField.textProperty().set(email.getFrom().getEmail());
        subjectTextField.textProperty().set("RE: " + email.getSubject());
        //Appends previous message to new message
        String prevMessage = getMessageContent(email.getAllMessages());
        editor.setHtmlText("<br/><br/><br/>-------------------<br/>" + prevMessage);
    }
    
    /**
     * Fill out the information needed to reply all. Similar to reply, but add
     * the CC field
     */
    public void fillReplyAllInfo(){
        fillReplyInfo();
        ccTextField.textProperty().set(ccToString(email.getCc()));
    }
    
    /**
     * Fill out information for a forwarded message which is simply the content
     * of the selected email.
     */
    public void fillForwardInfo(){
        String prevMessage = getMessageContent(email.getAllMessages());
        editor.setHtmlText("FORWARDED MESSAGE:<br/>-------------------<br/>" + prevMessage);
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
    
    /**
     * Prompts user for location of where to save the attachment
     */
    public void saveFile() {
        Stage stage = new Stage();

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select a directory to save");
        File selectedDirectory = chooser.showDialog(stage);

        if (selectedDirectory != null && selectedDirectory.exists()) {
            String path = selectedDirectory.getAbsolutePath();
            writeFile(path);
            
        }
    }
    
    /**
     * Writes the data of the attachment into the disk
     * @param path 
     */
    private void writeFile(String path) {
        List<EmailAttachment> attachments = email.getAttachments();

        try {
            for (EmailAttachment ea : attachments) {
                try (FileOutputStream fos = new FileOutputStream(path + "\\" + ea.getName())) {
                    fos.write(ea.toByteArray());
                    displayAlert("Successfully saved attachment");
                }
            }
        } catch (IOException ioe) {
            log.info("ERROR SAVING FILE:\n" + ioe.getMessage());
        }
    }
    
    /**
     * Validates the fields on the email and then send it
     */
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
                
                //Checks if message has any html content, if it does, 
                //Switch the html parameters with the message paramters
                if (message.matches(".*\\<[^>]+>.*")) {
                    mailer.sendEmail(to, Optional.of(cc), Optional.of(bcc), subject, "", message, embedPath, attachPath);
                } else {
                    mailer.sendEmail(to, Optional.of(cc), Optional.of(bcc), subject, message, "", embedPath, attachPath);
                }
                displayAlert("SUCCESSFULLY SENT EMAIL");
            }
            else{
                displayAlert("ERROR, EMAIL NOT SENT");
            }
            
            //disables the send/attach button after
            disableEditorButtons();
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
            throw new IllegalArgumentException("Please provide a recipient");
        }
        //Determines if the email inputted is a valid email address
        else{
            checkAddress(to);
        }
        
        //validate cc if there is any
        if(!cc.equals("")){
            checkAddress(cc);
        }
        
        if(!bcc.equals("")){
            checkAddress(bcc);
        }
        
        return isValid;
    }
    
    /**
     * Returns true if the inputted addresses are valid.
     * @param myAddresses
     * @return 
     */
    private void checkAddress(String myAddresses){
        String[] ccList = myAddresses.split(",");
            
        for(String CcAddress : ccList){
            EmailAddress addressToCheck = new EmailAddress(CcAddress);
            if(!addressToCheck.isValid()){
                throw new IllegalArgumentException("EMAIL IS NOT VALID");
            }
        }
    }
    
    /**
     * Allows user to select an attachment they would like to send
     * and places it in the email
     */
    public void placeAttachment(){
        Stage stage = new Stage();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select attachment");
        File file = chooser.showOpenDialog(stage);
        
        if(file != null && file.exists()){
             attachPath = file.getAbsolutePath();
        }
    }
    
    /**
     * TODO:
     */
    private void saveAttachmentFromEmail(){
        
    }
}
