package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Scraper {
    public static void main() {
        final String ATP_URL_PREFIX = "https://www.atptour.com/en/rankings/singles?";
        final String ATP_URL_SUFFIX = "&rankRange=0-100";
        // get the list of historical ranking weeks - basically from 1973-present.
        ArrayList<String> weeks = getWeeksForRankings(ATP_URL_PREFIX);
        // weeks might be null if no valid HTML
        if (weeks.isEmpty()) {
            System.out.println("Please provide a historical time range! Cannot rank otherwise!");
            return;
        }
        getPlayerNames(ATP_URL_PREFIX, ATP_URL_SUFFIX, weeks);
    }
    
    static ArrayList<String> getWeeksForRankings(String url) {
        ArrayList<String> weeks = new ArrayList<String>();
        try {
            final Document document = Jsoup.connect(url).get();
            // extract the series of list items corresponding to the ranking weeks, from the dropdown menu
            Elements rankingWeeksList = document.getElementsByAttributeValue("data-value", "rankDate").select("ul li");
            for (Element li : rankingWeeksList) {
                // for accessing the relevant week's ranking page later, the rankDate= param in the URL takes '-'s
                // instead of dots so we replace the characters here and then add them to out list.
                String week = li.text().replaceAll("\\.", "-");
                weeks.add(week);
            }
        } catch (IOException e) {
            System.out.println("Error while connecting and parsing HTML: " + e);
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Fatal Error: " + e);
            System.exit(1);
        }
        Collections.reverse(weeks); // start from 1973.
        return weeks;
    }

    static void getPlayerNames(String urlPrefix, String urlSuffix, ArrayList<String> weeks) {
        // dynamically update a player's ranking and animate his status
        for (String week : weeks) {
            String url = urlPrefix+"rankDate="+week+urlSuffix;
            try {
                final int SECONDS_TO_MILLISECONDS = 1000;
                // time out is an issue. ideally, try mutliple times to get the data??
                final Document document = Jsoup.connect(url).timeout(180 * SECONDS_TO_MILLISECONDS).get();
                Element player = document.getElementsByClass("player-cell").first();
                if (player == null) {
                    continue;
                } else {
                    System.out.println("Week: " + week + " No.1: "+ player.text());
                }
            } catch (IOException e) {
                System.out.println("Error while connecting and parsing HTML: " + e);
                System.exit(1);
            }
        }
    }
}