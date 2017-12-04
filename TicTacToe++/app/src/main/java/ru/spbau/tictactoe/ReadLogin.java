package ru.spbau.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import ru.spbau.tictactoe.ui.UI;

public class ReadLogin extends AppCompatActivity {

    static class ClientRunner extends AsyncTask<Activity, Void, Void> {

        String text;

        ClientRunner(String text) {
            super();
            this.text = text;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            Controller.optionConnectToFriend(text);
            Intent intent = new Intent(activities[0], UI.class);
            activities[0].startActivity(intent);
            Controller.newGame(Controller.myTurn);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_login);
        /*EditText id = */((EditText) findViewById(R.id.editText)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                new ClientRunner(v.getText().toString()).execute(ReadLogin.this);
                                return true; // consume.
                        }
                        return false; // pass on to other listeners.
                    }
                }
                    );

//        String text = id.getText().toString();
//        while (!Objects.equals(text, "")) {
//            text = id.getText().toString();
//        }

    }
}
