package ru.spbau.tictactoe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Drawer {

    static private Paint mPaint = new Paint();
    static private Paint mPaintFat = new Paint();

    static int CELL_WIDTH = 0;
    static int CELL_HEIGHT = 0;

    public static void drawBackground(Canvas canvas) { //TODO

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        CELL_WIDTH = width / 9;
        CELL_HEIGHT = height / 9;

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);

        mPaint.setColor(Color.BLACK);
        mPaintFat.setColor(Color.BLACK);
        mPaintFat.setStrokeWidth(23);

        int k = 0;
        for (int i = 0; i <= width; i += CELL_WIDTH, k++) {
            if (k % 3 == 0)
                canvas.drawLine(i, 0, i, height, mPaintFat);
            else
                canvas.drawLine(i, 0, i, height, mPaint);
        }

        k = 0;
        for (int i = 0; i <= height; i += CELL_HEIGHT) {
            if (k % 3 == 0)
                canvas.drawLine(0, i, width, i, mPaintFat);
            else
                canvas.drawLine(0, i, width, i, mPaint);
            k++;
        }
        for (int i = 0 ; i < 9; i++) {
            for (int j = 0 ; j < 9; j++) {
                if (Board.board[i][j] == 1) {
                    drawSquare(canvas, i*width, i*height);
                }
            }
        }
    }

    static private boolean drawSquare(Canvas mCanvas, int x, int y) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        while (x > x2) {
            x1 = x2;
            x2 += CELL_HEIGHT;
        }
        while (y > y2)  {
            y1 = y2;
            y2 += CELL_WIDTH;
        }
        mCanvas.drawLine(0 , 0 , 1000 , 1000 , mPaint);
        return true;
    }

    public static void drawEverything(Canvas canvas) {
        drawBackground(canvas);
    }
}
