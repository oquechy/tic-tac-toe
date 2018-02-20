package ru.spbau.tictactoe.Network;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.SocketCreator;
import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;

public class Client extends Connection {

    private Socket socket;

    private Socket createSocket(String... params) {
        try {
            String hostName = params[0];
            int portNumber = Integer.parseInt(params[1]);
            Socket socket =  new Socket();
            socket.connect(new InetSocketAddress(hostName, portNumber), 1000);
            return socket;
        } catch (IOException e) {
            return null;
        }
    }


    public void start(String clientName, String hostName, String portNumber)
            throws IOException, ExecutionException, InterruptedException {
        socket = isMainThread() ? new SocketCreator().execute(hostName, portNumber).get()
                : createSocket(hostName, portNumber);

        if (socket == null) {
            throw new ConnectException();
        }

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("ClientName: " + clientName);
        passTo(clientName);


    }

}