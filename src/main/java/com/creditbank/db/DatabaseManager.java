package com.creditbank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to the database.
 * This class follows the Singleton pattern to ensure only one connection instance is created.
 */
public class DatabaseManager {

    // --- IMPORTANT ---
    // UPDATE THESE VALUES WITH YOUR MYSQL USERNAME AND PASSWORD
    private static final String DB_URL = "jdbc:mysql://localhost:3306/credit_bank";
    private static final String DB_USER = "root"; // <-- CHANGE THIS
    private static final String DB_PASSWORD = "password"; // <-- CHANGE THIS

    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseManager() {}

    /**
     * Returns the singleton instance of the database connection.
     * If the connection does not exist or is closed, it creates a new one.
     *
     * @return The active database connection.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Register the MySQL driver and establish the connection
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (ClassNotFoundException e) {
                // This error occurs if the MySQL Connector/J JAR is not on the classpath
                System.err.println("MySQL JDBC Driver not found. Make sure it's in your classpath.");
                throw new SQLException("JDBC Driver not found", e);
            }
        }
        return connection;
    }

    /**
     * Closes the database connection if it is open.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
            }
        }
    }
}
