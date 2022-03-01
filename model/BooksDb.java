package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;

/**
 * An implementation of the BooksDBInterface interface.
 *
 * @author Umar A & Rabi S
 */
public class BooksDb implements BooksDbInterface {

    private Connection connection;
    private User currentUser;

    private CallableStatement insertBookCstmt;
    private CallableStatement insertAuthorCstmt;
    private CallableStatement insertWritesCstmt;
    private CallableStatement insertUserCstmt;
    private CallableStatement insertReviewCstmt;

    private CallableStatement deleteBookCstmt;

    private PreparedStatement getBooksStmt;
    private PreparedStatement getBooksByTitleStmt;
    private PreparedStatement getBooksByIsbnStmt;
    private PreparedStatement getBooksByGenreStmt;
    private PreparedStatement getBooksByRatingStmt;

    private PreparedStatement getBookByIdStmt;
    private PreparedStatement getUsersStmt;
    private PreparedStatement getReviewsStmt;
    private PreparedStatement getLoggedInUserStmt;

    private CallableStatement getBooksByAuthorCstmt;
    private CallableStatement getAuthorsCstmt;
    private CallableStatement searchDupAuthorsCstmt;

    private CallableStatement checkDupUsernameCstmt;
    private CallableStatement checkIsUserCstmt;
    private CallableStatement checkIsReviewedByUserCstmt;

    private String database;
    private String server;

    private String anonClient;
    private String anonpwd;

    private String userClient;
    private String userpwd;

    public BooksDb() {
        connection = null;
        currentUser = null;
        initConStrings();
    }

    private void initConStrings() {
        database = "booksDb";
        server = "jdbc:mysql://localhost:3306/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useClientEnc=UTF8";
        server = "";


        anonClient = "anonClient";
        anonpwd = "abc123456";

        userClient = "userClient";
        userpwd = "abc123456";
    }

    private void initAnonStatements() throws SQLException {
        insertUserCstmt = connection.prepareCall("{call new_user(?,?,?)}");
        insertUserCstmt.registerOutParameter(1, Types.INTEGER);
        insertUserCstmt.registerOutParameter(2, Types.VARCHAR);

        getBooksStmt = connection.prepareStatement("SELECT * FROM T_BOOK");
        getBooksByTitleStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE title LIKE ?");
        getBooksByIsbnStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE isbn LIKE ?");
        getBooksByGenreStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE genre = ?");
        getBooksByRatingStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE rating >= ? AND rating <= ?");

        getUsersStmt = connection.prepareStatement("SELECT uId, username FROM T_USER WHERE uId = ?");
        getBooksByAuthorCstmt = connection.prepareCall("{call search_by_author(?)}");

        getReviewsStmt = connection.prepareStatement("SELECT * FROM T_REVIEW WHERE T_REVIEW.bId = ?");

        getAuthorsCstmt = connection.prepareCall("{call get_authors(?)}");

        checkDupUsernameCstmt = connection.prepareCall("{? = call check_dup_username(?)}");
        checkDupUsernameCstmt.registerOutParameter(1, Types.BOOLEAN);

        checkIsUserCstmt = connection.prepareCall("{? = call check_is_user(?, ?)}");
        checkIsUserCstmt.registerOutParameter(1, Types.BOOLEAN);

        getLoggedInUserStmt = connection.prepareStatement("SELECT uId, username FROM T_USER WHERE UPPER(username) = UPPER(?) AND password = ?");
    }

