
import java.util.*;

public class Task_2 {

    static final int REPEATS = 5;

    static List<Integer> createList(boolean linked, int size) {

        List<Integer> list = linked
                ? new LinkedList<>()
                : new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        return list;
    }

    static double measure(Runnable task) {

        long total = 0;

        for (int i = 0; i < REPEATS; i++) {

            long start = System.nanoTime();

            task.run();

            long end = System.nanoTime();

            total += end - start;
        }

        return total / (double) REPEATS / 1_000_000.0;
    }

    static void testList(String name, boolean linked, int size) {

        System.out.println("\n" + name + " (" + size + " songs)");

        // Add at end
        double addEnd = measure(() -> {
            List<Integer> list = createList(linked, size);

            for (int i = 0; i < 1000; i++) {
                list.add(i);
            }
        });

        // Insert at front
        double addFront = measure(() -> {
            List<Integer> list = createList(linked, size);

            for (int i = 0; i < 100; i++) {
                list.add(0, i);
            }
        });

        // Remove from front
        double removeFront = measure(() -> {
            List<Integer> list = createList(linked, size);

            for (int i = 0; i < 100; i++) {
                list.remove(0);
            }
        });

        // Insert in middle
        double addMiddle = measure(() -> {
            List<Integer> list = createList(linked, size);

            for (int i = 0; i < 100; i++) {
                list.add(list.size() / 2, i);
            }
        });

        // Remove from middle
        double removeMiddle = measure(() -> {
            List<Integer> list = createList(linked, size);

            for (int i = 0; i < 100; i++) {
                list.remove(list.size() / 2);
            }
        });

        // Access kth element
        double access = measure(() -> {
            List<Integer> list = createList(linked, size);

            long sum = 0;

            for (int i = 0; i < 100; i++) {
                sum += list.get(size / 2);
            }

            if (sum == -1) System.out.println(sum);
        });

        // Iterate through entire list
        double iteration = measure(() -> {
            List<Integer> list = createList(linked, size);

            long sum = 0;

            for (int value : list) {
                sum += value;
            }

            if (sum == -1) System.out.println(sum);
        });

        System.out.printf("Add at end: %.3f ms%n", addEnd);
        System.out.printf("Insert at front: %.3f ms%n", addFront);
        System.out.printf("Remove at front: %.3f ms%n", removeFront);
        System.out.printf("Insert in middle: %.3f ms%n", addMiddle);
        System.out.printf("Remove in middle: %.3f ms%n", removeMiddle);
        System.out.printf("Access kth element: %.3f ms%n", access);
        System.out.printf("Full iteration: %.3f ms%n", iteration);
    }

    // Test ArrayDeque for Up Next queue
    static void testDeque(int size) {

        double time = measure(() -> {

            ArrayDeque<Integer> deque = new ArrayDeque<>();

            for (int i = 0; i < size; i++) {
                deque.addLast(i);
            }

            for (int i = 0; i < 1000; i++) {
                deque.addFirst(i);
                deque.removeLast();
            }

            for (int i = 0; i < 1000; i++) {
                deque.removeFirst();
                deque.addLast(i);
            }
        });

        System.out.printf(
                "ArrayDeque end operations: %.3f ms%n", time);
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        for (int size : sizes) {

            System.out.println("\n================================");
            System.out.println("PLAYLIST SIZE: " + size);
            System.out.println("================================");

            testList("ArrayList", false, size);

            testList("LinkedList", true, size);

            testDeque(size);
        }
    }
}
