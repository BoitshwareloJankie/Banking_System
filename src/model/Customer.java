package model;

import java.util.ArrayList;
import java.util.List;

// A person who banks with us and can own more than one account
public class Customer {

    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String phone;
    private String username;
    private String password;
    private List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String firstName, String surname, String address,
                    String phone, String username, String password) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    // used for login
    public boolean checkLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }   // needed so the DAO can save the customer
    public List<Account> getAccounts() { return accounts; }

    @Override
    public String toString() {
        return customerId + " - " + firstName + " " + surname;
    }
}