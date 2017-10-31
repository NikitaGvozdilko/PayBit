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
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lion-1 on 3/10/2017.
 */

public class fragment_success_fully_paid extends Fragment {

    public Context context;
    public fragment_activity_landing_driver fActivity;


    public TextView driverName;
    public CircleImageView imgDriver;
    public TextView driverRating;

    public fragment_success_fully_paid() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_success_fully_paid, container, false);
        context = getContext();

        driverName=(TextView)view.findViewById(R.id.driverName);
        //        imgUser=(ImageView)view.findViewById(R.id.imgUser);
        imgDriver=(CircleImageView)view.findViewById(R.id.imgDriver);
        driverRating=(TextView)view.findViewById(R.id.driverRating);


//        try {
//            userDCMList=userDAO.getAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        for (DriverDCM userDCMs:userDCMList
//                ) {
//            String newProfilePicture="";
//            if(!userDCMs.profilePicture.contains("http:")){
//                newProfilePicture= FixLabels.Server+ userDCMs.profilePicture;
//                Glide.with(this).load(newProfilePicture).into(imgUser);
//            }else {
//                Glide.with(this).load(userDCMs.profilePicture).into(imgUser);
//            }
//           userRating.setText(userDCMs.syncGUID+"/5");
//           userName.setText(userDCMs.userName);
//        }

        return view;
    }
}
