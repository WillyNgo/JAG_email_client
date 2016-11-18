/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.jagemail;

import com.williamngo.beans.ConfigBean;
import com.williamngo.business.PropertyManager;
import com.williamngo.controllers.ConfigController;
import com.williamngo.controllers.RootController;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.database.JagEmailDAOImpl;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static javafx.application.Application.launch;
import javafx.fxml.JavaFXBuilderFactory;

/**
 *
 * @author Willy
 */
public class MainApp extends Application {

    // Real programmers use logging, not System.out.println
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // The primary window or frame of this application
    //private Stage primaryStage;
    private JagEmailDAO jagDAO;
    private PropertyManager pm;
    private ConfigBean cb;

    /**
     * Constructor
     */
    public MainApp() {
        super();
        jagDAO = new JagEmailDAOImpl();
    }

    /**
     * The application starts here
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("Program Begins");
        this.pm = new PropertyManager("src/main/resources");
        this.cb = pm.loadTextProperties();
        // The Stage comes from the framework so make a copy to use elsewhere
        //this.primaryStage = primaryStage;
        // Create the Scene and put it on the Stage
        configureStage(primaryStage);
    }

    /**
     * Load the FXML and bundle, create a Scene and put the Scene on Stage.
     *
     * Using this approach allows you to use loader.getController() to get a
     * reference to the fxml's controller should you need to pass data to it.
     * Not used in this archetype.
     */
    private void configureStage(Stage primaryStage) {
        if (this.cb == null) {
            showConfigWindow(primaryStage);
        } else {
            showUserInterface(primaryStage);
        }
    }

    /**
     * Shows configuration window for the user to input a new config file.
     */
    public void showConfigWindow(Stage stage) {
        this.cb = new ConfigBean();
        FXMLLoader loader = null;
        
        try{
            URL path = Paths.get("src/main/resources/fxml/config.fxml").toUri().toURL();
            
            loader = new FXMLLoader();
            loader.setLocation(path);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            log.info("Loader done loading");
            Scene scene = new Scene(loader.load());
            log.info("Scene loaded");
            ConfigController control = (ConfigController) loader.getController();
            
            control.setJagEmailDAO(this.jagDAO);
            control.setConfigBean(this.cb);
            control.setPropertyManager(this.pm);
            
            
            //Set stage
            stage.setTitle("JagEmail Client");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Shows the main application page.
     * @param stage 
     */
    private void showUserInterface(Stage stage) {
        try {
            FXMLLoader loader = null;

            URL path = Paths.get("src/main/resources/fxml/root.fxml").toUri().toURL();
            
            loader = new FXMLLoader();
            loader.setLocation(path);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            
            Scene scene = new Scene(loader.load());
            
            RootController controller = (RootController) loader.getController();
            
            stage.setTitle("Email Client");
            stage.setResizable(true);
            stage.setScene(scene);
            stage.show();

        } catch (MalformedURLException mue) {
            mue.getMessage();
        } catch (IOException ioe) {
            ioe.getMessage();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Where it all begins
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
