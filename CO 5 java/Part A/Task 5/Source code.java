
import java.sql.*;
import java.util.Scanner;

public class A5Safety {

    static final String URL = "jdbc:mysql://localhost:3306/hostel";
    static final String USER = "root";
    static final String PASSWORD = "your_password";

    static Scanner sc = new Scanner(System.in);

    static void transferMoney() {

        String debit = "UPDATE wallet SET balance = balance - 500 WHERE id = 1 AND balance >= 500";
        String credit = "UPDATE wallet SET balance = balance + 500 WHERE id = 2";

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD)) {

            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps1 = con.prepareStatement(debit);
                     PreparedStatement ps2 = con.prepareStatement(credit)) {

                    int first = ps1.executeUpdate();

                    if (first != 1) {
                        throw new SQLException("Hostel wallet update failed.");
                    }

                    int second = ps2.executeUpdate();

                    if (second != 1) {
                        throw new SQLException("Canteen wallet update failed.");
                    }
                }

                con.commit();
                System.out.println("Transfer of Rs.500 Successful.");

            } catch (SQLException e) {
                con.rollback();
                System.out.println("Transfer Failed.");
                System.out.println("Transaction Rolled Back.");
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    static void secureLogin() {

        System.out.print("Enter User ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Password: ");
        String pwd = sc.nextLine();

        String sql = "SELECT * FROM users WHERE id = ? AND pwd = ?";

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, pwd);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println("Login Successful.");
                } else {
                    System.out.println("Invalid ID or Password.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    static void displayBalances() {

        String sql = "SELECT id, balance FROM wallet ORDER BY id";

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nWallet Balances:");

            while (rs.next()) {
                System.out.println(
                    "Wallet ID: " + rs.getInt("id") +
                    " Balance: Rs." + rs.getDouble("balance")
                );
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== HOSTEL SAFETY SYSTEM =====");
            System.out.println("1. Transfer Rs.500");
            System.out.println("2. Secure Login");
            System.out.println("3. Display Wallet Balances");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    transferMoney();
                    break;

                case "2":
                    secureLogin();
                    break;

                case "3":
                    displayBalances();
                    break;

                case "4":
                    System.out.println("Program Terminated.");
                    return;

                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
}
