package ru.spbau.tictactoe.Network;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.SocketCreator;
import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;

public class Client extends Connection {

    Socket socket;

    public void start(String clientName, String hostName, String portNumber) throws IOException {
        try {
            socket = new SocketCreator().execute(new String[]{hostName, portNumber}).get();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverName;

            serverName = new SocketReader().execute(in).get();
            System.out.println("ServerName: " + serverName);

            System.out.println("ClientName: " + clientName);
            new SocketWriter(out).execute(clientName);


        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
    }

}