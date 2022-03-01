package view;

import java.sql.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Book;
import model.Genre;
import model.SearchMode;
/**
 *
 * @author Rab_S
 */
public class RemoveBookDialog extends Dialog<Book> {

    private final Controller controller;

    private ButtonType buttonTypeOk;
    private ButtonType buttonTypeCancel;
    private Button searchButton;

    private TextField searchField;
    private TextField searchField_2;

    private ComboBox<SearchMode> searchModeBox;
    private ComboBox<Genre> genreBox;

    private Slider ratingSliderMin;
    private Slider ratingSliderMax;

    TableView<Book> booksTable = new TableView();
    private ObservableList<Book> booksInTable = FXCollections.observableArrayList();

    public RemoveBookDialog(Controller controller, ObservableList<Book> books) {
        this.controller = controller;
        booksInTable = books; // point to same list as in DView

        buildDialog();

    }

    private void buildDialog() {

        VBox dialogBox = new VBox(10.0);

        initTableView();
        HBox searchBox = initSearchHbox();
        dialogBox.getChildren().addAll(booksTable, searchBox);

        this.setHeaderText("Remove Book");

        this.getDialogPane().setContent(dialogBox);

        buttonTypeOk = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        searchButton.setDefaultButton(true);

        this.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        this.setResultConverter(new Callback<ButtonType, Book>() {
            @Override
            public Book call(ButtonType b) {
                Book result = null;
                if (b == buttonTypeOk) {
                    Book book = booksTable.getSelectionModel().getSelectedItem();
                    if (book != null) {
                        result = book;
                    }
                }
                return result;
            }
        });

        Button okButton = (Button) this.getDialogPane().lookupButton(buttonTypeOk);
        okButton.addEventFilter(ActionEvent.ACTION, new EventHandler() {
            @Override
            public void handle(Event event) {
                Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
                if (selectedBook == null) {
                    event.consume();
                    controller.handleInvalidInput("Select a book to remove.");
                }
            }
        });
    }

