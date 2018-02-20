package ru.spbau.tictactoe;

import ru.spbau.tictactoe.Logic.Turn.Turn;

public class UITurn {
    private boolean cross;
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCross() {
        return cross;
    }

    public UITurn(boolean isCross, int x, int y) {
        this.cross = isCross;
        this.x = x;
        this.y = y;
    }

    public UITurn(Turn turn) {
        cross = false;
        x = (turn.getInnerBoard() % 3) * 3 + turn.getInnerSquare() % 3;
        y = (turn.getInnerBoard() / 3) * 3 + turn.getInnerSquare() / 3;
    }

    public Turn convertToTurn() {
        int[][] bigBoard = new int[9][9];
        int[][] littleBoard = new int[9][9];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        bigBoard[i * 3 + x][j * 3 + y] = i * 3 + j;
                        littleBoard[i * 3 + x][j * 3 + y] = x * 3 + y;
                    }
                }
            }
        }

        return new Turn(bigBoard[y][x], littleBoard[y][x]);
    }

    @Override
    public String toString() {
        return (cross ? "1" : "0") + " " + x + " " + y;
    }

    public static UITurn fromString(String s) {
        String[] tokens = s.split(" ");
        return new UITurn(Integer.parseInt(tokens[0]) != 0,
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]));
    }
}
