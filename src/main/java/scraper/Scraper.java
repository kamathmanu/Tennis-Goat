package scraper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Scraper {
    private static final Logger logger = LogManager.getRootLogger();
    private final String urlPrefix;
    private final String urlSuffix;
    private final Duration timeout;

    public Scraper(final String urlPrefix, final String urlSuffix, final Duration timeout) {
        this.urlPrefix = urlPrefix;
        this.urlSuffix = urlSuffix;
        this.timeout = timeout;
    }

    private List<WeeklyResult> scrape() throws ScraperException {
        final List<String> weeks = loadWeeks();

        return loadResults(weeks);
    }

    private List<String> loadWeeks() throws ScraperException {
        final Document document = loadDocument(urlPrefix);
        final Elements elements = selectRankingWeeksElements(document);
        final List<String> weeks = extractWeeks(elements);

        return noEmptyElseThrow(weeks);
    }

    private Document loadDocument(final String url) throws ScraperException {
        Document document = null;
        for (int tries = 0; tries < 3; tries++) {
            try {
                document = Jsoup.connect(url).timeout((int) timeout.toMillis()).get();
                break;
            } catch (IOException e) {
                if (tries == 3) { throw new ScraperException ("Error loading ATP website: ", e);}
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
                        .collect(Collectors.toList());
    }

    private static List<String> noEmptyElseThrow(final List<String> weeks) throws ScraperException{
        if (weeks.isEmpty()) {
            throw new ScraperException("Cannot process empty data from the weeks calendar!");
        } else {
            return weeks;
        }
    }

    private List<WeeklyResult> loadResults(final List<String> weeks) throws ScraperException {
        final List<WeeklyResult> result = new ArrayList<>();
        for (String week : weeks) {
            loadWeeklyResult(week).ifPresent(result::add);
        }
        return result;
    }

    private Optional<WeeklyResult> loadWeeklyResult(final String week) throws ScraperException {
        final Document document = loadDocument(weeklyResultUrl(week));
        final Element playerCell = selectPlayerCellElement(document);
        return Optional.ofNullable(playerCell).map(element -> new WeeklyResult(week, element.text()));
    }

    private String weeklyResultUrl (final String week) {
        return urlPrefix+"rankDate="+week+urlSuffix;
    }

    private static Element selectPlayerCellElement(final Document document) {
        return document.getElementsByClass("player-cell").first();
    }

    private static String extractWeek(final Element li) {
        return li.text().replaceAll("\\.", "-");
    }

    public static List<WeeklyResult> main() throws ScraperException {
        final Scraper scraper =
                new Scraper("https://www.atptour.com/en/rankings/singles?",
                        "&rankRange=0-100", Duration.ofSeconds(90));
        return scraper.scrape();
    }
}