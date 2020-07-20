package rankallocator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import scraper.WeeklyResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// This class combines all the collective actions of ranking the players for each week
public class RankAllocator {
    private static final Logger logger = LogManager.getLogger(RankAllocator.class);
    private final PlayerScoring trackScores;

    public RankAllocator() {
        this.trackScores = new PlayerScoring();
    }

    public void updatePlayerScore(final WeeklyResult weeklyResult) {
        // given a weekly result, update the trackScores hashmap
        this.trackScores.updatePlayerValue(weeklyResult.getWeek(), weeklyResult.getPlayerName());
    }

    private List<PlayerRank> collatePlayerScores() {
        // Streams solution is more elegant. 
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

    public PlayerScoring getTrackScores() { return this.trackScores; }

    public WeeklyRanking rank (final WeeklyResult weeklyResult) {
        Configurator.setLevel(logger.getName(), Level.ERROR);
        updatePlayerScore(weeklyResult);
        logger.debug(this.trackScores.getPlayerScorer()); // quick check to see entries are being inserted. Refer to tests.
        List<PlayerRank> weeklyScores = collatePlayerScores();
        return new WeeklyRanking(weeklyScores);
    }
}
