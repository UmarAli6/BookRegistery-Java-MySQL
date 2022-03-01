package view;

import javafx.application.Platform;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.User;

/**
 *
 * @author Rab_S
 */
public class LoginDialog extends Dialog<User> {

    Controller controller;

    private ButtonType buttonTypeOk;
    private ButtonType buttonTypeCancel;

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private String invalidMsg;

    public LoginDialog(Controller controller) {
        this.controller = controller;
        buildDialog();
    }

    private void buildDialog() {

        GridPane dPane = new GridPane();
        dPane.setAlignment(Pos.CENTER);
        dPane.setHgap(10);
        dPane.setVgap(5);
        dPane.setPadding(new Insets(10, 10, 10, 10));

        dPane.add(new Label("Username"), 0, 0);
        usernameField.setPromptText("username");
        dPane.add(usernameField, 1, 0);
        dPane.add(new Label("Password"), 0, 1);
        passwordField.setPromptText("password");
        dPane.add(passwordField, 1, 1);

        this.setTitle("Login");
        this.getDialogPane().setContent(dPane);

        buttonTypeOk = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        this.setResultConverter(new Callback<ButtonType, User>() {
            @Override
            public User call(ButtonType b) {
                User result = null;
                if (b == buttonTypeOk) {
                    if (isValidInput()) {
                        result = new User(usernameField.getText().trim(), passwordField.getText());
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
                    usernameField.requestFocus();
                    controller.handleInvalidInput(invalidMsg);
                }
            }

        });
        Platform.runLater(() -> usernameField.requestFocus());
    }

    private boolean isValidInput() {
        boolean isValid = true;
        invalidMsg = "";
        boolean username = usernameField.getText().trim().isEmpty();
        boolean password = passwordField.getText().isEmpty();
        if (username) {
            isValid = false;
            invalidMsg += "username, ";
        }
        if (password) {
            isValid = false;
            invalidMsg += "password, ";
        }
        return isValid;
    }
}
