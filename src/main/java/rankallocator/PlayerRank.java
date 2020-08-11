package rankallocator;

//POJO coupling playerName and PlayerValue passed in to a WeeklyRanking's PriorityQueue.
public class PlayerRank {
    private final String playerName;
    private final PlayerValue currentValue;

    public PlayerRank(final String name, final PlayerValue value) {
        this.playerName = name;
        this.currentValue = value;
    }

    public String getPlayerName() { return playerName; }
    public PlayerValue getCurrentValue() { return currentValue; }
}
