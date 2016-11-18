
package com.williamngo.business;

import com.williamngo.beans.ConfigBean;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import static java.nio.file.Paths.get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author William Ngo
 */
public class PropertyManager {
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private String path;
    
    public PropertyManager(){
        this.path = "src/main/resources";
    }
    public PropertyManager(String path){
        this.path = path;
    }
    
    /**
     * Returns a ConfigBean object with the contents of the properties file. The
     * property files name should be named "config.properties".
     *
     * @param path Must exist, will not be created
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final ConfigBean loadTextProperties() throws IOException {
        Properties prop = new Properties();
        Path txtFile = get(this.path, "config.properties");
        ConfigBean cb = new ConfigBean();
        // File must exist
        if (Files.exists(txtFile)) {
            try (InputStream propFileStream = newInputStream(txtFile);) {
                prop.load(propFileStream);
            }
            log.info("File does exists, now going through settings");
            cb.setUserName(prop.getProperty("userName"));
            cb.setEmailPassword(prop.getProperty("emailPassword"));
            cb.setEmailAddress(prop.getProperty("emailAddress"));
            cb.setSmtpServerName(prop.getProperty("smtpServerName"));
            cb.setImapServerName(prop.getProperty("imapServerName"));
            cb.setSmtpPort(Integer.parseInt(prop.getProperty("smtpPort")));
            cb.setImapPort(Integer.parseInt(prop.getProperty("imapPort")));
            cb.setDatabaseUserName(prop.getProperty("databaseUserName"));
            cb.setDatabaseURL(prop.getProperty("databaseURL"));
            cb.setDatabasePassword(prop.getProperty("databasePassword"));
        }
        else{
            cb = null;
            log.info("THE CB RETURNED IS NULL");
        }
        return cb;
    }
    /**
     * Creates a plain text properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file
     * @param cb The bean to store into the properties
     * @throws IOException
     */
    public final void writeTextProperties(final String path, final String propFileName, final ConfigBean cb) throws IOException {
        Properties prop = new Properties();
        prop.setProperty("userName", cb.getUserName());
        prop.setProperty("emailPassword", cb.getEmailPassword());
        prop.setProperty("emailAddress", cb.getEmailAddress());
        prop.setProperty("smptServerName", cb.getSmtpServerName());
        prop.setProperty("imapServerName", cb.getImapServerName());
        prop.setProperty("smtpPort", cb.getSmtpPort()+"");
        prop.setProperty("imapPort", cb.getImapPort()+"");
        prop.setProperty("databaseName", cb.getDatabaseUserName());
        prop.setProperty("databaseURL", cb.getDatabaseURL());
        prop.setProperty("databasePassword", cb.getDatabasePassword());
        Path txtFile = get(path, propFileName + ".properties");
        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(txtFile)) {
            prop.store(propFileStream, "SMTP Properties");
        }
    }
    /**
     * Returns a MailConfigBean object with the contents of the properties file
     * that is in an XML format
     *
     * @param path Must exist, will not be created. Empty string means root of
     * program folder
     * @param propFileName Name of the properties file
     * @return The bean loaded with the properties
     * @throws IOException
     */
    public final ConfigBean loadXmlProperties(final String path, final String propFileName) throws IOException {
        Properties prop = new Properties();
        // The path of the XML file
        Path xmlFile = get(path, propFileName + ".xml");
        ConfigBean cb = new ConfigBean();
        // File must exist
        if (Files.exists(xmlFile)) {
            try (InputStream propFileStream = newInputStream(xmlFile);) {
                prop.loadFromXML(propFileStream);
            }
            cb.setUserName(prop.getProperty("userName"));
            cb.setEmailPassword(prop.getProperty("emailPassword"));
            cb.setEmailAddress(prop.getProperty("emailAddress"));
            cb.setSmtpServerName(prop.getProperty("smtpServerName"));
            cb.setImapServerName(prop.getProperty("imapServerName"));
            cb.setSmtpPort(Integer.parseInt(prop.getProperty("smtpPort")));
            cb.setImapPort(Integer.parseInt(prop.getProperty("imapPort")));
            cb.setDatabaseUserName(prop.getProperty("databaseName"));
            cb.setDatabaseURL(prop.getProperty("databaseURL"));
            cb.setDatabasePassword(prop.getProperty("databasePassword"));
        }
        return cb;
    }
    /**
     * Creates an XML properties file based on the parameters
     *
     * @param path Must exist, will not be created
     * @param propFileName Name of the properties file. Empty string means root
     * of program folder
     * @param cfb The bean to store into the properties
     * @throws IOException
     */
    public final void writeXmlProperties(final String path, final String propFileName, final ConfigBean cb) throws IOException {
        Properties prop = new Properties();
        prop.setProperty("userName", cb.getUserName());
        prop.setProperty("emailPassword", cb.getEmailPassword());
        prop.setProperty("emailAddress", cb.getEmailAddress());
        prop.setProperty("smptServerName", cb.getSmtpServerName());
        prop.setProperty("imapServerName", cb.getImapServerName());
        prop.setProperty("smtpPort", cb.getSmtpPort()+"");
        prop.setProperty("imapPort", cb.getImapPort()+"");
        prop.setProperty("databaseName", cb.getDatabaseUserName());
        prop.setProperty("databaseURL", cb.getDatabaseURL());
        prop.setProperty("databasePassword", cb.getDatabasePassword());
        
        Path xmlFile = get(path, propFileName + ".xml");
        // Creates the file or if file exists it is truncated to length of zero
        // before writing
        try (OutputStream propFileStream = newOutputStream(xmlFile)) {
            prop.storeToXML(propFileStream, "XML SMTP Properties");
        }
    }
    /**
     * Retrieve the properties file. This syntax rather than normal File I/O is
     * employed because the properties file is inside the jar. The technique
     * shown here will work in both Java SE and Java EE environments. A similar
     * technique is used for loading images.
     *
     * In a Maven project all configuration files, images and other files go
     * into src/main/resources. The files and folders placed there are moved to
     * the root of the project when it is built.
     *
     * @param propertiesFileName : Name of the properties file
     * @throws IOException : Error while reading the file
     * @throws NullPointerException : File not found
     * @return cb 
     */
    public final ConfigBean loadJarTextProperties(final String propertiesFileName) throws IOException, NullPointerException {
        Properties prop = new Properties();
        ConfigBean cb = new ConfigBean();
        // There is no exists method for files in a jar so we try to get the
        // resource and if its not there it returns a null
        if (this.getClass().getResource("/" + propertiesFileName) != null) {
            // Assumes that the properties file is in the root of the
            // project/jar.
            // Include a path from the root if required.
            try (InputStream stream = this.getClass().getResourceAsStream("/" + propertiesFileName);) {
                prop.load(stream);
            }
            cb.setUserName(prop.getProperty("userName"));
            cb.setEmailPassword(prop.getProperty("emailPassword"));
            cb.setEmailAddress(prop.getProperty("emailAddress"));
            cb.setSmtpServerName(prop.getProperty("smtpServerName"));
            cb.setImapServerName(prop.getProperty("imapServerName"));
            cb.setSmtpPort(Integer.parseInt(prop.getProperty("smtpPort")));
            cb.setImapPort(Integer.parseInt(prop.getProperty("imapPort")));
            cb.setDatabaseUserName(prop.getProperty("databaseName"));
            cb.setDatabaseURL(prop.getProperty("databaseURL"));
            cb.setDatabasePassword(prop.getProperty("databasePassword"));
        }
        return cb;
    }
}
