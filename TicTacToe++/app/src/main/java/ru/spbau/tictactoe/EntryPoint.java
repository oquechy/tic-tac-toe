package ru.spbau.tictactoe;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.spbau.tictactoe.ui.InviteFriend;
import ru.spbau.tictactoe.ui.ReadLogin;
import ru.spbau.tictactoe.ui.UI;
import ru.spbau.tictactoe.ui.WriteLogin;

public class EntryPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_point);

        //create buttons
        Button singleButton = (Button) findViewById(R.id.singleButton);
        Button inviteFriendButton = (Button) findViewById(R.id.friendButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        Button recordsButton = (Button) findViewById(R.id.recordsButton);
        TextView name = (TextView) findViewById(R.id.name);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        singleButton.setTypeface(font);
        inviteFriendButton.setTypeface(font);
        settingsButton.setTypeface(font);
        recordsButton.setTypeface(font);
        name.setTypeface(font);
        //create OnClickListener for all buttons
        View.OnClickListener oclSingleButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.optionGameWithBot(1);
                newGame();
            }
        };

        View.OnClickListener oclInviteFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPoint.this, InviteFriend.class);
                startActivity(intent);
            }
        };
        View.OnClickListener oclJoinFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        Controller.getIPtoShow(EntryPoint.this),
                        Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(EntryPoint.this, ReadLogin.class);
                startActivity(intent);

            }
        };
        View.OnClickListener oclSettingsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        };
        View.OnClickListener oclRecordsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPoint.this, RecordActivity.class);
                startActivity(intent);
            }

        };

        singleButton.setOnClickListener(oclSingleButton);
        inviteFriendButton.setOnClickListener(oclInviteFriendButton);
        settingsButton.setOnClickListener(oclSettingsButton);
        recordsButton.setOnClickListener(oclRecordsButton);
    }

    private void newGame() {
        Intent intent = new Intent(EntryPoint.this, UI.class);
        startActivity(intent);
    }
}
