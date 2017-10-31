package com.justimaginethat.gogodriver.activity_settings.updateModule;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.UpdateMobileNumberProfileAsync;
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.UserExistsCheckSettingsMobileAsync;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Lion-1 on 4/17/2017.
 */

public class UpdateMobileNumberProfile extends AppCompatActivity {

    public ImageView imageUpdate;
    public EditText edtPhoneNumber;
    public String mobileNumber;
    public Context context;
    public TextView title_text;
    public ImageView imgBackRight;
    public ImageView imgBack;

    public List<SessionDCM> sessionDCMs=new ArrayList<>();
    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();
    public int mRequestCodeName = 100;
    public UpdateMobileNumberProfile updateMobileProfile;
    public boolean valid = false;
    private AuthCallback authCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_mobile_number);
        context=this;
        updateMobileProfile =this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);

        title_text=(TextView)findViewById(R.id.title_text);
        imgBackRight=(ImageView)findViewById(R.id.imgBackRight);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        imageUpdate=(ImageView)findViewById(R.id.imageUpdate);
        edtPhoneNumber =(EditText)findViewById(R.id.edtPhoneNumber);


        sessionDAO=new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCMs=sessionDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        title_text.setText("Update Profile");
        mobileNumber=sessionDCMs.get(0).mobileNumber;
        edtPhoneNumber.setText(sessionDCMs.get(0).mobileNumber);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valid) {
                  startOTPProcess();
                }
            }
        });

        edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if(hasFocus == false && edtPhoneNumber.getText().toString().length() <= 10 || edtPhoneNumber.getText().toString().length() >= 7)
                {
                    new UserExistsCheckSettingsMobileAsync(updateMobileProfile,edtPhoneNumber.getText().toString()).execute();
                }


            }
        });

        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (!Patterns.PHONE.matcher(edtPhoneNumber.getText().toString()).matches()) {
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                    edtPhoneNumber.setError("Please Enter Valid Mobile Number");
                    valid = false;
                }
                else if (edtPhoneNumber.getText().toString().length() > 10 || edtPhoneNumber.getText().toString().length() < 7) {
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                    edtPhoneNumber.setError("Mobile number can be minimum 7 and maximum 10 digit long.");
                    valid = false;
                }
                else
                {
                    edtPhoneNumber.setError(null);

//                    valid = true;
                    new UserExistsCheckSettingsMobileAsync(updateMobileProfile,edtPhoneNumber.getText().toString()).execute();
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
    private void startOTPProcess() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(FixLabels.TWITTER_KEY, FixLabels.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        Digits.disableSandbox();
        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                if(phoneNumber.contains(edtPhoneNumber.getText().toString()))
                {
                    mobileNumber = edtPhoneNumber.getText().toString();
                    edtPhoneNumber.setText(mobileNumber);
                    new UpdateMobileNumberProfileAsync(updateMobileProfile).execute();
                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("To verify your mobile number ,you must need to use same mobile number that you used at registration form!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                }


            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                Toast.makeText(context, "failed to verify your mobile number.", Toast.LENGTH_SHORT).show();
            }
        };
        Digits.logout();
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                .withAuthCallBack(authCallback)
                .withEmailCollection(false)
//                .withPhoneNumber(edtPhoneNumber.getText().toString().trim().replace(" ", ""));//sp 9-5-2017
        .withPhoneNumber("+961"+edtPhoneNumber.getText().toString().trim().replace(" ", ""));
        Digits.authenticate(authConfigBuilder.build());
    }


}
