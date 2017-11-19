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
