package ru.spbau.tictactoe;

public class Turn {
    boolean player;
    int x;
    int y;

    public Turn(boolean player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public Turn(ru.spbau.tictactoe.Logic.Turn.Turn turn) {
        player = false;
        x = turn.getInnerBoard() % 3 * 3 + turn.getInnerSquare() % 3;
        y = turn.getInnerBoard() / 3 * 3 + turn.getInnerSquare() / 3;
    }

    public ru.spbau.tictactoe.Logic.Turn.Turn convertToTurn() {
        int[][] bigBoard = new int[9][9];
        int[][] littleBoard = new int[9][9];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        bigBoard[i * 3 + x][j * 3 + y] = i * 3 + j;
                        littleBoard[i * 3 + x][j * 3 + j] = x * 3 + y;
                    }
                }
            }
        }

        return new ru.spbau.tictactoe.Logic.Turn.Turn(bigBoard[x][y], littleBoard[x][y]);
    };

    @Override
    public String toString() {
        return (player ? "1" : "0") + " " + x + " " + y;
    }

    public static Turn fromString(String s) {
        String[] tokens = s.split(" ");
        return new Turn(Integer.parseInt(tokens[0]) != 0,
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2]));
    }
}
