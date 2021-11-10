package hu.iplayzed.reversiai.agents;

import game.oth.OthelloAction;
import game.oth.OthelloPlayer;

import java.util.ArrayList;
import java.util.Random;

import static game.engine.utils.Utils.copy;
import static game.oth.OthelloGame.*;

@SuppressWarnings("unused")
public class MinimaxAlphaBetaDepthLimitedAgent extends OthelloPlayer {

    private final ArrayList<OthelloAction> boardActions = new ArrayList<>();
    private static final int MAX_DEPTH = 4;
    private static final int EMPTY = 0;
    private static final int STARTING_DEPTH = 0;

    public MinimaxAlphaBetaDepthLimitedAgent(int color, int[][] board, Random random) {
        super(color, board, random);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardActions.add(new OthelloAction(i, j));
            }
        }
    }

    @Override
    public OthelloAction getAction(OthelloAction prevAction, long[] remainingTimes) {
        if (prevAction != null) setAction(board, prevAction.i, prevAction.j, 1 - color);
        OthelloAction bestStep = null;
        float bestActionVal = Integer.MIN_VALUE;
        float val = 0;
        for (OthelloAction step : boardActions) {
            if (isValid(board, step.i, step.j, color)) {
                int[][] childNode = copy(board);
                setAction(childNode, step.i, step.j, color);
                val = MMABDL(childNode, STARTING_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, step, enemyColor());
                if (val > bestActionVal) {
                    bestActionVal = val;
                    bestStep = step;
                }
            }
        }
        assert bestStep != null;
        setAction(board, bestStep.i, bestStep.j, color);
        return bestStep;
    }

    /***
     * This function is a modified version of the general minimax algorithm with alpha-beta pruning and depth
     * limitation. It builds up the game tree during search.
     *
     * The heuristic value is calculated based on how high is the mobility after a given step for a given player
     * (color).
     * @param node The current node represented by the complete gameboard.
     * @param depth The current depth of the MMADBL call.
     * @param alpha The best maximised value so far.
     * @param beta The best minimised value so far.
     * @param step A given step on the gameboard.
     * @param color The given color of the player (encompasses if it is maximising or minimising).
     * @return The heuristic value of a vertex in the game tree.
     */
    float MMABDL(int[][] node, int depth, float alpha, float beta, OthelloAction step, int color) {

        if (depth == MAX_DEPTH || getSteps(node, color).size() == EMPTY) {

            setAction(node, step.i, step.j, color);

            int myCoins = 0;
            int enemyCoins = 0;

            for (int i = 0; i < node.length; i++) {
                for (int j = 0; j < node[i].length; j++) {
                    if (node[i][j] == this.color) {
                        myCoins++;
                    } else if (node[i][j] == enemyColor()) {
                        enemyCoins++;
                    }
                }
            }

            if (myCoins + enemyCoins == 0) return 0;
            else {
                return 100 * (((float) myCoins - enemyCoins) / (myCoins + enemyCoins));
            }

        }
        ArrayList<OthelloAction> nextSteps = getSteps(node, color);
        if (color == this.color) {
            float max = Integer.MIN_VALUE;
            for (OthelloAction nextStep : nextSteps) {
                int[][] childNode = copy(node);
                setAction(childNode, nextStep.i, nextStep.j, color);
                max = Math.max(max, MMABDL(childNode, depth + 1, alpha, beta, nextStep, enemyColor()));
                if (max >= beta) return max;
                alpha = Math.max(max, alpha);
            }
            return max;
        } else {
            float min = Integer.MAX_VALUE;
            for (OthelloAction nextStep : nextSteps) {
                int[][] childNode = copy(node);
                setAction(childNode, nextStep.i, nextStep.j, color);
                min = Math.min(min, MMABDL(childNode, depth + 1, alpha, beta, nextStep, this.color));
                if (alpha >= min) return min;
                beta = Math.min(min, beta);
            }
            return min;
        }
    }

    private ArrayList<OthelloAction> getSteps(int[][] node, int color) {
        ArrayList<OthelloAction> steps = new ArrayList<>();
        for (int i = 0; i < node.length; i++) {
            for (int j = 0; j < node[i].length; j++) {
                if (isValid(node, i, j, color)) steps.add(new OthelloAction(i, j));
            }
        }
        return steps;
    }

    private int enemyColor() {
        return color == WHITE ? BLACK : WHITE;
    }
}