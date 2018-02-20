package ru.spbau.tictactoe.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.spbau.tictactoe.Controller;
import ru.spbau.tictactoe.R;

import static ru.spbau.tictactoe.Controller.newGame;

public class ChooseLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        Button bot1 = (Button) findViewById(R.id.B1);
        Button bot2 = (Button) findViewById(R.id.B2);
        Button bot3 = (Button) findViewById(R.id.B3);
        Button bot4 = (Button) findViewById(R.id.B4);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        bot1.setTypeface(font);
        bot2.setTypeface(font);
        bot3.setTypeface(font);
        bot4.setTypeface(font);
        View.OnClickListener ocl1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.optionGameWithBot(1);
                newGame();
            }
        };
        View.OnClickListener ocl2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.optionGameWithBot(2);
                newGame();
            }
        };
        View.OnClickListener ocl3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.optionGameWithBot(3);
                newGame();
            }
        };
        View.OnClickListener ocl4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.optionGameWithBot(4);
                newGame();
            }
        };
        bot1.setOnClickListener(ocl1);
        bot2.setOnClickListener(ocl2);
        bot3.setOnClickListener(ocl3);
        bot4.setOnClickListener(ocl4);
    }
}
