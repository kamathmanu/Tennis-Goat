package scraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Scraper {
    private static final Logger logger = LogManager.getLogger(Scraper.class);
    private final String urlPrefix;
    private final String urlSuffix;
    private final Duration timeout;
    private final int totalTries;
    private WeeklyResult latestResult;

    public Scraper(final String urlPrefix, final String urlSuffix, final Duration timeout, final int totalTries) {
        this.urlPrefix = urlPrefix;
        this.urlSuffix = urlSuffix;
        this.timeout = timeout;
        this.totalTries = totalTries;
        this.latestResult = new WeeklyResult("1973-08-16","N/A");
    }

    public WeeklyResult scrape(final String week) throws ScraperException {
        // in the case the latest scraped data returns an "empty" weekly result, simply retain the latest No.1
        // since it is likely he wouldn't have changed. A weekly result is deemed empty if no player or week info
        // can be found on the ATP page.
        this.latestResult = scrapeWeekly(week)
                .orElse(new WeeklyResult(updateLatestWeekByOne(), this.latestResult.getPlayerName()));
        return this.latestResult;
    }

    private Optional<WeeklyResult> scrapeWeekly(final String week) throws ScraperException {
        final Document document = loadDocument(weeklyResultUrl(week));
        final boolean numberOneDataExists = selectNumberOneRankCell(document).isPresent();
        final Element playerCell = numberOneDataExists ? selectPlayerCellElement(document) : null;

        return Optional.ofNullable(playerCell)
                .map(element -> new WeeklyResult(week, element.text()));
    }

    public List<String> loadWeeks() throws ScraperException {
        final Document document = loadDocument(urlPrefix);
        final Elements elements = selectRankingWeeksElements(document);
        final List<String> weeks = extractWeeks(elements);

        return noEmptyElseThrow(weeks);
    }

    private Document loadDocument(final String url) throws ScraperException {
        Document document = null;
        for (int tries = 0; tries < this.totalTries; tries++) {
            try {
                document = Jsoup.connect(url).timeout((int) timeout.toMillis()).get();
                break;
            } catch (IOException e) {
                if (tries == this.totalTries) {
                    throw new ScraperException("Error loading ATP website: ", e);
                }
            }
        }
        return document;
    }

    private static Elements selectRankingWeeksElements(final Document document) {
        // extract ranking weeks from the dropdown menu
        final Elements result = document.getElementsByAttributeValue("data-value", "rankDate")
                .select("ul li");

        Collections.reverse(result);
        return result;
    }

    private static List<String> extractWeeks(final Collection<Element> elements) {
        // refer to https://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
        // and https://www.baeldung.com/java-maps-streams.
        return elements.stream()
                .map(Scraper::extractWeek)
                .filter(week -> Optional.ofNullable(week).isPresent())
                .collect(Collectors.toList());
    }

    private static List<String> noEmptyElseThrow(final List<String> weeks) throws ScraperException {
        if (weeks.isEmpty()) {
            throw new ScraperException("Cannot process empty data from the weeks calendar!");
        } else {
            return weeks;
        }
    }

    private String weeklyResultUrl(final String week) {
        return urlPrefix + "rankDate=" + week + urlSuffix;
    }

    private static Optional<Element> selectNumberOneRankCell(final Document document) {
        final Element rankCell = selectPlayerRankCell(document);
        return Optional.ofNullable(rankCell).filter(element -> numberOneRankCellExists(element));
    }

    private static Element selectPlayerCellElement(final Document document) {
        return document.getElementsByClass("player-cell").first();
    }

    private static boolean numberOneRankCellExists(final Element rankCell) {
        return rankCell.text().equals("1");
    }

    private static Element selectPlayerRankCell(final Document document) {
        return document.getElementsByClass("rank-cell").first();
    }

    private static String extractWeek(final Element li) {
        return li.text().replaceAll("\\.", "-");
    }

    private String updateLatestWeekByOne() {
        return LocalDate.parse(this.latestResult.getWeek()).plusWeeks(1).toString();
    }
}