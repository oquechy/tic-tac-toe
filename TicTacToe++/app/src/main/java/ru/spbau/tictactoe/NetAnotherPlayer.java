package ru.spbau.tictactoe;

public interface NetAnotherPlayer {
    void setOpponentTurn(Turn turn);

    Turn getOpponentTurn();

    public boolean getFirstPlayer();

    String getName();
}
