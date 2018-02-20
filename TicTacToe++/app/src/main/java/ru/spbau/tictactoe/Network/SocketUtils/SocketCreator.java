package ru.spbau.tictactoe.Network.SocketUtils;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

public class SocketCreator extends AsyncTask<String, Void, Socket> {

    protected Socket doInBackground(String... params) {
        try {
            String hostName = params[0];
            int portNumber = Integer.parseInt(params[1]);
            return new Socket(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}