package com.creditbank.model;

/**
 * Admin user class extending User
 */
public class Admin extends User {
    
    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }
    
    /**
     * Checks if admin has permission to approve credit
     */
    public boolean canApproveCredit() {
        return true;
    }
    
    /**
     * Checks if admin can impose penalties
     */
    public boolean canImposePenalty() {
        return true;
    }
}
