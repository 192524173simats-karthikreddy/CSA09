public class Task_1 {
    static volatile long result;
    static long usingString(String[] fields, int tickets) {
        long checksum = 0;
        for (int t = 0; t < tickets; t++) {
            String ticket = "";
            for (String field : fields) {
                ticket = ticket + field + " | ";
            }
            checksum += ticket.hashCode();
        }
        return checksum;
    }
    static long usingStringBuilder(String[] fields, int tickets) {
        long checksum = 0;
        for (int t = 0; t < tickets; t++) {
            StringBuilder ticket = new StringBuilder();
            for (String field : fields) {
                ticket.append(field).append(" | ");
            }
            checksum += ticket.toString().hashCode();
        }
        return checksum;
    }
    static long usingStringBuffer(String[] fields, int tickets) {
        long checksum = 0;
        for (int t = 0; t < tickets; t++) {
            StringBuffer ticket = new StringBuffer();
            for (String field : fields) {
                ticket.append(field).append(" | ");
            }
            checksum += ticket.toString().hashCode();
        }
        return checksum;
    }
    static long memoryUsed() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }
    static void measure(
            String name,
            String[] fields,
            int tickets,
            int repetitions,
            int estimatedStrings) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            if (name.equals("String"))
                result = usingString(fields, tickets);
            else if (name.equals("StringBuilder"))
                result = usingStringBuilder(fields, tickets);
            else
                result = usingStringBuffer(fields, tickets);
        }
        System.gc();
        Thread.sleep(100);
        long memoryBefore = memoryUsed();
        long totalTime = 0;
        for (int i = 0; i < repetitions; i++) {
            long start = System.nanoTime();
            if (name.equals("String"))
                result = usingString(fields, tickets);
            else if (name.equals("StringBuilder"))
                result = usingStringBuilder(fields, tickets);
            else
                result = usingStringBuffer(fields, tickets);
            long end = System.nanoTime();
            totalTime += (end - start);
        }
        long memoryAfter = memoryUsed();
        double averageTime =
                (double) totalTime / repetitions;
        System.out.println("\nMethod: " + name);
        System.out.printf(
                "Average Time: %.2f ms%n",
                averageTime / 1_000_000.0);
        System.out.println(
                "Estimated String Objects per run: "
                + estimatedStrings);
        System.out.println(
                "Memory Before: "
                + memoryBefore / 1024 + " KB");
        System.out.println(
                "Memory After: "
                + memoryAfter / 1024 + " KB");
        System.out.println(
                "Approximate Memory Difference: "
                + (memoryAfter - memoryBefore) / 1024
                + " KB");
    }
    static void runWorkload(
            String workload,
            int tickets,
            int numberOfFields) throws InterruptedException {

        System.out.println("\n================================");
        System.out.println(workload);
        System.out.println("================================");

        String[] fields = new String[numberOfFields];

        for (int i = 0; i < numberOfFields; i++) {

            fields[i] = "Field" + (i + 1)
                    + ": Railway Ticket Details";
        }
        int repetitions = 5;
        int estimatedStrings = tickets * numberOfFields;
        measure("String", fields, tickets,
                repetitions, estimatedStrings);
        measure("StringBuilder", fields, tickets,
                repetitions, tickets);
        measure("StringBuffer", fields, tickets,
                repetitions, tickets);
    }
           public static void main(String[] args)
            throws InterruptedException {
        System.out.println(
                "STRING vs STRINGBUILDER vs STRINGBUFFER");

        // Workload 1
        runWorkload("WORKLOAD 1 - SMALL TICKET",
                1, 6);

        // Workload 2
        runWorkload("WORKLOAD 2 - MEDIUM TICKET",
                1000, 20);

        // Workload 3
        runWorkload("WORKLOAD 3 - LARGE TICKET",
                10000, 50);
    }
}
