package rankallocator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import scraper.WeeklyResult;

import java.util.*;
import java.util.stream.Collectors;

// This class combines all the collective actions of parsing a weekly result
// and generating a weekly ranking of the No1 players for a given week.
public class RankAllocator {
    private static final Logger logger = LogManager.getLogger(RankAllocator.class);
    private final PlayerScoring trackScores;
    private final int top_N; // display the top-N players for Weekly rankings.

    public RankAllocator(final int N) {
        this.top_N = N;
        this.trackScores = new PlayerScoring();
    }
    public PlayerScoring getTrackScores() { return this.trackScores; }

    private void updatePlayerScore(final WeeklyResult weeklyResult) {
        // given a weekly result, update the trackScores hashmap
        this.trackScores.updatePlayerValue(weeklyResult.getWeek(), weeklyResult.getPlayerName());
    }

    private List<PlayerRank> collatePlayerScores() {
        // for each entry in trackScores, construct a new PlayerRank
        // then return a list sorted in descending order by PlayerValue
        List<PlayerRank> ranks =
                (trackScores.getPlayerScorer().entrySet().stream()
                        .map(entry -> new PlayerRank(entry.getKey(), entry.getValue())))
                        .sorted(Comparator.comparingInt((PlayerRank player) ->
                                player.getCurrentValue().getWeeksAtNumberOne()).reversed() //get descending order by weeks @ No.1
                                .thenComparing(player -> player.getCurrentValue().getFirstReached())) //break ties based on who reached earlier
                        .collect(Collectors.toList());
        return ranks;
    }

    public WeeklyRanking rank (final WeeklyResult weeklyResult) {
        Configurator.setLevel(logger.getName(), Level.ERROR);
        updatePlayerScore(weeklyResult);
        logger.debug(this.trackScores.getPlayerScorer());
        List<PlayerRank> weeklyScores = collatePlayerScores();
        return new WeeklyRanking(weeklyResult.getWeek(), weeklyScores, this.top_N);
    }
}
