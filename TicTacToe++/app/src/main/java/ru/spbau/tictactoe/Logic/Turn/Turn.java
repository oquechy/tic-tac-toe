package ru.spbau.tictactoe.Logic.Turn;


import java.io.Serializable;

public class Turn implements Serializable {
    public enum Player implements Serializable {
        CROSS, NOUGHT;

        public Player opponent() {
            return this == CROSS ? NOUGHT : CROSS;
        }
    }

    public Turn(int innerBoard, int innerSquare) {
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

    @Override
    public int hashCode() {
        return innerBoard * 100 + innerSquare;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Turn && innerSquare == ((Turn) o).innerSquare
                && innerBoard == ((Turn) o).innerBoard;

    }
}
