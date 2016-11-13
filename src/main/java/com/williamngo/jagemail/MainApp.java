/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williamngo.jagemail;

import com.williamngo.beans.ConfigBean;
import com.williamngo.controllers.ConfigController;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.database.JagEmailDAOImpl;
import java.io.IOException;
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

/**
 *
 * @author Willy
 */
public class MainApp extends Application{
    // Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	// The primary window or frame of this application
	private Stage primaryStage;
        private JagEmailDAO jagDAO;

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

		// The Stage comes from the framework so make a copy to use elsewhere
		this.primaryStage = primaryStage;
		// Create the Scene and put it on the Stage
		configureStage();

		// Set the window title
		primaryStage.setTitle("JAG Email");
		// Raise the curtain on the Stage
		primaryStage.show();
	}

	/**
	 * Load the FXML and bundle, create a Scene and put the Scene on Stage.
	 *
	 * Using this approach allows you to use loader.getController() to get a
	 * reference to the fxml's controller should you need to pass data to it.
	 * Not used in this archetype.
	 */
	private void configureStage() {
		try {
			// Instantiate the FXMLLoader
			FXMLLoader loader = new FXMLLoader();

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainApp.class.getResource("/fxml/config.fxml"));

			// Localize the loader with its bundle
			// Uses the default locale and if a matching bundle is not found
			// will then use MessagesBundle.properties
			//loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
                        //DOES NOT WORK
                        
			// Parent is the base class for all nodes that have children in the
			// scene graph such as AnchorPane and most other containers
			Parent parent = (GridPane) loader.load();

			// Load the parent into a Scene
			Scene scene = new Scene(parent);

			// Put the Scene on Stage
			primaryStage.setScene(scene);

			// Retrieve a reference to the controller so that you can pass a
			// reference to the persistence object
			ConfigController controller = loader.getController();
			controller.setJagEmailDAO(jagDAO);

		} catch (IOException ex) {
			log.error(null, ex);
			System.exit(1);
		}
	}

	/**
	 * Where it all begins
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
