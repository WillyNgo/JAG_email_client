/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.beans.ConfigBean;
import com.williamngo.beans.FolderBean;
import com.williamngo.business.Folder;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.database.JagEmailDAOImpl;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class TreeController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private JagEmailDAO jagDAO;
    private TableController tableControl;
    
    @FXML
    private BorderPane treePane;

    @FXML
    private TreeView<String> folderTreeView;
    
    @FXML
    ObservableList<String> allFolders;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        Folder root = new Folder();
        FolderBean rootFb = new FolderBean();

        rootFb.setFoldername("root");
        folderTreeView.setRoot(new TreeItem<Folder>(root));

        folderTreeView.setCellFactory((e) -> new TreeCell<Folder>() {
            @Override
            protected void updateItem(Folder item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getFolderName());
                    setGraphic(getTreeItem().getGraphic());
                } else {
                    setText("");
                    setGraphic(null);
                }
            }
        });
        */
    }

    private void setJagEmailDAO(JagEmailDAO dao) {
        this.jagDAO = dao;
    }

    /**
     * Build the tree from the database
     *
     * @throws SQLException
     */
    public void displayTree() throws SQLException {
        // Retreive the list of fish
        List<String> foldersList = jagDAO.getAllFolders();
        folderTreeView.setRoot(new TreeItem(new String("ROOT folder")));
        folderTreeView.setCellFactory((e) -> new TreeCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if(item != null){
                    setText(item);
                    setGraphic(getTreeItem().getGraphic());
                }
                else{
                    setText("");
                    setGraphic(null);
                }
        }
    });

        
        allFolders = FXCollections.observableArrayList();
        for(String name : foldersList){
            allFolders.add(name);
        }
        
        if(allFolders != null){
            for(String fName : allFolders){
                TreeItem<String> item = new TreeItem<>(fName);
                
                Image img = new Image("images/folder.png");
                ImageView folderImg = new ImageView(img);
                item.setGraphic(folderImg);
                item.setValue(fName);
                folderTreeView.getRoot().getChildren().add(item);
            }
        }
        
        // Open the tree
        folderTreeView.getRoot().setExpanded(true);

        // Listen for selection changes and show the fishData details when changed.
        folderTreeView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> showTree(newValue));
    }
    
    private void showTree(TreeItem<String> folder){
        String foldername = folder.getValue();
        try{
            tableControl.setFoldername(foldername);
            tableControl.displayTable();
        }
        catch(SQLException sqle){
            log.info(sqle.getMessage());
        }
    }

}
