CREATE DEFINER=`root`@`localhost` PROCEDURE `new_user`(OUT newUId INT, INOUT usernameIn VARCHAR(50), IN passwordIn VARCHAR(50))
    SQL SECURITY INVOKER
BEGIN
    INSERT INTO T_User (username, password) VALUES (usernameIn, passwordIn);
    SELECT LAST_INSERT_ID() INTO newUId FOR UPDATE;
END