package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Account;
import model.Customer;
import view.AccountView;
import view.OpenAccountView;

// Controller: connects the open account screen to the model.
public class OpenAccountController {

    private Stage stage;
    private OpenAccountView view;
    private Customer customer;
    private BankService service;

    public OpenAccountController(Stage stage, OpenAccountView view, Customer customer, BankService service) {
        this.stage = stage;
        this.view = view;
        this.customer = customer;
        this.service = service;

        // employer details are only needed for a cheque account
        view.getTypeBox().setOnAction(e -> {
            boolean isCheque = "Cheque".equals(view.getTypeBox().getValue());
            view.getEmployerNameField().setDisable(!isCheque);
            view.getEmployerAddressField().setDisable(!isCheque);
        });

        view.getOpenButton().setOnAction(e -> handleOpen());
        view.getBackButton().setOnAction(e -> backToAccounts(null));
    }

    // shows the open account screen
    public void show() {
        stage.setScene(new Scene(view.getRoot(), 450, 450));
        stage.setTitle("Banking System - Open Account");
    }

    private void handleOpen() {
        String type = view.getTypeBox().getValue();
        String branch = view.getBranchBox().getValue();

        if (type == null) {
            view.showMessage("Please choose an account type.");
            return;
        }

        double deposit;
        try {
            deposit = Double.parseDouble(view.getDepositField().getText().trim());
        } catch (NumberFormatException ex) {
            view.showMessage("Please enter a valid opening deposit.");
            return;
        }

        if (deposit < 0) {
            view.showMessage("The opening deposit cannot be negative.");
            return;
        }

        String employerName = view.getEmployerNameField().getText().trim();
        String employerAddress = view.getEmployerAddressField().getText().trim();

        if (type.equals("Cheque") && (employerName.isEmpty() || employerAddress.isEmpty())) {
            view.showMessage("A cheque account needs the employer name and address.");
            return;
        }

        try {
            Account account = service.openAccount(customer, type, branch, deposit, employerName, employerAddress);

            if (account == null) {
                view.showMessage("The account could not be saved. Please try again.");
                return;
            }

            backToAccounts("New " + account.getAccountType() + " opened: " + account.getAccountNumber());

        } catch (IllegalArgumentException ex) {
            // for example an Investment account with less than P500
            view.showMessage(ex.getMessage());
        }
    }

    // goes back to the account screen, with an optional message
    private void backToAccounts(String message) {
        AccountView accountView = new AccountView();
        if (message != null) {
            accountView.showMessage(message);
        }
        new AccountController(stage, accountView, customer, service).show();
    }
}