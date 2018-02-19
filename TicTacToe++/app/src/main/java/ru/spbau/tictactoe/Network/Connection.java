package ru.spbau.tictactoe.Network;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Network.SocketUtils.SocketReader;
import ru.spbau.tictactoe.Network.SocketUtils.SocketWriter;
import ru.spbau.tictactoe.UITurn;

public class Connection {
    PrintWriter out;
    BufferedReader in;

    public void passTo(String s) {
        System.out.println(this.getClass() + " send: " + s);
        new SocketWriter(out).execute(s);
    }

    public String getFrom() {
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

    public NetAnotherPlayer getPlayer(final String name) {
        return new NetAnotherPlayer() {
            @Override
            public void setOpponentTurn(UITurn turn) {
                passTo(turn.toString());
            }

            @Override
            public UITurn getOpponentTurn() {
                return UITurn.fromString(getFrom());
            }

            @Override
            public boolean amIFirstPlayer() {
                return Boolean.parseBoolean(getFrom());
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
