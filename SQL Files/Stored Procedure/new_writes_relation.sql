CREATE DEFINER=`root`@`localhost` PROCEDURE `new_writes_relation`(IN bIdIn INT, INOUT aIdIn INT, OUT nameOut VARCHAR(60), OUT birthdateOut DATE, OUT uIdOut INT)
    SQL SECURITY INVOKER
BEGIN
	INSERT INTO T_WRITES (bId, aId) VALUES(bIdIn, aIdIn);
    
    SELECT 	name INTO nameOut
    FROM 	T_AUTHOR
    WHERE 	aId = aIdIn;
    
    SELECT 	birthdate INTO birthdateOut
    FROM	T_AUTHOR
    WHERE 	aId = aIdIn;
    
    SELECT 	uId INTO uIdOut
    FROM	T_AUTHOR
    WHERE 	aId = aIdIn;
END