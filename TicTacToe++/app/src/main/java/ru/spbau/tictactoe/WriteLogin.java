package ru.spbau.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.ui.UI;

public class WriteLogin extends AppCompatActivity {

    static class ServerRunner extends AsyncTask<Activity, Void, Void> {
        @Override
        protected Void doInBackground(Activity... activities) {
            Controller.optionInviteFriend();
            Intent intent = new Intent(activities[0], UI.class);
            activities[0].startActivity(intent);
            Controller.newGame(Controller.myTurn);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_login);
        TextView id = (TextView) findViewById(R.id.textView);
        id.setText(Controller.getEncodedIP(this));
        new ServerRunner().execute(this);
//        Intent intent = new Intent(WriteLogin.this, UI.class);
//        startActivity(intent);
    }

}
