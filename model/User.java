package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an user.
 *
 * @author Umar A & Rabi S
 */
public class User {
    private final int userId;
    private final String username;
    private final String password;
    
    private final ArrayList<Review> theReviews;
    
    public User(int userId, String username){
        this.userId = userId;
        this.username = username;
        this.password = "";
        this.theReviews = new ArrayList();
    }
    
    public User(String username, String password){
        this.userId = -1;
        this.username = username;
        this.password = password;
        this.theReviews = new ArrayList();
    }
    
    /**
     * Get the ID of the user
     *
     * @return an {@code int} with the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get the username of the user
     *
     * @return a {@code String} with the usernmae
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the users password
     *
     * @return a {@code String} with the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the review with the specified index
     *
     * @param index
     * @return a {@code Review} with the specified index
     */
    public Review getReview(int index) {
        return this.theReviews.get(index);
    }
    
    /**
     * Get the reviews the user has added
     *
     * @return a {@code Listt<Review>} with the reviews
     */
    public List<Review> getReviews() {
        return theReviews;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", username=" + username + ", password=" + password + ", reviews=" + theReviews + '}';
    }
}
