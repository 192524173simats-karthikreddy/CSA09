import java.util.*;
class Patient {
    final String name;
    final int severity;
    final long arrival;
    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }
    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}
public class Triage {
    private long counter = 0;
    private PriorityQueue<Patient> queue =
        new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                      .thenComparingLong(p -> p.arrival)
        );
    void admit(String name, int severity) {
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException(
                "Severity must be between 1 and 5");
        }
        queue.add(new Patient(name, severity, counter++));
    }
    Patient next() {
        return queue.poll();
    }
    void displayWaitingList() {
        PriorityQueue<Patient> copy =
                new PriorityQueue<>(queue);
        System.out.println("Waiting List:");
        while (!copy.isEmpty()) {
            System.out.println(copy.poll());
        }
    }
    static void testPatients() {
        System.out.println("EIGHT PATIENT TEST");
        Triage er = new Triage();
        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);
        er.admit("Priya", 2);
        er.admit("Rahul", 4);
        er.displayWaitingList();
        System.out.println("\nTreatment Order:");
        Patient p;
        while ((p = er.next()) != null) {
            System.out.print(p + " ");
        }
        System.out.println();
    }
    static void originalTest() {
        System.out.println("\nORIGINAL ASSIGNMENT TEST");
        Triage er = new Triage();
        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);
        StringBuilder order = new StringBuilder();
        Patient p;
        while ((p = er.next()) != null) {
            order.append(p).append(" ");
        }
        System.out.println(order.toString().trim());
    }
    static void performanceTest(int size) {
        System.out.println("\nQueue Size: " + size);
        Patient[] patients = new Patient[size];
        for (int i = 0; i < size; i++) {
            patients[i] = new Patient(
                "Patient" + i,
                (i % 5) + 1,
                i
            );
        }
        PriorityQueue<Patient> q =
            new PriorityQueue<>(
                Comparator.comparingInt(
                    (Patient p) -> p.severity)
                    .thenComparingLong(p -> p.arrival)
            );
        long start = System.nanoTime();
        for (Patient p : patients) {
            q.add(p);
        }
        long admissionTime =
                System.nanoTime() - start;
        start = System.nanoTime();
        while (!q.isEmpty()) {
            q.poll();
        }
        long servingTime =
                System.nanoTime() - start;
        System.out.printf(
            "Admission time: %.3f ms%n",
            admissionTime / 1_000_000.0
        );
        System.out.printf(
            "Serving time: %.3f ms%n",
            servingTime / 1_000_000.0
        );
    }
    public static void main(String[] args) {
        originalTest();
        testPatients();
        System.out.println("\nPERFORMANCE EXPERIMENT");
        int[] sizes = {1000, 10000, 50000};
        for (int size : sizes) {
            performanceTest(size);
        }
    }
}
