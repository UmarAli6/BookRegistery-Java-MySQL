package view;

import model.SearchMode;
import model.Book;
import model.BooksDb;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Genre;
import model.Review;
import model.User;

/**
 * The main pane for the view, extending VBox and including the menus. An
 * internal BorderPane holds the TableView for books and a search utility.
 *
 * @author Umar A & Rabi S
 */
public class DbView extends VBox {

    private final Controller controller;
    private TableView<Book> booksTable;
    private ObservableList<Book> booksInTable; // the data backing the table view

    private ComboBox<SearchMode> searchModeBox;
    private TextField searchField;
    private Button searchButton;
    private Button returnButton;
    private Button reviewButton;
    private Label currentUser;

    private MenuBar menuBar;

    protected AddBookDialog addBookDialog;
    protected RemoveBookDialog removeBookDialog;
    protected CreateAccDialog createAccDialog;
    protected LoginDialog loginDialog;
    protected AddReviewDialog addReviewDialog;
    protected DisplayReviewsDialog displayReviewsDialog;

    public DbView(BooksDb booksDb) {
        controller = new Controller(booksDb, this);
        this.init();
        controller.refreshBooksInView();
    }

    /**
     * Display a new set of books, e.g. from a database select, in the
     * booksTable table view.
     *
     * @param books the books to display
     */
    protected void displayBooks(List<Book> books) {
        booksInTable.clear();
        booksInTable.addAll(books);
    }

    /**
     * Notify user on input error or exceptions.
     *
     * @param msg the message
     * @param type types: ERROR, WARNING et c.
     * @param headerTxt
     */
    protected void showAlertAndWait(String msg, Alert.AlertType type, String headerTxt) {
        // types: INFORMATION, WARNING et c.
        Alert alert = new Alert(type, msg);
        alert.setHeaderText(headerTxt);
        alert.showAndWait();
    }

