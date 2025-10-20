/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bankmanagementgui;


    
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;

class Account {
    String accountNumber;
    String holderName;
    double balance;

    public Account(String accNum, String name, double bal) {
        accountNumber = accNum;
        holderName = name;
        balance = bal;
    }
@Override 
    public String toString() {
        return "Account Number: " + accountNumber + "\nName: " + holderName + "\nBalance: $" + balance;
    }

    void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

 public class BankManagementGUI {
    final static ArrayList<Account> accounts = new ArrayList<>();
    private static final String FILE_NAME = "accounts.txt";

    public static void main(String[] args) {
        loadAccountsFromFile();

        JFrame loginFrame = new JFrame("Bank Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        loginFrame.add(panel);
        placeLoginComponents(panel, loginFrame);
       
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private static void placeLoginComponents(JPanel panel, JFrame loginFrame) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 160, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 160, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 90, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String user = userText.getText();
            String pass = new String(passwordText.getPassword());

            if (user.equals("admin") && pass.equals("admin123")) {
                loginFrame.dispose();
                showAdminMenu();
            } else if (user.equals("user") && pass.equals("user123")) {
                loginFrame.dispose();
                showUserMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials");
            }
        });
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private static void showAdminMenu() {
        JFrame adminFrame = new JFrame("Admin Menu");
        adminFrame.setSize(400, 400);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Account");
        JButton viewButton = new JButton("View All Accounts");
        JButton searchButton = new JButton("Search Account");

        adminFrame.add(addButton);
        adminFrame.add(viewButton);
        adminFrame.add(searchButton);

        JTextArea outputArea = new JTextArea(15, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        adminFrame.add(scrollPane);

        addButton.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Enter Account Number:");
            if (!accNum.matches("\\d+")) {
                outputArea.setText("Invalid account number. Use digits only.");
                return;
            }
            String name = JOptionPane.showInputDialog("Enter Name:");
            String balStr = JOptionPane.showInputDialog("Enter Initial Balance:");
            try {
                double bal = Double.parseDouble(balStr);
                Account newAcc = new Account(accNum, name, bal);
                accounts.add(newAcc);
                saveAccountToFile(newAcc);
                outputArea.setText("Account added successfully.");
            } catch (Exception ex) {
                outputArea.setText("Invalid balance input.");
            }
        });

        viewButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Account a : accounts) {
                sb.append(a.toString()).append("\n------------------\n");
            }
            outputArea.setText(sb.length() > 0 ? sb.toString() : "No accounts found.");
        });

        searchButton.addActionListener(e -> {
            outputArea.setText("");  // clear old results
            String accNum = JOptionPane.showInputDialog("Enter Account Number to search:");
            boolean found = false;
            for (Account a : accounts) {
                if (a.accountNumber.equals(accNum)) {
                    outputArea.setText(a.toString());
                    found = true;
                    break;
                }
            }
            if (!found) {
                outputArea.setText("Account not found.");
            }
        });

        adminFrame.setVisible(true);
    }

    private static void showUserMenu() {
        JFrame userFrame = new JFrame("User Menu");
        userFrame.setSize(400, 400);
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userFrame.setLayout(new FlowLayout());

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton balanceButton = new JButton("View Balance");

        userFrame.add(depositButton);
        userFrame.add(withdrawButton);
        userFrame.add(balanceButton);

        JTextArea outputArea = new JTextArea(15, 30);
        outputArea.setEditable(false);
        userFrame.add(new JScrollPane(outputArea));

        depositButton.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Enter Account Number:");
            String amountStr = JOptionPane.showInputDialog("Enter Amount to Deposit:");
            try {
                double amount = Double.parseDouble(amountStr);
                boolean found = false;
                for (Account a : accounts) {
                    if (a.accountNumber.equals(accNum)) {
                        a.deposit(amount);
                        outputArea.setText("Deposit successful. New balance: $" + a.balance);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    outputArea.setText("Account not found.");
                }
            } catch (Exception ex) {
                outputArea.setText("Invalid amount.");
            }
        });

        withdrawButton.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Enter Account Number:");
            String amountStr = JOptionPane.showInputDialog("Enter Amount to Withdraw:");
            try {
                double amount = Double.parseDouble(amountStr);
                boolean found = false;
                for (Account a : accounts) {
                    if (a.accountNumber.equals(accNum)) {
                        if (a.withdraw(amount)) {
                            outputArea.setText("Withdrawal successful. New balance: $" + a.balance);
                        } else {
                            outputArea.setText("Insufficient funds.");
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    outputArea.setText("Account not found.");
                }
            } catch (Exception ex) {
                outputArea.setText("Invalid amount.");
            }
        });

        balanceButton.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Enter Account Number:");
            boolean found = false;
            for (Account a : accounts) {
                if (a.accountNumber.equals(accNum)) {
                    outputArea.setText("Balance: $" + a.balance);
                    found = true;
                    break;
                }
            }
            if (!found) {
                outputArea.setText("Account not found.");
            }
        });

        userFrame.setVisible(true);
    }

    private static void saveAccountToFile(Account acc) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(acc.accountNumber + "," + acc.holderName + "," + acc.balance + "\n");
        } catch (IOException e) {
            System.out.println("Error saving account to file.");
        }
    }

    private static void loadAccountsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String accNum = data[0];
                    String name = data[1];
                    double bal = Double.parseDouble(data[2]);
                    accounts.add(new Account(accNum, name, bal));
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data found.");
        }
    }
}