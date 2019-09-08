package com.example.luisreyes.proyecto_aguas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by luis.reyes on 13/08/2019.
 */

public class myCanvas extends  View {


    Paint paint;
    Path path;

    public myCanvas(Context context, AttributeSet attrs){

        super(context, attrs);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

        setDrawingCacheEnabled(true);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos, yPos);
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos, yPos);
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                return false;
        }
        invalidate();

        return true;
    }

}
