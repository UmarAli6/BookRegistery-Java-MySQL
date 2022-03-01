package model;

import java.sql.Date;

/**
 * Representation of a review of a book.
 *
 * @author Umar A & Rabi S
 */
public class Review {

    private final int revId;
    private final double bRating;
    private final String revString;
    private final Date dateAdded;
    private final int bId;
    private final User user;

    public Review(int revId, double bRating, String revString, String dateAdded, int bId, User user) {
        this.revId = revId;
        this.bRating = bRating;
        this.revString = revString;
        this.dateAdded = Date.valueOf(dateAdded);
        this.bId = bId;
        this.user = user;
    }

    public Review(double bRating, String revString, String dateAdded, int bId) {
        this.revId = -1;
        this.bRating = bRating;
        this.revString = revString;
        this.dateAdded = Date.valueOf(dateAdded);
        this.bId = bId;
        this.user = null;
    }

    /**
     * Get the id of the review
     *
     * @return an {@code int} of the review ID
     */
    public int getRevId() {
        return this.revId;
    }

    /**
     * Get the rating of the book in the reivew
     *
     * @return an {@code int} of the review's book rating
     */
    public double getbRating() {
        return this.bRating;
    }
    
    /**
     * Get the rating string
     *
     * @return a {@code String} of the review string
     */
    public String getRevString() {
        return this.revString;
    }

    /**
     * Get the date the review was added
     *
     * @return an {@code Date} the review was added
     */
    public Date getDateAdded() {
        return this.dateAdded;
    }

    /**
     * Get the book ID of the book the review belongs to
     *
     * @return an {@code int} of the book ID the review belongs to
     */
    public int getbId() {
        return this.bId;
    }

    /**
     * Get the user that wrote the review
     *
     * @return an {@code User} that wrote the reviews
     */
    public User getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return "Review{" + "revId=" + revId + ", bRating=" + bRating + ", revString=" + revString + ", dateAdded=" + dateAdded + ", bId=" + bId + ", user=" + user + '}';
    }
    
}
