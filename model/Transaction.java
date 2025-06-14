package model;

import java.time.LocalDateTime;
import model.Transaction;

public class Transaction {
    private String type;
    private double amount;
    private String date;
    private String description;

    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now().toString();
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + type + ": Rs." + amount + " (" + description + ")";
    }
}