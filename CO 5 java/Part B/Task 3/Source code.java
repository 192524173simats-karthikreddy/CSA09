
import java.sql.*;
import java.util.*;

public class PassLoader {

    static final String URL =
        "jdbc:mysql://localhost:3306/bus_transport";
    static final String USER = "root";
    static final String PASSWORD = "your_password";

    static final int BATCH_SIZE = 1000;

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void clearTable(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.executeUpdate("DELETE FROM bus_pass");
        }
    }

    static List<String[]> generatePasses(int count) {

        List<String[]> passes = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String[] p = {
                "P" + i,
                "REG" + i,
                "Student" + i,
                String.valueOf((i % 20) + 1),
                "4500.00"
            };

            passes.add(p);
        }

        return passes;
    }

    // TODO 1, TODO 2 and TODO 3 completed
    public static int loadAll(
            Connection con, List<String[]> passes)
            throws SQLException {

        String sql =
            "INSERT INTO bus_pass " +
            "(pass_id, reg_no, name, route_no, amount) " +
            "VALUES (?, ?, ?, ?, ?)";

        boolean oldAutoCommit = con.getAutoCommit();

        con.setAutoCommit(false);

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            int n = 0;

            for (String[] p : passes) {

                ps.setString(1, p[0]);
                ps.setString(2, p[1]);
                ps.setString(3, p[2]);
                ps.setInt(4, Integer.parseInt(p[3]));
                ps.setDouble(5, Double.parseDouble(p[4]));

                ps.addBatch();
                n++;

                if (n % BATCH_SIZE == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            ps.executeBatch();

            con.commit();

            return n;

        } catch (SQLException | RuntimeException e) {

            con.rollback();
            throw e;

        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }

    static int insertUsingStatement(
            Connection con, List<String[]> passes)
            throws SQLException {

        boolean oldAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);

        String sql;

        try (Statement st = con.createStatement()) {

            int count = 0;

            for (String[] p : passes) {

                sql = "INSERT INTO bus_pass " +
                      "(pass_id, reg_no, name, route_no, amount) " +
                      "VALUES ('" + p[0] + "', '" +
                      p[1] + "', '" + p[2] + "', " +
                      p[3] + ", " + p[4] + ")";

                st.executeUpdate(sql);
                count++;
            }

            con.commit();
            return count;

        } catch (SQLException e) {
            con.rollback();
            throw e;

        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }

    static int insertUsingPreparedStatement(
            Connection con, List<String[]> passes)
            throws SQLException {

        String sql =
            "INSERT INTO bus_pass " +
            "(pass_id, reg_no, name, route_no, amount) " +
            "VALUES (?, ?, ?, ?, ?)";

        boolean oldAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            int count = 0;

            for (String[] p : passes) {

                ps.setString(1, p[0]);
                ps.setString(2, p[1]);
                ps.setString(3, p[2]);
                ps.setInt(4, Integer.parseInt(p[3]));
                ps.setDouble(5, Double.parseDouble(p[4]));

                ps.executeUpdate();
                count++;
            }

            con.commit();
            return count;

        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;

        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }

    static int getRecordCount(Connection con) throws SQLException {

        String sql = "SELECT COUNT(*) FROM bus_pass";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }

    static void verifyRecords(Connection con) throws SQLException {

        String sql = "SELECT * FROM bus_pass ORDER BY pass_id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();

            System.out.println("\nColumn Information:");

            for (int i = 1; i <= md.getColumnCount(); i++) {
                System.out.println(
                    md.getColumnName(i) + " - " +
                    md.getColumnTypeName(i)
                );
            }

            System.out.println("\nFirst 10 Records:");

            int count = 0;

            while (rs.next() && count < 10) {
                System.out.println(
                    rs.getString("pass_id") + " " +
                    rs.getString("reg_no") + " " +
                    rs.getString("name") + " " +
                    rs.getInt("route_no") + " " +
                    rs.getBigDecimal("amount")
                );

                count++;
            }
        }

        System.out.println(
            "\nTotal Records: " + getRecordCount(con)
        );
    }

    static void testSmallData(Connection con) throws SQLException {

        System.out.println("\n===== B3.1 SMALL DATA TEST =====");

        List<String[]> passes = generatePasses(10);

        clearTable(con);
        insertUsingStatement(con, passes);

        System.out.println("\nStatement Result:");
        verifyRecords(con);

        clearTable(con);
        insertUsingPreparedStatement(con, passes);

        System.out.println("\nPreparedStatement Result:");
        verifyRecords(con);

        clearTable(con);
        loadAll(con, passes);

        System.out.println("\nBatch Result:");
        verifyRecords(con);
    }

    static void measurePerformance(Connection con)
            throws SQLException {

        System.out.println("\n===== B3.2 PERFORMANCE TEST =====");

        int[] sizes = {1000, 10000, 100000};

        System.out.printf(
            "%-12s %-18s %-22s %-18s%n",
            "Records", "Statement (ms)",
            "PreparedStatement (ms)", "Batch (ms)"
        );

        for (int size : sizes) {

            List<String[]> passes = generatePasses(size);

            clearTable(con);

            long start = System.nanoTime();
            insertUsingStatement(con, passes);
            long statementTime =
                (System.nanoTime() - start) / 1_000_000;

            clearTable(con);

            start = System.nanoTime();
            insertUsingPreparedStatement(con, passes);
            long preparedTime =
                (System.nanoTime() - start) / 1_000_000;

            clearTable(con);

            start = System.nanoTime();
            loadAll(con, passes);
            long batchTime =
                (System.nanoTime() - start) / 1_000_000;

            System.out.printf(
                "%-12d %-18d %-22d %-18d%n",
                size, statementTime, preparedTime, batchTime
            );

            System.out.println(
                "Verified rows: " + getRecordCount(con)
            );
        }
    }

    static void testFailure(Connection con, boolean transaction)
            throws SQLException {

        clearTable(con);

        String sql =
            "INSERT INTO bus_pass " +
            "(pass_id, reg_no, name, route_no, amount) " +
            "VALUES (?, ?, ?, ?, ?)";

        con.setAutoCommit(!transaction);

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 10000; i++) {

                ps.setString(1, "F" + i);
                ps.setString(2, "REG" + i);
                ps.setString(3, "Student" + i);
                ps.setInt(4, 10);

                if (i == 5001) {
                    ps.setDouble(5, -100);
                } else {
                    ps.setDouble(5, 4500);
                }

                ps.addBatch();

                if (i % BATCH_SIZE == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            ps.executeBatch();

            if (transaction) {
                con.commit();
            }

        } catch (SQLException e) {

            if (transaction) {
                con.rollback();
            }

            System.out.println(
                "Expected failure: " + e.getMessage()
            );

        } finally {
            con.setAutoCommit(true);
        }

        System.out.println(
            "Auto-commit: " + (!transaction)
        );

        System.out.println(
            "Transaction used: " + transaction
        );

        System.out.println(
            "Records remaining: " + getRecordCount(con)
        );
    }

    static void testTransactions(Connection con)
            throws SQLException {

        System.out.println("\n===== B3.3 FAILURE TEST =====");

        System.out.println("\nAuto-commit ON:");

        testFailure(con, false);

        System.out.println("\nSingle Transaction:");

        testFailure(con, true);
    }

    public static void main(String[] args) {

        try (Connection con = getConnection()) {

            testSmallData(con);

            measurePerformance(con);

            testTransactions(con);

            System.out.println("\nAll tests completed.");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
