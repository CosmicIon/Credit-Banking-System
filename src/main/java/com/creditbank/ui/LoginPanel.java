package com.creditbank.ui;

import com.creditbank.db.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Login panel with modern UI design
 */
public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private DatabaseManager dbManager;
    private MainFrame mainFrame;
    
    public LoginPanel(DatabaseManager dbManager, MainFrame mainFrame) {
        this.dbManager = dbManager;
        this.mainFrame = mainFrame;
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Create main login container
        JPanel loginContainer = new JPanel();
        loginContainer.setLayout(new BoxLayout(loginContainer, BoxLayout.Y_AXIS));
        loginContainer.setBackground(Color.WHITE);
        loginContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        
        // Title
        JLabel titleLabel = new JLabel("Credit Banking System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Secure Login");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Icon
        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username field
        JPanel usernamePanel = createFieldPanel("Username", true);
        usernameField = (JTextField) usernamePanel.getComponent(2);
        
        // Password field
        JPanel passwordPanel = createFieldPanel("Password", false);
        passwordField = (JPasswordField) passwordPanel.getComponent(2);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        loginButton = createStyledButton("Login", new Color(0, 123, 255), Color.WHITE);
        exitButton = createStyledButton("Exit", new Color(220, 53, 69), Color.WHITE);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(248, 249, 250));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel infoLabel = new JLabel("<html><center>Default Admin: admin / admin123<br>" +
                                      "Sample Customer: john_doe / password123</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(108, 117, 125));
        infoPanel.add(infoLabel);
        
        // Add components to container
        loginContainer.add(iconLabel);
        loginContainer.add(Box.createVerticalStrut(10));
        loginContainer.add(titleLabel);
        loginContainer.add(Box.createVerticalStrut(5));
        loginContainer.add(subtitleLabel);
        loginContainer.add(Box.createVerticalStrut(30));
        loginContainer.add(usernamePanel);
        loginContainer.add(Box.createVerticalStrut(15));
        loginContainer.add(passwordPanel);
        loginContainer.add(Box.createVerticalStrut(25));
        loginContainer.add(buttonPanel);
        loginContainer.add(Box.createVerticalStrut(20));
        loginContainer.add(infoPanel);
        
        add(loginContainer);
        
        // Add action listeners
        setupActionListeners();
        
        // Add key listeners for Enter key
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }
    
    private JPanel createFieldPanel(String label, boolean isTextField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fieldLabel.setForeground(new Color(73, 80, 87));
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField field;
        if (isTextField) {
            field = new JTextField(20);
        } else {
            field = new JPasswordField(20);
        }
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(300, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(fieldLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupActionListeners() {
        loginButton.addActionListener(e -> handleLogin());
        exitButton.addActionListener(e -> System.exit(0));
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        String userType = dbManager.authenticateUser(username, password);
        
        if (userType != null) {
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
            
            // Navigate to appropriate panel
            if ("ADMIN".equals(userType)) {
                mainFrame.showAdminPanel(username);
            } else if ("CUSTOMER".equals(userType)) {
                mainFrame.showCustomerPanel(username);
            }
        } else {
            showError("Invalid username or password");
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Login Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}