package com.williamngo.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.williamngo.beans.ConfigBean;
import com.williamngo.database.JagEmailDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
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
            log.info("cb is not null.");
        }
        else{
            log.info("cb is null.");

        }
    }

    @FXML
    void submitForm(ActionEvent event) throws SQLException {
        if (true) {
            System.out.println("Everything good!");
        }
        else{
            System.out.println("You have errors!");
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        //Validate email address for correct format: check if there's a @ sign
        String email = cb.getEmailAddress();
        if (email.indexOf("@") == -1) {
            Text t = new Text();
            t.setText("Please input a valid email address");
            t.setFill(Color.RED);
            emailAddressTextField.textProperty().set(t.toString());
            isValid = false;
        }

        return isValid;
    }

    public void setJagEmailDAO(JagEmailDAO jag) {
        this.jagemailDAO = jag;
    }

    public void setConfigBean(ConfigBean cb) {
        log.info("SETTING CONFIG BEAN");
        this.cb = cb;
    }

    public void setCurrentStage(Stage stage) {
        this.stage = stage;
    }

}
