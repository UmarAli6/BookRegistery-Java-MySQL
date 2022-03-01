package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Book;

/**
 *
 * @author Umar_Ali
 */
public class DisplayReviewsDialog extends Dialog {

    private ButtonType buttonTypeOk;
    private final Book bookToShow;

    public DisplayReviewsDialog(Book bookToShow) {
        this.bookToShow = bookToShow;
        buildDialog();
    }

    private void buildDialog() {
        VBox dBox = new VBox(30);

        dBox.getChildren().addAll(populateGridPane(), populateScrollPane());

        this.setTitle("Reviews");
        this.getDialogPane().setContent(dBox);

        buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(buttonTypeOk);
    }

    private GridPane populateGridPane() {
        GridPane dPane = new GridPane();

        dPane.setAlignment(Pos.CENTER_LEFT);
        dPane.setHgap(10);
        dPane.setVgap(5);
        dPane.setPadding(new Insets(10, 10, 10, 10));

        dPane.add(new Label("Title:"), 0, 0);
        dPane.add(new Label(bookToShow.getTitle()), 1, 0);

        dPane.add(new Label("Isbn-13:"), 0, 1);
        dPane.add(new Label(bookToShow.getIsbn()), 1, 1);

        dPane.add(new Label("Publish Date:"), 0, 2);
        dPane.add(new Label(bookToShow.getPublished().toString()), 1, 2);

        dPane.add(new Label("Genre:"), 0, 3);
        dPane.add(new Label(bookToShow.getGenre().name()), 1, 3);

        dPane.add(new Label("Rating:"), 0, 4);
        dPane.add(new Label(String.valueOf(bookToShow.getRating())), 1, 4);

        return dPane;
    }

    private ScrollPane populateScrollPane() {
        ScrollPane sPane = new ScrollPane();
        //sPane.setMaxHeight(100);

        VBox revBox = new VBox(20);

        revBox.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < bookToShow.getReviews().size(); i++) {
            String username = "User: "+bookToShow.getReviews().get(i).getUser().getUsername();
            String dateAdded = "Date: "+bookToShow.getReviews().get(i).getDateAdded().toString();
            String rating = "Rating: "+String.valueOf(bookToShow.getReviews().get(i).getbRating());
            
            String labelText = String.format("%-30s%-30s%-30s",username,dateAdded,rating);
            
            VBox rBox = new VBox(5);
            
            Label usernameLabel = new Label(labelText);
            usernameLabel.setAlignment(Pos.CENTER_RIGHT);

            Label label = new Label(bookToShow.getReviews().get(i).getRevString());
            label.setMaxWidth(600);
            label.setWrapText(true);
            

            rBox.getChildren().addAll(usernameLabel, label);

            String cssLayout = "-fx-border-color: black;\n"
                    + "-fx-border-insets: 3;\n"
                    + "-fx-border-width: 1.5;\n";

            rBox.setStyle(cssLayout);

            revBox.getChildren().add(rBox);

        }
        
        //revBox.setMaxWidth(650);
        sPane.setMinWidth(650);
        //sPane.setMaxWidth(800);
        sPane.setMaxHeight(500);
        sPane.setContent(revBox);
        return sPane;

    }
}
