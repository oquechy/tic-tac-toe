package ru.spbau.tictactoe.Bot;

import java.util.Random;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;
import ru.spbau.tictactoe.UITurn;

/**
 * First version of Bot
 * Makes random turns.
 */
public class Bot {
    protected Turn lastOpponentsTurn;
    private Random rand = new Random();
    protected final Board board;
    protected Board boardCopy;
    protected Turn.Player player = Turn.Player.NOUGHT;
    public Bot(Board board){
        this.board = board;
    }

    /**
     * Returns the turn to make on board.
     * @return the turn to make on board
     */
    public Turn makeTurn() {
        long start = System.currentTimeMillis();
        long end = start + 60;
        int cur = board.getCurrentInnerBoard();
        if(cur == -1){
            while(cur == -1 || board.getBlockStatus(cur) != Status.GAME_CONTINUES){
                if(System.currentTimeMillis() > end){
                    System.out.println(board.getCurrentInnerBoard());
                    BoardAnalyzer.printBoard(board);
                }
                cur = rand.nextInt(9);
            }
        }
        int x = rand.nextInt(9);
        while(board.getSquare(cur, x) != Status.GAME_CONTINUES){
            if(System.currentTimeMillis() > end){
                System.out.println(board.getCurrentInnerBoard());
                BoardAnalyzer.printBoard(board);
            }
            x = rand.nextInt(9);
        }
        if(System.currentTimeMillis() > end){
            System.out.println(System.currentTimeMillis() - start);
        }
        return new Turn(cur, x);
    }

    public void getTurn(UITurn turn){
       lastOpponentsTurn = turn.convertToTurn();
    }

    public String getName() {
        return "Robert";
    }

    public void setPlayer(Turn.Player player){
        this.player = player;
    }
}
