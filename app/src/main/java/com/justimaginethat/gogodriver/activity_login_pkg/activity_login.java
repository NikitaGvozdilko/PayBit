package com.justimaginethat.gogodriver.activity_login_pkg;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APILoginRequestModel;
import com.justimaginethat.gogodriver.APIModels.APILoginResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ExtendedView.AnimationButtonLogin;
import com.justimaginethat.gogodriver.ExtendedView.ButtonLogin;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_forgot_password_pkg.activity_forgot_password;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_registration_pkg.RegistrationActivityOne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */

public class activity_login extends Activity {


    // UI references.
    public EditText mEmailView;
    public EditText mPasswordView;
    public View mProgressView;
    public View mLoginFormView;
    ImageView background;
    TextView tvforgotPassword, tvNeedHelp, tvSignUp;
    Context context;
    private ImageView imageView;
    public RelativeLayout rel;
    public ScrollView scroller;
    public SessionDCM sessionDCM;
    public SessionDAO sessionDAO;
    private List<SessionDCM> sessionDCMs;
    public activity_login activity_login;

    public Gson gson = new Gson();
    private OrderDAO orderDAO;
    private DriverDAO driverDAO;
    private UserDAO userDAO;
    public ObjectAnimator loginScaleYObjectAnimator;
    public ObjectAnimator loginScaleXObjectAnimator;
    private ProgressBar pbCircular;
    private ProgressBar pbCircularShadow;
    private TextView txtLoginText;
    AnimationButtonLogin animation;
    boolean flag = true;
    public boolean a = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        activity_login = this;
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        sessionDCMs = new ArrayList<>();
        sessionDCM = new SessionDCM();
        scroller = (ScrollView) findViewById(R.id.scroller);
        pbCircular = (ProgressBar) findViewById(R.id.pbCircular);
        pbCircular.setVisibility(View.GONE);
        pbCircularShadow = (ProgressBar) findViewById(R.id.pbCircularShadow);
        pbCircularShadow.setVisibility(View.GONE);
        txtLoginText = (TextView) findViewById(R.id.txtLoginText);

        scroller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        rel = (RelativeLayout) findViewById(R.id.rel);
        rel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//            AskUserToTurnONGPS();
                Rect r = new Rect();
                rel.getWindowVisibleDisplayFrame(r);
                int screenHeight = rel.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d("asdasd", "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    int totalHeight = scroller.getChildAt(0).getHeight();
                    scroller.setScrollY(totalHeight);
//                scroller.fullScroll(View.FOCUS_DOWN);
                } else {
                    scroller.setScrollY(0);
//                scroller.fullScroll(View.FOCUS_UP);
                    // keyboard is closed
                }

                rel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard();
                    }
                });
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        LionUtilities helperSP = new LionUtilities();
        tvforgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvforgotPassword.setPaintFlags(tvforgotPassword.getPaintFlags());
        tvforgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLocationScreen = new Intent(activity_login.this, activity_forgot_password.class);
                startActivity(intentLocationScreen);
