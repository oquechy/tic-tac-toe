package ru.spbau.tictactoe.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.spbau.tictactoe.R;

public class InviteFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        Button inviteFriend = (Button) findViewById(R.id.inviteFriend);
        Button connectFriend = (Button) findViewById(R.id.connectFriend);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        inviteFriend.setTypeface(font);
        connectFriend.setTypeface(font);
        View.OnClickListener oclInviteriends = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteFriend.this, WriteLogin.class);
                startActivity(intent);
            }
        };
        View.OnClickListener oclConnectFriend = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InviteFriend.this, ReadLogin.class);
                startActivity(intent);
            }
        };
        inviteFriend.setOnClickListener(oclInviteriends);
        connectFriend.setOnClickListener(oclConnectFriend);
    }
}
