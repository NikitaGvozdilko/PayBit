package com.justimaginethat.gogodriver.ExtendedView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Jay Bhavsar on 04/08/2016.
 */
public class EditTextSegoePrintFont extends EditText {

    public EditTextSegoePrintFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextSegoePrintFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextSegoePrintFont(Context context) {
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