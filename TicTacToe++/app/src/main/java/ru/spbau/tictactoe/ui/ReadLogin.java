package ru.spbau.tictactoe.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.spbau.tictactoe.R;

public class ReadLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_login);
        TextView text = (TextView) findViewById(R.id.textView4);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        text.setTypeface(font);
        Button but = (Button) findViewById(R.id.button);
        final TextView text2 = (TextView) findViewById(R.id.editText);
        text2.setTypeface(font);
        View.OnClickListener oclBut = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println(text2.getText().toString());
            }
        };
        but.setOnClickListener(oclBut);
    }
}
