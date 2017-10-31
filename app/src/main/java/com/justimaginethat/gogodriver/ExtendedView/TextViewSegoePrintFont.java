package com.justimaginethat.gogodriver.ExtendedView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jay Bhavsar on 04/08/2016.
 */
public class TextViewSegoePrintFont extends TextView {

    public TextViewSegoePrintFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewSegoePrintFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewSegoePrintFont(Context context) {
        super(context);
        init();
    }

    public void init() {
//        if(!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir_light.otf");
            setTypeface(tf, 1);
//        }
    }

}