package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private BankSystem bankSystem;
    private Admin admin;

    public MainFrame(BankSystem bankSystem, Admin admin) {
        this.bankSystem = bankSystem;
        this.admin = admin;

        setTitle("Banking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showLoginPanel();
        setVisible(true);
    }

    public void showLoginPanel() {
        getContentPane().removeAll();
        setContentPane(new LoginPanel(this, bankSystem, admin));
        revalidate();
        repaint();
    }

    public void showAdminPanel() {
        getContentPane().removeAll();
        setContentPane(new AdminPanel(this, bankSystem, admin));
        revalidate();
        repaint();
    }

    public void showCustomerPanel(Customer customer) {
        getContentPane().removeAll();
        setContentPane(new CustomerPanel(this, bankSystem, customer));
        revalidate();
        repaint();
    }
}
