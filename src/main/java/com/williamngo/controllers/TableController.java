/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.business.JagEmail;
import com.williamngo.database.JagEmailDAO;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
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
    
    private JagEmailDAO jagemailDAO;
    
    @FXML
    private BorderPane borderpane;
    
    @FXML
    private TableView<JagEmail> emailTable; 
    
    @FXML
    private TableColumn<JagEmail, String> fromColumn;
    
    @FXML
    private TableColumn<JagEmail, String> subjectColumn;
    
    @FXML
    private TableColumn<JagEmail, Date> dateRecvColumn;
    
    public TableController()
    {
        super();
        log.info("Ok at least constructor works");
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //fromColumn.setCellValueFactory(cellData -> cellData.getValue().getFrom().getEmail());
    }    
    
}
