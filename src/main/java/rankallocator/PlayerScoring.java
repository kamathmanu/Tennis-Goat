package rankallocator;

// A wrapper class that uses a hashmap and additional metadata to maintain the
// score of each No.1 player. The score is maintained using the PlayerValue abstraction
import java.util.HashMap;
import java.util.Map;

public class PlayerScoring {
    private Map<String, PlayerValue> playerScorer;

    public PlayerScoring() {
        playerScorer = new HashMap<>();
    }

    public Map<String, PlayerValue> getPlayerScorer() {
        return playerScorer;
    }

    public PlayerValue findPlayerValue(final String player) {
        return this.playerScorer.get(player);
    }

    public void updatePlayerValue (final String week, final String player) {
        // if the player is a new No.1, construct a new entry
        // otherwise, update (by one) his week tally
        if (!this.playerScorer.containsKey(player)) {
            this.playerScorer.put(player, new PlayerValue(week));
        } else {
            // Test to make sure values are updated properly
            this.playerScorer.get(player).updateWeeksAtNumberOne();
        }
    }
}
