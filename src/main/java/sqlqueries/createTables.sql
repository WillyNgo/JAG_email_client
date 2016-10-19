DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS emails;
DROP TABLE IF EXISTS accounts;


CREATE TABLE accounts (
		account_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        account_username VARCHAR(255)NOT NULL,
        emailAddress VARCHAR(255) NOT NULL,
        account_password VARCHAR(255) NOT NULL
);

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
);


CREATE TABLE attachments(
	attachment_id int NOT NULL AUTO_INCREMENT,
    messageNumber int NOT NULL,
    attachName varchar(255),
    attachmentByte mediumblob,
    PRIMARY KEY (attachment_id),
    CONSTRAINT fk_messagenumber FOREIGN KEY (messageNumber) REFERENCES emails(messageNumber)
);