package ru.spbau.tictactoe.Logic.Board;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import static ru.spbau.tictactoe.Logic.Board.Status.DRAW;
import static ru.spbau.tictactoe.Logic.Board.Status.GAME_CONTINUES;

/**
 * General class for board. Also used for square because inner board is both board and square and
 * there is no multiple inheritance in Java.
 */
public class AbstractBoard implements Cloneable {
    /**
     * Game status on this board or square.
     * There are four opportunities:
     * the game continues - GAME_CONTINUES,
     * the first player won - CROSS,
     * the second player won - NOUGHT,
     * nobody won but the moves cannot be made - BLOCK.
     */
    protected Status status;
    /**
     * Squares of this board. All squares are null, if it is a square of the inner board.
     */
    protected AbstractBoard[] board;

    AbstractBoard(){
        status = GAME_CONTINUES;
        board = new AbstractBoard[9];
    }

    /**
     * Copy constructor
     * @param other is an object which deep copy is to be created
     */
    AbstractBoard(AbstractBoard other){
        assert other != null;
        assert other.status != null;
        status = other.status;
        if(other.board != null){
            board = new AbstractBoard[9];
            for(int i = 0; i < 9; i++){
                if(other.board[i] != null){
                    board[i] = new AbstractBoard(other.board[i]);
                }
            }
        }
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
        return board[4].status != GAME_CONTINUES
                && board[4].status != DRAW
                && (board[0].status == board[4].status
                && board[4].status == board[8].status
                || board[2].status == board[4].status
                && board[4].status == board[6].status);
    }

    public Status getStatus() {
        return status;
    }

    public AbstractBoard getBox(int index){
        return board[index];
    }
}
