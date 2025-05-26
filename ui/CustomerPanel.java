// CustomerPanel.java
package ui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private Customer customer;
    private BankSystem bankSystem;
    private MainFrame mainFrame;

    public CustomerPanel(MainFrame mainFrame, BankSystem bankSystem, Customer customer) {
        this.customer = customer;
        this.bankSystem = bankSystem;
        this.mainFrame = mainFrame;

        setLayout(new GridLayout(6, 1, 10, 10));

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton creditRequestBtn = new JButton("Request Credit");
        JButton viewCreditStatusBtn = new JButton("View Credit Status");
        JButton viewTransactionsBtn = new JButton("View Transactions");
        JButton logoutBtn = new JButton("Logout");

        add(depositBtn);
        add(withdrawBtn);
        add(creditRequestBtn);
        add(viewCreditStatusBtn);
        add(viewTransactionsBtn);
        add(logoutBtn);

        depositBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter deposit amount:");
            try {
                double amount = Double.parseDouble(input);
                customer.deposit(amount);
                JOptionPane.showMessageDialog(this, "Deposited successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        });

        withdrawBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter withdrawal amount:");
            try {
                double amount = Double.parseDouble(input);
                customer.withdraw(amount);
                JOptionPane.showMessageDialog(this, "Withdrawal processed.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        });

        creditRequestBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter credit amount:");
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) throw new NumberFormatException();
                String reason = JOptionPane.showInputDialog("Enter reason:");
                CreditRequest request = new CreditRequest(customer.getUsername(), amount, reason);
                bankSystem.addCreditRequest(request);
                customer.getTransactions().add(new Transaction("Credit Request", amount, reason));
                JOptionPane.showMessageDialog(this, "Credit request submitted.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid credit amount.");
            }
        });

        viewCreditStatusBtn.addActionListener(e -> {
            boolean approved = customer.isCreditApproved();
            JOptionPane.showMessageDialog(this, "Credit Approved: " + approved);
        });

        viewTransactionsBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Transaction t : customer.getTransactions()) {
                sb.append(t).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No transactions found.");
        });

        logoutBtn.addActionListener(e -> mainFrame.showLoginPanel());
    }
}
