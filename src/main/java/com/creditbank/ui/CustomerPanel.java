package com.creditbank.ui;

import com.creditbank.db.DatabaseManager;
import com.creditbank.model.Customer;
import com.creditbank.model.Transaction;

import javax.swing.*;
// ...existing imports...
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

public class CustomerPanel extends JPanel {
    private DatabaseManager dbManager;
    private String username;
    private Customer customer;
    
    private JLabel balanceLabel;
    private JLabel creditStatusLabel;
    private JLabel penaltyLabel;
    private JLabel welcomeLabel;
    private JTable transactionTable;
    private DefaultTableModel transactionModel;
    private JProgressBar balanceProgress;
    private JLabel statusLabel;
    private Timer refreshTimer;

    public CustomerPanel(DatabaseManager dbManager, String username) {
        this.dbManager = dbManager;
        this.username = username;
        
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 247, 250));
        
        initComponents();
        loadCustomerData();
        startAutoRefresh();
    }

    private void initComponents() {
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left Panel - Account Info and Actions
        JPanel leftPanel = createLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Center Panel - Transaction History
        JPanel centerPanel = createTransactionPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

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
        
        JLabel titleLabel = new JLabel("Customer Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        welcomeLabel = new JLabel("Welcome back, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(108, 117, 125));
        
        leftPanel.add(titleLabel);
        leftPanel.add(welcomeLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Right side - Quick Stats
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statsPanel.setOpaque(false);
        
        // Current Date/Time
        JLabel dateLabel = new JLabel(new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(108, 117, 125));
        
        statsPanel.add(dateLabel);
        headerPanel.add(statsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(350, 0));

        // Account Summary Card
        JPanel summaryCard = createAccountSummaryCard();
        leftPanel.add(summaryCard);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Quick Actions Card
        JPanel actionsCard = createQuickActionsCard();
        leftPanel.add(actionsCard);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Credit Request Card
        JPanel creditCard = createCreditRequestCard();
        leftPanel.add(creditCard);

        return leftPanel;
    }

    private JPanel createAccountSummaryCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // Card Title
        JLabel titleLabel = new JLabel("Account Summary");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Balance Section
        JPanel balancePanel = new JPanel(new BorderLayout());
        balancePanel.setOpaque(false);
        balancePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        balancePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel balanceInfo = new JPanel();
        balanceInfo.setLayout(new BoxLayout(balanceInfo, BoxLayout.Y_AXIS));
        balanceInfo.setOpaque(false);
        balanceInfo.setBackground(new Color(232, 240, 254));
        balanceInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel balanceTitle = new JLabel("Current Balance");
        balanceTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        balanceTitle.setForeground(new Color(108, 117, 125));
        
        balanceLabel = new JLabel("$0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        balanceLabel.setForeground(new Color(40, 167, 69));
        
        balanceProgress = new JProgressBar(0, 10000);
        balanceProgress.setValue(0);
        balanceProgress.setStringPainted(false);
        balanceProgress.setPreferredSize(new Dimension(0, 8));
        balanceProgress.setBackground(new Color(240, 240, 240));
        balanceProgress.setForeground(new Color(40, 167, 69));
        balanceProgress.setBorderPainted(false);
        
        balanceInfo.add(balanceTitle);
        balanceInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        balanceInfo.add(balanceLabel);
        balanceInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        balanceInfo.add(balanceProgress);
        
        balancePanel.add(balanceInfo);
        card.add(balancePanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Credit Status and Penalty
        JPanel statusPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        statusPanel.setOpaque(false);
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Credit Status
        JPanel creditPanel = new JPanel(new BorderLayout());
        creditPanel.setOpaque(false);
        creditPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel creditTitle = new JLabel("Credit Status");
        creditTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        creditTitle.setForeground(new Color(108, 117, 125));
        
        creditStatusLabel = new JLabel("Not Approved");
        creditStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        creditStatusLabel.setForeground(new Color(220, 53, 69));
        
        creditPanel.add(creditTitle, BorderLayout.NORTH);
        creditPanel.add(creditStatusLabel, BorderLayout.CENTER);
        
        // Penalty
        JPanel penaltyPanel = new JPanel(new BorderLayout());
        penaltyPanel.setOpaque(false);
        penaltyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel penaltyTitle = new JLabel("Current Penalty");
        penaltyTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        penaltyTitle.setForeground(new Color(108, 117, 125));
        
        penaltyLabel = new JLabel("$0.00");
        penaltyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        penaltyLabel.setForeground(new Color(255, 193, 7));
        
        penaltyPanel.add(penaltyTitle, BorderLayout.NORTH);
        penaltyPanel.add(penaltyLabel, BorderLayout.CENTER);
        
        statusPanel.add(creditPanel);
        statusPanel.add(penaltyPanel);
        card.add(statusPanel);

        return card;
    }

    private JPanel createQuickActionsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Card Title
        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Action Buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JButton depositBtn = createActionButton("💰 Deposit", new Color(40, 167, 69));
        depositBtn.addActionListener(e -> performDeposit());
        
        JButton withdrawBtn = createActionButton("💸 Withdraw", new Color(220, 53, 69));
        withdrawBtn.addActionListener(e -> performWithdrawal());
        
        JButton transferBtn = createActionButton("↔️ Transfer", new Color(23, 162, 184));
        transferBtn.addActionListener(e -> showInfo("Transfer feature coming soon!"));
        
        JButton historyBtn = createActionButton("📊 Statement", new Color(108, 117, 125));
        historyBtn.addActionListener(e -> generateStatement());
        
        buttonsPanel.add(depositBtn);
        buttonsPanel.add(withdrawBtn);
        buttonsPanel.add(transferBtn);
        buttonsPanel.add(historyBtn);
        
        card.add(buttonsPanel);

        return card;
    }

    private JPanel createCreditRequestCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Card Title
        JLabel titleLabel = new JLabel("Credit Services");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Request Credit Button
        JButton requestBtn = new JButton("Request Credit Line");
        requestBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        requestBtn.setForeground(Color.WHITE);
        requestBtn.setBackground(new Color(102, 16, 242));
        requestBtn.setBorderPainted(false);
        requestBtn.setFocusPainted(false);
        requestBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        requestBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        requestBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        requestBtn.addActionListener(e -> requestCredit());
        
        // Hover effect
        requestBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                requestBtn.setBackground(new Color(102, 16, 242).darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                requestBtn.setBackground(new Color(102, 16, 242));
            }
        });
        
        card.add(requestBtn);

        return card;
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel headerLabel = new JLabel("Transaction History");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(new Color(33, 37, 41));
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Filter/Search Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);
        
        JTextField searchField = new JTextField(15);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(new Color(23, 162, 184));
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadTransactions());
        
        filterPanel.add(searchField);
        filterPanel.add(refreshBtn);
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // Transaction Table
        String[] columns = {"Date", "Type", "Description", "Amount", "Balance"};
        transactionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return String.class;
                return String.class;
            }
        };
        
        transactionTable = new JTable(transactionModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transactionTable.setRowHeight(35);
        transactionTable.setSelectionBackground(new Color(232, 240, 254));
        transactionTable.setSelectionForeground(Color.BLACK);
        transactionTable.setShowGrid(false);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom renderer for amount column
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(new AmountCellRenderer());
        
        // Custom header renderer
        transactionTable.getTableHeader().setDefaultRenderer(new ModernHeaderRenderer());
        transactionTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        transactionTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setOpaque(false);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel summaryLabel = new JLabel("Total Transactions: 0");
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        summaryLabel.setForeground(new Color(108, 117, 125));
        summaryPanel.add(summaryLabel);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadCustomerData() {
        try {
            customer = dbManager.getCustomerDetails(username);
            if (customer != null) {
                refreshCustomerUI();
                loadTransactions();
            }
        } catch (Exception e) {
            showError("Failed to load customer data: " + e.getMessage());
        }
    }

    // Renamed from updateUI() to avoid reducing visibility of JPanel.updateUI()
    private void refreshCustomerUI() {
        if (customer != null) {
            balanceLabel.setText(String.format("$%.2f", customer.getBalance()));
            penaltyLabel.setText(String.format("$%.2f", customer.getPenalty()));
            creditStatusLabel.setText(customer.isCreditApproved() ? "Approved" : "Not Approved");
            creditStatusLabel.setForeground(customer.isCreditApproved() ? 
                new Color(40, 167, 69) : new Color(220, 53, 69));
            
            // Update progress bar (assuming max balance for visualization)
            double balance = customer.getBalance().doubleValue();
            int progress = (int) Math.min((balance / 10000) * 100, 100);
            balanceProgress.setValue(progress * 100);
        }
    }

    private void loadTransactions() {
        try {
            List<Transaction> transactions = dbManager.getCustomerTransactions(username);
            transactionModel.setRowCount(0);
            
            for (Transaction t : transactions) {
                transactionModel.addRow(new Object[]{
                    t.getTransactionDate(),
                    t.getType(),
                    t.getDescription(),
                    String.format("$%.2f", t.getAmount()),
                    String.format("$%.2f", t.getBalanceAfter())
                });
            }
            
            // Update summary
            JPanel summaryPanel = (JPanel) ((JPanel) ((JScrollPane) transactionTable.getParent().getParent()).getParent()).getComponent(2);
            JLabel summaryLabel = (JLabel) summaryPanel.getComponent(0);
            summaryLabel.setText("Total Transactions: " + transactions.size());
            
            statusLabel.setText(" Transactions loaded successfully");
        } catch (Exception e) {
            showError("Failed to load transactions: " + e.getMessage());
        }
    }

    private void performDeposit() {
        String amountStr = JOptionPane.showInputDialog(
            this,
            "Enter deposit amount:",
            "Deposit Funds",
            JOptionPane.QUESTION_MESSAGE
        );

        if (amountStr != null) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Amount must be positive!");
                    return;
                }

                boolean success = dbManager.performDeposit(username, amount);
                if (success) {
                    showSuccess("Deposit successful!");
                    loadCustomerData();
                } else {
                    showError("Failed to perform deposit!");
                }
            } catch (NumberFormatException e) {
                showError("Invalid amount format!");
            } catch (Exception e) {
                showError("Failed to perform deposit: " + e.getMessage());
            }
        }
    }

    private void performWithdrawal() {
        String amountStr = JOptionPane.showInputDialog(
            this,
            "Enter withdrawal amount:",
            "Withdraw Funds",
            JOptionPane.QUESTION_MESSAGE
        );

        if (amountStr != null) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Amount must be positive!");
                    return;
                }

                if (amount.compareTo(customer.getBalance()) > 0) {
                    showError("Insufficient balance!");
                    return;
                }

                boolean success = dbManager.performWithdrawal(username, amount);
                if (success) {
                    showSuccess("Withdrawal successful!");
                    loadCustomerData();
                } else {
                    showError("Failed to perform withdrawal!");
                }
            } catch (NumberFormatException e) {
                showError("Invalid amount format!");
            } catch (Exception e) {
                showError("Failed to perform withdrawal: " + e.getMessage());
            }
        }
    }

    private void requestCredit() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField amountField = new JTextField();
        JTextField reasonField = new JTextField();
        
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "Request Credit",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText());
                String reason = reasonField.getText().trim();

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Amount must be positive!");
                    return;
                }

                if (reason.isEmpty()) {
                    showError("Please provide a reason!");
                    return;
                }

                boolean success = dbManager.submitCreditRequest(username, amount, reason);
                if (success) {
                    showSuccess("Credit request submitted successfully!");
                } else {
                    showError("Failed to submit credit request!");
                }
            } catch (NumberFormatException e) {
                showError("Invalid amount format!");
            } catch (Exception e) {
                showError("Failed to submit credit request: " + e.getMessage());
            }
        }
    }

    private void generateStatement() {
        showInfo("Statement generation feature coming soon!");
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(60000, e -> {
            loadCustomerData();
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

    // Custom table header renderer
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

    // Custom renderer for amount column
    private class AmountCellRenderer extends JLabel implements TableCellRenderer {
        public AmountCellRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value instanceof String) {
                setText((String) value);
                
                String type = (String) table.getValueAt(row, 1);
                if ("DEPOSIT".equals(type)) {
                    setForeground(new Color(40, 167, 69));
                } else if ("WITHDRAWAL".equals(type)) {
                    setForeground(new Color(220, 53, 69));
                } else if ("PENALTY".equals(type)) {
                    setForeground(new Color(255, 193, 7));
                } else {
                    setForeground(new Color(108, 117, 125));
                }
            }
            
            if (isSelected) {
                setBackground(new Color(232, 240, 254));
            } else {
                setBackground(Color.WHITE);
            }
            
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            return this;
        }
    }

    public void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}