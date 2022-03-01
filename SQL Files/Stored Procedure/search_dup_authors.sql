CREATE DEFINER=`root`@`localhost` PROCEDURE `search_dup_authors`(IN nameIn VARCHAR(60))
    SQL SECURITY INVOKER
BEGIN
	SELECT	title, published, T_WRITES.aId, name, birthdate, T_AUTHOR.uId
    FROM	T_WRITES, T_AUTHOR, T_BOOK
    WHERE	name SOUNDS LIKE nameIn AND T_WRITES.aId = T_AUTHOR.aId 
			AND T_WRITES.bId = T_BOOK.bId;
END