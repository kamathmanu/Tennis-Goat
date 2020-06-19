package scraper;

public class ScraperException extends Exception {
    final String message;
    public ScraperException (String message) {
        this.message = message;
    }
    @Override
    public String toString() {
        return this.message;
    }
}
