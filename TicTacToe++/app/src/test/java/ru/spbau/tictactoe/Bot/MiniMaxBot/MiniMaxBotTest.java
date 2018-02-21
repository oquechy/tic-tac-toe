package ru.spbau.tictactoe.Bot.MiniMaxBot;

import org.junit.Test;

import ru.spbau.tictactoe.Bot.BoardAnalyzer;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;

import static org.junit.Assert.*;


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
            board.discardChanges(turn, 4);
        }

    }

    @Test
    public void preventOpponentFromWinOnSmallBoard(){
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
        assertFalse(board.getCurrentInnerBoard() == 6);
        assertFalse(board.getCurrentInnerBoard() == 7);
    }

    @Test
    public void statusToSign(){
        Board board = new Board();
        MiniMaxBot bot = new MiniMaxBot(board);
        assertEquals(1, bot.statusSign(Status.NOUGHT));
    }



}