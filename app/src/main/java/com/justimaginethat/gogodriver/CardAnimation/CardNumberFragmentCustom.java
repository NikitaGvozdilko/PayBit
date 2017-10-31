package com.justimaginethat.gogodriver.CardAnimation;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.pager.CreditCardFragment;

/**
 * Created by Lion-1 on 4/12/2017.
 */

public class CardNumberFragmentCustom extends CreditCardFragment {
    EditText mCardNumberView;

    public CardNumberFragmentCustom() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {
        View v = inflater.inflate(com.cooltechworks.creditcarddesign.R.layout.lyt_card_number, group, false);
        this.mCardNumberView = (EditText)v.findViewById(com.cooltechworks.creditcarddesign.R.id.card_number_field);
        String number = "";
        if(this.getArguments() != null && this.getArguments().containsKey("card_number")) {
            number = this.getArguments().getString("card_number");
        }

        if(number == null) {
            number = "";
        }
        this.mCardNumberView.setText(number);
        this.mCardNumberView.setTextColor(Color.BLACK);
        this.mCardNumberView.addTextChangedListener(this);
        return v;
    }

    public void afterTextChanged(Editable s) {
        int cursorPosition = this.mCardNumberView.getSelectionEnd();
        int previousLength = this.mCardNumberView.getText().length();
        String cardNumber = CreditCardUtils.handleCardNumber(s.toString());
        int modifiedLength = cardNumber.length();
        this.mCardNumberView.removeTextChangedListener(this);
        this.mCardNumberView.setText(cardNumber);
        this.mCardNumberView.setSelection(cardNumber.length() > 19?19:cardNumber.length());
        this.mCardNumberView.addTextChangedListener(this);
        if(modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            this.mCardNumberView.setSelection(cursorPosition);
        }

        this.onEdit(cardNumber);
        if(cardNumber.replace(" ", "").length() == 16) {
            this.onComplete();
        }

    }

    public void focus() {
        if(this.isAdded()) {
            this.mCardNumberView.selectAll();
        }

    }
}
