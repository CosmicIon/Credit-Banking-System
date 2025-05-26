// Customer.java
import java.util.*;

public class Customer extends User {
    private double balance;
    private double penalty;
    private boolean creditApproved;
    private List<String> transactions;

    public Customer(String username, String password) {
        super(username, password);
        this.balance = 0.0;
        this.penalty = 0.0;
        this.creditApproved = false;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add("Withdrawn: " + amount);
        } else {
            transactions.add("Failed Withdrawal Attempt: " + amount);
        }
    }

    public void addPenalty(double amount) {
        penalty += amount;
        transactions.add("Penalty added: " + amount);
    }

    public double getPenalty() {
        return penalty;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public boolean isCreditApproved() {
        return creditApproved;
    }

    public void setCreditApproved(boolean approved) {
        this.creditApproved = approved;
    }

    @Override
    public String toString() {
        return "Customer: " + getUsername() + ", Balance: " + balance + ", Penalty: " + penalty;
    }
}