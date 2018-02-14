package ru.spbau.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.spbau.tictactoe.ui.UI;

public class ReadLogin extends AppCompatActivity {

    static class ClientRunner extends AsyncTask<Activity, Activity, Activity> {

        String text;

        ClientRunner(String text) {
            super();
            this.text = text;
        }

        @Override
        protected Activity doInBackground(Activity... activities) {
            Controller.optionJoinFriend(text);
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

        final EditText id = ((EditText) findViewById(R.id.editText)); /*.setOnEditorActionListener(
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
        );*/

        Button connectionButton = (Button) findViewById(R.id.connectionButton);
        View.OnClickListener oclConnectionButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = id.getText().toString();
                Controller.optionJoinFriend(text);
                Intent intent = new Intent(ReadLogin.this, UI.class);
                ReadLogin.this.startActivity(intent);
            }
        };

        connectionButton.setOnClickListener(oclConnectionButton);


//        while (!Objects.equals(text, "")) {
//            text = id.getText().toString();
//        }

    }
}
