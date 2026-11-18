
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Q3 extends JFrame {

    JTextField searchField = new JTextField(20);

    JComboBox<String> category = new JComboBox<>(
        new String[]{"All", "Books", "Authors", "Subjects"}
    );

    JButton search = new JButton("Search");
    JButton prev = new JButton("Prev");
    JButton next = new JButton("Next");
    JButton reserve = new JButton("Reserve");

    JTextArea results = new JTextArea();
    JLabel status = new JLabel("Ready to search");

    Q3() {

        setTitle("Campus Library Search");
        setSize(650, 450);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
            "CAMPUS LIBRARY SEARCH", JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel searchPanel = new JPanel(new FlowLayout());

        searchPanel.add(searchField);
        searchPanel.add(category);
        searchPanel.add(search);

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        results.setEditable(false);
        results.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(results);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(prev);
        buttonPanel.add(next);
        buttonPanel.add(reserve);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(status, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> {
            String text = searchField.getText().trim();

            if (text.isEmpty()) {
                status.setText("Please enter a search keyword");
                return;
            }

            results.setText(
                "Search Results\n\n" +
                "Book 1: Java Programming\n" +
                "Book 2: Data Structures\n" +
                "Book 3: Computer Networks\n" +
                "Book 4: Operating Systems\n"
            );

            status.setText(
                "Search completed for: " + text
            );
        });

        prev.addActionListener(e -> {
            status.setText("Previous page selected");
        });

        next.addActionListener(e -> {
            status.setText("Next page selected");
        });

        reserve.addActionListener(e -> {
            if (results.getText().isEmpty()) {
                status.setText("Search for a book first");
            } else {
                status.setText("Book reserved successfully");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Q3());
    }
}
