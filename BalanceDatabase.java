import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BalanceDatabase {
    private static final String FILE_NAME = "balances.txt";

    public static double getBalance(int userId) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            createNewBalanceFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && Integer.parseInt(parts[0].trim()) == userId) {
                    return Double.parseDouble(parts[1].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Balance read error: " + e.getMessage());
        }
        return 0.0;
    }

    public static boolean updateBalance(int userId, double newBalance) {
        Map<Integer, Double> balances = new HashMap<>();
        boolean userExists = false;

        // Wczytaj istniejące salda
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0].trim());
                    double balance = Double.parseDouble(parts[1].trim());
                    balances.put(id, balance);
                    if (id == userId) userExists = true;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Balance read error: " + e.getMessage());
        }

        // Aktualizuj saldo
        balances.put(userId, newBalance);

        // Zapisz nowe dane
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<Integer, Double> entry : balances.entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Balance write error: " + e.getMessage());
            return false;
        }
    }

    private static void createNewBalanceFile() {
        try {
            new File(FILE_NAME).createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating balance file: " + e.getMessage());
        }
    }

    public static boolean deleteBalance(int userId) {
        Map<Integer, Double> balances = new HashMap<>();
        boolean userExists = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                int id = Integer.parseInt(parts[0].trim());
                double balance = Double.parseDouble(parts[1].trim());
                if (id == userId) {
                    userExists = true;
                    continue; // Pomijamy tego użytkownika
                }
                balances.put(id, balance);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Balance read error: " + e.getMessage());
            return false;
        }

        if (!userExists) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<Integer, Double> entry : balances.entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Balance write error: " + e.getMessage());
            return false;
        }
    }
}