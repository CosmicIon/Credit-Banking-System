package com.creditbank.model;

import java.math.BigDecimal;

/**
 * Customer user class with financial information
 */
public class Customer extends User {
    private BigDecimal balance;
    private BigDecimal penalty;
    private boolean creditApproved;
    private BigDecimal creditLimit;
    private String accountStatus;
    
    public Customer(String username, String password) {
        super(username, password, "CUSTOMER");
        this.balance = BigDecimal.ZERO;
        this.penalty = BigDecimal.ZERO;
        this.creditApproved = false;
        this.creditLimit = BigDecimal.ZERO;
        this.accountStatus = "ACTIVE";
    }
    
    public Customer(String username, String password, BigDecimal balance, 
                   BigDecimal penalty, boolean creditApproved, BigDecimal creditLimit) {
        super(username, password, "CUSTOMER");
        this.balance = balance;
        this.penalty = penalty;
        this.creditApproved = creditApproved;
        this.creditLimit = creditLimit;
        this.accountStatus = "ACTIVE";
    }
    
    // Business logic methods
    public boolean canWithdraw(BigDecimal amount) {
        return balance.compareTo(amount) >= 0 && "ACTIVE".equals(accountStatus);
    }
    
    public BigDecimal getAvailableCredit() {
        return creditApproved ? creditLimit : BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalAvailable() {
        return balance.add(getAvailableCredit());
    }
    
    // Getters and Setters
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public BigDecimal getPenalty() {
        return penalty;
    }
    
    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
    
    public boolean isCreditApproved() {
        return creditApproved;
    }
    
    public void setCreditApproved(boolean creditApproved) {
        this.creditApproved = creditApproved;
    }
    
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
    
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
    
    public String getAccountStatus() {
        return accountStatus;
    }
    
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
