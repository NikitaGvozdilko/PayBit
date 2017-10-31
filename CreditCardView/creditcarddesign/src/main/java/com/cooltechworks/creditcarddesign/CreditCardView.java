package com.cooltechworks.creditcarddesign;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CardSelector;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.FlipAnimator;


/**
 * Created by Harish on 03/01/16.
 */
public class CreditCardView extends FrameLayout {

    private static final int TEXTVIEW_CARD_HOLDER_ID = com.cooltechworks.creditcarddesign.R.id.front_card_holder_name;
    private static final int TEXTVIEW_CARD_EXPIRY_ID = com.cooltechworks.creditcarddesign.R.id.front_card_expiry;
    private static final int TEXTVIEW_CARD_NUMBER_ID = com.cooltechworks.creditcarddesign.R.id.front_card_number;
    private static final int TEXTVIEW_CARD_CVV_ID = com.cooltechworks.creditcarddesign.R.id.back_card_cvv;
    private static final int FRONT_CARD_ID = com.cooltechworks.creditcarddesign.R.id.front_card_container;
    private static final int BACK_CARD_ID = com.cooltechworks.creditcarddesign.R.id.back_card_container;
    private static final int FRONT_CARD_OUTLINE_ID = com.cooltechworks.creditcarddesign.R.id.front_card_outline;
    private static final int BACK_CARD_OUTLINE_ID = com.cooltechworks.creditcarddesign.R.id.back_card_outline;


    private int mCurrentDrawable;
    private String mRawCardNumber;
    private ICustomCardSelector mSelectorLogic;

    private String mCardHolderName, mCVV, mExpiry;

    public CreditCardView(Context context) {
        super(context);
        init();
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CreditCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public String getCardHolderName() {
        return mCardHolderName;
    }

    public String getCVV() {
        return mCVV;
    }

    public String getExpiry() {
        return mExpiry;
    }

    interface ICustomCardSelector {
        CardSelector getCardSelector(String cardNumber);
    }
    
    private void init() {

        mCurrentDrawable = com.cooltechworks.creditcarddesign.R.drawable.card_color_round_rect_default;
        mRawCardNumber = "";

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(com.cooltechworks.creditcarddesign.R.layout.view_creditcard, this, true);

    }

    private void init(AttributeSet attrs) {

        init();

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                com.cooltechworks.creditcarddesign.R.styleable.creditcard, 0, 0);


        String cardHolderName = a.getString(com.cooltechworks.creditcarddesign.R.styleable.creditcard_card_holder_name);
        String expiry = a.getString(com.cooltechworks.creditcarddesign.R.styleable.creditcard_card_expiration);
        String cardNumber = a.getString(com.cooltechworks.creditcarddesign.R.styleable.creditcard_card_number);
        int cvv = a.getInt(com.cooltechworks.creditcarddesign.R.styleable.creditcard_cvv, 0);
        int cardSide = a.getInt(com.cooltechworks.creditcarddesign.R.styleable.creditcard_card_side, CreditCardUtils.CARD_SIDE_FRONT);

        setCardNumber(cardNumber);
        setCVV(cvv);
        setCardExpiry(expiry);
        setCardHolderName(cardHolderName);

        if(cardSide == CreditCardUtils.CARD_SIDE_BACK) {
            showBackImmediate();
        }

        paintCard();

        a.recycle();

    }


    private void flip(final boolean ltr, boolean isImmediate) {

        View layoutContainer = findViewById(com.cooltechworks.creditcarddesign.R.id.card_outline_container);
        View frontView = findViewById(FRONT_CARD_OUTLINE_ID);
        View backView = findViewById(BACK_CARD_OUTLINE_ID);

        final View frontContentView = findViewById(FRONT_CARD_ID);
        final View backContentView = findViewById(BACK_CARD_ID);
        View layoutContentContainer = findViewById(com.cooltechworks.creditcarddesign.R.id.card_container);


        if(isImmediate) {
            frontContentView.setVisibility(ltr?VISIBLE:GONE);
            backContentView.setVisibility(ltr?GONE:VISIBLE);

        }
        else {

            int duration =  600;

            FlipAnimator flipAnimator = new FlipAnimator(frontView, backView, frontView.getWidth() / 2, backView.getHeight() / 2);
            flipAnimator.setInterpolator(new OvershootInterpolator(0.5f));
            flipAnimator.setDuration(duration);

            if (ltr) {
                flipAnimator.reverse();
            }

            flipAnimator.setTranslateDirection(FlipAnimator.DIRECTION_Z);
            flipAnimator.setRotationDirection(FlipAnimator.DIRECTION_Y);
            layoutContainer.startAnimation(flipAnimator);

            FlipAnimator flipAnimator1 = new FlipAnimator(frontContentView, backContentView, frontContentView.getWidth() / 2, backContentView.getHeight() / 2);
            flipAnimator1.setInterpolator(new OvershootInterpolator(0.5f));
            flipAnimator1.setDuration(duration);

            if (ltr) {
                flipAnimator1.reverse();
            }

            flipAnimator1.setTranslateDirection(FlipAnimator.DIRECTION_Z);
            flipAnimator1.setRotationDirection(FlipAnimator.DIRECTION_Y);

            layoutContentContainer.startAnimation(flipAnimator1);
        }

    }

