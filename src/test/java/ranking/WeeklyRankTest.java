package ranking;

// Test class to see that players get properly ranked.
// Note that since we are counting tallies on
// a weekly basis, it is impossible for players to "jump"
// over the other. i.e. if Sam was ranked 3rd by weeks at No.1,
// he has to become 2nd and then become 1st.
// This means that we only need to test for the following cases:

// 1. Players are being ranked *in descending order* by total weeks at No.1
// 2. Ties are broken by using the week first reached
// 3. Weekly Ranking top-N lists are generated correctly when a) len(list) < N and b) >= N.
// 4. When len(list) >= N, changes in the Nth ranked player are reflected correctly.

import junit.framework.TestCase;
import org.junit.Test;
import rankallocator.RankAllocator;
import rankallocator.WeeklyRanking;
import scraper.WeeklyResult;

import java.util.ArrayList;
import java.util.List;

public class WeeklyRankTest extends TestCase {
    private RankAllocator allocator;
    private List<WeeklyResult> weeklyResults;
    private List<WeeklyRanking> weeklyRankings;

    public void setUp() {
        this.allocator = new RankAllocator(3);
        this.weeklyResults = setUpInitialResults();

        this.weeklyRankings = new ArrayList<>();
        for (WeeklyResult weeklyResult : weeklyResults) {
            weeklyRankings.add(allocator.rank(weeklyResult));
        }
    }

    private List<WeeklyResult> setUpInitialResults() {
        List<WeeklyResult> results = new ArrayList<>();
        results.add(new WeeklyResult("1", "Max"));
        results.add(new WeeklyResult("2", "James"));
        results.add(new WeeklyResult("3", "Max"));
        results.add(new WeeklyResult("4", "Max"));
        results.add(new WeeklyResult("5", "James"));
        return results;
    }

    public void tearDown() {
    }

    @Test
    public void testRankingOrderAndLessThanN() {
        // Cases 1, 2 & 3a).
        // We'll show the list as [<week, <name, PlayerValue>>]
        // After setup:
        // check Max is ranked 1 for all weeks,
        for (WeeklyRanking ranking : weeklyRankings) {
            assertEquals(ranking.getRanks().get(0).getPlayerName(), "Max");
        }

        final int numWeeks = weeklyRankings.size();
        final WeeklyRanking fifthWeekRanking = weeklyRankings.get(numWeeks-1);
        // check weeks have been assigned correctly to weekly ranking
        assertEquals(fifthWeekRanking.getWeek(), "5");
        assertEquals(fifthWeekRanking.getRanks().size(), 2);

        // check Max and James' tallies after week 5 to be 3 and 2.
        assertEquals(fifthWeekRanking.getRanks().get(0).getCurrentValue().getWeeksAtNumberOne(), 3);
        assertEquals(fifthWeekRanking.getRanks().get(1).getPlayerName(), "James");
        assertEquals(fifthWeekRanking.getRanks().get(1).getCurrentValue().getFirstReached(), "2");
        assertEquals(fifthWeekRanking.getRanks().get(1).getCurrentValue().getWeeksAtNumberOne(), 2);
    }

    @Test
    public void testNPlusPlayers() {
        // Case 3 b) & Case 4
        // Populate with more weekly results and rankings
        populateWithN();
    }

    private void populateWithN() {
        weeklyResults.add(new WeeklyResult("6", "Will"));
        weeklyResults.add(new WeeklyResult("7", "Mark"));
        weeklyResults.add(new WeeklyResult("8", "Mark"));

        for (WeeklyResult result : weeklyResults.subList(5, weeklyResults.size())) {
            weeklyRankings.add(allocator.rank(result));
        }
        assertEquals(weeklyRankings.size(), 8);

        // for week 7, Will should be number 3 with 1 week at No.1.
        final WeeklyRanking seventhWeekRanking = weeklyRankings.get(6);
        assertEquals(seventhWeekRanking.getRanks().get(2).getPlayerName(), "Will");
        assertEquals(seventhWeekRanking.getRanks().get(2).getCurrentValue().getFirstReached(), "6");
        assertEquals(seventhWeekRanking.getRanks().get(2).getCurrentValue().getWeeksAtNumberOne(), 1);

        // for week 8, Mark should be number 3, but have the same weeks tally as James
        final WeeklyRanking eighthWeekRanking = weeklyRankings.get(7);
        assertEquals(eighthWeekRanking.getRanks().get(2).getPlayerName(), "Mark");
        assertEquals(eighthWeekRanking.getRanks().get(2).getCurrentValue().getWeeksAtNumberOne(),
                eighthWeekRanking.getRanks().get(1).getCurrentValue().getWeeksAtNumberOne());
    }
}
