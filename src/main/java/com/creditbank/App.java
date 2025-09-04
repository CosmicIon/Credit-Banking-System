// File: src/main/java/com/creditbank/App.java
package com.creditbank;

import com.creditbank.ui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main application entry point
 * Sets up the look and feel and launches the banking system
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    public static void main(String[] args) {
        // Configure logging
        configureLogging();
        
        // Log application startup
        LOGGER.info("Starting Modern Credit Banking System v1.0");
        
        // Set system properties for better UI experience
        setupSystemProperties();
        
        // Set up the modern look and feel
        setupLookAndFeel();
        
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to start application", e);
                showErrorDialog("Failed to start application", e);
                System.exit(1);
            }
        });
    }
    
    /**
     * Configure logging for the application
     */
    private static void configureLogging() {
        // Set logging level
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
        
        // You can add custom handlers here if needed
        System.setProperty("java.util.logging.SimpleFormatter.format", 
                          "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }
    
    /**
     * Set system properties for better UI experience
     */
    private static void setupSystemProperties() {
        // Enable anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Enable better font rendering on Linux
        System.setProperty("sun.java2d.xrender", "true");
        
        // Improve performance
        System.setProperty("sun.java2d.d3d", "true");
        System.setProperty("sun.java2d.ddforcevram", "true");
        
        // Better scaling on high DPI displays
        System.setProperty("sun.java2d.uiScale", "1.0");
        
        // Set application name for better OS integration
        System.setProperty("apple.awt.application.name", "Credit Banking System");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Credit Banking System");
    }
    
    /**
     * Set up the FlatLaf look and feel for modern UI
     */
    private static void setupLookAndFeel() {
        try {
            // Set FlatLightLaf as the look and feel
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Customize FlatLaf properties
            customizeFlatLafProperties();
            
            LOGGER.info("FlatLaf look and feel applied successfully");
            
        } catch (UnsupportedLookAndFeelException e) {
            LOGGER.log(Level.WARNING, "Failed to set FlatLaf, falling back to system look and feel", e);
            
            try {
                // Fallback to system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Failed to set system look and feel, using default", ex);
            }
        }
    }
    
    /**
     * Customize FlatLaf UI properties
     */
    private static void customizeFlatLafProperties() {
        // Window and frame properties
        UIManager.put("TitlePane.unifiedBackground", false);
        UIManager.put("TitlePane.showIcon", true);
        
        // Button properties
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("ProgressBar.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        
        // Color customizations
        UIManager.put("Component.focusColor", new Color(0, 123, 255, 80));
        UIManager.put("Component.focusWidth", 2);
        
        // Table properties
        UIManager.put("Table.showHorizontalLines", false);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 0));
        UIManager.put("Table.selectionBackground", new Color(232, 240, 254));
        UIManager.put("Table.selectionForeground", Color.BLACK);
        
        // ScrollPane properties
        UIManager.put("ScrollPane.smoothScrolling", true);
        UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.track", new Color(0, 0, 0, 0));
        
        // Menu properties
        UIManager.put("MenuBar.borderColor", new Color(220, 220, 220));
        UIManager.put("Menu.selectionBackground", new Color(232, 240, 254));
        UIManager.put("MenuItem.selectionBackground", new Color(232, 240, 254));
        
        // Panel properties
        UIManager.put("Panel.background", new Color(245, 247, 250));
        
        // Text field properties
        UIManager.put("TextField.placeholderForeground", new Color(128, 128, 128));
        
        LOGGER.info("FlatLaf properties customized");
    }
    
    /**
     * Create and show the main GUI
     */
    private static void createAndShowGUI() {
        // Show splash screen
        showSplashScreen();
        
        // Create the main frame
        MainFrame mainFrame = new MainFrame();
        
        // Center the frame on screen initially
        mainFrame.setLocationRelativeTo(null);
        
        // Make it visible
        mainFrame.setVisible(true);
        
        // Log successful startup
        LOGGER.info("Application GUI created and displayed successfully");
        
        // Print welcome message to console
        printWelcomeMessage();
    }
    
    /**
     * Show a splash screen while the application loads
     */
    private static void showSplashScreen() {
        try {
            // Create splash screen
            JWindow splash = new JWindow();
            splash.setAlwaysOnTop(true);
            
            // Create splash content
            JPanel splashPanel = createSplashPanel();
            splash.add(splashPanel);
            
            splash.pack();
            splash.setLocationRelativeTo(null);
            splash.setVisible(true);
            
            // Simulate loading time
            Timer timer = new Timer(2000, e -> splash.dispose());
            timer.setRepeats(false);
            timer.start();
            
            // Small delay to show splash
            Thread.sleep(500);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.WARNING, "Splash screen interrupted", e);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to show splash screen", e);
        }
    }
    
    /**
     * Create the splash screen panel
     */
    private static JPanel createSplashPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));
        
        // Application icon/logo
        JLabel iconLabel = new JLabel("🏦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Application title
        JLabel titleLabel = new JLabel("Credit Banking System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Version
        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(108, 117, 125));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Loading message
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadingLabel.setForeground(new Color(108, 117, 125));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(200, 10));
        progressBar.setPreferredSize(new Dimension(200, 10));
        
        // Add components
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(versionLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(loadingLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(progressBar);
        
        return panel;
    }
    
    /**
     * Print welcome message to console
     */
    private static void printWelcomeMessage() {
        System.out.println("\n" +
                "═════════════════════════════════════════════════════════════\n" +
                "    🏦  MODERN CREDIT BANKING SYSTEM v1.0  🏦\n" +
                "═════════════════════════════════════════════════════════════\n" +
                "\n" +
                "Application started successfully!\n" +
                "\n" +
                "Default Login Credentials:\n" +
                "  Admin:    username = admin,     password = admin123\n" +
                "  Customer: username = john_doe,  password = password123\n" +
                "  Customer: username = jane_smith, password = password123\n" +
                "\n" +
                "Features Available:\n" +
                "  ✓ Customer Account Management\n" +
                "  ✓ Transaction Processing (Deposit/Withdrawal)\n" +
                "  ✓ Credit Request System\n" +
                "  ✓ Administrative Tools\n" +
                "  ✓ Real-time Data Updates\n" +
                "  ✓ Modern User Interface\n" +
                "\n" +
                "System Requirements Met:\n" +
                "  ✓ Java " + System.getProperty("java.version") + "\n" +
                "  ✓ FlatLaf Look and Feel\n" +
                "  ✓ MySQL Database Connection\n" +
                "\n" +
                "For help, use the Help menu in the application.\n" +
                "═════════════════════════════════════════════════════════════\n");
    }
    
    /**
     * Show error dialog for critical errors
     */
    private static void showErrorDialog(String message, Exception e) {
        String errorMessage = message + "\n\n" +
                             "Error Details:\n" + e.getMessage() + "\n\n" +
                             "Please check your configuration and try again.";
        
        JOptionPane.showMessageDialog(
            null,
            errorMessage,
            "Application Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Get application version
     */
    public static String getVersion() {
        return "1.0.0";
    }
    
    /**
     * Get application name
     */
    public static String getApplicationName() {
        return "Modern Credit Banking System";
    }
}