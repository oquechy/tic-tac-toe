package ru.spbau.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EntryPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_point);
        //create buttons
        Button singleButton = (Button) findViewById(R.id.singleButton);
        Button multiplayButton = (Button) findViewById(R.id.multiplayButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        Button recordsButton = (Button) findViewById(R.id.recordsButton);
        //create OnClickListener for all buttons
        View.OnClickListener oclSingleButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryPoint.this, Board.class);
                startActivity(intent);
            }
        };
        View.OnClickListener oclMultyplayButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
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
        multiplayButton.setOnClickListener(oclMultyplayButton);
        settingsButton.setOnClickListener(oclSettingsButton);
        recordsButton.setOnClickListener(oclRecordsButton);
    }
}
