
import java.sql.*;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;

public class B2Adversarial {

    static final String URL = "jdbc:mysql://localhost:3306/b2_test";
    static final String USER = "root";
    static final String PASSWORD = "your_password";

    static void unsafeLogin(String id, String pwd) {

        String sql = "SELECT * FROM users WHERE id='" + id +
                     "' AND pwd='" + pwd + "'";

        System.out.println("\nUnsafe SQL:");
        System.out.println(sql);

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("Unsafe Login: ACCEPTED");
            } else {
                System.out.println("Unsafe Login: REJECTED");
            }

        } catch (SQLException e) {
            System.out.println("Unsafe Login Error: " + e.getMessage());
        }
    }

    static void safeLogin(String id, String pwd) {

        String sql = "SELECT * FROM users WHERE id = ? AND pwd = ?";

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, pwd);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println("PreparedStatement Login: ACCEPTED");
                } else {
                    System.out.println("PreparedStatement Login: REJECTED");
                }
            }

        } catch (SQLException e) {
            System.out.println("Safe Login Error: " + e.getMessage());
        }
    }

    static class PassStore {

        Path file;

        PassStore(Path file) {
            this.file = file;
        }

        String[] find(String passId) throws IOException {

            if (!Files.exists(file)) {
                System.out.println("Bus-pass file not found.");
                return null;
            }

            try (BufferedReader br = Files.newBufferedReader(file)) {

                String line;
                int lineNumber = 0;

                while ((line = br.readLine()) != null) {

                    lineNumber++;

                    if (line.trim().isEmpty()) {
                        System.out.println(
                            "Empty line skipped at line " + lineNumber
                        );
                        continue;
                    }

                    String[] data = line.split(",", -1);

                    if (data.length != 5) {
                        System.out.println(
                            "Wrong number of fields at line " + lineNumber
                        );
                        continue;
                    }

                    try {
                        Integer.parseInt(data[3]);
                        Double.parseDouble(data[4]);
                    } catch (NumberFormatException e) {
                        System.out.println(
                            "Invalid numeric data at line " + lineNumber
                        );
                        continue;
                    }

                    if (data[0].equals(passId)) {
                        return data;
                    }
                }
            }

            return null;
        }
    }

    static void testDamagedFile() throws IOException {

        Path file = Files.createTempFile("damaged_passes", ".csv");

        try {
            String records =
                "P1001,192311001,Arun,12,4500.0\n" +
                "P1002,DAMAGED LINE\n" +
                "P1003,192311003,Divya,7,ABC\n" +
                "\n" +
                "P1004,192311004,Ravi,10,3500.0\n";

            Files.writeString(file, records);

            PassStore store = new PassStore(file);

            System.out.println("\n===== DAMAGED FILE TEST =====");

            System.out.println("\nSearching P1001:");
            System.out.println(Arrays.toString(store.find("P1001")));

            System.out.println("\nSearching P1002:");
            System.out.println(Arrays.toString(store.find("P1002")));

            System.out.println("\nSearching P1003:");
            System.out.println(Arrays.toString(store.find("P1003")));

            System.out.println("\nSearching P1004:");
            System.out.println(Arrays.toString(store.find("P1004")));

        } finally {
            Files.deleteIfExists(file);
        }
    }

    public static void main(String[] args) {

        System.out.println("===== SQL INJECTION TEST =====");

        String attackId = "admin' OR '1'='1' -- ";
        String wrongPassword = "wrongpassword";

        System.out.println("\nTesting invalid login input:");

        unsafeLogin(attackId, wrongPassword);

        safeLogin(attackId, wrongPassword);

        try {
            testDamagedFile();
        } catch (IOException e) {
            System.out.println("File Error: " + e.getMessage());
        }
    }
}
