package com.justimaginethat.gogodriver.activity_reset_password_pkg;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIResetPasswordRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIResetPasswordResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;
import com.justimaginethat.gogodriver.activity_settings.activity_update_setting;

import java.util.HashMap;

/**
 * Created by Jay Bhavsar on 04/08/2016.
 */
public class activity_reset_password extends Activity {


    private static final String TAG = "activity_pkg";
    APIResetPasswordRequestModel requestModel;
    BaseAPIResponseModel<APIResetPasswordResponseModel> responseDataModel = new BaseAPIResponseModel<APIResetPasswordResponseModel>();
    APIResetPasswordResponseModel responseModel = new APIResetPasswordResponseModel();

    private EditText passwordPin;
    private EditText confirmPin;
    private LinearLayout btnSumit;
    private Context context;
    String identifier;
    public boolean validEx;

    @Override
    public void onBackPressed() {
        super.onBackPressed();


//        Intent i = new Intent(activity_reset_password.this,activity_login.class);
//        startActivity(i);
        finish();//sp 10-5-2017


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        context = this;


        identifier = getIntent().getStringExtra("identifier").toString();
        passwordPin = (EditText) findViewById(R.id.edtPassword);
        confirmPin = (EditText) findViewById(R.id.edtCPassword);

        btnSumit = (LinearLayout) findViewById(R.id.ibtnSubmit);


        passwordPin.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (passwordPin.getText().toString().length() == 0) {
                    passwordPin.setError("Please Enter Your Desired  Password.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    validEx = false;
                } else if (passwordPin.getText().toString().length() > 0 && passwordPin.getText().toString().length() < 6) {
                    passwordPin.setError("Password requires minimum 6 character length.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    validEx = false;
                } else {
                    passwordPin.setError(null);

                    validEx = true;
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });


        confirmPin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if (hasFocus == false) {
                    if (confirmPin.getText().toString().length() == 0) {
                        confirmPin.setError("Please re-enter password.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        validEx = false;
                    } else if (confirmPin.getText().toString().length() > 0 && confirmPin.getText().toString().length() < 6) {
                        confirmPin.setError("Confirm password requires minimum 6 character length.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        validEx = false;
                    } else if (!confirmPin.getText().toString().equals(passwordPin.getText().toString())) {
                        confirmPin.setError("Confirm password do not match!,Please try again.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        validEx = false;
                    } else {
                        confirmPin.setError(null);

                        validEx = true;

                    }
                }

            }
        });
        confirmPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (confirmPin.getText().toString().length() == 0) {
                    confirmPin.setError("Please re-enter password.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    validEx = false;
                } else if (confirmPin.getText().toString().length() > 0 && confirmPin.getText().toString().length() < 6) {
                    confirmPin.setError("Confirm password requires minimum 6 character length.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    validEx = false;
                } else if (!confirmPin.getText().toString().equals(passwordPin.getText().toString())) {
                    confirmPin.setError("Confirm password do not match!,Please try again.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    validEx = false;
                } else {
                    confirmPin.setError(null);

                    validEx = true;

                }


            }
        });

        btnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                //Validation for Blank Field


                if (passwordPin.getText().toString().length() == 0 || passwordPin.getText().toString().length() < 6) {
                    passwordPin.setError("Password Pin incorrect");
                    //Validation for Website Address
                    // Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();

                    valid = false;
                }
                if (confirmPin.getText().toString().length() == 0 || confirmPin.getText().toString().length() < 6) {
                    confirmPin.setError("Confirm Password Pin incorrect");
                    //Validation for Website Address
                    // Toast.makeText(getApplicationContext(), "Confirm Password Pin incorrect", Toast.LENGTH_LONG).show();

                    valid = false;
                }
                if (!passwordPin.getText().toString().equals(confirmPin.getText().toString())) {
                    confirmPin.setError("Confirm Password Pin do not match with password pin above.");
                    //Validation for Website Address
                    //  Toast.makeText(getApplicationContext(), "Confirm Password Pin do not match with password pin above.", Toast.LENGTH_LONG).show();

                    valid = false;
                }

                if (valid == true && validEx == true) {

                    requestModel = new APIResetPasswordRequestModel();
                    if (identifier.contains("@")) {
                        requestModel.emailAddress = identifier;
                    } else {
                        requestModel.mobileNumber = identifier;
                    }
                    requestModel.password = passwordPin.getText().toString();

                    new submitPasswordResetRequest().execute();
                }

            }
        });


    }


    public class submitPasswordResetRequest extends AsyncTask<Void, Void, Boolean> {

        public CustomProgressBar progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(activity_reset_password.this, "Submitting", "", true, false);

            progressDialog = CustomProgressBar.show(activity_reset_password.this, "", true, false, null);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();


            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestModel));
            String str = helperSP.requestHTTPResponse(APILinks.APIResetPassword, map, "POST", false);

            if (str.equals("")) {
                return false;
            } else {
                responseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseDataModel.Data);
                responseModel = gson.fromJson(str2, APIResetPasswordResponseModel.class);
                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (response == false) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else {
                if (responseModel != null) {
                    if (responseModel.errorLogId <= 0) {

                        if ((responseModel.emailAddress != null && responseModel.mobileNumber != null)) {
                            if (responseModel.isChanged == true && (responseModel.emailAddress.equals(identifier) || responseModel.mobileNumber.equals(identifier))) {


                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                        .setTitle("Reset Success")
                                        .setCancelable(false)
                                        .setMessage("You password has been reset,Please. try login with new password.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (activity_update_setting.updateSetting != null) {
                                                    activity_update_setting.updateSetting.finish();
                                                }
                                                if (fragment_activity_landing_user.fragment_activity_landing_user != null) {
                                                    fragment_activity_landing_user.fragment_activity_landing_user.finish();
                                                }

                                                if (fragment_activity_landing_driver.fragment_activity_landing_driver != null) {
                                                    fragment_activity_landing_driver.fragment_activity_landing_driver.finish();
                                                }


                                                Intent i = new Intent(activity_reset_password.this, activity_login.class);
                                                startActivity(i);
                                                finish();//sp 9-5-2017


                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert);
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                        .setTitle("Reset Status")
                                        .setCancelable(false)
                                        .setMessage("Password Reset failed , try again.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert);
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            }
                        }
                        else
                        {
                            if(responseModel.isChanged == true && (responseModel.emailAddress!=null || responseModel.mobileNumber!=null)){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle("Reset Success")
                                    .setCancelable(false)
                                    .setMessage("You password has been reset,Please. try login with new password.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (activity_update_setting.updateSetting != null) {
                                                activity_update_setting.updateSetting.finish();
                                            }
                                            if (fragment_activity_landing_user.fragment_activity_landing_user != null) {
                                                fragment_activity_landing_user.fragment_activity_landing_user.finish();
                                            }

                                            if (fragment_activity_landing_driver.fragment_activity_landing_driver != null) {
                                                fragment_activity_landing_driver.fragment_activity_landing_driver.finish();
                                            }


                                            Intent i = new Intent(activity_reset_password.this, activity_login.class);
                                            startActivity(i);
                                            finish();//sp 9-5-2017


                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle("Reset Status")
                                    .setCancelable(false)
                                    .setMessage("Password Reset failed , try again.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                        }

                    } else {
                        Log.i(TAG, "APIResetPassword Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                    }

                } else {
                    Log.i(TAG, "APIResetPassword Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                }
            }
        }

    }


}

