package ru.spbau.tictactoe.Network;

import ru.spbau.tictactoe.UITurn;

public interface NetAnotherPlayer {
    void setOpponentTurn(UITurn turn);

    UITurn getOpponentTurn();

    public boolean amIFirstPlayer();

    String getName();

    void receivePlayer(boolean b);
}
