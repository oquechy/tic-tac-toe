package ru.spbau.tictactoe;
/*
import ru.spbau.tictactoe.Bot;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class CleverBot extends Bot {
    public CleverBot(Board board){
        super(board);
    }

    protected int analyzeBlock(int block){
        for()
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
        while(board.getSquare(cur, x) != Status.GAME_CONTINUES){
            x = rand.nextInt(9);
        }
        return new Turn(cur, x);
    }
}
*/