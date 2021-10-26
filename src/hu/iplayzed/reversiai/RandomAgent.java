package hu.iplayzed.reversiai;

import game.oth.OthelloAction;
import game.oth.OthelloPlayer;

import java.util.Random;

public class RandomAgent extends OthelloPlayer {

    public RandomAgent(int color, int[][] board, Random random) {
        super(color, board, random);
    }

    @Override
    public OthelloAction getAction(OthelloAction prevAction, long[] remainingTimes) {
        int i = random.nextInt(board.length);
        int j = random.nextInt(board[i].length);
        return new OthelloAction(i, j);
    }
}
