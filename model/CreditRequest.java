package model;

import model.CreditRequest;
public class CreditRequest {
    private String customerUsername;
    private double amount;
    private boolean approved;
    private String reason;

    public CreditRequest(String customerUsername, double amount, String reason) {
        this.customerUsername = customerUsername;
        this.amount = amount;
        this.reason = reason;
        this.approved = false;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isApproved() {
        return approved;
    }

    public void approve() {
        approved = true;
    }

    public String getReason() {
        return reason;
    }

    public String toString() {
        return "CreditRequest from " + customerUsername + ", Amount: " + amount + ", Reason: " + reason + ", Approved: " + approved;
    }
}