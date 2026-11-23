package model;

public class ChequeAccount  extends Account implements Withdrawable {

    private String employerName;
    private String employerAddress;

    public ChequeAccount(String accountNumber, String branch, double openingBalance,
                         Customer owner, String employerName, String employerAddress) {
        super(accountNumber, branch, openingBalance, owner);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
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

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }

    @Override
    public String getAccountType() {
        return "Cheque Account";
    }
}
