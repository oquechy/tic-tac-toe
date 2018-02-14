package ru.spbau.tictactoe.Network;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.ServerSocketCreator;
import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;

public class Server extends Connection {

    Socket clientSocket;

    Socket getClientSocket(int portNumber) {
        System.err.println("in server creator method");
        ServerSocket serverSocket;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientSocket;
    }

    protected String readFromSocket(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void start(String serverName, int portNumber) throws IOException, ExecutionException, InterruptedException {

        clientSocket = isMainThread() ? new ServerSocketCreator().execute(portNumber).get()
                : getClientSocket(portNumber);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


//        String clientName;

        passTo(serverName);
        System.out.println("ServerName: " + serverName);
//        clientName = in.readLine();
    }

}