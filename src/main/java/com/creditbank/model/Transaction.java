package com.creditbank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a financial transaction in the system
 */
public class Transaction {
    private int id;
    private String customerUsername;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private Timestamp transactionDate;
    
    public Transaction() {}
    
    public Transaction(String customerUsername, String type, BigDecimal amount,
                      BigDecimal balanceAfter, String description) {
        this.customerUsername = customerUsername;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCustomerUsername() {
        return customerUsername;
    }
    
    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Timestamp getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}
