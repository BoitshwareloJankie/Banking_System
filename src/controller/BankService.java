package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import model.Account;
import model.BankEmployee;
import model.Customer;
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

    public BankEmployee getEmployee() {
        return employee;
    }
}