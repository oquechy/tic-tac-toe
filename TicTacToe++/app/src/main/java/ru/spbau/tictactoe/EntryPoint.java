package ru.spbau.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EntryPoint extends AppCompatActivity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_point);
        controller = new Controller(this);
        controller.start();
    }
}
