package rankallocator;

import scraper.WeeklyResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// This class combines all the collective actions of ranking the players for each week
public class RankAllocator {
    private final PlayerScoring trackScores;

    public RankAllocator() {
        this.trackScores = new PlayerScoring();
    }

    private void updatePlayerScore(final WeeklyResult weeklyResult) {
        // given a weekly result, update the trackScores hashmap
        // if the player doesn't exist in the hashmap, construct a new PlayerValue
        // otherwise, update (increment by one) his week tally
    }

    private List<PlayerRank> collatePlayerScores() {
        // combine the Key and Value of each entry of the trackScores hashmap.
        List<PlayerRank> ranks = new ArrayList<>();
        for (final Map.Entry<String, PlayerValue> playerValueEntry : trackScores.getPlayerScorer().entrySet()) {
            final String name = playerValueEntry.getKey();
            final PlayerValue score = playerValueEntry.getValue();
            final PlayerRank entryRank = new PlayerRank(name, score);
            ranks.add(entryRank);
        }
        return ranks;
    }

    public WeeklyRanking rank (final WeeklyResult weeklyResult) {
        updatePlayerScore(weeklyResult);
        List<PlayerRank> weeklyScores = collatePlayerScores();
        return new WeeklyRanking(weeklyScores);
    }
}
