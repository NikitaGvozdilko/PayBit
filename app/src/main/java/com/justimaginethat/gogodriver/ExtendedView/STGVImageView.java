
package com.justimaginethat.gogodriver.ExtendedView;
/**
 * Created by Jay Bhavsar on 04/08/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Random;

public class STGVImageView extends ImageView {

    public static final String TAG = STGVImageView.class.getSimpleName();
    public int mWidth = 0;
    public int mHeight = 0;

    private static final float Trans = -25f;

    private final static float[] BT_SELECTED = new float[]{
            1, 0, 0, 0, Trans,
            0, 1, 0, 0, Trans,
            0, 0, 1, 0, Trans,
            0, 0, 0, 1, 0};

    private final static float[] BT_NOT_SELECTED = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0};

    private ColorMatrixColorFilter mPressFilter;
    private ColorMatrixColorFilter mNormalFilter;

    public STGVImageView(Context context) {
        super(context);
        init();
    }

    public STGVImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public STGVImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public void init()
    {

             Random rnd = new Random();
             int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
             setBackgroundColor(color);
     }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getDrawable() != null) {
                    if (mPressFilter == null) {
                        mPressFilter = new ColorMatrixColorFilter(BT_SELECTED);
                    }
                    getDrawable().setColorFilter(mPressFilter);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (getDrawable() != null) {
                    if (mNormalFilter == null) {
                        mNormalFilter = new ColorMatrixColorFilter(BT_NOT_SELECTED);
                    }
                    getDrawable().setColorFilter(mNormalFilter);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
