package ru.spbau.tictactoe.Network;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.ServerSocketCreator;
import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;

public class LinuxServer {

    public static void main(String[] args) throws IOException {
                ServerSocket serverSocket = new ServerSocket(3030);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                String clientName;

                out.println("LinuxServer");
                System.out.println("ServerName: " + "LinuxServer");
                clientName = in.readLine();
                System.out.println("ClientName: " + clientName);

                for (int i = 0; i < 10; ++i) {
                    out.println("1 2 2");
                }
        }

    }