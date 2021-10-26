package hu.iplayzed.reversiai;

import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static void testEngine() {
        String seed = Integer.toString(ThreadLocalRandom.current().nextInt());
        String[] engineArgs = {"0", "game.oth.OthelloGame", seed, "10", "3", "2000", "game.oth.players.GreedyPlayer",
                "RandomAgent"};
    }

    public static void main(String[] args) {

    }
}
