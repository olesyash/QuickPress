package com.example.olesya.quickpress;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by itamar on 12-Dec-15.
 */
public class MyView extends View {
    private int height, width, top, right, bx, by;
    private Paint paint;
    private Path path;
    private final static int POWER = 2;
    private final static float RADIUS = 50;
    private final static float bw = 120, bh = 80;
    private float[] x;
    private float[] y;
    private RectF rect;
    private Random rand;
    private Context context;
    private GameInterface gi;
    private SharedPreferences memory;
    private SharedPreferences.Editor edit;
    protected boolean bttn_pressed = false;
    int level, complexity;
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
        rect = new RectF();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        path = new Path();
        gi = (GameInterface)context;

    }
    private void getRandomCircle(int i){
        rand = new Random();
        do {
                x[i] = rand.nextInt((int) (width - RADIUS));
                y[i] = rand.nextInt((int) (height - RADIUS));
            } while (intersect(i)||(x[i]<RADIUS||y[i]<RADIUS));
    }

    private boolean intersect(int i){
        double a, b, c, d, r1, r2;
        r2 = Math.pow(RADIUS+bh , POWER);
        c = Math.pow((rect.left+bw/2)-x[i], POWER);
        d = Math.pow((rect.top+bh/2)-y[i], POWER);
        if((c+d)<=r2)
            return true;
        r1 = Math.pow(2*RADIUS, POWER);
        for(int j= i-1;j>=0;j--){
            a = Math.pow(x[j]-x[i],POWER);
            b = Math.pow(y[j]-y[i], POWER);
            if((a+b)<=r1)
                return true;
        }
        return false;
    }
    private void getRandomButton()
    {
        rand = new Random();
        bx = rand.nextInt((int)(width - bw));
        by = rand.nextInt((int)(height - bh));
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
        getRandomButton();
        rect.top = by;
        rect.left = bx;
        rect.right = (int)(bx + bw);
        rect.bottom = (int)(by + bh);

        if(bttn_pressed) {
            for (int i = 0; i < complexity; i++) {
                getRandomCircle(i);

                path.addCircle(x[i], y[i], RADIUS, Path.Direction.CCW);
            }

            path.addRect(rect, Path.Direction.CCW);
        }

        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if(rect.contains(x, y))
                {
                    Log.i("logs", "pressed");
                   gi.pressed();
                }
                break;
        }
        return true;
    }

}
