package dao;

import model.Account;
import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// DAO = Data Access Object. This class talks to the customer table.
public class CustomerDAO {

    private AccountDAO accountDAO = new AccountDAO();

    // adds a new customer to the database
    public boolean save(Customer customer) {
        String sql = "INSERT INTO customer (customer_id, first_name, surname, address, phone, username, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getFirstName());
            ps.setString(3, customer.getSurname());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getPhone());
            ps.setString(6, customer.getUsername());
            ps.setString(7, customer.getPassword());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Could not save customer: " + e.getMessage());
            return false;
        }
    }

    // finds a customer by username (and loads their accounts), or returns null
    public Customer findByUsername(String username) {
        String sql = "SELECT * FROM customer WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer(
                            rs.getString("customer_id"),
                            rs.getString("first_name"),
                            rs.getString("surname"),
                            rs.getString("address"),
                            rs.getString("phone"),
                            rs.getString("username"),
                            rs.getString("password"));

                    for (Account account : accountDAO.findByCustomer(customer)) {
                        customer.addAccount(account);
                    }
                    return customer;
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not find customer: " + e.getMessage());
        }
        return null;
    }
}
