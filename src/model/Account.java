package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    protected String accountNumber;
    protected String branch;
    protected double balance;
    protected Customer owner;
    protected List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String branch, double openingBalance, Customer owner) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.balance = openingBalance;
        this.owner = owner;
    }

    // every account type can receive money
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit must be more than 0.");
            return;
        }
        balance += amount;
        transactions.add(new Transaction(accountNumber, "DEPOSIT", amount, balance));
    }

    // each account type says what it is called
    public abstract String getAccountType();

    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public double getBalance() { return balance; }
    public Customer getOwner() { return owner; }
    public List<Transaction> getTransactions() { return transactions; }

    @Override
    public String toString() {
        return getAccountType() + " " + accountNumber + " (" + branch + ") Balance: P" + balance;
    }
}





