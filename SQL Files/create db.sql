CREATE DATABASE IF NOT EXISTS booksDb;
USE booksDb;

DROP TABLE IF EXISTS T_WRITES;
DROP TABLE IF EXISTS T_REVIEW;
DROP TABLE IF EXISTS T_BOOK;
DROP TABLE IF EXISTS T_AUTHOR;
DROP TABLE IF EXISTS T_USER;

CREATE TABLE IF NOT EXISTS T_USER (
	uId			INT NOT NULL AUTO_INCREMENT,
    username	VARCHAR(50) NOT NULL UNIQUE,
    password	VARCHAR(50) NOT NULL,
    PRIMARY KEY(uId)
);

CREATE TABLE IF NOT EXISTS T_BOOK (
	bId			INT 		 NOT NULL AUTO_INCREMENT,
    title		VARCHAR(50)	 NOT NULL,
    isbn		VARCHAR(13)  NOT NULL UNIQUE,
    published	DATE 		 NOT NULL,
    genre		ENUM('Fantasy','Romance','Adventure','Mystery','Thriller','ScienceFiction',
					 'Autobiography','Dystopian','Drama','Humor', 'Allegory', 'Satire', 
                     'Horror', 'Contemporary', 'Educational', 'History') NOT NULL,
    rating		DECIMAL(2,1) DEFAULT 0.0,
    uId			INT			 NOT NULL,
    nrOfRatings	INT 		 DEFAULT 0,
    sumRating	DECIMAL(5,0) DEFAULT 0.0,
    PRIMARY KEY (bId),
    FOREIGN KEY (uId) REFERENCES T_USER(uId)
);

CREATE TABLE IF NOT EXISTS T_AUTHOR (
	aId			INT NOT NULL AUTO_INCREMENT,
    name		VARCHAR(60) NOT NULL,
    birthdate	DATE NOT NULL,
    uId			INT	NOT NULL,
    PRIMARY KEY (aId),
    FOREIGN KEY (uId) REFERENCES T_USER(uId)
);

CREATE TABLE IF NOT EXISTS T_WRITES (
	bId			INT NOT NULL,
    aId			INT NOT NULL,
    PRIMARY KEY(bId,aId),
    FOREIGN KEY(bId) REFERENCES T_Book(bId),
    FOREIGN KEY(aId) REFERENCES T_Author(aId)
);

CREATE TABLE IF NOT EXISTS T_REVIEW (
	rId			INT 			NOT NULL AUTO_INCREMENT,
    bRating		INT				NOT NULL,
    rString		VARCHAR(1000)	NOT NULL,
    dateAdded	DATE			NOT NULL,
    bId			INT				NOT NULL,
	uId			INT				NOT NULL,
    PRIMARY KEY(rId),
	FOREIGN KEY(bId) REFERENCES T_Book(bId),
    FOREIGN KEY(uId) REFERENCES T_USER(uId)
);