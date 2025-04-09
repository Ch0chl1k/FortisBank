import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI {
    public static void Login() {

        JFrame frame = new JFrame("oldVersion.Login");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(192, 192, 192));

        JLabel infoLogin = new JLabel("Enter your login details", SwingConstants.CENTER);
        infoLogin.setFont(new Font("Arial", Font.BOLD, 20));
        infoLogin.setForeground(Color.CYAN);
        infoLogin.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        frame.add(infoLogin, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setBackground(new Color(192, 192, 192));
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        JLabel emailLabel = new JLabel("Email:        ");
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 30));
        emailField.setMargin(new Insets(5, 5, 5, 5));

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 30));
        passwordField.setMargin(new Insets(5, 5, 5, 5));

        fieldsPanel.add(emailLabel);
        fieldsPanel.add(emailField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        frame.add(fieldsPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton submitButton = new JButton("Submit");
        buttonPanel.add(submitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.equals("Admin") && password.equals("admin1")) {
                    JOptionPane.showMessageDialog(frame, "Logged in as Admin.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    AdminGUI adminGui = new AdminGUI();
                    adminGui.showAdminPanel();
                    frame.dispose();
                }

                else if (Database.checkLogin(email, password) != -1) {
                    int userId = Database.checkLogin(email, password);
                    JOptionPane.showMessageDialog(frame, "Logged in successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new MainAppGUI(userId);
                    frame.dispose();
                }

                else {
                    JOptionPane.showMessageDialog(frame, "Email or password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                    frame.dispose();
                    MainGUI.MainGUI();
                }
            }
        });
    }
}
