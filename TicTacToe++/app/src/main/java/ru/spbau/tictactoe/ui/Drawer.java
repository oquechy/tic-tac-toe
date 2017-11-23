package ru.spbau.tictactoe.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ru.spbau.tictactoe.Logic.Result.Result;

class Drawer {

    static int CELL_WIDTH = 0;
    static int CELL_HEIGHT = 0;
    static private Paint mPaint = new Paint();
    static private Paint mPaintFat = new Paint();
    private static float indentX = 0;
    private static float indentY = 0;

    private static void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.rgb(218, 195, 148));
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        width = height = Math.min(width, height);

        CELL_WIDTH = width / 9;
        CELL_HEIGHT = width / 9;

        indentX = (float) CELL_WIDTH / 6;
        indentY = (float) CELL_HEIGHT / 6;

        canvas.drawColor(Color.WHITE);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaintFat.setColor(Color.BLACK);
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
                    drawCross(canvas, i, j, Color.BLACK);
                }
                if (UI.board[i][j] * UI.crossOrZero == -1) {
                    drawCircle(canvas, i, j, Color.BLACK);
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (UI.smallBoard[i][j] * UI.crossOrZero == 1) {
                    drawBigCross(canvas, i, j, Color.BLACK);
                }
                if (UI.smallBoard[i][j] * UI.crossOrZero == -1) {
                    drawBigCircle(canvas, i, j, Color.BLACK);
                }
            }
        }

        mPaintFat.setStyle(Paint.Style.STROKE);
        mPaintFat.setColor(Color.RED);

        if (UI.Hx >= 0 && UI.Hy >= 0) {
            canvas.drawRect(UI.Hx * 3 * CELL_WIDTH, UI.Hy * 3 * CELL_HEIGHT,
                    (UI.Hx + 1) * 3 * CELL_WIDTH, (UI.Hy + 1) * 3 * CELL_HEIGHT, mPaintFat);
        }

        if (UI.Hx == -2 && UI.Hy == -2) {
            canvas.drawRect(0, 0,
                    9 * CELL_WIDTH, 9 * CELL_HEIGHT, mPaintFat);
        }
        if (UI.Lx >= 0 && UI.Ly >= 0)
            drawCircle(canvas, UI.Lx, UI.Ly, Color.MAGENTA);
        if (UI.Lbx >= 0 && UI.Lby >= 0)
            drawBigCircle(canvas, UI.Lbx, UI.Lby, Color.MAGENTA);
    }

    static private boolean drawCircle(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mCanvas.drawCircle(x * CELL_WIDTH + (CELL_WIDTH / 2), y * CELL_HEIGHT + (CELL_HEIGHT / 2),
                (CELL_WIDTH / 2) - indentX / 2, mPaint);
        return true;
    }

    static private boolean drawCross(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(6);
        mCanvas.drawLine(x * CELL_WIDTH + indentX, (y + 1) * CELL_HEIGHT - indentY,
                (x + 1) * CELL_WIDTH - indentX, y * CELL_HEIGHT + indentY, mPaint);
        mCanvas.drawLine(x * CELL_WIDTH + indentX, y * CELL_HEIGHT + indentY,
                (x + 1) * CELL_WIDTH - indentX, (y + 1) * CELL_HEIGHT - indentY, mPaint);
        return true;
    }

    static private boolean drawBigCross(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStrokeWidth(6);
        mCanvas.drawLine(x * CELL_WIDTH * 3 + indentX, (y + 1) * CELL_HEIGHT * 3 - indentY,
                (x + 1) * CELL_WIDTH * 3 - indentX, y * CELL_HEIGHT * 3 + indentY, mPaint);
        mCanvas.drawLine(x * CELL_WIDTH * 3 + indentX, y * CELL_HEIGHT * 3 + indentY,
                (x + 1) * CELL_WIDTH * 3 - indentX, (y + 1) * CELL_HEIGHT * 3 - indentY, mPaint);
        return true;
    }

    static private boolean drawBigCircle(Canvas mCanvas, int x, int y, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mCanvas.drawCircle(x * CELL_WIDTH * 3 + (CELL_WIDTH / 2) * 3, y * CELL_HEIGHT * 3 + (CELL_HEIGHT / 2) * 3,
                (CELL_WIDTH / 2) * 3 - indentX, mPaint);
        return true;
    }
    static void drawEverything(Canvas canvas) {
        drawBackground(canvas);
    }

    static void writeWin(Canvas canvas, Result r) {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(100);
        if (r == Result.DRAW) {
            canvas.drawText("DRAW!", CELL_WIDTH * 3 + Math.round(CELL_WIDTH / 3), CELL_HEIGHT * 10, mPaint);
            return;
        }
        if (r == Result.CROSS && UI.crossOrZero == 1 || r == Result.NOUGHT && UI.crossOrZero == -1)
            canvas.drawText("YOU WIN!", CELL_WIDTH * 3 - Math.round(CELL_WIDTH / 3), CELL_HEIGHT * 10, mPaint);
        else
            canvas.drawText("YOU LOSE!", CELL_WIDTH * 3 - Math.round(CELL_WIDTH / 3), CELL_HEIGHT * 10, mPaint);
    }
}
