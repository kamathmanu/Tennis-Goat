package rankallocator;

// Given a specific week, this class builds a
// list of the top ten (or the size of the queue, if lesser)
// players for that week

import java.util.ArrayList;
import java.util.List;

public class WeeklyRanking {
    private final String week;
    private final List<PlayerRank> ranks;
    public WeeklyRanking(final String week, final List<PlayerRank> playerRanks, final int N) {
        this.week = week;
        // construct a list of the top N (max) players based on weeks at No.1
        this.ranks = new ArrayList<>();
        for (PlayerRank rank : playerRanks) {
            this.ranks.add(rank);
            if (this.ranks.size() == N) {
                break;
            }
        }
    }
    
    public String getWeek() { return week; }
    public List<PlayerRank> getRanks() { return ranks; }
}
