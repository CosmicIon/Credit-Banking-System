package model;
import java.util.*;
import model.Customer;

public class Customer extends User {
    private double balance;
    private double penalty;
    private List<Transaction> transactions;
    private boolean creditApproved;

    public Customer(String username, String password) {
        super(username, password);
        this.balance = 0.0;
        this.penalty = 0.0;
        this.transactions = new ArrayList<>();
        this.creditApproved = false;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount, "User deposit"));
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction("Withdrawal", amount, "User withdrawal"));
        } else {
            transactions.add(new Transaction("Failed Withdrawal", amount, "Insufficient funds"));
        }
    }

    public void addPenalty(double amount) {
        penalty += amount;
        transactions.add(new Transaction("Penalty", amount, "Admin-imposed penalty"));
    }

    public double getPenalty() {
        return penalty;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setCreditApproved(boolean approved) {
        this.creditApproved = approved;
    }

    public boolean isCreditApproved() {
        return creditApproved;
    }

    @Override
    public String toString() {
        return "Customer: " + getUsername() + ", Balance: Rs." + balance + ", Penalty: Rs." + penalty + ", Credit Approved: " + creditApproved;
    }
} 