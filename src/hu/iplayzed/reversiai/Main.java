package hu.iplayzed.reversiai;

import game.engine.Engine;

import java.util.concurrent.ThreadLocalRandom;

// TODO: find out why my agent class is not reflected.
public class Main {
    private static void testEngine() throws Exception {
        String seed = Integer.toString(ThreadLocalRandom.current().nextInt());
        String[] engineArgs = {"0", "game.oth.OthelloGame", seed, "10", "3", "2000", "game.oth.players.GreedyPlayer",
                "SamplePlayer"};
        Engine.main(engineArgs);
    }

    public static void main(String[] args) {
        try {
            testEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
