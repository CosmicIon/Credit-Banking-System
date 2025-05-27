package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private BankSystem bankSystem;
    private Admin admin;

    public LoginPanel(MainFrame mainFrame, BankSystem bankSystem, Admin admin) {
        this.mainFrame = mainFrame;
        this.bankSystem = bankSystem;
        this.admin = admin;

        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Select Login Type", SwingConstants.CENTER);
        JButton adminLoginBtn = new JButton("Login as Admin");
        JButton customerLoginBtn = new JButton("Login as Customer");
        JButton exitBtn = new JButton("Exit");

        add(welcomeLabel);
        add(adminLoginBtn);
        add(customerLoginBtn);
        add(exitBtn);

        adminLoginBtn.addActionListener(e -> showAdminLogin());
        customerLoginBtn.addActionListener(e -> showCustomerLogin());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void showAdminLogin() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        Object[] fields = {
                "Username:", userField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (admin.getUsername().equals(user) && admin.authenticate(pass)) {
                mainFrame.showAdminPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showCustomerLogin() {
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        Object[] fields = {
                "Username:", userField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Customer Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            Customer cust = bankSystem.getCustomerByUsername(user);
            if (cust != null && cust.authenticate(pass)) {
                mainFrame.showCustomerPanel(cust);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid customer credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
