package com.example.olesya.quickpress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by itamar on 12-Dec-15.
 */
public class MyView extends View{
    private int height,width,top,right;
    private Paint paint;
    private Path path;
    private final static float RADIUS = 50;
    public float x = 0 ,y = 0 ;
    private Random rand;
    public MyView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        path = new Path();

    }
    public void getRandomCircle(){
        rand = new Random();
        do  {
            x = rand.nextInt((int) (width - RADIUS));
            y = rand.nextInt((int) (height - RADIUS));
       }    while (x < RADIUS || y < RADIUS);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        right = getPaddingRight();
        top = getPaddingTop();
        width = w - (getPaddingLeft() + getPaddingRight());
        height = h - (getPaddingTop() + getPaddingBottom());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getRandomCircle();
        path.reset();
        path.addCircle(x, y, RADIUS, Path.Direction.CCW);
        path.close();
        canvas.drawPath(path, paint);
    }
}
