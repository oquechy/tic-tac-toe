package ru.spbau.tictactoe.Logic.Board;


import static ru.spbau.tictactoe.Logic.Board.Status.DRAW;
import static ru.spbau.tictactoe.Logic.Board.Status.GAME_CONTINUES;

public abstract class AbstractBoard {
    protected Status status = GAME_CONTINUES;
    protected AbstractBoard[] board;
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

    public Status getStatus() {
        return status;
    }
}
