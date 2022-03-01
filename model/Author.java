package model;

import java.sql.Date;

/**
 * Representation of an author.
 *
 * @author Umar A & Rabi S
 */
public class Author {

    private int authorId;
    private final String name;
    private final Date dateOfBirth;
    private final User user;

    public Author(int authorId, String name, String dateOfBirth, User user) {
        this.authorId = authorId;
        this.name = name;
        this.dateOfBirth = Date.valueOf(dateOfBirth);
        this.user = user;
    }
    
    public Author(String name, String dateOfBirth) {
        this.authorId = -1;
        this.name = name;
        this.dateOfBirth = Date.valueOf(dateOfBirth);
        this.user = null;
    }

    /**
     * Get the ID of the author
     *
     * @return an {@code int} of the book ID
     */
    public int getAuthorId() {
        return this.authorId;
    }

    /**
     * Get name of the author
     *
     * @return a {@code String} of the authors name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the author date of birth
     *
     * @return a {@code Date} of the authors date of birth
     */
    public Date getDateOfBirth() {
        return this.dateOfBirth;
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
     * Add an authorId to the author to the book.
     *
     * @param authorId
     */
    public void setAuthorId(int authorId){
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "Author{" + "authorId=" + authorId + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", user=" + user + '}';
    }
}
