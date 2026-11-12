
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Q1 {

    static int readNumber(Scanner sc) {
        String s = sc.next();

        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input! Enter numbers only.");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int account, amount;
        int balance = 10000;

        try {
            System.out.print("Enter Account Number: ");
            account = readNumber(sc);

            if (account <= 0) {
                throw new IllegalArgumentException("Invalid account number.");
            }

            File file = new File("card_reader.txt");

            if (!file.exists()) {
                throw new FileNotFoundException("Card reader file is missing.");
            }

            System.out.print("Enter Withdrawal Amount: ");
            amount = readNumber(sc);

            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive.");
            }

            if (amount > balance) {
                throw new ArithmeticException("Insufficient account balance.");
            }

            balance = balance - amount;

            System.out.println("\nWithdrawal Successful!");
            System.out.println("Withdrawn Amount: " + amount);
            System.out.println("Remaining Balance: " + balance);
        }

        catch (IllegalArgumentException e) {
            System.out.println("\nInput Error: " + e.getMessage());
        }

        catch (FileNotFoundException e) {
            System.out.println("\nFile Error: " + e.getMessage());
        }

        catch (ArithmeticException e) {
            System.out.println("\nTransaction Error: " + e.getMessage());
        }

        catch (Exception e) {
            System.out.println("\nGeneral Error: " + e.getMessage());
        }

        finally {
            System.out.println("\nATM session closed safely.");
            sc.close();
        }
    }
}
