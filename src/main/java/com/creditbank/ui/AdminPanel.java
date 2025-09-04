package com.creditbank.ui;

import com.creditbank.db.DatabaseManager;
import com.creditbank.model.Customer;
import com.creditbank.model.CreditRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

public class AdminPanel extends JPanel {
    private DatabaseManager dbManager;
    private JTable customerTable;
    private JTable creditRequestTable;
    private DefaultTableModel customerModel;
    private DefaultTableModel creditRequestModel;
    private JLabel statusLabel;
    private Timer refreshTimer;
    private JLabel totalCustomersLabel;
    private JLabel pendingRequestsLabel;
    private JLabel totalBalanceLabel;

    public AdminPanel(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 247, 250));
        
        initComponents();
        loadData();
        startAutoRefresh();
    }

    private void initComponents() {
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel with Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setBackground(new Color(245, 247, 250));

        // Top Panel - Customers
        JPanel customersPanel = createCustomersPanel();
        splitPane.setTopComponent(customersPanel);

        // Bottom Panel - Credit Requests
        JPanel requestsPanel = createCreditRequestsPanel();
        splitPane.setBottomComponent(requestsPanel);

        add(splitPane, BorderLayout.CENTER);

        // Status Bar
        statusLabel = new JLabel(" Ready", JLabel.LEFT);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Left side - Title and Welcome
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JLabel welcomeLabel = new JLabel("Welcome back, Administrator");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(108, 117, 125));
        
        leftPanel.add(titleLabel);
        leftPanel.add(welcomeLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Right side - Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);
        
        // Total Customers Card
        JPanel customersCard = createStatsCard("Total Customers", "0", new Color(40, 167, 69));
        totalCustomersLabel = (JLabel) ((JPanel) customersCard.getComponent(1)).getComponent(0);
        statsPanel.add(customersCard);
        
        // Pending Requests Card
        JPanel requestsCard = createStatsCard("Pending Requests", "0", new Color(255, 193, 7));
        pendingRequestsLabel = (JLabel) ((JPanel) requestsCard.getComponent(1)).getComponent(0);
        statsPanel.add(requestsCard);
        
        // Total Balance Card
        JPanel balanceCard = createStatsCard("Total Balance", "$0.00", new Color(23, 162, 184));
        totalBalanceLabel = (JLabel) ((JPanel) balanceCard.getComponent(1)).getComponent(0);
        statsPanel.add(balanceCard);
        
        headerPanel.add(statsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatsCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setPreferredSize(new Dimension(180, 80));

        // Icon panel
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(color);
        iconPanel.setPreferredSize(new Dimension(4, 0));
        card.add(iconPanel, BorderLayout.WEST);

        // Content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 3));
        contentPanel.setOpaque(false);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(new Color(33, 37, 41));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(108, 117, 125));
        
        contentPanel.add(valueLabel);
        contentPanel.add(titleLabel);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 10, 20),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        ));

        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel headerLabel = new JLabel("Customer Management");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(new Color(33, 37, 41));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton registerBtn = createStyledButton("Register New Customer", new Color(40, 167, 69));
        registerBtn.addActionListener(e -> registerCustomer());
        buttonPanel.add(registerBtn);

        JButton penaltyBtn = createStyledButton("Impose Penalty", new Color(220, 53, 69));
        penaltyBtn.addActionListener(e -> imposePenalty());
        buttonPanel.add(penaltyBtn);

        JButton refreshBtn = createStyledButton("Refresh", new Color(23, 162, 184));
        refreshBtn.addActionListener(e -> loadCustomers());
        buttonPanel.add(refreshBtn);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Customer Table
        String[] columns = {"Username", "Balance", "Penalty", "Credit Status", "Credit Limit"};
        customerModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(customerModel);
        customerTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        customerTable.setRowHeight(35);
        customerTable.setSelectionBackground(new Color(232, 240, 254));
        customerTable.setSelectionForeground(Color.BLACK);
        customerTable.setShowGrid(false);
        customerTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom header renderer
        customerTable.getTableHeader().setDefaultRenderer(new ModernHeaderRenderer());
        customerTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        customerTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCreditRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 20, 20),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        ));

        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel headerLabel = new JLabel("Credit Requests");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(new Color(33, 37, 41));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton approveBtn = createStyledButton("Approve Selected", new Color(40, 167, 69));
        approveBtn.addActionListener(e -> approveCreditRequest());
        buttonPanel.add(approveBtn);

        JButton rejectBtn = createStyledButton("Reject Selected", new Color(220, 53, 69));
        rejectBtn.addActionListener(e -> rejectCreditRequest());
        buttonPanel.add(rejectBtn);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Credit Request Table
        String[] columns = {"ID", "Customer", "Amount", "Reason", "Status", "Request Date"};
        creditRequestModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        creditRequestTable = new JTable(creditRequestModel);
        creditRequestTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        creditRequestTable.setRowHeight(35);
        creditRequestTable.setSelectionBackground(new Color(232, 240, 254));
        creditRequestTable.setSelectionForeground(Color.BLACK);
        creditRequestTable.setShowGrid(false);
        creditRequestTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom header renderer
        creditRequestTable.getTableHeader().setDefaultRenderer(new ModernHeaderRenderer());
        creditRequestTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        creditRequestTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(creditRequestTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void loadData() {
        loadCustomers();
        loadCreditRequests();
        updateStatistics();
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = dbManager.getAllCustomers();
            customerModel.setRowCount(0);
            
            for (Customer customer : customers) {
                customerModel.addRow(new Object[]{
                    customer.getUsername(),
                    String.format("$%.2f", customer.getBalance()),
                    String.format("$%.2f", customer.getPenalty()),
                    customer.isCreditApproved() ? "Approved" : "Not Approved",
                    String.format("$%.2f", customer.getCreditLimit())
                });
            }
            
            statusLabel.setText(" Customers loaded successfully");
        } catch (Exception e) {
            showError("Failed to load customers: " + e.getMessage());
        }
    }

    private void loadCreditRequests() {
        try {
            List<CreditRequest> requests = dbManager.getAllCreditRequests();
            creditRequestModel.setRowCount(0);
            
            for (CreditRequest request : requests) {
                creditRequestModel.addRow(new Object[]{
                    request.getId(),
                    request.getCustomerUsername(),
                    String.format("$%.2f", request.getAmount()),
                    request.getReason(),
                    request.isApproved() ? "Approved" : "Pending",
                    request.getRequestDate()
                });
            }
            
            statusLabel.setText(" Credit requests loaded successfully");
        } catch (Exception e) {
            showError("Failed to load credit requests: " + e.getMessage());
        }
    }

    private void updateStatistics() {
        try {
            List<Customer> customers = dbManager.getAllCustomers();
            totalCustomersLabel.setText(String.valueOf(customers.size()));
            
            double totalBalance = 0;
            for (Customer customer : customers) {
                totalBalance += customer.getBalance().doubleValue();
            }
            totalBalanceLabel.setText(String.format("$%.2f", totalBalance));
            
            List<CreditRequest> requests = dbManager.getAllCreditRequests();
            long pendingCount = 0;
            for (CreditRequest request : requests) {
                if (!request.isApproved()) {
                    pendingCount++;
                }
            }
            pendingRequestsLabel.setText(String.valueOf(pendingCount));
        } catch (Exception e) {
            // Silent fail for statistics
        }
    }

    private void registerCustomer() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Register New Customer",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showError("Username and password cannot be empty!");
                return;
            }

            try {
                boolean success = dbManager.registerCustomer(username, password);
                if (success) {
                    showSuccess("Customer registered successfully!");
                    loadData();
                } else {
                    showError("Failed to register customer. Username might already exist.");
                }
            } catch (Exception e) {
                showError("Failed to register customer: " + e.getMessage());
            }
        }
    }

    private void imposePenalty() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a customer first!");
            return;
        }

        String username = (String) customerModel.getValueAt(selectedRow, 0);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField amountField = new JTextField();
        JTextField reasonField = new JTextField();
        
        panel.add(new JLabel("Penalty Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Impose Penalty on " + username,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                String reason = reasonField.getText().trim();
                
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Penalty amount must be positive!");
                    return;
                }
                
                if (reason.isEmpty()) {
                    reason = "Administrative penalty";
                }

                boolean success = dbManager.imposePenalty(username, amount, reason);
                if (success) {
                    showSuccess("Penalty imposed successfully!");
                    loadData();
                } else {
                    showError("Failed to impose penalty!");
                }
            } catch (NumberFormatException e) {
                showError("Invalid amount format!");
            } catch (Exception e) {
                showError("Failed to impose penalty: " + e.getMessage());
            }
        }
    }

    private void approveCreditRequest() {
        int selectedRow = creditRequestTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a credit request first!");
            return;
        }

        int requestId = (int) creditRequestModel.getValueAt(selectedRow, 0);
        String status = (String) creditRequestModel.getValueAt(selectedRow, 4);
        
        if ("Approved".equals(status)) {
            showError("This request has already been approved!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to approve this credit request?",
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = dbManager.approveCreditRequest(requestId, "admin");
                if (success) {
                    showSuccess("Credit request approved successfully!");
                    loadData();
                } else {
                    showError("Failed to approve credit request!");
                }
            } catch (Exception e) {
                showError("Failed to approve request: " + e.getMessage());
            }
        }
    }

    private void rejectCreditRequest() {
        int selectedRow = creditRequestTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a credit request first!");
            return;
        }

        showInfo("Reject functionality not implemented in database manager");
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(30000, e -> {
            loadData();
            statusLabel.setText(" Data refreshed at " + new java.util.Date());
        });
        refreshTimer.start();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText(" Error: " + message);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(" " + message);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Custom table header renderer for modern look
    private class ModernHeaderRenderer extends JLabel implements TableCellRenderer {
        public ModernHeaderRenderer() {
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(new Color(73, 80, 87));
            setBackground(new Color(248, 249, 250));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    public void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}