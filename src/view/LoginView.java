package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

// Boundary class: the login screen. It only shows things, the controller will add the logic later.
public class LoginView {

    private VBox root;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label messageLabel;

    public LoginView() {
        Label title = new Label("Banking System");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        loginButton = new Button("Login");

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);
        form.add(new Label("Username:"), 0, 0);
        form.add(usernameField, 1, 0);
        form.add(new Label("Password:"), 0, 1);
        form.add(passwordField, 1, 1);

        root = new VBox(15, title, form, loginButton, messageLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
    }

    public VBox getRoot() { return root; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button getLoginButton() { return loginButton; }

    public void showMessage(String text) {
        messageLabel.setText(text);
    }
}