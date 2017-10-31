package com.justimaginethat.gogodriver.ExtendedView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.justimaginethat.gogodriver.R;

import retrofit2.http.HEAD;

/**
 * Created by jayb6 on 04-04-2017.
 */

public class RippleSurface extends View {




    public final Paint paint;
    public RectF rect;
//    final int strokeWidth = 1;
    public int cornerRadious = 0;
    public int width = 10;
    public int height = 10;
    public int oldWidth = 0;
    public int oldHeight = 0;
    public int startX = 0;
    public int startY = 0;


    public RippleSurface(Context context) {
        super(context);


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(strokeWidth);
        //Circle color
        int myColor = ContextCompat.getColor(context, R.color.colorPrimary); // new

        paint.setColor(myColor);
        //size 200x200 example




    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        oldWidth =  w;

        oldHeight = h;



//        int min = (oldWidth>oldHeight)?oldHeight:oldWidth;
//        startX = (oldWidth/2)  ;
//        startY = (oldHeight/2)  ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        int radius = Math.min(width, height) ;


        canvas.drawCircle(startX, startY, radius, paint);
    }


}
