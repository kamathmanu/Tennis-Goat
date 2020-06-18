// A POJO that encapsulates a ranking week and the name of the corresponding No.1 player
public class WeeklyResult {
    private final String week;
    private final String playerName;

    public WeeklyResult(final String week, final String playerName) {
        this.week = week;
        this.playerName = playerName;
    }
    public String getWeek() {
        return week;
    }
    public String getPlayerName() {
        return playerName;
    }
}
