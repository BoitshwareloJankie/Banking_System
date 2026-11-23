package model;

import model.*;

// Console test for the core model (no GUI or database yet)
public class Main {

    public static void main(String[] args) {

        // 1. Create a customer and an employee
        Customer customer = new Customer("C001", "Thato", "Molefe", "Gaborone",
                "71234567", "thato", "pass123");
        BankEmployee employee = new BankEmployee("E001", "Neo Kgosi", "Teller");

        System.out.println("=== Test 1: Login ===");
        System.out.println("Correct login: " + customer.checkLogin("thato", "pass123"));
        System.out.println("Wrong login: " + customer.checkLogin("thato", "wrong"));

        // 2. Employee opens one of each account type
        System.out.println("\n=== Test 2: Open accounts ===");
        SavingsAccount savings = new SavingsAccount("S001", "Main Mall", 1000.00, customer);
        InvestmentAccount investment = new InvestmentAccount("I001", "Main Mall", 2000.00, customer);
        ChequeAccount cheque = new ChequeAccount("CH001", "Main Mall", 500.00, customer,
                "Kgalagadi Traders", "Gaborone");

        employee.openAccount(customer, savings);
        employee.openAccount(customer, investment);
        employee.openAccount(customer, cheque);

        for (Account a : customer.getAccounts()) {
            System.out.println(a);
        }

        // 3. Investment account needs at least P500
        System.out.println("\n=== Test 3: Investment minimum ===");
        try {
            new InvestmentAccount("I002", "Main Mall", 100.00, customer);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // 4. Deposits
        System.out.println("\n=== Test 4: Deposits ===");
        savings.deposit(200.00);
        cheque.deposit(300.00);
        savings.deposit(-50.00);  // should be refused
        System.out.println(savings);
        System.out.println(cheque);

        // 5. Withdrawals (only Investment and Cheque can withdraw)
        System.out.println("\n=== Test 5: Withdrawals ===");
        System.out.println("Cheque withdraw P100: " + cheque.withdraw(100.00));
        System.out.println("Cheque withdraw P5000: " + cheque.withdraw(5000.00));
        System.out.println("Investment withdraw P500: " + investment.withdraw(500.00));
        System.out.println("Is Savings withdrawable? " + (savings instanceof Withdrawable));

        // 6. Interest (only Savings and Investment earn it)
        System.out.println("\n=== Test 6: Pay interest ===");
        employee.payInterest(savings);
        employee.payInterest(investment);
        employee.payInterest(cheque);   // should say it does not earn interest
        System.out.println(savings);
        System.out.println(investment);
        System.out.println(cheque);

        // 7. Transaction history
        System.out.println("\n=== Test 7: Transactions for savings ===");
        for (Transaction t : savings.getTransactions()) {
            System.out.println(t);
        }
    }
}