//                finish();
            }
        });
      /*  tvNeedHelp = (TextView) findViewById(R.id.tvNeedHelp);
        tvNeedHelp.setPaintFlags(tvNeedHelp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvNeedHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
           *//*      Intent intentLocationScreen = new Intent(activity_login.this, activity_need_help.class).putExtra("type","Query");
                startActivity(intentLocationScreen);
*//*
                Toast.makeText(DriverHeadingToPickup, "Under Construction...", Toast.LENGTH_SHORT).show();
            }
        });*/
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setPaintFlags(tvSignUp.getPaintFlags());
        tvSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startFCMServices();
                startRegistrationProcess();
            }
        });

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);


        mPasswordView = (EditText) findViewById(R.id.password);

        if (!FixLabels.Server.contains(":1044")) {
            mEmailView.setText("");
            mPasswordView.setText("");
        }

        final ButtonLogin mEmailSignInButton = (ButtonLogin) findViewById(R.id.btnLogin);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {


                if (a == false) {
                    a = true;
                    boolean valid = true;


                    if (mPasswordView.getText().toString().length() == 0) {

                        mPasswordView.setError("Password cannot be Blank");
                        mPasswordView.requestFocus();
                        valid = false;
                    } else {
                        mPasswordView.setError(null);
                    }


                    //Validation for Blank Field
                    if (mEmailView.getText().toString().length() == 0) {

                        mEmailView.setError("Please enter valid details");
                        mEmailView.requestFocus();


                        valid = false;
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailView.getText().toString()).matches() && mEmailView.getText().toString().contains("@")) {
                        //Validation for Invalid Email Address
//                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                        mEmailView.setError("Please enter valid details");
                        valid = false;
                    } else {
                        mEmailView.setError(null);
                    }


                    if (valid == true) {
                        a = true;
                        hideKeyboard();
                        animation = new AnimationButtonLogin(mEmailSignInButton, context, pbCircular, pbCircularShadow, txtLoginText, rel, (activity_login));
                        animation.setDuration(500);


                        requestModel = new APILoginRequestModel();
                        if (mEmailView.getText().toString().contains("@")) {
                            requestModel.emailAddress = mEmailView.getText().toString();
                        } else {
                            requestModel.mobileNumber = mEmailView.getText().toString();
                        }
                        requestModel.password = mPasswordView.getText().toString();
                        requestModel.gcmId = FixLabels.gcmid;
                        requestModel.udid = FixLabels.udId;
                        animation.startAnimation();
                        new checkLogin().execute();

                    } else {
                        a = false;
                    }


                }


            }
        });
        mLoginFormView = findViewById(R.id.email_login_form);
        startFCMServices();
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity_login.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity_login.getCurrentFocus().getWindowToken(), 0);
    }

    public void onSuccessResultAnimation() {
        if (responseModel.user.isCustomer == true) {
            Intent intentLocationScreen = new Intent(activity_login.this, fragment_activity_landing_user.class);
            intentLocationScreen.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentLocationScreen);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (responseModel.user.isDriver == true) {
            Intent intentLocationScreen = new Intent(activity_login.this, fragment_activity_landing_driver.class);
            intentLocationScreen.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentLocationScreen);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    public void startFCMServices() {

        FixLabels.gcmid = FirebaseInstanceId.getInstance().getToken();
        Log.i("PayBit", "Current token: " + FixLabels.gcmid);
    }

    public void startRegistrationProcess() {
        startFCMServices();
        Intent intentLocationScreen = new Intent(activity_login.this, RegistrationActivityOne.class);
        startActivity(intentLocationScreen);
        finish();

    }

    APILoginRequestModel requestModel;
    BaseAPIResponseModel<APILoginResponseModel> responseDataModel = new BaseAPIResponseModel<APILoginResponseModel>();
    APILoginResponseModel responseModel = new APILoginResponseModel();

    public class checkLogin extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(activity_login.this, "", "", true, false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestModel));
            String str = helperSP.requestHTTPResponse(APILinks.APILogin, map, "POST", false);
            if (str.equals("")) {
                return false;
            } else {
                responseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseDataModel.Data);
                responseModel = gson.fromJson(str2, APILoginResponseModel.class);
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);

            if (response == false) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                animation.abortAnimation();
                                a = false;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else {
                if (responseModel != null) {

                    if (responseModel.errorLogId == 0) {
                        if (responseModel.isAuthorised == true) {

                            sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            sessionDAO.deleteAll();
                            orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            orderDAO.deleteAll();

                            driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            driverDAO.deleteAll();

                            userDAO = new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            userDAO.deleteAll();
//                            FixLabels.sessionDatabase.user = null;
                            FixLabels.sessionDatabase.pickupPointId = 0;
                            FixLabels.sessionDatabase.pickupAddress = "";
                            FixLabels.sessionDatabase.pickupAddress = "";
                            FixLabels.sessionDatabase.pickupLatitude = "";
                            FixLabels.sessionDatabase.pickupLongitude = "";
                            FixLabels.sessionDatabase.pickupTitle = "";
                            sessionDCM = new SessionDCM();
                            sessionDAO.create(responseModel.user);
                            if (responseModel.user.isCustomer == true) {
                                gson = new Gson();
                                sessionDCM = gson.fromJson(gson.toJson(responseModel.user), SessionDCM.class);
                                sessionDAO.create(responseModel.user);
                            } else if (responseModel.user.isDriver == true) {
                                sessionDCM = gson.fromJson(gson.toJson(responseModel.user), SessionDCM.class);
                                sessionDAO.create(responseModel.user);
                            }
                            animation.finishAnimation();
                            a = false;
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle("Login failed")
                                    .setCancelable(false)
                                    .setMessage("Either your email/mobile number and password do not match or your account is not active.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            animation.abortAnimation();
                                            a = false;
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }

                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = responseModel.errorURL;
                                        Intent i = new Intent(activity_login.this, activity_error_webview.class);
                                        i.putExtra("url", url);
                                        startActivity(i);
                                        animation.abortAnimation();
                                        a = false;
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                        Log.i("PayBit", "APILogin Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    animation.abortAnimation();
                                    a = false;
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                }
            }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void AskUserToTurnONGPS() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (statusOfGPS == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new android.app.AlertDialog.Builder(this, R.style.AlertDialogCustom)
                        .setCancelable(false)
                        .setTitle("GPS Offline")
                        .setMessage("To track location we need gps enabled,Please turn on gps.")
                        .setPositiveButton("Settings",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 105);
                                    }
                                })
                        .setNegativeButton("Exit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        finish();
                                    }
                                }).show();
            }
        }
    }
}

