package ru.spbau.tictactoe;

public interface AnotherPlayer {
    void setOpponentTurn(Turn turn);

    Turn getOpponentTurn();

    public boolean getFirstPlayer();

    String getName();
}
