package com.justimaginethat.gogodriver.ProcessBar;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.justimaginethat.gogodriver.R;

/**
 * Created by Lion-1 on 4/28/2017.
 */

public class CustomProgressBar extends Dialog {
    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, int theme) {
        super(context, theme);
    }


    public void onWindowFocusChanged(boolean hasFocus){




        ProgressBar pbCircular = (ProgressBar) findViewById(R.id.spinnerImageView);


        ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(pbCircular,
                "rotation", 0f, 360f);
        processObjectAnimator.setDuration(1000);
        processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        processObjectAnimator.setInterpolator(new LinearInterpolator());
        processObjectAnimator.start();



//        AnimationDrawable spinner = (AnimationDrawable) pbCircular.getBackground();
//        spinner.start();
    }

//    public void setMessage(CharSequence message) {
//        if(message != null && message.length() > 0) {
//            findViewById(R.id.message).setVisibility(View.VISIBLE);
//            TextView txt = (TextView)findViewById(R.id.message);
//            txt.setText(message);
//            txt.invalidate();
//        }
//    }

    public static CustomProgressBar show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
                                         OnCancelListener cancelListener) {
        CustomProgressBar dialog = new CustomProgressBar(context,R.style.CustomProgressBar);
        dialog.setTitle("");
        dialog.setContentView(R.layout.custom_progress_bar);
//        if(message == null || message.length() == 0) {
//            dialog.findViewById(R.id.message).setVisibility(View.GONE);
//        } else {
//            TextView txt = (TextView)dialog.findViewById(R.id.message);
//            txt.setText(message);
//        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity= Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.2f;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}