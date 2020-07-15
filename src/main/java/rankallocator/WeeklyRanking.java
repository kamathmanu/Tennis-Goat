package rankallocator;

// Given a specific week, this class builds a
// priority queue that ranks the players for that week

public class WeeklyRanking {
    // Need to update this to include a priority queue for 10 entries of the tuple <playerName, weeksAtNo1>
    private final String playerName;
    private final int totalWeeksAtNo1;

    public WeeklyRanking(final String player, final int weeks) {
        this.playerName = player;
        this.totalWeeksAtNo1 = weeks;
    }

    public String getPlayerName() { return playerName; }
    public int getTotalWeeksAtNo1() { return totalWeeksAtNo1; }
}
