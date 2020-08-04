package legacyvisualizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import rankallocator.PlayerRank;
import rankallocator.WeeklyRanking;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// A class to write out a list of the "GOAT" status for each week.
public class LegacyVisualizer {
    private final String dirName;
    private static final Logger logger = LogManager.getLogger(LegacyVisualizer.class);
    
    public LegacyVisualizer(final String path) {
        this.dirName = path;
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private void createListFile(WeeklyRanking weeklyRanking) throws IOException {
        List<String> lines = generateLegacyList(weeklyRanking);
        Path file = Paths.get(dirName + "/" + weeklyRanking.getWeek()+".txt");
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    private static List<String> generateLegacyList (WeeklyRanking weeklyRanking) {
        final List<PlayerRank> ranks = weeklyRanking.getRanks();

        List<String> lines = new ArrayList<>();
        lines.add("Rank\t\tPlayer\t\tWeeks At #1\t\tFirst Reached On");
        for (int i = 0; i < ranks.size(); i++) {
            final String player = ranks.get(i).getPlayerName();
            final int weeksTally = ranks.get(i).getCurrentValue().getWeeksAtNumberOne();
            final String firstReached = ranks.get(i).getCurrentValue().getFirstReached();
            final String line = Integer.toString(i+1) + "." + "\t\t" + player + "\t\t" + Integer.toString(weeksTally) + "\t\t" + firstReached;
            lines.add(line);
        }
        return lines;
    }

    public void visualize(final WeeklyRanking weeklyRanking) {
        // create a file for that week. No need to check if that file
        // for the week exists - if we are retrying, it's probably due to a valid
        // reason (timeout, re-running the visualization etc) and the data we get
        // is at least the same or newer than the previous try,
        // so overwriting the exist file would be fine.
        Configurator.setLevel(logger.getName(), Level.DEBUG);
        try {
            createListFile(weeklyRanking);
        } catch (IOException e) {
            System.out.println("Failed to create file. " + e);
        }
    }
}
