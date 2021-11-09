package hu.iplayzed.reversiai;

import game.engine.Engine;

import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static final String ENGINE_PACKAGE_PATH_OTHELLO_GAME = "game.oth.OthelloGame";
    public static final String ENGINE_PACKAGE_PATH_GREEDY_PLAYER = "game.oth.players.GreedyPlayer";
    public static final String ENGINE_FPS_EVALUATION_MODE = "0";
    public static final String ENGINE_TABLE_SIZE = "10";
    public static final String ENGINE_HOLE_GEN_ITERATIONS = "3";
    public static final String ENGINE_MAX_ALLOWED_DECISION_TIMEOUT_MS = "2000";

    @SuppressWarnings("SameParameterValue")
    private static void testEngine(final String fpsEvaluationMode, final String player, final String tableSize,
                                   final String holeGenIterations) throws Exception {

        //Generate a seed for hole placement.
        String seed = Integer.toString(ThreadLocalRandom.current().nextInt());

        //Create args for engine with some parameters predefined.
        String[] engineArgs = {fpsEvaluationMode, ENGINE_PACKAGE_PATH_OTHELLO_GAME, seed, tableSize,
                holeGenIterations, ENGINE_MAX_ALLOWED_DECISION_TIMEOUT_MS, ENGINE_PACKAGE_PATH_GREEDY_PLAYER, player};
        //Start the game.
        Engine.main(engineArgs);

    }

    public static void main(String[] args) {
        try {
            testEngine("1", ENGINE_PACKAGE_PATH_GREEDY_PLAYER, ENGINE_TABLE_SIZE,
                    ENGINE_HOLE_GEN_ITERATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
