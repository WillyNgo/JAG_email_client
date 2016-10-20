DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS emails;
DROP TABLE IF EXISTS accounts;


CREATE TABLE accounts (
		account_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        account_username VARCHAR(255)NOT NULL,
        emailAddress VARCHAR(255) NOT NULL,
        account_password VARCHAR(255) NOT NULL
)ENGINE=InnoDB;

create table emails (
		messageNumber INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        email_account_id INT,
        receiver VARCHAR(255) NOT NULL,
        sender VARCHAR(255) NOT NULL,
        cc varchar(255),
        subject_text VARCHAR(255),
        message VARCHAR(255),
        html VARCHAR(255),
        typeFlag boolean,
        receive_date date,
        folder VARCHAR(255),
        CONSTRAINT fk_account_id FOREIGN KEY (email_account_id) REFERENCES accounts(account_id)
)ENGINE=InnoDB;


CREATE TABLE attachments(
	attachment_id int NOT NULL AUTO_INCREMENT,
    attach_messageNumber int,
    attachmentName varchar(255),
    attachmentByte mediumblob,
    PRIMARY KEY (attachment_id),
    CONSTRAINT fk_messagenumber FOREIGN KEY (attach_messageNumber) REFERENCES emails(messageNumber)
)ENGINE=InnoDB;

INSERT INTO accounts (account_username, emailAddress, account_password) VALUES ('sender', 'williamngosend@gmail.com', 'sendanemail');
INSERT INTO accounts (account_username, emailAddress, account_password) VALUES ('receiver', 'williamngoreceive@gmail.com', 'receiveanemail');

INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (1, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', true, NOW(), 'sent');
INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (2, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', false, NOW(), 'inbox');
INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (1, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', true, NOW(), 'sent');
INSERT INTO emails (email_account_id, receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES (2, 'williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', false, NOW(), 'inbox');
