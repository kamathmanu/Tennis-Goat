package tennisgoat;

import scraper.Scraper;

import java.io.IOException;

public class TennisGOAT {
    public static void main(String[] args) {
        try {
            Scraper.main();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
