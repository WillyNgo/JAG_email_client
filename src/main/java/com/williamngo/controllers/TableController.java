/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.business.JagEmail;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.database.JagEmailDAOImpl;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class TableController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());
    
    private JagEmailDAOImpl jagDAO;
    private String foldername;
    private EditorController editorControl;
    private RootController rootControl;
    private JagEmail email;
    
    @FXML
    private BorderPane tablePane;
    
    @FXML
    private TableView<JagEmail> emailsTableView; 
    
    @FXML
    private TableColumn<JagEmail, String> fromColumn;
    
    @FXML
    private TableColumn<JagEmail, String> subjectColumn;
    
    @FXML
    private TableColumn<JagEmail, String> dateRecvColumn;
    
    public TableController()
    {
        super();
        log.info("Ok at least constructor: TableControl works");
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fromColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getFrom().toString()));
        
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getSubject()));
        
        dateRecvColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getReceiveDate().toString()));
        
        
        emailsTableView.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> clickOnEmail(newValue));
        
        
    }    
    
    /**
     * Method is called when user clicks on an email in the table
     * It will display 
     * @param mail 
     */
    public void clickOnEmail(JagEmail mail){
        email = mail;
        editorControl.setEmail(mail);
        editorControl.displayEmailContent(mail);
        //When user clicks on new email from table, disables the send and attach
        editorControl.disableEditorButtons();
        //Enable the reply buttons
        rootControl.enableReplyButtons();
        
    }
    
    private void adjustColumnWidth(){
        double width = tablePane.getPrefWidth();
        
        fromColumn.setPrefWidth(width * .33);
        subjectColumn.setPrefWidth(width * .67);
        dateRecvColumn.setPrefWidth(width * .33);
    }
    
    public void displayTable() throws SQLException {
        emailsTableView.setItems(getAllEmails());
    }
    
    private ObservableList<JagEmail> getAllEmails() throws SQLException{
        List<JagEmail> emails = this.jagDAO.retrieveEmail(foldername);
        
        ObservableList<JagEmail> mails = FXCollections.observableArrayList();
        
        for(JagEmail j : emails){
            mails.add(j);
        }
        
        return mails;
    }
    
    public void setFoldername(String foldername){
        this.foldername = foldername;
    }
    
    public void setJagDAO(JagEmailDAOImpl jagDAO){
        this.jagDAO = jagDAO;
    }
    
    public void setEditorController(EditorController editorControl){
        this.editorControl = editorControl;
    }
    
    public void setRootController(RootController rootControl){
        this.rootControl = rootControl;
    }
    
    public TableView<JagEmail> getEmailsTable(){
        return emailsTableView;
    }
}
