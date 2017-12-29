package ru.spbau.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import ru.spbau.tictactoe.ui.UI;

import ru.spbau.tictactoe.Statistic.DataBase;


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

        DataBase dataBase = new DataBase(this);
        DataBase.Entry[] entries = dataBase.readRecords();

        StringBuilder recordString = new StringBuilder();
        for (DataBase.Entry entry : entries) {
            if (entry.message != null) {
                recordString.append(entry.message).append('\n');
            } else {
                recordString.append(entry.rowNumber).append(". ")
                        .append(entry.result).append(' ')
                        .append(entry.opponent).append(' ')
                        .append(entry.moves).append('\n');
            }
        }

        id.setText(recordString.toString());
    }

}
