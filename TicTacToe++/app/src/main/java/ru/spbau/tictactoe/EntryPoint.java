package ru.spbau.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ru.spbau.tictactoe.ui.UI;

public class EntryPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_point);

        //create buttons
        Button singleButton = (Button) findViewById(R.id.singleButton);
        Button inviteFriendButton = (Button) findViewById(R.id.inviteFriendButton);
        Button joinAFriendButton = (Button) findViewById(R.id.joinAFriendButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        Button recordsButton = (Button) findViewById(R.id.recordsButton);
        //create OnClickListener for all buttons
        View.OnClickListener oclSingleButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.optionGameWithBot();
                newGame();
            }
        };

        View.OnClickListener oclInviteFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        Controller.getIPtoShow(EntryPoint.this),
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),
                        Controller.getEncodedIP(EntryPoint.this),
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EntryPoint.this, WriteLogin.class);
                startActivity(intent);
            }
        };
        View.OnClickListener oclJoinFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        Controller.getDecodedIP("manuda"),
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
                //TODO
            }
        };

        singleButton.setOnClickListener(oclSingleButton);
        inviteFriendButton.setOnClickListener(oclInviteFriendButton);
        joinAFriendButton.setOnClickListener(oclJoinFriendButton);
        settingsButton.setOnClickListener(oclSettingsButton);
        recordsButton.setOnClickListener(oclRecordsButton);
    }

    private void newGame() {
        Intent intent = new Intent(EntryPoint.this, UI.class);
        startActivity(intent);
    }
}
