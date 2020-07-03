package tennisgoat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import scraper.Scraper;
import scraper.ScraperException;
import scraper.WeeklyResult;

import java.time.Duration;
import java.util.List;

// Main class to manage the visualization of player's legacy rankings
public class TennisGOAT {
    private static final Logger logger = LogManager.getRootLogger();

    private static void utilizeScrapedResult(WeeklyResult weeklyResult) {
        // pass the scraped result to the next stage of the visualization logic.
        logger.info("Week: " + weeklyResult.getWeek() + " No.1: " + weeklyResult.getPlayerName());
    }

    public static void main(String[] args) {

        Configurator.setRootLevel(Level.DEBUG);

        final Scraper scraper =
                new Scraper("https://www.atptour.com/en/rankings/singles?",
                        "&rankRange=0-100", Duration.ofSeconds(90), 3);

        // The flow is as follows: scrape the latest weekly results (starting from 1973),
        // then pass it to the ranking logic (IPR). Rinse and repeat
        try {
            final List<String> weeks = scraper.loadWeeks();
            for (String week : weeks) {
                scraper.scrapeWeekly(week).ifPresent(result -> {
                    utilizeScrapedResult(result);
                });
            }
        } catch (ScraperException e) {
            System.out.println(e.toString());
        }
    }
}
