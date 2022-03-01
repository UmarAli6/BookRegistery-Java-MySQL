package view;

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
public class CreateAccDialog extends Dialog<User> {

    private final Controller controller;

    private ButtonType buttonTypeOk;
    private ButtonType buttonTypeCancel;

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField checkPasswordField = new PasswordField();

    private String invalidMsg;

    public CreateAccDialog(Controller controller) {
        this.controller = controller;
        buildDialog();
    }

    private void buildDialog() {
        GridPane dPane = new GridPane();

        //Set pref
        dPane.setAlignment(Pos.CENTER);
        dPane.setHgap(10);
        dPane.setVgap(5);
        dPane.setPadding(new Insets(10, 10, 10, 10));

        dPane.add(new Label("User Name"), 0, 3);
        usernameField.setPromptText("Username");
        dPane.add(usernameField, 1, 3);

        dPane.add(new Label("Password:"), 0, 4);
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(usernameField.getWidth());
        dPane.add(passwordField, 1, 4);

        checkPasswordField.setPromptText("Re-enter password");
        checkPasswordField.setPrefWidth(passwordField.getWidth());
        dPane.add(checkPasswordField, 1, 5);

        this.getDialogPane().setContent(dPane);
        this.setTitle("Create Account");

        buttonTypeOk = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        this.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        this.setResultConverter(new Callback<ButtonType, User>() {
            @Override
            public User call(ButtonType b) {
                User user = null;
                if (b == buttonTypeOk) {
                    if (isValidInput()) {
                        String username = usernameField.getText().trim();
                        String password = passwordField.getText();

                        user = new User(username, password);
                    }
                }
                return user;
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
    }

    private boolean isValidInput() {
        invalidMsg = "";
        Boolean isValid = true;
        if (usernameField.getText().trim().isEmpty()) {
            invalidMsg += "Username, ";
            isValid = false;
        }
        if (passwordField.getText().isEmpty()) {
            invalidMsg += "Password, ";
            isValid = false;
        } else if (!checkPasswordField.getText().equals(passwordField.getText())) {
            invalidMsg += "Password does not match, ";
            isValid = false;
        }
        return isValid;
    }

    private String getInvalidMsg() {
        return invalidMsg;
    }

}