    private void init() {
        booksInTable = FXCollections.observableArrayList();

        // init views and event handlers
        FlowPane bottomPane = new FlowPane();

        GridPane buttonPane = new GridPane();
        buttonPane.setVgap(5.0);
        buttonPane.setHgap(5.0);

        initBooksTable();
        initMenus();
        initSearchView(bottomPane, buttonPane);

        bottomPane.setHgap(10);
        bottomPane.setPadding(new Insets(10, 10, 10, 10));
        bottomPane.getChildren().addAll(searchModeBox, searchField, buttonPane);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(booksTable);
        mainPane.setBottom(bottomPane);
        mainPane.setPadding(new Insets(0, 10, 10, 10));

        currentUser = new Label("  User: Guest");
        currentUser.setPrefHeight(50);

        this.getChildren().addAll(menuBar, currentUser, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
    }

    private void initBooksTable() {
        booksTable = new TableView<>();
        booksTable.setEditable(false); // don't allow user updates (yet)

        // define columns
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, Date> publishedCol = new TableColumn<>("Published");
        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        TableColumn<Book, String> ratingCol = new TableColumn<>("Rating");
        TableColumn<Book, String> bookAddedByCol = new TableColumn<>("Added by");

        TableColumn<Book, String> authorNameCol = new TableColumn<>("Author(s)");
        TableColumn<Book, String> authorDobCol = new TableColumn<>("Birth Date");
        TableColumn<Book, String> authorAddedBy = new TableColumn<>("Added by");

        booksTable.getColumns().addAll(titleCol, isbnCol, publishedCol, genreCol, ratingCol, bookAddedByCol, authorNameCol, authorDobCol, authorAddedBy);
        //give columns some extra space and center
        titleCol.setPrefWidth(230.0);
        isbnCol.setPrefWidth(130.0);
        publishedCol.setPrefWidth(110.0);
        genreCol.setPrefWidth(110.0);
        ratingCol.setPrefWidth(70.0);
        bookAddedByCol.setPrefWidth(100.0);

        authorNameCol.setPrefWidth(230.0);
        authorDobCol.setPrefWidth(110.0);
        authorAddedBy.setPrefWidth(100.0);

        titleCol.setStyle("-fx-alignment: CENTER;");
        isbnCol.setStyle("-fx-alignment: CENTER;");
        publishedCol.setStyle("-fx-alignment: CENTER;");
        genreCol.setStyle("-fx-alignment: CENTER;");
        ratingCol.setStyle("-fx-alignment: CENTER;");
        bookAddedByCol.setStyle("-fx-alignment: CENTER;");

        authorNameCol.setStyle("-fx-alignment: CENTER;");
        authorDobCol.setStyle("-fx-alignment: CENTER;");
        authorAddedBy.setStyle("-fx-alignment: CENTER;");

        // define how to fill data for each cell, 
        // get values from Book properties
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("published"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        ratingCol.setCellValueFactory(new Callback<CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Book, String> data) {
                if (data.getValue().getRating() == 0.0) {
                    return new SimpleStringProperty("Not Set");
                }
                return new SimpleStringProperty(data.getValue().getRating() + "/5.0");
            }
        }
        );

        bookAddedByCol.setCellValueFactory(
                new Callback<CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Book, String> data) {
                return new SimpleStringProperty(data.getValue().getUser().getUsername());
            }
        }
        );

        authorNameCol.setCellValueFactory(
                new Callback<CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Book, String> data) {
                String authorNames = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorNames += data.getValue().getAuthors().get(i).getName() + "\n";
                }
                return new SimpleStringProperty(authorNames);
            }
        }
        );

        authorDobCol.setCellValueFactory(
                new Callback<CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Book, String> data
            ) {
                String authorDobs = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorDobs += data.getValue().getAuthors().get(i).getDateOfBirth() + "\n";
                }
                return new SimpleStringProperty(authorDobs);
            }
        }
        );

        authorAddedBy.setCellValueFactory(
                new Callback<CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Book, String> data
            ) {
                String authorUsernames = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorUsernames += data.getValue().getAuthor(i).getUser().getUsername() + "\n";
                }
                return new SimpleStringProperty(authorUsernames);
            }
        }
        );

        // associate the table view with the data
        booksTable.setItems(booksInTable);

    }

    private void initMenus() {

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem createAccItem = new MenuItem("Create Account");
        MenuItem loginUserItem = new MenuItem("Login as User");
        MenuItem logoutItem = new MenuItem("Log out");
        fileMenu.getItems().addAll(exitItem, loginUserItem, createAccItem, logoutItem);

        Menu manageMenu = new Menu("Manage Books");
        MenuItem addItem = new MenuItem("Add Book");
        MenuItem removeItem = new MenuItem("Remove Book");
        MenuItem reviewItem = new MenuItem("Review Book");
        manageMenu.getItems().addAll(addItem, removeItem, reviewItem);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, manageMenu);

        logoutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleLoginAsGuestEvent();
                currentUser.setText("  User: Guest");
            }
        });

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleDisconnectEvent();
                Platform.exit();
            }
        });

        addItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.checkDuplicateAuthor(null, false);
            }
        });

        removeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleRemoveBookDialogEvent(null, false);
            }
        });

        reviewItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.handleAddReviewDialogEvent(null, false);
            }
        });

        loginUserItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.isUser(null, false);
            }
        });

        createAccItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.isUsernameAvailable(null, false);
            }
        });
    }
    
    protected void setCurrentUsername(String username){
        currentUser.setText("  User: "+username);
    }

    protected void showWriteReviewDialog(Book bookToReview) {
        WriteReviewDialog writeReviewDialog = new WriteReviewDialog(controller, bookToReview);
        Optional<Review> result = writeReviewDialog.showAndWait();

        if (result.isPresent()) {
            controller.handleAddReviewDialogEvent(result.get(), true);
        }
    }

    protected void showAddBookDialog() {
        addBookDialog = new AddBookDialog(controller);
        Optional<Book> result = addBookDialog.showAndWait();

        if (result.isPresent()) {
            controller.checkDuplicateAuthor(result.get(), true);
        }
    }

    protected void showRemoveBookDialog() {
        removeBookDialog = new RemoveBookDialog(controller, booksInTable);
        Optional<Book> result = removeBookDialog.showAndWait();

        if (result.isPresent()) {
            controller.handleRemoveBookDialogEvent(result.get(), true);
        }
    }

    protected void showAddReviewDialog() {
        addReviewDialog = new AddReviewDialog(controller, booksInTable);
        Optional<Book> result = addReviewDialog.showAndWait();

        if (result.isPresent()) {
            controller.isBookReviewedByUser(result.get());
        }
    }

    protected void showLoginDialog() {
        loginDialog = new LoginDialog(controller);
        Optional<User> result = loginDialog.showAndWait();

        if (result.isPresent()) {
            controller.isUser(result.get(), true);
        }
    }

    protected void showCreateAccDialog() {
        createAccDialog = new CreateAccDialog(controller);
        Optional<User> result = createAccDialog.showAndWait();

        if (result.isPresent()) {
            controller.isUsernameAvailable(result.get(), true);
        }
    }

    protected void showDupAuthorsDialog(List<Book> dupAuthors) {
        DuplicateAuthorDialog dupAuthorDialog = new DuplicateAuthorDialog(controller, dupAuthors);
        Optional<Book> result = dupAuthorDialog.showAndWait();

        if (result.isPresent()) {
            controller.handleAddBookDialogEvent(result.get(), true);
        }
    }

    private void initSearchView(FlowPane bottomPane, GridPane buttonPane) {

        searchField = new TextField();
        searchField.setPromptText("Search Title...");
        searchField.setPrefWidth(280.0);

        TextField authorNameField = new TextField();
        authorNameField.setPromptText("Last Name...");
        authorNameField.setPrefWidth(130.0);

        searchModeBox = new ComboBox<>();
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);

        ComboBox<Genre> genreBox = new ComboBox<>();
        genreBox.setPrefWidth(195.0);
        genreBox.setValue(Genre.Adventure);
        genreBox.getItems().addAll(Genre.values());

        GridPane ratingGrid = new GridPane();
        ratingGrid.setVgap(3);

        Slider ratingSliderMin = new Slider(1.0, 5.0, 1.0);
        ratingSliderMin.setShowTickLabels(true);
        ratingSliderMin.setShowTickMarks(true);
        ratingSliderMin.setMajorTickUnit(1.0);
        ratingSliderMin.setMinorTickCount(9);
        ratingSliderMin.setSnapToTicks(true);
        ratingSliderMin.setPrefWidth(640);

        Slider ratingSliderMax = new Slider(1.0, 5.0, 5.0);
        ratingSliderMax.setShowTickLabels(true);
        ratingSliderMax.setShowTickMarks(true);
        ratingSliderMax.setMajorTickUnit(1.0);
        ratingSliderMax.setMinorTickCount(9);
        ratingSliderMax.setSnapToTicks(true);
        ratingSliderMax.setPrefWidth(640);

        TextField minRatingField = new TextField(String.format("%.1f", ratingSliderMin.getValue()));
        TextField maxRatingField = new TextField(String.format("%.1f", ratingSliderMax.getValue()));

        minRatingField.setPrefWidth(70);
        maxRatingField.setPrefWidth(70);

        ratingGrid.add(ratingSliderMin, 0, 0);
        ratingGrid.add(ratingSliderMax, 0, 1);
        ratingGrid.add(new Label("MIN:"), 1, 0);
        ratingGrid.add(new Label("MAX:"), 1, 1);
        ratingGrid.add(minRatingField, 2, 0);
        ratingGrid.add(maxRatingField, 2, 1);

        ratingSliderMin.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (ratingSliderMax.getValue() < newValue.doubleValue()) {
                    ratingSliderMax.setValue(newValue.doubleValue());
                }
                minRatingField.textProperty().setValue(String.format("%.1f", newValue.doubleValue()));
            }
        });

        ratingSliderMax.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (ratingSliderMin.getValue() > newValue.doubleValue()) {
                    ratingSliderMin.setValue(newValue.doubleValue());
                }
                maxRatingField.textProperty().setValue(String.format("%.1f", newValue.doubleValue()));
            }
        });

        searchButton = new Button("Search");
        returnButton = new Button("Show All Books");
        Platform.runLater(() -> searchButton.setPrefWidth(returnButton.getWidth()));
        reviewButton = new Button("Show Reviews");
        Button notRatedButton = new Button("Search Not Rated");

        buttonPane.add(searchButton, 0, 0);
        buttonPane.add(returnButton, 1, 0);
        buttonPane.add(reviewButton, 2, 0);

        searchModeBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bottomPane.getChildren().clear();
                buttonPane.getChildren().clear();
                if (searchModeBox.getValue() == SearchMode.Genre) {
                    buttonPane.add(searchButton, 0, 0);
                    buttonPane.add(returnButton, 1, 0);
                    buttonPane.add(reviewButton, 2, 0);
                    bottomPane.getChildren().addAll(searchModeBox, genreBox, buttonPane);
                    bottomPane.requestFocus();
                } else if (searchModeBox.getValue() == SearchMode.Rating) {
                    buttonPane.add(searchButton, 0, 0);
                    buttonPane.add(notRatedButton, 1, 0);
                    buttonPane.add(returnButton, 0, 1);
                    buttonPane.add(reviewButton, 1, 1);
                    bottomPane.getChildren().addAll(searchModeBox, ratingGrid, buttonPane);
                    bottomPane.requestFocus();
                } else if (searchModeBox.getValue() == SearchMode.Author) {
                    buttonPane.add(searchButton, 0, 0);
                    buttonPane.add(returnButton, 1, 0);
                    buttonPane.add(reviewButton, 2, 0);
                    bottomPane.getChildren().addAll(searchModeBox, searchField, authorNameField, buttonPane);
                    bottomPane.requestFocus();
                    searchField.clear();
                    authorNameField.clear();
                    searchField.setPromptText("First Name...");
                    searchField.setPrefWidth(130.0);
                } else if (searchModeBox.getValue() == SearchMode.ISBN) {
                    buttonPane.add(searchButton, 0, 0);
                    buttonPane.add(returnButton, 1, 0);
                    buttonPane.add(reviewButton, 2, 0);
                    bottomPane.getChildren().addAll(searchModeBox, searchField, buttonPane);
                    searchField.clear();
                    searchField.setPromptText("(978/979)-XXXX-XXXXXX");
                    searchField.setPrefWidth(280.0);
                    bottomPane.requestFocus();
                } else {
                    buttonPane.add(searchButton, 0, 0);
                    buttonPane.add(returnButton, 1, 0);
                    buttonPane.add(reviewButton, 2, 0);
                    bottomPane.getChildren().addAll(searchModeBox, searchField, buttonPane);
                    bottomPane.requestFocus();
                    searchField.clear();
                    searchField.setPromptText("Search Title");
                    searchField.setPrefWidth(280.0);
                }
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchFor = searchField.getText();
                SearchMode mode = searchModeBox.getValue();

                if (mode == SearchMode.Genre) {
                    controller.onSearchSelected(genreBox.getValue().name(), mode);
                } else if (mode == SearchMode.Rating) {
                    controller.onSearchRatingSelected(minRatingField.getText(), maxRatingField.getText());
                } else if (mode == SearchMode.Author) {
                    searchFor = searchField.getText().trim() + authorNameField.getText().trim();
                    controller.onSearchSelected(searchFor, mode);
                } else {
                    controller.onSearchSelected(searchFor, mode);
                }
            }
        });

        notRatedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.onSearchRatingSelected("0", "0");

            }
        });

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.refreshBooksInView();
            }
        });

        reviewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (booksTable.getSelectionModel().getSelectedItem() != null) {
                    if (controller.isBookReviewed(booksTable.getSelectionModel().getSelectedItem())) {
                        displayReviewsDialog = new DisplayReviewsDialog(booksTable.getSelectionModel().getSelectedItem());
                        displayReviewsDialog.showAndWait();
                    }

                } else {
                    controller.handleInvalidInput("Select a book to see reviews");
                }
            }
        });
    }

}
