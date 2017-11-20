package ru.spbau.tictactoe.Network;

import ru.spbau.tictactoe.Turn;

public interface NetAnotherPlayer {
    void setOpponentTurn(Turn turn);

    Turn getOpponentTurn();

    public boolean getFirstPlayer();

    String getName();
}
