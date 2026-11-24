package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Account;
import model.Transaction;

// Boundary class: shows a customer's accounts, balance and transactions.
public class AccountView {

    private VBox root;
    private Label welcomeLabel;
    private ComboBox<Account> accountBox;
    private Label typeLabel;
    private Label balanceLabel;
    private TextField amountField;
    private Button depositButton;
    private Button withdrawButton;
    private Button payInterestButton;
    private Button openAccountButton;
    private Button logoutButton;
    private TableView<Transaction> transactionTable;
    private Label messageLabel;

    public AccountView() {
        welcomeLabel = new Label("Welcome");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        accountBox = new ComboBox<>();
        accountBox.setPromptText("Choose an account");

        typeLabel = new Label("Account type: -");
        balanceLabel = new Label("Balance: P0.00");
        balanceLabel.setStyle("-fx-font-size: 16px;");

        amountField = new TextField();
        amountField.setPromptText("Amount");

        depositButton = new Button("Deposit");
        withdrawButton = new Button("Withdraw");
        payInterestButton = new Button("Pay Interest");
        openAccountButton = new Button("Open New Account");
        logoutButton = new Button("Logout");

        HBox buttons = new HBox(10, depositButton, withdrawButton, payInterestButton);

        transactionTable = new TableView<>();
        transactionTable.getColumns().add(makeColumn("ID", "transactionId"));
        transactionTable.getColumns().add(makeColumn("Type", "type"));
        transactionTable.getColumns().add(makeColumn("Amount", "amount"));
        transactionTable.getColumns().add(makeColumn("Balance After", "balanceAfter"));
        transactionTable.getColumns().add(makeColumn("Date", "date"));
        transactionTable.setPrefHeight(200);

        messageLabel = new Label();

        root = new VBox(10, welcomeLabel, accountBox, typeLabel, balanceLabel,
                amountField, buttons, new Label("Transactions:"),
                transactionTable, messageLabel, new HBox(10, openAccountButton, logoutButton));
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(20));
    }

    // helper so we do not repeat the same code for every column
    private TableColumn<Transaction, Object> makeColumn(String title, String property) {
        TableColumn<Transaction, Object> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    public VBox getRoot() { return root; }
    public Label getWelcomeLabel() { return welcomeLabel; }
    public ComboBox<Account> getAccountBox() { return accountBox; }
    public Label getTypeLabel() { return typeLabel; }
    public Label getBalanceLabel() { return balanceLabel; }
    public TextField getAmountField() { return amountField; }
    public Button getDepositButton() { return depositButton; }
    public Button getWithdrawButton() { return withdrawButton; }
    public Button getPayInterestButton() { return payInterestButton; }
    public Button getOpenAccountButton() { return openAccountButton; }
    public Button getLogoutButton() { return logoutButton; }
    public TableView<Transaction> getTransactionTable() { return transactionTable; }

    public void showMessage(String text) {
        messageLabel.setText(text);
    }
}