package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;
import view.AccountView;
import view.LoginView;

// Controller: connects the login screen to the model.
public class LoginController {

    private Stage stage;
    private LoginView view;
    private BankService service;

    public LoginController(Stage stage, LoginView view, BankService service) {
        this.stage = stage;
        this.view = view;
        this.service = service;

        // what happens when Login is clicked
        view.getLoginButton().setOnAction(e -> handleLogin());
    }

    // shows the login screen
    public void show() {
        stage.setScene(new Scene(view.getRoot(), 400, 300));
        stage.setTitle("Banking System - Login");
        stage.show();
    }

    private void handleLogin() {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("Please enter your username and password.");
            return;
        }

        Customer customer = service.login(username, password);

        if (customer == null) {
            view.showMessage("Wrong username or password.");
            return;
        }

        // login worked, so open the account screen
        AccountView accountView = new AccountView();
        new AccountController(stage, accountView, customer, service).show();
    }
}