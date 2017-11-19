package ru.spbau.tictactoe.Network.SocketUtils;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketCreator extends AsyncTask<Integer, Void, Socket> {

    Exception exception;

    @Override
    protected Socket doInBackground(Integer... portNumber) {
        ServerSocket serverSocket;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber[0]);
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            exception = e;
        }
        return clientSocket;
    }
}
