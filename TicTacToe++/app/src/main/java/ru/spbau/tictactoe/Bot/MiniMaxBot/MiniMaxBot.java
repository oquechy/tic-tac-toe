package ru.spbau.tictactoe.Bot.MiniMaxBot;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ru.spbau.tictactoe.Bot.CleverBot.CleverBot;
import ru.spbau.tictactoe.Logic.Board.AbstractBoard;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;

import static ru.spbau.tictactoe.Bot.BoardAnalyzer.getAvailableMoves;

/**
 * Bot which implements the MiniMax algorithm.
 */
public class MiniMaxBot extends CleverBot {
    private int maxDepth;

    public MiniMaxBot(Board board) {
        super(board);
        maxDepth = 4;
    }

    @Override
    public Turn makeTurn() {
        boardCopy = board.deepCopy();
        return miniMax(boardCopy, 0, null).turn;
    }

    /**
     * The body of the algorithm.
     *
     * @param currentDepth the current depth
     * @return the score of the board
     */
    private TurnWithScore miniMax(Board givenBoard, int currentDepth, Turn turn) {
        if (currentDepth++ == maxDepth || givenBoard.isOver()) {
            return new TurnWithScore(turn, score(givenBoard));
        }
        TurnWithScore result = null;
        if (givenBoard.getCurrentPlayer() == player) {
            result = getMax(givenBoard, currentDepth);

        } else {
            result = getMin(givenBoard, currentDepth);
        }
        if(result.score < Integer.MIN_VALUE + 1000){
            result.score++;
        }
        if(result.score > Integer.MAX_VALUE - 1000){
            result.score--;
        }
        return result;
    }

    /**
     * Play the move with the highest score.
     *
     * @param currentPly the current depth
     * @return the score of the board
     */
    private TurnWithScore getMax(Board givenBoard, int currentPly) {
        ArrayList<TurnWithScore> turnWithScores = new ArrayList<>();
        int currentInnerBoard = givenBoard.getCurrentInnerBoard();
        for (Turn turn : getAvailableMoves(givenBoard)) {
            givenBoard.makeMove(turn);

            int score = miniMax(givenBoard, currentPly, turn).score;
            turnWithScores.add(new TurnWithScore(turn, score));
            givenBoard.discardChanges(turn, currentInnerBoard);
        }
        return randomBestTurn(turnWithScores, true);
    }

    /**
     * Gets an ArrayList of TurnWithScore and returns one with the best(min or max).
     *
     * @param turns is an ArrayList of turns with score to choose from
     * @param max   indicates which score is to be considered as the best (min or max)
     * @return random TurnWithScore from TurnWithScores with the best (equal) score
     */
    @SuppressWarnings("unchecked")
    private TurnWithScore randomBestTurn(ArrayList<TurnWithScore> turns, boolean max) {
        int bestScore;
        if (max) {

            bestScore = Collections.max(turns).score;
        } else {
            bestScore = Collections.min(turns).score;
        }
        ArrayList<TurnWithScore> bestTurns = new ArrayList<>();
        for (TurnWithScore turnWithScore : turns) {
            if (turnWithScore.score == bestScore) {
                bestTurns.add(turnWithScore);
            }
        }
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(bestTurns.size());
        return bestTurns.get(index);
    }

    /**
     * Play the move with the lowest score.
     *
     * @param currentDepth the current depth
     * @return the score of the board
     */
    private TurnWithScore getMin(Board givenBoard, int currentDepth) {
        ArrayList<TurnWithScore> turnWithScores = new ArrayList<>();
        int currentInnerBoard = givenBoard.getCurrentInnerBoard();
        for (Turn turn : getAvailableMoves(givenBoard)) {
            givenBoard.makeMove(turn);
            int score = miniMax(givenBoard, currentDepth, turn).score;
            turnWithScores.add(new TurnWithScore(turn, score));
            givenBoard.discardChanges(turn, currentInnerBoard);
        }
        return randomBestTurn(turnWithScores, false);
    }

    /**
     * Gets ids of three squares on board which combination can provide a win
     * and calculates he score relevant to this three squares.
     *
     * @param board   is board from where the squares will be taken
     * @param coef    a coefficient on which result will be multiplied
     * @param values  values for these squares
     * @param squares square ids
     * @return the score for these squares
     */
    private int getThreeSquaresScores(AbstractBoard board, int coef, int[] values, int... squares) {
        int cntStatuses[] = new int[4];
        for (int x : squares) {
            cntStatuses[board.getBox(x).getStatus().ordinal()]++;
        }
        int score = 0;
        if ((cntStatuses[1] == 0 || cntStatuses[2] == 0) &&
                cntStatuses[3] == 0) {
            for (int x : squares) {
                score += values[x];
            }
            if (cntStatuses[0] < 2) {
                score *= coef;
            }
        }
        return score;
    }

    /**
     * Transforms board status to sign (0, +1, -1)
     * @param status is a status to be transformed
     * @return 1 if bot won, -1 if bot lost, 0 if the status is draw
     */
    private int statusSign(Status status){
        if(status == Status.DRAW){
            return 0;
        }
        return Status.playerToStatus(this.player) == board.getStatus() ? 1 : -1;
    }


    /**
     * Calculates the score on given board.
     *
     * @return the score of the board
     */
    private int scoreOnBoard(int coef, AbstractBoard board) {
        if (board.getStatus() == Status.CROSS
                || board.getStatus() == Status.NOUGHT) {
            return coef * statusSign(board.getStatus());
        }
        int values[] = new int[9];
        if ((board instanceof Board.InnerBoard || board instanceof Board)
                && board.getStatus() != Status.DRAW) {
            for (int i = 0; i < 9; i++) {
                values[i] = scoreOnBoard(coef / 64, board.getBox(i));
            }
            int score = 0;
            for (int i = 0; i < 3; i++) {
                score += getThreeSquaresScores(board, coef / 16, values, 3 * i, 3 * i + 1, 3 * i + 2);
                score += getThreeSquaresScores(board, coef / 16, values, i, i + 3, i + 6);
            }
            score += getThreeSquaresScores(board, coef / 16, values, 0, 4, 8);
            score += getThreeSquaresScores(board, coef / 16, values, 2, 4, 6);
            return score;
        }
        return 0;
    }

    /**
     * Get the score of the board.
     *
     * @return the score of the board
     */
    int score(Board board) {
        int score = 0;
        if (board.getStatus() != Status.GAME_CONTINUES) {
            return Integer.MAX_VALUE * statusSign(board.getStatus());
        }
        if (board.getCurrentInnerBoard() == -1) {
            score += 48 * (player == board.getCurrentPlayer() ? 1 : -1);
        }
        score += scoreOnBoard(4096, board);
        return score;
    }

    /**
     * Class which represents a turn and a score relevant to this turn.
     */
    private static class TurnWithScore implements Comparable {
        private Turn turn;
        private int score;

        private TurnWithScore(Turn turn, int score) {
            this.turn = turn;
            this.score = score;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            return ((Integer) this.score).compareTo(((TurnWithScore) o).score);
        }
    }

    public static void main(String[] args) {
        Board board = new Board();
        MiniMaxBot bot = new MiniMaxBot(board);
        bot.go();
    }
}
