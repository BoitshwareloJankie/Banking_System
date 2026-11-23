package controller;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Account;
import model.Customer;
import model.InterestBearing;
import model.Withdrawable;
import view.AccountView;
import view.LoginView;

// Controller: connects the account screen to the model.
public class AccountController {

    private Stage stage;
    private AccountView view;
    private Customer customer;
    private BankService service;

    public AccountController(Stage stage, AccountView view, Customer customer, BankService service) {
        this.stage = stage;
        this.view = view;
        this.customer = customer;
        this.service = service;

        setupScreen();
        setupButtons();
    }

    // shows the account screen
    public void show() {
        stage.setScene(new Scene(view.getRoot(), 650, 600));
        stage.setTitle("Banking System - Accounts");
    }

    // fills the screen with the customer's details
    private void setupScreen() {
        view.getWelcomeLabel().setText("Welcome, " + customer.getFirstName() + " " + customer.getSurname());

        // show "Savings Account - S001" in the dropdown (no balance, so it never goes out of date)
        view.getAccountBox().setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account account) {
                if (account == null) {
                    return "";
                }
                return account.getAccountType() + " - " + account.getAccountNumber();
            }

            @Override
            public Account fromString(String text) {
                return null;
            }
        });

        view.getAccountBox().setItems(FXCollections.observableArrayList(customer.getAccounts()));

        // when the customer picks a different account, update the screen
        view.getAccountBox().setOnAction(e -> updateScreen());

        view.getDepositButton().setDisable(true);
        view.getWithdrawButton().setDisable(true);
        view.getPayInterestButton().setDisable(true);
    }

    private void setupButtons() {
        view.getDepositButton().setOnAction(e -> handleDeposit());
        view.getWithdrawButton().setOnAction(e -> handleWithdraw());
        view.getPayInterestButton().setOnAction(e -> handlePayInterest());
        view.getLogoutButton().setOnAction(e -> handleLogout());
    }

    private Account getSelectedAccount() {
        return view.getAccountBox().getValue();
    }

    // refreshes the labels, buttons and table for the chosen account
    private void updateScreen() {
        Account account = getSelectedAccount();
        if (account == null) {
            return;
        }

        view.getTypeLabel().setText("Account type: " + account.getAccountType());
        view.getBalanceLabel().setText(String.format("Balance: P%.2f", account.getBalance()));
        view.getTransactionTable().setItems(FXCollections.observableArrayList(account.getTransactions()));

        // only turn on the buttons that this account type is allowed to use
        view.getDepositButton().setDisable(false);
        view.getWithdrawButton().setDisable(!(account instanceof Withdrawable));
        view.getPayInterestButton().setDisable(!(account instanceof InterestBearing));
    }

    // reads the amount the customer typed, or returns null if it is not valid
    private Double readAmount() {
        try {
            double amount = Double.parseDouble(view.getAmountField().getText().trim());
            if (amount <= 0) {
                view.showMessage("The amount must be more than 0.");
                return null;
            }
            return amount;
        } catch (NumberFormatException ex) {
            view.showMessage("Please enter a valid number for the amount.");
            return null;
        }
    }

    private void handleDeposit() {
        Account account = getSelectedAccount();
        if (account == null) {
            view.showMessage("Choose an account first.");
            return;
        }

        Double amount = readAmount();
        if (amount == null) {
            return;
        }

        account.deposit(amount);
        service.saveAccountChanges(account);   // save to the database
        updateScreen();
        view.getAmountField().clear();
        view.showMessage("Deposit successful.");
    }

    private void handleWithdraw() {
        Account account = getSelectedAccount();
        if (account == null) {
            view.showMessage("Choose an account first.");
            return;
        }

        if (!(account instanceof Withdrawable)) {
            view.showMessage("This account does not allow withdrawals.");
            return;
        }

        Double amount = readAmount();
        if (amount == null) {
            return;
        }

        boolean worked = ((Withdrawable) account).withdraw(amount);

        if (worked) {
            service.saveAccountChanges(account);   // save to the database
            updateScreen();
            view.getAmountField().clear();
            view.showMessage("Withdrawal successful.");
        } else {
            updateScreen();
            view.showMessage("Withdrawal failed. You do not have enough money.");
        }
    }

    private void handlePayInterest() {
        Account account = getSelectedAccount();
        if (account == null) {
            view.showMessage("Choose an account first.");
            return;
        }

        if (!(account instanceof InterestBearing)) {
            view.showMessage("This account does not earn interest.");
            return;
        }

        // the bank employee is the one who pays interest
        service.getEmployee().payInterest(account);
        service.saveAccountChanges(account);   // save to the database
        updateScreen();
        view.showMessage("Interest paid.");
    }

    // goes back to the login screen
    private void handleLogout() {
        new LoginController(stage, new LoginView(), service).show();
    }
}