package labb1db;

import model.BooksDb;
import view.DbView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * Application start up.
 *
 * @author Umar A & Rabi S
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        BooksDb booksDb = new BooksDb(); // model
        // Don't forget to connect to the db, somewhere...

        DbView root = new DbView(booksDb);

        Scene scene = new Scene(root, 1212, 720);

        primaryStage.setTitle("Books Database Client");
        // add an exit handler to the stage (X) ?
        primaryStage.setOnCloseRequest(event -> {
            try {
                booksDb.disconnect();
            } catch (Exception e) {
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
