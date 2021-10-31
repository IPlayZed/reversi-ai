package hu.iplayzed.reversiai;

import game.oth.OthelloAction;
import game.oth.OthelloGame;
import game.oth.OthelloPlayer;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("unused")
public class MinimaxAlphaBeta extends OthelloPlayer {

    private ArrayList<OthelloAction> othelloActions = new ArrayList<>();

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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                othelloActions.add(new OthelloAction(i, j));
            }
        }
    }

    private int updateColor() {
        return 1 - color;
    }

    @Override
    public OthelloAction getAction(OthelloAction prevAction, long[] remainingTimes) {
        if (prevAction != null) OthelloGame.setAction(board, prevAction.i, prevAction.j, updateColor());
        return null;
    }
}
