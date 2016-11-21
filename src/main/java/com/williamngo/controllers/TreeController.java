/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.controllers;

import com.williamngo.beans.ConfigBean;
import static com.williamngo.business.utility.displayAlert;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.database.JagEmailDAOImpl;
import com.williamngo.interfaces.MailerImpl;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class TreeController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private JagEmailDAOImpl jagDAO;
    private TableController tableControl;
    private RootController rootControl;
    private EditorController editorControl;

    @FXML
    private BorderPane treePane;

    @FXML
    private TreeView<String> foldersTreeView;

    @FXML
    ObservableList<String> allFolders;
    
    @FXML
    private TextField addFolderTextField;
    @FXML
    private Button addFolderBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    /************* SETTERS ***********/
    public void setJagEmailDAO(JagEmailDAOImpl dao) {
        this.jagDAO = dao;
    }
    public void setTableController(TableController control) {
        this.tableControl = control;
        log.info("TABLE CONTROLLER HAS BEEN PASSED TO TREE CONTROLLER!");
    }
    public void setRootController(RootController rootControl) {
        this.rootControl = rootControl;
    }
    
    public void setEditorController(EditorController editorControl){
        this.editorControl = editorControl;
    }
    
    /**
     * Enables the delete folder button when user has selected a folder,
     * otherwise it should be disabled
     */
    public void enableDeleteFolderButton() {
        rootControl.enableDeleteFolderButton();
    }

    /**
     * Build the tree from the database
     *
     * @throws SQLException
     */
    public void displayTree() throws SQLException {
        // Retreive the list of folder
        List<String> foldersList = jagDAO.getAllFolders();
        foldersTreeView.setRoot(new TreeItem(new String("Folders")));
        foldersTreeView.setCellFactory((e) -> new TreeCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    
                    setText(item);
                    setGraphic(getTreeItem().getGraphic());
                } else {
                    
                    setText("");
                    setGraphic(null);
                }
            }
        });
        
        //Puts all folder names inside an observable array list
        allFolders = FXCollections.observableArrayList();
        for (String name : foldersList) {
            allFolders.add(name);
        }
        
        //Creates a tree item for every foldername in the allFolders array list
        if (allFolders != null) {
            for (String fName : allFolders) {
                TreeItem<String> item = new TreeItem<>(fName);

                Image img = new Image("icons/folderImage.png");
                ImageView folderImg = new ImageView(img);
                item.setGraphic(folderImg);
                item.setValue(fName);
                foldersTreeView.getRoot().getChildren().add(item);
            }
        }

        // Open the tree
        foldersTreeView.getRoot().setExpanded(true);

        // Listen for selection changes details when changed.
        foldersTreeView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> clickOnFolder(newValue));
    }
    
    /**
     * When user clicks on a folder, this method disables buttons that interacts
     * with emails.
     * @param folder 
     */
    private void clickOnFolder(TreeItem<String> folder) {
        String foldername = folder.getValue();
        try {
            tableControl.setFoldername(foldername);
            tableControl.displayTable();
            rootControl.enableDeleteFolderButton();
            rootControl.disableMessageButtons();
        } catch (SQLException sqle) {
            log.info(sqle.getMessage());
        }
    }
    
    /**
     * This method prompts the user for a name that will be associated with the
     * newly created folder
     * 
     * @throws IOException 
     */
    public void showAddFolderWindow() throws IOException {
        Stage myStage = new Stage();
        //SEtting label
        Label l = new Label();
        l.setLayoutX(95);
        l.setLayoutY(125);
        l.setText("New folder name: ");
        //SEtting textfield
        TextField tf = new TextField();
        tf.setLayoutX(225);
        tf.setLayoutY(120);
        //Setting button
        Button b = new Button();
        b.setLayoutX(275);
        b.setLayoutY(175);
        b.setText("Submit");
        //Add onclick event that adds folder to database
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String newname = tf.textProperty().get();
                jagDAO.addFolder(newname);
                displayAlert("SUCCESSFULLY ADDED NEW FOLDER");
                //Reloads the tree view to display the created folder
                try {
                    displayTree();
                } catch (SQLException ex) {
                    ex.getMessage();
                }
                myStage.close();
            }
        });
        
        
        AnchorPane root = new AnchorPane();
        root.getChildren().add(l);
        root.getChildren().add(tf);
        root.getChildren().add(b);
        myStage.setScene(new Scene(root, 600, 250));
        myStage.show();
    }
    
    /**
     * Displays a window prompting the user to confirm whether he deletes a 
     * folder.
     */
    public void showDeleteFolderWindow() {
        Stage myStage = new Stage();
        //SEtting label
        Label l = new Label();
        l.setLayoutX(95);
        l.setLayoutY(125);
        l.setText("Are you sure you want to delete this folder ?");
        
        //Setting confirm button
        Button bYes = new Button();
        bYes.setLayoutX(225);
        bYes.setLayoutY(145);
        bYes.setText("Yes");
        //Add onclick event that confirms deletion
        bYes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteFolder();
                displayAlert("SUCCESSFULLY DELETED FOLDER");
                try {
                    displayTree();
                } catch (SQLException ex) {
                    ex.getMessage();
                }
                myStage.close();
            }
        });
        
        //Set decline button
        Button bNo = new Button();
        bNo.setLayoutX(275);
        bNo.setLayoutY(145);
        bNo.setText("No");
        //Simply close prompt window
        bNo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myStage.close();
            }
        });
        
        
        AnchorPane root = new AnchorPane();
        root.getChildren().add(l);
        root.getChildren().add(bYes);
        root.getChildren().add(bNo);
        myStage.setScene(new Scene(root, 600, 250));
        myStage.show();
    }
    
    /**
     * Deletes a folder, cannot delete inbox, sent or trash
     */
    private void deleteFolder(){
        String foldername = foldersTreeView.getSelectionModel().getSelectedItem().getValue();
        if(foldername.equals("inbox") || foldername.equals("sent") || foldername.equals("trash")){
            displayAlert("SORRY! CANNOT DELETE INBOX / SENT / TRASH FOLDER");
        }
        else{
            jagDAO.deleteFolder(foldername);
        }
    }
    
}
