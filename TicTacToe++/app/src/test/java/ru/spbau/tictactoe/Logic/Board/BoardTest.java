package ru.spbau.tictactoe.Logic.Board;

import org.junit.Test;

import java.util.Scanner;

import ru.spbau.tictactoe.Bot.BoardAnalyzer;
import ru.spbau.tictactoe.Logic.Turn.Turn;

import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void getCurrentInnerBoard() throws Exception {
        Board board = new Board();
        assertEquals(4, board.getCurrentInnerBoard());
    }

    @Test
    public void makeMove() throws Exception {
        Board board = new Board();
        board.makeMove(1);
        assertEquals(Status.CROSS, board.getSquare(4, 1));
        assertEquals(1, board.getCurrentInnerBoard());
        assertEquals(Status.GAME_CONTINUES, board.getStatus());
    }


    @Test
    public void winOnSmallBoard(){
        Board board = new Board();
        board.makeMove(5);
        board.makeMove(4);
        board.makeMove(2);
        board.makeMove(4);
        board.makeMove(8);
        assertEquals(Status.CROSS, board.getBlockStatus(4));
    }

    @Test
    public void discardChanges(){
        Board board = new Board();
        board.makeMove(5);
        board.makeMove(4);
        board.makeMove(2);
        board.makeMove(4);
        board.makeMove(8);
        board.discardChanges(new Turn(4, 8), 4);
        assertEquals(Status.GAME_CONTINUES, board.getBlockStatus(4));
        assertEquals(4, board.getCurrentInnerBoard());
        assertEquals(Status.GAME_CONTINUES, board.getSquare(4, 8));
    }

    @Test
    public void deepCopyIsCopy(){
        Board board = new Board();
        board.makeMove(8);
        Board copy = board.deepCopy();
        assertEquals(Status.GAME_CONTINUES, copy.getStatus());
        assertEquals(8, copy.getCurrentInnerBoard());
        assertEquals(Turn.Player.NOUGHT, copy.getCurrentPlayer());
        for(int i = 0; i < 9; i++){
            assertEquals(((Board.InnerBoard)board.getBox(i)).getNumberOfMarkedSquares(),
                    ((Board.InnerBoard)copy.getBox(i)).getNumberOfMarkedSquares());
            assertEquals(board.getBlockStatus(i), copy.getBlockStatus(i));
            for(int j = 0; j < 9; j++){
                assertEquals(board.getSquare(i, j), copy.getSquare(i, j));
            }
        }
    }

    @Test
    public void deepCopyIsDeep(){
        Board board = new Board();
        board.makeMove(8);
        Board copy = board.deepCopy();
        board.makeMove(7);
        assertEquals(Status.NOUGHT, board.getSquare(8, 7));
        assertEquals(Status.GAME_CONTINUES, copy.getSquare(8, 7));
    }

    @Test
    public void clear(){
        Board board = new Board();
        board.makeMove(0); //cross
        board.makeMove(4); //nought
        board.makeMove(1); //cross
        board.makeMove(4); //nought
        board.makeMove(2); //cross
        board.clear();
        assertEquals(4, board.getCurrentInnerBoard());
        assertEquals(Turn.Player.CROSS, board.getCurrentPlayer());
        assertEquals(0, board.getNumberOfMarkedSquares());
        for(int i = 0; i < 9; i++){
            assertEquals(Status.GAME_CONTINUES, board.getBlockStatus(i));
            assertEquals(0, board.getNumberOfMarkedSquares());
            for(int j = 0; j < 9; j++){
                assertEquals(Status.GAME_CONTINUES, board.getSquare(i, j));
            }
        }
    }
}