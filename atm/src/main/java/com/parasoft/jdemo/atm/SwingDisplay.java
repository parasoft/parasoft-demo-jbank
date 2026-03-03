package com.parasoft.jdemo.atm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Swing-based ATM display interface that provides a graphical user interface
 * resembling a physical ATM machine.
 */
public class SwingDisplay extends JFrame {
    
    private JTextArea displayArea;
    private JTextField inputField;
    private JPanel buttonPanel;
    private JPanel keypadPanel;
    private Bank bank;
    private ATM atm;
    private Account currentAccount;
    private enum State { MAIN_MENU, LOGIN, CREATE_ACCOUNT, TRANSACTION_MENU }
    private State currentState = State.MAIN_MENU;
    
    public SwingDisplay() {
        bank = new Bank();
        atm = new ATM(bank, new ConsoleDisplay());
        
        setTitle("Parasoft Demo - ATM Banking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(590, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
        showMainMenu();
    }
    
    private void initComponents() {
        // Main container with ATM-like appearance
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(45, 45, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Display area (screen)
        displayArea = new JTextArea(11, 50);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(false);
        displayArea.setBackground(new Color(173, 216, 230));
        displayArea.setForeground(new Color(33, 33, 33));
        displayArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(new Color(45, 45, 45));
        
        JLabel inputLabel = new JLabel("Input: ");
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputField.addActionListener(e -> handleEnter());
        
        inputPanel.add(inputLabel, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        
        // Keypad panel (left side - smaller)
        keypadPanel = new JPanel(new GridLayout(4, 3, 3, 3));
        keypadPanel.setBackground(new Color(45, 45, 45));
        keypadPanel.setPreferredSize(new Dimension(360, 185));
        
        // Create number buttons
        for (int i = 1; i <= 9; i++) {
            JButton btn = createKeypadButton(String.valueOf(i));
            keypadPanel.add(btn);
        }
        keypadPanel.add(createKeypadButton("Clear"));
        keypadPanel.add(createKeypadButton("0"));
        keypadPanel.add(createKeypadButton("Enter"));
        
        // Left panel combining input and keypad
        JPanel leftPanel = new JPanel(new BorderLayout(0, 5));
        leftPanel.setBackground(new Color(45, 45, 45));
        leftPanel.setPreferredSize(new Dimension(360, 225));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(keypadPanel, BorderLayout.CENTER);
        
        // Button panel (right side) - single column with fixed stable height
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(45, 45, 45));
        buttonPanel.setPreferredSize(new Dimension(180, 225));
        
        // Controls panel combining left panel and buttons side by side
        JPanel controlsPanel = new JPanel(new BorderLayout(8, 8));
        controlsPanel.setBackground(new Color(45, 45, 45));
        controlsPanel.add(leftPanel, BorderLayout.WEST);
        controlsPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Bottom panel with just controls
        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBackground(new Color(45, 45, 45));
        bottomPanel.add(controlsPanel, BorderLayout.CENTER);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JButton createKeypadButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(96, 125, 139));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        
        button.addActionListener(e -> {
            if (text.equals("Clear")) {
                inputField.setText("");
            } else if (text.equals("Enter")) {
                handleEnter();
            } else {
                inputField.setText(inputField.getText() + text);
            }
        });
        
        return button;
    }
    
    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        button.setPreferredSize(new Dimension(180, 38));
        button.setMaximumSize(new Dimension(180, 38));
        button.setMinimumSize(new Dimension(180, 38));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
    
    private void showMainMenu() {
        currentState = State.MAIN_MENU;
        displayArea.setText("========================================\n");
        displayArea.append("   WELCOME TO ATM BANKING SYSTEM\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Please select an option:\n\n");
        displayArea.append("1. Create New Account\n");
        displayArea.append("2. Login to Account\n");
        displayArea.append("3. Exit\n");
        
        buttonPanel.removeAll();
        
        JButton createBtn = createMenuButton("Create Account", new Color(70, 130, 180));
        createBtn.addActionListener(e -> showCreateAccount());
        
        JButton loginBtn = createMenuButton("Login", new Color(100, 100, 100));
        loginBtn.addActionListener(e -> showLogin());
        
        JButton exitBtn = createMenuButton("Exit", new Color(120, 120, 120));
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(createBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(exitBtn);
        buttonPanel.add(Box.createVerticalGlue());
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
    }
    
    private void showCreateAccount() {
        currentState = State.CREATE_ACCOUNT;
        displayArea.setText("========================================\n");
        displayArea.append("       CREATE NEW ACCOUNT\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Step 1: Enter initial deposit amount\n");
        displayArea.append("(Use keypad or type amount)\n");
        
        buttonPanel.removeAll();
        
        JButton confirmBtn = createMenuButton("Confirm", new Color(70, 130, 180));
        confirmBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(inputField.getText());
                createAccountStep2(amount);
            } catch (NumberFormatException ex) {
                showError("Invalid amount. Please enter a valid number.");
            }
        });
        
