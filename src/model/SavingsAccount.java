package model;

public class SavingsAccount extends Account implements InterestBearing {

    private static final double MONTHLY_INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, String branch, double openingBalance, Customer owner) {
        super(accountNumber, branch, openingBalance, owner);
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
    public String getAccountType() {
        return "Savings Account";
    }
}

