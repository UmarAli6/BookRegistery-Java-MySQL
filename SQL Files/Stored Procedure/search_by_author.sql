CREATE DEFINER=`root`@`localhost` PROCEDURE `search_by_author`(IN nameIn VARCHAR(60))
    SQL SECURITY INVOKER
BEGIN
	SELECT	DISTINCT	T_WRITES.bId, title, isbn, published, genre, rating, T_BOOK.uId
    FROM				T_WRITES, T_BOOK, T_AUTHOR
    WHERE				(T_AUTHOR.name LIKE nameIn OR T_AUTHOR.name SOUNDS LIKE nameIn) AND T_WRITES.bId = T_BOOK.bId AND T_WRITES.aId = T_AUTHOR.aId; 
END