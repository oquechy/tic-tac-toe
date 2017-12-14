package ru.spbau.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
                Intent intent = new Intent(EntryPoint.this, SelectLevelActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener oclInviteFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        Controller.getIPtoShow(EntryPoint.this),
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),
                        WordCoder.encode(Controller.getIP(EntryPoint.this)),
                        Toast.LENGTH_LONG).show();
            }
        };
        View.OnClickListener oclJoinFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Input address of server",
                        Toast.LENGTH_LONG).show();
            }
        };
        View.OnClickListener oclSettingsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPoint.this, RecordActivity.class);
                startActivity(intent);
            }
        };
        View.OnClickListener oclRecordsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPoint.this, WriteLogin.class);
                startActivity(intent);
            }

        };

        singleButton.setOnClickListener(oclSingleButton);
        inviteFriendButton.setOnClickListener(oclInviteFriendButton);
        joinAFriendButton.setOnClickListener(oclJoinFriendButton);
        settingsButton.setOnClickListener(oclSettingsButton);
        recordsButton.setOnClickListener(oclRecordsButton);
    }
}
