package com.malwinakapala.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class CanvasView extends View {
    private static final int DOWN = 1;
    private static final int RIGHT = 1;

    private double y = 550;
    private double x = 400;
    private double directionY = DOWN;
    private double directionX = RIGHT;
    private Bitmap ball2;
    private Bitmap background;
    private Bitmap hole;
    private double vx;
    private double vy;
    private int ballRadius;
    private int holeX = 700;
    private int holeY = 600;
    private Random random = new Random();
    private int score = 0;
    private boolean warningVisible;
    private boolean wallCrash;
    private Paint brush = new Paint();
    private Paint scorePaint = new Paint();
    private Paint warningPaint = new Paint();
    private long lastInvalidate;
    private int lives = 5;

    public CanvasView(Context context) {
        super(context);
        init();
    }
;
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ball2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ball2);
        ballRadius = ball2.getWidth() / 2;
        background = BitmapFactory.decodeResource(getResources(), R.mipmap.background);
        hole = BitmapFactory.decodeResource(getResources(), R.mipmap. hole);
    }

    private void wallCrash() {
        if (warningVisible) {
            return;
        }
        score = score - 1;
        lives = lives -1;
        warningVisible = true;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                warningVisible = false;
            }
        }, 3000);

        TextView  tv = new TextView(getContext());
        tv.setTextColor(Color.RED);
        tv.setTextSize(20);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setText("BOOM!   -1");

        LinearLayout  layout = new LinearLayout(getContext());
        layout.setBackgroundResource(R.color.black_overlay);
        layout.addView(tv);

        Toast toast = new Toast(getContext());
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 100, 300);
        toast.show();
    }

    private void move() {
        y = y + vy * directionY;
        x = x + vx * directionX;

        double distance = Math.sqrt(Math.pow(x - holeX, 2) + Math.pow(y - holeY, 2));

        if (distance < 50) {
            holeX = random.nextInt(getWidth() - ballRadius *2) + ballRadius;
            holeY = random.nextInt(getHeight() - ballRadius *2) + ballRadius;
            score = score + 1;
        }

        if (x < ballRadius) {
            x = ballRadius;
        }
        if (y < ballRadius) {
            y = ballRadius;
        }
        if (x >= getWidth() - ballRadius) {
            x = getWidth() - ballRadius;
        }
        if (y >= getHeight() - ballRadius) {
            y = getHeight() - ballRadius;
        }
        if (x >= getWidth() - ballRadius) {
            wallCrash();
        }
        if (y >= getHeight() - ballRadius) {
            wallCrash();
        }
        if (x <= ballRadius) {
            wallCrash();
        }
        if (y <= ballRadius) {
            wallCrash();
        }
        if ( System.currentTimeMillis() - lastInvalidate > 5) {
            invalidate();
            lastInvalidate = System.currentTimeMillis();
        }
    }

    public void changeVelocity(float dx, float dy) {
        vx = dx;
        vy = dy;
        move();
    }

    protected void onDraw(Canvas canvas) {
        scorePaint.setColor(Color.BLACK);
        scorePaint.setFakeBoldText(true);
        scorePaint.setTextSize(80);
        warningPaint.setColor(Color.RED);
        warningPaint.setTextSize(120);
        brush.setStrokeWidth(10);
        brush.setColor(Color.WHITE);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(hole, holeX - ballRadius, holeY - ballRadius, null);
        canvas.drawBitmap(ball2, (int) (x - ballRadius), (int) (y - ballRadius), null);
        canvas.drawRect(30, 30, 520, 170, brush);
        canvas.drawRect(getWidth() - 490, 30, getWidth() - 30, 170,  brush);
        canvas.drawText("SCORE: " + score, 70, 130, scorePaint);
        canvas.drawText("LIVES: " + lives, getWidth() - 470, 130, scorePaint);
        if (warningVisible) {
        }
    }
}
