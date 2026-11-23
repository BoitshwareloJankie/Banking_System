package model;

import java.time.LocalDateTime;

// Keeps a record of one deposit, withdrawal or interest payment
public class Transaction {

    private static int counter = 1;

    private int transactionId;
    private String accountNumber;
    private String type;          // DEPOSIT, WITHDRAWAL or INTEREST
    private double amount;
    private double balanceAfter;
    private LocalDateTime date;

    // used when a NEW transaction happens
    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this.transactionId = counter++;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.date = LocalDateTime.now();
    }

    // used when an OLD transaction is loaded from the database
    public Transaction(int transactionId, String accountNumber, String type, double amount,
                       double balanceAfter, LocalDateTime date) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.date = date;
    }

    public int getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getDate() { return date; }

    // the database makes the real ID when the transaction is saved
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    @Override
    public String toString() {
        return transactionId + " | " + date + " | " + type + " | P" + amount
                + " | Balance: P" + balanceAfter;
    }
}