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
    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}
class Leaderboard<T> {
    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;
    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }
    void add(T item) {
        items.add(item);
    }
    void addAll(List<? extends T> values) {
        items.addAll(values);
    }
    List<T> ranking() {
        List<T> result = new ArrayList<>(items);
        Collections.sort(result, order);
        return result;
    }
}
public class LeaderboardDemo {
    public static void main(String[] args) {
        Comparator<Team> byRank =
            Comparator.comparingInt(
                (Team t) -> t.points).reversed()
            .thenComparing(
                Comparator.comparingInt(
                    (Team t) -> t.goalDiff).reversed())
            .thenComparing(t -> t.name);
        Leaderboard<Team> board =
                new Leaderboard<>(byRank);
        System.out.println("TEST 1 - NORMAL RANKING");
        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));
        System.out.println(board.ranking());
        System.out.println("\nTEST 2 - MULTIPLE TIES");
        Leaderboard<Team> board2 =
                new Leaderboard<>(byRank);
        List<Team> teams = Arrays.asList(
            new Team("Tigers", 12, 5),
            new Team("Lions", 12, 5),
            new Team("Wolves", 12, 5),
            new Team("Hawks", 12, 3),
            new Team("Falcons", 10, 8),
            new Team("Bears", 10, 8)
        );
        board2.addAll(teams);
        System.out.println(board2.ranking());
        System.out.println("\nTEST 3 - FAULTY COMPARATOR");
        Comparator<Team> faulty =
            Comparator.comparingInt(
                (Team t) -> t.points).reversed()
            .thenComparing(
                Comparator.comparingInt(
                    (Team t) -> t.goalDiff).reversed()
            );
        Leaderboard<Team> board3 =
                new Leaderboard<>(faulty);
        board3.add(new Team("Tigers", 12, 5));
        board3.add(new Team("Lions", 12, 5));
        board3.add(new Team("Wolves", 12, 5));
        System.out.println("Faulty ranking: "
                + board3.ranking());
        System.out.println("\nOriginal list size: "
                + teams.size());
    }
}
