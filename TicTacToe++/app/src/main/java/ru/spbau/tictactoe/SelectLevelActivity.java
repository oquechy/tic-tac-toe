package ru.spbau.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.spbau.tictactoe.ui.UI;

public class SelectLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);
        Button easyButton = (Button) findViewById(R.id.buttonEasy);
        Button mediumButton = (Button) findViewById(R.id.buttonMedium);
        Button hardButton = (Button) findViewById(R.id.buttonHard);
        View.OnClickListener easyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.optionGameWithBot(1);
                Intent intent = new Intent(SelectLevelActivity.this, UI.class);
                startActivity(intent);
            }
        };
        View.OnClickListener mediumListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.optionGameWithBot(2);
                Intent intent = new Intent(SelectLevelActivity.this, UI.class);
                startActivity(intent);
            }
        };
        View.OnClickListener hardListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.optionGameWithBot(3);
                Intent intent = new Intent(SelectLevelActivity.this, UI.class);
                startActivity(intent);
            }
        };
    }
}
