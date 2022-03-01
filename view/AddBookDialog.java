package view;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import model.Author;
import model.Book;
import model.Genre;

/**
 *
 * @author Rab_S
 */
public class AddBookDialog extends Dialog<Book> {

    private final Controller controller;

    private ButtonType buttonTypeOk;
    private ButtonType buttonTypeCancel;

    private final TextField titleField = new TextField();
    private final TextField isbnField = new TextField();

    private final ArrayList<String> yearPub = new ArrayList();
    private final ArrayList<String> monthPub = new ArrayList();
    private final ArrayList<String> dayPub = new ArrayList();

    private final ComboBox<Genre> genreChoice = new ComboBox(FXCollections.observableArrayList(Genre.values()));
    private ComboBox<String> yearChoicePub;
    private ComboBox<String> monthChoicePub;
    private ComboBox<String> dayChoicePub;

    private String invalidMsg;
    
    private static final int MAXA = 5;

    private final Hyperlink addAuthor = new Hyperlink("+ Add Author");
    private final Hyperlink removeAuthor = new Hyperlink("- Remove Author");
    private final ArrayList<TextField> authorFields = new ArrayList();
    private final ArrayList<HBox> nameBoxes = new ArrayList();

    private final ArrayList<ComboBox<String>> yearChoiceDob = new ArrayList();
    private final ArrayList<ComboBox<String>> monthChoiceDob = new ArrayList();
    private final ArrayList<ComboBox<String>> dayChoiceDob = new ArrayList();
    private final ArrayList<HBox> birthBoxes = new ArrayList();
    private int noOfAuthors;

    public AddBookDialog(Controller controller) {
        this.controller = controller;
        buildDialog();
    }

