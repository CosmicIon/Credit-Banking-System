// File: src/main/java/com/creditbank/ui/MainFrame.java
package com.creditbank.ui;

import com.creditbank.db.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application window that manages different panels
 */
public class MainFrame extends JFrame {
    private DatabaseManager dbManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Panel components
    private LoginPanel loginPanel;
    private AdminPanel adminPanel;
    private CustomerPanel customerPanel;
    
    // Current user info
    private String currentUser;
    private String currentUserType;
    
    public MainFrame() {
        initializeDatabase();
        initializeUI();
        setupWindowProperties();
    }
    
    private void initializeDatabase() {
        try {
            dbManager = new com.creditbank.db.DatabaseManager();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Failed to connect to database. Please ensure MySQL is running and configured properly.\n" +
                "Error: " + e.getMessage(),
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }
    
    private void initializeUI() {
        // Set up the card layout for switching between panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create and add panels
        createPanels();
        
        // Set initial panel
        showLoginPanel();
        
        // Add main panel to frame
        add(mainPanel);
        
        // Create menu bar
        createMenuBar();
    }
    
    private void createPanels() {
        // Create login panel
        loginPanel = new LoginPanel(dbManager, this);
        mainPanel.add(loginPanel, "LOGIN");
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(248, 249, 250));
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutItem.addActionListener(e -> logout());
        logoutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        exitItem.addActionListener(e -> exitApplication());
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aboutItem.addActionListener(e -> showAbout());
        
        JMenuItem helpItem = new JMenuItem("User Guide");
        helpItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        helpItem.addActionListener(e -> showUserGuide());
        
        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void setupWindowProperties() {
        setTitle("Modern Credit Banking System v1.0");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
        
        // Center on screen if not maximized
        setLocationRelativeTo(null);
        
        // Set application icon
        try {
            // You can add a custom icon here
            setIconImage(createDefaultIcon());
        } catch (Exception e) {
            System.out.println("Could not load application icon");
        }
        
        // Add window listener for cleanup on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    private Image createDefaultIcon() {
        // Create a simple default icon
        BufferedImage icon = new java.awt.image.BufferedImage(32, 32, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = icon.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(new Color(0, 123, 255));
        g2.fillRoundRect(0, 0, 32, 32, 8, 8);
        
        // Bank symbol
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        String text = "$";
        int x = (32 - fm.stringWidth(text)) / 2;
        int y = (32 - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, x, y);
        
        g2.dispose();
        return icon;
    }
    
    /**
     * Shows the login panel
     */
    public void showLoginPanel() {
        currentUser = null;
        currentUserType = null;
        
        // Clean up existing panels
        cleanup();
        
        cardLayout.show(mainPanel, "LOGIN");
        updateTitle("Login");
    }
    
    /**
     * Shows the admin panel for the given admin user
     */
    public void showAdminPanel(String adminUsername) {
        currentUser = adminUsername;
        currentUserType = "ADMIN";
        
        // Create admin panel if it doesn't exist
        if (adminPanel == null) {
            adminPanel = new AdminPanel(dbManager);
            mainPanel.add(adminPanel, "ADMIN");
        }
        
        cardLayout.show(mainPanel, "ADMIN");
        updateTitle("Admin Dashboard - " + adminUsername);
        
        // Update menu bar state
        updateMenuState(true);
    }
    
    /**
     * Shows the customer panel for the given customer user
     */
    public void showCustomerPanel(String customerUsername) {
        currentUser = customerUsername;
        currentUserType = "CUSTOMER";
        
        // Create customer panel (always create new to ensure fresh data)
        if (customerPanel != null) {
            customerPanel.cleanup();
            mainPanel.remove(customerPanel);
        }
        
        customerPanel = new CustomerPanel(dbManager, customerUsername);
        mainPanel.add(customerPanel, "CUSTOMER");
        
        cardLayout.show(mainPanel, "CUSTOMER");
        updateTitle("Customer Portal - " + customerUsername);
        
        // Update menu bar state
        updateMenuState(true);
    }
    
    private void updateTitle(String subtitle) {
        setTitle("Modern Credit Banking System v1.0 - " + subtitle);
    }
    
    private void updateMenuState(boolean loggedIn) {
        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            JMenu fileMenu = menuBar.getMenu(0);
            fileMenu.getItem(0).setEnabled(loggedIn); // Logout item
        }
    }
    
    private void logout() {
        if (currentUser != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                cleanup();
                showLoginPanel();
                updateMenuState(false);
                
                // Show logout message
                Timer timer = new Timer(2000, e -> {
                    // Clear any status messages after 2 seconds
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    private void cleanup() {
        // Cleanup admin panel
        if (adminPanel != null) {
            adminPanel.cleanup();
            mainPanel.remove(adminPanel);
            adminPanel = null;
        }
        
        // Cleanup customer panel
        if (customerPanel != null) {
            customerPanel.cleanup();
            mainPanel.remove(customerPanel);
            customerPanel = null;
        }
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Cleanup resources
            cleanup();
            
            if (dbManager != null) {
                dbManager.close();
            }
            
            System.exit(0);
        }
    }
    
    private void showAbout() {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Modern Credit Banking System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><center>A comprehensive banking solution<br>" +
                                     "Built with Java Swing and MySQL<br><br>" +
                                     "Features:<br>" +
                                     "• Customer Account Management<br>" +
                                     "• Transaction Processing<br>" +
                                     "• Credit Request System<br>" +
                                     "• Administrative Tools<br>" +
                                     "• Modern User Interface</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        aboutPanel.add(titleLabel);
        aboutPanel.add(Box.createVerticalStrut(10));
        aboutPanel.add(versionLabel);
        aboutPanel.add(Box.createVerticalStrut(20));
        aboutPanel.add(descLabel);
        
        JOptionPane.showMessageDialog(
            this,
            aboutPanel,
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showUserGuide() {
        JPanel guidePanel = new JPanel(new BorderLayout());
        guidePanel.setPreferredSize(new Dimension(500, 400));
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setText(
            "CREDIT BANKING SYSTEM - USER GUIDE\n\n" +
            "GETTING STARTED:\n" +
            "• Use the default admin login: admin / admin123\n" +
            "• Sample customer accounts are pre-loaded\n\n" +
            "ADMIN FEATURES:\n" +
            "• View and manage all customer accounts\n" +
            "• Register new customers\n" +
            "• Approve/reject credit requests\n" +
            "• Impose penalties on accounts\n" +
            "• Monitor system statistics\n\n" +
            "CUSTOMER FEATURES:\n" +
            "• View account balance and transaction history\n" +
            "• Perform deposits and withdrawals\n" +
            "• Request credit lines\n" +
            "• Monitor credit status and penalties\n\n" +
            "NAVIGATION:\n" +
            "• Use Ctrl+L to logout\n" +
            "• Use Ctrl+Q to exit application\n" +
            "• Data refreshes automatically\n\n" +
            "SECURITY:\n" +
            "• All transactions are logged\n" +
            "• User authentication required\n" +
            "• Database transactions are atomic\n\n" +
            "For technical support or issues, please check\n" +
            "your database connection and configuration."
        );
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        guidePanel.add(scrollPane, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(
            this,
            guidePanel,
            "User Guide",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Get current logged-in user
     */
    public String getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get current user type
     */
    public String getCurrentUserType() {
        return currentUserType;
    }
    
    /**
     * Get database manager instance
     */
    public DatabaseManager getDatabaseManager() {
        return dbManager;
    }
}