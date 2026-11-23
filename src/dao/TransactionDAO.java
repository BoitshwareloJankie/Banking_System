package dao;

import model.Transaction;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// DAO for the bank_transaction table.
public class TransactionDAO {

    // adds a transaction to the database and gives it the ID the database made
    public boolean save(Transaction transaction) {
        String sql = "INSERT INTO bank_transaction (account_number, transaction_type, amount, "
                + "balance_after, transaction_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, transaction.getAccountNumber());
            ps.setString(2, transaction.getType());
            ps.setDouble(3, transaction.getAmount());
            ps.setDouble(4, transaction.getBalanceAfter());
            ps.setTimestamp(5, Timestamp.valueOf(transaction.getDate()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    transaction.setTransactionId(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.out.println("Could not save transaction: " + e.getMessage());
            return false;
        }
    }

    // loads all transactions of one account, oldest first
    public List<Transaction> findByAccount(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM bank_transaction WHERE account_number = ? ORDER BY transaction_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getString("account_number"),
                            rs.getString("transaction_type"),
                            rs.getDouble("amount"),
                            rs.getDouble("balance_after"),
                            rs.getTimestamp("transaction_date").toLocalDateTime()));
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not load transactions: " + e.getMessage());
        }
        return transactions;
    }
}