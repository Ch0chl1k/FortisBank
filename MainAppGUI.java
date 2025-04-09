import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainAppGUI {
    private JFrame mainFrame;
    private JLabel balanceLabel;
    private double balance;
    private int userId;
    private final Color BACKGROUND_COLOR = new Color(192, 192, 192);
    private final Color TEXT_COLOR = Color.CYAN;

    public MainAppGUI(int userId) {
        this.userId = userId;
        this.balance = BalanceDatabase.getBalance(userId);

        if (this.balance == 0.0) {
            BalanceDatabase.updateBalance(userId, 100.0);
            this.balance = 100.0;
        }

        initializeGUI();
    }

    private void initializeGUI() {
        mainFrame = new JFrame("Fortis Bank");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(BACKGROUND_COLOR);

        mainFrame.add(createTopPanel(), BorderLayout.NORTH);
        mainFrame.add(createCenterPanel(), BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(BACKGROUND_COLOR);

        JButton profileButton = new JButton();
        try {
            ImageIcon accountImage = new ImageIcon(getClass().getResource("/icons/account.png"));
            Image scaledImage = accountImage.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            profileButton.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            profileButton.setText("≡");
            profileButton.setForeground(TEXT_COLOR);
        }

        profileButton.setPreferredSize(new Dimension(40, 40));
        profileButton.setBorderPainted(false);
        profileButton.setContentAreaFilled(false);

        JPopupMenu profileMenu = new JPopupMenu();
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setForeground(Color.BLACK);
        profileMenu.add(logoutItem).addActionListener(e -> {
            mainFrame.dispose();
            MainGUI.MainGUI();
        });
        profileButton.addActionListener(e -> profileMenu.show(profileButton, 0, profileButton.getHeight()));

        topPanel.add(profileButton);
        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(BACKGROUND_COLOR);
        balanceLabel = new JLabel(String.format("$%.2f", balance), SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 36));
        balanceLabel.setForeground(TEXT_COLOR);
        balancePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TEXT_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        balancePanel.add(balanceLabel);

        gbc.gridy = 0;
        centerPanel.add(balancePanel, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton loanButton = new JButton("Take Loan");
        JButton creditButton = new JButton("Take Credit");

        Dimension buttonSize = new Dimension(140, 50);
        depositButton.setPreferredSize(buttonSize);
        withdrawButton.setPreferredSize(buttonSize);
        loanButton.setPreferredSize(buttonSize);
        creditButton.setPreferredSize(buttonSize);

        depositButton.setBackground(new Color(169, 169, 169));
        withdrawButton.setBackground(new Color(169, 169, 169));
        loanButton.setBackground(new Color(169, 169, 169));
        creditButton.setBackground(new Color(169, 169, 169));

        depositButton.addActionListener(e -> handleTransaction(true));
        withdrawButton.addActionListener(e -> handleTransaction(false));
        loanButton.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, "Loan feature coming soon!"));
        creditButton.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame, "Credit feature coming soon!"));

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(loanButton);
        buttonPanel.add(creditButton);

        gbc.gridy = 1;
        centerPanel.add(buttonPanel, gbc);

        return centerPanel;
    }

    private void handleTransaction(boolean isDeposit) {
        String input = JOptionPane.showInputDialog(mainFrame, "Enter amount:");
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(mainFrame, "Amount must be positive!");
                return;
            }

            if (isDeposit) {
                balance += amount;
            } else {
                if (amount > balance) {
                    JOptionPane.showMessageDialog(mainFrame, "Insufficient funds!");
                    return;
                }
                balance -= amount;
            }

            if (BalanceDatabase.updateBalance(userId, balance)) {
                balanceLabel.setText(String.format("$%.2f", balance));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid amount!");
        }
    }
}