    private void buildDialog() {
        noOfAuthors = 0;
        populateComboBoxes();
        populateAuthorFields();
        GridPane dialogPane = populateGridPane();

        this.setTitle("Add new Book");
        this.setResizable(false);

        this.getDialogPane().setContent(dialogPane);

        buttonTypeOk = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        this.setResultConverter(new Callback<ButtonType, Book>() {
            @Override
            public Book call(ButtonType b) {
                Book result = null;
                if (b == buttonTypeOk) {
                    if (isValidInput()) {
                        String title = titleField.getText().trim();
                        String isbn = isbnField.getText().trim();
                        String published = yearChoicePub.getValue() + "-" + monthChoicePub.getValue() + "-" + dayChoicePub.getValue();
                        String genre = genreChoice.getValue().name();
                        result = new Book(-1, title, isbn, published, genre, 0.0, null);

                        for (int i = 0, j = 0; i < noOfAuthors + 1; i++, j = j + 2) {
                            String name = authorFields.get(j).getText().trim() + " " + authorFields.get(j + 1).getText().trim();
                            String birth = yearChoiceDob.get(i).getValue() + "-" + monthChoiceDob.get(i).getValue() + "-" + dayChoiceDob.get(i).getValue();
                            result.addAuthor(new Author(name, birth));
                        }
                    }
                }
                return result;
            }
        });

        Button okButton = (Button) this.getDialogPane().lookupButton(buttonTypeOk);
        okButton.addEventFilter(ActionEvent.ACTION, new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!isValidInput()) {
                    event.consume();
                    controller.handleInvalidInput(getInvalidMsg());
                } 
            }
        });

        addAuthor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateMoreAuthors(dialogPane);
            }
        });

        removeAuthor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateFewerAuthors(dialogPane);
            }
        });
    }

    private void updateMoreAuthors(GridPane dPane) {
        noOfAuthors++;
        dPane.getChildren().remove(nameBoxes.get(MAXA));

        addAuthor.setVisited(false);
        removeAuthor.setVisible(true);

        dPane.add(new Label("Author " + (noOfAuthors + 1) + ":"), 0, getRowCount(dPane));
        dPane.add(nameBoxes.get(noOfAuthors), 1, getRowCount(dPane) - 1);

        dPane.add(new Label("Date of Birth:"), 0, getRowCount(dPane));
        dPane.add(birthBoxes.get(noOfAuthors), 1, getRowCount(dPane) - 1);

        if (noOfAuthors > 3) {
            addAuthor.setVisible(false);
        }

        dPane.add(nameBoxes.get(MAXA), 1, getRowCount(dPane));

        this.setHeight(this.getHeight() + titleField.getHeight() + yearChoicePub.getHeight() + 10);
        this.getDialogPane().setContent(dPane);
    }

    private void updateFewerAuthors(GridPane dPane) {
        noOfAuthors--;

        dPane.getChildren().remove(nameBoxes.get(MAXA));

        removeAuthor.setVisited(false);

        dPane.getChildren().remove(getRowCount(dPane) * 2 - 2);
        dPane.getChildren().remove(getRowCount(dPane) * 2 - 3);

        dPane.getChildren().remove(getRowCount(dPane) * 2 - 2);
        dPane.getChildren().remove(getRowCount(dPane) * 2 - 3);

        if (noOfAuthors < 1) {
            removeAuthor.setVisible(false);
            addAuthor.setVisible(true);
        }

        dPane.add(nameBoxes.get(MAXA), 1, getRowCount(dPane));

        this.setHeight(this.getHeight() - (titleField.getHeight() + yearChoicePub.getHeight() + 10));
        this.getDialogPane().setContent(dPane);
    }

    private boolean isValidInput() {
        invalidMsg = "";
        Boolean isValid = true;
        if (titleField.getText().trim().isEmpty()) {
            invalidMsg += "Title, ";
            isValid = false;
        }
        if (isbnField.getText().trim().isEmpty()) {
            invalidMsg += "Isbn, ";
            isbnField.clear();
            isValid = false;
        } else {
            if (!isValidIsbn(isbnField.getText())) {
                invalidMsg += "Isbn, ";
                isValid = false;
                isbnField.clear();
            }
        }
        if (yearChoicePub.getValue() == null || monthChoicePub.getValue() == null || dayChoicePub.getValue() == null) {
            invalidMsg += "Publishing date, ";
            isValid = false;
        }
        if (genreChoice.getValue() == null) {
            invalidMsg += "Genre ";
            isValid = false;
        }
        

        for (int i = 0, j = 0; i < noOfAuthors + 1; i++, j = j + 2) {

            if (authorFields.get(j).getText().trim().isEmpty() || authorFields.get(j + 1).getText().trim().isEmpty()) {
                invalidMsg += "\nAuthor " + (i + 1) + ": Name";
                isValid = false;
            }
            if (yearChoiceDob.get(i).getValue() == null || monthChoiceDob.get(i).getValue() == null || dayChoiceDob.get(i).getValue() == null) {
                if (invalidMsg.contains("Author " + (i + 1))) {
                    invalidMsg += ", Date of Birth";
                } else {
                    invalidMsg += "\nAuthor " + (i + 1) + ": Date of Birth";
                }

                isValid = false;
            }
        }
        return isValid;
    }

    private boolean isValidIsbn(String isbnStr) {
        isbnStr = isbnStr.replace("-", "").trim();
        return isbnStr.matches("[0-9]{13}") && (isbnStr.startsWith("979") || isbnStr.startsWith("978"));
    }

    private String getInvalidMsg() {
        return invalidMsg;
    }

    private GridPane populateGridPane() {
        // Create GridPane to be added to the dialog
        GridPane dPane = new GridPane();

        //Set pref
        dPane.setAlignment(Pos.CENTER);
        dPane.setHgap(10);
        dPane.setVgap(5);
        dPane.setPadding(new Insets(10, 10, 10, 10));

        dPane.add(new Label("Title:"), 0, 0);
        titleField.setPromptText("title...");
        dPane.add(titleField, 1, 0);

        dPane.add(new Label("Isbn-13:"), 0, 1);
        isbnField.setPromptText("(978/979)-XXXX-XXXXXX");
        dPane.add(isbnField, 1, 1);

        dPane.add(new Label("Publish Date:"), 0, 2);
        HBox publishBox = new HBox(5);
        publishBox.getChildren().addAll(yearChoicePub, monthChoicePub, dayChoicePub);
        dPane.add(publishBox, 1, 2);

        dPane.add(new Label("Genre:"), 0, 3);
        genreChoice.setMinWidth(193);
        genreChoice.setPromptText("None");
        dPane.add(genreChoice, 1, 3);

        Rectangle rect = new Rectangle();
        rect.setHeight(31.0);
        rect.setWidth(31);
        rect.setFill(Color.TRANSPARENT);
        dPane.add(rect, 1, 4);

        dPane.add(new Label("Author 1: "), 0, 5);
        dPane.add(nameBoxes.get(noOfAuthors), 1, 5);

        dPane.add(new Label("Date of Birth:"), 0, 6);
        dPane.add(birthBoxes.get(noOfAuthors), 1, 6);

        dPane.add(nameBoxes.get(MAXA), 1, 7);
        return dPane;
    }

    private void populateComboBoxes() {
        DecimalFormat twoDig = new DecimalFormat("00");
        for (int i = LocalDate.now().getYear(); i > 0; i--) {
            if (i >= 1400) {
                yearPub.add(String.valueOf(i));
            }
        }
        for (int i = 1; i <= 31; i++) {
            if (i <= 12) {
                monthPub.add(twoDig.format(i));
            }
            if (i <= 31) {
                dayPub.add(twoDig.format(i));
            }
        }
        yearChoicePub = new ComboBox(FXCollections.observableArrayList(yearPub));
        monthChoicePub = new ComboBox(FXCollections.observableArrayList(monthPub));
        dayChoicePub = new ComboBox(FXCollections.observableArrayList(dayPub));

        yearChoicePub.setPromptText("Year");
        monthChoicePub.setPromptText("Month");
        dayChoicePub.setPromptText("Day");
        yearChoicePub.setMinWidth(90);
    }

    private void populateAuthorFields() {
        for (int i = 0; i < (MAXA * 2); i++) {
            authorFields.add(new TextField());
            if (i % 2 == 0) {
                authorFields.get(i).setPromptText("First Name");
            } else {
                authorFields.get(i).setPromptText("Last Name");
            }
            if (i <= MAXA) {
                nameBoxes.add(new HBox(5.0));
                birthBoxes.add(new HBox(5.0));
            }
        }

        for (int i = 0, j = 0; i < MAXA; i++, j = j + 2) {
            nameBoxes.get(i).getChildren().add(authorFields.get(j));
            nameBoxes.get(i).getChildren().add(authorFields.get(j + 1));

            yearChoiceDob.add(new ComboBox(FXCollections.observableArrayList(yearPub)));
            monthChoiceDob.add(new ComboBox(FXCollections.observableArrayList(monthPub)));
            dayChoiceDob.add(new ComboBox(FXCollections.observableArrayList(dayPub)));

            yearChoiceDob.get(i).setPromptText("Year");
            monthChoiceDob.get(i).setPromptText("Month");
            dayChoiceDob.get(i).setPromptText("Day");
            yearChoiceDob.get(i).setMinWidth(90);

            birthBoxes.get(i).getChildren().addAll(yearChoiceDob.get(i), monthChoiceDob.get(i), dayChoiceDob.get(i));
        }

        addAuthor.setBorder(Border.EMPTY);

        removeAuthor.setVisible(false);
        removeAuthor.setBorder(Border.EMPTY);

        nameBoxes.get(MAXA).getChildren().addAll(addAuthor, removeAuthor);

    }

    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if (rowIndex != null) {
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }
}
