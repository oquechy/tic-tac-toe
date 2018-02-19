package ru.spbau.tictactoe.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ru.spbau.tictactoe.Controller;
import ru.spbau.tictactoe.UITurn;


public class LinuxClient {

    public static void main(String[] args) throws IOException {
        new LinuxClient().start();
    }

    public void start() throws IOException {

        String hostName = "192.168.1.39";
        int portNumber = 3030;

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()));
        ) {
//            BufferedReader stdIn =
//                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            fromUser = "Client";
            System.out.println("LinuxClient: " + fromUser);
            out.println(fromUser);


            Random random = new Random();

            for (int i = 0; i < 10000; ++i) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fromUser = new UITurn(false, Math.abs(random.nextInt() % 9), Math.abs(random.nextInt() % 9)).toString();
                System.out.println("LinuxClient: " + fromUser);
                out.println(fromUser);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            e.printStackTrace();
//            start();
        }
    }
}
