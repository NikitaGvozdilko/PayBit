package com.justimaginethat.gogodriver.activity_otp_verification_pkg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIOTPVerifyRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIOTPVerifyResponseModel;
import com.justimaginethat.gogodriver.APIModels.APIResendOTPRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIResendOTPResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_reset_password_pkg.activity_reset_password;

import java.util.HashMap;

/**
 * Created by Jay Bhavsar on 04/08/2016.
 */
public class activity_otp_verification extends Activity {


    private static final String TAG = "activity_pkg";
    APIOTPVerifyRequestModel requestModel;
    BaseAPIResponseModel<APIOTPVerifyResponseModel> responseDataModel = new BaseAPIResponseModel<APIOTPVerifyResponseModel>();
    APIOTPVerifyResponseModel responseModel = new APIOTPVerifyResponseModel();
    private EditText otp;
    String emailAddress;
    private LinearLayout btnSumit;
    public ImageView btnResend;
    public Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        context = this;

        emailAddress = getIntent().getStringExtra("emailAddress").toString();
        otp = (EditText) findViewById(R.id.edtConfirmOTP);

        btnSumit = (LinearLayout) findViewById(R.id.ibtnSubmit);
        btnResend = (ImageView) findViewById(R.id.btnResend);

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new submitResendOTP().execute();

            }
        });

        btnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                //Validation for Blank Field

                if (otp.toString().length() == 1) {
                    //Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid OTP,OTP is required to reset new password.", Toast.LENGTH_LONG).show();
                    otp.setError("Invalid OTP,OTP is required to reset new password.");
                    return;
                }

                if (valid == true) {

                    requestModel = new APIOTPVerifyRequestModel();
                    requestModel.otp = otp.getText().toString();
                    requestModel.emailAddress = emailAddress;

                    new submitPasswordResetRequest().execute();
                }

            }
        });

    }


    public class submitPasswordResetRequest extends AsyncTask<Void, Void, Boolean> {

        CustomProgressBar progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(activity_otp_verification.this, "Submitting", "", true, false);

            progressDialog = CustomProgressBar.show(activity_otp_verification.this, "", true, false, null);

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();


            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestModel));
            String str = helperSP.requestHTTPResponse(APILinks.APIOTPVerify, map, "POST", false);

            if (str.equals("")) {
                return false;
            } else {
                responseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseDataModel.Data);
                responseModel = gson.fromJson(str2, APIOTPVerifyResponseModel.class);
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
                        if (responseModel.isOTPValid == true && responseModel.emailAddress.equals(emailAddress)) {
                            Intent intentLocationScreen = new Intent(activity_otp_verification.this, activity_reset_password.class).
                                    putExtra("identifier", emailAddress);
                            startActivity(intentLocationScreen);
                            finish();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("You might have entered wrong email or mobile number.Ple. try again.")
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
                    } else {
                        Log.i(TAG, "APIStep5ForgetPassword Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                    }

                } else {
                    Log.i(TAG, "APIStep5ForgetPassword Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                }
            }
        }

    }


    APIResendOTPRequestModel requestModel2;
    BaseAPIResponseModel<APIResendOTPResponseModel> responseDataModel2 = new BaseAPIResponseModel<APIResendOTPResponseModel>();
    APIResendOTPResponseModel responseModel2 = new APIResendOTPResponseModel();

    public class submitResendOTP extends AsyncTask<Void, Void, Boolean> {

        public CustomProgressBar progressDialog;
//        public activity_otp_verification otpVerification;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(activity_otp_verification.this, "Submitting", "", true, false);

            progressDialog = CustomProgressBar.show(activity_otp_verification.this, "", true, false, null);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();

            requestModel2 = new APIResendOTPRequestModel();
            requestModel2.emailAddress = emailAddress;

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestModel2));
            String str = helperSP.requestHTTPResponse(APILinks.APIResendOTP, map, "POST", false);

            if (str.equals("")) {
                return false;
            } else {
                responseDataModel2 = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseDataModel2.Data);
                responseModel2 = gson.fromJson(str2, APIResendOTPResponseModel.class);
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
                if (responseModel2 != null) {
                    if (responseModel2.errorLogId <= 0) {
                        if (responseModel2.emailAddress.equals(emailAddress)) {
                            Toast.makeText(context, "OTP resend to you email address.Ple. check it.", Toast.LENGTH_LONG).show();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("You might have entered wrong email or mobile number.Ple. try again.")
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
                    } else {
                        Log.i(TAG, "APIStep5ForgetPassword Failed from server Error Log : " + responseModel2.errorLogId + "\n errorURL :" + responseModel2.errorURL);
                    }

                } else {
                    Log.i(TAG, "APIStep5ForgetPassword Failed from server Error Log : " + responseModel2.errorLogId + "\n errorURL :" + responseModel2.errorURL);
                }
            }
        }

    }


}

