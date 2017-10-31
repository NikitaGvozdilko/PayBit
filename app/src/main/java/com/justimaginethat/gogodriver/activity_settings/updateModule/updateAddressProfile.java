package com.justimaginethat.gogodriver.activity_settings.updateModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.UpdateAddressProfileAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.AddressMapPickerActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lion-1 on 4/17/2017.
 */

public class updateAddressProfile extends AppCompatActivity {

    public ImageView imgLocation;

    public EditText edtAddress;
    public EditText edtBuildingNumber;
    public EditText edtFloorNumber;

    public String address1;
    public String messageAddress;
    public String latitude1 ="";
    public String longitude1 ="";

    public String buildingName1;
    public String floorNumber1;

    public Context context;
    public TextView title_text;
    public ImageView imgBackRight;
    public ImageView imgBack;

    public List<SessionDCM> sessionDCMs=new ArrayList<>();
    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();
    public int mRequestCodeAddress = 100;

    public updateAddressProfile updateAddressProfile;
    private boolean valid = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_address);
        context=this;
        updateAddressProfile =this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);

        title_text=(TextView)findViewById(R.id.title_text);
        imgBackRight=(ImageView)findViewById(R.id.imgBackRight);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        imgLocation=(ImageView)findViewById(R.id.imgLocation);
        edtAddress=(EditText)findViewById(R.id.edtAddress);

        edtBuildingNumber=(EditText)findViewById(R.id.edtBuildingNumber);
        edtFloorNumber=(EditText)findViewById(R.id.edtFloorNumber);




        sessionDAO=new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCMs=sessionDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        title_text.setText("Update Profile");
        address1=sessionDCMs.get(0).address1;
        buildingName1=sessionDCMs.get(0).buildingName1;
        floorNumber1=sessionDCMs.get(0).floorNumber1;

        edtAddress.setText(address1);
        edtBuildingNumber.setText(buildingName1);
        edtFloorNumber.setText(floorNumber1);
        latitude1 = sessionDCMs.get(0).latitude1;
        longitude1 = sessionDCMs.get(0).longitude1;

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(updateAddressProfile.this,AddressMapPickerActivity.class);
                startActivityForResult(intent, mRequestCodeAddress);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAddress.setError(null);
                edtAddress.setError(null);
                edtFloorNumber.setError(null);
                valid = true;
                if (edtAddress.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Address  cannot be Blank", Toast.LENGTH_LONG).show();
                    imgLocation.callOnClick();
                    edtAddress.setError("Please Enter Your Address Manually");
                    valid = false;
                }

                if (edtBuildingNumber.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
                    edtBuildingNumber.setError("Please Enter Your Building Name");
                    valid = false;
                }

                if (edtFloorNumber.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
                    edtFloorNumber.setError("Please Enter Your Floor Number");
                    valid = false;
                }


                if(valid) {
                    messageAddress = edtAddress.getText().toString();
//                    edtAddress.setText(messageAddress);


                    buildingName1 = edtBuildingNumber.getText().toString();
//                    edtBuildingNumber.setText(buildingName1);


                    floorNumber1 = edtFloorNumber.getText().toString();
//                    edtFloorNumber.setText(floorNumber1);


                    new UpdateAddressProfileAsync(updateAddressProfile).execute();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });




        edtAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if(hasFocus == true) {

                    if (edtAddress.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Address  cannot be Blank", Toast.LENGTH_LONG).show();
                        imgLocation.callOnClick();
                        edtAddress.setError("Please Enter Your Address Manually");
                        valid = false;
                    }
                    else
                    {
                        edtAddress.setError(null);

                        valid = true;
                    }
                }
            }
        });

        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (edtAddress.getText().toString().length() == 0) {

//                    Toast.makeText(getApplicationContext(), "Address  cannot be Blank", Toast.LENGTH_LONG).show();
                    edtAddress.setError("Please Enter Your Address Manually Or tap/click on address icon for address picker.");
                    valid = false;
                }
                else
                {
                    edtAddress.setError(null);

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






        edtBuildingNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here

                if (edtBuildingNumber.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
                    edtBuildingNumber.setError("Please Enter Your Building Name");
                    valid = false;
                }

            }
        });

        edtBuildingNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (edtBuildingNumber.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
//                    edtBuildingNumber.setError("Building Number cannot be Blank");
                    edtBuildingNumber.setError("Please Enter Your Building Name");
                    valid = false;
                }
                else
                {
                    edtBuildingNumber.setError(null);

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


        edtFloorNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (edtFloorNumber.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
                    edtFloorNumber.setError("Please Enter Your Floor Number");
                    valid = false;
                }
                else
                {
                    edtFloorNumber.setError(null);
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == mRequestCodeAddress && resultCode == RESULT_OK){
            messageAddress = data.getStringExtra("messageAddress");
            latitude1 = data.getStringExtra("latitude1");
            longitude1 = data.getStringExtra("longitude1");
            edtAddress.setText(messageAddress);
        }


    }

}