    public void setCardNumber(String rawCardNumber) {


        this.mRawCardNumber = rawCardNumber == null ? "" : rawCardNumber;

        String newCardNumber = mRawCardNumber;
        for(int i=mRawCardNumber.length();i<16;i++) {
            newCardNumber +=CreditCardUtils.CHAR_X;
        }

        String cardNumber = CreditCardUtils.handleCardNumber(newCardNumber, CreditCardUtils.DOUBLE_SPACE_SEPERATOR);
        ((TextView)findViewById(TEXTVIEW_CARD_NUMBER_ID)).setText(cardNumber);

        if(mRawCardNumber.length() == 3) {
            revealCardAnimation();
        }
        else {
            paintCard();
        }

    }

    public void setCVV(int cvvInt) {

        if(cvvInt == 0) {
            setCVV("");
        }
        else {
            String cvv = String.valueOf(cvvInt);
            setCVV(cvv);
        }

    }

    public void showFront() {
        flip(true,false);
    }

    public void showFrontImmediate() {
        flip(true,true);
    }

    public void showBack() {
        flip(false,false);
    }

    public void showBackImmediate() {
        flip(false,true);
    }

    public void setCVV(String cvv) {
        if(cvv == null) {
            cvv = "";
        }

        this.mCVV = cvv;
        ((TextView)findViewById(TEXTVIEW_CARD_CVV_ID)).setText(cvv);
    }

    public void setCardExpiry(String dateYear) {

        dateYear = dateYear == null ? "": CreditCardUtils.handleExpiration(dateYear);

        this.mExpiry = dateYear;

        ((TextView) findViewById(TEXTVIEW_CARD_EXPIRY_ID)).setText(dateYear);


    }

    public void setCardHolderName(String cardHolderName) {

        cardHolderName = cardHolderName == null ? "" : cardHolderName;
        if(cardHolderName.length() > 16) {
            cardHolderName = cardHolderName.substring(0,16);
        }

        this.mCardHolderName = cardHolderName;

        ((TextView)findViewById(TEXTVIEW_CARD_HOLDER_ID)).setText(cardHolderName);
    }

    public void paintCard() {

        CardSelector card = selectCard();

        View cardContainer = findViewById(com.cooltechworks.creditcarddesign.R.id.card_outline_container);

        View chipContainer = findViewById(com.cooltechworks.creditcarddesign.R.id.chip_container);
        View chipInner = findViewById(com.cooltechworks.creditcarddesign.R.id.chip_inner_view);

        View cardBack =  findViewById(BACK_CARD_OUTLINE_ID);
        View cardFront = findViewById(FRONT_CARD_OUTLINE_ID);


        chipContainer.setBackgroundResource(card.getResChipOuterId());
        chipInner.setBackgroundResource(card.getResChipInnerId());

        ImageView frontLogoImageView = (ImageView) cardContainer.findViewById(com.cooltechworks.creditcarddesign.R.id.logo_img);
        frontLogoImageView.setImageResource(card.getResLogoId());

        ImageView centerImageView = (ImageView) cardContainer.findViewById(com.cooltechworks.creditcarddesign.R.id.logo_center_img);
        centerImageView.setImageResource(card.getResCenterImageId());


        ImageView backLogoImageView = (ImageView) findViewById(BACK_CARD_ID).findViewById(com.cooltechworks.creditcarddesign.R.id.logo_img);
        backLogoImageView.setImageResource(card.getResLogoId());

        cardBack.setBackgroundResource(card.getResCardId());
        cardFront.setBackgroundResource(card.getResCardId());
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void revealCardAnimation() {

        CardSelector card = selectCard();

        View cardFront = findViewById(FRONT_CARD_OUTLINE_ID);
        View cardContainer = findViewById(com.cooltechworks.creditcarddesign.R.id.card_outline_container);

        paintCard();

        animateChange(cardContainer, cardFront, card.getResCardId());
    }

    public CardSelector selectCard() {
        if(mSelectorLogic != null) {
            return mSelectorLogic.getCardSelector(mRawCardNumber);
        }
        return CardSelector.selectCard(mRawCardNumber);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateChange(final View cardContainer, final View v, final int drawableId) {
        showAnimation(cardContainer, v, drawableId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showAnimation(final View cardContainer, final View v, final int drawableId) {

        final View mRevealView = v;
        mRevealView.setBackgroundResource(drawableId);

        if (mCurrentDrawable == drawableId) {
            return;
        }

        int duration = 1000;
        int cx = mRevealView.getLeft();
        int cy = mRevealView.getTop();

        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight()) * 4;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


            Animator animator =
                    ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(duration);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardContainer.setBackgroundResource(drawableId);
                }
            }, duration);

            mRevealView.setVisibility(View.VISIBLE);
            animator.start();
            mCurrentDrawable = drawableId;

        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            mRevealView.setVisibility(View.VISIBLE);
            anim.setDuration(duration);
            anim.start();
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    cardContainer.setBackgroundResource(drawableId);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });


            mCurrentDrawable = drawableId;
        }
    }

    public void setSelectorLogic(ICustomCardSelector mSelectorLogic) {
        this.mSelectorLogic = mSelectorLogic;
    }

    public String getCardNumber() {
        return mRawCardNumber;
    }


}
