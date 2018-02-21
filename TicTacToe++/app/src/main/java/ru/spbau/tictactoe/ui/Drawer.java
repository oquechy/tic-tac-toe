package ru.spbau.tictactoe.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Drawer {

    static int CELL_WIDTH = 0;
    static int CELL_HEIGHT = 0;
    static private Paint mPaint = new Paint();
    static private Paint mPaintFat = new Paint();
    private static float indentX = 0;
    private static float indentY = 0;

    private static void drawBackground(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        width = height = Math.min(width, height);

        CELL_WIDTH = width / 9;
        CELL_HEIGHT = width / 9;

        indentX = (float) CELL_WIDTH / 6;
        indentY = (float) CELL_HEIGHT / 6;

        canvas.drawColor(Color.rgb(255, 235, 229));

        mPaint.setColor(Color.rgb(76, 51, 26));
        mPaint.setStrokeWidth(3);
        mPaintFat.setColor(Color.rgb(76, 51, 26));
        mPaintFat.setStrokeWidth(15);

        int k = 0;
        for (int i = 0; i <= width && k <= 9; i += CELL_WIDTH, k++) {
            if (k % 3 == 0)
                canvas.drawLine(i, 0, i, height, mPaintFat);
            else
                canvas.drawLine(i, 0, i, height, mPaint);
        }

        k = 0;
        for (int i = 0; i <= height && k <= 9; i += CELL_HEIGHT) {
            if (k % 3 == 0)
                canvas.drawLine(0, i, width, i, mPaintFat);
            else
                canvas.drawLine(0, i, width, i, mPaint);
            k++;
        }
        for (int i = 0 ; i < 9; i++) {
            for (int j = 0 ; j < 9; j++) {
                if (UI.board[i][j] * UI.crossOrZero == 1) {
                    drawCross(canvas, i, j, Color.rgb(201, 102, 102));
                }
                if (UI.board[i][j] * UI.crossOrZero == -1) {
                    drawCircle(canvas, i, j, Color.rgb(68, 152, 163));
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (UI.smallBoard[i][j] * UI.crossOrZero == 1) {
                    drawBigCross(canvas, i, j, Color.rgb(201, 102, 102));
                }
                if (UI.smallBoard[i][j] * UI.crossOrZero == -1) {
                    drawBigCircle(canvas, i, j, Color.rgb(68, 152, 163));
                }
            }
        }

        mPaintFat.setStyle(Paint.Style.STROKE);
        mPaintFat.setColor(Color.RED);

        if (UI.highlightX >= 0 && UI.highlightY >= 0) {
            canvas.drawRect(UI.highlightX * 3 * CELL_WIDTH, UI.highlightY * 3 * CELL_HEIGHT,
                    (UI.highlightX + 1) * 3 * CELL_WIDTH, (UI.highlightY + 1) * 3 * CELL_HEIGHT, mPaintFat);
        }

        if (UI.highlightX == -2 && UI.highlightY == -2) {
            canvas.drawRect(0, 0,
                    9 * CELL_WIDTH, 9 * CELL_HEIGHT, mPaintFat);
        }
        if (UI.lightX >= 0 && UI.lightY >= 0)
            drawCircle(canvas, UI.lightX, UI.lightY, Color.rgb(108, 221, 221));
        if (UI.lightBigX >= 0 && UI.lightBigY >= 0)
            drawBigCircle(canvas, UI.lightBigX, UI.lightBigY, Color.rgb(108, 221, 221));
    }

    static private boolean drawCircle(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mCanvas.drawCircle(x * CELL_WIDTH + (CELL_WIDTH / 2), y * CELL_HEIGHT + (CELL_HEIGHT / 2),
                (CELL_WIDTH / 2) - indentX, mPaint);
        return true;
    }

    static private boolean drawCross(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(8);
        mCanvas.drawLine(x * CELL_WIDTH + indentX, (y + 1) * CELL_HEIGHT - indentY,
                (x + 1) * CELL_WIDTH - indentX, y * CELL_HEIGHT + indentY, mPaint);
        mCanvas.drawLine(x * CELL_WIDTH + indentX, y * CELL_HEIGHT + indentY,
                (x + 1) * CELL_WIDTH - indentX, (y + 1) * CELL_HEIGHT - indentY, mPaint);
        return true;
    }

    static private boolean drawBigCross(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(8);
        mCanvas.drawLine(x * CELL_WIDTH * 3 + indentX, (y + 1) * CELL_HEIGHT * 3 - indentY,
                (x + 1) * CELL_WIDTH * 3 - indentX, y * CELL_HEIGHT * 3 + indentY, mPaint);
        mCanvas.drawLine(x * CELL_WIDTH * 3 + indentX, y * CELL_HEIGHT * 3 + indentY,
                (x + 1) * CELL_WIDTH * 3 - indentX, (y + 1) * CELL_HEIGHT * 3 - indentY, mPaint);
        return true;
    }

    static private boolean drawBigCircle(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mCanvas.drawCircle(x * CELL_WIDTH * 3 + (CELL_WIDTH / 2) * 3, y * CELL_HEIGHT * 3 + (CELL_HEIGHT / 2) * 3,
                (CELL_WIDTH / 2) * 3 - indentX, mPaint);
        return true;
    }
    static void drawEverything(Canvas canvas) {
        drawBackground(canvas);
    }
}
