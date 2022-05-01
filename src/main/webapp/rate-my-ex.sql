CREATE DATABASE IF NOT EXISTS ratemyex;
USE ratemyex;

CREATE TABLE Username (
	email VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    userpassword VARCHAR(100) NOT NULL,
    PRIMARY KEY (email)
);
