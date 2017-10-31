package com.justimaginethat.gogodriver.activity_forgot_password_pkg;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIForgotPasswordRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIForgotPasswordResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.ExtendedView.EditTextSegoePrintFont;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_reset_password_pkg.activity_reset_password;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Jay Bhavsar on 04/08/2016.
 */
public class activity_forgot_password extends Activity implements View.OnClickListener {


    private static final String TAG = "activity_pkg";
    APIForgotPasswordRequestModel requestModel;
    BaseAPIResponseModel<APIForgotPasswordResponseModel> responseDataModel = new BaseAPIResponseModel<APIForgotPasswordResponseModel>();
    APIForgotPasswordResponseModel responseModel = new APIForgotPasswordResponseModel();
    private EditText email_mobile;
    private EditText passwordPin;
    private EditText confirmPin;
    private LinearLayout btnSumit;
    private Context context;
    AuthCallback authCallback;
    public String CalledBy = "";

    RelativeLayout phone_auth_fields;
    EditTextSegoePrintFont mVerificationField;
    LinearLayout mVerifyButton;
    ImageView mResendButton;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        context = this;


        email_mobile = (EditText) findViewById(R.id.email_mobile);

        btnSumit = (LinearLayout) findViewById(R.id.ibtnSubmit);

        Intent i = getIntent();
        CalledBy = i.getStringExtra("CalledBy");


        phone_auth_fields=(RelativeLayout)findViewById(R.id.phone_auth_fields);

        phone_auth_fields.setVisibility(View.GONE);
        mVerificationField = (EditTextSegoePrintFont) findViewById(R.id.field_verification_code);


        mVerifyButton = (LinearLayout) findViewById(R.id.button_verify_phone);
        mResendButton = (ImageView) findViewById(R.id.button_resend);

        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);

        btnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                //Validation for Blank Field

                if (!Patterns.EMAIL_ADDRESS.matcher(email_mobile.getText().toString()).matches() && email_mobile.getText().toString().contains("@")) {
                    //Validation for Invalid Email Address
//                    Toast.makeText(getApplicationContext(), "Please enter valid details", Toast.LENGTH_LONG).show();
                    email_mobile.setError("Please enter valid details");
                    return;
                } else if (!Patterns.PHONE.matcher(email_mobile.getText().toString()).matches() && !email_mobile.getText().toString().contains("@")) {
                    //Validation for Invalid Email Address
//                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    email_mobile.setError("Please enter valid details");
                    return;
                }

//                if (password.getText().toString().length() == 0 || password.getText().toString().length() < 6) {
//                    password.setError("Password Pin incorrect");
//                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
//
//                    validEx = false;
//                }
//                if (confirmPin.getText().toString().length() == 0 || confirmPin.getText().toString().length() < 6) {
//                    confirmPin.setError("Confirm Password Pin incorrect");
//                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Confirm Password Pin incorrect", Toast.LENGTH_LONG).show();
//
//                    validEx = false;
//                }
//                if (!password.getText().toString().equals(confirmPin.getText().toString())) {
//                    confirmPin.setError("Confirm Password Pin do not match with password pin above.");
//                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Confirm Password Pin do not match with password pin above.", Toast.LENGTH_LONG).show();
//
//                    validEx = false;
//                }

                if (valid == true) {

                    requestModel = new APIForgotPasswordRequestModel();
                    requestModel.udid = FixLabels.udId;
                    if (email_mobile.getText().toString().contains("@")) {
                        requestModel.email = email_mobile.getText().toString();
                    } else {
                        requestModel.mobileNo = email_mobile.getText().toString();
                    }

//                    requestModelNearbyD.password = password.getText().toString();

                    new submitPasswordResetRequest().execute();
                }

            }
        });


