-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS credit_bank;
USE credit_bank;

-- Drop tables if they exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS credit_requests;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;

-- Create users table for authentication
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    user_type ENUM('ADMIN', 'CUSTOMER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create customers table for customer-specific data
CREATE TABLE customers (
    username VARCHAR(50) PRIMARY KEY,
    balance DECIMAL(15, 2) DEFAULT 0.00,
    penalty DECIMAL(15, 2) DEFAULT 0.00,
    credit_approved BOOLEAN DEFAULT FALSE,
    credit_limit DECIMAL(15, 2) DEFAULT 0.00,
    account_status ENUM('ACTIVE', 'SUSPENDED', 'CLOSED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Create transactions table for logging all financial activities
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_username VARCHAR(50) NOT NULL,
    type ENUM('DEPOSIT', 'WITHDRAWAL', 'PENALTY', 'CREDIT_DISBURSEMENT') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    balance_after DECIMAL(15, 2) NOT NULL,
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_username) REFERENCES customers(username) ON DELETE CASCADE,
    INDEX idx_customer_date (customer_username, transaction_date)
);

-- Create credit_requests table for credit applications
CREATE TABLE credit_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_username VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    reason VARCHAR(500) NOT NULL,
    approved BOOLEAN DEFAULT FALSE,
    approved_by VARCHAR(50),
    approved_date TIMESTAMP NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    FOREIGN KEY (customer_username) REFERENCES customers(username) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES users(username),
    INDEX idx_status (status),
    INDEX idx_customer (customer_username)
);

-- Insert default admin user
INSERT INTO users (username, password, user_type) 
VALUES ('admin', 'admin123', 'ADMIN');

-- Insert some sample customer accounts for testing
INSERT INTO users (username, password, user_type) VALUES
    ('john_doe', 'password123', 'CUSTOMER'),
    ('jane_smith', 'password123', 'CUSTOMER'),
    ('bob_wilson', 'password123', 'CUSTOMER');

-- Initialize customer records
INSERT INTO customers (username, balance, penalty, credit_approved, credit_limit) VALUES
    ('john_doe', 5000.00, 0.00, FALSE, 0.00),
    ('jane_smith', 10000.00, 50.00, TRUE, 5000.00),
    ('bob_wilson', 2500.00, 0.00, FALSE, 0.00);

-- Insert sample transactions
INSERT INTO transactions (customer_username, type, amount, balance_after, description) VALUES
    ('john_doe', 'DEPOSIT', 5000.00, 5000.00, 'Initial deposit'),
    ('jane_smith', 'DEPOSIT', 10000.00, 10000.00, 'Initial deposit'),
    ('jane_smith', 'PENALTY', 50.00, 10000.00, 'Late payment penalty'),
    ('bob_wilson', 'DEPOSIT', 2500.00, 2500.00, 'Initial deposit');

-- Insert sample credit requests
INSERT INTO credit_requests (customer_username, amount, reason, approved, status) VALUES
    ('jane_smith', 5000.00, 'Business expansion loan', TRUE, 'APPROVED'),
    ('john_doe', 3000.00, 'Personal emergency loan', FALSE, 'PENDING'),
    ('bob_wilson', 10000.00, 'Home renovation loan', FALSE, 'PENDING');