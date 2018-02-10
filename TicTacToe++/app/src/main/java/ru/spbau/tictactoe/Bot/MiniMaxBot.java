package ru.spbau.tictactoe.Bot;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ru.spbau.tictactoe.Logic.Board.AbstractBoard;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;
import static ru.spbau.tictactoe.Bot.BoardAnalyzer.*;


public class MiniMaxBot extends CleverBot {
    private int maxPly;
    private static final Logger logger = LoggerFactory.getLogger(MiniMaxBot.class);
    private ArrayList<Turn> bestTurns = new ArrayList<>();

    public MiniMaxBot(Board board) {
        super(board);
        maxPly = 4;
    }

    public Turn makeTurn() {
        boardCopy = board.deepCopy();
        bestTurns.clear();
        Turn res = miniMax(boardCopy, 0, null).turn;
        //logger.debug("turn " + Integer.toOctalString(res.getInnerBoard())
        // + " " + Integer.toOctalString(res.getInnerSquare()));
        return res;
    }

    /**
     * The meat of the algorithm.
     *
     * @param currentPly the current depth
     * @return the score of the board
     */
    private TurnWithScore miniMax( Board givenBoard, int currentPly, Turn turn) {
        if(turn != null){
            //logger.debug("current play = {}, turn = {} {}", currentPly, turn.getInnerBoard(), turn.getInnerSquare());
            //printBoard(givenBoard);
        }
        if (currentPly++ == maxPly || givenBoard.isOver()) {
            //logger.debug("{} {} score = {}", turn.getInnerBoard(), turn.getInnerSquare(), score(givenBoard));
            return new TurnWithScore(turn, score(givenBoard));
        }
        if (givenBoard.getCurrentPlayer() == player) {
            return getMax(givenBoard, currentPly);
        } else {
            return getMin(givenBoard, currentPly);
        }
    }

    /**
     * Play the move with the highest score.
     *
     * @param currentPly the current depth
     * @return the score of the board
     */
    TurnWithScore getMax(Board givenBoard, int currentPly) {
        ArrayList<TurnWithScore> turnWithScores = new ArrayList<>();
        //int bestScore = Integer.MIN_VALUE;
        int currentInnerBoard = givenBoard.getCurrentInnerBoard();
        assert givenBoard.getCurrentPlayer() == player;
        for (Turn turn : getAvailableMoves(givenBoard)) {
            givenBoard.makeMove(turn);
            //logger.debug("current inner board before changes " + Integer.toString(currentInnerBoard));

            int score = miniMax(givenBoard, currentPly, turn).score;
            turnWithScores.add(new TurnWithScore(turn, score));
            /*if (score >= bestScore) {
                bestScore = score;
            }
            printBoard(givenBoard);*/
            givenBoard.discardChanges(turn, currentInnerBoard);
            /*assert currentInnerBoard == givenBoard.getCurrentInnerBoard();
            logger.debug("current inner board after changes " + Integer.toString(currentInnerBoard));
            printBoard(givenBoard);*/
        }

        TurnWithScore ts = randomBestTurn(turnWithScores, true);
        bestTurns.add(ts.turn);
        return ts;
    }

    private TurnWithScore randomBestTurn(ArrayList<TurnWithScore> turns, boolean max){
        int bestScore;
        if(max){
            bestScore = Collections.max(turns).score;
        }
        else{
            bestScore = Collections.min(turns).score;
        }
        ArrayList<TurnWithScore> bestTurns = new ArrayList<>();
        for(TurnWithScore turnWithScore : turns){
            if(turnWithScore.score == bestScore){
                bestTurns.add(turnWithScore);
            }
        }
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(bestTurns.size());
        //logger.debug("best turn = {}, {}", bestTurns.get(index).turn.getInnerSquare(), max ? "max" : "min");
        return bestTurns.get(index);
    }

