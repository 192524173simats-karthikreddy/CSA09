
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Q3 {

    static int openHandles = 0;

    static List<TrackedReader> leakedReaders = new ArrayList<>();

    static class TrackedReader extends BufferedReader {

        boolean closed = false;

        TrackedReader(String file) throws IOException {
            super(new FileReader(file));
            openHandles++;
        }

        public void close() throws IOException {
            if (!closed) {
                try {
                    super.close();
                }
                finally {
                    closed = true;
                    openHandles--;
                }
            }
        }
    }

    static class TrackedWriter extends BufferedWriter {

        boolean closed = false;

        TrackedWriter(String file) throws IOException {
            super(new FileWriter(file));
            openHandles++;
        }

        public void close() throws IOException {
            if (!closed) {
                try {
                    super.close();
                }
                finally {
                    closed = true;
                    openHandles--;
                }
            }
        }
    }

    static void createFiles() throws IOException {

        Files.writeString(Path.of("store1.txt"), "100\n200\n");
        Files.writeString(Path.of("store2.txt"), "300\n400\n");
        Files.writeString(Path.of("store3.txt"), "500\n600\n");
    }

    static void manualReport(String output, boolean fail)
    {
        TrackedWriter writer = null;
        TrackedReader reader = null;

        String currentFile = "";

        int total = 0;

        try {
            writer = new TrackedWriter(output);

            String[] files = {
                "store1.txt",
                "store2.txt",
                "store3.txt"
            };

            for (String file : files) {

                currentFile = file;

                reader = new TrackedReader(file);

                String line = reader.readLine();

                if (fail && file.equals("store2.txt")) {
                    throw new IOException("Reading failed");
                }

                while (line != null) {
                    total += Integer.parseInt(line);
                    line = reader.readLine();
                }

                reader.close();
                reader = null;
            }

            writer.write("Total Sales: " + total);

            System.out.println("Manual report generated.");

        }
        catch (IOException e) {
            System.out.println("Manual Error in " +
                               currentFile + ": " + e.getMessage());
        }
        finally {

            try {
                if (writer != null) {
                    writer.close();
                }
            }
            catch (IOException e) {
                System.out.println("Writer closing error");
            }

            if (reader != null) {
                leakedReaders.add(reader);
            }
        }
    }

    static void tryWithResourcesReport(String output, boolean fail) {

        String currentFile = "";

        int total = 0;

        try (TrackedWriter writer = new TrackedWriter(output)) {

            String[] files = {
                "store1.txt",
                "store2.txt",
                "store3.txt"
            };

            for (String file : files) {

                currentFile = file;

                try (TrackedReader reader = new TrackedReader(file)) {

                    String line = reader.readLine();

                    if (fail && file.equals("store2.txt")) {
                        throw new IOException("Reading failed");
                    }

                    while (line != null) {
                        total += Integer.parseInt(line);
                        line = reader.readLine();
                    }
                }
            }

            writer.write("Total Sales: " + total);

            System.out.println("Try-with-resources report generated.");

        }
        catch (IOException e) {
            System.out.println("Try-with-resources Error in " +
                               currentFile + ": " + e.getMessage());
        }
    }

    static void closeLeakedReaders() {

        for (TrackedReader reader : leakedReaders) {
            try {
                reader.close();
            }
            catch (IOException e) {
                System.out.println("Closing error");
            }
        }

        leakedReaders.clear();
    }

    public static void main(String[] args) throws IOException {

        createFiles();

        System.out.println("TEST 1: Manual Resource Management");

        manualReport("manual_report.txt", false);

        System.out.println("Open Handles: " + openHandles);

        System.out.println("\nTEST 2: Try-With-Resources");

        tryWithResourcesReport("twr_report.txt", false);

        System.out.println("Open Handles: " + openHandles);

        String manual = Files.readString(
            Path.of("manual_report.txt")
        );

        String automatic = Files.readString(
            Path.of("twr_report.txt")
        );

        System.out.println("\nReports Match: " +
                           manual.equals(automatic));

        System.out.println("\nTEST 3: Manual Version With Failure");

        manualReport("manual_failed.txt", true);

        System.out.println("Open Handles After Failure: " +
                           openHandles);

        closeLeakedReaders();

        System.out.println("Open Handles After Cleanup: " +
                           openHandles);

        System.out.println("\nTEST 4: Try-With-Resources With Failure");

        tryWithResourcesReport("twr_failed.txt", true);

        System.out.println("Open Handles After Failure: " +
                           openHandles);
    }
}
