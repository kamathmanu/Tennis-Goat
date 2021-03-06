package tennisgoat;

import legacyvisualizer.LegacyVisualizer;
import rankallocator.RankAllocator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import rankallocator.WeeklyRanking;
import scraper.Scraper;
import scraper.ScraperException;
import scraper.WeeklyResult;

import java.time.Duration;
import java.util.List;

// Main class to manage the visualization of player's legacy rankings
public class TennisGOAT {
    private static final Logger logger = LogManager.getRootLogger();
    private static final RankAllocator allocator = new RankAllocator(5);
    private static final String dirName = System.getProperty("user.dir")+"/legacy";
    private static final LegacyVisualizer visualizer = new LegacyVisualizer(dirName);

    private static void generateRankingAllocation(WeeklyResult weeklyResult) {
        // pass the scraped result to the next stage of the visualization logic.
        logger.debug("Week: " + weeklyResult.getWeek() + " No.1: " + weeklyResult.getPlayerName());
        WeeklyRanking weeklyRanking = allocator.rank(weeklyResult);
        visualizer.visualize(weeklyRanking);
    }

    private static void consumeWeeklyRanking(WeeklyRanking weeklyRanking) {
        //do something interesting with weekly rankings
        //example of something decidedly not interesting
        logger.info("Week of " + weeklyRanking.getWeek() + ": " +
                weeklyRanking.getRanks().get(0).getPlayerName() + " leading the race with " +
                weeklyRanking.getRanks().get(0).getCurrentValue().getWeeksAtNumberOne() + " weeks at No.1!");
    }

    public static void main(String[] args) {

        Configurator.setRootLevel(Level.INFO);

        final Scraper scraper =
                new Scraper("https://www.atptour.com/en/rankings/singles?",
                        "&rankRange=0-100", Duration.ofSeconds(90), 3);

        // The flow is as follows: scrape the latest weekly results (starting from 1973),
        // then pass it to the ranking logic (IPR). Rinse and repeat
        try {
            final List<String> weeks = scraper.loadWeeks();
            for (String week : weeks) {
                logger.debug(week);
                WeeklyResult weeklyResult =  scraper.scrape(week);
                generateRankingAllocation(weeklyResult);
            }
        } catch (ScraperException e) {
            logger.error(e.toString());
        }
    }
}
