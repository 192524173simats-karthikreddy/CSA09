
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Q2 extends JFrame {

    JTextField patientId = new JTextField(15);

    JComboBox<String> department;
    JComboBox<String> doctor = new JComboBox<>();
    JComboBox<String> timeSlot;

    JButton book = new JButton("Book Appointment");
    JButton clear = new JButton("Clear");

    Map<String, java.util.List<String>> doctorMap =
        new HashMap<>();

    boolean bookingInProgress = false;
    SwingWorker<String, Void> bookingWorker;

    Q2() {

        setTitle("Hospital Appointment Desk");
        setSize(450, 350);
        setLayout(new BorderLayout(10, 10));

        doctorMap.put("Cardiology",
            Arrays.asList("Dr. Ravi", "Dr. Kumar"));

        doctorMap.put("Neurology",
            Arrays.asList("Dr. Priya", "Dr. Arun"));

        doctorMap.put("Orthopedics",
            Arrays.asList("Dr. Suresh", "Dr. Anil"));

        department = new JComboBox<>(
            new String[]{"Cardiology", "Neurology", "Orthopedics"}
        );

        timeSlot = new JComboBox<>(
            new String[]{"10:00 AM", "11:00 AM", "12:00 PM",
                         "02:00 PM", "03:00 PM"}
        );

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));

        form.add(new JLabel("Patient ID:"));
        form.add(patientId);

        form.add(new JLabel("Department:"));
        form.add(department);

        form.add(new JLabel("Doctor:"));
        form.add(doctor);

        form.add(new JLabel("Time Slot:"));
        form.add(timeSlot);

        JPanel buttons = new JPanel();

        buttons.add(book);
        buttons.add(clear);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        refreshDoctors();

        department.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refreshDoctors();
            }
        });

        book.addActionListener(e -> bookAppointment());

        clear.addActionListener(e -> {
            if (bookingInProgress) {
                JOptionPane.showMessageDialog(
                    this, "Booking is in progress"
                );
                return;
            }

            patientId.setText("");
            department.setSelectedIndex(0);
            timeSlot.setSelectedIndex(0);
        });

        addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {
                System.out.println("Window Opened");
            }

            public void windowClosing(WindowEvent e) {

                if (bookingInProgress) {

                    int choice = JOptionPane.showConfirmDialog(
                        Q2.this,
                        "Booking is in progress. Do you want to close?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (choice == JOptionPane.YES_OPTION) {
                        bookingWorker.cancel(true);
                        dispose();
                    }

                } else {

                    int choice = JOptionPane.showConfirmDialog(
                        Q2.this,
                        "Are you sure you want to close?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (choice == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                }
            }

            public void windowClosed(WindowEvent e) {
                System.out.println("Window Closed");
            }
        });

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void refreshDoctors() {

        String selectedDepartment =
            (String) department.getSelectedItem();

        doctor.removeAllItems();

        if (selectedDepartment != null) {

            java.util.List<String> doctors =
                doctorMap.get(selectedDepartment);

            if (doctors != null) {
                for (String name : doctors) {
                    doctor.addItem(name);
                }
            }
        }
    }

    void bookAppointment() {

        String id = patientId.getText().trim();
        String dept = (String) department.getSelectedItem();
        String doc = (String) doctor.getSelectedItem();
        String slot = (String) timeSlot.getSelectedItem();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter Patient ID"
            );
            return;
        }

        if (doc == null || slot == null) {
            JOptionPane.showMessageDialog(
                this, "Please select Doctor and Time Slot"
            );
            return;
        }

        bookingInProgress = true;
        book.setEnabled(false);

        bookingWorker = new SwingWorker<String, Void>() {

            protected String doInBackground() throws Exception {

                Thread.sleep(2000);

                return "Appointment Booked Successfully!\n"
                     + "Patient ID: " + id
                     + "\nDepartment: " + dept
                     + "\nDoctor: " + doc
                     + "\nTime Slot: " + slot;
            }

            protected void done() {

                bookingInProgress = false;
                book.setEnabled(true);

                if (isCancelled()) {
                    return;
                }

                try {
                    JOptionPane.showMessageDialog(
                        Q2.this, get()
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        Q2.this, "Booking Failed"
                    );
                }
            }
        };

        bookingWorker.execute();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Q2());
    }
}