        JButton cancelBtn = createMenuButton("Cancel", new Color(120, 120, 120));
        cancelBtn.addActionListener(e -> showMainMenu());
        
        buttonPanel.add(confirmBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(cancelBtn);
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
        inputField.requestFocus();
    }
    
    private void createAccountStep2(double initialDeposit) {
        displayArea.setText("========================================\n");
        displayArea.append("       CREATE NEW ACCOUNT\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Step 2: Set your password\n");
        displayArea.append("(4-8 digits recommended)\n");
        
        buttonPanel.removeAll();
        
        JButton confirmBtn = createMenuButton("Create", new Color(70, 130, 180));
        confirmBtn.addActionListener(e -> {
            String password = inputField.getText();
            if (password.isEmpty()) {
                showError("Password cannot be empty!");
                return;
            }
            
            Account account = bank.addAccount();
            account.setPassword(password);
            account.deposit(initialDeposit);
            
            displayArea.setText("========================================\n");
            displayArea.append("      ACCOUNT CREATED SUCCESSFULLY!\n");
            displayArea.append("========================================\n\n");
            displayArea.append("Your Account Number: " + account.getAccountNumber() + "\n");
            displayArea.append("Initial Balance: $" + String.format("%.2f", initialDeposit) + "\n\n");
            displayArea.append("Please remember your account number\n");
            displayArea.append("and password for future access.\n");
            
            buttonPanel.removeAll();
            JButton okBtn = createMenuButton("OK", new Color(70, 130, 180));
            okBtn.addActionListener(ev -> showMainMenu());
            buttonPanel.add(okBtn);
            buttonPanel.add(Box.createVerticalGlue());
            buttonPanel.revalidate();
            buttonPanel.repaint();
            inputField.setText("");
        });
        
        JButton cancelBtn = createMenuButton("Cancel", new Color(120, 120, 120));
        cancelBtn.addActionListener(e -> showMainMenu());
        
        buttonPanel.add(confirmBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createVerticalGlue());
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
        inputField.requestFocus();
    }
    
    private void showLogin() {
        currentState = State.LOGIN;
        displayArea.setText("========================================\n");
        displayArea.append("           ACCOUNT LOGIN\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Step 1: Enter your account number\n");
        
        buttonPanel.removeAll();
        
        JButton confirmBtn = createMenuButton("Next", new Color(70, 130, 180));
        confirmBtn.addActionListener(e -> {
            try {
                int accountNumber = Integer.parseInt(inputField.getText());
                loginStep2(accountNumber);
            } catch (NumberFormatException ex) {
                showError("Invalid account number!");
            }
        });
        
        JButton cancelBtn = createMenuButton("Cancel", new Color(120, 120, 120));
        cancelBtn.addActionListener(e -> showMainMenu());
        
        buttonPanel.add(confirmBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createVerticalGlue());
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
        inputField.requestFocus();
    }
    
