package ru.spbau.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class Board extends AppCompatActivity {

    public class Draw2D extends View implements View.OnTouchListener {
        public Draw2D(Context context) {
            super(context);
            this.setOnTouchListener(this);
        }


        private Paint mPaint = new Paint();
        private Paint mPaintFat = new Paint();

        int CELL_WIDTH = 0;
        int CELL_HEIGHT = 0;

        Canvas mCanvas;
        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            mCanvas = canvas;

            int width = canvas.getWidth();
            int height = canvas.getHeight();

            CELL_WIDTH = width / 9;
            CELL_HEIGHT = height / 9;

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.WHITE);
            canvas.drawPaint(mPaint);

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
        }

        private boolean drawSquare(int x, int y) {
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
            mCanvas.drawRect(0 , 0 , 100 , 100 , mPaint);
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawSquare((int) x, (int) y);

            }
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Draw2D draw2D = new Draw2D(this);
        setContentView(draw2D);
    }
}
