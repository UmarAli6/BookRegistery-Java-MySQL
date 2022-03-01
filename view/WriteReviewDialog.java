package view;

import java.time.LocalDate;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import model.Book;
import model.Review;

/**
 *
 * @author Umar_Ali
 */
public class WriteReviewDialog extends Dialog<Review> {

    private final Controller controller;
    private final Book bookToReview;

    private ButtonType buttonTypeOk;
    private ButtonType buttonTypeCancel;

    private final TextArea reviewField = new TextArea();
    private final Slider ratingSlider = new Slider(1.0, 5.0, 1.0);

    public WriteReviewDialog(Controller controller, Book bookToReview) {
        this.controller = controller;
        this.bookToReview = bookToReview;
        buildDialog();
    }

    private void buildDialog() {

        this.getDialogPane().setContent(populateGridPane());

        buttonTypeOk = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        this.setResultConverter(new Callback<ButtonType, Review>() {
            @Override
            public Review call(ButtonType b) {
                Review result = null;
                if (b == buttonTypeOk && !reviewField.getText().trim().isEmpty()) {
                    result = new Review(ratingSlider.getValue(), reviewField.getText().trim(), LocalDate.now().toString(), bookToReview.getBookId());
                }
                return result;
            }
        });

        Button okButton = (Button) this.getDialogPane().lookupButton(buttonTypeOk);
        okButton.addEventFilter(ActionEvent.ACTION, new EventHandler() {
            @Override
            public void handle(Event event) {
                if (reviewField.getText().trim().isEmpty()) {
                    event.consume();
                    controller.handleInvalidInput("Write your review to continue");
                }
            }
        });

    }

    private GridPane populateGridPane() {
        GridPane dPane = new GridPane();

        dPane.setAlignment(Pos.CENTER);
        dPane.setHgap(10);
        dPane.setVgap(5);
        dPane.setPadding(new Insets(10, 10, 10, 10));

        dPane.add(new Label("Title:"), 0, 0);
        dPane.add(new Label(bookToReview.getTitle()), 1, 0);

        dPane.add(new Label("Isbn-13:"), 0, 1);
        dPane.add(new Label(bookToReview.getIsbn()), 1, 1);

        dPane.add(new Label("Publish Date:"), 0, 2);
        dPane.add(new Label(bookToReview.getPublished().toString()), 1, 2);

        dPane.add(new Label("Genre:"), 0, 3);
        dPane.add(new Label(bookToReview.getGenre().name()), 1, 3);

        dPane.add(new Label("Rating:"), 0, 4);
        dPane.add(new Label(String.valueOf(bookToReview.getRating())), 1, 4);

        Rectangle rect = new Rectangle();
        rect.setHeight(31.0);
        rect.setWidth(31);
        rect.setFill(Color.TRANSPARENT);
        dPane.add(rect, 1, 5);

        dPane.add(new Label("Review"), 0, 6);
        reviewField.setPrefSize(300, 100);
        reviewField.setWrapText(true);
        reviewField.setStyle("-fx-white-space: pre-wrap;");
        reviewField.setPromptText("Write your review");
        dPane.add(reviewField, 1, 6);

        dPane.add(new Label("Rating"), 0, 7);
                
        ratingSlider.setShowTickLabels(true);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setMajorTickUnit(1.0);
        ratingSlider.setMinorTickCount(0);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setPrefWidth(90);

        dPane.add(ratingSlider, 1, 7);

        this.setTitle("Add new Book");
        this.setResizable(false);

        return dPane;
    }
}
