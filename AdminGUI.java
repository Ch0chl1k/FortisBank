import javax.swing.*;
import java.awt.*;

public class AdminGUI {

    public void showAdminPanel() {
        JFrame frame = new JFrame("Admin Panel");
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(192, 192, 192));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(192, 192, 192));

        JLabel titleLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 85, 10, 50));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(192, 192, 192));
        centerPanel.add(titleLabel);
        headerPanel.add(centerPanel, BorderLayout.CENTER);

        JButton profileButton = createProfileButton();
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePanel.setBackground(new Color(192, 192, 192));
        profilePanel.add(profileButton);
        headerPanel.add(profilePanel, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(192, 192, 192));
        buttonPanel.setLayout(new GridLayout(3, 2, 2, 2));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton userCountButton = createButton("Get User Count");
        JButton userListButton = createButton("Show User List");
        JButton addUserButton = createButton("Add User");
        JButton deleteUserButton = createButton("Delete User");
        JButton addMoneyButton = createButton("Add Money");
        JButton deleteMoneyButton = createButton("Delete Money");

        buttonPanel.add(userCountButton);
        buttonPanel.add(userListButton);
        buttonPanel.add(addUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(addMoneyButton);
        buttonPanel.add(deleteMoneyButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        userCountButton.addActionListener(e -> {
            int userCount = Database.getUserCount();
            JOptionPane.showMessageDialog(frame, "Total users: " + userCount);
        });

        userListButton.addActionListener(e -> {
            String userList = Database.getUserList();
            JOptionPane.showMessageDialog(frame, "Users:\n" + userList);
        });

        addUserButton.addActionListener(e -> showAddUserDialog(frame));
        deleteUserButton.addActionListener(e -> showDeleteUserDialog(frame));
        addMoneyButton.addActionListener(e -> showAddMoneyDialog(frame));
        deleteMoneyButton.addActionListener(e -> showDeleteMoneyDialog(frame));

        frame.setVisible(true);
    }

    private JButton createProfileButton() {
        JButton profileButton = new JButton();
        try {
            ImageIcon accountImage = new ImageIcon(getClass().getResource("/icons/account.png"));
            Image scaledImage = accountImage.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            profileButton.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            profileButton.setText("≡");
        }

        profileButton.setPreferredSize(new Dimension(35, 35));
        profileButton.setBorderPainted(false);
        profileButton.setContentAreaFilled(false);

        JPopupMenu profileMenu = new JPopupMenu();
        profileMenu.add(new JMenuItem("Logout")).addActionListener(e -> System.exit(0));

        profileButton.addActionListener(e -> profileMenu.show(profileButton, 0, profileButton.getHeight()));

        return profileButton;
    }
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }

    private void showAddUserDialog(JFrame parentFrame) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passwordField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(parentFrame, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Database.emailExists(email)) {
                JOptionPane.showMessageDialog(parentFrame, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Database.saveUser(name, email, password);
            JOptionPane.showMessageDialog(parentFrame, "User added successfully!");
        }
    }

    private void showDeleteUserDialog(JFrame parentFrame) {
        JTextField idField = new JTextField();

        Object[] message = {
                "User ID:", idField
        };

        int option = JOptionPane.showConfirmDialog(parentFrame, message, "Delete User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                if (id == 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Cannot delete Admin!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = Database.deleteUserById(id);
                if (success) {
                    JOptionPane.showMessageDialog(parentFrame, "User deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddMoneyDialog(JFrame parentFrame) {
        JTextField idField = new JTextField();
        JTextField amountField = new JTextField();

        Object[] message = {
                "User ID:", idField,
                "Amount:", amountField
        };

        int option = JOptionPane.showConfirmDialog(parentFrame, message, "Add Money", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());
                double currentBalance = BalanceDatabase.getBalance(id);

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = BalanceDatabase.updateBalance(id, currentBalance + amount);
                if (success) {
                    JOptionPane.showMessageDialog(parentFrame, "Money added successfully!");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Operation failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid input format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteMoneyDialog(JFrame parentFrame) {
        JTextField idField = new JTextField();
        JTextField amountField = new JTextField();

        Object[] message = {
                "User ID:", idField,
                "Amount:", amountField
        };

        int option = JOptionPane.showConfirmDialog(parentFrame, message, "Delete Money", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());
                double currentBalance = BalanceDatabase.getBalance(id);

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (currentBalance < amount) {
                    JOptionPane.showMessageDialog(parentFrame, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = BalanceDatabase.updateBalance(id, currentBalance - amount);
                if (success) {
                    JOptionPane.showMessageDialog(parentFrame, "Money removed successfully!");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Operation failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid input format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminGUI().showAdminPanel());
    }
}