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

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private String errorMsg = "";

    static class ClientRunner extends AsyncTask<Activity, Activity, Activity> {

        String text;

        ClientRunner(String text) {
            super();
            this.text = text;
        }

        @Override
        protected Activity doInBackground(Activity... activities) {
//            Controller.optionJoinFriend(text);
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
        setContentView(R.layout.activity_read_login);
        TextView text = (TextView) findViewById(R.id.textView4);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        text.setTypeface(font);

        Button but = (Button) findViewById(R.id.button);
        final TextView text2 = (TextView) findViewById(R.id.editText);
        text2.setTypeface(font);

        errorMsg = "";

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
        TextView error = (TextView) findViewById(R.id.textView6);
        error.setTypeface(font);
        error.setText(errorMsg);
    }

}
