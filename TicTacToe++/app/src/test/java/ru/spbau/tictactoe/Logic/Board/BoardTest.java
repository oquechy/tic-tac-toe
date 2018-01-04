package ru.spbau.tictactoe.Logic.Board;

import org.junit.Test;

import java.util.Scanner;

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
    public void makeMoveToAnyOuterSquare() throws Exception {

    }

    @Test
    public void getSquare() throws Exception {

    }

    @Test
    public void getBlockStatus() throws Exception {

    }

    @Test
    public void getGameStatus() throws Exception {

    }

    @Test
    public void isOver() throws Exception {
        Board board = new Board();
        assertFalse(board.isOver());
    }

}