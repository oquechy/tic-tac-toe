package ru.spbau.tictactoe.Logic.Board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import ru.spbau.tictactoe.Logic.Turn.Turn;

import static ru.spbau.tictactoe.Logic.Board.Status.DRAW;
import static ru.spbau.tictactoe.Logic.Board.Status.GAME_CONTINUES;


/**
 * Class which represents board where game goes.
 * The board consists of a grid of 9 blocks.
 * Each block contains 9 boxes arranged in 3 rows and 3 columns and
 * could be considered as a standard tic-tac-toe board.
 * At any moment there is a block where the next move will happen (initially it is the medium block).
 */
public class Board extends AbstractBoard {

    /**
     * An outer square or an inner board.
     */
    public static class InnerBoard extends AbstractBoard {

        /**
         * Initializes the values of squares in innerBoard with GAME_CONTINUES, as they should be empty in
         * the beginning of game.
         */
        private InnerBoard() {
            super();
            for (int i = 0; i < 9; i++) {
                board[i] = new AbstractBoard();
            }
        }

        private InnerBoard(InnerBoard other) {
            super(other);
        }

        /**
         * Changes a status of empty square to the player which makes a move,
         * then checks if the game on this block is over or the block is completely filled.
         *
         * @param squareId is a number square from 0 to 8 to be changed
         * @param player   is a player who makes move (CROSS or NOUGHT)
         */
        public boolean setSquare(int squareId, Turn.Player player) {
            board[squareId].status = Status.playerToStatus(player);
            numberOfMarkedSquares++;
            if (isOver()) {
                status = Status.playerToStatus(player);
                return true;
            }
            if (numberOfMarkedSquares == 9) {
                status = DRAW;
                return true;
            }
            return false;
        }

        /**
         * Makes the specified square empty. Used in class Bot and its derived classes to calculate the turn.
         *
         * @param squareId is a square to be freed
         */
        public void discardChanges(int squareId) {
            if (board[squareId].status == GAME_CONTINUES) {
                return;
            }
            board[squareId].status = GAME_CONTINUES;
            numberOfMarkedSquares--;
            status = GAME_CONTINUES;
        }

        public Status getSquare(int squareId) {
            return board[squareId].status;
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(Board.class);

    /**
     * The inner board where the next will be made.
     * Initially, it is the block in the center.
     * After each move the currentInnerBoard changes to the id
     * of the inner square that was changed during the move.
     * If the relevant block is busy (somebody won there or it is completely filled), it becomes -1,
     * which means that the next move can be made to any block.
     */
    private int currentInnerBoard = 4;

    /**
     * The player who makes next move.
     */
    private Turn.Player currentPlayer = Turn.Player.CROSS;

    public Turn.Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board() {
        super();
        for (int i = 0; i < 9; i++) {
            board[i] = new InnerBoard();
        }
    }

    public Board(Board other) {
        super();
        status = other.status;
        currentPlayer = other.currentPlayer;
        currentInnerBoard = other.currentInnerBoard;
        numberOfMarkedSquares = other.numberOfMarkedSquares;
        for (int i = 0; i < 9; i++) {
            assert other.board[i] != null;
            board[i] = new InnerBoard((InnerBoard) other.board[i]);
        }
    }

    public int getCurrentInnerBoard() {
        return currentInnerBoard;
    }

    public Status getSquare(int outerSquare, int innerSquare) {
        return board[outerSquare].board[innerSquare].status;
    }

    public Status getBlockStatus(int outerSquare) {
        return board[outerSquare].status;
    }

    public InnerBoard[] getBoard() {
        return Arrays.copyOf(board, board.length, InnerBoard[].class);
    }

    /**
     * Makes the turn on board.
     *
     * @param turn is a turn to be made.
     * @return the status of the inner board after the move.
     * @throws IncorrectMoveException if the current inner board is undefined or a given square is busy
     */
    public Status makeMove(Turn turn) {
        if (!verifyTurn(turn)) {
            throw new IncorrectMoveException();
        }
        int innerSquare = turn.getInnerSquare();
        int block = turn.getInnerBoard();
        boolean blockIsOver = ((InnerBoard) board[block])
                .setSquare(innerSquare, currentPlayer);
        if (blockIsOver) {
            numberOfMarkedSquares++;
        }
        if (board[innerSquare].status == GAME_CONTINUES) {
            currentInnerBoard = innerSquare;
        } else {
            currentInnerBoard = -1;
        }
        if (isOver()) {
            status = Status.playerToStatus(currentPlayer);
        } else {
            if (numberOfMarkedSquares == 9) {
                status = DRAW;
            }
        }
        currentPlayer = currentPlayer.opponent();
        return board[block].status;
    }

    /**
     * Sets the current player's sign on a given inner square of the current inner board.
     *
     * @param innerSquare is an inner square on current inner board which status is to be changed.
     * @throws IncorrectMoveException if the current inner board is undefined or a given square is busy
     */
    public void makeMove(int innerSquare) {
        if (currentInnerBoard == -1) {
            throw new IncorrectMoveException();
        }
        makeMove(new Turn(currentInnerBoard, innerSquare));
    }

    /**
     * Checks if the given turn is valid.
     *
     * @param turn is the turn to be checked
     * @return true if the turn is valid, false otherwise
     */
    public boolean verifyTurn(Turn turn) {
        return (currentInnerBoard == -1 || turn.getInnerBoard() == currentInnerBoard)
                && board[turn.getInnerBoard()].getStatus() == GAME_CONTINUES
                && ((InnerBoard) board[turn.getInnerBoard()])
                .getSquare(turn.getInnerSquare()) == GAME_CONTINUES;
    }

    /**
     * Creates a copy of this board without any shared references.
     *
     * @return a copy of this board
     */
    public Board deepCopy() {
        return new Board(this);
    }

    /**
     * Annuls the last turn.
     *
     * @param turn       is the turn to annulled.
     * @param innerBoard is an inner board number to be set as a current inner board.
     */
    public void discardChanges(Turn turn, int innerBoard) {
        status = GAME_CONTINUES;
        if (board[turn.getInnerBoard()].getStatus() != Status.GAME_CONTINUES) {
            numberOfMarkedSquares--;
        }
        ((InnerBoard) board[turn.getInnerBoard()]).
                discardChanges(turn.getInnerSquare());
        currentInnerBoard = innerBoard;
        currentPlayer = currentPlayer.opponent();
    }

    @Override
    public void clear() {
        super.clear();
        currentInnerBoard = 4;
        currentPlayer = Turn.Player.CROSS;
    }

}
