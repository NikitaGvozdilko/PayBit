package com.justimaginethat.gogodriver.ExtendedView;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;

/**
 * Created by jayb6 on 04-04-2017.
 */

public class AnimationButtonLogin extends Animation {

    private final activity_login result;
    private TextView txtLoginText;
    private ButtonLogin circle;

    private float oldAngle;
    private float newAngle;

    private float oldWidth;
    private float newWidth;
    private float oldBoxSize;
    private float newBoxSize;

    public RelativeLayout rootLayout;
    private float oldX = 0;
    private float newX;

    Context context;
    RippleSurface sur;
    public ProgressBar pb;
    public ProgressBar pbShadow;


    public AnimationButtonLogin(final ButtonLogin circle, final Context context, final ProgressBar pb, final ProgressBar pbShadow, final TextView txtLoginText, final RelativeLayout rootLayout, activity_login result) {

        this.rootLayout = rootLayout;
        this.result = result;
        this.context = context;
        this.pb = pb;
        this.circle = circle;
        this.txtLoginText = txtLoginText;
        this.pbShadow = pbShadow;



    }

    public int animationState = 0;

    public static class AnimationState {
        public static int START_ANIMATION = 0;
        public static int FINISH_ANIMATION = 1;
        public static int ABORT_ANIMATION = 2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle;
        float width;
        float x;
        switch (animationState) {
            case 0:
                angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);
                width = (oldWidth) + (((newWidth - oldWidth)) * (interpolatedTime));
                x = oldX + ((newX - oldX) * interpolatedTime);
                circle.startX = (int) x;
                circle.width = (int) width;
                circle.cornerRadious = (int) angle;
                circle.requestLayout();
                break;
            case 1:
                width = oldBoxSize + (((newBoxSize - oldBoxSize)) * (interpolatedTime));
                sur.width = (int) width;
                sur.height = (int) width;
                sur.requestLayout();
                break;
            case 2:
                angle = oldAngle * (1 - interpolatedTime);
                width = oldWidth + (((newWidth - oldWidth)) * (interpolatedTime));
                x = oldX * (1 - interpolatedTime);
                circle.startX = (int) x;
                circle.width = (int) width;
                circle.cornerRadious = (int) angle;
                circle.requestLayout();
                break;
        }

    }


    public void startAnimation() {

        animationState = AnimationState.START_ANIMATION;
        this.oldAngle = circle.cornerRadious;
        this.oldWidth = circle.oldWidth;
        this.newAngle = circle.oldWidth / 2;
        this.newWidth = (circle.oldWidth / 2) + (circle.oldHeight / 2);
        this.newX = (circle.oldWidth / 2) - (circle.oldHeight / 2);

        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                txtLoginText.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                pbShadow.setVisibility(View.VISIBLE);
                ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(pb,
                        "rotation", 0f, 360f);
                processObjectAnimator.setDuration(1000);
                processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                processObjectAnimator.setInterpolator(new LinearInterpolator());
                processObjectAnimator.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        circle.startAnimation(this);
    }

    public void finishAnimation() {

        animationState = AnimationState.FINISH_ANIMATION;
        sur = new RippleSurface(context);
        sur.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        sur.setX(0);
        sur.setY(0);
        int x = getRelativeLeft(circle);
        int y = getRelativeTop(circle);
        sur.startX = (x + (circle.oldWidth / 2));
        sur.startY = (y);
        rootLayout.addView(sur);
        oldBoxSize = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        newBoxSize = ((displayMetrics.widthPixels > displayMetrics.heightPixels) ? displayMetrics.widthPixels : displayMetrics.heightPixels);

        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                result.onSuccessResultAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        circle.startAnimation(this);
    }

    public void abortAnimation() {
        animationState = AnimationState.ABORT_ANIMATION;

        this.oldAngle = circle.cornerRadious;
        this.oldWidth = circle.width;
        this.newAngle = 0;
        this.newWidth = circle.oldWidth;
        this.newX = 0;
        this.oldX = (circle.oldWidth / 2) - (circle.oldHeight / 2);
        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtLoginText.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                pbShadow.setVisibility(View.GONE);
                ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(pb,
                        "rotation", 0f, 0f);
                processObjectAnimator.setDuration(1000);
                processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                processObjectAnimator.setInterpolator(new LinearInterpolator());
                processObjectAnimator.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        circle.startAnimation(this);
    }


    private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return (int) myView.getX();
        else
            return (int) (myView.getX() + getRelativeLeft((View) myView.getParent()));
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return (int) myView.getY();
        else
            return (int) (myView.getY() + getRelativeTop((View) myView.getParent()));
    }

}
