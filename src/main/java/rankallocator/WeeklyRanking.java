package rankallocator;

// Given a specific week, this class builds a
// priority queue that ranks the players for that week

import java.util.List;
import java.util.PriorityQueue;

public class WeeklyRanking {
    private PriorityQueue<PlayerRank> ranks;
    public WeeklyRanking(final List<PlayerRank> playerRanks) {
        this.ranks = new PriorityQueue<>(playerRanks);
    }

    public PriorityQueue<PlayerRank> getRanks() {
        return ranks;
    }

    public void setRanks(PriorityQueue<PlayerRank> ranks) {
        this.ranks = ranks;
    }
    // functions to write to file?
}
