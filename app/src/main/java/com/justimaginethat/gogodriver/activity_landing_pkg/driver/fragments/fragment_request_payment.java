package com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.AddOrderAmountAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;

/**
 * Created by Lion-1 on 3/7/2017.
 */

public class fragment_request_payment extends Fragment{




          public Context context;
          public fragment_activity_landing_driver fActivity;
          public View view;
          public LinearLayout action_done;
          public EditText orderRate;
          public fragment_request_payment requestPayment;
            public String paymentRate="";
            public boolean valid;

    public fragment_request_payment(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_request_payment, container, false);
        context = getContext();
        requestPayment=this;
        orderRate=(EditText)view.findViewById(R.id.orderRate);
        action_done=(LinearLayout)view.findViewById(R.id.action_done);


//        orderRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);


        valid = true;
        if (orderRate.getText().toString().length() == 0) {
            orderRate.setError("Please Enter Amount!");
            valid = false;
        }

        action_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (orderRate.getText().toString().length() == 0) {
                    orderRate.setError("Please Enter Amount!");
                }else
                {
//                    if(Integer.parseInt(orderRate.getText().toString())>0){
                        paymentRate = orderRate.getText().toString();
                        new AddOrderAmountAsync(requestPayment).execute();
//                    }else {
//                        orderRate.setError("Please Enter Amount!");
//                    }
                }
            }
        });
        return view;
    }


    public void hideKeyboard()
    {
        InputMethodManager inputMethodManager =
                (InputMethodManager) requestPayment.getContext().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
