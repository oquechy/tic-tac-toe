package ru.spbau.tictactoe.Network;

import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;
import ru.spbau.tictactoe.Turn;

public class Connection {
    protected PrintWriter out;
    protected BufferedReader in;

    protected void asyncPassTo(String s) {
        System.out.println(this.getClass() + " passed: " + s);
        new SocketWriter(out).execute(s);
    }

    protected void directPassTo(String s) {
        System.out.println(this.getClass() + " passed: " + s);
        out.println(s);
    }

    protected String asyncGetFrom() {
        String s = null;
        try {
            s = new SocketReader().execute(in).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(this.getClass() + " got: " + s);
        return s;
    }

    protected String directGetFrom() {
        try {
            String s = in.readLine();
            System.out.println(this.getClass() + " got: " + s);
            return s;
        } catch (IOException e) {
            return null;
        }
    }

    protected boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public NetAnotherPlayer getPlayer() {
        return new NetAnotherPlayer() {
            String name;

            @Override
            public void setOpponentTurn(Turn turn) {
                String stringTurn = turn.toString();
                passTo(stringTurn);
            }

            @Override
            public Turn getOpponentTurn() {
                Turn turn = null;
                while (turn == null) {
                    try {
                        turn = Turn.fromString(getFrom());
                    } catch (NumberFormatException ignored) { }
                }
                return turn;
            }

            @Override
            public boolean choosePlayer() {
                return Boolean.parseBoolean(getFrom());
            }

            @Override
            public String getName() {
                return name == null ?
                        name = getFrom() : name;
            }

            @Override
            public void receivePlayer(boolean b) {
                String player = String.valueOf(b);
                passTo(player);
            }
        };
    }

    public String getFrom() {
        return isMainThread() ? asyncGetFrom() : directGetFrom();
    }

    public void passTo(String string) {
        if (isMainThread()) {
            asyncPassTo(string);
        } else {
            directPassTo(string);
        }
    }
}
