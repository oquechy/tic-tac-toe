package ru.spbau.tictactoe;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Board extends Activity implements SurfaceHolder.Callback, View.OnTouchListener {

    public static int[][] board = new int[9][9];

    public static int crossOrZero = -1;

    static private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        final SurfaceView surface = findViewById(R.id.surfaceView);
        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        surface.setOnTouchListener(Board.this);

    }

    static private class Pair<T, S> {
        T first;
        S second;

        Pair(T f, S s) {
            first = f;
            second = s;
        }
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
                if (Controller.verifyTurn(new Turn(false, p.first, p.second))) {
                    board[p.first - 1][p.second - 1] = 1;
                    redraw();
                }
        }
        return true;
    }

    public void applyOpponentTurn(int x, int y) {
        board[x - 1][y - 1] = -1;
        redraw();
    }

    //1 - cross
    //-1 - zero
    public void whoAmI(int i) {
        crossOrZero = i;
    }

    public void redraw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        Drawer.drawEverything(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        redraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        redraw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
