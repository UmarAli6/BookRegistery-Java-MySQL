package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a book.
 *
 * @author Umar A & Rabi S
 */
public class Book {

    private final int bookId;
    private final String title;
    private final String isbn;
    private final Date published;
    private final Genre genre;
    private final double rating;
    private final User user;

    private final ArrayList<Author> theAuthors;
    private final ArrayList<Review> theReviews;

    public Book(int bookId, String title, String isbn, String published, String genre, double rating, User user) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.rating = rating;
        this.published = Date.valueOf(published);
        this.genre = Genre.valueOf(genre);
        this.user = user;
        
        this.theAuthors = new ArrayList();
        this.theReviews = new ArrayList();
    }
    
    public Book(String title, String isbn, String published, String genre) {
        this.bookId = -1;
        this.title = title;
        this.isbn = isbn;
        this.rating = 0.0;
        this.published = Date.valueOf(published);
        this.genre = Genre.valueOf(genre);
        this.user = null;

        this.theAuthors = new ArrayList();
        this.theReviews = new ArrayList();
    }

    /**
     * Get the id of the book
     *
     * @return an {@code int} of the book ID
     */
    public int getBookId() {
        return this.bookId;
    }

    /**
     * Get the title of the book
     *
     * @return a {@code String} of the books title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the isbn of the book
     *
     * @return a {@code String} of the books isbn
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * Get the Date the book was published
     *
     * @return a {@code Date} of the books publish date
     */
    public Date getPublished() {
        return this.published;
    }

    /**
     * Get the genre of the book
     *
     * @return a {@code Genre} of the books genre
     */
    public Genre getGenre() {
        return this.genre;
    }

    /**
     * Get the rating of the book
     *
     * @return an {@code int} of the books rating
     */
    public double getRating() {
        return this.rating;
    }
    
    /**
     * Get the user that added the book
     *
     * @return the {@code User} that added the book
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Get the author with the specified index
     *
     * @param index
     * @return a {@code Author} with the specified index
     */
    public Author getAuthor(int index) {
        return this.theAuthors.get(index);
    }

    /**
     * Get the authors of the book
     *
     * @return a {@code List<Author>} of the books authors
     */
    public List<Author> getAuthors() {
        List<Author> temp = this.theAuthors;
        return temp;
    }
    
    /**
     * Get the reviews of the book
     *
     * @return a {@code List<Review>} of the books reviews
     */
    public List<Review> getReviews() {
        List<Review> temp = this.theReviews;
        return temp;
    }

    /**
     * Add an author to the book.
     *
     * @param author a {@code Author} representing the auhtor to be added.
     */
    public void addAuthor(Author author) {
        this.theAuthors.add(author);
    }

    /**
     * Add authors to the book.
     *
     * @param authors a {@code List<Author>} representing the auhtors to be added.
     */
    public void addAuthors(List<Author> authors) {
        for (int i = 0; i < authors.size(); i++) {
            this.theAuthors.add(authors.get(i));
        }
    }
    
    /**
     * Add an review to the book.
     *
     * @param review a {@code Review} representing the review to be added.
     */
    public void addReview(Review review) {
        this.theReviews.add(review);
    }

    @Override
    public String toString() {
        return "Book{" + "bookId=" + bookId + ", title=" + title + ", isbn=" + isbn + ", published=" + published + ", genre=" + genre + ", rating=" + rating + ", user=" + user + ", theAuthors=" + theAuthors + ", theReviews=" + theReviews + '}';
    }
}
