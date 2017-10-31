package com.justimaginethat.gogodriver.activity_landing_pkg.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.gms.nearby.messages.internal.Update;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.UpdateAddressAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.AddressMapPickerActivity;

import java.sql.SQLException;

/**
 * Created by Lion-1 on 3/17/2017.
 */

public class activity_default_address_setter extends AppCompatActivity {
    LinearLayout linearMainPrimary;
    LinearLayout linear_primary;
    LinearLayout linearAction_imageP;
    ImageView imgArrow_down_P;
    LinearLayout linearAddressP;
    LinearLayout addressFieldP;
    LinearLayout addressHomeP;
    LinearLayout addressFloorP;
    EditText edtBuildingNumberP;
    EditText edtFloorNumberP;
    LinearLayout linearGetAddressP;
    EditText edtAddressFieldP;
    Switch switch_Primary;


    LinearLayout linearMainSecondary;
    LinearLayout linear_secondary;
    LinearLayout linearAction_imageS;
    ImageView imgArrow_down_S;
    LinearLayout linearAddressS;
    LinearLayout addressFieldS;
    LinearLayout addressHomeS;
    LinearLayout addressFloorS;
    EditText edtBuildingNumberS;
    EditText edtFloorNumberS;
    LinearLayout linearGetAddressS;
    EditText edtAddressFieldS;
    Switch switch_Secondary;

    public  activity_default_address_setter activity_default_address_setter;
    public View mCustomView;
    ImageView imgBack;
    ImageView btnSave;


    public SessionDCM sessionDCM;
    public SessionDAO sessionDAO;
    public Context context;


    public UserDCM userDCM;
    public UserDAO userDAO;




    private int mRequestCodeP = 100;
    private int mRequestCodeS = 101;
    private boolean valid  = false;
    private String lonP;
    private String latP;
    private String lonS;
    private String latS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbar_address_layout);
        context=this;

        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());

        activity_default_address_setter = this;
        sessionDCM = new SessionDCM();
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);
        mCustomView = getSupportActionBar().getCustomView();


        imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
        btnSave = (ImageView) mCustomView.findViewById(R.id.imgBackRight);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentBack = new Intent(activity_default_address_setter.this, fragment_activity_landing_user.class);
//                startActivity(intentBack);
                finish();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (valid == true) {

                    if (switch_Primary.isChecked()) {
                        sessionDCM.defaultAddress = edtAddressFieldP.getText().toString();
                        sessionDCM.defaultBuildingName = edtBuildingNumberP.getText().toString();
                        sessionDCM.defaultFloorNumber = edtFloorNumberP.getText().toString();
                        sessionDCM.defaultLatitude = String.valueOf(latP);
                        sessionDCM.defaultLongitude = String.valueOf(lonP);
                    } else {
                        sessionDCM.defaultAddress = edtAddressFieldS.getText().toString();
                        sessionDCM.defaultBuildingName = edtBuildingNumberS.getText().toString();
                        sessionDCM.defaultFloorNumber = edtFloorNumberS.getText().toString();
                        sessionDCM.defaultLatitude = String.valueOf(latS);
                        sessionDCM.defaultLongitude = String.valueOf(lonS);
                    }

                    sessionDCM.address1 = edtAddressFieldP.getText().toString();
                    sessionDCM.buildingName1 = edtBuildingNumberP.getText().toString();
                    sessionDCM.floorNumber1 = edtFloorNumberP.getText().toString();
                    sessionDCM.latitude1 = String.valueOf(latP);
                    sessionDCM.longitude1 = String.valueOf(lonP);

                    sessionDCM.address2 = edtAddressFieldS.getText().toString();
                    sessionDCM.buildingName2 = edtBuildingNumberS.getText().toString();
                    sessionDCM.floorNumber2 = edtFloorNumberS.getText().toString();
                    sessionDCM.latitude2 = String.valueOf(latS);
                    sessionDCM.longitude2 = String.valueOf(lonS);

                    new UpdateAddressAsync(activity_default_address_setter, sessionDCM).execute();
                }

            }
        });





//        LayoutInflater mInflater = LayoutInflater.from(this);

