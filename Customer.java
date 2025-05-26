import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private boolean hasRequestedCredit = false;
    private boolean isCreditApproved = false;
    private double balance;
    private double penaltyAmount = 0.0;
    private List<Transaction> transactions;


    public Customer(String name, String email, String password) {
        super(name, email, password);
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public CreditRequest requestCredit(double amount) {
        hasRequestedCredit = true;
        return new CreditRequest(this, amount);
    }

    public void setCreditApproved(boolean approved) {
        isCreditApproved = approved;
    }

    public boolean isCreditApproved() {
        return isCreditApproved;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        addTransaction(new Transaction("Deposit", amount, "Deposited to account"));
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            addTransaction(new Transaction("Withdraw", amount, "Withdrawn from account"));
            return true;
        }
        return false;
    }

    public void addPenalty(double amount, String reason) {
        penaltyAmount += amount;
        addTransaction(new Transaction("Penalty", amount, reason));
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactionHistory() {
        return transactions;
    }

    @Override
    public String toString() {
        return name + " (" + email + ") - Credit Approved: " + isCreditApproved;
    }
}