package ru.spbau.tictactoe.AnotherPlayer.Bot;

import java.util.Random;

import ru.spbau.tictactoe.AnotherPlayer.AnotherPlayer;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class Bot implements AnotherPlayer {
    private Random rand = new Random();
    private Board board;
    public Bot(Board board){
        this.board = board;
    }
    @Override
    public Turn makeTurn() {
        int cur = board.getCurrentInnerBoard();
        if(cur == -1){
            while(cur == -1 || board.getBlockStatus(cur) != Status.GAME_CONTINUES){
                cur = rand.nextInt(9);
            }
        }
        int x = rand.nextInt(9);
        while(board.getSquare(cur, 9) != Status.GAME_CONTINUES){
            x = rand.nextInt(9);
        }
        return new Turn(cur, x);
    }

    @Override
    public String getName() {
        return "Robert";
    }
}
