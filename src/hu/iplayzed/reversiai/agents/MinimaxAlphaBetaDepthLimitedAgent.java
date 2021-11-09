package hu.iplayzed.reversiai.agents;

import game.oth.OthelloAction;
import game.oth.OthelloPlayer;

import java.util.ArrayList;
import java.util.Random;

import static game.engine.utils.Utils.copy;
import static game.oth.OthelloGame.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

@SuppressWarnings("unused")
public class MinimaxAlphaBetaDepthLimitedAgent extends OthelloPlayer {

    private static final int NO_MORE_CHILDREN = 0;
    private static final int MAX_DEPTH = 6;
    private static final int STARTING_DEPTH = 0;

    private final ArrayList<OthelloAction> boardActions = new ArrayList<>();
    private final ArrayList<OthelloAction> noHoles = new ArrayList<>();

    @SuppressWarnings("unused")
    public MinimaxAlphaBetaDepthLimitedAgent(int color, int[][] board, Random random) {
        super(color, board, random);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boardActions.add(new OthelloAction(i, j));
                if (board[i][j] == MISSING) noHoles.add(new OthelloAction(i, j));
            }
        }
    }

    @Override
    public OthelloAction getAction(OthelloAction prevAction, long[] remainingTimes) {
        applyPreviousAction(prevAction);

        OthelloAction bestAction = null;
        float bestHeuristicValue = Integer.MIN_VALUE;

        for (OthelloAction action : boardActions) {
            if (isValid(board, action.i, action.j, color)) {
                int[][] childNode = getChildNode(board, action, color);
                float val = minMaxAlphaBetaLimitedPruning(childNode, STARTING_DEPTH, Integer.MIN_VALUE,
                        Integer.MAX_VALUE, action, getEnemyColor());
                if (val > bestHeuristicValue) {
                    bestAction = action;
                    bestHeuristicValue = val;
                }
            }
        }
        assert bestAction != null;
        setAction(board, bestAction.i, bestAction.j, color);
        return bestAction;
    }

    private void applyPreviousAction(OthelloAction previousAction) {
        if (previousAction != null) setAction(board, previousAction.i, previousAction.j, 1 - color);
    }

    private float minMaxAlphaBetaLimitedPruning(int[][] node, int currentDepth, float alpha, float beta,
                                                @SuppressWarnings("unused") OthelloAction action, int color) {
        if (currentDepth == MAX_DEPTH || getPossibleActions(node, color).size() == NO_MORE_CHILDREN) {
            return getHeuristicValue(node, action, color); // test if it runs at all
        }

        ArrayList<OthelloAction> possibleActions = getPossibleActions(node, color);
        if (isMaximizing(color)) {
            float max = Integer.MIN_VALUE;
            for (OthelloAction possibleAction : possibleActions) {
                int[][] childNode = getChildNode(node, possibleAction, color);
                max = max(max, minMaxAlphaBetaLimitedPruning(childNode, currentDepth + 1, alpha, beta,
                        possibleAction, getEnemyColor()));
                if (max >= beta) return max;
                alpha = max(max, alpha);
            }
            return max;
        } else {
            float min = Integer.MAX_VALUE;
            for (OthelloAction possibleAction : possibleActions) {
                int[][] childNode = getChildNode(node, possibleAction, color);
                min = min(min, minMaxAlphaBetaLimitedPruning(childNode, currentDepth + 1, alpha, beta,
                        possibleAction, this.color));
                if (alpha >= min) return min;
                beta = min(min, beta);
            }
            return min;
        }
    }

    private int getEnemyColor() {
        return color == WHITE ? BLACK : WHITE;
    }

    private boolean isMaximizing(int color) {
        return color == this.color;
    }

    private int[][] getChildNode(int[][] parentNode, OthelloAction possibleAction, int parentColor) {
        int[][] childNode = copy(parentNode);
        setAction(childNode, possibleAction.i, possibleAction.j, parentColor);
        return childNode;
    }

    private ArrayList<OthelloAction> getPossibleActions(int[][] node, int nodeColor) {
        ArrayList<OthelloAction> possibleActions = new ArrayList<>();
        for (int i = 0; i < node.length; i++) {
            for (int j = 0; j < node[0].length; j++) {
                if (isValid(node, i, j, nodeColor)) possibleActions.add(new OthelloAction(i, j));
            }
        }
        return possibleActions;
    }

    // TODO : Add other heuristic functions as well.
    private float getHeuristicValue(int[][] node, OthelloAction action, int color) {

        return 80f * getMobilityValue(node);
    }

    private float getMobilityValue(int[][] node) {
        int myValidMoves = getValidMoves(node, color);
        int enemyValidMoves = getValidMoves(node, getEnemyColor());
        if (myValidMoves == enemyValidMoves) return 0;
        else {
            // 100f to optimize for floating point calculations.
            float result = (100f * myValidMoves) / (myValidMoves + enemyValidMoves);
            if (myValidMoves < enemyValidMoves) result *= -1;
            return result;
        }
    }

    private int getValidMoves(int[][] board, int color) {
        int validMoves = 0;
        for (OthelloAction action : noHoles) {
            if (isValid(board, action.i, action.j, color)) validMoves++;
        }
        return validMoves;
    }
}