    private void initUserStatements() throws SQLException {
        insertBookCstmt = connection.prepareCall("{call new_book(?,?,?,?,?,?,?)}");
        insertBookCstmt.registerOutParameter(1, Types.INTEGER);
        insertBookCstmt.registerOutParameter(2, Types.VARCHAR);
        insertBookCstmt.registerOutParameter(3, Types.VARCHAR);
        insertBookCstmt.registerOutParameter(4, Types.DATE);
        insertBookCstmt.registerOutParameter(5, Types.VARCHAR);
        insertBookCstmt.registerOutParameter(6, Types.DECIMAL);

        insertAuthorCstmt = connection.prepareCall("{call new_author_relation(?,?,?,?,?)}");
        insertAuthorCstmt.registerOutParameter(1, Types.INTEGER);
        insertAuthorCstmt.registerOutParameter(3, Types.VARCHAR);
        insertAuthorCstmt.registerOutParameter(4, Types.DATE);

        insertWritesCstmt = connection.prepareCall("{call new_writes_relation(?,?,?,?,?)}");
        insertWritesCstmt.registerOutParameter(2, Types.INTEGER);
        insertWritesCstmt.registerOutParameter(3, Types.VARCHAR);
        insertWritesCstmt.registerOutParameter(4, Types.DATE);
        insertWritesCstmt.registerOutParameter(5, Types.INTEGER);

        insertReviewCstmt = connection.prepareCall("{call new_review(?,?,?,?,?,?)}");
        insertReviewCstmt.registerOutParameter(1, Types.INTEGER);
        insertReviewCstmt.registerOutParameter(2, Types.INTEGER);
        insertReviewCstmt.registerOutParameter(3, Types.VARCHAR);
        insertReviewCstmt.registerOutParameter(4, Types.DATE);
        insertReviewCstmt.registerOutParameter(5, Types.INTEGER);
        insertReviewCstmt.registerOutParameter(6, Types.INTEGER);

        checkIsReviewedByUserCstmt = connection.prepareCall("{? = call check_user_has_reviewed_book(?,?)}");
        checkIsReviewedByUserCstmt.registerOutParameter(1, Types.BOOLEAN);

        getBooksStmt = connection.prepareStatement("SELECT * FROM T_BOOK");
        getBookByIdStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE bId = ?");
        getBooksByTitleStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE title LIKE ?");
        getBooksByIsbnStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE isbn LIKE ?");
        getBooksByGenreStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE genre = ?");
        getBooksByRatingStmt = connection.prepareStatement("SELECT * FROM T_BOOK WHERE rating >= ? AND rating <= ?");

        getUsersStmt = connection.prepareStatement("SELECT uId, username FROM T_USER WHERE uId = ?");

        getBooksByAuthorCstmt = connection.prepareCall("{call search_by_author(?)}");

        getReviewsStmt = connection.prepareStatement("SELECT * FROM T_REVIEW WHERE T_REVIEW.bId = ?");

        getAuthorsCstmt = connection.prepareCall("{call get_authors(?)}");

        searchDupAuthorsCstmt = connection.prepareCall("{call search_dup_authors(?)}");

        deleteBookCstmt = connection.prepareCall("{call delete_book(?,?)}");

    }

    @Override
    public synchronized boolean loginAsGuest() throws SQLException {
        try {
            //connection = DriverManager.getConnection(server, anonClient, anonpwd);
            currentUser = null;

            initAnonStatements();
            return true;
        } catch (SQLException ex) {
            throw ex;
        }
    }

    @Override
    public synchronized User loginAsUser(User user) throws SQLException {
        ResultSet resUser = null;
        try {
            getLoggedInUserStmt.clearParameters();
            getLoggedInUserStmt.setString(1, user.getUsername());
            getLoggedInUserStmt.setString(2, user.getPassword());

            resUser = getLoggedInUserStmt.executeQuery();

            resUser.next();
            currentUser = new User(resUser.getInt(1), resUser.getString(2));

            connection = DriverManager.getConnection(server, userClient, userpwd);
            initUserStatements();

            return currentUser;
        } finally {
            if (resUser != null) {
                resUser.close();
            }
        }
    }

