package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Opens a connection to the H2 database.
// H2 saves the database as a file in a "data" folder inside the project.
public class DBConnection {

    private static final String URL = "jdbc:h2:./data/bankingdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
