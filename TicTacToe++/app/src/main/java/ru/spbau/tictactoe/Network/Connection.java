package ru.spbau.tictactoe.Network;

import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;
import ru.spbau.tictactoe.Turn;

public class Connection {
    PrintWriter out;
    BufferedReader in;

    public void asyncPassTo(String s) {
        System.out.println(this.getClass() + " send: " + s);
        new SocketWriter(out).execute(s);
    }

    public void directPassTo(String s) {
        System.out.println("Server passed: " + s);
        out.println(s);
    }

    public String asyncGetFrom() {
        String s = null;
        try {
            s = new SocketReader().execute(in).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass() + " get: " + s);
        return s;
    }

    public String directGetFrom() {
        try {
            String s = in.readLine();
            System.out.println("Server got: " + s);
            return s;
        } catch (IOException e) {
            return null;
        }
    }

//    public NetAnotherPlayer getAnotherPlayer(final String name) {
//        return new NetAnotherPlayer() {
//            @Override
//            public void setOpponentTurn(Turn turn) {
//                passTo(turn.toString());
//            }
//
//            @Override
//            public Turn getOpponentTurn() {
//                return Turn.fromString(getFrom());
//            }
//
//            @Override
//            public boolean getFirstPlayer() {
//                return Boolean.parseBoolean(getFrom());
//            }
//
//            @Override
//            public String getName() {
//                return name;
//            }
//        };
//    }

    public NetAnotherPlayer getClientPlayer(final String name) {
        return new NetAnotherPlayer() {
            @Override
            public void setOpponentTurn(Turn turn) {
                String stringTurn = turn.toString();
                boolean mainThread = Looper.myLooper() == Looper.getMainLooper();
                if (mainThread) {
                    asyncPassTo(stringTurn);
                } else {
                    directPassTo(stringTurn);
                }
            }

            @Override
            public Turn getOpponentTurn() {
                boolean mainThread = Looper.myLooper() == Looper.getMainLooper();
                return Turn.fromString(mainThread ? asyncGetFrom() : directGetFrom());
            }

            @Override
            public boolean getFirstPlayer() {
                return Boolean.parseBoolean(directGetFrom());
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
