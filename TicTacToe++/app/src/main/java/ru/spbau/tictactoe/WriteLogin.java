package ru.spbau.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.spbau.tictactoe.ui.UI;

public class WriteLogin extends AppCompatActivity {

    static class ServerRunner extends AsyncTask<Activity, Void, Activity> {

        @Override
        protected Activity doInBackground(Activity... activities) {
            Controller.optionInviteFriend();
            return activities[0];
        }

        @Override
        protected void onPostExecute(Activity activity) {
            Intent intent = new Intent(activity, UI.class);
            activity.startActivity(intent);
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
