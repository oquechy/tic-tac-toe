package ru.spbau.tictactoe.Network.SocketUtils;

import android.os.AsyncTask;

import java.io.PrintWriter;

public class SocketWriter extends AsyncTask<String, Void, Void> {

    PrintWriter out;

    public SocketWriter(PrintWriter out) {
        super();
        this.out = out;
    }

    @Override
    protected Void doInBackground(String... fromUser) {
        out.println(fromUser[0]);
        return null;
    }
}
