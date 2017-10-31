package com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.AddressMapPickerActivity;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;

import java.sql.SQLException;

/**
 * Created by Lion-1 on 3/17/2017.
 */

public class fragment_address_driver extends AppCompatActivity {

    LinearLayout linearMainPrimary;
    LinearLayout linear_primary;
    LinearLayout linearAction_imageP;
    ImageView imgArrow_down_P;
    LinearLayout linearAddressP;
    LinearLayout addressFieldP;
    LinearLayout addressHomeP;
    LinearLayout addressFloorP;
    TextView edtBuildingNumberP;
    TextView edtFloorNumberP;
    LinearLayout linearGetAddressP;
    TextView edtAddressFieldP;
    Switch switch_Primary;


    LinearLayout linearMainSecondary;
    LinearLayout linear_secondary;
    LinearLayout linearAction_imageS;
    ImageView imgArrow_down_S;
    LinearLayout linearAddressS;
    LinearLayout addressFieldS;
    LinearLayout addressHomeS;
    LinearLayout addressFloorS;
    TextView edtBuildingNumberS;
    TextView edtFloorNumberS;
    LinearLayout linearGetAddressS;
    TextView edtAddressFieldS;
    Switch switch_Secondary;


    public View mCustomView;
    ImageView imgBack;
    ImageView imgBackRight;
    String messageAddress;

    public SessionDCM sessionDCM;
    public SessionDAO sessionDAO;

    public DriverDCM driverDCM;
    public DriverDAO driverDAO;




    private int mRequestCodeP = 100;
    private int mRequestCodeS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbar_address_layout);

        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        sessionDCM=new SessionDCM();
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        driverDCM=new DriverDCM();


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);
        mCustomView = getSupportActionBar().getCustomView();


        imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
        imgBackRight = (ImageView) mCustomView.findViewById(R.id.imgBackRight);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(fragment_address_driver.this, fragment_activity_landing_driver.class);
                startActivity(intentBack);
                finish();
            }
        });


        imgBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                sessionDCM.address2 = messageAddress;

                sessionDAO.update(sessionDCM);
                edtAddressFieldS.setText(messageAddress);



                driverDCM.defaultAddress = messageAddress;
                driverDAO.update(driverDCM);


            }
        });




//        LayoutInflater mInflater = LayoutInflater.from(this);

