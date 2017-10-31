package com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;

import uk.co.deanwild.marqueetextview.MarqueeTextView;

/**
 * Created by Lion-1 on 3/9/2017.
 */

public class fragment_driver_conform  extends Fragment {
    public Context context;
    public fragment_activity_landing_driver fActivity;

    public View view;

    public TextView display_customer;
    public TextView lblCustomerName;
    public MarqueeTextView lblPickupAddress;

    public TextView lblCustomerAddress;

    public ImageView btnImgAccept;
    public ImageView btnImgReject;


    public TextView userName;
    public ImageView imgUser;
    public TextView userRating;





    public fragment_driver_conform() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_conform, container, false);
        context = getContext();
        setUI();
        clickEvent();
        return view;
    }
    public void setUI(){
        display_customer=(TextView)view.findViewById(R.id.display_customer);
        lblCustomerName =(TextView)view.findViewById(R.id.lblCustomerName);
        lblPickupAddress =(MarqueeTextView)view.findViewById(R.id.lblPickupAddress);
        lblCustomerAddress =(TextView)view.findViewById(R.id.lblCustomerAddress);
        btnImgAccept =(ImageView)view.findViewById(R.id.btnImgAccept);
        btnImgReject =(ImageView)view.findViewById(R.id.btnImgReject);
    }
    void clickEvent(){
        btnImgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnImgReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                cardActivity.showLayout();
            }
        });
    }
}
