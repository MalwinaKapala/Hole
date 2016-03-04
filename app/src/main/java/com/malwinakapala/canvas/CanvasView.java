package com.malwinakapala.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;

public class CanvasView extends View {
    private static final int DOWN = 1;
    private static final int RIGHT = 1;

    private double y = 550;
    private double x = 400;
    private double directionY = DOWN;
    private double directionX = RIGHT;
    private Bitmap redBall;
    private double vx;
    private double vy;
    private int holeRadius;
    private int holeX = 700;
    private int holeY = 600;
    private Random random = new Random();
    private int score = 0;
    private boolean warningVisible;
    private boolean wallCrash;

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        redBall = BitmapFactory.decodeResource(getResources(), R.mipmap.red_ball);
        holeRadius = redBall.getWidth() / 2;
    }

    private void wallCrash() {
        if (warningVisible) {
            return;
        }
        score = score - 1;
        warningVisible = true;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                warningVisible = false;
            }
        }, 3000);
    }

    private void move() {
        y = y + vy * directionY;
        x = x + vx * directionX;

        double distance = Math.sqrt(Math.pow(x - holeX, 2) + Math.pow(y - holeY, 2));

        if (distance < 50) {
            holeX = random.nextInt(getWidth() - holeRadius*2) + holeRadius;
            holeY = random.nextInt(getHeight() - holeRadius*2) + holeRadius;
            score = score + 1;
        }

        if (x < holeRadius) {
            x = holeRadius;
        }
        if (y < holeRadius) {
            y = holeRadius;
        }
        if (x >= getWidth() - holeRadius) {
            x = getWidth() - holeRadius;
        }
        if (y >= getHeight() - holeRadius) {
            y = getHeight() - holeRadius;
        }
        if (x >= getWidth() - holeRadius) {
            wallCrash();
        }
        if (y >= getHeight() - holeRadius) {
            wallCrash();
        }
        if (x <= holeRadius ) {
            wallCrash();
        }
        if (y <= holeRadius) {
            wallCrash();
        }
        invalidate();
    }

    public void changeVelocity(int dx, int dy) {
        vx = dx;
        vy = dy;
        move();
    }

    protected void onDraw(Canvas canvas) {
        Paint brush = new Paint();
        Paint scorePaint = new Paint();
        Paint warningPaint = new Paint();
        scorePaint.setColor(Color.BLUE);
        scorePaint.setFakeBoldText(true);
        scorePaint.setTextSize(80);
        warningPaint.setColor(Color.RED);
        warningPaint.setTextSize(100);
        brush.setColor(Color.BLACK);
        brush.setStrokeWidth(10);
        canvas.drawCircle(holeX, holeY, holeRadius, brush);
        canvas.drawBitmap(redBall, (int) (x - holeRadius), (int) (y - holeRadius), null);
        canvas.drawText("SCORE: " + score, 70, 250, scorePaint);
        if (warningVisible) {
            canvas.drawText("WARNING: -1 SCORE", 700, 1300, warningPaint);
        }
    }
}