//        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        linearMainPrimary = (LinearLayout) findViewById(R.id.linearMainPrimary);
        linear_primary = (LinearLayout) findViewById(R.id.linear_primary);
        linearAction_imageP = (LinearLayout) findViewById(R.id.linearAction_imageP);
        imgArrow_down_P = (ImageView) findViewById(R.id.imgArrow_down_P);
        linearAddressP = (LinearLayout) findViewById(R.id.linearAddressP);
        edtBuildingNumberP=(TextView)findViewById(R.id.edtBuildingNumberP);
        edtFloorNumberP=(TextView)findViewById(R.id.edtFloorNumberP);

        addressFieldP = (LinearLayout) findViewById(R.id.addressFieldP);
        addressHomeP = (LinearLayout) findViewById(R.id.addressHomeP);
        addressFloorP = (LinearLayout) findViewById(R.id.addressFloorP);
        linearGetAddressP = (LinearLayout) findViewById(R.id.linearGetAddressP);
        edtAddressFieldP = (TextView) findViewById(R.id.edtAddressFieldP);
        switch_Primary = (Switch) findViewById(R.id.switch_Primary);



        linearMainSecondary = (LinearLayout) findViewById(R.id.linearMainSecondary);
        linear_secondary = (LinearLayout) findViewById(R.id.linear_secondary);
        linearAction_imageS = (LinearLayout) findViewById(R.id.linearAction_imageS);
        imgArrow_down_S = (ImageView) findViewById(R.id.imgArrow_down_S);
        linearAddressS = (LinearLayout) findViewById(R.id.linearAddressS);
        addressFieldS = (LinearLayout) findViewById(R.id.addressFieldS);
        addressHomeS = (LinearLayout) findViewById(R.id.addressHomeS);
        addressFloorS = (LinearLayout) findViewById(R.id.addressFloorS);
        edtBuildingNumberS=(TextView)findViewById(R.id.edtBuildingNumberS);
        edtFloorNumberS=(TextView)findViewById(R.id.edtFloorNumberS);


        linearGetAddressS = (LinearLayout) findViewById(R.id.linearGetAddressS);
        edtAddressFieldS = (TextView) findViewById(R.id.edtAddressFieldS);
        switch_Secondary = (Switch) findViewById(R.id.switch_Secondary);
        imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
        imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
        linear_secondary.setVisibility(View.GONE);

        linearMainPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linear_primary.getVisibility() == View.GONE) {
                    imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    linear_primary.setVisibility(View.VISIBLE);
                } else {
                    linear_primary.setVisibility(View.GONE);
                    imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));

                }
            }
        });


        linearMainSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linear_secondary.getVisibility() == View.GONE) {
                    imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    linear_secondary.setVisibility(View.VISIBLE);
                } else {
                    linear_secondary.setVisibility(View.GONE);
                    imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                }
            }
        });


        linearGetAddressP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment_address_driver.this, AddressMapPickerDriverActivity.class);
                startActivityForResult(intent, mRequestCodeP);
            }
        });
        linearGetAddressS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragment_address_driver.this, AddressMapPickerDriverActivity.class);
                startActivityForResult(intent, mRequestCodeS);
            }
        });

        switch_Primary.setChecked(true);
        switch_Secondary.setChecked(false);

        switch_Primary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch_Secondary.setChecked(false);
                if (switch_Primary.isChecked()) {
                    if (linear_secondary.getVisibility() == View.GONE) {
                        imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                        linear_secondary.setVisibility(View.VISIBLE);
                    } else {
                        linear_secondary.setVisibility(View.GONE);
                        imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));

                    }
                } else {
                    switch_Secondary.setChecked(true);
                    if (linear_secondary.getVisibility() == View.GONE) {
                        imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                        linear_secondary.setVisibility(View.VISIBLE);
                    } else {
                        linear_secondary.setVisibility(View.GONE);
                        imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));

                    }
                }
            }
        });
        switch_Secondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_Primary.setChecked(false);
                imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                if (switch_Secondary.isChecked()) {
                    if (linear_primary.getVisibility() == View.GONE) {
                        imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                        linear_primary.setVisibility(View.VISIBLE);
                    } else {
                        linear_primary.setVisibility(View.GONE);
                        imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    }
                } else {
                    switch_Primary.setChecked(true);
                    if (linear_primary.getVisibility() == View.GONE) {
                        imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                        linear_primary.setVisibility(View.VISIBLE);
                    } else {
                        linear_primary.setVisibility(View.GONE);
                        imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    }
                }
            }
        });


        if(sessionDCM!=null){


            edtAddressFieldP.setText(sessionDCM.defaultAddress);
            edtBuildingNumberP.setText(sessionDCM.defaultBuildingName);
            edtFloorNumberP.setText(sessionDCM.defaultFloorNumber);
            edtAddressFieldS.setText(sessionDCM.address2);
            edtBuildingNumberS.setText(sessionDCM.buildingName2);
            edtFloorNumberS.setText(sessionDCM.floorNumber2);

        }else {
            boolean valid = true;

            if (edtAddressFieldP.getText().toString().length() == 0) {
                edtAddressFieldP.setError("Address cannot be Blank");
                valid = false;
            }

            if (edtBuildingNumberP.getText().toString().length() == 0) {
                edtBuildingNumberP.setError("Building Number be Blank");
                valid = false;
            }



            if (edtFloorNumberP.getText().toString().length() == 0) {
                edtFloorNumberP.setError("Floor Number cannot be Blank");
                valid = false;
            }

            if (edtAddressFieldS.getText().toString().length() == 0) {
                edtAddressFieldS.setError("Address Field cannot be Blank");
                valid = false;
            }

            if (edtBuildingNumberS.getText().toString().length() == 0) {
                edtBuildingNumberS.setError("Building Number cannot be Blank");
                valid = false;
            }

            if (edtFloorNumberS.getText().toString().length() == 0) {
                edtFloorNumberS.setError("Floor Number cannot be Blank");
                valid = false;
            }


            if (valid == true) {

                sessionDCM.defaultAddress=edtAddressFieldP.getText().toString();
                sessionDCM.defaultBuildingName=edtBuildingNumberP.getText().toString();
                sessionDCM.defaultFloorNumber=edtFloorNumberP.getText().toString();
                sessionDCM.address2=edtAddressFieldS.getText().toString();
                sessionDCM.buildingName2=edtBuildingNumberS.getText().toString();
                sessionDCM.floorNumber2=edtFloorNumberS.getText().toString();


                sessionDCM.address2 = messageAddress;
                sessionDCM.address2=messageAddress;
                sessionDAO.update(sessionDCM);
                edtAddressFieldS.setText(messageAddress);


                driverDCM.defaultAddress=edtAddressFieldP.getText().toString();
                driverDCM.defaultBuildingName=edtBuildingNumberP.getText().toString();
                driverDCM.defaultFloorNumber=edtFloorNumberP.getText().toString();
                driverDCM.address2=edtAddressFieldS.getText().toString();
                driverDCM.buildingName2=edtBuildingNumberS.getText().toString();
                driverDCM.floorNumber2=edtFloorNumberS.getText().toString();
                driverDCM.defaultAddress = messageAddress;
                driverDAO.update(driverDCM);
            }

        }











//        edtAddressFieldP.setText(sessionDCM.defaultAddress);
//        edtBuildingNumberP.setText(sessionDCM.defaultBuildingName);
//        edtFloorNumberP.setText(sessionDCM.defaultFloorNumber);
//        edtAddressFieldS.setText(sessionDCM.address2);
//        edtBuildingNumberS.setText(sessionDCM.buildingName2);
//        edtFloorNumberS.setText(sessionDCM.floorNumber2);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mRequestCodeP && resultCode == RESULT_OK){
            messageAddress = data.getStringExtra("messageAddress");
            sessionDCM=new SessionDCM();
            sessionDCM.defaultAddress = messageAddress;
            sessionDCM.defaultAddress=messageAddress;
            sessionDAO.update(sessionDCM);

            edtAddressFieldP.setText(messageAddress);
            driverDCM=new DriverDCM();
            driverDCM.defaultAddress = messageAddress;
            driverDAO.update(driverDCM);

        }
        else if(requestCode == mRequestCodeS && resultCode == RESULT_OK){
            messageAddress = data.getStringExtra("messageAddress");

            sessionDCM=new SessionDCM();
            sessionDCM.address2 = messageAddress;
            sessionDCM.address2=messageAddress;
            sessionDAO.update(sessionDCM);
            edtAddressFieldS.setText(messageAddress);


            driverDCM=new DriverDCM();
            driverDCM.defaultAddress = messageAddress;
            driverDAO.update(driverDCM);


        }
    }



}

