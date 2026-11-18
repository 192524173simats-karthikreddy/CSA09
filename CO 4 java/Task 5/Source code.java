
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Q5A extends JFrame implements ActionListener {

    JTextField amount = new JTextField(10);
    JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    Q5A() {
        setTitle("Currency Converter");
        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        convert.addActionListener(this);
        amount.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        convertAmount();
    }

    void convertAmount() {
        try {
            double value = Double.parseDouble(amount.getText().trim());

            if (!Double.isFinite(value) || value < 0)
                throw new NumberFormatException();

            String currency = (String) target.getSelectedItem();
            double rate = currency.equals("USD") ? 0.012 : 0.011;
            double total = value * rate;

            String message = String.format("Converted Amount: %.2f %s", total, currency);
            result.setText(message);
            JOptionPane.showMessageDialog(this, message);

        } catch (NumberFormatException e) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Q5A());
    }
}


class Q5B extends JFrame {

    JTextField amount = new JTextField(10);
    JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    Q5B() {
        setTitle("Currency Converter");
        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        ConvertHandler handler = new ConvertHandler();
        convert.addActionListener(handler);
        amount.addActionListener(handler);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class ConvertHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            convertAmount();
        }
    }

    void convertAmount() {
        try {
            double value = Double.parseDouble(amount.getText().trim());

            if (!Double.isFinite(value) || value < 0)
                throw new NumberFormatException();

            String currency = (String) target.getSelectedItem();
            double rate = currency.equals("USD") ? 0.012 : 0.011;
            double total = value * rate;

            String message = String.format("Converted Amount: %.2f %s", total, currency);
            result.setText(message);
            JOptionPane.showMessageDialog(this, message);

        } catch (NumberFormatException e) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Q5B());
    }
}


class Q5C extends JFrame {

    JTextField amount = new JTextField(10);
    JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    Q5C() {
        setTitle("Currency Converter");
        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        ActionListener handler = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertAmount();
            }
        };

        convert.addActionListener(handler);
        amount.addActionListener(handler);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void convertAmount() {
        try {
            double value = Double.parseDouble(amount.getText().trim());

            if (!Double.isFinite(value) || value < 0)
                throw new NumberFormatException();

            String currency = (String) target.getSelectedItem();
            double rate = currency.equals("USD") ? 0.012 : 0.011;
            double total = value * rate;

            String message = String.format("Converted Amount: %.2f %s", total, currency);
            result.setText(message);
            JOptionPane.showMessageDialog(this, message);

        } catch (NumberFormatException e) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Q5C());
    }
}


class Q5D extends JFrame {

    JTextField amount = new JTextField(10);
    JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    Q5D() {
        setTitle("Currency Converter");
        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        ActionListener handler = e -> convertAmount();

        convert.addActionListener(handler);
        amount.addActionListener(handler);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void convertAmount() {
        try {
            double value = Double.parseDouble(amount.getText().trim());

            if (!Double.isFinite(value) || value < 0)
                throw new NumberFormatException();

            String currency = (String) target.getSelectedItem();
            double rate = currency.equals("USD") ? 0.012 : 0.011;
            double total = value * rate;

            String message = String.format("Converted Amount: %.2f %s", total, currency);
            result.setText(message);
            JOptionPane.showMessageDialog(this, message);

        } catch (NumberFormatException e) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Q5D());
    }
}
