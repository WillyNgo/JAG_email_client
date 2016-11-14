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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class FoldersTreeController implements Initializable {

    private JagEmailDAO jagemailDAO;

    @FXML
    private BorderPane treeLayout;

    @FXML
    private TreeView<Folder> folderTreeView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
    }

    private void setJagEmailDAO(JagEmailDAO dao) {
        this.jagemailDAO = dao;
    }

    /**
     * Build the tree from the database
     *
     * @throws SQLException
     */
    public void displayTree() throws SQLException {
        // Retreive the list of fish
        List<Folder> foldersList = jagemailDAO.getAllFolders();
        
        // Build an item for each fish and add it to the root
        if (foldersList != null) {
            for (Folder fd : foldersList) {
                TreeItem<Folder> item = new TreeItem<>(fd);
                //Add picture later
                //item.setGraphic(new ImageView(getClass().getResource("/images/fish.png").toExternalForm()));
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
                        (observable, oldValue, newValue) -> System.out.print(newValue.getValue()));
    }

}
