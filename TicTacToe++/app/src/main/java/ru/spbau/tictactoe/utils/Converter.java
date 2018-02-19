package ru.spbau.tictactoe.utils;

import ru.spbau.tictactoe.UITurn;

/**
 * Class with utils for transmitting information
 * from UI to Logic and back.
 */
public class Converter {
    public static int getX(UITurn newTurn) {
        return newTurn.getX() + 1;
    }

    public static int getXOfBoard(int littleWinCoords) {
        return littleWinCoords % 3 + 1;
    }

    public static int getYOfBoard(int littleWinCoords) {
        return littleWinCoords / 3 + 1;
    }

    public static int getYOfNextBoard(UITurn turn) {
        return turn.getY() % 3 + 1;
    }

    public static int getXOfNextBoard(UITurn turn) {
        return turn.getX() % 3 + 1;
    }

    public static int getY(UITurn turn) {
        return turn.getY() + 1;
    }
}
