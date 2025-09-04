package com.creditbank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a credit request from a customer
 */
public class CreditRequest {
    private int id;
    private String customerUsername;
    private BigDecimal amount;
    private String reason;
    private boolean approved;
    private String approvedBy;
    private Timestamp approvedDate;
    private Timestamp requestDate;
    private String status;
    
    public CreditRequest() {}
    
    public CreditRequest(String customerUsername, BigDecimal amount, String reason) {
        this.customerUsername = customerUsername;
        this.amount = amount;
        this.reason = reason;
        this.approved = false;
        this.status = "PENDING";
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
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public boolean isApproved() {
        return approved;
    }
    
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
    public String getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public Timestamp getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(Timestamp approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public Timestamp getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}