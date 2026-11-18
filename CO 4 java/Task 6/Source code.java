
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.io.*;

public class Q6 extends JFrame {

    JTextField studentId = new JTextField(15);
    JTextField studentName = new JTextField(15);
    JTextField email = new JTextField(15);

    JComboBox<String> department = new JComboBox<>(
        new String[]{"AI&DS", "CSE", "ECE", "EEE", "MECH"}
    );

    JComboBox<String> event = new JComboBox<>(
        new String[]{"Dance", "Music", "Drama", "Singing", "Sports"}
    );

    JComboBox<String> filter = new JComboBox<>(
        new String[]{"All Events", "Dance", "Music", "Drama",
                     "Singing", "Sports"}
    );

    JButton register = new JButton("Register");
    JButton clear = new JButton("Clear");
    JButton search = new JButton("Filter");
    JButton export = new JButton("Export CSV");

    JLabel status = new JLabel("Ready");

    String[] columns = {
        "ID", "Student ID", "Name", "Department",
        "Email", "Event", "Registration Date"
    };

    DefaultTableModel model =
        new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    JTable table = new JTable(model);

    public Q6() {

        setTitle("Campus Event Registration");
        setSize(950, 600);
        setLayout(new BorderLayout(10, 10));

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu registration = new JMenu("Registration");
        JMenu help = new JMenu("Help");

        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem exportItem = new JMenuItem("Export CSV");
        JMenuItem about = new JMenuItem("About");

        exit.addActionListener(e -> dispose());
        refresh.addActionListener(e -> loadRegistrations());
        exportItem.addActionListener(e -> exportCSV());
        about.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this, "Campus Event Registration System"
            )
        );

        file.add(exportItem);
        file.add(exit);
        registration.add(refresh);
        help.add(about);

        menuBar.add(file);
        menuBar.add(registration);
        menuBar.add(help);

        setJMenuBar(menuBar);

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));

        form.setBorder(
            BorderFactory.createTitledBorder("Student Registration")
        );

        form.add(new JLabel("Student ID:"));
        form.add(studentId);

        form.add(new JLabel("Student Name:"));
        form.add(studentName);

        form.add(new JLabel("Department:"));
        form.add(department);

        form.add(new JLabel("Email:"));
        form.add(email);

        form.add(new JLabel("Select Event:"));
        form.add(event);

        JPanel buttons = new JPanel();

        buttons.add(register);
        buttons.add(clear);

        form.add(buttons);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        filterPanel.setBorder(
            BorderFactory.createTitledBorder("Committee Dashboard")
        );

        filterPanel.add(new JLabel("Filter Event:"));
        filterPanel.add(filter);
        filterPanel.add(search);
        filterPanel.add(export);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        topPanel.add(form, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        register.addActionListener(e -> registerStudent());

        clear.addActionListener(e -> clearFields());

        search.addActionListener(e -> loadRegistrations());

        export.addActionListener(e -> exportCSV());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        loadRegistrations();
    }

    void registerStudent() {

        String id = studentId.getText().trim();
        String name = studentName.getText().trim();
        String dept = (String) department.getSelectedItem();
        String mail = email.getText().trim();
        String selectedEvent = (String) event.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || mail.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please fill all fields"
            );
            return;
        }

        if (!mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(
                this, "Enter a valid email address"
            );
            return;
        }

        register.setEnabled(false);
        status.setText("Registering...");

        SwingWorker<Boolean, Void> worker =
            new SwingWorker<Boolean, Void>() {

            protected Boolean doInBackground() throws Exception {

                return EventService.register(
                    id, name, dept, mail, selectedEvent
                );
            }

            protected void done() {

                register.setEnabled(true);

                try {

                    boolean success = get();

                    if (success) {

                        JOptionPane.showMessageDialog(
                            Q6.this,
                            "Registration Successful!"
                        );

                        clearFields();
                        loadRegistrations();

                    } else {

                        JOptionPane.showMessageDialog(
                            Q6.this,
                            "Student already registered for this event!"
                        );

                        status.setText("Duplicate registration");
                    }

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                        Q6.this,
                        "Database Error: " + ex.getMessage()
                    );

                    status.setText("Registration failed");
                }
            }
        };

        worker.execute();
    }

    void loadRegistrations() {

        search.setEnabled(false);
        status.setText("Loading registrations...");

        String selectedEvent = (String) filter.getSelectedItem();

        SwingWorker<java.util.List<String[]>, Void> worker =
            new SwingWorker<java.util.List<String[]>, Void>() {

            protected java.util.List<String[]> doInBackground()
                    throws Exception {

                return EventService.getRegistrations(selectedEvent);
            }

            protected void done() {

                search.setEnabled(true);

                try {

                    java.util.List<String[]> records = get();

                    model.setRowCount(0);

                    for (String[] row : records) {
                        model.addRow(row);
                    }

                    status.setText(
                        "Total Registrations: " + records.size()
                    );

                } catch (Exception ex) {

                    status.setText("Unable to load registrations");

                    JOptionPane.showMessageDialog(
                        Q6.this,
                        "Database Error: " + ex.getMessage()
                    );
                }
            }
        };

        worker.execute();
    }

    void clearFields() {

        studentId.setText("");
        studentName.setText("");
        email.setText("");

        department.setSelectedIndex(0);
        event.setSelectedIndex(0);
    }

    void exportCSV() {

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this, "No registrations to export"
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();

        chooser.setSelectedFile(
            new File("Campus_Registrations.csv")
        );

        if (chooser.showSaveDialog(this) ==
                JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (PrintWriter writer = new PrintWriter(file)) {

                for (int i = 0; i < model.getColumnCount(); i++) {
                    if (i > 0) writer.print(",");
                    writer.print(csvValue(
                        model.getColumnName(i)
                    ));
                }

                writer.println();

                for (int i = 0; i < model.getRowCount(); i++) {

                    for (int j = 0; j < model.getColumnCount(); j++) {

                        if (j > 0) writer.print(",");

                        writer.print(csvValue(
                            model.getValueAt(i, j).toString()
                        ));
                    }

                    writer.println();
                }

                JOptionPane.showMessageDialog(
                    this, "CSV Export Successful!"
                );

                status.setText("CSV file exported successfully");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                    this, "Export Failed: " + ex.getMessage()
                );
            }
        }
    }

    String csvValue(String value) {

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    static class EventDAO {

        static final String URL =
            "jdbc:mysql://localhost:3306/campus_events";

        static final String USER = "root";

        static final String PASSWORD = "your_password";

        static Connection getConnection() throws SQLException {

            return DriverManager.getConnection(
                URL, USER, PASSWORD
            );
        }

        static boolean insert(String id, String name,
                              String dept, String email,
                              String event) throws SQLException {

            String sql =
                "INSERT INTO registrations " +
                "(student_id, student_name, department, email, event_name) " +
                "VALUES (?, ?, ?, ?, ?)";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, id);
                ps.setString(2, name);
                ps.setString(3, dept);
                ps.setString(4, email);
                ps.setString(5, event);

                ps.executeUpdate();

                return true;

            } catch (SQLIntegrityConstraintViolationException ex) {

                return false;
            }
        }

        static java.util.List<String[]> getAll(String event)
                throws SQLException {

            java.util.List<String[]> records = new ArrayList<>();

            String sql;

            if (event == null || event.equals("All Events")) {

                sql = "SELECT * FROM registrations ORDER BY id";

            } else {

                sql = "SELECT * FROM registrations " +
                      "WHERE event_name = ? ORDER BY id";
            }

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                if (event != null && !event.equals("All Events")) {
                    ps.setString(1, event);
                }

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        String[] row = {
                            String.valueOf(rs.getInt("id")),
                            rs.getString("student_id"),
                            rs.getString("student_name"),
                            rs.getString("department"),
                            rs.getString("email"),
                            rs.getString("event_name"),
                            rs.getTimestamp("registration_date").toString()
                        };

                        records.add(row);
                    }
                }
            }

            return records;
        }
    }

    static class EventService {

        static boolean register(String id, String name,
                                String dept, String email,
                                String event) throws SQLException {

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                throw new IllegalArgumentException(
                    "All fields are required"
                );
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException(
                    "Invalid email address"
                );
            }

            return EventDAO.insert(
                id, name, dept, email, event
            );
        }

        static java.util.List<String[]> getRegistrations(String event)
                throws SQLException {

            return EventDAO.getAll(event);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Q6().setVisible(true));
    }
}