    private void loginStep2(int accountNumber) {
        displayArea.setText("========================================\n");
        displayArea.append("           ACCOUNT LOGIN\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Account Number: " + accountNumber + "\n\n");
        displayArea.append("Step 2: Enter your password\n");
        
        buttonPanel.removeAll();
        
        JButton loginBtn = createMenuButton("Login", new Color(70, 130, 180));
        loginBtn.addActionListener(e -> {
            String password = inputField.getText();
            Account account = bank.getAccount(accountNumber, password);
            
            if (account == null) {
                showError("Login failed!\nInvalid account number or password.");
            } else {
                currentAccount = account;
                showTransactionMenu();
            }
        });
        
        JButton cancelBtn = createMenuButton("Cancel", new Color(120, 120, 120));
        cancelBtn.addActionListener(e -> showMainMenu());
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createVerticalGlue());
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
        inputField.requestFocus();
    }
    
    private void showTransactionMenu() {
        currentState = State.TRANSACTION_MENU;
        displayArea.setText("========================================\n");
        displayArea.append("        TRANSACTION MENU\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Account: " + currentAccount.getAccountNumber() + "\n");
        displayArea.append("Current Balance: $" + String.format("%.2f", currentAccount.getBalance()) + "\n\n");
        displayArea.append("Select a transaction:\n");
        
        buttonPanel.removeAll();
        
        JButton balanceBtn = createMenuButton("View Balance", new Color(100, 100, 100));
        balanceBtn.addActionListener(e -> showBalance());
        
        JButton depositBtn = createMenuButton("Deposit", new Color(70, 130, 180));
        depositBtn.addActionListener(e -> showDeposit());
        
        JButton withdrawBtn = createMenuButton("Withdraw", new Color(70, 130, 180));
        withdrawBtn.addActionListener(e -> showWithdraw());
        
        JButton logoutBtn = createMenuButton("Logout", new Color(120, 120, 120));
        logoutBtn.addActionListener(e -> {
            currentAccount = null;
            showMainMenu();
        });
        
        buttonPanel.add(balanceBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(depositBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(logoutBtn);
        buttonPanel.add(Box.createVerticalGlue());
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
    }
    
    private void showBalance() {
        displayArea.setText("========================================\n");
        displayArea.append("         BALANCE INQUIRY\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Account Number: " + currentAccount.getAccountNumber() + "\n\n");
        displayArea.append("Current Balance: $" + String.format("%.2f", currentAccount.getBalance()) + "\n");
        
        buttonPanel.removeAll();
        JButton backBtn = createMenuButton("Back to Menu", new Color(100, 100, 100));
        backBtn.addActionListener(e -> showTransactionMenu());
        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
    
    private void showDeposit() {
        displayArea.setText("========================================\n");
        displayArea.append("            DEPOSIT\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Current Balance: $" + String.format("%.2f", currentAccount.getBalance()) + "\n\n");
        displayArea.append("Enter amount to deposit:\n");
        
        buttonPanel.removeAll();
        
        JButton confirmBtn = createMenuButton("Deposit", new Color(70, 130, 180));
        confirmBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(inputField.getText());
                if (amount <= 0) {
                    showError("Amount must be positive!");
                    return;
                }
                double newBalance = currentAccount.deposit(amount);
                displayArea.setText("========================================\n");
                displayArea.append("       TRANSACTION SUCCESSFUL\n");
                displayArea.append("========================================\n\n");
                displayArea.append("Deposited: $" + String.format("%.2f", amount) + "\n");
                displayArea.append("New Balance: $" + String.format("%.2f", newBalance) + "\n");
                
                buttonPanel.removeAll();
                JButton backBtn = createMenuButton("Back to Menu", new Color(33, 150, 243));
                backBtn.addActionListener(ev -> showTransactionMenu());
                buttonPanel.add(backBtn);
                buttonPanel.revalidate();
                buttonPanel.repaint();
                inputField.setText("");
            } catch (NumberFormatException ex) {
                showError("Invalid amount!");
            }
        });
        
        JButton cancelBtn = createMenuButton("Cancel", new Color(120, 120, 120));
        cancelBtn.addActionListener(e -> showTransactionMenu());
        
        buttonPanel.add(confirmBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createVerticalGlue());
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
        inputField.requestFocus();
    }
    
    private void showWithdraw() {
        displayArea.setText("========================================\n");
        displayArea.append("           WITHDRAWAL\n");
        displayArea.append("========================================\n\n");
        displayArea.append("Current Balance: $" + String.format("%.2f", currentAccount.getBalance()) + "\n\n");
        displayArea.append("Enter amount to withdraw:\n");
        
        buttonPanel.removeAll();
        
        JButton confirmBtn = createMenuButton("Withdraw", new Color(70, 130, 180));
        confirmBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(inputField.getText());
                if (amount <= 0) {
                    showError("Amount must be positive!");
                    return;
                }
                if (amount > currentAccount.getBalance()) {
                    showError("Insufficient funds!");
                    return;
                }
                double newBalance = currentAccount.deposit(amount * -1.0);
                displayArea.setText("========================================\n");
                displayArea.append("       TRANSACTION SUCCESSFUL\n");
                displayArea.append("========================================\n\n");
                displayArea.append("Withdrawn: $" + String.format("%.2f", amount) + "\n");
                displayArea.append("New Balance: $" + String.format("%.2f", newBalance) + "\n");
                
                buttonPanel.removeAll();
                JButton backBtn = createMenuButton("Back to Menu", new Color(33, 150, 243));
                backBtn.addActionListener(ev -> showTransactionMenu());
                buttonPanel.add(backBtn);
                buttonPanel.add(Box.createVerticalGlue());
                buttonPanel.revalidate();
                buttonPanel.repaint();
                inputField.setText("");
            } catch (NumberFormatException ex) {
                showError("Invalid amount!");
            }
        });
        
        JButton cancelBtn = createMenuButton("Cancel", new Color(120, 120, 120));
        cancelBtn.addActionListener(e -> showTransactionMenu());
        
        buttonPanel.add(confirmBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createVerticalGlue());
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
        inputField.setText("");
        inputField.requestFocus();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void handleEnter() {
        // Simulate clicking the primary action button based on current state
        Component[] components = buttonPanel.getComponents();
        if (components.length > 0 && components[0] instanceof JButton) {
            ((JButton) components[0]).doClick();
        }
    }
    
    /**
     * Main entry point for Swing ATM application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingDisplay display = new SwingDisplay();
            display.setVisible(true);
        });
    }
}
