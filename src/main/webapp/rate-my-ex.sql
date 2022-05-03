CREATE DATABASE IF NOT EXISTS ratemyex;
USE ratemyex;

CREATE TABLE Username (
	email VARCHAR(100) NOT NULL,
	userid INT(100) NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    userpassword VARCHAR(100) NOT NULL,
    PRIMARY KEY (userid)
);

CREATE TABLE Person (
	personid INT(100) NOT NULL,
	name VARCHAR(100) NOT NULL,
	gender VARCHAR(100) NOT NULL,
	overall_rating DOUBLE(100, 2) NOT NULL,
	rating_count INT(100) NOT NULL,
	PRIMARY KEY (personid)
);

CREATE TABLE Post (
	postid INT(100) NOT NULL AUTO_INCREMENT,
	personid INT(100) NOT NULL,
	userid INT(100) NOT NULL,
	description VARCHAR(2000) NOT NULL,
	rating DOUBLE(100, 2) NOT NULL,
	PRIMARY KEY (postid)
);