import java.util.*;
class Team {
    final String name;
    final int points;
    final int goalDiff;
    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }
    public String toString() {
        return name + " (" + points + ", " + goalDiff + ")";
    }
}
class OriginalLeaderboard<T> {
    private List<T> teams = new ArrayList<>();
    private Comparator<T> comparator;
    int sortCount = 0;
    OriginalLeaderboard(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    void add(T team) {
        teams.add(team);
    }
    List<T> ranking() {
        List<T> result = new ArrayList<>(teams);
        Collections.sort(result, comparator);
        sortCount++;
        return result;
    }
}
class ImprovedLeaderboard<T> {
    private List<T> teams = new ArrayList<>();
    private Comparator<T> comparator;
    private List<T> cachedRanking;
    private boolean isDirty = true;
    int sortCount = 0;
    ImprovedLeaderboard(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    void add(T team) {
        teams.add(team);
        isDirty = true;
    }
    List<T> ranking() {
        if (isDirty) {
            List<T> result = new ArrayList<>(teams);
            Collections.sort(result, comparator);
            cachedRanking = Collections.unmodifiableList(result);
            isDirty = false;
            sortCount++;
        }
        return cachedRanking;
    }
}
public class Q7 {
    public static void main(String[] args) {
        Comparator<Team> rule =
            Comparator.comparingInt((Team t) -> t.points).reversed()
            .thenComparing(
                Comparator.comparingInt(
                    (Team t) -> t.goalDiff
                ).reversed()
            )
            .thenComparing(t -> t.name);
        int[] sizes = {1000, 10000, 50000};
        int requests = 50;
        System.out.println("Teams | Original(ms) | Improved(ms)");
        System.out.println("--------------------------------------");
        for (int size : sizes) {
            OriginalLeaderboard<Team> original =
                new OriginalLeaderboard<>(rule);
            ImprovedLeaderboard<Team> improved =
                new ImprovedLeaderboard<>(rule);
            for (int i = 0; i < size; i++) {
                Team t = new Team(
                    "Team" + i,
                    i % 100,
                    i % 20
                );
                original.add(t);
                improved.add(t);
            }
            long start1 = System.nanoTime();
            for (int i = 0; i < requests; i++) {
                original.ranking();
            }
            long end1 = System.nanoTime();
            long start2 = System.nanoTime();
            for (int i = 0; i < requests; i++) {
                improved.ranking();
            }
            long end2 = System.nanoTime();
            double originalTime =
                (end1 - start1) / 1_000_000.0;
            double improvedTime =
                (end2 - start2) / 1_000_000.0;
            System.out.printf(
                "%d | %.3f | %.3f%n",
                size, originalTime, improvedTime
            );
            System.out.println(
                "Original sorts: " + original.sortCount
            );
            System.out.println(
                "Improved sorts: " + improved.sortCount
            );
        }
        ImprovedLeaderboard<Team> test =
            new ImprovedLeaderboard<>(rule);
        test.add(new Team("Eagles", 9, 6));
        test.add(new Team("Bisons", 9, 4));
        System.out.println("\nBefore update:");
        System.out.println(test.ranking());
        test.add(new Team("Panthers", 9, 5));
        System.out.println("\nAfter update:");
        System.out.println(test.ranking());
        System.out.println(
            "Total sorts after update: " + test.sortCount
        );
    }
}
