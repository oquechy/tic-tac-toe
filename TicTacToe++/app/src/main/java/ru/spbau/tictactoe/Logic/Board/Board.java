package ru.spbau.tictactoe.Logic.Board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class Board extends AbstractBoard implements Serializable {

    /**
     * An outer square or an inner board.
     */
    public static class InnerBoard extends AbstractBoard implements Serializable {
        /**
         * Number of squares on inner board that are not empty.
         */
        private int numberOfMarkedSquares;

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
            board[squareId].status = GAME_CONTINUES;
            numberOfMarkedSquares--;
            status = GAME_CONTINUES;
        }

        public Status getSquare(int squareId) {
            return board[squareId].status;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
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

    /**
     * Number of blocks where the game is over.
     */
    private int numberOfInvalidBlocks;

    public Board() {
        super();
        for (int i = 0; i < 9; i++) {
            board[i] = new InnerBoard();
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


    /**
     * Marks the specified square on current inner board with the sign of the specified player.
     *
     * @param innerSquare is the id of the square to be marked
     */
    public Status makeMove(
            int innerSquare) {
        boolean blockIsOver = ((InnerBoard) board[currentInnerBoard])
                .setSquare(innerSquare, currentPlayer);
        Status res = board[currentInnerBoard].status;
        if (blockIsOver) {
            numberOfInvalidBlocks++;
        }
        if (board[innerSquare].status == Status.GAME_CONTINUES) {
            currentInnerBoard = innerSquare;
        } else {
            currentInnerBoard = -1;
        }
        if (isOver()) {
            status = Status.playerToStatus(currentPlayer);
        } else {
            if (numberOfInvalidBlocks == 9) {
                status = DRAW;
            }
        }
        currentPlayer = currentPlayer.opponent();
        return res;
    }

    /**
     * Marks the specified square on specified inner board with the sign of the given player
     * if the current inner square is not specified yet.
     *
     * @param block       is the block id where a square will be marked
     * @param innerSquare is the id of the inner square to be marked
     */
    public Status makeMoveToAnyOuterSquare(
            int block, int innerSquare) {

        boolean blockIsOver = ((InnerBoard) board[block])
                .setSquare(innerSquare, currentPlayer);
        if (blockIsOver) {
            numberOfInvalidBlocks++;
        }
        if (board[innerSquare].status == GAME_CONTINUES) {
            currentInnerBoard = innerSquare;
        } else {
            currentInnerBoard = -1;
        }
        if (isOver()) {
            status = Status.playerToStatus(currentPlayer);
        } else {
            if (numberOfInvalidBlocks == 9) {
                status = DRAW;
            }
        }
        currentPlayer = currentPlayer.opponent();
        return board[block].status;
    }

    public Status makeMove(Turn turn){
        if(currentInnerBoard == -1){
            return makeMoveToAnyOuterSquare(turn.getInnerBoard(), turn.getInnerSquare());
        }
        return makeMove(turn.getInnerSquare());
    }

    public InnerBoard[] getBoard() {
        return Arrays.copyOf(board, board.length, InnerBoard[].class);
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
     * @return a copy of this board
     */
    public Board deepCopy(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Board boardCopy = null;
        try{
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            boardCopy = (Board) ois.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            //TODO
        }
        return boardCopy;
    }

    public void discardChanges(Turn turn, int innerBoard){
        Logger logger = LoggerFactory.getLogger(Board.class);
        //logger.debug(Integer.toString(turn.getInnerBoard()) + " " + Integer.toString(turn.getInnerSquare()));
        //logger.debug(((InnerBoard) board[turn.getInnerBoard()]).getSquare(turn.getInnerSquare()).name());
        ((InnerBoard) board[turn.getInnerBoard()]).
                discardChanges(turn.getInnerSquare());
        currentInnerBoard = innerBoard;
        currentPlayer = currentPlayer.opponent();
        //logger.debug(((InnerBoard) board[turn.getInnerBoard()]).getSquare(turn.getInnerSquare()).name());
    }

}
