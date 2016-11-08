/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.beans.ConfigBean;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class FoldersController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ConfigBean cb = new ConfigBean();
        /*
        
		// We need a root node for the tree and it must be the same type as all nodes
		FishData rootFish = new FishData();
		// The tree will display common name so we set this for the root
		rootFish.setCommonName("Fishies");
		fishFXTreeView.setRoot(new TreeItem<FishData>(rootFish));
		
		// This cell factory is used to choose which field in the FihDta object is used for the node name
		fishFXTreeView.setCellFactory((e) -> new TreeCell<FishData>(){
            @Override
            protected void updateItem(FishData item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null) {
                    setText(item.getCommonName());
                    setGraphic(getTreeItem().getGraphic());
                } else {
                    setText("");
                    setGraphic(null);
                }
            }
        });
        
        */
    }    
    
}
