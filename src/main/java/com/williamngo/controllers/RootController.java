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
    private Button moveMessageButton;
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
            cb = pm.loadTextProperties();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        
        //If a config.properties file was found in the resources, start the app
        if(cb != null){
            try{
            //Initializes the config, dao and properties
            setUpNewRoot();
            
            //Insert editor
            insertEditor();
            insertTable();
            insertTree();
            log.info("INSERTED ALL MODULES");
            
            //Disables buttons that interacts with messages(reply/All, forward...)
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
            moveMessageButton.setDisable(true);
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
        moveMessageButton.setDisable(false);
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
     * either new message, reply, reply all or Forward
     */
    public void enableEditorButtons(){
        editorControl.enableEditorButtons();
    }
    
    /**
     * When user clicks on the new message button, this method will clear all
     * the input fields on the editor and enable the send and attach button in
     * then editor toolbar. It will also disable the reply/all and forward button
     */
    @FXML
    public void clickOnNewMsg(){
        //Clears input fields of the editor for a new message
        clearInputFields();
        enableEditorButtons();
        disableMessageButtons();
    }
    
    /**
     * This method will display a window that will prompt the user to confirm
     * if they would like to send their mail to the trash. If the user is 
     * selecting an email from the trash folder, it will prompt the user to
     * confirm whether they would like to indefinetely delete the email.
     */
    @FXML
    public void clickOnDeleteMsg(){
        tableControl.showTrashEmailWindow();
    }
    
    /**
     * Will display a window that will ask the user to which folder they would
     * like to move their selected email
     */
    @FXML
    public void clickOnMoveMsg(){
        tableControl.showMoveEmailWindow();
    }
    
    /**
     * This method will fill the editor with the information necessary for
     * replying to an email. It will input the sender for the TO field. It will
     * append "RE: " at the beginning of the subject and will append the sender's
     * original message at the end of the email.
     */
    @FXML
    public void clickOnReply(){
        editorControl.enableEditorButtons();
        editorControl.clearInputFields();
        editorControl.fillReplyInfo();
    }
    
    /**
     * Similar to the reply, it will additionally input the list of CCs into the
     * CC field of the editor.
     */
    @FXML
    public void clickOnReplyAll(){
        editorControl.enableEditorButtons();
        editorControl.clearInputFields();
        editorControl.fillReplyAllInfo();
    }
    
    /**
     * This method will clear all fields aside from the message content of the
     * selected email.
     */
    @FXML
    public void clickOnForward(){
        editorControl.enableEditorButtons();
        editorControl.clearInputFields();
        editorControl.fillForwardInfo();
    }
    
    /**
     * This method will reload the email table and the folder tree.
     * @throws SQLException 
     */
    @FXML
    public void clickOnReload() throws SQLException{
        tableControl.displayTable();
        treeControl.displayTree();
    }
    
    /**
     * This method will prompt the user for naming the new folder.
     * @throws IOException 
     */
    @FXML
    public void clickOnAddFolder() throws IOException{
        treeControl.showAddFolderWindow();
    }
    
    /**
     * Will prompt the user to confirm whether they would like to delete the 
     * selected folder. It will additionally delete all emails associated with
     * the folder.
     */
    @FXML
    public void clickOnDeleteFolder(){
        treeControl.showDeleteFolderWindow();
    }
    
    
    /**
     * Clears all input fields of the editor view.
     */
    public void clearInputFields(){
        editorControl.clearInputFields();
    }
    
    
    /**
     * Set up new root view
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
     * Inserts the HTML Editor and sets necessary controllers
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
            editorControl.disableAttachButton();
            editorPane.getChildren().add(bp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the tree controller for the folders as well as other necessary 
     * controllers. 
     */
    private void insertTree(){
        try{
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/fxml/foldersTree.fxml"));
            BorderPane bp = (BorderPane) loader.load();
            
            treeControl = loader.getController();
            
            treeControl.setJagEmailDAO(jagDAO);
            treeControl.setTableController(tableControl);
            treeControl.setRootController(this);
            treeControl.setEditorController(editorControl);
            treeControl.displayTree();
            
            treePane.getChildren().add(bp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Inserts the email table and sets the necessary controllers.
     */
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
    
    /**
     * TRYING THIS OUT TO BE REMOVED IF UNSUCCESFUL
     */
    public void addDefaultFolders(){
        jagDAO.addFolder("sent");
        jagDAO.addFolder("inbox");
        jagDAO.addFolder("trash");
    }
}
