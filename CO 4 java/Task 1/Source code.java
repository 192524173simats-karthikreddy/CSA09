
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Q1 extends JFrame {

    JTextField regNo = new JTextField(15);
    JTextField amount = new JTextField(15);

    JButton pay = new JButton("Pay");
    JButton clear = new JButton("Clear");
    JButton receipt = new JButton("Receipt");

    FeeService service = new FeeService();
    String lastReceipt = "";

    Q1() {
        setTitle("Student Fee Payment");
        setSize(350, 250);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Registration Number:"));
        add(regNo);

        add(new JLabel("Amount:"));
        add(amount);

        add(pay);
        add(clear);
        add(receipt);

        pay.addActionListener(e -> {
            try {
                String result = service.payFee(
                    regNo.getText(),
                    amount.getText()
                );

                lastReceipt = result;

                JOptionPane.showMessageDialog(this, result);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        clear.addActionListener(e -> {
            regNo.setText("");
            amount.setText("");
        });

        receipt.addActionListener(e -> {
            if (lastReceipt.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "No payment receipt available"
                );
            } else {
                JOptionPane.showMessageDialog(this, lastReceipt);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Q1());
    }
}

class FeeService {

    public String payFee(String regNo, String amount) {

        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Enter Registration Number"
            );
        }

        double a;

        try {
            a = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Enter a valid amount"
            );
        }

        if (!Double.isFinite(a) || a <= 0) {
            throw new IllegalArgumentException(
                "Amount must be greater than zero"
            );
        }

        return "Payment Successful!\n"
             + "Registration Number: " + regNo
             + "\nAmount Paid: Rs. " + a;
    }
}
