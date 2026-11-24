package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import model.Account;
import model.BankEmployee;
import model.ChequeAccount;
import model.Customer;
import model.InvestmentAccount;
import model.SavingsAccount;
import model.Transaction;
import util.DatabaseSetup;

import java.util.List;

// Sits between the controllers and the DAOs. Controllers ask this class,
// and this class asks the database.
public class BankService {

    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    private BankEmployee employee = new BankEmployee("E001", "Neo Kgosi", "Teller");

    public BankService() {
        // makes the tables and the sample customer if they are not there yet
        DatabaseSetup.setup();
    }

    // returns the customer if the login is correct, or null if it is wrong
    public Customer login(String username, String password) {
        Customer customer = customerDAO.findByUsername(username);
        if (customer != null && customer.checkLogin(username, password)) {
            return customer;
        }
        return null;
    }

    // call this after a successful deposit, withdrawal or interest payment.
    // It saves the new balance and the newest transaction.
    public void saveAccountChanges(Account account) {
        accountDAO.updateBalance(account);

        List<Transaction> transactions = account.getTransactions();
        if (!transactions.isEmpty()) {
            transactionDAO.save(transactions.get(transactions.size() - 1));
        }
    }

    // registers a new customer and opens a Savings account for them.
    // Returns the new customer, or null if the username is taken or saving failed.
    public Customer register(String firstName, String surname, String address,
                             String phone, String username, String password) {
        if (customerDAO.findByUsername(username) != null) {
            return null;
        }

        String customerId = customerDAO.nextCustomerId();
        Customer customer = new Customer(customerId, firstName, surname, address, phone, username, password);

        if (!customerDAO.save(customer)) {
            return null;
        }

        // every new customer starts with an empty Savings account, e.g. C011 -> S011
        SavingsAccount savings = new SavingsAccount("S" + customerId.substring(1), "Main Mall", 0.00, customer);
        accountDAO.save(savings);
        customer.addAccount(savings);
        return customer;
    }

    // opens a new account for a customer. type is "Savings", "Investment" or "Cheque".
    // Returns the new account, or null if saving failed.
    // Throws IllegalArgumentException if a rule is broken (for example Investment under P500).
    public Account openAccount(Customer customer, String type, String branch, double openingDeposit,
                               String employerName, String employerAddress) {
        Account account;

        if (type.equals("Savings")) {
            account = new SavingsAccount(accountDAO.nextAccountNumber("S"), branch, openingDeposit, customer);
        } else if (type.equals("Investment")) {
            account = new InvestmentAccount(accountDAO.nextAccountNumber("I"), branch, openingDeposit, customer);
        } else {
            account = new ChequeAccount(accountDAO.nextAccountNumber("CH"), branch, openingDeposit,
                    customer, employerName, employerAddress);
        }

        if (!accountDAO.save(account)) {
            return null;
        }

        // the bank employee opens the account in the model
        employee.openAccount(customer, account);
        return account;
    }

    public BankEmployee getEmployee() {
        return employee;
    }
}