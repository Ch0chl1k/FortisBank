import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGUI {
    public static void RegisterGUI() {
        JFrame frame = new JFrame("Register");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(192, 192, 192));

        JLabel infoRegister = new JLabel("Enter your details to register", SwingConstants.CENTER);
        infoRegister.setFont(new Font("Arial", Font.BOLD, 20));
        infoRegister.setForeground(Color.CYAN);
        infoRegister.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        frame.add(infoRegister, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setBackground(new Color(192, 192, 192));
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        JLabel nameLabel = new JLabel("Name:                          ");
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(150, 30));
        nameField.setMargin(new Insets(5, 5, 5, 5));

        JLabel emailLabel = new JLabel("Email:                          ");
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 30));
        emailField.setMargin(new Insets(5, 5, 5, 5));

        JLabel confirmEmailLabel = new JLabel("Confirm Email:          ");
        JTextField confirmEmailField = new JTextField();
        confirmEmailField.setPreferredSize(new Dimension(150, 30));
        confirmEmailField.setMargin(new Insets(5, 5, 5, 5));

        JLabel passwordLabel = new JLabel("Password:                 ");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 30));
        passwordField.setMargin(new Insets(5, 5, 5, 5));

        JLabel confirmPasswordLabel = new JLabel("Confirm Password: ");
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(151, 30));
        confirmPasswordField.setMargin(new Insets(5, 5, 5, 5));

        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameField);
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(emailField);
        fieldsPanel.add(confirmEmailLabel);
        fieldsPanel.add(confirmEmailField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);
        fieldsPanel.add(confirmPasswordLabel);
        fieldsPanel.add(confirmPasswordField);

        frame.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel .setBackground(new Color(192, 192, 192));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton registerButton = new JButton("Register");
        buttonPanel.add(registerButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String confirmEmail = confirmEmailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (validateInput(name, email, confirmEmail, password, confirmPassword)) {
                    if (Database.emailExists(email)) {
                        JOptionPane.showMessageDialog(frame, "Email is already registered. Please log in.", "Error", JOptionPane.ERROR_MESSAGE);
                        LoginGUI login = new LoginGUI();
                        login.Login();
                        frame.dispose();
                    } else {
                        Database.saveUser (name, email, password);
                        JOptionPane.showMessageDialog(frame, "Registration successful!");
                        LoginGUI login = new LoginGUI();
                        login.Login();
                        frame.dispose();
                    }
                }
            }

        });


        frame.setVisible(true);
    }

    private static boolean validateInput(String name, String email, String confirmEmail, String password, String confirmPassword) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(null, "Name can't have numbers or special characters", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!email.endsWith("@gmail.com") && !email.endsWith("@outlook.com")) {
            JOptionPane.showMessageDialog(null, "E-mail must end with @gmail.com or @outlook.com", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (confirmEmail.isEmpty() || !email.equals(confirmEmail)) {
            JOptionPane.showMessageDialog(null, "Confirm email does not match", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(null, "Password must have 12 characters, 1 capital letter and 1 special letter", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 12 && password.matches("^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{12,}$");
    }

    private static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z]+$");
    }
}