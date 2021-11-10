package hu.iplayzed.reversiai.agents;

import game.oth.OthelloAction;
import game.oth.OthelloPlayer;

import java.util.ArrayList;
import java.util.Random;

import static game.engine.utils.Utils.copy;
import static game.oth.OthelloGame.*;

@SuppressWarnings("unused")
public class MinimaxAlphaBetaDepthLimitedAgent extends OthelloPlayer {

    /**
     * This is a list of actions, or in other words the places on the gameboard. In some cases it is easier or required
     * to use this in instead of double-looping through all board indexes.
     */
    private final ArrayList<OthelloAction> boardActions = new ArrayList<>();
    /**
     * The maximum recursion depth of the {@link #MMABDL(int[][], int, float, float, OthelloAction, int)} algorithm.
     * On a typical laptop this much never ran out on a 10x10 board, with hole morphing set to 3 and total maximum
     * step time set to 2 ms. Could be set dynamically in the future.
     */
    private static final int MAX_DEPTH = 4;
    /**
     * Represent that there are no more possible steps left in the game.
     */
    private static final int NONE = 0;
    /**
     * Represent the initial depth of {@link #MMABDL(int[][], int, float, float, OthelloAction, int)}.
     */
    private static final int STARTING_DEPTH = 0;
    /**
     * The color of the enemy player.
     */
    private final int enemyColor = enemyColor();
    /**
     * Keeps track of this player's remaining time in ns.
     */
    private long remainingTime = 0;

    /**
     * The public constructor for the agent.
     * Besides, calling the super constructor, it also fills up {@link #boardActions}
     * with indexes, where there is no hole, as there is no need to check them.
     *
     * @param color  The color of the player Agent.
     * @param board  The actual gameboard.
     * @param random A pseudorandom generator required by super.
     */
    public MinimaxAlphaBetaDepthLimitedAgent(int color, int[][] board, Random random) {
        super(color, board, random);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != MISSING) boardActions.add(new OthelloAction(i, j));
            }
        }
    }

    /**
     * This function evaluates the possible steps in the game and decides on what step to make.
     * It also updates the board with the previous player's step if there was a valid one.
     *
     * @param prevAction     The previous action in the game.
     * @param remainingTimes The two players' remaining time in ns.
     * @return The action to be stepped by this player, which was considered best.
     */
    @Override
    public OthelloAction getAction(OthelloAction prevAction, long[] remainingTimes) {
        remainingTime = remainingTimes[color];
        if (prevAction != null) setAction(board, prevAction.i, prevAction.j, 1 - color);
        OthelloAction bestStep = null;
        float bestActionVal = Integer.MIN_VALUE;
        float val;
        for (OthelloAction step : boardActions) {
            if (isValid(board, step.i, step.j, color)) {
                int[][] childNode = copy(board);
                setAction(childNode, step.i, step.j, color);
                val = MMABDL(childNode, STARTING_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, step, enemyColor);
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

    /**
     * This function is a modified version of the general minimax algorithm with alpha-beta pruning and depth
     * limitation. It builds up the game tree during search.
     * <p>
     * The heuristic value is calculated based on how much the difference in coins would be in a given step.
     * It naturally weighs steps with positive or negative weighs if it would result in more and less coins for the
     * given player. If there is no change, it is considered as neutral.
     * <p>
     * Nodes of the game tree are considered to be boards and are built up using the possible steps.
     *
     * @param node  The current node represented by the complete gameboard.
     * @param depth The current depth of the MMADBL call.
     * @param alpha The best maximised value so far.
     * @param beta  The best minimised value so far.
     * @param step  A given step on the gameboard.
     * @param color The given color of the player (encompasses if it is maximising or minimising).
     * @return The heuristic value of a vertex in the game tree.
     */
    float MMABDL(int[][] node, int depth, float alpha, float beta, OthelloAction step, int color) {

        if (depth >= MAX_DEPTH || remainingTime <= 100000000 || getSteps(node, color).size() == NONE) {

            setAction(node, step.i, step.j, color);

            int myCoins = 0;
            int enemyCoins = 0;
            for (OthelloAction action : boardActions) {
                if (node[action.i][action.j] == this.color) myCoins++;
                else if (node[action.i][action.j] == enemyColor) enemyCoins++;
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
                max = Math.max(max, MMABDL(childNode, depth + 1, alpha, beta, nextStep, enemyColor));
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

    /**
     * A helper function.
     * Gets the possible steps for a given board (which is the node in the game tree).
     *
     * @param node  The given node to check possible steps for.
     * @param color The player's color, whose possible steps are checked for.
     * @return A list of possible steps on a node.
     */
    private ArrayList<OthelloAction> getSteps(int[][] node, int color) {
        ArrayList<OthelloAction> steps = new ArrayList<>(boardActions.size());
        for (OthelloAction action : boardActions) {
            if (isValid(node, action.i, action.j, color)) steps.add(new OthelloAction(action.i, action.j));
        }
        return steps;
    }

    /**
     * Helper function for realizing what color the enemy is.
     *
     * @return The enemy's color.
     */
    private int enemyColor() {
        return color == WHITE ? BLACK : WHITE;
    }
}