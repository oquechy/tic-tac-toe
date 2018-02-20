package ru.spbau.tictactoe.ui;

import android.app.Activity;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

public class ErrorHandler {
    static void handleConnectionError(Activity activity) {
        if (activity instanceof ReadLogin) {
            ((ReadLogin) activity).setErrorMsg("Connection failed. Try again, please!");
        } else if (activity instanceof WriteLogin) {
            ((WriteLogin) activity).setErrorMsg("Connection failed. Try again, please!");
        }

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException ignored) { }
        Intent intent = new Intent(activity, UI.class);
        activity.startActivity(intent);
    }
}
