package ru.spbau.tictactoe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class Drawer {

    static private Paint mPaint = new Paint();
    static private Paint mPaintFat = new Paint();

    static int CELL_WIDTH = 0;
    static int CELL_HEIGHT = 0;

    private static float indentX = 0;
    private static float indentY = 0;

    private static void drawBackground(Canvas canvas) {
        int width = canvas.getWidth();
        int height = width;

        CELL_WIDTH = width / 9;
        CELL_HEIGHT = width / 9;

        indentX = (float) CELL_WIDTH / 6;
        indentY = (float) CELL_HEIGHT / 6;

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaintFat.setColor(Color.BLACK);
        mPaintFat.setStrokeWidth(23);

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
                if (Board.board[i][j] * Board.crossOrZero == 1) {
                    drawCross(canvas, i, j);
                }
                if (Board.board[i][j] * Board.crossOrZero == -1) {
                    drawCircle(canvas, i, j);
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Board.smallBoard[i][j] * Board.crossOrZero == 1) {
                    drawBigCross(canvas, i, j);
                }
                if (Board.smallBoard[i][j] * Board.crossOrZero == -1) {
                    drawBigCircle(canvas, i, j);
                }
            }
        }
    }

    static private boolean drawCircle(Canvas mCanvas, int x, int y) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mCanvas.drawCircle(x * CELL_WIDTH + (CELL_WIDTH / 2), y * CELL_HEIGHT + (CELL_HEIGHT / 2),
                (CELL_WIDTH / 2) - indentX / 2, mPaint);
        return true;
    }

    static private boolean drawCross(Canvas mCanvas, int x, int y) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
        mCanvas.drawLine(x * CELL_WIDTH + indentX, (y + 1) * CELL_HEIGHT - indentY,
                (x + 1) * CELL_WIDTH - indentX, y * CELL_HEIGHT + indentY, mPaint);
        mCanvas.drawLine(x * CELL_WIDTH + indentX, y * CELL_HEIGHT + indentY,
                (x + 1) * CELL_WIDTH - indentX, (y + 1) * CELL_HEIGHT - indentY, mPaint);
        return true;
    }

    static private boolean drawBigCross(Canvas mCanvas, int x, int y) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
        mCanvas.drawLine(x * CELL_WIDTH * 3 + indentX, (y + 1) * CELL_HEIGHT * 3 - indentY,
                (x + 1) * CELL_WIDTH * 3 - indentX, y * CELL_HEIGHT * 3 + indentY, mPaint);
        mCanvas.drawLine(x * CELL_WIDTH * 3 + indentX, y * CELL_HEIGHT * 3 + indentY,
                (x + 1) * CELL_WIDTH * 3 - indentX, (y + 1) * CELL_HEIGHT * 3 - indentY, mPaint);
        return true;
    }

    static private boolean drawBigCircle(Canvas mCanvas, int x, int y) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mCanvas.drawCircle(x * CELL_WIDTH * 3 + (CELL_WIDTH / 2) * 3, y * CELL_HEIGHT * 3 + (CELL_HEIGHT / 2) * 3,
                (CELL_WIDTH / 2) * 3 - indentX, mPaint);
        return true;
    }
    static void drawEverything(Canvas canvas) {
        drawBackground(canvas);
    }
}
