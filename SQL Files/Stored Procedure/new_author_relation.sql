CREATE DEFINER=`root`@`localhost` PROCEDURE `new_author_relation`(OUT newAId INT, IN bIdIn INT, INOUT nameIn VARCHAR(60), INOUT birthdateIn DATE, IN uIdIn INT)
    SQL SECURITY INVOKER
BEGIN
	INSERT INTO T_AUTHOR(name, birthdate, uId) VALUES (nameIn, birthdateIn, uIdIn);
    SELECT LAST_INSERT_ID() INTO newAId FOR UPDATE;
    INSERT INTO T_WRITES(bId, aId) VALUES (bIdIn, newAId);
END