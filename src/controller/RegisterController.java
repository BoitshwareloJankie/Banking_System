package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;
import view.LoginView;
import view.RegisterView;

// Controller: connects the register screen to the model.
public class RegisterController {

    private Stage stage;
    private RegisterView view;
    private BankService service;

    public RegisterController(Stage stage, RegisterView view, BankService service) {
        this.stage = stage;
        this.view = view;
        this.service = service;

        view.getRegisterButton().setOnAction(e -> handleRegister());
        view.getBackButton().setOnAction(e -> backToLogin(null));
    }

    // shows the register screen
    public void show() {
        stage.setScene(new Scene(view.getRoot(), 450, 520));
        stage.setTitle("Banking System - Register");
    }

    private void handleRegister() {
        String firstName = view.getFirstNameField().getText().trim();
        String surname = view.getSurnameField().getText().trim();
        String address = view.getAddressField().getText().trim();
        String phone = view.getPhoneField().getText().trim();
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();
        String confirm = view.getConfirmField().getText();

        if (firstName.isEmpty() || surname.isEmpty() || address.isEmpty()
                || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
            view.showMessage("Please fill in every field.");
            return;
        }

        if (!phone.matches("[0-9 +]{7,15}")) {
            view.showMessage("Contact number must be 7 to 15 digits.");
            return;
        }

        if (!password.equals(confirm)) {
            view.showMessage("The two passwords do not match.");
            return;
        }

        Customer customer = service.register(firstName, surname, address, phone, username, password);

        if (customer == null) {
            view.showMessage("That username is already taken. Please choose another.");
            return;
        }

        backToLogin("Registered! Your customer ID is " + customer.getCustomerId()
                + ". You can log in now.");
    }

    // goes back to the login screen, with an optional green message
    private void backToLogin(String successMessage) {
        LoginView loginView = new LoginView();
        if (successMessage != null) {
            loginView.showSuccess(successMessage);
        }
        new LoginController(stage, loginView, service).show();
    }
}
