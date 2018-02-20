package ru.spbau.tictactoe.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import ru.spbau.tictactoe.Controller;
import ru.spbau.tictactoe.R;

public class ReadLogin extends AppCompatActivity {

    private static boolean error = false;

    static class ClientRunner extends AsyncTask<Activity, Activity, Activity> {

        String code;

        ClientRunner(String code) {
            super();
            this.code = code;
        }

        @Override
        protected Activity doInBackground(Activity... activities) {
            try {
                Controller.optionJoinFriend(code);
            } catch (IOException e) {
                error = true;
            }
            return activities[0];
        }

        @Override
        protected void onPostExecute(Activity activity) {
            if (!error) {
                Intent intent = new Intent(activity, UI.class);
                activity.startActivity(intent);
            } else {
                ErrorHandler.handleConnectionError(activity);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_login);
        TextView text = (TextView) findViewById(R.id.textView4);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        text.setTypeface(font);

        Button but = (Button) findViewById(R.id.button);
        final TextView text2 = (TextView) findViewById(R.id.editText);
        text2.setTypeface(font);

        View.OnClickListener oclBut = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = text2.getText().toString();
                System.err.println("code = " + code);
                try {
                    Controller.optionJoinFriend(code);
                    Intent intent = new Intent(ReadLogin.this, UI.class);
                    ReadLogin.this.startActivity(intent);
                } catch (IOException e) {
                    ErrorHandler.handleConnectionError(ReadLogin.this);
                }
            }
        };

        but.setOnClickListener(oclBut);
    }

}
