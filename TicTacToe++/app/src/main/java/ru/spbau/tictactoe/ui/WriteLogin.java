package ru.spbau.tictactoe.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Controller;
import ru.spbau.tictactoe.R;

public class WriteLogin extends AppCompatActivity {

    private String errorMsg;

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    static class ServerRunner extends AsyncTask<Activity, Void, Activity> {

        @Override
        protected Activity doInBackground(Activity... activities) {
            try {
                Controller.optionInviteFriend();
                return activities[0];
            } catch (IOException e) {
                ErrorHandler.handleConnectionError(activities[0]);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Activity activity) {
            activity.setContentView(R.layout.activity_board);
            Intent intent = new Intent(activity, UI.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_login);
        TextView text = (TextView) findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        text.setTypeface(font);
        TextView code = (TextView) findViewById(R.id.textView3);
        code.setTypeface(font);
        code.setText(Controller.getEncodedIP(this));
        new ServerRunner().execute(this);

        TextView error = (TextView) findViewById(R.id.textView5);
        error.setTypeface(font);
        error.setText(errorMsg);
//        ErrorHandler.setErrorMsgAndReturnToMenu(WriteLogin.this);
    }
}
