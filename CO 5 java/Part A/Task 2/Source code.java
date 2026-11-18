
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AttendanceLog {

    static String fileName = "attendance.txt";

    static void readOneByteAtATime() {
        long lines = 0;
        long start = System.currentTimeMillis();

        try (FileInputStream fis = new FileInputStream(fileName)) {

            int data;

            while ((data = fis.read()) != -1) {
                if (data == '\n') {
                    lines++;
                }
            }

            System.out.println("\nOne Byte at a Time:");
            System.out.println("Number of Lines: " + lines);

        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        System.out.println("Time Taken: " +
            (System.currentTimeMillis() - start) + " ms");
    }

    static void readUsingBuffer() {
        long lines = 0;
        long start = System.currentTimeMillis();

        try (BufferedInputStream bis =
                 new BufferedInputStream(
                     new FileInputStream(fileName))) {

            int data;

            while ((data = bis.read()) != -1) {
                if (data == '\n') {
                    lines++;
                }
            }

            System.out.println("\nUsing BufferedInputStream:");
            System.out.println("Number of Lines: " + lines);

        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        System.out.println("Time Taken: " +
            (System.currentTimeMillis() - start) + " ms");
    }

    static void readEntireFile() {
        long start = System.currentTimeMillis();

        try {
            List<String> records =
                Files.readAllLines(Paths.get(fileName));

            System.out.println("\nUsing Files.readAllLines():");
            System.out.println("Number of Lines: " + records.size());

        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        System.out.println("Time Taken: " +
            (System.currentTimeMillis() - start) + " ms");
    }

    public static void main(String[] args) {

        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Attendance file not found.");
            return;
        }

        System.out.println("File Size: " +
            file.length() / (1024 * 1024) + " MB");

        readOneByteAtATime();

        readUsingBuffer();

        readEntireFile();
    }
}
