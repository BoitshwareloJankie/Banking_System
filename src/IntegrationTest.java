import controller.BankService;
import model.Account;
import model.Customer;
import model.Withdrawable;

// Integration test: checks that login, the model and the database work together.
// It does the same steps a user does in the GUI, but prints PASS or FAIL for each one.
public class IntegrationTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        // 1. start the service (this opens the database and adds the sample data)
        BankService service = new BankService();

        System.out.println("=== Step 1: Login ===");
        check("Wrong password is refused", service.login("thato", "wrong") == null);
        check("Unknown username is refused", service.login("nobody", "pass123") == null);

        Customer customer = service.login("thato", "pass123");
        check("Correct login works", customer != null);
        if (customer == null) {
            System.out.println("Cannot continue without a login.");
            return;
        }
        check("Accounts were loaded from the database", customer.getAccounts().size() >= 2);

        Account savings = findAccount(customer, "S001");
        Account investment = findAccount(customer, "I001");
        if (savings == null || investment == null) {
            System.out.println("Sample accounts not found. Cannot continue.");
            return;
        }

        // 2. deposit
        System.out.println("\n=== Step 2: Deposit into savings ===");
        double savingsBefore = savings.getBalance();
        int savingsTransactionsBefore = savings.getTransactions().size();
        savings.deposit(500.00);
        service.saveAccountChanges(savings);
        check("Balance went up by P500", close(savings.getBalance(), savingsBefore + 500.00));
        check("One transaction was added", savings.getTransactions().size() == savingsTransactionsBefore + 1);
        check("Savings account cannot withdraw", !(savings instanceof Withdrawable));

        // 3. withdraw
        System.out.println("\n=== Step 3: Withdraw from investment ===");
        double investmentBefore = investment.getBalance();
        boolean worked = ((Withdrawable) investment).withdraw(100.00);
        service.saveAccountChanges(investment);
        check("Withdrawal of P100 worked", worked);
        check("Balance went down by P100", close(investment.getBalance(), investmentBefore - 100.00));

        boolean tooMuch = ((Withdrawable) investment).withdraw(99999999.00);
        check("Withdrawing more than the balance is refused", !tooMuch);

        // 4. interest
        System.out.println("\n=== Step 4: Pay interest on investment ===");
        double beforeInterest = investment.getBalance();
        service.getEmployee().payInterest(investment);
        service.saveAccountChanges(investment);
        check("Interest was added to the balance", investment.getBalance() > beforeInterest);

        // 5. load everything again from the database, like a new login
        System.out.println("\n=== Step 5: Check the database saved everything ===");
        BankService freshService = new BankService();
        Customer reloaded = freshService.login("thato", "pass123");
        check("Login works again", reloaded != null);

        Account savingsReloaded = findAccount(reloaded, "S001");
        Account investmentReloaded = findAccount(reloaded, "I001");
        check("Savings balance was saved",
                savingsReloaded != null && close(savingsReloaded.getBalance(), savings.getBalance()));
        check("Savings transactions were saved",
                savingsReloaded != null && savingsReloaded.getTransactions().size() == savings.getTransactions().size());
        check("Investment balance was saved",
                investmentReloaded != null && close(investmentReloaded.getBalance(), investment.getBalance()));
        check("Investment transactions were saved",
                investmentReloaded != null && investmentReloaded.getTransactions().size() == investment.getTransactions().size());

        // summary
        System.out.println("\n=== Result: " + passed + " passed, " + failed + " failed ===");
    }

    // looks for one of the customer's accounts by its number
    private static Account findAccount(Customer customer, String accountNumber) {
        if (customer == null) {
            return null;
        }
        for (Account account : customer.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    // money amounts are doubles, so compare them with a tiny margin
    private static boolean close(double a, double b) {
        return Math.abs(a - b) < 0.01;
    }

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + description);
        } else {
            failed++;
            System.out.println("FAIL: " + description);
        }
    }
}