//        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        linearMainPrimary = (LinearLayout) findViewById(R.id.linearMainPrimary);
        linear_primary = (LinearLayout) findViewById(R.id.linear_primary);
        linearAction_imageP = (LinearLayout) findViewById(R.id.linearAction_imageP);
        imgArrow_down_P = (ImageView) findViewById(R.id.imgArrow_down_P);
        linearAddressP = (LinearLayout) findViewById(R.id.linearAddressP);
        edtBuildingNumberP=(EditText)findViewById(R.id.edtBuildingNumberP);
        edtFloorNumberP=(EditText)findViewById(R.id.edtFloorNumberP);

        addressFieldP = (LinearLayout) findViewById(R.id.addressFieldP);
        addressHomeP = (LinearLayout) findViewById(R.id.addressHomeP);
        addressFloorP = (LinearLayout) findViewById(R.id.addressFloorP);
        linearGetAddressP = (LinearLayout) findViewById(R.id.linearGetAddressP);
        edtAddressFieldP = (EditText) findViewById(R.id.edtAddressFieldP);
        switch_Primary = (Switch) findViewById(R.id.switch_Primary);



        linearMainSecondary = (LinearLayout) findViewById(R.id.linearMainSecondary);
        linear_secondary = (LinearLayout) findViewById(R.id.linear_secondary);
        linearAction_imageS = (LinearLayout) findViewById(R.id.linearAction_imageS);
        imgArrow_down_S = (ImageView) findViewById(R.id.imgArrow_down_S);
        linearAddressS = (LinearLayout) findViewById(R.id.linearAddressS);
        addressFieldS = (LinearLayout) findViewById(R.id.addressFieldS);
        addressHomeS = (LinearLayout) findViewById(R.id.addressHomeS);
        addressFloorS = (LinearLayout) findViewById(R.id.addressFloorS);
        edtBuildingNumberS=(EditText)findViewById(R.id.edtBuildingNumberS);
        edtFloorNumberS=(EditText)findViewById(R.id.edtFloorNumberS);

        linearGetAddressS = (LinearLayout) findViewById(R.id.linearGetAddressS);
        edtAddressFieldS = (EditText) findViewById(R.id.edtAddressFieldS);
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
                Intent intent = new Intent(activity_default_address_setter.this, AddressMapPickerActivity.class);
                startActivityForResult(intent, mRequestCodeP);
