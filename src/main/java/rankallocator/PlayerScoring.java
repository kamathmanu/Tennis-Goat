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

    // functions to interface with the private hashmap
}
