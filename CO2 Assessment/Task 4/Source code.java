import java.util.*;
public class Task_4 {
    static final int LOOKUPS = 100000;
    static volatile int result;
    static class Medicine {
        String code;
        String name;
        int stock;
        int sequence;
        Medicine(String code, String name,
                 int stock, int sequence) {
            this.code = code;
            this.name = name;
            this.stock = stock;
            this.sequence = sequence;
        }
        @Override
        public String toString() {
            return code + " " + name + " " + stock;
        }
    }
    static Map<String, Medicine> createMap(
            String type, int size) {
        Map<String, Medicine> map;
        if (type.equals("HashMap"))
            map = new HashMap<>();
        else if (type.equals("TreeMap"))
            map = new TreeMap<>();
        else
            map = new LinkedHashMap<>();
        for (int i = size - 1; i >= 0; i--) {
            String code = String.format("M%06d", i);
            Medicine medicine = new Medicine(
                    code, "Medicine" + i, 100 + i, size - 1 - i);
            map.put(code, medicine);
        }
        return map;
    }
    static double lookupTime(
            Map<String, Medicine> map,
            String[] queries) {
        long start = System.nanoTime();
        long sum = 0;
        for (String code : queries) {
            Medicine medicine = map.get(code);
            if (medicine != null)
                sum += medicine.stock;
        }
        long end = System.nanoTime();
        result = (int) sum;
        return (end - start) / 1_000_000.0;
    }
    static String codeReport(
            Map<String, Medicine> map,
            boolean alreadySorted) {
        List<Medicine> list =
                new ArrayList<>(map.values());
        if (!alreadySorted) {
            list.sort(
                Comparator.comparing(m -> m.code)
            );
        }
        StringBuilder report = new StringBuilder();
        for (Medicine m : list) {
            report.append(m.code)
                  .append(" : ")
                  .append(m.stock)
                  .append("\n");
        }
        return report.toString();
    }
    static String insertionReport(
            Map<String, Medicine> map,
            boolean alreadyOrdered) {
        List<Medicine> list =
                new ArrayList<>(map.values());
        if (!alreadyOrdered) {
            list.sort(
                Comparator.comparingInt(m -> m.sequence)
            );
        }
        StringBuilder report = new StringBuilder();
        for (Medicine m : list) {
            report.append(m.code)
                  .append(" : ")
                  .append(m.stock)
                  .append("\n");
        }
        return report.toString();
    }
    static boolean checkCodeOrder(String report) {
        String[] lines = report.split("\n");
        for (int i = 1; i < lines.length; i++) {
            String previous = lines[i - 1].split(" : ")[0];
            String current = lines[i].split(" : ")[0];
            if (previous.compareTo(current) > 0)
                return false;
        }
        return true;
    }
    static boolean checkInsertionOrder(
            String report, int size) {
        String[] lines = report.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String expected =
                    String.format("M%06d", size - 1 - i);
            String actual = lines[i].split(" : ")[0];
            if (!expected.equals(actual))
                return false;
        }
        return true;
    }
    static void test(int size) {
        System.out.println("\n================================");
        System.out.println("MEDICINES: " + size);
        System.out.println("================================");
        Map<String, Medicine> hash =
                createMap("HashMap", size);
        Map<String, Medicine> tree =
                createMap("TreeMap", size);
        Map<String, Medicine> linked =
                createMap("LinkedHashMap", size);
        String[] queries = new String[LOOKUPS];
        Random random = new Random(42);
        for (int i = 0; i < LOOKUPS; i++) {
            int index = random.nextInt(size);
            queries[i] = String.format("M%06d", index);
        }
        System.out.printf(
                "\nHashMap Lookup: %.3f ms%n",
                lookupTime(hash, queries));
        System.out.printf(
                "TreeMap Lookup: %.3f ms%n",
                lookupTime(tree, queries));
        System.out.printf(
                "LinkedHashMap Lookup: %.3f ms%n",
                lookupTime(linked, queries));
        long start = System.nanoTime();
        String hashCode = codeReport(hash, false);
        long hashCodeTime = System.nanoTime() - start;
        start = System.nanoTime();
        String treeCode = codeReport(tree, true);
        long treeCodeTime = System.nanoTime() - start;
        start = System.nanoTime();
        String linkedCode = codeReport(linked, false);
        long linkedCodeTime = System.nanoTime() - start;
        System.out.println("\nCODE ORDER REPORT");
        System.out.printf("HashMap: %.3f ms%n",
                hashCodeTime / 1_000_000.0);
        System.out.printf("TreeMap: %.3f ms%n",
                treeCodeTime / 1_000_000.0);
        System.out.printf("LinkedHashMap: %.3f ms%n",
                linkedCodeTime / 1_000_000.0);
        System.out.println("HashMap order correct: "
                + checkCodeOrder(hashCode));
        System.out.println("TreeMap order correct: "
                + checkCodeOrder(treeCode));
        System.out.println("LinkedHashMap order correct: "
                + checkCodeOrder(linkedCode));
        start = System.nanoTime();
        String hashInsertion =
                insertionReport(hash, false);
        long hashInsertionTime =
                System.nanoTime() - start;
        start = System.nanoTime();
        String treeInsertion =
                insertionReport(tree, false);
        long treeInsertionTime =
                System.nanoTime() - start;
        start = System.nanoTime();
        String linkedInsertion =
                insertionReport(linked, true);
        long linkedInsertionTime =
                System.nanoTime() - start;
        System.out.println("\nINSERTION ORDER REPORT");
        System.out.printf("HashMap: %.3f ms%n",
                hashInsertionTime / 1_000_000.0);
        System.out.printf("TreeMap: %.3f ms%n",
                treeInsertionTime / 1_000_000.0);
        System.out.printf("LinkedHashMap: %.3f ms%n",
                linkedInsertionTime / 1_000_000.0);
        System.out.println("HashMap order correct: "
                + checkInsertionOrder(hashInsertion, size));
        System.out.println("TreeMap order correct: "
                + checkInsertionOrder(treeInsertion, size));
        System.out.println("LinkedHashMap order correct: "
                + checkInsertionOrder(linkedInsertion, size));
        result = hashCode.length()
                + treeCode.length()
                + linkedCode.length();
    }
    public static void main(String[] args) {
        System.out.println(
                "PHARMACY INVENTORY MAP COMPARISON");
        int[] sizes = {1000, 10000, 50000};
        for (int size : sizes) {
            test(size);
        }
    }
}
