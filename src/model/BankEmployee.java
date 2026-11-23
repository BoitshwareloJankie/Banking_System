package model;

public class BankEmployee {


    private String employeeId;
    private String name;
    private String position;

    public BankEmployee(String employeeId, String name, String position) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
    }

    // opens an account for a customer
    public void openAccount(Customer customer, Account account) {
        customer.addAccount(account);
    }

    // pays interest on one account, only if it earns interest
    public void payInterest(Account account) {
        if (account instanceof InterestBearing) {
            ((InterestBearing) account).payInterest();
        } else {
            System.out.println(account.getAccountType() + " does not earn interest.");
        }
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getPosition() { return position; }

    @Override
    public String toString() {
        return employeeId + " - " + name + " (" + position + ")";
    }
}