    /**
     * Play the move with the lowest score.
     *
     * @param currentPly the current depth
     * @return the score of the board
     */
    TurnWithScore getMin(Board givenBoard, int currentPly) {
        assert givenBoard.getCurrentPlayer() == player.opponent();
        //printBoard(givenBoard);
        //logger.debug("current inner board = {}", givenBoard.getCurrentInnerBoard());
        ArrayList<TurnWithScore> turnWithScores = new ArrayList<>();
        int currentInnerBoard = givenBoard.getCurrentInnerBoard();
        for (Turn turn : getAvailableMoves(givenBoard)) {
            givenBoard.makeMove(turn);
            printBoard(givenBoard);
            int score = miniMax(givenBoard, currentPly, turn).score;
            turnWithScores.add(new TurnWithScore(turn, score));
            givenBoard.discardChanges(turn, currentInnerBoard);
            //logger.debug("board after discard");
            //printBoard(givenBoard);
        }
        //printBoard(givenBoard);
        TurnWithScore ts = randomBestTurn(turnWithScores, false);
        bestTurns.add(ts.turn);
        return ts;
    }

    int getThreeSquaresScores(AbstractBoard board, int coef, int[] values, int... squares) {
        //logger.debug("three {} coef = {}", board.getClass().getSimpleName(), coef);
        int cntStatuses[] = new int[4];
        for (int x : squares) {
            cntStatuses[board.getBox(x).getStatus().ordinal()]++;
        }
        int score = 0;
        if ((cntStatuses[1] == 0 || cntStatuses[2] == 0) &&
                cntStatuses[3] == 0) {
            for(int x : squares){
                score += values[x];
                //logger.debug("value " + Integer.toString(x) + " " + Integer.toString(values[x]));
            }
            if(cntStatuses[0] < 2){
                score *= coef;
            }
        }
        return score;
    }



    /**
     * Calculates the score on given board.
     *
     * @return the score of the board
     */
    int scoreOnBoard(int coef, AbstractBoard board) {
        //logger.debug("{} coef = {}", board.getClass().getSimpleName(), coef);
        if (board.getStatus() == Status.CROSS
                || board.getStatus() == Status.NOUGHT) {
            return coef * (Status.playerToStatus(this.player) == board.getStatus() ? 1 : -1);
        }
        int values[] = new int[9];
        if ((board instanceof Board.InnerBoard || board instanceof Board)
                && board.getStatus() != Status.DRAW) {
            boolean debugOutput = false;
            for (int i = 0; i < 9; i++) {
                //logger.debug("{} i = {}", board.getClass().getSimpleName(),  i);
                values[i] = scoreOnBoard(coef / 64, board.getBox(i));
                /*if(values[i] != 0){
                    debugOutput = true;
                    logger.debug("{} values[{}] = {}",board.getClass().getSimpleName(), i, values[i]);
                }*/
            }
            int score = 0;
            //logger.debug(board.getClass().getSimpleName());
            for (int i = 0; i < 3; i++) {
                score += getThreeSquaresScores(board, coef / 16, values,3 * i, 3 * i + 1, 3 * i + 2);
                if(debugOutput)
                    logger.debug("vertical {}, score = {}", i, score);
                score += getThreeSquaresScores(board, coef / 16, values, i, i + 3, i + 6);
                if(debugOutput)
                    logger.debug("horizontal {}, score = {}", i, score);
            }
            score += getThreeSquaresScores(board, coef / 16, values, 0, 4, 8);
            if(debugOutput)
                logger.debug("diagonal from 0, score = {}", score);
            score += getThreeSquaresScores(board, coef / 16, values, 2, 4, 6);
            if(debugOutput)
                logger.debug("diagonal from 2, score = {}", score);
            //logger.debug("score = {}", score);
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
        if(board.getStatus() != Status.GAME_CONTINUES){
            return scoreOnBoard(Integer.MAX_VALUE / 32, board);
        }
        if(board.getCurrentInnerBoard() == -1){
            score += 48 * (player == board.getCurrentPlayer() ? 1 : -1);
        }
        score += scoreOnBoard(4096, board);
        return score;
    }

    private static class TurnWithScore implements Comparable{
        Turn turn;
        int score;

        TurnWithScore(Turn turn, int score) {
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
        //board.makeMove(8);
        //System.out.printf("%d\n", bot.score(board));
        bot.go();
    }


}
