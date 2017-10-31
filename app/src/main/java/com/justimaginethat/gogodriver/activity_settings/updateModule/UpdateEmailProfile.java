package com.justimaginethat.gogodriver.activity_settings.updateModule;

import android.content.Context;
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
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.UpdateEmailProfileAsync;
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.UserExistsCheckSettingsEmailAsync;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lion-1 on 4/17/2017.
 */

public class UpdateEmailProfile extends AppCompatActivity {

    public ImageView imageUpdate;
    public EditText edtEmail;
    public String emailAddress;
    public Context context;
    public TextView title_text;
    public ImageView imgBackRight;
    public ImageView imgBack;

    public List<SessionDCM> sessionDCMs = new ArrayList<>();
    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();
    public int mRequestCodeName = 100;
    public UpdateEmailProfile UpdateEmailProfile;
    public boolean valid = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_email);
        context = this;
        UpdateEmailProfile = this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);
        title_text = (TextView) findViewById(R.id.title_text);
        imgBackRight = (ImageView) findViewById(R.id.imgBackRight);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imageUpdate = (ImageView) findViewById(R.id.imageUpdate);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCMs = sessionDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        title_text.setText("Update Profile");
        emailAddress = sessionDCMs.get(0).emailAddress;
        edtEmail.setText(emailAddress);
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
                    emailAddress = edtEmail.getText().toString();
                    edtEmail.setText(emailAddress);
                    new UpdateEmailProfileAsync(UpdateEmailProfile).execute();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if (hasFocus == false && android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                    new UserExistsCheckSettingsEmailAsync(UpdateEmailProfile, edtEmail.getText().toString()).execute();
                }
            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (edtEmail.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "First Name cannot be Blank", Toast.LENGTH_LONG).show();
                    edtEmail.setError("Please Enter Your Email Address");
                    valid = false;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
//                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    edtEmail.setError("Please enter validEx email address.");
                    valid = false;
                } else {
                    edtEmail.setError(null);
//                    valid = true;
                    new UserExistsCheckSettingsEmailAsync(UpdateEmailProfile, edtEmail.getText().toString()).execute();
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

}
