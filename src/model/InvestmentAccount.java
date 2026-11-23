package model;

// Earns interest AND allows withdrawals, but needs a minimum opening amount
public class InvestmentAccount extends Account implements InterestBearing, Withdrawable {

    private static final double MONTHLY_INTEREST_RATE = 0.05; // 5%
    private static final double MINIMUM_OPENING_BALANCE = 500.00;

    // used when a NEW account is opened (checks the minimum amount)
    public InvestmentAccount(String accountNumber, String branch, double openingBalance, Customer owner) {
        super(accountNumber, branch, openingBalance, owner);
        if (openingBalance < MINIMUM_OPENING_BALANCE) {
            throw new IllegalArgumentException(
                    "Investment account needs at least P" + MINIMUM_OPENING_BALANCE + " to open.");
        }
    }

    // used when an OLD account is loaded from the database.
    // The balance may be below P500 after withdrawals, so we do not check it here.
    public InvestmentAccount(String accountNumber, String branch, double balance, Customer owner,
                             boolean loadedFromDatabase) {
        super(accountNumber, branch, balance, owner);
    }

    @Override
    public double calculateInterest() {
        return balance * MONTHLY_INTEREST_RATE;
    }

    @Override
    public void payInterest() {
        double interest = calculateInterest();
        balance += interest;
        transactions.add(new Transaction(accountNumber, "INTEREST", interest, balance));
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            System.out.println("Withdrawal not allowed. Check the amount.");
            return false;
        }
        balance -= amount;
        transactions.add(new Transaction(accountNumber, "WITHDRAWAL", amount, balance));
        return true;
    }

    @Override
    public String getAccountType() {
        return "Investment Account";
    }
}