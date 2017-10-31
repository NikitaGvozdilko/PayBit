package com.justimaginethat.gogodriver.ExtendedView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.justimaginethat.gogodriver.R;

/**
 * Created by jayb6 on 04-04-2017.
 */

public class ButtonLogin extends View {




    public final Paint paint;
    public RectF rect;
//    final int strokeWidth = 1;
    public int cornerRadious = 0;
    public int width = 0;
    public int height = 0;
    public int oldWidth = 0;
    public int oldHeight = 0;
    public int startX = 0;
    public int oldX = 0;


    public ButtonLogin(Context context, AttributeSet attrs) {
        super(context, attrs);


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
        width = oldWidth =  w;
        height = oldHeight = h;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rect = new RectF(startX  , 0 , width , height  );
        canvas.drawRoundRect(rect,cornerRadious,cornerRadious, paint);
    }


}
