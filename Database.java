import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {
    private static final String FILE_NAME = "users.txt";
    private static AtomicInteger idCounter = new AtomicInteger(1);

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        if (!emailExists("Admin")) {
            saveUser(0, "Admin", "Admin", "admin1");
        } else {
            updateIdCounter();
        }
    }

    private static void updateIdCounter() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(" : ");
                int userId = Integer.parseInt(user[0]);
                if (userId > maxId) {
                    maxId = userId;
                }
            }
            idCounter.set(maxId + 1);
        } catch (IOException | NumberFormatException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }
    }

    private static int getNextAvailableId() {
        Set<Integer> existingIds = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(" : ");
                int userId = Integer.parseInt(user[0]);
                existingIds.add(userId);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }

        int nextId = 1;
        while (existingIds.contains(nextId)) {
            nextId++;
        }
        return nextId;
    }

    public static void saveUser(int id, String name, String email, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(id + " : " + name + " : " + email + " : " + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }
    }

    public static void saveUser(String name, String email, String password) {
        int id = getNextAvailableId();
        saveUser(id, name, email, password);
    }

    public static int checkLogin(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(" : ");
                if (user.length == 4 && user[2].equals(email) && user[3].equals(password)) {
                    return Integer.parseInt(user[0]); // Zwróć ID użytkownika
                }
            }
        } catch (IOException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }
        return -1; // Zwróć -1 jeśli logowanie nieudane
    }

    public static boolean emailExists(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(" : ");
                if (user.length >= 3 && user[2].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteUserById(int id) {
        if (id == 0) return false;

        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp_users.txt");
        boolean userDeleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(" : ");
                if (Integer.parseInt(user[0]) == id) {
                    userDeleted = true;
                    BalanceDatabase.deleteBalance(id);
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }

        if (userDeleted) {
            inputFile.delete();
            tempFile.renameTo(inputFile);
        }
        return userDeleted;
    }

    public static int getUserCount() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(" : ");
                if (user.length == 4 && !user[2].equals("Admin")) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }
        return count;
    }

    public static String getUserList() {
        StringBuilder users = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                users.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("File ERROR: " + e.getMessage());
        }
        return users.toString();
    }
    public static boolean addMoney(int id, double amount) {
        double currentBalance = BalanceDatabase.getBalance(id);
        return BalanceDatabase.updateBalance(id, currentBalance + amount);
    }

    public static boolean deleteMoney(int id, double amount) {
        double currentBalance = BalanceDatabase.getBalance(id);
        if (currentBalance >= amount) {
            return BalanceDatabase.updateBalance(id, currentBalance - amount);
        }
        return false;
    }
}
