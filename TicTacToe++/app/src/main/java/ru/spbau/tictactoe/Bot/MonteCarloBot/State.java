package ru.spbau.tictactoe.Bot.MonteCarloBot;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.tictactoe.Bot.BoardAnalyzer;
import ru.spbau.tictactoe.Bot.Bot;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * A class which represents a state of the game for a certain node.
 */
public class State {
    private Board board;
    private Turn lastTurn;
    private int visitCount;
    private double winScore;
    private Bot cross;
    private Bot nought;

    public State() {
        board = new Board();
        cross = new Bot(board);
        nought = new Bot(board);
        cross.setPlayer(Turn.Player.CROSS);
        nought.setPlayer(Turn.Player.NOUGHT);
    }

    public State(State state) {
        board = (state.getBoard().deepCopy());
        visitCount = state.getVisitCount();
        winScore = state.getWinScore();
        cross = new Bot(board);
        nought = new Bot(board);
        cross.setPlayer(Turn.Player.CROSS);
        nought.setPlayer(Turn.Player.NOUGHT);
        lastTurn = new Turn(state.lastTurn.getInnerBoard(), state.lastTurn.getInnerSquare());
    }

    public State(Board board) {
        this.board = board.deepCopy();
        cross = new Bot(board);
        nought = new Bot(board);
        cross.setPlayer(Turn.Player.CROSS);
        nought.setPlayer(Turn.Player.NOUGHT);
    }

    public Turn getLastTurn() {
        return lastTurn;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Turn.Player getPlayer() {
        return board.getCurrentPlayer();
    }

    public int getVisitCount() {
        return visitCount;
    }

    public double getWinScore() {
        return winScore;
    }

    public void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    /**
     * Returns all states approachable from this state after one turn.
     * @return list of all states
     */
    public List<State> getAllPossibleStates() {
        List<State> possibleStates = new ArrayList<>();
        List<Turn> availablePositions = BoardAnalyzer.getAvailableMoves(board);
        for (Turn turn : availablePositions) {
            State newState = new State(this.board);
            newState.getBoard().makeMove(turn);
            newState.lastTurn = turn;
            possibleStates.add(newState);
        }
        return possibleStates;
    }

    /**
     * Increments total number of visits.
     */
    public void incrementVisit() {
        this.visitCount++;
    }

    /**
     * Adds a score to an existing win score.
     * @param score is a score to be added
     */
    public void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE)
            this.winScore += score;
    }

    /**
     * Simulates random play on board using two random bots.
     */
    public void randomPlay() {
        if (board.getCurrentPlayer() == Turn.Player.CROSS) {
            board.makeMove(cross.makeTurn());
        } else {
            board.makeMove(nought.makeTurn());
        }
    }
}

