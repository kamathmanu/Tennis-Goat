package rankallocator;

// POJO class to hold the "score" of a player,
// which consists of the weeks they've spent at No.1,
// and the week they first attained No.1 (to break ties deterministically)

public class PlayerValue {
    private final String firstReached;
    private int weeksAtNumberOne;

    public PlayerValue(final String week) {
        this.firstReached = week;
        this.weeksAtNumberOne = 1;
    }

    public int getWeeksAtNumberOne() {
        return weeksAtNumberOne;
    }

    public String getFirstReached() {
        return firstReached;
    }

    public void updateWeeksAtNumberOne() {
        this.weeksAtNumberOne++;
    }
}
