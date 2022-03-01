USE booksDb;

DROP USER IF EXISTS 'anonClient'@'localhost';
DROP USER IF EXISTS 'userClient'@'localhost';

CREATE USER IF NOT EXISTS								   'anonClient'@'localhost' IDENTIFIED BY 'abc123456';
GRANT SELECT 		 ON T_WRITES						TO 'anonClient'@'localhost';
GRANT SELECT 		 ON T_REVIEW						TO 'anonClient'@'localhost';
GRANT SELECT 		 ON T_BOOK							TO 'anonClient'@'localhost';
GRANT SELECT 		 ON T_AUTHOR						TO 'anonClient'@'localhost';
GRANT SELECT, INSERT ON T_USER							TO 'anonClient'@'localhost';

CREATE USER IF NOT EXISTS								   'userClient'@'localhost' IDENTIFIED BY 'abc123456';
GRANT SELECT, INSERT, DELETE, UPDATE ON T_WRITES		TO 'userClient'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON T_REVIEW		TO 'userClient'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON T_BOOK			TO 'userClient'@'localhost';
GRANT SELECT, INSERT, DELETE, UPDATE ON T_AUTHOR		TO 'userClient'@'localhost';
GRANT SELECT 						 ON T_USER			TO 'userClient'@'localhost';

GRANT EXECUTE ON PROCEDURE get_authors					TO 'anonClient'@'localhost';
GRANT EXECUTE ON PROCEDURE new_user						TO 'anonClient'@'localhost';
GRANT EXECUTE ON PROCEDURE search_by_author				TO 'anonClient'@'localhost';
GRANT EXECUTE ON FUNCTION  check_dup_username			TO 'anonClient'@'localhost';
GRANT EXECUTE ON FUNCTION  check_is_user				TO 'anonClient'@'localhost';

GRANT EXECUTE ON PROCEDURE delete_book			 		TO 'userClient'@'localhost';
GRANT EXECUTE ON PROCEDURE get_authors			 		TO 'userClient'@'localhost';
GRANT EXECUTE ON PROCEDURE new_author_relation	 		TO 'userClient'@'localhost';
GRANT EXECUTE ON PROCEDURE new_book				 		TO 'userClient'@'localhost';
GRANT EXECUTE ON PROCEDURE new_review 			 		TO 'userClient'@'localhost';
GRANT EXECUTE ON PROCEDURE new_writes_relation	 		TO 'userClient'@'localhost';
GRANT EXECUTE ON PROCEDURE search_by_author		 		TO 'userClient'@'localhost';
GRANT EXECUTE ON PROCEDURE search_dup_authors	 		TO 'userClient'@'localhost';
GRANT EXECUTE ON FUNCTION  check_user_has_reviewed_book TO 'userClient'@'localhost';

FLUSH PRIVILEGES;