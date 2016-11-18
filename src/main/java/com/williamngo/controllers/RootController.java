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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author 1435707
 */
public class RootController implements Initializable {
    
    private TreeController treeControl;
    private EditorController editorControl;
    private TableController tableControl;
    //private QuickViewController quickViewCon;
    //private MailDetailsController detailsCon;
    
    private ConfigBean cb; 
    private JagEmailDAO jagDAO;
    private MailerImpl mailer; 
    private PropertyManager pm = new PropertyManager("src/main/resources");
    
    //Each individual panes in the root
    @FXML
    private BorderPane treePane;
    @FXML
    private BorderPane tablePane;
    @FXML
    private BorderPane editorPane;
    
    
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
        
        if(cb != null){
            //SetDatabase()
            this.mailer = new MailerImpl(cb);
            //SetDAO()
            this.jagDAO = new JagEmailDAOImpl(cb);
            //Receive Email here
            
            //Insert editor
            insertEditor();
            insertTree();
            insertTable();
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
            //detailsCon.setEmailClient(mailSystem);

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
            
            tablePane.getChildren().add(bp);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
