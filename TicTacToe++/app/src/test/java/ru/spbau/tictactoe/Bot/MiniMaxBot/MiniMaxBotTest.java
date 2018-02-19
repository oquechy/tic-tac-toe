package ru.spbau.tictactoe.Bot.MiniMaxBot;

import org.junit.Test;

import ru.spbau.tictactoe.Bot.BoardAnalyzer;
import ru.spbau.tictactoe.Bot.MiniMaxBot.MiniMaxBot;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class MiniMaxBotTest {
    @Test
    public void calculateScoreAfterOneMove(){
        Board board = new Board();
        MiniMaxBot bot = new MiniMaxBot(board);
        board.makeMove(6);
        System.out.println(bot.score(board));
    }

    @Test
    public void calculateScoreAfterTwoMoves(){
        Board board = new Board();
        MiniMaxBot bot = new MiniMaxBot(board);
        board.makeMove(6);
        for(int i = 0; i < 1; i++){
            board.makeMove(i);
            System.out.println(bot.score(board));
            BoardAnalyzer.printBoard(board);
            board.discardChanges(new Turn(6, i), 6);
        }
    }

    @Test
    public void calculateScoreWithOneInnerBoardWon() {
        Board board = new Board();
        MiniMaxBot bot = new MiniMaxBot(board);
        board.makeMove(6);
        board.makeMove(4);
        board.makeMove(2);
        board.makeMove(4);
        for(Turn turn : BoardAnalyzer.getAvailableMoves(board)){
            board.makeMove(turn);
            System.out.println(bot.score(board));
            BoardAnalyzer.printBoard(board);
            board.discardChanges(turn, 4);
        }

    }

    @Test
    public void situation1(){
        Board board = new Board();
        MiniMaxBot bot = new MiniMaxBot(board);
        board.makeMove(0);
        board.makeMove(6);
        board.makeMove(0);
        board.makeMove(7);
        board.makeMove(2);
        board.makeMove(6);
        board.makeMove(3);
        board.makeMove(7);
        board.makeMove(4);
        board.makeMove(3);
        board.makeMove(8);
        board.makeMove(bot.makeTurn());
        BoardAnalyzer.printBoard(board);
        /*board.makeMove(5);
        int min = Integer.MAX_VALUE;
        for(Turn turn : bot.getAvailableMoves()){
            board.makeMove(turn);
            int score = bot.score(board);
            System.out.println(score);
            if(score < min){
                min = score;
            }
            CleverBot.printBoard(board);
            board.discardChanges(turn, 5);
        }*/
        //System.out.println(min);
    }

    @Test
    public void situation2(){
        Board board = new Board();
        MiniMaxBot bot = new MiniMaxBot(board);
        board.makeMove(5);
        board.makeMove(6);
        board.makeMove(0);
        board.makeMove(7);
        board.makeMove(2);
        board.makeMove(6);
        board.makeMove(3);
        board.makeMove(7);
        board.makeMove(4);
        board.makeMove(3);
        board.makeMove(8);
        board.makeMove(bot.makeTurn());
        BoardAnalyzer.printBoard(board);
        /*board.makeMove(5);
        int min = Integer.MAX_VALUE;
        for(Turn turn : bot.getAvailableMoves()){
            board.makeMove(turn);
            int score = bot.score(board);
            System.out.println(score);
            if(score < min){
                min = score;
            }
            CleverBot.printBoard(board);
            board.discardChanges(turn, 5);
        }*/
        //System.out.println(min);
    }

}