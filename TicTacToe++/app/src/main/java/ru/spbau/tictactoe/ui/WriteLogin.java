package ru.spbau.tictactoe.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.spbau.tictactoe.Controller;
import ru.spbau.tictactoe.R;

public class WriteLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_login);
        TextView text = (TextView) findViewById(R.id.textView);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        text.setTypeface(font);
        text.setText(Controller.getEncodedIP(this));
    }
}
