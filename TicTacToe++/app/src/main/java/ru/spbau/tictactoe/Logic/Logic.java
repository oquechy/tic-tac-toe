package ru.spbau.tictactoe.Logic;


import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Result.Result;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * Class responsible for the logic of the game.
 */
public class Logic {
    /**
     * A board for the game.
     */
    private Board board = new Board();

    /**
     * Last turn made on board.
     */
    private Turn lastTurn;

    /**
     * Status of the small board where the last turn was made.
     */
    private Status isLittleWin;

    /**
     * Number of turns made.
     */
    private int turnCounter;

    /**
     * Checks if the given turn could be applied on board.
     *
     * @param turn is a turn to be checked
     * @return true if the turn is correct
     */
    public boolean verifyTurn(Turn turn) {
        return board.verifyTurn(turn);
    }

    /**
     * Applies opponent's turn on board.
     *
     * @param turn is a turn to be applied
     */
    public void applyOpponentsTurn(Turn turn) {
        lastTurn = turn;
        isLittleWin = board.makeMove(turn);
    }

    /**
     * Applies player's turn on board
     *
     * @param turn is a turn to be applied
     */
    public void applyMyTurn(Turn turn) {
        turnCounter++;
        applyOpponentsTurn(turn);
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public Status isLittleWin() {
        return isLittleWin;
    }

    public Result getResult() {
        if (board.getStatus() == Status.CROSS) {
            return Result.CROSS;
        }
        if (board.getStatus() == Status.NOUGHT) {
            return Result.NOUGHT;
        }
        return Result.DRAW;
    }

    public int getLittleWinCoords() {
        return lastTurn.getInnerBoard();
    }

    public boolean isEndOfGame() {
        return board.getStatus() != Status.GAME_CONTINUES;
    }


    public Board getBoard() {
        return board;
    }

    public void reset() {
        board = new Board();
    }

    public Status getStatusOfInner(int block) {
        return board.getBlockStatus(block);
    }
}
