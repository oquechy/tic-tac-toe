package ru.spbau.tictactoe.Network.SocketUtils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;

public class SocketReader extends AsyncTask<BufferedReader, Void, String> {

    private IOException exception;

    @Override
    protected String doInBackground(BufferedReader... in) {
        try {
            return in[0].readLine();
        } catch (IOException e) {
            exception = e;
            return "fail in socket reader";
        }
    }
}
