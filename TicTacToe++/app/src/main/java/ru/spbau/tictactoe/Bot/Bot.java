package ru.spbau.tictactoe.Bot;

import java.util.Random;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * First version of Bot
 * Makes random turns.
 */
public class Bot {
    protected Random rand = new Random();
    protected Board board;
    public Bot(Board board){
        this.board = board;
    }

    /**
     * Returns the turn to make on board.
     * @return the turn to make on board
     */
    public Turn makeTurn() {
        int cur = board.getCurrentInnerBoard();
        if(cur == -1){
            while(cur == -1 || board.getBlockStatus(cur) != Status.GAME_CONTINUES){
                cur = rand.nextInt(9);
            }
        }
        int x = rand.nextInt(9);
        while(board.getSquare(cur, x) != Status.GAME_CONTINUES){
            x = rand.nextInt(9);
        }
        return new Turn(cur, x);
    }

    public String getName() {
        return "Robert";
    }
}
