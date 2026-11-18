
import java.io.*;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class PassStore {

    private final Path file;

    PassStore(Path file) {
        this.file = file;
    }

    public void add(String passId, String regNo, String name,
                    int route, double amount) throws IOException {

        try (BufferedWriter w = Files.newBufferedWriter(
                file,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            w.write(String.join(",",
                    passId,
                    regNo,
                    name,
                    String.valueOf(route),
                    String.valueOf(amount)));

            w.newLine();
        }
    }

    public String[] find(String passId) throws IOException {

        if (!Files.exists(file)) {
            return null;
        }

        try (BufferedReader br = Files.newBufferedReader(file)) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length != 5) {
                    continue;
                }

                try {
                    Integer.parseInt(data[3]);
                    Double.parseDouble(data[4]);
                } catch (NumberFormatException e) {
                    continue;
                }

                if (data[0].equals(passId)) {
                    return data;
                }
            }
        }

        return null;
    }

    public void listByRoute(int route) throws IOException {

        if (!Files.exists(file)) {
            System.out.println("File not found.");
            return;
        }

        boolean found = false;

        try (BufferedReader br = Files.newBufferedReader(file)) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length != 5) {
                    continue;
                }

                try {
                    int recordRoute = Integer.parseInt(data[3]);
                    Double.parseDouble(data[4]);

                    if (recordRoute == route) {
                        System.out.println(Arrays.toString(data));
                        found = true;
                    }

                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        if (!found) {
            System.out.println("No passes found for this route.");
        }
    }

    static long countUnbuffered(Path file) throws IOException {

        long lines = 0;

        try (FileInputStream fis =
                 new FileInputStream(file.toFile())) {

            int data;

            while ((data = fis.read()) != -1) {
                if (data == '\n') {
                    lines++;
                }
            }
        }

        return lines;
    }

    static long countBuffered(Path file) throws IOException {

        long lines = 0;

        try (BufferedInputStream bis =
                 new BufferedInputStream(
                     new FileInputStream(file.toFile()))) {

            int data;

            while ((data = bis.read()) != -1) {
                if (data == '\n') {
                    lines++;
                }
            }
        }

        return lines;
    }

    static void createTestFile(Path file, int sizeMB)
            throws IOException {

        String record = "101,Karthik,Present,AI-DS\n";

        try (BufferedWriter bw = Files.newBufferedWriter(file)) {

            while (Files.size(file) < sizeMB * 1024L * 1024L) {
                bw.write(record);
            }
        }
    }

    static void measureBuffering() throws IOException {

        System.out.println("\n===== BUFFERING PERFORMANCE =====");

        int[] sizes = {1, 10, 50};

        for (int size : sizes) {

            Path testFile = Files.createTempFile(
                "attendance_" + size, ".txt"
            );

            createTestFile(testFile, size);

            long start1 = System.nanoTime();
            long lines1 = countUnbuffered(testFile);
            long time1 = System.nanoTime() - start1;

            long start2 = System.nanoTime();
            long lines2 = countBuffered(testFile);
            long time2 = System.nanoTime() - start2;

            System.out.println("\nFile Size: " +
                Files.size(testFile) / (1024 * 1024) + " MB");

            System.out.println("Unbuffered Lines: " + lines1);
            System.out.println("Unbuffered Time: " +
                time1 / 1_000_000.0 + " ms");

            System.out.println("Buffered Lines: " + lines2);
            System.out.println("Buffered Time: " +
                time2 / 1_000_000.0 + " ms");

            Files.deleteIfExists(testFile);
        }
    }

    static void measureSearchGrowth() throws IOException {

        System.out.println("\n===== SEARCH PERFORMANCE =====");

        int[] sizes = {1000, 10000, 50000, 100000};

        for (int size : sizes) {

            Path testFile = Files.createTempFile(
                "pass_growth", ".csv"
            );

            try (BufferedWriter bw = Files.newBufferedWriter(testFile)) {

                for (int i = 1; i <= size; i++) {
                    bw.write("P" + i + ",REG" + i +
                        ",Student" + i + ",10,4500.0");
                    bw.newLine();
                }
            }

            PassStore store = new PassStore(testFile);

            String target = "P" + size;

            long start = System.nanoTime();

            String[] result = null;

            for (int i = 0; i < 10; i++) {
                result = store.find(target);
            }

            long end = System.nanoTime();

            System.out.println("\nRecords: " + size);
            System.out.println("Search Result: " +
                Arrays.toString(result));

            System.out.println("Average Search Time: " +
                (end - start) / 10.0 / 1_000_000.0 + " ms");

            Files.deleteIfExists(testFile);
        }
    }

    public static void main(String[] args) throws IOException {

        Path f = Files.createTempFile("passes", ".csv");

        PassStore store = new PassStore(f);

        System.out.println("===== BUS PASS FILE STORE =====");

        store.add("P1001", "192311001", "Arun", 12, 4500);

        Files.writeString(
            f,
            "P1002,DAMAGED LINE\n",
            StandardOpenOption.APPEND
        );

        store.add("P1003", "192311003", "Divya", 7, 3800);

        System.out.println("\nNormal Search:");
        System.out.println(Arrays.toString(store.find("P1003")));

        System.out.println("\nDamaged Record Search:");
        System.out.println(Arrays.toString(store.find("P1002")));

        System.out.println("\nNon-existing Pass Search:");
        System.out.println(Arrays.toString(store.find("P9999")));

        System.out.println("\nList by Route 7:");
        store.listByRoute(7);

        System.out.println("\nList by Route 12:");
        store.listByRoute(12);

        Files.deleteIfExists(f);

        measureBuffering();

        measureSearchGrowth();

        System.out.println("\nAll tests completed.");
    }
}
