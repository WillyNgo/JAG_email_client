/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.beans.ConfigBean;
import com.williamngo.business.JagEmail;
import com.williamngo.business.PropertyManager;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.database.JagEmailDAOImpl;
import com.williamngo.interfaces.MailerImpl;
import com.williamngo.jagemail.MainApp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author 1435707
 */
public class RootController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    private TreeController treeControl;
    private EditorController editorControl;
    private TableController tableControl;
    
    private ConfigBean cb; 
    private JagEmailDAOImpl jagDAO;
    private MailerImpl mailer; 
    private PropertyManager pm = new PropertyManager("src/main/resources");
    
    //Each individual panes in the root
    @FXML
    private BorderPane treePane;
    @FXML
    private BorderPane tablePane;
    @FXML
    private BorderPane editorPane;
    
    //Buttons on the the task bar
    @FXML
    private Button newMessageButton;
    @FXML
    private Button replyButton;
    @FXML
    private Button replyAllButton;
    @FXML
    private Button deleteMessageButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button addFolderButton;
    @FXML
    private Button deleteFolderButton;
    @FXML
    private Button reloadButton;
    
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            log.info("BEFORE LOAD PROPERTIES ROOT INITIALIZE");
            cb = pm.loadTextProperties();
            log.info("INITIALIZE ROOT CONTROLLER");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        
        if(cb != null){
            try{
            //Initializes the config, dao and properties
            setUpNewRoot();
            
            //Insert editor
            insertEditor();
            insertTable();
            insertTree();
            
            log.info("INSERTED ALL MODULES");
            disableMessageButtons();
            disableDeleteFolderButton();
            }
            catch(IOException ioe){
                log.info(ioe.getMessage());
            }
        }
        
    }
    
    /**
     * Disables all buttons that interacts with messages when user clicks elsewhere
     * than the table
     */
    public void disableMessageButtons(){
            replyButton.setDisable(true);
            replyAllButton.setDisable(true);
            forwardButton.setDisable(true);
            deleteMessageButton.setDisable(true);
    }
    
    /**
     * Enables all buttons that interacts with messages
     * when user clicks on a message in the table
     */
    public void enableMessageButtons(){
        replyButton.setDisable(false);
        replyAllButton.setDisable(false);
        forwardButton.setDisable(false);
        deleteMessageButton.setDisable(false);
    }
    
    /**
     * Enables the delete folder button when user selects a folder
     */
    public void enableDeleteFolderButton(){
            deleteFolderButton.setDisable(false);
        
    }
    
    /**
     * Disables delete folder button when user clicks elsewhere than the treeView
     */
    public void disableDeleteFolderButton(){
        deleteFolderButton.setDisable(true);
    }
    
    /**
     * Enables the send and attach button in the editor when use clicks on
     * either new message or reply or reply all
     */
    public void enableEditorButtons(){
        editorControl.enableEditorButtons();
    }
    
    @FXML
    public void clickOnNewMsg(){
        //Clears input fields of the editor for a new message
        clearInputFields();
        enableEditorButtons();
        disableMessageButtons();
    }
    
    @FXML
    public void clickOnReply(){
        editorControl.enableEditorButtons();
        editorControl.clearInputFields();
        editorControl.fillReplyInfo();
    }
    
    @FXML
    public void clickOnReplyAll(){
        editorControl.enableEditorButtons();
        editorControl.clearInputFields();
        editorControl.fillReplyAllInfo();
    }
    
    @FXML
    public void clickOnForward(){
        editorControl.enableEditorButtons();
        editorControl.clearInputFields();
        editorControl.fillForwardInfo();
    }
    
    @FXML
    public void clickOnReload() throws SQLException{
        tableControl.displayTable();
    }
    
    @FXML
    public void clickOnAddFolder() throws IOException{
        treeControl.showAddFolderWindow();
    }
    
    
    
    public void clearInputFields(){
        editorControl.clearInputFields();
    }
    
    
    /**
     * Set up new session
     * @throws IOException 
     */
    public void setUpNewRoot() throws IOException{
        try{
            //Setting up new config, dao and mailer
            this.cb = pm.loadTextProperties();
            this.jagDAO = new JagEmailDAOImpl(cb);
            this.mailer = new MailerImpl(cb);
            log.info("SETTING UP NEW ROOT");
            
        }
        catch (IOException ioe){
            log.info(ioe.getMessage());
        }
    }
    
    /**
     * Inserts the HTML Editor
     */
    private void insertEditor(){
           try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(MainApp.class.getResource("/fxml/editor.fxml"));
            BorderPane bp = (BorderPane) loader.load();

            // Give the controller the data object.
            editorControl = loader.getController();
            editorControl.setRootController(this);
            editorControl.setMailer(mailer);

            editorPane.getChildren().add(bp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void insertTree(){
        try{
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/fxml/foldersTree.fxml"));
            BorderPane bp = (BorderPane) loader.load();
            
            treeControl = loader.getController();
            
            treeControl.setJagEmailDAO(jagDAO);
            treeControl.setTableController(tableControl);
            treeControl.setRootController(this);
            treeControl.displayTree();
            
            treePane.getChildren().add(bp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void insertTable(){
        try{
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/fxml/table.fxml"));
            BorderPane bp = (BorderPane) loader.load();
            
            tableControl = loader.getController();
            
            tableControl.setEditorController(editorControl);
            tableControl.setRootController(this);
            tableControl.setJagDAO(jagDAO);
            
            tablePane.getChildren().add(bp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //DELETE THIS
    public void showProperties() {
        log.info("YOUR CURRENT USERNAME IS: " + cb.getUserName());
        log.info("YOUR CURRENT EMAILADDRESS IS: " + cb.getEmailAddress());
        log.info("YOUR CURRENT PASSWORD IS: " + cb.getEmailPassword());
    }
}
