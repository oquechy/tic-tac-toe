package ru.spbau.tictactoe.Logic.Turn;


public class Turn {
    public enum Player {
        CROSS, NOUGHT;
    }

    public Turn(int innerBoard, int innerSquare){
        this.innerBoard = innerBoard;
        this.innerSquare = innerSquare;
    }

    private int innerBoard;
    private int innerSquare;

    public int getInnerBoard() {
        return innerBoard;
    }

    public int getInnerSquare() {
        return innerSquare;
    }
}
