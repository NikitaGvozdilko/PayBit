package com.justimaginethat.gogodriver.activity_landing_pkg.user.Payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CardSelector;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.cooltechworks.creditcarddesign.pager.CardFragmentAdapter;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.adapter.CreditCardListAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lion-1 on 4/9/2017.
 */

public class PaymentCardActivity extends AppCompatActivity {


    int mLastPageSelected = 0;
    public CreditCardView mCreditCardView;
    public String mCardNumber;
    public String mCVV;
    public String mCardHolderName;
    public String mExpiry;
    public int mStartPage = 0;
    public CardFragmentAdapter mCardAdapter;
    public RecyclerView recyclerView;
    public CreditCardListAdapter cardListAdapter;

    public Context context;
    public PaymentCardActivity cardActivity;

    public List<CardsDCM> cardsDCMList = new ArrayList<>();
    public Button btnAdd;
    public CardsDAO cardsDAO;
    public CardsDCM cardsDCM = new CardsDCM();
    public LinearLayout parent;

    public View mCustomView;
    public ImageView imgBack;
    public ImageView imgBackRight;
    public TextView txtPayment;
    public TextView title_text;
public boolean selectionOnly = false;
    public PaymentCardActivity() {
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_card);
        context = this;
        cardActivity = this;
        selectionOnly = getIntent().getBooleanExtra("selectionOnly",false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);
        mCustomView = getSupportActionBar().getCustomView();

        title_text=(TextView)mCustomView.findViewById(R.id.title_text);
        title_text.setText("Payment");

        imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
        imgBackRight = (ImageView) mCustomView.findViewById(R.id.imgBackRight);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardActivity.setResult(Activity.RESULT_OK);
                cardActivity.finish();
            }
        });
        imgBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardActivity.setResult(Activity.RESULT_OK);
                cardActivity.finish();
            }
        });
        cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        recyclerView = (RecyclerView) findViewById(R.id.recList);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        parent=(LinearLayout)findViewById(R.id.parent);
        try {
            if (cardsDAO.getAll().size() > 0) {
                cardsDCMList = cardsDAO.getAll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cardListAdapter = new CreditCardListAdapter(cardActivity);
        recyclerView.setAdapter(cardListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        setKeyboardVisibility(true);
        mCreditCardView = (CreditCardView)findViewById(R.id.credit_card_view);
        Bundle args = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
        checkParams(args);
        loadPager(args);
        if (mStartPage > 0 && mStartPage <= 3) {
            getViewPager().setCurrentItem(mStartPage);
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prepareCardData();
//                onDoneTapped();
            }
        });

    }
    public void prepareCardData() {
        if(mCardNumber != null) {
            if (!mCardNumber.equals("") && mCardNumber.length() == 16) {
                CardsDCM cardsDCM = new CardsDCM();
                cardsDCM.number = mCardNumber;
                cardsDCM.cvvNumber = mCVV;
                cardsDCM.expiryMonth = mExpiry;
                cardsDCM.expiryYear = mExpiry;
                try {
                    if (cardsDAO.getAll().size() == 0) {
                        cardsDCM.isDefault = true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cardsDAO.create(cardsDCM);
                cardsDCMList.add(cardsDCM);


                try {
                    cardsDCMList = cardsDAO.getAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cardListAdapter.notifyDataSetChanged();

                mCreditCardView.setCVV("");
                mCreditCardView.setCardHolderName("");
                mCreditCardView.setCardExpiry("");
                mCreditCardView.setCardNumber("");
                traverseEditTexts((ViewGroup) ((CardFragmentAdapter) getViewPager().getAdapter()).getItem(0).getView());
                traverseEditTexts((ViewGroup) ((CardFragmentAdapter) getViewPager().getAdapter()).getItem(1).getView());
                traverseEditTexts((ViewGroup) ((CardFragmentAdapter) getViewPager().getAdapter()).getItem(2).getView());
                mCreditCardView.showFront();
                getViewPager().setCurrentItem(0);
            }
        }
    }

    public void checkParams(Bundle bundle) {
        if (bundle != null) {
            mCardHolderName = bundle.getString("card_holder_name");
            mCVV = bundle.getString("card_cvv");
            mExpiry = bundle.getString("card_expiry");
            mCardNumber = bundle.getString("card_number");
            mStartPage = bundle.getInt("start_page");
            int maxCvvLength = CardSelector.selectCard(mCardNumber).getCvvLength();
            if (mCVV != null && mCVV.length() >= maxCvvLength) {
                mCVV = mCVV.substring(0, maxCvvLength);
            }
            mCreditCardView.setCVV(mCVV);
            SessionDAO sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
            try {
                String name = sessionDAO.getAll().get(0).firstName  + " " + sessionDAO.getAll().get(0).lastName;
                mCreditCardView.setCardHolderName(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            mCreditCardView.setCardExpiry(mExpiry);
            mCreditCardView.setCardNumber(mCardNumber);
            if (mCardAdapter != null) {
                mCardAdapter.setMaxCVV(maxCvvLength);
                mCardAdapter.notifyDataSetChanged();
            }


        }
    }

    public void refreshNextButton() {
        ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        int max = pager.getAdapter().getCount();
        int text = R.string.next;
        if (pager.getCurrentItem() == max - 1) {
            text = R.string.done;
        }
        ((TextView) findViewById(R.id.next)).setText(text);
    }

    ViewPager getViewPager() {

        return (ViewPager) findViewById(R.id.card_field_container_pager);
    }

    public void loadPager(Bundle bundle) {
        ViewPager pager = getViewPager();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                mCardAdapter.focus(position);
                if (position == 2) {
                    mCreditCardView.showBack();
                } else if (position == 1 && mLastPageSelected == 2 || position == 3) {
                    mCreditCardView.showFront();
                }

                mLastPageSelected = position;
                refreshNextButton();
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        pager.setOffscreenPageLimit(4);
        mCardAdapter = new CardFragmentAdapter(getSupportFragmentManager(), bundle);
        mCardAdapter.setOnCardEntryCompleteListener(new CardFragmentAdapter.ICardEntryCompleteListener() {
            public void onCardEntryComplete(int currentIndex) {
                showNext();
            }

            public void onCardEntryEdit(int currentIndex, String entryValue) {
                switch (currentIndex) {
                    case 0:
                        mCardNumber = entryValue.replace(" ", "");
                        mCreditCardView.setCardNumber(mCardNumber);
                        if (mCardAdapter != null) {
                            mCardAdapter.setMaxCVV(CardSelector.selectCard(mCardNumber).getCvvLength());
                        }
                        break;
                    case 1:
                        mExpiry = entryValue;
                        mCreditCardView.setCardExpiry(entryValue);
                        break;
                    case 2:
                        mCVV = entryValue;
                        mCreditCardView.setCVV(entryValue);
                        break;
                    case 3:
                        mCardHolderName = entryValue;
                        mCreditCardView.setCardHolderName(entryValue);
                }

            }
        });
        pager.setAdapter(mCardAdapter);

        int cardSide = getIntent().getIntExtra("card_side", 1);
        if (cardSide == 0) {
            pager.setCurrentItem(2);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("card_cvv", mCVV);
        outState.putString("card_holder_name", mCardHolderName);
        outState.putString("card_expiry", mExpiry);
        outState.putString("card_number", mCardNumber);
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        checkParams(inState);
    }

    public void showPrevious() {
        ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        int currentIndex = pager.getCurrentItem();
        if (currentIndex == 0) {
            setResult(0);
//            finish();
        }

        if (currentIndex - 1 >= 0) {
            pager.setCurrentItem(currentIndex - 1);
        }

        refreshNextButton();
    }

    public void showNext() {
        ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        CardFragmentAdapter adapter = (CardFragmentAdapter) pager.getAdapter();
        int max = adapter.getCount();
        int currentIndex = pager.getCurrentItem();
        if (currentIndex + 1 < max - 1) {
            pager.setCurrentItem(currentIndex + 1);
            refreshNextButton();
        } else {

//            mCreditCardView=new CreditCardView(context);

            setKeyboardVisibility(false);
            mCreditCardView.showFront();
            pager.setCurrentItem(0);
        }

    }
    public void traverseEditTexts(ViewGroup v)
    {
        EditText invalid = null;
        for (int i = 0; i < v.getChildCount(); i++)
        {
            Object child = v.getChildAt(i);
            if (child instanceof EditText)
            {
                EditText e = (EditText)child;
                e.setText("");
            }
            else if(child instanceof ViewGroup)
            {
                 traverseEditTexts((ViewGroup)child);  // Recursive call.
            }
        }
    }

    public void onDoneTapped() {
        Intent intent = new Intent();
        intent.putExtra("card_cvv", mCVV);
        intent.putExtra("card_holder_name", mCardHolderName);
        intent.putExtra("card_expiry", mExpiry);
        intent.putExtra("card_number", mCardNumber);
        setResult(-1, intent);
        finish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.hardKeyboardHidden == 1) {
            LinearLayout parent = (LinearLayout) findViewById(R.id.parent);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) parent.getLayoutParams();
            layoutParams.addRule(13, 0);
            parent.setLayoutParams(layoutParams);
        }
    }

    public void setKeyboardVisibility(boolean visible) {
        EditText editText = (EditText) findViewById(R.id.card_number_field);
        if (!visible) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } else {
            getWindow().setSoftInputMode(5);
        }
    }






    public void onBackPressed() {
        cardActivity.setResult(Activity.RESULT_OK);
        cardActivity.finish();
    }
}



