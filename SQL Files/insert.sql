USE booksDb;

INSERT INTO T_User (username, password) VALUES ('muali', '123456');
INSERT INTO T_User (username, password) VALUES ('rabisu', '123456');
INSERT INTO T_User (username, password) VALUES ('booklover', '123456');
INSERT INTO T_User (username, password) VALUES ('booksaregreat', '123456');

INSERT INTO T_Book (title, isbn, published, genre, uId) VALUES ('Dune', '9780441172719', '1965-08-01', 'ScienceFiction', 1);
INSERT INTO T_Book (title, isbn, published, genre, uId) VALUES ('1984', '9780141036144', '1949-06-08', 'Dystopian', 2);
INSERT INTO T_Book (title, isbn, published, genre, uId) VALUES ('The Lord of the Rings', '9780544003415', '1954-07-29', 'Fantasy', 2);
INSERT INTO T_Book (title, isbn, published, genre, uId) VALUES ('The Life of Pi', '9780552779685', '2001-09-11', 'Drama', 1);
INSERT INTO T_Book (title, isbn, published, genre, uId) VALUES ('Good Omens', '9781473200852', '1990-05-10', 'Fantasy', 3);

INSERT INTO T_Author (name, birthdate, uId) VALUES ('Frank Herbert', '1920-10-08', 1);
INSERT INTO T_Author (name, birthdate, uId) VALUES ('George Orwell', '1903-06-25', 2);
INSERT INTO T_Author (name, birthdate, uId) VALUES ('J.R.R Tolkien', '1892-01-03', 2);
INSERT INTO T_Author (name, birthdate, uId) VALUES ('Yann Martel', '1963-06-25', 1);
INSERT INTO T_Author (name, birthdate, uId) VALUES ('Neil Gaiman', '1960-11-10', 3);
INSERT INTO T_Author (name, birthdate, uId) VALUES ('Terry Pratchett', '1948-04-25', 3);

INSERT INTO T_Writes (bId, aId) VALUES (1,1);
INSERT INTO T_Writes (bId, aId) VALUES (2,2);
INSERT INTO T_Writes (bId, aId) VALUES (3,3);
INSERT INTO T_Writes (bId, aId) VALUES (4,4);
INSERT INTO T_Writes (bId, aId) VALUES (5,5);
INSERT INTO T_Writes (bId, aId) VALUES (5,6);