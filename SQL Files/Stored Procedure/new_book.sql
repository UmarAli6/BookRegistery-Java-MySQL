CREATE DEFINER=`root`@`localhost` PROCEDURE `new_book`(OUT newBId INT, INOUT titleIn VARCHAR(50), INOUT isbnIn VARCHAR(13), INOUT publishedIn DATE, INOUT genreIn VARCHAR(30), OUT ratingOut DECIMAL(2,1), IN uIdIn INT)
    SQL SECURITY INVOKER
BEGIN
	INSERT INTO T_BOOK(title, isbn, published, genre, uId) VALUES (titleIn, isbnIn, publishedIn, genreIn, uIdIn);
    SELECT LAST_INSERT_ID() INTO newBId FOR UPDATE;
    SELECT 0.0 INTO ratingOut;
END