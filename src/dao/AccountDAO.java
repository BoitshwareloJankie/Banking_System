package dao;

import model.Account;
import model.ChequeAccount;
import model.Customer;
import model.InvestmentAccount;
import model.SavingsAccount;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

// DAO for the account table. It handles all three account types.
public class AccountDAO {

    private TransactionDAO transactionDAO = new TransactionDAO();

    // adds a new account to the database
    public boolean save(Account account) {
        String sql = "INSERT INTO account (account_number, account_type, branch, balance, customer_id, "
                + "employer_name, employer_address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, account.getAccountNumber());
            ps.setString(2, getTypeCode(account));
            ps.setString(3, account.getBranch());
            ps.setDouble(4, account.getBalance());
            ps.setString(5, account.getOwner().getCustomerId());

            // only cheque accounts have employer details
            if (account instanceof ChequeAccount) {
                ChequeAccount cheque = (ChequeAccount) account;
                ps.setString(6, cheque.getEmployerName());
                ps.setString(7, cheque.getEmployerAddress());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not save account: " + e.getMessage());
            return false;
        }
    }

    // saves the new balance after a deposit, withdrawal or interest payment
    public boolean updateBalance(Account account) {
        String sql = "UPDATE account SET balance = ? WHERE account_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, account.getBalance());
            ps.setString(2, account.getAccountNumber());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not update balance: " + e.getMessage());
            return false;
        }
    }

    // loads all accounts of one customer, with their transactions
    public List<Account> findByCustomer(Customer customer) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE customer_id = ? ORDER BY account_number";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getCustomerId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String number = rs.getString("account_number");
                    String type = rs.getString("account_type");
                    String branch = rs.getString("branch");
                    double balance = rs.getDouble("balance");

                    Account account;
                    if (type.equals("SAVINGS")) {
                        account = new SavingsAccount(number, branch, balance, customer);
                    } else if (type.equals("INVESTMENT")) {
                        // "true" means: loaded from the database, so skip the P500 opening check
                        account = new InvestmentAccount(number, branch, balance, customer, true);
                    } else {
                        account = new ChequeAccount(number, branch, balance, customer,
                                rs.getString("employer_name"), rs.getString("employer_address"));
                    }

                    account.getTransactions().addAll(transactionDAO.findByAccount(number));
                    accounts.add(account);
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not load accounts: " + e.getMessage());
        }
        return accounts;
    }

    // turns the account object into the text stored in the account_type column
    private String getTypeCode(Account account) {
        if (account instanceof SavingsAccount) {
            return "SAVINGS";
        } else if (account instanceof InvestmentAccount) {
            return "INVESTMENT";
        } else {
            return "CHEQUE";
        }
    }
}
