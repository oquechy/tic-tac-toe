package ru.spbau.tictactoe.Logic.Board;


import java.util.Arrays;

import ru.spbau.tictactoe.Logic.Turn.Turn;

import static ru.spbau.tictactoe.Logic.Board.Status.*;


/**
 * Class which represents board where game goes.
 * The board consists of a grid of 9 blocks.
 * Each block contains 9 boxes arranged in 3 rows and 3 columns and
 * could be considered as a standard tic-tac-toe board.
 * At any moment there is a block where the next move will happen (initially it is the medium block).
 */
public class Board {

    /**
     * An outer square or an inner board.
     */
    public static class InnerBoard {
        private int numberOfMarkedSquares;
        /**
         * Each box could be empty (GAME_CONTINUES), or marked as CROSS or NOUGHT (the BLOCK is not used here).
         */
        private Status[] innerBoard = new Status[9];
        /**
         * If the game in this block is not finished, the status is GAME_CONTINUES.
         * If one of the players won on this block the status is the player who won (CROSS or NOUGHT).
         * If nobody won but all boxes are filled, the status is BLOCK.
         */
        private Status status = GAME_CONTINUES;

        /**
         * Initializes the values of squares in innerBoard with GAME_CONTINUES, as they should be empty in
         * the beginning of game.
         */
        private InnerBoard() {
            Arrays.fill(innerBoard, GAME_CONTINUES);
        }

        /**
         * Changes a status of empty square to the player which makes a move,
         * then checks if the game on this block is over or the block is completely filled.
         *
         * @param squareId is a number square from 0 to 8 to be changed
         * @param player   is a player who makes move (CROSS or NOUGHT)
         * @throws IncorrectMoveException if the square is already marked or the game on this block is over
         */
        private boolean setSquare(int squareId, Turn.Player player) throws IncorrectMoveException {
            if (status != GAME_CONTINUES) {
                throw new IncorrectMoveException("This block is invalid");
            }
            if (innerBoard[squareId] != GAME_CONTINUES) {
                throw new IncorrectMoveException("The square is busy");
            }
            innerBoard[squareId] = player == Turn.Player.CROSS ? CROSS : NOUGHT;
            numberOfMarkedSquares++;
            if (isOver()) {
                status = player == Turn.Player.CROSS ? CROSS : NOUGHT;
                return true;
            }
            if (numberOfMarkedSquares == 9) {
                status = DRAW;
                return true;
            }
            return false;
        }

        /**
         * Checks if the game on this block is over.
         * Checks every row, column and diagonal if there are three squares
         * with the same status which is not GAME_CONTINUES.
         *
         * @return true if the game on this block is over
         * (three squares in row, column or diagonal were marked by the same player)
         */
        private boolean isOver() {
            //check columns
            for (int i = 0; i < 3; i++) {
                if (innerBoard[i] != GAME_CONTINUES && innerBoard[i] == innerBoard[i + 3]
                        && innerBoard[i] == innerBoard[i + 6]) {
                    return true;
                }
            }
            //check rows
            for (int i = 0; i < 9; i += 3) {
                if (innerBoard[i] != GAME_CONTINUES && innerBoard[i] == innerBoard[i + 1]
                        && innerBoard[i] == innerBoard[i + 2]) {
                    return true;
                }
            }
            //check diagonals
            return innerBoard[4] != GAME_CONTINUES && (innerBoard[0] == innerBoard[4]
                    && innerBoard[4] == innerBoard[8] ||
                    innerBoard[2] == innerBoard[4] && innerBoard[4] == innerBoard[6]);
        }

        public Status getSquare(int squareId) {
            return innerBoard[squareId];
        }

        public Status getStatus() {
            return status;
        }
    }

    /**
     * Board consists of grid of nine inner boards.
     */
    private InnerBoard[] board = new InnerBoard[9];

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
     * There are four opportunities:
     * the game continues - GAME_CONTINUES,
     * the first player won - CROSS,
     * the second player won - NOUGHT,
     * nobody won but the moves cannot be made - BLOCK.
     */
    private Status status = GAME_CONTINUES;

    /**
     * The player who makes next move.
     */
    private Turn.Player currentPlayer = Turn.Player.CROSS;

    /**
     * Number of blocks where the game is over.
     */
    private int numberOfInvalidBlocks;

