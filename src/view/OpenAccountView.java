package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Boundary class: the screen for opening a new account. It only shows things.
public class OpenAccountView {

    private VBox root;
    private ComboBox<String> typeBox;
    private ComboBox<String> branchBox;
    private TextField depositField;
    private TextField employerNameField;
    private TextField employerAddressField;
    private Button openButton;
    private Button backButton;
    private Label messageLabel;

    public OpenAccountView() {
        Label title = new Label("Open a New Account");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Savings", "Investment", "Cheque");
        typeBox.setPromptText("Choose a type");

        branchBox = new ComboBox<>();
        branchBox.getItems().addAll("Main Mall", "Francistown");
        branchBox.setValue("Main Mall");

        depositField = new TextField();
        depositField.setPromptText("Opening deposit");

        employerNameField = new TextField();
        employerNameField.setPromptText("Cheque accounts only");
        employerNameField.setDisable(true);

        employerAddressField = new TextField();
        employerAddressField.setPromptText("Cheque accounts only");
        employerAddressField.setDisable(true);

        openButton = new Button("Open Account");
        backButton = new Button("Back");

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);
        form.add(new Label("Account type:"), 0, 0);
        form.add(typeBox, 1, 0);
        form.add(new Label("Branch:"), 0, 1);
        form.add(branchBox, 1, 1);
        form.add(new Label("Opening deposit (P):"), 0, 2);
        form.add(depositField, 1, 2);
        form.add(new Label("Employer name:"), 0, 3);
        form.add(employerNameField, 1, 3);
        form.add(new Label("Employer address:"), 0, 4);
        form.add(employerAddressField, 1, 4);

        HBox buttons = new HBox(10, openButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        root = new VBox(15, title, form, buttons, messageLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
    }

    public VBox getRoot() { return root; }
    public ComboBox<String> getTypeBox() { return typeBox; }
    public ComboBox<String> getBranchBox() { return branchBox; }
    public TextField getDepositField() { return depositField; }
    public TextField getEmployerNameField() { return employerNameField; }
    public TextField getEmployerAddressField() { return employerAddressField; }
    public Button getOpenButton() { return openButton; }
    public Button getBackButton() { return backButton; }

    public void showMessage(String text) {
        messageLabel.setText(text);
    }
}
