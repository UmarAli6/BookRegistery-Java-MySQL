CREATE DEFINER=`root`@`localhost` PROCEDURE `new_review`(OUT newRId INT, INOUT bRatingIn DECIMAL(2,1), INOUT rStringIn VARCHAR(1000), INOUT dateAddedIn DATE, INOUT bIdIn INT, INOUT uIdIn INT)
    SQL SECURITY INVOKER
BEGIN
    INSERT INTO T_REVIEW (bRating, rString, dateAdded, bId, uId) VALUES (bRatingIn, rStringIn, dateAddedIn, bIdIn, uIdIn);
    SELECT LAST_INSERT_ID() INTO newRId FOR UPDATE;
    
    UPDATE T_BOOK SET T_BOOK.sumRating = T_BOOK.sumRating + bRatingIn, T_BOOK.nrOfRatings = T_BOOK.nrOfRatings + 1 WHERE T_BOOK.bId = bIdIn;
    UPDATE T_BOOK SET T_BOOK.rating = sumRating/nrOfRatings WHERE T_BOOK.bId = bIdIn;
END