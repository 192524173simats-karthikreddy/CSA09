
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BusPassRecords {

    static String textFile = "buspasses.txt";
    static String binaryFile = "buspasses.dat";

    static Scanner sc = new Scanner(System.in);

    static class BusPass {
        String passId;
        String regNo;
        String name;
        int routeNo;
        String boardingStop;
        String validityDate;
        double amountPaid;

        BusPass(String passId, String regNo, String name,
                int routeNo, String boardingStop,
                String validityDate, double amountPaid) {

            this.passId = passId;
            this.regNo = regNo;
            this.name = name;
            this.routeNo = routeNo;
            this.boardingStop = boardingStop;
            this.validityDate = validityDate;
            this.amountPaid = amountPaid;
        }

        public String toString() {
            return "Pass ID: " + passId +
                   "\nRegister Number: " + regNo +
                   "\nStudent Name: " + name +
                   "\nRoute Number: " + routeNo +
                   "\nBoarding Stop: " + boardingStop +
                   "\nValidity Date: " + validityDate +
                   "\nAmount Paid: " + amountPaid +
                   "\n-----------------------------";
        }
    }

    static String escape(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    static String[] splitRecord(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (escaped) {
                if (ch != '\\' && ch != '|') {
                    throw new IllegalArgumentException("Invalid escape");
                }
                field.append(ch);
                escaped = false;
            } else if (ch == '\\') {
                escaped = true;
            } else if (ch == '|') {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }

        if (escaped) {
            throw new IllegalArgumentException("Incomplete escape");
        }

        fields.add(field.toString());

        if (fields.size() != 7) {
            throw new IllegalArgumentException("Invalid record");
        }

        return fields.toArray(new String[0]);
    }

    static String createRecord(BusPass p) {
        return escape(p.passId) + "|" +
               escape(p.regNo) + "|" +
               escape(p.name) + "|" +
               p.routeNo + "|" +
               escape(p.boardingStop) + "|" +
               p.validityDate + "|" +
               p.amountPaid;
    }

    static BusPass convertRecord(String line) {
        String[] data = splitRecord(line);

        return new BusPass(
            data[0],
            data[1],
            data[2],
            Integer.parseInt(data[3]),
            data[4],
            data[5],
            Double.parseDouble(data[6])
        );
    }

    static ArrayList<BusPass> readTextRecords() {
        ArrayList<BusPass> records = new ArrayList<>();

        try (BufferedReader br =
                 new BufferedReader(new FileReader(textFile))) {

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                try {
                    records.add(convertRecord(line));
                } catch (Exception e) {
                    System.out.println(
                        "Damaged record skipped at line " + lineNumber
                    );
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Text file not found.");
        } catch (IOException e) {
            System.out.println("Error reading text file.");
        }

        return records;
    }

    static void addPass() {
        System.out.print("Enter Pass ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Register Number: ");
        String reg = sc.nextLine();

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Route Number: ");
        int route = Integer.parseInt(sc.nextLine());

        System.out.print("Enter Boarding Stop: ");
        String stop = sc.nextLine();

        System.out.print("Enter Validity Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        System.out.print("Enter Amount Paid: ");
        double amount = Double.parseDouble(sc.nextLine());

        if (id.isEmpty() || reg.isEmpty() || name.isEmpty()
                || stop.isEmpty() || route <= 0 || amount < 0) {
            System.out.println("Invalid bus-pass details.");
            return;
        }

        BusPass newPass = new BusPass(
            id, reg, name, route, stop, date, amount
        );

        ArrayList<BusPass> records = readTextRecords();

        for (BusPass p : records) {
            if (p.passId.equals(id)) {
                System.out.println("Pass ID already exists.");
                return;
            }
        }

        records.add(newPass);

        Path tempFile = Paths.get(textFile + ".tmp");

        try (BufferedWriter bw =
                 Files.newBufferedWriter(tempFile)) {

            for (BusPass p : records) {
                bw.write(createRecord(p));
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing temporary file.");
            return;
        }

        try {
            try {
                Files.move(tempFile, Paths.get(textFile),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempFile, Paths.get(textFile),
                    StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("Bus pass added successfully.");

        } catch (IOException e) {
            System.out.println("Error saving bus-pass record.");
        }
    }

    static void findPass() {
        System.out.print("Enter Pass ID to search: ");
        String id = sc.nextLine();

        ArrayList<BusPass> records = readTextRecords();

        for (BusPass p : records) {
            if (p.passId.equals(id)) {
                System.out.println("\nBus Pass Found:");
                System.out.println(p);
                return;
            }
        }

        System.out.println("Bus pass not found.");
    }

    static void listByRoute() {
        System.out.print("Enter Route Number: ");
        int route = Integer.parseInt(sc.nextLine());

        ArrayList<BusPass> records = readTextRecords();
        boolean found = false;

        for (BusPass p : records) {
            if (p.routeNo == route) {
                System.out.println(p);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No bus passes found for this route.");
        }
    }

    static void writeBinaryFile() {
        ArrayList<BusPass> records = readTextRecords();

        try (DataOutputStream dos =
                 new DataOutputStream(
                     new FileOutputStream(binaryFile))) {

            for (BusPass p : records) {
                dos.writeUTF(p.passId);
                dos.writeUTF(p.regNo);
                dos.writeUTF(p.name);
                dos.writeInt(p.routeNo);
                dos.writeUTF(p.boardingStop);
                dos.writeUTF(p.validityDate);
                dos.writeDouble(p.amountPaid);
            }

            System.out.println("Binary file created successfully.");

        } catch (IOException e) {
            System.out.println("Error writing binary file.");
        }
    }

    static void readBinaryFile() {
        try (DataInputStream dis =
                 new DataInputStream(
                     new FileInputStream(binaryFile))) {

            boolean found = false;

            while (true) {
                try {
                    String id = dis.readUTF();
                    String reg = dis.readUTF();
                    String name = dis.readUTF();
                    int route = dis.readInt();
                    String stop = dis.readUTF();
                    String date = dis.readUTF();
                    double amount = dis.readDouble();

                    BusPass p = new BusPass(
                        id, reg, name, route, stop, date, amount
                    );

                    System.out.println(p);
                    found = true;

                } catch (EOFException e) {
                    break;
                }
            }

            if (!found) {
                System.out.println("No complete records found.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Binary file not found. Create it first.");
        } catch (IOException e) {
            System.out.println("Error reading binary file.");
        }
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n===== BUS PASS RECORD SYSTEM =====");
            System.out.println("1. Add Bus Pass");
            System.out.println("2. Find Bus Pass");
            System.out.println("3. List Bus Passes by Route");
            System.out.println("4. Create Binary File");
            System.out.println("5. Display Binary Records");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        addPass();
                        break;

                    case 2:
                        findPass();
                        break;

                    case 3:
                        listByRoute();
                        break;

                    case 4:
                        writeBinaryFile();
                        break;

                    case 5:
                        readBinaryFile();
                        break;

                    case 6:
                        System.out.println("Program terminated.");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