    @Override
    public synchronized boolean disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
            return true;
        }
        return false;
    }

    @Override
    public synchronized List<Book> getTheBooks() throws SQLException {
        ResultSet resBook = null;
        try {
            resBook = getBooksStmt.executeQuery();

            List<Book> list = getQueriedBooks(resBook);
            return list;

        } finally {
            if (resBook != null) {
                resBook.close();
            }
        }
    }

    @Override
    public synchronized Book addBookToDb(Book bookToBeAdded) throws SQLException {
        ResultSet resUser = null;
        try {
            connection.setAutoCommit(false);

            insertBookCstmt.clearParameters();
            insertBookCstmt.setString(2, bookToBeAdded.getTitle());
            insertBookCstmt.setString(3, bookToBeAdded.getIsbn());
            insertBookCstmt.setDate(4, bookToBeAdded.getPublished());
            insertBookCstmt.setString(5, bookToBeAdded.getGenre().name());
            insertBookCstmt.setInt(7, currentUser.getUserId());
            insertBookCstmt.execute();

            int bookId = insertBookCstmt.getInt(1);

            Book newBookfromDb = new Book(bookId, insertBookCstmt.getString(2), insertBookCstmt.getString(3), insertBookCstmt.getString(4), insertBookCstmt.getString(5), insertBookCstmt.getDouble(6), currentUser);

            for (int i = 0; i < bookToBeAdded.getAuthors().size(); i++) {
                Author author = bookToBeAdded.getAuthor(i);
                if (-1 == author.getAuthorId()) {
                    insertAuthorCstmt.setInt(2, bookId);
                    insertAuthorCstmt.setString(3, author.getName());
                    insertAuthorCstmt.setDate(4, author.getDateOfBirth());
                    insertAuthorCstmt.setInt(5, currentUser.getUserId());
                    insertAuthorCstmt.execute();
                    newBookfromDb.addAuthor(new Author(insertAuthorCstmt.getInt(1), insertAuthorCstmt.getString(3), insertAuthorCstmt.getString(4), currentUser));
                } else {
                    insertWritesCstmt.setInt(1, bookId);
                    insertWritesCstmt.setInt(2, author.getAuthorId());
                    insertWritesCstmt.execute();

                    getUsersStmt.clearParameters();
                    getUsersStmt.setInt(1, insertWritesCstmt.getInt(5));
                    resUser = getUsersStmt.executeQuery();

                    resUser.next();
                    User authorAddedByUser = new User(resUser.getInt(1), resUser.getString(2));
                    newBookfromDb.addAuthor(new Author(insertWritesCstmt.getInt(2), insertWritesCstmt.getString(3), insertWritesCstmt.getString(4), authorAddedByUser));
                }
            }

            connection.commit();
            return newBookfromDb;
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            if (resUser != null) {
                resUser.close();
            }
            connection.setAutoCommit(true);
        }
    }

    @Override
    public synchronized Book deleteBookFromDb(Book bookToBeDeleted) throws SQLException {
        ResultSet resBook = null;
        try {
            connection.setAutoCommit(false);

            getBookByIdStmt.clearParameters();
            getBookByIdStmt.setInt(1, bookToBeDeleted.getBookId());
            resBook = getBookByIdStmt.executeQuery();
            
            Book bookFromDb = getQueriedBooks(resBook).get(0);

            deleteBookCstmt.clearParameters();
            deleteBookCstmt.setInt(1, bookToBeDeleted.getBookId());
            deleteBookCstmt.setInt(2, currentUser.getUserId());
            deleteBookCstmt.execute();

            connection.commit();
            return bookFromDb;
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            if (resBook != null) {
                resBook.close();
            }
            connection.setAutoCommit(true);
        }
    }

    @Override
    public synchronized Review addReviewToDb(Review newReview) throws SQLException {
        try {
            connection.setAutoCommit(false);

            insertReviewCstmt.clearParameters();
            insertReviewCstmt.setDouble(2, newReview.getbRating());
            insertReviewCstmt.setString(3, newReview.getRevString());
            insertReviewCstmt.setDate(4, newReview.getDateAdded());
            insertReviewCstmt.setInt(5, newReview.getbId());
            insertReviewCstmt.setInt(6, currentUser.getUserId());
            insertReviewCstmt.execute();

            Review newReviewFromDb = new Review(insertReviewCstmt.getInt(1), insertReviewCstmt.getDouble(2), insertReviewCstmt.getString(3), insertReviewCstmt.getString(4), insertReviewCstmt.getInt(5), currentUser);

            connection.commit();
            return newReviewFromDb;
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public synchronized User createAccToDb(User user) throws SQLException {
        try {
            connection.setAutoCommit(false);

            insertUserCstmt.clearParameters();
            insertUserCstmt.setString(2, user.getUsername());
            insertUserCstmt.setString(3, user.getPassword());
            insertUserCstmt.execute();

            User newUserfromDb = new User(insertUserCstmt.getInt(1), insertUserCstmt.getString(2));

            connection.commit();
            return newUserfromDb;
        } catch (SQLException ex) {
            if (connection != null) {
                connection.rollback();
            }
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public synchronized boolean isUsernameAvailable(User user) throws SQLException {
        checkDupUsernameCstmt.clearParameters();
        checkDupUsernameCstmt.setString(2, user.getUsername());
        checkDupUsernameCstmt.execute();

        return checkDupUsernameCstmt.getBoolean(1);
    }

    @Override
    public synchronized boolean isUser(User user) throws SQLException {
        checkIsUserCstmt.clearParameters();
        checkIsUserCstmt.setString(2, user.getUsername());
        checkIsUserCstmt.setString(3, user.getPassword());
        checkIsUserCstmt.execute();

        return checkIsUserCstmt.getBoolean(1);
    }

    @Override
    public synchronized boolean isLoggedIn() throws SQLException {
        return connection.getMetaData().getUserName().equals(userClient + "@localhost");
    }

    @Override
    public boolean isBookReviewedByUser(Book bookToBeReviewed) throws SQLException {
        checkIsReviewedByUserCstmt.clearParameters();
        checkIsReviewedByUserCstmt.setInt(2, bookToBeReviewed.getBookId());
        checkIsReviewedByUserCstmt.setInt(3, currentUser.getUserId());
        checkIsReviewedByUserCstmt.execute();

        return checkIsReviewedByUserCstmt.getBoolean(1);
    }

    @Override
    public synchronized List<Book> checkDuplicateAuthor(Book book) throws SQLException {

        ResultSet resDupAuthor = null;
        try {
            ArrayList<Book> matches = new ArrayList();
            int aId = -1;
            for (int i = 0; i < book.getAuthors().size(); i++) {
                searchDupAuthorsCstmt.clearParameters();
                searchDupAuthorsCstmt.setString(1, book.getAuthors().get(i).getName());
                searchDupAuthorsCstmt.execute();

                resDupAuthor = searchDupAuthorsCstmt.getResultSet();

                while (resDupAuthor.next()) {
                    if (aId != resDupAuthor.getInt(3)) {
                        aId = resDupAuthor.getInt(3);
                        Book bookMatch = new Book(resDupAuthor.getString(1), "", resDupAuthor.getString(2), Genre.Adventure.name());
                        bookMatch.addAuthor(new Author(resDupAuthor.getInt(3), resDupAuthor.getString(4), resDupAuthor.getString(5), null));
                        book.getAuthor(i).setAuthorId(aId);
                        matches.add(bookMatch);
                    }
                }
            }
            matches.add(book);
            return matches;
        } finally {
            if (resDupAuthor != null) {
                resDupAuthor.close();
            }
        }
    }

    @Override
    public synchronized List<Book> searchBooksByTitle(String title) throws SQLException {
        ResultSet resBook = null;
        try {
            getBooksByTitleStmt.clearParameters();
            getBooksByTitleStmt.setString(1, "%" + title + "%");
            resBook = getBooksByTitleStmt.executeQuery();

            List<Book> list = getQueriedBooks(resBook);
            return list;
        } finally {
            if (resBook != null) {
                resBook.close();
            }
        }
    }

    @Override
    public synchronized List<Book> searchBooksByISBN(String isbn) throws SQLException {
        ResultSet resBook = null;
        try {
            getBooksByIsbnStmt.clearParameters();
            getBooksByIsbnStmt.setString(1, isbn + "%");
            resBook = getBooksByIsbnStmt.executeQuery();

            List<Book> list = getQueriedBooks(resBook);
            return list;
        } finally {
            if (resBook != null) {
                resBook.close();
            }
        }
    }

    @Override
    public synchronized List<Book> searchBooksByAuthor(String author) throws SQLException {
        ResultSet resBook = null;
        try {

            getBooksByAuthorCstmt.clearParameters();
            getBooksByAuthorCstmt.setString(1, "%" + author + "%");
            getBooksByAuthorCstmt.execute();
            resBook = getBooksByAuthorCstmt.getResultSet();

            List<Book> list = getQueriedBooks(resBook);
            return list;
        } finally {
            if (resBook != null) {
                resBook.close();
            }
        }
    }

    @Override
    public synchronized List<Book> searchBooksByRating(double min, double max) throws SQLException {
        ResultSet resBook = null;
        try {
            getBooksByRatingStmt.clearParameters();
            getBooksByRatingStmt.setDouble(1, min);
            getBooksByRatingStmt.setDouble(2, max);
            resBook = getBooksByRatingStmt.executeQuery();

            List<Book> list = getQueriedBooks(resBook);
            return list;
        } finally {
            if (resBook != null) {
                resBook.close();
            }
        }
    }

    @Override
    public synchronized List<Book> searchBooksByGenre(String genre) throws SQLException {
        ResultSet resBook = null;
        try {
            getBooksByGenreStmt.clearParameters();
            getBooksByGenreStmt.setString(1, genre);
            resBook = getBooksByGenreStmt.executeQuery();

            List<Book> list = getQueriedBooks(resBook);
            return list;
        } finally {
            if (resBook != null) {
                resBook.close();
            }
        }
    }

    private List<Book> getQueriedBooks(ResultSet resBook) throws SQLException {
        ResultSet resUser = null;
        ResultSet resAuthor = null;
        ResultSet resReview = null;

        try {
            ArrayList<Book> list = new ArrayList();
            while (resBook.next()) {
                int bookId = resBook.getInt(1);
                int bookUserId = resBook.getInt(7);

                getUsersStmt.clearParameters();
                getUsersStmt.setInt(1, bookUserId);
                resUser = getUsersStmt.executeQuery();

                resUser.next();
                User bookAddedByUser = new User(resUser.getInt(1), resUser.getString(2));

                Book book = new Book(bookId, resBook.getString(2), resBook.getString(3), resBook.getString(4), resBook.getString(5), resBook.getDouble(6), bookAddedByUser);

                getAuthorsCstmt.clearParameters();
                getAuthorsCstmt.setInt(1, bookId);
                getAuthorsCstmt.execute();

                resAuthor = getAuthorsCstmt.getResultSet();
                while (resAuthor.next()) {
                    getUsersStmt.clearParameters();
                    getUsersStmt.setInt(1, resAuthor.getInt(4));
                    resUser = getUsersStmt.executeQuery();

                    resUser.next();
                    User authorAddedByUser = new User(resUser.getInt(1), resUser.getString(2));

                    Author newAuthor = new Author(resAuthor.getInt(1), resAuthor.getString(2), resAuthor.getString(3), authorAddedByUser);
                    book.addAuthor(newAuthor);
                }

                getReviewsStmt.clearParameters();
                getReviewsStmt.setInt(1, bookId);
                resReview = getReviewsStmt.executeQuery();

                while (resReview.next()) {
                    getUsersStmt.clearParameters();
                    getUsersStmt.setInt(1, resReview.getInt(6));
                    resUser = getUsersStmt.executeQuery();

                    resUser.next();
                    User reviewAddedByUser = new User(resUser.getInt(1), resUser.getString(2));

                    Review newReview = new Review(resReview.getInt(1), resReview.getInt(2), resReview.getString(3), resReview.getString(4), resReview.getInt(5), reviewAddedByUser);
                    book.addReview(newReview);
                }
                list.add(book);

            }
            return list;
        } finally {
            if (resAuthor != null) {
                resAuthor.close();
            }
            if (resReview != null) {
                resReview.close();
            }
            if (resUser != null) {
                resUser.close();
            }
        }
    }

}
