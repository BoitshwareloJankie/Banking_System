package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Boundary class: the register new customer screen. It only shows things.
public class RegisterView {

    private VBox root;
    private TextField firstNameField;
    private TextField surnameField;
    private TextField addressField;
    private TextField phoneField;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmField;
    private Button registerButton;
    private Button backButton;
    private Label messageLabel;

    public RegisterView() {
        Label title = new Label("Register New Customer");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        firstNameField = new TextField();
        surnameField = new TextField();
        addressField = new TextField();
        phoneField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();
        confirmField = new PasswordField();

        registerButton = new Button("Register");
        backButton = new Button("Back to Login");

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);
        form.add(new Label("First name:"), 0, 0);
        form.add(firstNameField, 1, 0);
        form.add(new Label("Surname:"), 0, 1);
        form.add(surnameField, 1, 1);
        form.add(new Label("Address:"), 0, 2);
        form.add(addressField, 1, 2);
        form.add(new Label("Contact number:"), 0, 3);
        form.add(phoneField, 1, 3);
        form.add(new Label("Username:"), 0, 4);
        form.add(usernameField, 1, 4);
        form.add(new Label("Password:"), 0, 5);
        form.add(passwordField, 1, 5);
        form.add(new Label("Confirm password:"), 0, 6);
        form.add(confirmField, 1, 6);

        HBox buttons = new HBox(10, registerButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        root = new VBox(15, title, form, buttons, messageLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
    }

    public VBox getRoot() { return root; }
    public TextField getFirstNameField() { return firstNameField; }
    public TextField getSurnameField() { return surnameField; }
    public TextField getAddressField() { return addressField; }
    public TextField getPhoneField() { return phoneField; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public PasswordField getConfirmField() { return confirmField; }
    public Button getRegisterButton() { return registerButton; }
    public Button getBackButton() { return backButton; }

    public void showMessage(String text) {
        messageLabel.setText(text);
    }
}
