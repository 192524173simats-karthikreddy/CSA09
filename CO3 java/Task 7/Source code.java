
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Q7 {

    static class Summary {
        int success;
        int failure;
        double time;

        Summary(int success, int failure, double time) {
            this.success = success;
            this.failure = failure;
            this.time = time;
        }
    }

    static String processFile(Path file) throws IOException {

        try (BufferedReader br = Files.newBufferedReader(file)) {

            String line = br.readLine();

            if (line == null) {
                throw new IllegalArgumentException("Empty file");
            }

            String[] data = line.split(",", -1);

            if (data.length != 2 || data[0].trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid file format");
            }

            String studentId = data[0].trim();

            int marks = Integer.parseInt(data[1].trim());

            if (marks < 0 || marks > 100) {
                throw new IllegalArgumentException("Invalid marks");
            }

            String result = marks >= 40 ? "PASS" : "FAIL";

            return studentId + " | Marks: " + marks +
                   " | Result: " + result;
        }
    }

    static void createFiles(Path folder, int total)
            throws IOException {

        Files.createDirectories(folder);

        int interval = total == 5 ? 5 : 1000;

        for (int i = 1; i <= total; i++) {

            Path file = folder.resolve(
                String.format("student_%05d.txt", i)
            );

            Files.deleteIfExists(file);

            if (i % interval == 0) {
                continue;
            }

            String data;

            if (i % interval == 1) {
                data = "S" + i + ",ABC";
            }
            else {
                int marks = (i * 37) % 101;
                data = "S" + i + "," + marks;
            }

            Files.writeString(file, data);
        }
    }

    static Summary processFiles(Path folder, int total, int poolSize)
            throws InterruptedException {

        ExecutorService executor =
            Executors.newFixedThreadPool(poolSize);

        Queue<String> successQueue =
            new ConcurrentLinkedQueue<>();

        Queue<String> failureQueue =
            new ConcurrentLinkedQueue<>();

        List<Future<?>> futures = new ArrayList<>();

        long start = System.nanoTime();

        try {

            for (int i = 1; i <= total; i++) {

                Path file = folder.resolve(
                    String.format("student_%05d.txt", i)
                );

                Future<?> future = executor.submit(() -> {

                    try {
                        String result = processFile(file);
                        successQueue.add(result);
                    }
                    catch (IOException | IllegalArgumentException e) {

                        failureQueue.add(
                            file.getFileName() + ": " + e.getMessage()
                        );
                    }
                });

                futures.add(future);
            }

            executor.shutdown();

            for (int i = 0; i < futures.size(); i++) {

                try {
                    futures.get(i).get();
                }
                catch (ExecutionException e) {

                    failureQueue.add(
                        "Task failed for student_" +
                        (i + 1) + ": " + e.getCause()
                    );
                }
            }

        }
        catch (InterruptedException e) {

            executor.shutdownNow();

            Thread.currentThread().interrupt();

            throw e;
        }
        finally {

            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }

        long end = System.nanoTime();

        double time = (end - start) / 1_000_000.0;

        return new Summary(
            successQueue.size(),
            failureQueue.size(),
            time
        );
    }

    public static void main(String[] args)
            throws IOException, InterruptedException {

        Path smallFolder = Path.of("small_data");

        createFiles(smallFolder, 5);

        System.out.println("SMALL DATASET VERIFICATION");

        Summary small = processFiles(smallFolder, 5, 2);

        System.out.println("Successful Files: " + small.success);
        System.out.println("Failed Files: " + small.failure);

        if (small.success == 3 && small.failure == 2) {
            System.out.println("Verification: PASSED");
        }
        else {
            System.out.println("Verification: FAILED");
        }

        Path folder = Path.of("student_data");

        createFiles(folder, 10000);

        int[] poolSizes = {1, 2, 4, 8};

        System.out.println("\n10,000 FILE PROCESSING");

        System.out.println(
            "\nThreads\tSuccess\tFailure\tTime (ms)"
        );

        for (int pool : poolSizes) {

            Summary result =
                processFiles(folder, 10000, pool);

            System.out.printf(
                "%d\t%d\t%d\t%.2f%n",
                pool,
                result.success,
                result.failure,
                result.time
            );
        }

        System.out.println("\nExpected Success: 9980");
        System.out.println("Expected Failure: 20");
    }
}
