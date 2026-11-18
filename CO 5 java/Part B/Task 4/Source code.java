
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.*;

public class B4Migration {

    static final String URL =
        "jdbc:mysql://localhost:3306/bus_transport";

    static final String USER = "root";
    static final String PASSWORD = "your_password";

    static final int BATCH_SIZE = 1000;

    static int migrate(Connection con, Path inputFile,
                       Path errorFile) throws Exception {

        String sql =
            "INSERT INTO bus_pass " +
            "(pass_id, reg_no, name, route_no, amount) " +
            "VALUES (?, ?, ?, ?, ?)";

        boolean oldAutoCommit = con.getAutoCommit();
        int validCount = 0;
        int invalidCount = 0;
        int batchCount = 0;
        int lineNumber = 0;

        con.setAutoCommit(false);

        try (BufferedReader br = Files.newBufferedReader(inputFile);
             BufferedWriter errorWriter =
                 Files.newBufferedWriter(errorFile);
             PreparedStatement ps = con.prepareStatement(sql)) {

            String line;

            while ((line = br.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    errorWriter.write(
                        "Line " + lineNumber + ": Empty record"
                    );
                    errorWriter.newLine();
                    invalidCount++;
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length != 5) {
                    errorWriter.write(
                        "Line " + lineNumber +
                        ": Wrong number of fields | " + line
                    );
                    errorWriter.newLine();
                    invalidCount++;
                    continue;
                }

                String passId = data[0].trim();
                String regNo = data[1].trim();
                String name = data[2].trim();

                int routeNo;
                BigDecimal amount;

                try {
                    routeNo = Integer.parseInt(data[3].trim());
                    amount = new BigDecimal(data[4].trim());

                    if (passId.isEmpty() ||
                        regNo.isEmpty() ||
                        name.isEmpty() ||
                        routeNo <= 0 ||
                        amount.compareTo(BigDecimal.ZERO) < 0) {

                        throw new IllegalArgumentException(
                            "Invalid field value"
                        );
                    }

                } catch (Exception e) {

                    errorWriter.write(
                        "Line " + lineNumber +
                        ": Invalid data | " + line
                    );
                    errorWriter.newLine();
                    invalidCount++;
                    continue;
                }

                ps.setString(1, passId);
                ps.setString(2, regNo);
                ps.setString(3, name);
                ps.setInt(4, routeNo);
                ps.setBigDecimal(5, amount);

                ps.addBatch();

                validCount++;
                batchCount++;

                if (batchCount == BATCH_SIZE) {
                    ps.executeBatch();
                    ps.clearBatch();
                    batchCount = 0;
                }
            }

            if (batchCount > 0) {
                ps.executeBatch();
                ps.clearBatch();
            }

            con.commit();

            System.out.println("\nMigration completed successfully.");
            System.out.println("Valid records inserted: " + validCount);
            System.out.println("Invalid records skipped: " + invalidCount);

            return validCount;

        } catch (Exception e) {

            con.rollback();

            System.out.println("\nMigration failed.");
            System.out.println("All database changes rolled back.");
            throw e;

        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }

    static void inspectDatabase(Connection con) throws SQLException {

        DatabaseMetaData md = con.getMetaData();

        System.out.println("\n===== DATABASE INFORMATION =====");

        System.out.println(
            "Database Product: " + md.getDatabaseProductName()
        );

        System.out.println(
            "Database Version: " + md.getDatabaseProductVersion()
        );

        System.out.println(
            "JDBC Driver: " + md.getDriverName()
        );

        System.out.println(
            "Driver Version: " + md.getDriverVersion()
        );

        System.out.println("\nTables:");

        try (ResultSet rs =
                 md.getTables(con.getCatalog(), null, "%",
                     new String[]{"TABLE"})) {

            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        }
    }

    static void displayRecordCount(Connection con)
            throws SQLException {

        String sql = "SELECT COUNT(*) FROM bus_pass";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println(
                    "\nTotal records in database: " + rs.getInt(1)
                );
            }
        }
    }

    public static void main(String[] args) {

        Path inputFile = Paths.get("buspasses.csv");
        Path errorFile = Paths.get("migration_errors.txt");

        if (!Files.exists(inputFile)) {
            System.out.println("Bus-pass file not found.");
            System.out.println(
                "Place buspasses.csv in the program folder."
            );
            return;
        }

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD)) {

            inspectDatabase(con);

            migrate(con, inputFile, errorFile);

            displayRecordCount(con);

            System.out.println(
                "\nInvalid records report: " + errorFile
            );

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
