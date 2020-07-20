package ranking;

import rankallocator.PlayerValue;
import rankallocator.RankAllocator;
import junit.framework.TestCase;
import org.junit.Test;
import scraper.Scraper;
import scraper.ScraperException;
import scraper.WeeklyResult;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/** Given a series of WeeklyResults, ensure that the tracking/updating of
 *  player's scores (given be a PlayerValue type) is achieved correctly.
 *  This class will test the classes PlayerScoring and PlayerValue for the following:
 *  1) That the hashmap is correctly initializing scores for new No.1s
 *  2) That the hashmap is updating the player values when a No.1 retains his position
 *  3) That the hashmap's PlayerValues can be used to deterministically break a tie
 *  between players with the same tally, by favouring the player who reached No.1 earlier.
 */

public class TrackScoresTest extends TestCase {
    private RankAllocator allocator;
    private List<WeeklyResult> weeklyResults;

    public void setUp() {
        this.allocator = new RankAllocator();

        weeklyResults = new ArrayList<>();
        weeklyResults.add(new WeeklyResult("2000-11-06", "Pete Sampras"));
        weeklyResults.add(new WeeklyResult("2000-11-13", "Pete Sampras"));
        weeklyResults.add(new WeeklyResult("2000-11-20", "Marat Safin"));
        weeklyResults.add(new WeeklyResult("2000-11-27", "Marat Safin"));
        weeklyResults.add(new WeeklyResult("2000-12-04", "Gustavo Kuerten"));

        for (WeeklyResult weeklyResult : weeklyResults) {
            allocator.updatePlayerScore(weeklyResult);
        }
    }

    public void tearDown() {
    }
    
    @Test
    public void testTotalTallies() {
        assertEquals(allocator.getTrackScores().findPlayerValue("Pete Sampras").getWeeksAtNumberOne(), 2);
        assertEquals(allocator.getTrackScores().findPlayerValue("Marat Safin").getWeeksAtNumberOne(), 2);
        assertEquals(allocator.getTrackScores().findPlayerValue("Gustavo Kuerten").getWeeksAtNumberOne(), 1);
    }

    @Test
    public void testFirstReached() {
        assertEquals(allocator.getTrackScores().findPlayerValue("Pete Sampras").getFirstReached(), "2000-11-06");
        assertFalse(allocator.getTrackScores().findPlayerValue("Marat Safin").getFirstReached().equals("2000-11-27"));
    }

    @Test
    public void testNewNumberOne() {
        assertEquals(allocator.getTrackScores().findPlayerValue("Gustavo Kuerten").getFirstReached(), "2000-12-04");
        assertEquals(allocator.getTrackScores().findPlayerValue("Gustavo Kuerten").getWeeksAtNumberOne(), 1);
    }
}
