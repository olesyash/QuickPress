package com.example.olesya.quickpress;

import android.content.Context;
import android.content.SharedPreferences;
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
    public float[] x;
    public float[] y;
    private Random rand;
    private SharedPreferences memory;
    private SharedPreferences.Editor edit;
    int level,complexity;
    public MyView(Context context) {
        super(context);
//        edit = memory.edit();
        init(null, 0,context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0,context);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle,context);
    }

    private void init(AttributeSet attrs, int defStyle, Context context)
    {
        memory = context.getSharedPreferences("setting", context.MODE_PRIVATE);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        path = new Path();

    }
    public void getRandomCircle(int i){
        rand = new Random();
        do  {
            x[i] = rand.nextInt((int) (width - RADIUS));
            y[i] = rand.nextInt((int) (height - RADIUS));
       }    while (x[i] < RADIUS || y[i] < RADIUS);
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
        level = memory.getInt("level",1);
        complexity = memory.getInt("complexity",0);
        x = new float[complexity+1];
        y = new float[complexity+1];
        path.reset();
        for(int i = 0; i < complexity; i++) {
            getRandomCircle(i);

            path.addCircle(x[i], y[i], RADIUS, Path.Direction.CCW);
        }

        path.close();
        canvas.drawPath(path, paint);
    }
}
