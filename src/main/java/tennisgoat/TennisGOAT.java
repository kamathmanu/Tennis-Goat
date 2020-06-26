package tennisgoat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import scraper.Scraper;
import scraper.ScraperException;
import scraper.WeeklyResult;

import java.util.Collections;
import java.util.List;

public class TennisGOAT {
    private static final Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {

        Configurator.setRootLevel(Level.DEBUG);

        // Scrape data off the ATP website
        List<WeeklyResult> weeklyResults = Collections.emptyList();
        try {
            weeklyResults = Scraper.main();
        } catch (ScraperException e) {
            System.out.println(e.toString());
        }

        // Check that this works
        for (final WeeklyResult weeklyResult : weeklyResults) {
            logger.debug("Week: " + weeklyResult.getWeek() + " No.1: " + weeklyResult.getPlayerName());
        }

        // Pass the weekly result to the rank allocator

        

    }
}
