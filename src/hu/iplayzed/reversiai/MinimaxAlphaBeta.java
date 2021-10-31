package hu.iplayzed.reversiai;

import game.oth.OthelloAction;
import game.oth.OthelloPlayer;

import java.util.Random;

public class MinimaxAlphaBeta extends OthelloPlayer {

    /**
     * Specifies the class of a player, that is represented by the specified color
     * can use the specified board, that is a copy of the game's board, and a
     * random number generator.
     *
     * @param color  player's color
     * @param board  game board
     * @param random random number generator
     */
    public MinimaxAlphaBeta(int color, int[][] board, Random random) {
        super(color, board, random);
    }

    @Override
    public OthelloAction getAction(OthelloAction prevAction, long[] remainingTimes) {
        return null;
    }
}
