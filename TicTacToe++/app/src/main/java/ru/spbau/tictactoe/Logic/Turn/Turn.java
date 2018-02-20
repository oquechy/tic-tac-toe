package ru.spbau.tictactoe.Logic.Turn;


/**
 * Class which keeps the id of the inner board
 * and the inner square where the turn is to be made.
 */
public class Turn {
    public enum Player {
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
