package ranking;

import junit.framework.TestSuite;
import org.junit.Test;

public class RankingTests {
    public static TestSuite suite() {
        TestSuite rankTest = new TestSuite("Generation of Accurate Weekly Rankings Suite");
        rankTest.addTestSuite(TrackScoresTest.class);
        rankTest.addTestSuite(WeeklyRankTest.class);
        return rankTest;
    }
}
