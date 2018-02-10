package ru.spbau.tictactoe.Logic.Board;

import org.junit.Test;

import java.util.Scanner;

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
}