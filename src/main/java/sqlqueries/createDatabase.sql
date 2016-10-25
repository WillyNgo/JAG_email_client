DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS emails;

create table emails (
	messageNumber INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
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

INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', true, NOW(), 'sent');
INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'values present', 'Hello chicken', '', false, NOW(), 'inbox');
INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', true, NOW(), 'sent');
INSERT INTO emails (receiver, sender, cc, subject_text, message, html, typeFlag, receive_date, folder) VALUES ('williamngoreceive@gmail.com', 'williamngosend@gmail.com', 'shiftkun662@gmail.com', 'my banana', 'submarine taurus', '', false, NOW(), 'inbox');