    private HBox initSearchHbox() {
        HBox searchViewBox = new HBox(5);

        searchField = new TextField();
        searchField.setPromptText("Search Title...");
        searchField.setPrefWidth(265.0);

        searchField_2 = new TextField();
        searchField_2.setPromptText("Last Name...");
        searchField_2.setPrefWidth(130.0);

        searchModeBox = new ComboBox<>();
        searchModeBox.getItems().addAll(SearchMode.values());
        searchModeBox.setValue(SearchMode.Title);

        genreBox = new ComboBox<>();
        genreBox.setValue(Genre.Adventure);
        genreBox.setPrefWidth(195.0);
        genreBox.getItems().addAll(Genre.values());

        ratingSliderMin = new Slider(1.0, 5.0, 1.0);
        ratingSliderMin.setShowTickLabels(true);
        ratingSliderMin.setShowTickMarks(true);
        ratingSliderMin.setMajorTickUnit(1.0);
        ratingSliderMin.setMinorTickCount(9);
        ratingSliderMin.setSnapToTicks(true);
        ratingSliderMin.setPrefWidth(640);

        ratingSliderMax = new Slider(1.0, 5.0, 5.0);
        ratingSliderMax.setShowTickLabels(true);
        ratingSliderMax.setShowTickMarks(true);
        ratingSliderMax.setMajorTickUnit(1.0);
        ratingSliderMax.setMinorTickCount(9);
        ratingSliderMax.setSnapToTicks(true);
        ratingSliderMax.setPrefWidth(640);

        Label minLabel = new Label("MIN:");
        Label maxLabel = new Label("MAX:");

        TextField minRatingField = new TextField(String.format("%.1f", ratingSliderMin.getValue()));
        TextField maxRatingField = new TextField(String.format("%.1f", ratingSliderMax.getValue()));

        minRatingField.setPrefWidth(70);
        maxRatingField.setPrefWidth(70);

        GridPane ratingGrid = new GridPane();
        ratingGrid.setVgap(3);

        ratingGrid.add(ratingSliderMin, 0, 0);
        ratingGrid.add(ratingSliderMax, 0, 1);
        ratingGrid.add(minLabel, 1, 0);
        ratingGrid.add(maxLabel, 1, 1);
        ratingGrid.add(minRatingField, 2, 0);
        ratingGrid.add(maxRatingField, 2, 1);

        ratingSliderMin.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (Double.parseDouble(maxRatingField.getText()) < newValue.doubleValue()) {
                    ratingSliderMax.setValue(newValue.doubleValue());
                }
                minRatingField.textProperty().setValue(String.format("%.1f", newValue.doubleValue()));
            }
        });

        ratingSliderMax.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (Double.parseDouble(minRatingField.getText()) > newValue.doubleValue()) {
                    ratingSliderMin.setValue(newValue.doubleValue());
                }
                maxRatingField.textProperty().setValue(String.format("%.1f", newValue.doubleValue()));
            }
        });

        searchButton = new Button("Search");

        // event handling (dispatch to controller)
        searchModeBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (searchModeBox.getValue() == SearchMode.Genre) {
                    searchViewBox.getChildren().clear();
                    searchViewBox.getChildren().addAll(searchModeBox, genreBox, searchButton);
                    searchViewBox.requestFocus();
                } else if (searchModeBox.getValue() == SearchMode.Rating) {
                    searchViewBox.getChildren().clear();
                    searchViewBox.getChildren().addAll(searchModeBox, ratingGrid, searchButton);
                    searchViewBox.setStyle("-fx-alignment: CENTER-LEFT;");
                    searchViewBox.requestFocus();
                } else if (searchModeBox.getValue() == SearchMode.Author) {
                    searchViewBox.getChildren().clear();
                    searchViewBox.getChildren().addAll(searchModeBox, searchField, searchField_2, searchButton);
                    searchViewBox.requestFocus();
                    searchField.clear();
                    searchField_2.clear();
                    searchField.setPromptText("First Name...");
                    searchField.setPrefWidth(130.0);
                } else if (searchModeBox.getValue() == SearchMode.ISBN) {
                    searchViewBox.getChildren().clear();
                    searchViewBox.getChildren().addAll(searchModeBox, searchField, searchButton);
                    searchField.clear();
                    searchField.setPromptText("(978/979)-XXXX-XXXXXX");
                    searchField.setPrefWidth(265.0);
                    searchViewBox.requestFocus();
                } else {
                    searchViewBox.getChildren().clear();
                    searchViewBox.getChildren().addAll(searchModeBox, searchField, searchButton);
                    searchField.clear();
                    searchField.setPromptText("Search Title...");
                    searchField.setPrefWidth(265.0);
                    searchViewBox.requestFocus();
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
                    searchFor = searchField.getText().trim() + searchField_2.getText().trim();
                    controller.onSearchSelected(searchFor, mode);
                } else {
                    controller.onSearchSelected(searchFor, mode);
                }
            }
        });

        searchViewBox.getChildren().addAll(searchModeBox, searchField, searchButton);
        return searchViewBox;
    }

    private void initTableView() {
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, Date> publishedCol = new TableColumn<>("Published");
        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        TableColumn<Book, String> ratingCol = new TableColumn<>("Rating");
        TableColumn<Book, String> bookAddedByCol = new TableColumn<>("Added by");

        TableColumn<Book, String> authorNameCol = new TableColumn<>("Author(s)");
        TableColumn<Book, String> authorDobCol = new TableColumn<>("Birth Date");

        booksTable.getColumns().addAll(titleCol, isbnCol, publishedCol, genreCol, ratingCol, bookAddedByCol, authorNameCol, authorDobCol);
        //give columns some extra space and center
        titleCol.setPrefWidth(230.0);
        isbnCol.setPrefWidth(130.0);
        publishedCol.setPrefWidth(110.0);
        genreCol.setPrefWidth(110.0);
        ratingCol.setPrefWidth(70.0);
        bookAddedByCol.setPrefWidth(100.0);

        authorNameCol.setPrefWidth(230.0);
        authorDobCol.setPrefWidth(110.0);

        titleCol.setStyle("-fx-alignment: CENTER;");
        isbnCol.setStyle("-fx-alignment: CENTER;");
        publishedCol.setStyle("-fx-alignment: CENTER;");
        genreCol.setStyle("-fx-alignment: CENTER;");
        ratingCol.setStyle("-fx-alignment: CENTER;");
        bookAddedByCol.setStyle("-fx-alignment: CENTER;");

        authorNameCol.setStyle("-fx-alignment: CENTER;");
        authorDobCol.setStyle("-fx-alignment: CENTER;");

        // define how to fill data for each cell, 
        // get values from Book properties
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        publishedCol.setCellValueFactory(new PropertyValueFactory<>("published"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        ratingCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> data) {
                if(data.getValue().getRating() > 0.0){
                    return new SimpleStringProperty(data.getValue().getRating() + "/5.0");
                }
                return new SimpleStringProperty("Not Set");
            }

        }
        );

        bookAddedByCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> data) {
                return new SimpleStringProperty(data.getValue().getUser().getUsername());
            }
        });

        authorNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> data) {
                String authorNames = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorNames += data.getValue().getAuthors().get(i).getName() + "\n";
                }
                return new SimpleStringProperty(authorNames);
            }
        });

        authorDobCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> data) {
                String authorDobs = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorDobs += data.getValue().getAuthors().get(i).getDateOfBirth() + "\n";
                }
                return new SimpleStringProperty(authorDobs);
            }
        });

        // associate the table view with the data
        // associate the table view with the data
        booksTable.setItems(booksInTable);
    }
}
