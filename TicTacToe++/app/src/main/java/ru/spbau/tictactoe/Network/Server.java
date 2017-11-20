package ru.spbau.tictactoe.Network;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.ServerSocketCreator;
import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;

public class Server extends Connection {

    Socket clientSocket;

    public void start(String serverName, int portNumber) throws IOException {

        try {
            clientSocket = new ServerSocketCreator().execute(portNumber).get();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            String clientName;

            new SocketWriter(out).execute(serverName);
            System.out.println("ServerName: " + serverName);
            clientName = new SocketReader().execute(in).get();
            System.out.println("ClientName: " + clientName);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}