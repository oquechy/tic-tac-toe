package ru.spbau.tictactoe.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.spbau.tictactoe.Controller;
import ru.spbau.tictactoe.Logic.Result.Result;
import ru.spbau.tictactoe.R;

public class UI extends Activity implements SurfaceHolder.Callback, View.OnTouchListener {

    static int[][] board = new int[9][9];
    static int[][] smallBoard = new int[3][3];
    static int Hx = 1;
    static int Hy = 1;
    static int Lx = -1;
    static int Ly = -1;
    static int Lbx = -1;
    static int Lby = -1;

    static int crossOrZero = 1;

    static private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        final SurfaceView surface = findViewById(R.id.surfaceView);
        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        surface.setOnTouchListener(UI.this);
        Controller.initController(this);
        Button buttonReplay = findViewById(R.id.buttonReplay);
        View.OnClickListener replay = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpField();
                Controller.newGame();
            }
        };
        buttonReplay.setOnClickListener(replay);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        buttonReplay.setTypeface(font);
    }

    private Pair<Integer, Integer> getCoordinates(float x, float y) {
        int x1 = 0;
        int y1 = 0;
        int k1 = 0;
        int k2 = 0;
        while (x > x1) {
            k1++;
            x1 += Drawer.CELL_WIDTH;
        }
        while (y > y1) {
            k2++;
            y1 += Drawer.CELL_HEIGHT;
        }
        return new Pair<>(k1, k2);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Pair<Integer, Integer> p = getCoordinates(x, y);
                if (p.first - 1 < 9 && p.second - 1 < 9)
                    Controller.verifyTurn(p.first - 1, p.second - 1);
        }
        return true;
    }

    public void applyTurn(int x, int y, int who) {
        System.err.println("sasha's from " + (who == 1 ? "ui" : "bot") + ": " + x + " " + y);
        board[x - 1][y - 1] = who;
        if (who * crossOrZero < 0) {
            Lx = x - 1;
            Ly = y - 1;
            Lby = -1;
            Lbx = -1;
        }
        redraw();
    }

    //1 - cross
    //-1 - zero
    public void whoAmI(int i) {
        crossOrZero = i;
    }

    public void setUpField() {
        board = new int[9][9];
        smallBoard = new int[3][3];
        Hx = 1;
        Hy = 1;
        Lx = -1;
        Ly = -1;
        Lbx = -1;
        Lby = -1;
        redraw();
    }

    public void setHighlight(int x, int y) {
        Hx = x - 1;
        Hy = y - 1;
        redraw();
    }

    public void highlightAll() {
        Hx = -2;
        Hy = -2;
        redraw();
    }

    public void disableHighlight() {
        Hx = -1;
        Hy = -1;
        redraw();
    }

    public void smallWin(int x, int y, int who) {
        smallBoard[x - 1][y - 1] = who;
        if (who * crossOrZero < 0) {
            Lx = -1;
            Ly = -1;
            Lbx = x - 1;
            Lby = y - 1;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[(x - 1) * 3 + i][(y - 1) * 3 + j] = 0;
            }
        }
        redraw();
    }

    public void displayResult(Result r) {
        //Canvas canvas = surfaceHolder.lockCanvas();
        //Drawer.drawEverything(canvas);
        TextView res = findViewById(R.id.textView2);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        res.setTypeface(font);
        res.setTextColor(Color.rgb(163, 121, 73));
        res.setTextSize(50);
        if (r == Result.DRAW) {
            res.setText("DRAW!");
            return;
        }
        if (r == Result.CROSS && UI.crossOrZero == 1 || r == Result.NOUGHT && UI.crossOrZero == -1) {
            res.setText("WIN!");
        } else
            res.setText("LOSE!");
        //surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void incorrectTurnTime() {
        TextView res = findViewById(R.id.textView2);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/maintypeface.ttf");
        res.setTypeface(font);
        res.setTextColor(Color.rgb(163, 121, 73));
        res.setTextSize(40);
        res.setText("OPPONENT TURN");
    }

    public void clearMessage() {
        TextView res = findViewById(R.id.textView2);
        res.setText("");
    }

    public void redraw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        Drawer.drawEverything(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Controller.initBoard();
        redraw();
        Controller.startGameCycle();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        redraw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    static private class Pair<T, S> {
        T first;
        S second;

        Pair(T f, S s) {
            first = f;
            second = s;
        }
    }
}
