package ru.spbau.tictactoe.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import ru.spbau.tictactoe.R;

public class ErrorHandler {
    static void handleConnectionError(Activity activity) {
//        if (activity instanceof ReadLogin) {
//            ((ReadLogin) activity).setErrorMsg("Connection failed. Try again, please!");
//        } else if (activity instanceof WriteLogin) {
//            ((WriteLogin) activity).setErrorMsg("Connection failed. Try again, please!");
//        }
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "font/maintypeface.ttf");

        TextView error = activity.findViewById(R.id.textView6);
        error.setTypeface(font);
        error.setText("Connection failed. Try again, please!");

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException ignored) { }
        Intent intent = new Intent(activity, UI.class);
        activity.startActivity(intent);
    }

//    public static void setErrorMsgAndReturnToMenu(Activity activity) {
//        TextView error = (TextView) activity.findViewById(R.id.textView6);
//        error.setTypeface(font);
//        error.setText(activity.errorMsg);
//
//        try {
//            TimeUnit.SECONDS.sleep(4);
//        } catch (InterruptedException ignored) { }
//        Intent intent = new Intent(activity, UI.class);
//        activity.startActivity(intent);
//    }
}
