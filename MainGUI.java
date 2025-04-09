import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    public static void MainGUI() {

        JFrame frame = new JFrame("Fortis Bank");
        frame.setSize(400, 230);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(192,192,192));

        JLabel welcome = new JLabel("Welcome to the Fortis Bank", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        welcome.setForeground(Color.CYAN);
        welcome.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        frame.add(welcome, BorderLayout.NORTH);

        JLabel connect = new JLabel("Choose one of the actions to connect", SwingConstants.CENTER);
        connect.setFont(new Font("Arial", Font.BOLD, 15));
        connect.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        connect.setForeground(Color.CYAN);

        frame.add(connect);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 30));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.setBackground(new Color(169,169,169));
        registerButton.setBackground(new Color(169,169,169));
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI login = new LoginGUI();
                login.Login();
                frame.dispose();
            }
        });

       registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               RegisterGUI register = new RegisterGUI();
               register.RegisterGUI();
               frame.dispose();
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
