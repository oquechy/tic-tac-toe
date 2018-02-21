package ru.spbau.tictactoe;

import android.os.AsyncTask;

import ru.spbau.tictactoe.Network.NetAnotherPlayer;

/**
 * Class for making bot's computations in another thread.
 */
public class TurnTransmitter extends AsyncTask<NetAnotherPlayer, Void, UITurn> {

    @Override
    protected UITurn doInBackground(NetAnotherPlayer... friends) {
        return friends[0].getOpponentTurn();
    }

    @Override
    protected void onPostExecute(UITurn uiTurn) {
        Controller.setOpponentTurn(uiTurn);
    }
}