//                Intent intent=new Intent(activity_default_address_setter.this,AddressMapPickerActivity.class);
//                startActivity(intent);
            }
        });
        linearGetAddressS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_default_address_setter.this, AddressMapPickerActivity.class);
                startActivityForResult(intent, mRequestCodeS);
            }
        });





        switch_Primary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                sessionDCM.defaultAddress = edtAddressFieldP.getText().toString();
                sessionDCM.defaultBuildingName = edtBuildingNumberP.getText().toString();
                sessionDCM.defaultFloorNumber = edtFloorNumberP.getText().toString();
                sessionDCM.defaultLatitude = String.valueOf(latP);
                sessionDCM.defaultLongitude = String.valueOf(lonP);



                if (switch_Primary.isChecked()) {

                    switch_Secondary.setChecked(false);

                    imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    linear_primary.setVisibility(View.VISIBLE);

                        imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                        linear_secondary.setVisibility(View.GONE);




//                    Toast.makeText(getApplicationContext(), "your select :- " + switch_Driver.getText().toString(), Toast.LENGTH_LONG).show(); // display the current state for switch's
                } else {
                    switch_Secondary.setChecked(true);


                        imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                        linear_secondary.setVisibility(View.VISIBLE);


                    imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    linear_primary.setVisibility(View.GONE);

//                    Toast.makeText(getApplicationContext(), "your select :- " + switch_Driver.getText().toString(), Toast.LENGTH_LONG).show(); // display the current state for switch's
                }




            }
        });

        switch_Secondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionDCM.defaultAddress = edtAddressFieldS.getText().toString();
                sessionDCM.defaultBuildingName = edtBuildingNumberS.getText().toString();
                sessionDCM.defaultFloorNumber = edtFloorNumberS.getText().toString();
                sessionDCM.defaultLatitude = String.valueOf(latS);
                sessionDCM.defaultLongitude = String.valueOf(lonS);


                switch_Primary.setChecked(false);
                imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                if (switch_Secondary.isChecked()) {


                    switch_Primary.setChecked(false);


                    imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    linear_secondary.setVisibility(View.VISIBLE);


                    imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    linear_primary.setVisibility(View.GONE);



//                    Toast.makeText(getApplicationContext(), "your select :- " + switch_Driver.getText().toString(), Toast.LENGTH_LONG).show(); // display the current state for switch's
                } else {


                    switch_Primary.setChecked(true);

                    imgArrow_down_P.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    linear_primary.setVisibility(View.VISIBLE);

                    imgArrow_down_S.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    linear_secondary.setVisibility(View.GONE);




//                    Toast.makeText(getApplicationContext(), "your select :- " + switch_Driver.getText().toString(), Toast.LENGTH_LONG).show(); // display the current state for switch's
                }

            }
        });
        edtAddressFieldP.setText(sessionDCM.address1);
        edtBuildingNumberP.setText(sessionDCM.buildingName1);
        edtFloorNumberP.setText(sessionDCM.floorNumber1);
        edtAddressFieldS.setText(sessionDCM.address2);
        edtBuildingNumberS.setText(sessionDCM.buildingName2);
        edtFloorNumberS.setText(sessionDCM.floorNumber2);



        latP = sessionDCM.latitude1;
        lonP = sessionDCM.longitude1;
        latS = sessionDCM.latitude2;
        lonS = sessionDCM.longitude2;









        edtAddressFieldP.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if(hasFocus == true) {

                    if (edtAddressFieldP.getText().toString().length() == 0) {
                        linearGetAddressP.callOnClick();
                        edtAddressFieldP.setError("Please Enter Your Address Manually");
                        valid = false;
                    }
                    else
                    {
                        edtAddressFieldP.setError(null);
//                        requestModel.UserDCM.addressHomeP = edtAddressFieldP.getText().toString();
                        valid = true;
                    }
                }
            }
        });

        edtAddressFieldP.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (edtAddressFieldP.getText().toString().length() == 0) {
                    edtAddressFieldP.setError("Please Enter Your Address Manually Or tap/click on address icon for address picker.");
                    valid = false;
                }
                else
                {
                    edtAddressFieldP.setError(null);
//                    requestModel.UserDCM.addressHomeP = edtAddressFieldP.getText().toString();
                    valid = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });





        edtAddressFieldS.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if(hasFocus == true) {

                    if (edtAddressFieldS.getText().toString().length() == 0) {
                        linearGetAddressS.callOnClick();
                        edtAddressFieldS.setError("Please Enter Your Address Manually");
                        valid = false;
                    }
                    else
                    {
                        edtAddressFieldS.setError(null);
//                        requestModel.UserDCM.addressHomeP = edtAddressFieldP.getText().toString();
                        valid = true;
                    }
                }
            }
        });

        edtAddressFieldS.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (edtAddressFieldS.getText().toString().length() == 0) {
                    edtAddressFieldS.setError("Please Enter Your Address Manually Or tap/click on address icon for address picker.");
                    valid = false;
                }
                else
                {
                    edtAddressFieldS.setError(null);
//                    requestModel.UserDCM.addressHomeP = edtAddressFieldP.getText().toString();
                    valid = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });




        edtBuildingNumberP.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (edtBuildingNumberP.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
//                    edtBuildingNumber.setError("Building Number cannot be Blank");
                    edtBuildingNumberP.setError("Please Enter Your Building Name");
                    valid = false;
                }
                else
                {
                    edtBuildingNumberP.setError(null);
//                    requestModel.UserDCM.buildingName1 = edtBuildingNumber.getText().toString();
                    valid = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });




        edtBuildingNumberS.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (edtBuildingNumberS.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
//                    edtBuildingNumber.setError("Building Number cannot be Blank");
                    edtBuildingNumberS.setError("Please Enter Your Building Name");
                    valid = false;
                }
                else
                {
                    edtBuildingNumberS.setError(null);
//                    requestModel.UserDCM.buildingName1 = edtBuildingNumber.getText().toString();
                    valid = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });






        edtFloorNumberP.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (edtFloorNumberP.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
                    edtFloorNumberP.setError("Please Enter Your Floor Number");
                    valid = false;
                }
                else
                {
                    edtFloorNumberP.setError(null);
//                    requestModel.UserDCM.floorNumber1 = edtFloorNumber.getText().toString();
                    valid = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

        edtFloorNumberS.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (edtFloorNumberS.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
                    edtFloorNumberS.setError("Please Enter Your Floor Number");
                    valid = false;
                }
                else
                {
                    edtFloorNumberS.setError(null);
//                    requestModel.UserDCM.floorNumber1 = edtFloorNumber.getText().toString();
                    valid = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });




        if(sessionDCM.defaultAddress.equals(sessionDCM.address1))
        {
            switch_Primary.setChecked(true);
            switch_Secondary.setChecked(false);
            switch_Primary.callOnClick();
        }
        else
        {
            switch_Primary.setChecked(false);
            switch_Secondary.setChecked(true);
            switch_Secondary.callOnClick();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mRequestCodeP && resultCode == RESULT_OK){
            String messageAddress = data.getStringExtra("messageAddress");
            latP = data.getStringExtra("latitude1");
            lonP = data.getStringExtra("longitude1");
            edtAddressFieldP.setText(messageAddress);

        }
        else if(requestCode == mRequestCodeS && resultCode == RESULT_OK){
            String  messageAddress = data.getStringExtra("messageAddress");
            latS = data.getStringExtra("latitude1");
            lonS = data.getStringExtra("longitude1");
            edtAddressFieldS.setText(messageAddress);


        }
    }




}

