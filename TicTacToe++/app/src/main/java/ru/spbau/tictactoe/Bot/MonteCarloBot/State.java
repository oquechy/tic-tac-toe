package ru.spbau.tictactoe.Bot.MonteCarloBot;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.tictactoe.Bot.BoardAnalyzer;
import ru.spbau.tictactoe.Bot.Bot;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * A class which represents a state of the game for a certain node.
 * This class is used only for MonteCarloBot, so class and all methods are package private.
 */
class State {
    private Board board;
    private Turn lastTurn;
    private int visitCount;
    private double winScore;
    private Bot cross;
    private Bot nought;

    State() {
        board = new Board();
        cross = new Bot(board);
        nought = new Bot(board);
        cross.setPlayer(Turn.Player.CROSS);
        nought.setPlayer(Turn.Player.NOUGHT);
    }

    State(State state) {
        board = (state.getBoard().deepCopy());
        visitCount = state.getVisitCount();
        winScore = state.getWinScore();
        cross = new Bot(board);
        nought = new Bot(board);
        cross.setPlayer(Turn.Player.CROSS);
        nought.setPlayer(Turn.Player.NOUGHT);
        lastTurn = new Turn(state.lastTurn.getInnerBoard(), state.lastTurn.getInnerSquare());
    }

    private State(Board board) {
        this.board = board.deepCopy();
        cross = new Bot(board);
        nought = new Bot(board);
        cross.setPlayer(Turn.Player.CROSS);
        nought.setPlayer(Turn.Player.NOUGHT);
    }

    Turn getLastTurn() {
        return lastTurn;
    }

    Board getBoard() {
        return board;
    }

    void setBoard(Board board) {
        this.board = board;
    }

    Turn.Player getPlayer() {
        return board.getCurrentPlayer();
    }

    int getVisitCount() {
        return visitCount;
    }

    double getWinScore() {
        return winScore;
    }

    void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    /**
     * Returns all states approachable from this state after one turn.
     * @return list of all states
     */
    List<State> getAllPossibleStates() {
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
    void incrementVisit() {
        this.visitCount++;
    }

    /**
     * Adds a score to an existing win score.
     * @param score is a score to be added
     */
    void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE)
            this.winScore += score;
    }

    /**
     * Simulates random play on board using two random bots.
     */
    void randomPlay() {
        if (board.getCurrentPlayer() == Turn.Player.CROSS) {
            board.makeMove(cross.makeTurn());
        } else {
            board.makeMove(nought.makeTurn());
        }
    }
}

