/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.business;

import javafx.scene.control.Alert;

/**
 *
 * @author Willy
 */
public class utility {
    public static void displayAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
