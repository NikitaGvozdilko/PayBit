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
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.UpdateNameProfileAsync;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lion-1 on 4/17/2017.
 */

public class updateNameProfile extends AppCompatActivity {

    public ImageView imageUpdate;
    public EditText edtName;
    public String firstName;
    public Context context;
    public TextView title_text;
    public ImageView imgBackRight;
    public ImageView imgBack;

    public List<SessionDCM> sessionDCMs=new ArrayList<>();
    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();
    public int mRequestCodeName = 100;
    public updateNameProfile updateNameProfile;
    private boolean valid = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_name);
        context=this;
        updateNameProfile =this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);

        title_text=(TextView)findViewById(R.id.title_text);
        imgBackRight=(ImageView)findViewById(R.id.imgBackRight);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        imageUpdate=(ImageView)findViewById(R.id.imageUpdate);
        edtName=(EditText)findViewById(R.id.edtName);


        sessionDAO=new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCMs=sessionDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        title_text.setText("Update Profile");
        firstName=sessionDCMs.get(0).firstName;
        edtName.setText(firstName);
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
                    firstName = edtName.getText().toString();
                    edtName.setText(firstName);
                    new UpdateNameProfileAsync(updateNameProfile).execute();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });






        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (edtName.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "First Name cannot be Blank", Toast.LENGTH_LONG).show();
                    edtName.setError("Please Enter Your Name");
                    valid = false;
                }
                else
                {
                    edtName.setError(null);

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



}