    public Board() {
        for (int i = 0; i < 9; i++) {
            board[i] = new InnerBoard();
        }
    }

    public int getCurrentInnerBoard() {
        return currentInnerBoard;
    }

    public Status getSquare(int outerSquare, int innerSquare) {
        return board[outerSquare].innerBoard[innerSquare];
    }

    public Status getBlockStatus(int outerSquare) {
        return board[outerSquare].status;
    }


    /**
     * Marks the specified square on current inner board with the sign of the specified player.
     *
     * @param innerSquare is the id of the square to be marked
     * @throws IncorrectMoveException if the inner square is busy or current square must be specified.
     */
    public boolean makeMove(
            int innerSquare) throws IncorrectMoveException {
        if (currentInnerBoard == -1) {
            throw new IncorrectMoveException(
                    "The inner board must be also specified");
        }
        boolean blockIsOver = board[currentInnerBoard].setSquare(innerSquare, currentPlayer);
        if (blockIsOver) {
            numberOfInvalidBlocks++;
        }
        if (board[innerSquare].status == Status.GAME_CONTINUES) {
            currentInnerBoard = innerSquare;
        } else {
            currentInnerBoard = -1;
        }
        if (isOver()) {
            status = currentPlayer == Turn.Player.CROSS ? CROSS : NOUGHT;
        } else {
            if (numberOfInvalidBlocks == 9) {
                status = DRAW;
            }
        }
        currentPlayer =
                currentPlayer == Turn.Player.CROSS ? Turn.Player.NOUGHT : Turn.Player.CROSS;
        return status != GAME_CONTINUES;
    }

    /**
     * Marks the specified square on specified inner board with the sign of the given player
     * if the current inner square is not specified yet.
     *
     * @param block       is the block id where a square will be marked
     * @param innerSquare is the id of the inner square to be marked
     * @throws IncorrectMoveException if the inner board could not be specified
     */
    public boolean makeMoveToAnyOuterSquare(
            int block, int innerSquare) throws IncorrectMoveException {

        if (currentInnerBoard != -1) {
            throw new IncorrectMoveException(
                    "The inner board is already assigned");
        }
        boolean blockIsOver = board[block].setSquare(innerSquare, currentPlayer);
        if (blockIsOver) {
            numberOfInvalidBlocks++;
        }
        if (board[innerSquare].status == GAME_CONTINUES) {
            currentInnerBoard = innerSquare;
        } else {
            currentInnerBoard = -1;
        }
        if (isOver()) {
            status = currentPlayer == Turn.Player.CROSS ? CROSS : NOUGHT;
        } else {
            if (numberOfInvalidBlocks == 9) {
                status = DRAW;
            }
        }
        currentPlayer = currentPlayer == Turn.Player.CROSS ? Turn.Player.NOUGHT : Turn.Player.CROSS;
        return status != GAME_CONTINUES;
    }

    public Status getGameStatus() {
        return status;
    }

    /**
     * Checks if the game on board is over.
     * Checks every row, column and diagonal if there are three squares
     * with the same status which is not GAME_CONTINUES or BLOCK.
     *
     * @return true if the game is over
     * (three squares in row, column or diagonal were marked by the same player)
     */
    public boolean isOver() {
        //check columns
        for (int i = 0; i < 3; i++) {
            if (board[i].status != GAME_CONTINUES
                    && board[i].status != DRAW
                    && board[i].status == board[i + 3].status
                    && board[i].status == board[i + 6].status) {
                return true;
            }
        }
        //check rows
        for (int i = 0; i < 9; i += 3) {
            if (board[i].status != GAME_CONTINUES
                    && board[i].status != DRAW
                    && board[i].status == board[i + 1].status
                    && board[i].status == board[i + 2].status) {
                return true;
            }
        }
        //check diagonals
        return board[4].status != GAME_CONTINUES && board[4].status != DRAW
                && (board[0].status == board[4].status
                && board[4].status == board[8].status ||
                board[2].status == board[4].status && board[4].status == board[6].status);
    }

    public InnerBoard[] getBoard() {
        return Arrays.copyOf(board, board.length);
    }

    public boolean verifyTurn(Turn turn){
        return (currentInnerBoard == -1 || turn.getInnerBoard() == currentInnerBoard) &&
                board[turn.getInnerBoard()].getSquare(turn.getInnerSquare()) == GAME_CONTINUES;
    }
}

