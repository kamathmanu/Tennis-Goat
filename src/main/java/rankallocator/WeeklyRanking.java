package rankallocator;

// Given a specific week, this class builds a
// list of the top ten (or the size of the queue, if lesser)
// players for that week

import java.util.ArrayList;
import java.util.List;

public class WeeklyRanking {
    private final List<PlayerRank> ranks;
    public WeeklyRanking(final List<PlayerRank> playerRanks) {
        // construct a list of the top 10 (max) players based on weeks at No.1
        this.ranks = new ArrayList<>();
        for (PlayerRank rank : playerRanks) {
            this.ranks.add(rank);
            if (this.ranks.size() == 10) {
                break;
            }
        }
    }
    public List<PlayerRank> getRanks() { return ranks; }
}
