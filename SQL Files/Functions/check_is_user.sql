CREATE DEFINER=`root`@`localhost` FUNCTION `check_is_user`(usernameIn VARCHAR(50), passwordIn VARCHAR(50)) RETURNS tinyint(1)
    READS SQL DATA
    SQL SECURITY INVOKER
BEGIN
	IF EXISTS(SELECT * FROM	T_USER WHERE UPPER(username) = UPPER(usernameIn) AND password = passwordIn)
		THEN RETURN TRUE;
		ELSE RETURN FALSE;
    END IF;
    
END