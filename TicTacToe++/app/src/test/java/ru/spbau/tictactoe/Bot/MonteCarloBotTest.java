package ru.spbau.tictactoe.Bot;

import org.junit.Test;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;

import static org.junit.Assert.*;

/**
 * Created by olga on 2/10/2018.
 */
public class MonteCarloBotTest {

    @Test
    public void monteCarloTest(){
        int victories = 0;
        int draws = 0;
        for(int i = 0; i < 1; i++){
            Board board = new Board();
            Bot bot = new MonteCarloBot(board);
            Bot opponent = new MonteCarloBot(board);
            bot.player = Turn.Player.CROSS;
            while(board.getStatus() == Status.GAME_CONTINUES){
                if(board.getCurrentPlayer() == Turn.Player.CROSS){
                    board.makeMove(bot.makeTurn());
                }
                else {
                    board.makeMove(opponent.makeTurn());
                }
                BoardAnalyzer.printBoard(board);
            }
            if(board.getStatus() == Status.DRAW){
                draws++;
            }
            if(board.getStatus() == Status.CROSS){
                victories++;
            }
        }
       System.out.printf("vict = %d, draws = %d\n", victories, draws);
    }

}