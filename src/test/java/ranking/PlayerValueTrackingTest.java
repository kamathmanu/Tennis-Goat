package ranking;

import rankallocator.RankAllocator;
import junit.framework.TestCase;
import org.junit.Test;
import scraper.Scraper;
import scraper.ScraperException;
import scraper.WeeklyResult;

import java.time.Duration;
import java.util.List;

public class PlayerValueTrackingTest extends TestCase {
    private RankAllocator allocator;
    private Scraper scraper;

    public void setUp() {
        scraper =
                new Scraper("https://www.atptour.com/en/rankings/singles?",
                        "&rankRange=0-100", Duration.ofSeconds(90), 3);
    }

    public void tearDown() {
    }

    private void scrapeAll() {
        try {
            final List<String> weeks = scraper.loadWeeks();
            for (String week : weeks) {
                WeeklyResult weeklyResult =  scraper.scrape(week);

            }
        } catch (ScraperException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void firstReached() {
//       TennisGOAT.main();
        //check various first reached for different players
    }

    @Test
    public void totalWeeksAtNo1() {
//        check total weeks at No.1 for various players
    }

    @Test
    public void weeksAtNo1AtGivenPoint() {
//        check updating of players where a person loses and regains number 1
    }
}
