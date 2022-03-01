package view;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import model.Book;

/**
 *
 * @author Rab_S
 */
public class DuplicateAuthorDialog extends Dialog<Book> {

    Controller controller;

    private ButtonType buttonTypeApply;
    private ButtonType buttonTypeDisregard;

    TableView<Book> booksTable = new TableView();

    private final ObservableList<Book> booksInTable = FXCollections.observableArrayList();

    private final Book newBook;
    private final ArrayList<Book> authorMatches = new ArrayList();

    public DuplicateAuthorDialog(Controller controller, List<Book> authorMatches) {
        this.controller = controller;
        this.newBook = authorMatches.get(authorMatches.size() - 1);
        authorMatches.remove(authorMatches.size() - 1);
        this.authorMatches.addAll(authorMatches);
        buildDialog();
    }

    private void buildDialog() {

        VBox dialogBox = populateVbox();

        this.getDialogPane().setContent(dialogBox);

        buttonTypeApply = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        buttonTypeDisregard = new ButtonType("Ignore", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(buttonTypeApply, buttonTypeDisregard);

        this.setResultConverter(new Callback<ButtonType, Book>() {
            @Override
            public Book call(ButtonType b) {
                Book result = null;
                if (b == buttonTypeApply) {
                    if (booksTable.getSelectionModel().getSelectedItem() != null) {
                        List<Book> selectedAuthors = booksTable.getSelectionModel().getSelectedItems();

                        for (int i = 0; i < newBook.getAuthors().size(); i++) {
                            boolean isMatched = false;
                            for (int j = 0; j < selectedAuthors.size(); j++) {
                                if ((newBook.getAuthor(i).getAuthorId() == selectedAuthors.get(j).getAuthor(0).getAuthorId())) {
                                    isMatched = true;
                                }
                            }
                            if (!isMatched) {
                                newBook.getAuthor(i).setAuthorId(-1);
                            }
                        }

                    }
                } else if (b == buttonTypeDisregard) {
                    for (int i = 0; i < newBook.getAuthors().size(); i++) {
                        newBook.getAuthor(i).setAuthorId(-1);
                    }
                }
                result = newBook;
                return result;
            }
        });

        Button okButton = (Button) this.getDialogPane().lookupButton(buttonTypeApply);
        okButton.addEventFilter(ActionEvent.ACTION, new EventHandler() {
            @Override
            public void handle(Event event) {
                if (booksTable.getSelectionModel().getSelectedItem() == null) {
                    event.consume();
                    controller.handleInvalidInput("Select an author to Apply.");
                }
            }
        });
    }

    private GridPane populateGridPane() {
        GridPane dPane = new GridPane();

        dPane.setAlignment(Pos.CENTER_LEFT);
        dPane.setHgap(10);
        dPane.setVgap(5);
        dPane.setPadding(new Insets(10, 10, 10, 10));

        dPane.add(new Label("Title:"), 0, 0);
        dPane.add(new Label(newBook.getTitle()), 1, 0);

        dPane.add(new Label("Isbn-13:"), 0, 1);
        dPane.add(new Label(newBook.getIsbn()), 1, 1);

        dPane.add(new Label("Publish Date:"), 0, 2);
        dPane.add(new Label(newBook.getPublished().toString()), 1, 2);

        dPane.add(new Label("Genre:"), 0, 3);
        dPane.add(new Label(newBook.getGenre().name()), 1, 3);

        dPane.add(new Label("Rating:"), 0, 4);
        dPane.add(new Label(String.valueOf(newBook.getRating())), 1, 4);

        dPane.add(new Label("Authors:"), 0, 5);
        String authors = "";
        for (int i = 0; i < newBook.getAuthors().size(); i++) {
            authors += newBook.getAuthor(i).getName() + "\t";
        }
        Label authorLabel = new Label(authors);
        authorLabel.setFont(Font.font(16.0));
        dPane.add(authorLabel, 1, 5);
        return dPane;
    }

    private VBox populateVbox() {

        VBox dialogBox = new VBox();
        Label info = new Label(duplicateAuthorMsg());
        info.setFont(Font.font(24.0));

        dialogBox.getChildren().addAll(populateGridPane(), info);

        HBox tables = new HBox();

        booksInTable.addAll(authorMatches);

        TableColumn<Book, String> authorIdCol = new TableColumn<>("AuthorId");
        TableColumn<Book, String> nameCol = new TableColumn<>("Name");
        TableColumn<Book, String> birthCol = new TableColumn<>("Birth Date");
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        TableColumn<Book, String> pubCol = new TableColumn<>("Published");

        booksTable.getColumns().addAll(authorIdCol, nameCol, birthCol, titleCol, pubCol);
        booksTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        authorIdCol.setStyle("-fx-alignment: CENTER;");
        nameCol.setStyle("-fx-alignment: CENTER;");
        birthCol.setStyle("-fx-alignment: CENTER;");
        titleCol.setStyle("-fx-alignment: CENTER;");
        pubCol.setStyle("-fx-alignment: CENTER;");

        titleCol.setPrefWidth(200.0);
        pubCol.setPrefWidth(130.0);

        authorIdCol.setPrefWidth(90.0);
        nameCol.setPrefWidth(200.0);
        birthCol.setPrefWidth(130.0);

        authorIdCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> data) {
                String authorIds = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorIds += data.getValue().getAuthors().get(i).getAuthorId() + "\n";
                }
                return new SimpleStringProperty(authorIds);
            }
        });

        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> data) {
                String authorNames = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorNames += data.getValue().getAuthors().get(i).getName() + "\n";
                }
                return new SimpleStringProperty(authorNames);
            }
        });

        birthCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> data) {
                String authorDobs = "";
                for (int i = 0; i < data.getValue().getAuthors().size(); i++) {
                    authorDobs += data.getValue().getAuthors().get(i).getDateOfBirth() + "\n";
                }
                return new SimpleStringProperty(authorDobs);
            }
        });

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        pubCol.setCellValueFactory(new PropertyValueFactory<>("published"));

        booksTable.setItems(booksInTable);
        booksTable.setMinWidth(titleCol.getPrefWidth() + pubCol.getPrefWidth() + authorIdCol.getPrefWidth() + nameCol.getPrefWidth() + birthCol.getPrefWidth());

        booksTable.setBorder(Border.EMPTY);

        tables.getChildren().addAll(booksTable);
        dialogBox.getChildren().add(tables);

        return dialogBox;
    }

    private String duplicateAuthorMsg() {
        String msg = "Existing author(s) found for the book you are trying to add.\n"
                + "Select the indended authors and click Apply.\n"
                + "Hold ctrl for multiple authors.\n"
                + "Click ignore to add these authors as new authors.";
        return msg;
    }

}
