package ru.spbau.tictactoe.Logic.Turn;


public class Turn implements Cloneable {
    public enum Player implements Cloneable {
        CROSS, NOUGHT;
        public Player opponent(){
            return this == CROSS ? NOUGHT : CROSS;
        }
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
