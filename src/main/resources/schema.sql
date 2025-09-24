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
    ('hashtag_harsh', 'password123', 'CUSTOMER'),
    ('artist_prakshi', 'password123', 'CUSTOMER');

-- Initialize customer records
INSERT INTO customers (username, balance, penalty, credit_approved, credit_limit) VALUES
    ('hashtag_harsh', 5000.00, 0.00, FALSE, 0.00),
    ('artist_prakshi', 10000.00, 50.00, TRUE, 5000.00);

-- Insert sample transactions
INSERT INTO transactions (customer_username, type, amount, balance_after, description) VALUES
    ('hashtag_harsh', 'DEPOSIT', 5000.00, 5000.00, 'Initial deposit'),
    ('artist_prakshi', 'DEPOSIT', 10000.00, 10000.00, 'Initial deposit'),
    ('artist_prakshi', 'PENALTY', 50.00, 10000.00, 'Late payment penalty');

-- Insert sample credit requests
INSERT INTO credit_requests (customer_username, amount, reason, approved, status) VALUES
    ('artist_prakshi', 5000.00, 'Business expansion loan', TRUE, 'APPROVED'),
    ('hashtag_harsh', 3000.00, 'Personal emergency loan', FALSE, 'PENDING');