//        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    PhoneNumber.setError("Invalid phone number.");
                    Toast t=Toast.makeText(activity_forgot_password.this,"Invalid Phone Number",Toast.LENGTH_SHORT);
                    t.show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;

                phone_auth_fields.setVisibility(View.VISIBLE);
            }
        };

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if(CalledBy.equals("Settings")) {
//            Intent intentBack = new Intent(activity_forgot_password.this, activity_update_setting.class);
//            startActivity(intentBack);
//            finish();
//
//        }
//        else
//        {
//            Intent intentBack = new Intent(activity_forgot_password.this, activity_login.class);
//            startActivity(intentBack);
//            finish();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);

                break;
            case R.id.button_resend:
                resendVerificationCode(responseModel.countryCode+" "+requestModel.mobileNo, mResendToken);
                break;
        }

    }


    public class
    submitPasswordResetRequest extends AsyncTask<Void, Void, Boolean> {

        CustomProgressBar progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = CustomProgressBar.show(activity_forgot_password.this, "", true, true, null);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();


            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestModel));
            String str = helperSP.requestHTTPResponse(APILinks.APIForgotPassword, map, "POST", false);

            if (str.equals("")) {
                return false;
            } else {
                responseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseDataModel.Data);
                responseModel = gson.fromJson(str2, APIForgotPasswordResponseModel.class);
                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);


            if (response == false) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finish();
                            }
                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else {
                if (responseModel != null) {
                    if (responseModel.errorLogId <= 0) {
                        if ((responseModel.mobileNumber.equals(email_mobile.getText().toString()) || responseModel.emailAddress.equals(email_mobile.getText().toString()))) {

//                            if (email_mobile.getText().toString().contains("@")) {
//                                Intent intentLocationScreen = new Intent(activity_forgot_password.this, activity_otp_verification.class).
//                                        putExtra("emailAddress", responseModel.emailAddress);
//                                startActivity(intentLocationScreen);
//                                finish();
//                            } else {
//                                startSMSOTPConfirmation();
//
//                            }



                            if (!validatePhoneNumber(responseModel.countryCode+" "+requestModel.mobileNo)) {
                                return;
                            }
                            startPhoneNumberVerification(responseModel.countryCode+" "+requestModel.mobileNo);

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
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
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
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private boolean validatePhoneNumber(String s) {
        String phoneNumber = s;
        if (TextUtils.isEmpty(phoneNumber)) {
//            PhoneNumber.setError("Invalid phone number.");
            Toast t=Toast.makeText(activity_forgot_password.this,"Invalid Phone Number",Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        return true;
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            phone_auth_fields.setVisibility(View.GONE);
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
//                            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
//                            finish();
//                            new RegistrationActivityOne.submitRegistration().execute();
                            Intent intentLocationScreen = new Intent(activity_forgot_password.this, activity_reset_password.class).
                                    putExtra("identifier", requestModel.mobileNo);
                            startActivity(intentLocationScreen);
                            finish();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    public void startSMSOTPConfirmation() {

        TwitterAuthConfig authConfig = new TwitterAuthConfig(FixLabels.TWITTER_KEY, FixLabels.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
//        Digits.clearActiveSession();
        Digits.logout();
        //        DigitsAuthButton digitsAuthButton = (DigitsAuthButton) findViewById(R.id.auth_button);
//        DigitsAuthButton digitsAuthButton = new DigitsAuthButton(DriverHeadingToPickup);


        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {

                if (phoneNumber.contains(email_mobile.getText().toString())) {
                    Intent intentLocationScreen = new Intent(activity_forgot_password.this, activity_reset_password.class).
                            putExtra("identifier", requestModel.mobileNo);
                    startActivity(intentLocationScreen);
                    finish();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setMessage("To verify your mobile number ,you must need to use same mobile number that you used for reset request!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setMessage("Failed to verify ,please try again!")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                finish();//sp 9-5-2017
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        };
//        digitsAuthButton.setCallback(authCallback);
//        Digits.clearActiveSession();
        Digits.logout();
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                .withEmailCollection(false)
                .withAuthCallBack(authCallback)
                .withPhoneNumber("+961" + responseModel.mobileNumber.trim().replace(" ", ""));
//                .withPhoneNumber(responseModel.countryCode.trim().replace(" ", "") + responseModel.mobileNumber.trim().replace(" ", ""));sp 9-5-2017
        Digits.authenticate(authConfigBuilder.build());

//        Intent intentLocationScreen = new Intent(activity_profile.this, activity_otp_verification.class);
//        startActivity(intentLocationScreen);
//        finish();
    }

}

