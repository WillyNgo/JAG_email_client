package com.williamngo.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.williamngo.beans.ConfigBean;
import com.williamngo.database.JagEmailDAO;
import com.williamngo.business.*;
import static com.williamngo.business.utility.displayAlert;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import jodd.mail.EmailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author Willy
 */
public class ConfigController {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private ConfigBean cb;
    private JagEmailDAO jagemailDAO;
    private PropertyManager pm;
    private Stage stage;
    private RootController controller;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField SMTPServerNameTextField;

    @FXML
    private TextField IMAPServerNameTextField;

    @FXML
    private TextField emailAddressTextField;

    @FXML
    private PasswordField emailPasswordField;

    @FXML
    private TextField SMTPPortNoTextField;

    @FXML
    private TextField IMAPPortNoTextField;

    @FXML
    private TextField databaseURLTextField;

    @FXML
    private TextField databaseUserNameTextField;

    @FXML
    private PasswordField databasePasswordField;

    /**
     * Default constructor
     */
    public ConfigController() {
        super();
    }

    /**
     * This method is automatically called after the fxml file has been loaded.
     * This code binds the properties of the data bean to the JavaFX controls.
     * Changes to a control is immediately written to the bean and a change to
     * the bean is immediately shown in the control.
     */
    @FXML
    private void initialize() {
        //Set a new configBean for user to input new properties
        cb = new ConfigBean();
        if (cb != null) {
            Bindings.bindBidirectional(userNameTextField.textProperty(), cb.userNameProperty());
            Bindings.bindBidirectional(SMTPServerNameTextField.textProperty(), cb.smtpServerNameProperty());
            Bindings.bindBidirectional(IMAPServerNameTextField.textProperty(), cb.imapServerNameProperty());
            Bindings.bindBidirectional(emailAddressTextField.textProperty(), cb.emailAddressProperty());
            Bindings.bindBidirectional(emailPasswordField.textProperty(), cb.emailPasswordProperty());
            Bindings.bindBidirectional(SMTPPortNoTextField.textProperty(), cb.smtpPortNoProperty(), new NumberStringConverter());
            Bindings.bindBidirectional(IMAPPortNoTextField.textProperty(), cb.imapPortNoProperty(), new NumberStringConverter());
            Bindings.bindBidirectional(databaseURLTextField.textProperty(), cb.databaseURLProperty());
            Bindings.bindBidirectional(databaseUserNameTextField.textProperty(), cb.databaseUserNameProperty());
            Bindings.bindBidirectional(databasePasswordField.textProperty(), cb.databasePasswordProperty());
            log.info("BINDED cb is not null.");
        }
    }
    /**
     * This method will start the root view if user has submitted proper
     * information in the configuration
     * @param event
     * @throws IOException 
     */
    @FXML
    void submitForm(ActionEvent event) throws IOException {
        if (validateForm(cb)) {
            System.out.println("Everything good!");
            //Save the config info into a new properties file
            pm.writeTextProperties(cb);
            log.info("ConfigController: New Properties updated");
            startRootView();
            stage.close();
        } 
    }
    
    /**
     * Starts a new root view
     */
    private void startRootView() {
        try {
            FXMLLoader loader = null;
            Stage myStage = new Stage();
            URL path = Paths.get("src/main/resources/fxml/root.fxml").toUri().toURL();

            loader = new FXMLLoader();
            loader.setLocation(path);
            loader.setBuilderFactory(new JavaFXBuilderFactory());

            Scene scene = new Scene(loader.load());
            
            controller = loader.getController();
            //LAST THING I ADDED, TAKE THIS OUT IF DOESNT WORK TMR
            controller.addDefaultFolders();
            myStage.setTitle("Email Client - " + cb.getUserName());
            myStage.setResizable(true);
            myStage.setScene(scene);
            myStage.show();
            showProperties();
        } catch (MalformedURLException mue) {
            mue.getMessage();
        } catch (IOException ioe) {
            ioe.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Ensures that form has all the values given and validates email
     * 
     * @param myCb
     * @return  isValid - indicates if config has valid information.
     */
    private boolean validateForm(ConfigBean myCb) {
        boolean isValid = true;
        
        if (areFieldsEmpty(myCb)) {
            isValid = false;
        }
        if(!validateEmailAddress()){
            isValid = false;
        }
        return isValid;
    }

    /**
     * Returns true if any field on the configuration form is empty, else return
     * false
     *
     * @param myCb
     * @return isEmpty - boolean indicating if a field was found to be empty or not
     * @throws IllegalArgumentException
     */
    private boolean areFieldsEmpty(ConfigBean myCb) throws IllegalArgumentException {
        boolean isEmpty = false;
        if (myCb.getUserName().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getUserName");
            //userNameTextField.textProperty().get().length()
        }
        if (myCb.getEmailAddress().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getEmailAddress");
        }
        if (myCb.getEmailPassword().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getEmailPassword");
        }
        if (myCb.getSmtpServerName().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getSmtpServerName");
        }
        if (myCb.getImapServerName().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getImapServerName");
        }
        if (myCb.getDatabaseURL().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getDatabaseURL");
        }
        if (myCb.getDatabaseUserName().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getDatabaseUserName");
        }
        if (myCb.getDatabasePassword().length() == 0) {
            isEmpty = true;
            displayAlert("Empty field: getDatabasePassword");
        }

        return isEmpty;

    }
    
    /**
     * Returns true if email address from configBean is valid
     */
    private boolean validateEmailAddress() {
        boolean isValid = true;
        EmailAddress ea = new EmailAddress(cb.getEmailAddress());
        if (!ea.isValid()) {
            displayAlert("Invalid email address!");
            return false;
        }
        
        return isValid;
    }
    
    /**
     * Setter for DAO object
     * @param jag 
     */
    public void setJagEmailDAO(JagEmailDAO jag) {
        this.jagemailDAO = jag;
    }
    
    /**
     * Setter for property manager
     * @param pm 
     */
    public void setPropertyManager(PropertyManager pm) {
        this.pm = pm;
    }
    
    /**
     * Setter for the configuration bean
     * @param cb 
     */
    public void setConfigBean(ConfigBean cb) {
        log.info("SETTING CONFIG BEAN");
        this.cb = cb;
    }
    
    /**
     * NOT SURE IF SHOULD BE TAKEN OUT
     * @param stage 
     */
    public void setCurrentStage(Stage stage) {
        this.stage = stage;
    }
    
    //FOR TESTING, DELETE LATER
    public void showProperties() {
        log.info("YOUR CURRENT USERNAME IS: " + cb.getUserName());
        log.info("YOUR CURRENT EMAILADDRESS IS: " + cb.getEmailAddress());
        log.info("YOUR CURRENT PASSWORD IS: " + cb.getEmailPassword());
        log.info("YOUR CURRENT DATABASE URL IS: " + cb.getDatabaseURL());
    }
}
