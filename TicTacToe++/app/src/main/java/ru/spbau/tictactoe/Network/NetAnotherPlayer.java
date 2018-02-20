package ru.spbau.tictactoe.Network;

import ru.spbau.tictactoe.UITurn;

public interface NetAnotherPlayer {
    void setOpponentTurn(UITurn turn);

    UITurn getOpponentTurn();

    boolean amIFirstPlayer();

    String getName();

    void receivePlayer(boolean b);
}
