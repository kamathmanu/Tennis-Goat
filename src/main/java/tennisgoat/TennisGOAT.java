package tennisgoat;

import scraper.Scraper;
import scraper.ScraperException;

import java.io.IOException;

public class TennisGOAT {
    public static void main(String[] args) {
        try {
            Scraper.main();
        } catch (ScraperException e) {
            System.out.println(e.toString());
        }
    }
}
