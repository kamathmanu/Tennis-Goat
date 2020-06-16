package Scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

public class Scraper {
    public static void main() {
        final String ATP_URL_PREFIX = "https://www.atptour.com/en/rankings/singles?";
        final String ATP_URL_SUFFIX = "&rankRange=0-100";
        // get the list of historical ranking weeks - basically from 1973-present.
        ArrayList<String> weeks = new ArrayList<String>();
        try {
            final Document document = Jsoup.connect(ATP_URL_PREFIX).get();
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
        // weeks might be null if no valid HTML
        Collections.reverse(weeks);
        if (weeks.size() == 0) {
            System.out.println("Please provide a historical time range! Cannot rank otherwise!");
            return;
        }
    }
}