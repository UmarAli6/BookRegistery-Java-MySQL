package view;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javafx.application.Platform;
import static javafx.scene.control.Alert.AlertType.*;

import model.Book;
import model.User;
import model.BooksDbInterface;
import model.Review;
import model.SearchMode;

/**
 * The controller is responsible for handling user requests and update the view
 * (and in some cases the model).
 *
 * @author Umar A & Rabi S
 */
public final class Controller {

    private final DbView booksView; // view
    private final BooksDbInterface booksDb; // model

    public Controller(BooksDbInterface booksDb, DbView booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
        initLogin();
    }

    protected void onSearchSelected(String searchFor, SearchMode mode) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (searchFor != null && searchFor.length() > 0) {
                        List<Book> result;
                        switch (mode) {
                            case Title:
                                result = booksDb.searchBooksByTitle(searchFor);
                                break;
                            case ISBN:
                                result = booksDb.searchBooksByISBN(searchFor);
                                break;
                            case Author:
                                result = booksDb.searchBooksByAuthor(searchFor);
                                break;
                            case Genre:
                                result = booksDb.searchBooksByGenre(searchFor);
                                break;
                            default:
                                result = null;
                        }
                        if (result == null || result.isEmpty()) {
                            Platform.runLater(() -> booksView.showAlertAndWait("No results found", INFORMATION, "INFORMATION"));
                        } else {
                            Platform.runLater(() -> booksView.displayBooks(result));
                        }
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("Enter a search string", WARNING, "WARNING"));

                    }
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait("Database error\n" + ex.getLocalizedMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void onSearchRatingSelected(String min, String max) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (min != null && max != null) {
                        {
                            double minD = Double.parseDouble(min.replace(",", "."));
                            double maxD = Double.parseDouble(max.replace(",", "."));

                            if (minD >= 0.0 && maxD <= 5.0) {
                                List<Book> result = booksDb.searchBooksByRating(minD, maxD);

                                if (result == null || result.isEmpty()) {
                                    Platform.runLater(() -> booksView.showAlertAndWait("No results found", INFORMATION, "INFORMATION"));
                                } else {
                                    Platform.runLater(() -> booksView.displayBooks(result));
                                }
                            }
                        }
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("Enter a search string", WARNING, "WARNING"));
                    }
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void refreshBooksInView() {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<Book> list = booksDb.getTheBooks();
                    Platform.runLater(() -> booksView.displayBooks(list));
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void handleLoginAsGuestEvent() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (booksDb.isLoggedIn()) {
                        booksDb.loginAsGuest();
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You are already logged out", ERROR, "LOGOUT ERROR"));
                    }
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void initLogin() {
        new Thread() {
            @Override
            public void run() {
                try {
                    booksDb.loginAsGuest();
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void handleDisconnectEvent() {
        new Thread() {
            @Override
            public void run() {
                try {
                    booksDb.disconnect();
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void handleAddBookDialogEvent(Book book, boolean loggedCheck) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (booksDb.isLoggedIn() && !loggedCheck) {
                        Platform.runLater(() -> booksView.showAddBookDialog());
                    } else if (booksDb.isLoggedIn() && loggedCheck) {
                        booksDb.addBookToDb(book);
                        List<Book> list = booksDb.getTheBooks();
                        Platform.runLater(() -> booksView.displayBooks(list));
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You need to log in to add a book", INFORMATION, "NOT LOGGED IN"));
                    }
                } catch (SQLIntegrityConstraintViolationException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait("The book you are trying to add already exists", ERROR, "DUPLICATE ISBN"));
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void handleRemoveBookDialogEvent(Book book, boolean loggedCheck) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (booksDb.isLoggedIn() && !loggedCheck) {
                        Platform.runLater(() -> booksView.showRemoveBookDialog());
                    } else if (booksDb.isLoggedIn() && loggedCheck) {
                        booksDb.deleteBookFromDb(book);
                        List<Book> list = booksDb.getTheBooks();
                        Platform.runLater(() -> booksView.displayBooks(list));
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You need to log in to remove a book", INFORMATION, "NOT LOGGED IN"));
                    }

                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void handleAddReviewDialogEvent(Review review, boolean loggedCheck) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (booksDb.isLoggedIn() && !loggedCheck) {
                        Platform.runLater(() -> booksView.showAddReviewDialog());
                    } else if (booksDb.isLoggedIn() && loggedCheck) {
                        booksDb.addReviewToDb(review);
                        List<Book> list = booksDb.getTheBooks();
                        Platform.runLater(() -> booksView.displayBooks(list));
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You need to log in to review a book", INFORMATION, "NOT LOGGED IN"));
                    }

                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void handleInvalidInput(String invalidMsg) {
        booksView.showAlertAndWait(invalidMsg, ERROR, "INVALID INPUT");
    }

    protected void checkDuplicateAuthor(Book book, boolean loggedCheck) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (booksDb.isLoggedIn() && !loggedCheck) {
                        Platform.runLater(() -> booksView.showAddBookDialog());
                    } else if (booksDb.isLoggedIn() && loggedCheck) {
                        List<Book> dupAuthors = booksDb.checkDuplicateAuthor(book);
                        if (dupAuthors.size() > 1) {
                            Platform.runLater(() -> booksView.showDupAuthorsDialog(dupAuthors));
                        } else {
                            booksDb.addBookToDb(book);
                            List<Book> list = booksDb.getTheBooks();
                            Platform.runLater(() -> booksView.displayBooks(list));
                        }
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You need to log in to add a book", INFORMATION, "NOT LOGGED IN"));
                    }

                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected boolean isBookReviewed(Book book) {
        if (book.getReviews().size() > 0) {
            return true;
        } else {
            booksView.showAlertAndWait("This book has no reviews", INFORMATION, "No Reviews");
            return false;
        }
    }

    protected void isUsernameAvailable(User newUser, boolean loggedCheck) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (!booksDb.isLoggedIn() && !loggedCheck) {
                        Platform.runLater(() -> booksView.showCreateAccDialog());
                    } else if (!booksDb.isLoggedIn() && loggedCheck) {
                        if (booksDb.isUsernameAvailable(newUser)) {
                            booksDb.createAccToDb(newUser);
                        } else {
                            Platform.runLater(() -> booksView.showCreateAccDialog());
                            Platform.runLater(() -> booksView.showAlertAndWait("Username Taken", INFORMATION, ""));
                        }
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You are already logged in", INFORMATION, "ALREADY LOGGED IN"));
                    }
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void isUser(User user, boolean loggedCheck) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (!booksDb.isLoggedIn() && !loggedCheck) {
                        Platform.runLater(() -> booksView.showLoginDialog());
                    } else if (!booksDb.isLoggedIn() && loggedCheck) {
                        if (booksDb.isUser(user)) {
                            User current = booksDb.loginAsUser(user);
                            Platform.runLater(() -> booksView.setCurrentUsername(current.getUsername()));
                        } else {
                            Platform.runLater(() -> booksView.showLoginDialog());
                            Platform.runLater(() -> booksView.showAlertAndWait("Wrong username or password", ERROR, "ERROR"));
                        }
                    } else {
                        Platform.runLater(() -> booksView.showAlertAndWait("You are already logged in", INFORMATION, "ALREADY LOGGED IN"));
                    }
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }

    protected void isBookReviewedByUser(Book bookToBeReviewed) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (booksDb.isBookReviewedByUser(bookToBeReviewed)) {
                        Platform.runLater(() -> booksView.showAddReviewDialog());
                        Platform.runLater(() -> booksView.showAlertAndWait("You have already reviewed this book", INFORMATION, "CHOOSE A DIFFERENT BOOK"));
                    } else {
                        Platform.runLater(() -> booksView.showWriteReviewDialog(bookToBeReviewed));
                    }
                } catch (SQLException ex) {
                    Platform.runLater(() -> booksView.showAlertAndWait(ex.getMessage(), ERROR, "ERROR"));
                }
            }
        }.start();
    }
}
