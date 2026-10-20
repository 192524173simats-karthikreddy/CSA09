
import java.util.Scanner;

public class task2 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Number of floors
        System.out.print("Enter number of floors: ");
        int floors = sc.nextInt();

        // Jagged array: each floor can have different slots
        String[][] slots = new String[floors][];

        for (int i = 0; i < floors; i++) {
            System.out.print("Enter slots on floor " + (i + 1) + ": ");
            int count = sc.nextInt();

            slots[i] = new String[count];
        }

        // Vehicle details
        sc.nextLine();

        System.out.print("Enter vehicle registration number: ");
        String vehicleNo = sc.nextLine();

        System.out.print("Enter floor number: ");
        int floor = sc.nextInt();

        System.out.print("Enter slot number: ");
        int slot = sc.nextInt();

        // Validate the floor and slot
        if (floor < 1 || floor > floors ||
                slot < 1 || slot > slots[floor - 1].length) {

            System.out.println("Invalid floor or slot number.");

        } else if (slots[floor - 1][slot - 1] != null) {

            System.out.println("Slot is already occupied.");

        } else {

            slots[floor - 1][slot - 1] = vehicleNo;
            System.out.println("Vehicle parked successfully.");
        }

        // Calculate parking fee
        System.out.print("Enter parking duration in minutes: ");
        int minutes = sc.nextInt();

        System.out.print("Enter hourly rate: ");
        double rate = sc.nextDouble();

        if (minutes < 0 || rate < 0) {
            System.out.println("Invalid duration or rate.");
        } else {
            // Every started hour is charged in full
            int hours = minutes / 60;

            if (minutes % 60 != 0) {
                hours++;
            }

            double fee = hours * rate;

            System.out.println("Billable hours: " + hours);
            System.out.println("Parking fee: Rs " + fee);
        }

        // Display parking slots
        System.out.println("\nParking Slot Details:");

        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[i].length; j++) {

                System.out.println(
                    "Floor " + (i + 1) +
                    ", Slot " + (j + 1) +
                    ": " +
                    (slots[i][j] == null ? "Free" : slots[i][j])
                );
            }
        }

        sc.close();
    }
}
