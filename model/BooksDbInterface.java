package model;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface declares methods for querying a Books database. Different
 * implementations of this interface handles the connection and queries to a
 * specific DBMS and database, for example a MySQL or a MongoDB database.
 *
 * @author Umar A & Rabi S
 */
public interface BooksDbInterface {

    /**
     * This method logs you in as an Guest, anon client.
     *
     * @return a {@code boolean} if the login was successful or not
     * 
     * @throws java.sql.SQLException
     */
    public boolean loginAsGuest() throws SQLException;

    /**
     * This method logs you in as the user that was inputted. It will
     * log you in as an user client and you will be able manage the books.
     *
     * @param user
     * 
     * @return a {@code boolean} if the login was successful or not
     * 
     * @throws java.sql.SQLException
     */
    public User loginAsUser(User user) throws SQLException;
    
    /**
     * This method closes the connection.
     *
     * @return a {@code boolean} if the disconnect was successful or not
     * 
     * @throws java.sql.SQLException
     */
    public boolean disconnect() throws SQLException;

    /**
     * This method gets the books from the database returns the data.
     *
     * @return a {@code List<Book>} with the book data.
     * 
     * @throws java.sql.SQLException
     */
    public List<Book> getTheBooks() throws SQLException;

    /**
     * This method inserts a complete book to the database.
     *
     * @param bookToBeAdded
     * 
     * @return the {@code Book} you added to the database.
     * 
     * @throws java.sql.SQLException
     */
    public Book addBookToDb(Book bookToBeAdded) throws SQLException;
    
    /**
     * This method searches and deletes the book in the database.
     *
     * @param bookToBeDeleted
     * 
     * @return the deleted {@code Book}
     * 
     * @throws java.sql.SQLException
     */
    public Book deleteBookFromDb(Book bookToBeDeleted) throws SQLException;

    /**
     * This method inserts a complete review to the database.
     *
     * @param reviewToBeAdded
     * 
     * @return the {@code Review} you added to the database.
     * 
     * @throws java.sql.SQLException
     */
    public Review addReviewToDb(Review reviewToBeAdded) throws SQLException;
    
    /**
     * This creates a new user in the database.
     *
     * @param userToBeAdded
     * 
     * @return the created {@code user}
     * 
     * @throws java.sql.SQLException
     */
    public User createAccToDb(User userToBeAdded) throws SQLException;

    /**
     * This method queries the database to check if an user already exists with 
     * the same username
     *
     * @param user
     * 
     * @return a {@code boolean} if the username is available or not
     * 
     * @throws java.sql.SQLException
     */
    public boolean isUsernameAvailable(User user) throws SQLException;

    /**
     * This method queries the database to check if the user exists
     * in the database and the password is correct.
     *
     * @param user

     * @return a {@code boolean} if the user is an user or not
     * 
     * @throws java.sql.SQLException
     */
    public boolean isUser(User user) throws  SQLException;

    /**
     * This method checks if the user is logged in as an userclient or
     * guestclient.
     *
     * @return a {@code boolean} if the user is an userclient
     * 
     * @throws java.sql.SQLException
     */
    public boolean isLoggedIn() throws SQLException;

    /**
     * This method checks if the book already has been reviewed by the
     * current logged in user.
     *
     * @param bookToBeReviewed
     * 
     * @return a {@code boolean} if the book has previously been reviewed or not
     * 
     * @throws java.sql.SQLException
     */
    public boolean isBookReviewedByUser(Book bookToBeReviewed) throws SQLException;
    
    /**
     * This method queries the database for duplicate authors by
     * searching by name.
     *
     * @param book
     * 
     * @return a {@code List<Book>} with the duplicate author result.
     * 
     * @throws java.sql.SQLException
     */
    public List<Book> checkDuplicateAuthor(Book book) throws SQLException;
    
     /**
     * This method queries the database by searching books by title.
     *
     * @param title
     * 
     * @return a {@code List<Book>} with the returned book results.
     * 
     * @throws java.sql.SQLException
     */
    public List<Book> searchBooksByTitle(String title) throws SQLException;

    /**
     * This method queries the database by searching books by isbn.
     *
     * @param isbn
     * 
     * @return a {@code List<Book>} with the returned book results.
     * 
     * @throws java.sql.SQLException
     */
    public List<Book> searchBooksByISBN(String isbn) throws SQLException;

    /**
     * This method queries the database by searching books by auhtor.
     *
     * @param author
     * 
     * @return a {@code List<Book>} with the returned book results.
     * 
     * @throws java.sql.SQLException
     */
    public List<Book> searchBooksByAuthor(String author) throws SQLException;

    /**
     * This method queries the database by searching books by the rating.
     * Max and min represent the interval the rating should be between.
     *
     * @param min
     * @param max
     * 
     * @return a {@code List<Book>} with the returned book results.
     * 
     * @throws java.sql.SQLException
     */
    public List<Book> searchBooksByRating(double min, double max) throws  SQLException;

    /**
     * This method queries the database by searching books by the genre.
     *
     * @param genre
     * 
     * @return a {@code List<Book>} with the returned book results.
     * 
     * @throws java.sql.SQLException
     */
    public List<Book> searchBooksByGenre(String genre) throws SQLException;
}
