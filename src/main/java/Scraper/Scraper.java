package Scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Scraper {
    public static void main() {
        final String URL = "https://www.atptour.com/en/rankings/singles";
        try {
            final Document document = Jsoup.connect(URL).get();
            
        } catch (Exception e) {
            e.printStackTrace(); //do something better - show error code
        }
    }
}