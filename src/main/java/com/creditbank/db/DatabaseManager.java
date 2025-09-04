package com.creditbank.db;

import com.creditbank.model.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database manager class handling all JDBC operations
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/credit_bank";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Change this to your MySQL password
    
    private Connection connection;
    
    /**
     * Constructor - establishes database connection
     */
    public DatabaseManager() throws Exception {
        // Load driver and attempt to connect. Throw on failure so caller can handle it.
        Class.forName("com.mysql.cj.jdbc.Driver");
        connect();
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Failed to establish database connection to " + DB_URL);
        }
    }
    
    /**
     * Establishes connection to the database
     */
    private void connect() throws SQLException {
        // Let SQLException propagate to caller for proper handling
        connection = DriverManager.getConnection(
            DB_URL + "?useSSL=false&serverTimezone=UTC",
            DB_USER,
            DB_PASSWORD
        );
        System.out.println("Database connected successfully!");
    }
    
    /**
     * Ensures database connection is active
     */
    private void ensureConnected() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
    }
    
    /**
     * Authenticates a user and returns their role
     */
    public String authenticateUser(String username, String password) {
        String query = "SELECT user_type FROM users WHERE username = ? AND password = ?";
        try {
            ensureConnected();
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("user_type");
                        return role != null ? role.toUpperCase() : null;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Gets customer details including financial information
     */
    public Customer getCustomerDetails(String username) {
        String query = "SELECT c.*, u.password FROM customers c " +
                      "JOIN users u ON c.username = u.username " +
                      "WHERE c.username = ?";
        try {
            ensureConnected();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getBigDecimal("balance"),
                    rs.getBigDecimal("penalty"),
                    rs.getBoolean("credit_approved"),
                    rs.getBigDecimal("credit_limit")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer details: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Registers a new customer
     */
    public boolean registerCustomer(String username, String password) {
        Connection conn = null;
        try {
            ensureConnected();
            conn = connection;
            conn.setAutoCommit(false);
            
            // Insert into users table
            String userQuery = "INSERT INTO users (username, password, user_type) VALUES (?, ?, 'CUSTOMER')";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, username);
            userStmt.setString(2, password);
            userStmt.executeUpdate();
            
            // Insert into customers table
            String customerQuery = "INSERT INTO customers (username, balance, penalty, credit_approved) " +
                                 "VALUES (?, 0.00, 0.00, FALSE)";
            PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
            customerStmt.setString(1, username);
            customerStmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error registering customer: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gets all customers in the system
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT c.*, u.password FROM customers c " +
                      "JOIN users u ON c.username = u.username";
        
        try {
            ensureConnected();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getBigDecimal("balance"),
                    rs.getBigDecimal("penalty"),
                    rs.getBoolean("credit_approved"),
                    rs.getBigDecimal("credit_limit")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customers: " + e.getMessage());
        }
        return customers;
    }
    
    /**
     * Gets transactions for a specific customer
     */
    public List<Transaction> getCustomerTransactions(String username) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE customer_username = ? " +
                      "ORDER BY transaction_date DESC";
        
        try {
            ensureConnected();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setCustomerUsername(rs.getString("customer_username"));
                transaction.setType(rs.getString("type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));
                transaction.setDescription(rs.getString("description"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    /**
     * Gets all credit requests
     */
    public List<CreditRequest> getAllCreditRequests() {
        List<CreditRequest> requests = new ArrayList<>();
        String query = "SELECT * FROM credit_requests ORDER BY request_date DESC";
        
        try {
            ensureConnected();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                CreditRequest request = new CreditRequest();
                request.setId(rs.getInt("id"));
                request.setCustomerUsername(rs.getString("customer_username"));
                request.setAmount(rs.getBigDecimal("amount"));
                request.setReason(rs.getString("reason"));
                request.setApproved(rs.getBoolean("approved"));
                request.setApprovedBy(rs.getString("approved_by"));
                request.setApprovedDate(rs.getTimestamp("approved_date"));
                request.setRequestDate(rs.getTimestamp("request_date"));
                request.setStatus(rs.getString("status"));
                requests.add(request);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching credit requests: " + e.getMessage());
        }
        return requests;
    }
    
    /**
     * Approves a credit request
     */
    public boolean approveCreditRequest(int requestId, String approvedBy) {
        Connection conn = null;
        try {
            ensureConnected();
            conn = connection;
            conn.setAutoCommit(false);
            
            // Get request details
            String getQuery = "SELECT * FROM credit_requests WHERE id = ?";
            PreparedStatement getStmt = conn.prepareStatement(getQuery);
            getStmt.setInt(1, requestId);
            ResultSet rs = getStmt.executeQuery();
            
            if (!rs.next()) {
                return false;
            }
            
            String customerUsername = rs.getString("customer_username");
            BigDecimal amount = rs.getBigDecimal("amount");
            
            // Update credit request
            String updateQuery = "UPDATE credit_requests SET approved = TRUE, status = 'APPROVED', " +
                               "approved_by = ?, approved_date = NOW() WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, approvedBy);
            updateStmt.setInt(2, requestId);
            updateStmt.executeUpdate();
            
            // Update customer credit status
            String customerQuery = "UPDATE customers SET credit_approved = TRUE, credit_limit = ? " +
                                 "WHERE username = ?";
            PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
            customerStmt.setBigDecimal(1, amount);
            customerStmt.setString(2, customerUsername);
            customerStmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error approving credit request: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Performs a deposit transaction
     */
    public boolean performDeposit(String username, BigDecimal amount) {
        Connection conn = null;
        try {
            ensureConnected();
            conn = connection;
            conn.setAutoCommit(false);
            
            // Get current balance
            String balanceQuery = "SELECT balance FROM customers WHERE username = ?";
            PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery);
            balanceStmt.setString(1, username);
            ResultSet rs = balanceStmt.executeQuery();
            
            if (!rs.next()) {
                return false;
            }
            
            BigDecimal currentBalance = rs.getBigDecimal("balance");
            BigDecimal newBalance = currentBalance.add(amount);
            
            // Update balance
            String updateQuery = "UPDATE customers SET balance = ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setBigDecimal(1, newBalance);
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();
            
            // Log transaction
            String transQuery = "INSERT INTO transactions (customer_username, type, amount, " +
                              "balance_after, description) VALUES (?, 'DEPOSIT', ?, ?, ?)";
            PreparedStatement transStmt = conn.prepareStatement(transQuery);
            transStmt.setString(1, username);
            transStmt.setBigDecimal(2, amount);
            transStmt.setBigDecimal(3, newBalance);
            transStmt.setString(4, "Deposit transaction");
            transStmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error performing deposit: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Performs a withdrawal transaction
     */
    public boolean performWithdrawal(String username, BigDecimal amount) {
        Connection conn = null;
        try {
            ensureConnected();
            conn = connection;
            conn.setAutoCommit(false);
            
            // Get current balance
            String balanceQuery = "SELECT balance FROM customers WHERE username = ?";
            PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery);
            balanceStmt.setString(1, username);
            ResultSet rs = balanceStmt.executeQuery();
            
            if (!rs.next()) {
                return false;
            }
            
            BigDecimal currentBalance = rs.getBigDecimal("balance");
            
            // Check if sufficient balance
            if (currentBalance.compareTo(amount) < 0) {
                return false;
            }
            
            BigDecimal newBalance = currentBalance.subtract(amount);
            
            // Update balance
            String updateQuery = "UPDATE customers SET balance = ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setBigDecimal(1, newBalance);
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();
            
            // Log transaction
            String transQuery = "INSERT INTO transactions (customer_username, type, amount, " +
                              "balance_after, description) VALUES (?, 'WITHDRAWAL', ?, ?, ?)";
            PreparedStatement transStmt = conn.prepareStatement(transQuery);
            transStmt.setString(1, username);
            transStmt.setBigDecimal(2, amount);
            transStmt.setBigDecimal(3, newBalance);
            transStmt.setString(4, "Withdrawal transaction");
            transStmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error performing withdrawal: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Submits a new credit request
     */
    public boolean submitCreditRequest(String username, BigDecimal amount, String reason) {
        String query = "INSERT INTO credit_requests (customer_username, amount, reason, " +
                      "approved, status) VALUES (?, ?, ?, FALSE, 'PENDING')";
        
        try {
            ensureConnected();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setBigDecimal(2, amount);
            stmt.setString(3, reason);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error submitting credit request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Imposes a penalty on a customer
     */
    public boolean imposePenalty(String username, BigDecimal amount, String reason) {
        Connection conn = null;
        try {
            ensureConnected();
            conn = connection;
            conn.setAutoCommit(false);
            
            // Get current penalty and balance
            String query = "SELECT penalty, balance FROM customers WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (!rs.next()) {
                return false;
            }
            
            BigDecimal currentPenalty = rs.getBigDecimal("penalty");
            BigDecimal currentBalance = rs.getBigDecimal("balance");
            BigDecimal newPenalty = currentPenalty.add(amount);
            
            // Update penalty
            String updateQuery = "UPDATE customers SET penalty = ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setBigDecimal(1, newPenalty);
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();
            
            // Log as transaction
            String transQuery = "INSERT INTO transactions (customer_username, type, amount, " +
                              "balance_after, description) VALUES (?, 'PENALTY', ?, ?, ?)";
            PreparedStatement transStmt = conn.prepareStatement(transQuery);
            transStmt.setString(1, username);
            transStmt.setBigDecimal(2, amount);
            transStmt.setBigDecimal(3, currentBalance);
            transStmt.setString(4, "Penalty: " + reason);
            transStmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Error imposing penalty: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes the database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}