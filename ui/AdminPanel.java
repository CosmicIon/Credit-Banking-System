package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    private BankSystem bankSystem;
    private Admin admin;
    private MainFrame mainFrame;

    public AdminPanel(MainFrame mainFrame, BankSystem bankSystem, Admin admin) {
        this.bankSystem = bankSystem;
        this.admin = admin;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(7, 1, 10, 10));

        JButton registerCustomerBtn = new JButton("Register Customer");
        JButton viewRequestsBtn = new JButton("View Credit Requests");
        JButton approveRequestBtn = new JButton("Approve Credit Request");
        JButton viewCustomersBtn = new JButton("View All Customers");
        JButton imposePenaltyBtn = new JButton("Impose Penalty");
        JButton viewTransactionsBtn = new JButton("View Customer Transactions");
        JButton logoutBtn = new JButton("Logout");

        centerPanel.add(registerCustomerBtn);
        centerPanel.add(viewRequestsBtn);
        centerPanel.add(approveRequestBtn);
        centerPanel.add(viewCustomersBtn);
        centerPanel.add(imposePenaltyBtn);
        centerPanel.add(viewTransactionsBtn);
        centerPanel.add(logoutBtn);

        add(centerPanel, BorderLayout.CENTER);

        registerCustomerBtn.addActionListener(e -> registerCustomer());
        viewRequestsBtn.addActionListener(e -> viewCreditRequests());
        approveRequestBtn.addActionListener(e -> approveCreditRequest());
        viewCustomersBtn.addActionListener(e -> viewAllCustomers());
        imposePenaltyBtn.addActionListener(e -> imposePenalty());
        viewTransactionsBtn.addActionListener(e -> viewCustomerTransactions());
        logoutBtn.addActionListener(e -> mainFrame.showLoginPanel());
    }

    private void registerCustomer() {
        String username = JOptionPane.showInputDialog(this, "Enter Customer Username:");
        String password = JOptionPane.showInputDialog(this, "Enter Customer Password:");
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            Customer newCustomer = new Customer(username, password);
            bankSystem.addCustomer(newCustomer);
            JOptionPane.showMessageDialog(this, "Customer registered successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void viewCreditRequests() {
        StringBuilder sb = new StringBuilder();
        for (CreditRequest cr : bankSystem.getCreditRequests()) {
            sb.append(cr).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No credit requests.");
    }

    private void approveCreditRequest() {
        String username = JOptionPane.showInputDialog(this, "Enter username to approve request:");
        for (CreditRequest cr : bankSystem.getCreditRequests()) {
            if (cr.getCustomerUsername().equalsIgnoreCase(username)) {
                admin.approveCreditRequest(cr);
                Customer cust = bankSystem.getCustomerByUsername(username);
                if (cust != null) cust.setCreditApproved(true);
                JOptionPane.showMessageDialog(this, "Request approved.");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Request not found.");
    }

    private void viewAllCustomers() {
        StringBuilder sb = new StringBuilder();
        for (Customer c : bankSystem.getCustomers()) {
            sb.append(c.getUsername()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No customers found.");
    }

    private void imposePenalty() {
        String username = JOptionPane.showInputDialog(this, "Enter Customer Username:");
        Customer c = bankSystem.getCustomerByUsername(username);
        if (c != null) {
            String amtStr = JOptionPane.showInputDialog(this, "Enter Penalty Amount:");
            try {
                double amt = Double.parseDouble(amtStr);
                admin.imposePenalty(c, amt);
                JOptionPane.showMessageDialog(this, "Penalty imposed.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found.");
        }
    }

    private void viewCustomerTransactions() {
        String username = JOptionPane.showInputDialog(this, "Enter Customer Username:");
        Customer c = bankSystem.getCustomerByUsername(username);
        if (c != null) {
            StringBuilder sb = new StringBuilder();
            for (Transaction t : c.getTransactions()) {
                sb.append(t).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No transactions found.");
        } else {
            JOptionPane.showMessageDialog(this, "Customer not found.");
        }
    }
}
