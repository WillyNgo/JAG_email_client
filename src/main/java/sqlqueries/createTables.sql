DROP TABLE IF EXISTS accounts;
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
        folder VARCHAR(255)
);



CREATE TABLE accounts (
		account_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
        account_username VARCHAR(255)NOT NULL,
        emailAddress VARCHAR(255) NOT NULL,
        account_password VARCHAR(255) NOT NULL
);