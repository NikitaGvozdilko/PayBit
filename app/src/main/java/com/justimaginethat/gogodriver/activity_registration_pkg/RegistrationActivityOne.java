package com.justimaginethat.gogodriver.activity_registration_pkg;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.justimaginethat.gogodriver.APIModels.APIRegistrationRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIRegistrationResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ExtendedView.EditTextSegoePrintFont;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.AddressMapPickerActivity;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;
import com.justimaginethat.gogodriver.activity_registration_pkg.AsynchronousTasks.UserExistsCheckAsync;
import com.justimaginethat.gogodriver.activity_registration_pkg.PickupLocation.PickUpLocation;
import com.justimaginethat.gogodriver.activity_registration_pkg.models.Countries;
import com.justimaginethat.gogodriver.imagecompress.conversionImage;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class RegistrationActivityOne extends AppCompatActivity implements View.OnClickListener {
    public EditText FirstName;
    public EditText Email;
    public TextView CountryCode;
    public EditText PhoneNumber;
    public EditText PasswordPin;
    public EditText conPasswordPin;
    public EditText address1;

    public RadioButton switch_Client, switch_Driver;

    public LinearLayout action_image;
    public LinearLayout action_done;
    public EditText ConfirmPin;
    public ImageButton btnSumit;
    public ImageView imgProfilePicture;
    public ImageView imgBack;
    public TextView img_profileCamera;
    public TextView img_profileGallery;
    public Context context;
    public Countries finalCountries;
    public TextView TlblSign;


    public SessionDCM sessionDCM;
    public UserDAO userDAO;
    public String messageAddress;
    public String latitude1 ="";
    public   String longitude1 ="";

    public List<SessionDCM> sessionDCMs = new ArrayList<SessionDCM>();



    public RegistrationActivityOne registrationActivity;

    APIRegistrationRequestModel requestModel;
    BaseAPIResponseModel<APIRegistrationResponseModel> responseDataModel = new BaseAPIResponseModel<APIRegistrationResponseModel>();
    APIRegistrationResponseModel responseModel = new APIRegistrationResponseModel();
    private String TAG = "RegistrationActivityLog";
    AuthCallback authCallback;
    String statusSwitch;







    PickUpLocation appLocationService;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    public String attachment="",type="";

    private int PICK_IMAGE_REQUEST = 1;
    private int mRequestCodeAddress = 100;

    LinearLayout   ledtEmail,ledtAddress,ledtBuilding,ledtFloor;


    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private int RESULT_CAMERA_LOAD_IMG = 10;
    private int RESULT_GALLERY_LOAD_IMG = 11;
    private int RESULT_IMG_LOAD_UPLOAD = 12;
    private ExifInterface exif;
    Bitmap photo;
    public String ba1;
    String picturePath="img";
    Uri selectedImage;
    public String profilePicture ="";
    public View mCustomView;
    public TextView mTitleTextView;
    ByteArrayOutputStream bytearrayoutputstream;

    public boolean valid;
    private Drawable errorIcon;

    public SessionDAO sessionDAO;


    RelativeLayout phone_auth_fields;
    EditTextSegoePrintFont mVerificationField;
    LinearLayout mVerifyButton;
    ImageView mResendButton;

    EditText edtFName,edtEmail,edtPassword,edtConPassword,edtPhoneNumber,txtAddress,edtBuildingNumber,edtFloorNumber;

    @Override
    protected void onResume() {
        super.onResume();

        int  i = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        int j = 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentLocationScreen = new Intent(RegistrationActivityOne.this, activity_login.class);
        intentLocationScreen.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentLocationScreen);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        context = this;
        errorIcon = getResources().getDrawable(R.drawable.icon_cancel_red);
        bytearrayoutputstream = new ByteArrayOutputStream();
        appLocationService = new PickUpLocation(RegistrationActivityOne.this);
        FirstName = (EditText) findViewById(R.id.edtFName);
        Email = (EditText) findViewById(R.id.edtEmail);
        PasswordPin = (EditText) findViewById(R.id.edtPassword);
        conPasswordPin=(EditText) findViewById(R.id.edtConPassword);
        CountryCode = (TextView) findViewById(R.id.edtCountryCode);
        PhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        address1 = (EditText) findViewById(R.id.txtAddress);


        edtFName=(EditText) findViewById(R.id.edtFName);
        edtEmail=(EditText) findViewById(R.id.edtEmail);
        edtPassword=(EditText) findViewById(R.id.edtPassword);
        edtConPassword=(EditText) findViewById(R.id.edtConPassword);
        edtPhoneNumber=(EditText) findViewById(R.id.edtPhoneNumber);
        txtAddress=(EditText) findViewById(R.id.txtAddress);
        edtBuildingNumber=(EditText) findViewById(R.id.edtBuildingNumber);
        edtFloorNumber=(EditText) findViewById(R.id.edtFloorNumber);


        address1.setMovementMethod(new ScrollingMovementMethod());
        switch_Client = (RadioButton) findViewById(R.id.switch_Client);
        switch_Driver = (RadioButton) findViewById(R.id.switch_Driver);
//        TlblSign = (TextView) findViewById(R.id.lblSign);
        img_profileCamera = (TextView) findViewById(R.id.img_profileCamera);
        img_profileGallery = (TextView) findViewById(R.id.img_profileGallery);
        imgProfilePicture = (ImageView) findViewById(R.id.imgProfilePicture);
        action_done=(LinearLayout)findViewById(R.id.action_done);
        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.create_account);
        imgBack=(ImageView)mCustomView.findViewById(R.id.imgBack);
        action_image=(LinearLayout)findViewById(R.id.action_image);
        ledtAddress=(LinearLayout)findViewById(R.id.ledtAddress);

        ledtEmail=(LinearLayout)findViewById(R.id.ledtEmail);
        ledtBuilding=(LinearLayout)findViewById(R.id.ledtBuilding);
        ledtFloor=(LinearLayout)findViewById(R.id.ledtFloor);

        phone_auth_fields=(RelativeLayout)findViewById(R.id.phone_auth_fields);

        phone_auth_fields.setVisibility(View.GONE);
        mVerificationField = (EditTextSegoePrintFont) findViewById(R.id.field_verification_code);


        mVerifyButton = (LinearLayout) findViewById(R.id.button_verify_phone);
        mResendButton = (ImageView) findViewById(R.id.button_resend);

        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);

        registrationActivity = this;

        requestModel = new APIRegistrationRequestModel();
        requestModel.UserDCM = new UserDCM();


        action_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSubmitClick();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack=new Intent(RegistrationActivityOne.this,activity_login.class);
                startActivity(intentBack);
                finish();


            }
        });
        img_profileCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (LionUtilities.hasConnection(RegistrationActivityOne.this)) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, RESULT_IMG_LOAD_UPLOAD);
                } else {
                    LionUtilities.makeToast(RegistrationActivityOne.this,
                            "No INTERNET Connection ");
                }

            }
        });
        img_profileGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_GALLERY_LOAD_IMG);
            }
        });
        action_image.setVisibility(View.VISIBLE);
        statusSwitch = switch_Client.getText().toString();


        switch_Client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_image.setVisibility(View.VISIBLE);

                switch_Driver.setTextColor(getResources().getColor(R.color.color_loading_bg_transparent));
                switch_Client.setTextColor(getResources().getColor(R.color.colorPrimary));
                switch_Driver.setChecked(false);
                if (switch_Client.isChecked()) {
                    statusSwitch = switch_Client.getText().toString();
                } else {
                    switch_Driver.setChecked(true);
                }
//                ledtFloor.setVisibility(View.VISIBLE);
//                ledtBuilding.setVisibility(View.VISIBLE);
//                ledtAddress.setVisibility(View.VISIBLE);
//                ledtEmail.setVisibility(View.VISIBLE);



                edtFName.setHint(getResources().getString(R.string.namec));
                edtEmail.setHint(getResources().getString(R.string.emailc));

                edtPassword.setHint(getResources().getString(R.string.passc));
                edtConPassword.setHint(getResources().getString(R.string.cpassc));
                edtPhoneNumber.setHint(getResources().getString(R.string.phonec));
                txtAddress.setHint(getResources().getString(R.string.addc));
                edtBuildingNumber.setHint(getResources().getString(R.string.bnumc));
                edtFloorNumber.setHint(getResources().getString(R.string.fnumc));


            }
        });



        switch_Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_Client.setTextColor(getResources().getColor(R.color.color_loading_bg_transparent)); //white
                switch_Driver.setTextColor(getResources().getColor(R.color.colorPrimary)); //white
                action_image.setVisibility(View.VISIBLE);
                switch_Client.setChecked(false);
                if (switch_Driver.isChecked()) {
                    statusSwitch = switch_Driver.getText().toString();
                } else {
                    switch_Client.setChecked(true);
                }
//                ledtFloor.setVisibility(View.GONE);
//                ledtBuilding.setVisibility(View.GONE);
//                ledtAddress.setVisibility(View.GONE);
//                ledtEmail.setVisibility(View.GONE);




                        edtFName.setHint(getResources().getString(R.string.named));
                edtEmail.setHint(getResources().getString(R.string.emaild));

                edtPassword.setHint(getResources().getString(R.string.passd));
                edtConPassword.setHint(getResources().getString(R.string.cpassd));
                edtPhoneNumber.setHint(getResources().getString(R.string.phoned));
                txtAddress.setHint(getResources().getString(R.string.addd));
                edtBuildingNumber.setHint(getResources().getString(R.string.bnumd));
                edtFloorNumber.setHint(getResources().getString(R.string.fnumd));



            }
        });


        ledtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationActivityOne.this,AddressMapPickerActivity.class);
                startActivityForResult(intent, mRequestCodeAddress);
            }
        });
        if (!FixLabels.Server.contains(":1044")) {
            FirstName.setText("");
            Email.setText("");
            PhoneNumber.setText("");
            PasswordPin.setText("");
            conPasswordPin.setText("");
        }
        this.getSupportActionBar().setCustomView(mCustomView);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        Gson gson = new Gson();



        FirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                    if (FirstName.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "First Name cannot be Blank", Toast.LENGTH_LONG).show();
                        FirstName.setError("Please Enter Your Name");
                        valid = false;
                    }
                else
                    {
                        FirstName.setError(null);
                        requestModel.UserDCM.firstName = FirstName.getText().toString();
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



//
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here

                if(hasFocus == false && Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches())
                {
                    new UserExistsCheckAsync(registrationActivity,Email.getText().toString()).execute();
                }

            }
        });

        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (Email.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "First Name cannot be Blank", Toast.LENGTH_LONG).show();
                    Email.setError("Please Enter Your Email Address");
                    valid = false;
                }


               else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
//                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    Email.setError("Please enter valid email address.");
                    valid = false;
                }
                else
                {
                    Email.setError(null);
                    requestModel.UserDCM.emailAddress =Email.getText().toString();
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

        PhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        // Your validation code goes here
                        if(hasFocus == false && PhoneNumber.getText().toString().length() <= 10 || PhoneNumber.getText().toString().length() >= 7)
                        {
                           new UserExistsCheckAsync(registrationActivity,PhoneNumber.getText().toString()).execute();
                        }


                    }
                });

        PhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (!Patterns.PHONE.matcher(PhoneNumber.getText().toString()).matches()) {
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                    PhoneNumber.setError("Please Enter Valid Mobile Number");
                    valid = false;
                }
                else if (PhoneNumber.getText().toString().length() > 10 || PhoneNumber.getText().toString().length() < 7) {
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                    PhoneNumber.setError("Mobile number can be minimum 7 and maximum 10 digit long.");
                    valid = false;
                }
                else
                {
                    PhoneNumber.setError(null);
                    requestModel.UserDCM.mobileNumber = PhoneNumber.getText().toString();
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


        PasswordPin.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here

                if(hasFocus == false)
                {
                    if (PasswordPin.getText().toString().length() == 0 ) {
                        PasswordPin.setError("Please Enter Your Desired  Password.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        valid = false;
                    }
                    else if ( PasswordPin.getText().toString().length() > 0 && PasswordPin.getText().toString().length() < 6) {
                        PasswordPin.setError("Password requires minimum 6 character length.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        valid = false;
                    }
                    else
                    {
                        PasswordPin.setError(null);
                        requestModel.UserDCM.password = PasswordPin.getText().toString();
                        valid = true;
                    }
                }


            }
        });

        PasswordPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here
                if (PasswordPin.getText().toString().length() == 0 ) {
                    PasswordPin.setError("Please Enter Your Desired  Password.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    valid = false;
                }
                else if ( PasswordPin.getText().toString().length() > 0 && PasswordPin.getText().toString().length() < 6) {
                    PasswordPin.setError("Password requires minimum 6 character length.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    valid = false;
                }
                else
                {
                    PasswordPin.setError(null);
                    requestModel.UserDCM.password = PasswordPin.getText().toString();
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



        conPasswordPin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if(hasFocus == false)
                {
                    if (conPasswordPin.getText().toString().length() == 0 ) {
                        conPasswordPin.setError("Please re-enter password.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        valid = false;
                    }
                    else if ( conPasswordPin.getText().toString().length() >0 && conPasswordPin.getText().toString().length() < 6) {
                        conPasswordPin.setError("Confirm password requires minimum 6 character length.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        valid = false;
                    }

                    else if (!conPasswordPin.getText().toString().equals(PasswordPin.getText().toString())) {
                        conPasswordPin.setError("Confirm password do not match!,Please try again.");
                        //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                        valid = false;
                    }
                    else
                    {
                        conPasswordPin.setError(null);
                        requestModel.UserDCM.password = PasswordPin.getText().toString();
                        valid = true;

                    }
                }

            }
        });

        conPasswordPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (conPasswordPin.getText().toString().length() == 0 ) {
                    conPasswordPin.setError("Please re-enter password.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    valid = false;
                }
                else if ( conPasswordPin.getText().toString().length() >0 && conPasswordPin.getText().toString().length() < 6) {
                    conPasswordPin.setError("Confirm password requires minimum 6 character length.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    valid = false;
                }

               else if (!conPasswordPin.getText().toString().equals(PasswordPin.getText().toString())) {
                    conPasswordPin.setError("Confirm password do not match!,Please try again.");
                    //Validation for Website Address
//                    Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
                    valid = false;
                }
                else
                {
                    conPasswordPin.setError(null);
                    requestModel.UserDCM.password = PasswordPin.getText().toString();
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

        address1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Your validation code goes here
                if(hasFocus == true) {

                    if (address1.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "Address  cannot be Blank", Toast.LENGTH_LONG).show();
                        ledtAddress.callOnClick();
                        address1.setError("Please Enter Your Address Manually");
                        valid = false;
                    }
                    else
                    {
                        address1.setError(null);
                        requestModel.UserDCM.address1 = address1.getText().toString();
                        valid = true;
                    }
                }
            }
        });

        address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Your validation code goes here

                if (address1.getText().toString().length() == 0) {

//                    Toast.makeText(getApplicationContext(), "Address  cannot be Blank", Toast.LENGTH_LONG).show();
                    address1.setError("Please Enter Your Address Manually Or tap/click on address icon for address picker.");
                    valid = false;
                }
                else
                {
                    address1.setError(null);
                    requestModel.UserDCM.address1 = address1.getText().toString();
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
                    requestModel.UserDCM.buildingName1 = edtBuildingNumber.getText().toString();
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

//        if(statusSwitch.equals("Driver")||statusSwitch.equals("Customer")) {
//            if (profilePicture == null || profilePicture.equals("") || profilePicture.equals(null)) {
////                Toast.makeText(getApplicationContext(), "Take photo please.", Toast.LENGTH_LONG).show();
//                validEx = false;
//            }
//
//        }else {
            requestModel.UserDCM.profilePicture = profilePicture;
//            validEx = true;
//        }
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
                    requestModel.UserDCM.floorNumber1 = edtFloorNumber.getText().toString();
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
                    PhoneNumber.setError("Invalid phone number.");
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
                            new submitRegistration().execute();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
        RegistrationActivityOne.this);
    alertDialog.setCancelable(false);
        alertDialog.setTitle(FixLabels.alertDefaultTitle);
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
        new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(
        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        RegistrationActivityOne.this.startActivity(intent);
        }
        });
        alertDialog.setNegativeButton("Cancel",
        new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        }
        });
        alertDialog.show();
        }



    void doSubmitClick() {
        valid = true;
        //Validation for Blank Field
        if (FirstName.getText().toString().length() == 0) {
//            Toast.makeText(getApplicationContext(), "First Name cannot be Blank", Toast.LENGTH_LONG).show();
            FirstName.setError("Please Enter Your Name");
            valid = false;
        }
        if (!statusSwitch.equals("Driver")){
            if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                //Validation for Invalid Email Address
//            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                Email.setError("Please Enter Your Email Address");
                valid = false;
//            return;
            }
            if (address1.getText().toString().length() == 0) {
//            Toast.makeText(getApplicationContext(), "Address  cannot be Blank", Toast.LENGTH_LONG).show();
                address1.setError("Please Enter Your Address Manually");
                valid = false;
            }
            if (edtBuildingNumber.getText().toString().length() == 0) {
//            Toast.makeText(getApplicationContext(), "Building Number  cannot be Blank", Toast.LENGTH_LONG).show();
                edtBuildingNumber.setError("Please Enter Your Building Name");
                valid = false;
            }
            if (edtFloorNumber.getText().toString().length() == 0) {
//            Toast.makeText(getApplicationContext(), "Floor Number  cannot be Blank", Toast.LENGTH_LONG).show();
                edtFloorNumber.setError("Please Enter Your Floor Number");
                valid = false;
            }
        }

        if (!Patterns.PHONE.matcher(PhoneNumber.getText().toString()).matches()) {
            //Validation for Website Address
//            Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
            PhoneNumber.setError("Please Enter Your Mobile Number");
            valid = false;
        }
        if (PasswordPin.getText().toString().length() == 0 || PasswordPin.getText().toString().length() < 6) {
            PasswordPin.setError("Please Enter Your Desired  Password");
            //Validation for Website Address
//            Toast.makeText(getApplicationContext(), "Password Pin incorrect", Toast.LENGTH_LONG).show();
            valid = false;
        }




        if (conPasswordPin.getText().toString().length() == 0 || conPasswordPin.getText().toString().length() < 6 || !conPasswordPin.getText().toString().equals(PasswordPin.getText().toString())) {

//            Toast.makeText(getApplicationContext(), "Floor Number  cannot be Blank", Toast.LENGTH_LONG).show();
            conPasswordPin.setError("Please confirm password by re-entering");
            valid = false;
        }

//
//        if(statusSwitch.equals("Driver")||statusSwitch.equals("Customer")) {
//            if (profilePicture == null || profilePicture.equals("") || profilePicture.equals(null)) {
////                Toast.makeText(getApplicationContext(), "Take photo please.", Toast.LENGTH_LONG).show();
//                validEx = false;
//            }
//
//        }else {
        requestModel.UserDCM.profilePicture = profilePicture;
//            validEx = true;
//        }


        if (valid == true) {
//            requestModel = new APIRegistrationRequestModel();
//            requestModel.UserDCM = new UserDCM();
//            requestModel.UserDCM.firstName = FirstName.getText().toString();
//            requestModel.UserDCM.emailAddress = Email.getText().toString();
            requestModel.UserDCM.countryCode = CountryCode.getText().toString();
//            requestModel.UserDCM.password = PasswordPin.getText().toString();
            requestModel.UserDCM.mobileNumber = requestModel.UserDCM.countryCode + " " + PhoneNumber.getText().toString();
//            requestModel.UserDCM.address1 = address1.getText().toString();
            requestModel.UserDCM.defaultAddress = address1.getText().toString();
//
//            requestModel.UserDCM.buildingName1 = edtBuildingNumber.getText().toString();
            requestModel.UserDCM.defaultBuildingName = edtBuildingNumber.getText().toString();
//
//            requestModel.UserDCM.floorNumber1 = edtFloorNumber.getText().toString();
            requestModel.UserDCM.defaultFloorNumber = edtFloorNumber.getText().toString();


            requestModel.UserDCM.latitude1 = latitude1;
            requestModel.UserDCM.defaultLatitude = latitude1;

            requestModel.UserDCM.longitude1 = longitude1;
            requestModel.UserDCM.defaultLongitude = longitude1;

            requestModel.UserDCM.gcmId = FixLabels.gcmid;
            requestModel.UserDCM.udId = FixLabels.udId;
            if (statusSwitch.equals("Driver")) {
                requestModel.UserDCM.isDriver = true;
                requestModel.UserDCM.isCustomer = false;
            } else {
                requestModel.UserDCM.isDriver = false;
                requestModel.UserDCM.isCustomer = true;
            }
            requestModel.UserDCM.profilePicture = profilePicture;
            startOTPProcess();
//            new submitRegistration().execute();
//            new submitRegistration().execute();
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                requestModel.UserDCM.mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private boolean validatePhoneNumber() {
        String phoneNumber = requestModel.UserDCM.mobileNumber;
        if (TextUtils.isEmpty(phoneNumber)) {
            PhoneNumber.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    private void startOTPProcess() {


        if (!validatePhoneNumber()) {
            return;
        }
        startPhoneNumberVerification(requestModel.UserDCM.mobileNumber);

//        TwitterAuthConfig authConfig = new TwitterAuthConfig(FixLabels.TWITTER_KEY, FixLabels.TWITTER_SECRET);
//        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
//        Digits.disableSandbox();
//        authCallback = new AuthCallback() {
//            @Override
//            public void success(DigitsSession session, String phoneNumber) {
//                if(phoneNumber.contains(PhoneNumber.getText().toString()))
//                {
//                    new submitRegistration().execute();
//                }
//                else
//                {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
//                            .setTitle(FixLabels.alertDefaultTitle)
//                            .setCancelable(false)
//                            .setMessage("To verify your mobile number ,you must need to use same mobile number that you used at registration form!")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                }
//                            })
//                              .setIcon(android.R.drawable.ic_dialog_alert);
// AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
//                }
//
//
//            }
//
//            @Override
//            public void failure(DigitsException exception) {
//                // Do something on failure
//                Toast.makeText(context, "failed to verify your mobile number.", Toast.LENGTH_SHORT).show();
//            }
//        };
//        Digits.logout();
//        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
//                .withAuthCallBack(authCallback)
//                .withEmailCollection(false)
//                .withPhoneNumber(requestModel.UserDCM.mobileNumber.trim().replace(" ", ""));
//
////                .withPhoneNumber(requestModel.UserDCM.mobileNumber.trim().replace(" ", ""));sp 9-5-2017
//        Digits.authenticate(authConfigBuilder.build());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CAMERA_LOAD_IMG) {
                /**
                 * Helper method to access the camera returns null if it cannot get the
                 * camera or does not exist
                 *
                 * @return
                 */







              /*  String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                picturePath = cursor.getString(column_index_data);
                Bitmap bitmapImage = BitmapFactory.decodeFile(picturePath);
//                    ivShow.setImageBitmap(bitmapImage);
                File imgFile = new File(picturePath);
                if (imgFile.exists()) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imgProfilePicture.setImageBitmap(photo);
                }*/
            }


            if (requestCode == RESULT_GALLERY_LOAD_IMG && null != data) {
                profilePicture = "";
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                profilePicture = cursor.getString(columnIndex);
                cursor.close();
                profilePicture = new conversionImage().compressImage(profilePicture, this, 864.0f, 1152.0f, 100);
                Bitmap bitmapImage = BitmapFactory.decodeFile(profilePicture);
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                imgProfilePicture.setImageBitmap(scaled);
            }
            if (requestCode == RESULT_IMG_LOAD_UPLOAD && resultCode == Activity.RESULT_OK) {
                profilePicture = "";

                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();

                profilePicture = cursor.getString(column_index_data);
//                String photoFile = profilePicture.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                profilePicture = new conversionImage().compressImage(profilePicture, this, 864.0f, 1152.0f, 100);
//                Bitmap bitmapImage = BitmapFactory.decodeFile(profilePicture);
//                    ivShow.setImageBitmap(bitmapImage);
                File imgFile = new File(profilePicture);
                if (imgFile.exists()) {
                    Bitmap bmp = BitmapFactory.decodeFile(profilePicture);
                    imgProfilePicture.setImageBitmap(bmp);
                }
//                Toast.makeText(context, profilePicture + "", Toast.LENGTH_SHORT).show();
            }
            if(requestCode == mRequestCodeAddress && resultCode == RESULT_OK){
                messageAddress = data.getStringExtra("messageAddress");
                latitude1 = data.getStringExtra("latitude1");
                longitude1 = data.getStringExtra("longitude1");
                address1.setText(messageAddress);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);

                break;
            case R.id.button_resend:
                resendVerificationCode(requestModel.UserDCM.mobileNumber, mResendToken);
                break;
        }

    }

    public class submitRegistration extends AsyncTask<Void, Void, Boolean> {
        CustomProgressBar progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(RegistrationActivity.this, "", "", true, false);

            progressDialog = CustomProgressBar.show(RegistrationActivityOne.this,"", true,false,null);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSp = new LionUtilities();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestModel));
            HashMap<String, String> mapFile = new HashMap<String, String>();
            if(new File(profilePicture).exists()) {
                mapFile.put("File1", profilePicture);
            }
            String str = null;
            try {
                str = helperSp.requestHTTPResponseWithFile(APILinks.APIRegistration, map,mapFile,"POST", true);
                if (str.equals("")) {
                    return false;
                } else {
                    responseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                    String str2 = gson.toJson(responseDataModel.Data);
                    responseModel = gson.fromJson(str2, APIRegistrationResponseModel.class);
                    Log.i(TAG, String.valueOf(requestModel));
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);

            sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());

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
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            } else {
                if (responseModel != null) {
                    if (responseModel.errorLogId <= 0) {
                        if (responseModel.user != null) {

                            if (responseModel.user.idUser > 0) {
                                sessionDCM = new SessionDCM();
                                sessionDCM = responseModel.user;

//                            requestModel.UserDCM.profilePicture = profilePicture;


                                sessionDAO.create(sessionDCM);


                                sessionDCMs = new ArrayList<>();
                                sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                try {
                                    sessionDCMs = sessionDAO.getAll();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }


//                            FixLabels.sessionDatabase.session.idUser=responseModel.idUser;
//                            FixLabels.sessionDatabase.session.isCustomer = requestModel.UserDCM.isCustomer;
//                            FixLabels.sessionDatabase.session.isDriver = requestModel.UserDCM.isDriver;
//                            FixLabels.sessionDatabase.session.isAdmin = requestModel.UserDCM.isAdmin;
//                            FixLabels.sessionDatabase.session.workStatus = requestModel.UserDCM.workStatus;
//                            FixLabels.sessionDatabase.session.recordBySystem = requestModel.UserDCM.recordBySystem;
//                            FixLabels.sessionDatabase.session.isActive = requestModel.UserDCM.isActive;
//                            FixLabels.sessionDatabase.session.isDeleted = requestModel.UserDCM.isDeleted;
//                            FixLabels.sessionDatabase.session.attachment = requestModel.UserDCM.attachment;
//                            FixLabels.sessionDatabase.session.type = requestModel.UserDCM.type;
//                            FixLabels.sessionDatabase.session.address1 = requestModel.UserDCM.address1;
//                            FixLabels.sessionDatabase.session.countryCode = requestModel.UserDCM.countryCode;
//                            FixLabels.sessionDatabase.session.emailAddress = requestModel.UserDCM.emailAddress;
//                            FixLabels.sessionDatabase.session.firstName = requestModel.UserDCM.firstName;
//                            FixLabels.sessionDatabase.session.gcmId = requestModel.UserDCM.gcmId;
//                            FixLabels.sessionDatabase.session.mobileNumber = requestModel.UserDCM.mobileNumber;
//                            FixLabels.sessionDatabase.session.password = requestModel.UserDCM.password;
//                            FixLabels.sessionDatabase.session.profilePicture = requestModel.UserDCM.profilePicture;
//                            FixLabels.sessionDatabase.session.udId = requestModel.UserDCM.udId;
//                            FixLabels.sessionDatabase.session.userName = requestModel.UserDCM.userName;
//                            FixLabels.sessionDatabase.session.lastName = requestModel.UserDCM.lastName;
//                            FixLabels.sessionDatabase.session.passwordEncryptionKey = requestModel.UserDCM.passwordEncryptionKey;
//                            FixLabels.sessionDatabase.session.lastPasswordResetDate = requestModel.UserDCM.lastPasswordResetDate;
//                            FixLabels.sessionDatabase.session.otp = requestModel.UserDCM.otp;
//                            FixLabels.sessionDatabase.session.apnID = requestModel.UserDCM.apnID;
//                            FixLabels.sessionDatabase.session.address2 = requestModel.UserDCM.address2;
//                            FixLabels.sessionDatabase.session.longitude1 = requestModel.UserDCM.longitude1;
//                            FixLabels.sessionDatabase.session.latitude1 = requestModel.UserDCM.latitude1;
//                            FixLabels.sessionDatabase.session.longitude2 = requestModel.UserDCM.longitude2;
//                            FixLabels.sessionDatabase.session.latitude2 = requestModel.UserDCM.latitude2;
//                            FixLabels.sessionDatabase.session.streetName1 =requestModel.UserDCM.streetName1;
//                            FixLabels.sessionDatabase.session.streetName2 = requestModel.UserDCM.streetName2;
//                            FixLabels.sessionDatabase.session.sessionID = requestModel.UserDCM.sessionID;
//                            FixLabels.sessionDatabase.session.sessionValue = requestModel.UserDCM.sessionValue;
//                            FixLabels.sessionDatabase.session.sessionExpireyDateTime = requestModel.UserDCM.sessionExpireyDateTime;
//                            FixLabels.sessionDatabase.session.sessionCreateTime = requestModel.UserDCM.sessionCreateTime;
//                            FixLabels.sessionDatabase.session.blockExpiry = requestModel.UserDCM.blockExpiry;
//                            FixLabels.sessionDatabase.session.userAccountStatus = requestModel.UserDCM.userAccountStatus;
//                            FixLabels.sessionDatabase.session.lastChangeDate = requestModel.UserDCM.lastChangeDate;
//                            FixLabels.sessionDatabase.session.entryByUserName = requestModel.UserDCM.entryByUserName;
//                            FixLabels.sessionDatabase.session.entryDate = requestModel.UserDCM.entryDate;
//                            FixLabels.sessionDatabase.session.changeByUserName =requestModel.UserDCM.changeByUserName;
//                            FixLabels.sessionDatabase.session.insertRoutePoint = requestModel.UserDCM.insertRoutePoint;
//                            FixLabels.sessionDatabase.session.updateRoutePoint = requestModel.UserDCM.updateRoutePoint;
//                            FixLabels.sessionDatabase.session.syncGUID = requestModel.UserDCM.syncGUID;
//                            FixLabels.sessionDatabase.session.buildingName1 = requestModel.UserDCM.buildingName1;
//                            FixLabels.sessionDatabase.session.floorNumber1 = requestModel.UserDCM.floorNumber1;
//                            FixLabels.sessionDatabase.session.buildingName2 = requestModel.UserDCM.buildingName2;
//                            FixLabels.sessionDatabase.session.floorNumber2 = requestModel.UserDCM.floorNumber2;
//                            FixLabels.sessionDatabase.session.defaultAddress = requestModel.UserDCM.address1;
//                            FixLabels.sessionDatabase.session.defaultBuildingName = requestModel.UserDCM.buildingName1;
//                            FixLabels.sessionDatabase.session.defaultFloorNumber = requestModel.UserDCM.floorNumber1;
//                            FixLabels.sessionDatabase.session.defaultLongitude = requestModel.UserDCM.longitude1;
//                            FixLabels.sessionDatabase.session.defaultLatitude = requestModel.UserDCM.latitude1;
//                            FixLabels.sessionDatabase.session.heading = requestModel.UserDCM.heading;
//                            FixLabels.sessionDatabase.session.profilePicture=requestModel.UserDCM.profilePicture;
//                            FixLabels.sessionDatabase.session.defaultLongitude=requestModel.UserDCM.longitude1;
//                            FixLabels.sessionDatabase.session.profilePicture = profilePicture;
//                            Toast.makeText(context, "Registration completed.You can now login.", Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(RegistrationActivity.this,activity_login.class);
//                            startActivity(intent);
//                            finish();


                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                        .setTitle(FixLabels.alertDefaultTitle)
                                        .setCancelable(false)
                                        .setMessage("Registration Done")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete


                                                if (requestModel.UserDCM.isCustomer == true) {
                                                    Intent intentLocationScreen = new Intent(RegistrationActivityOne.this, fragment_activity_landing_user.class);
                                                    intentLocationScreen.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    startActivity(intentLocationScreen);
                                                    finish();
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                } else if (requestModel.UserDCM.isDriver == true) {
                                                    Intent intentLocationScreen = new Intent(RegistrationActivityOne.this, fragment_activity_landing_driver.class);
                                                    intentLocationScreen.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    startActivity(intentLocationScreen);
                                                    finish();
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                }


                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert);
                                AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();


                            } else if (responseModel.idUser < 0) {
                                AlertDialog.Builder alertDialogBuilder =   new AlertDialog.Builder(context)
                                        .setTitle("Registration")
                                        .setCancelable(false)
                                        .setMessage("Based on your profile,you are already registered,If you have forgotten your password then ple. select forgot password on login screen. ")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
//                                            finish();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert);  AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                            } else {
                                AlertDialog.Builder alertDialogBuilder =   new AlertDialog.Builder(context)
                                        .setTitle("Registration")
                                        .setCancelable(false)
                                        .setMessage("Registration Failed! try again.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert);  AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                            }
                        }
                        else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                    .setTitle("Registration")
                                    .setCancelable(false)
                                    .setMessage("Registration Failed! try again.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                      .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        }
                    } else {
                        Log.i(TAG, "Registration Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                    }
                } else {
                    Log.i(TAG, "Registration Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                }
            }
        }



    }
private class GeocoderHandler extends Handler {
    @Override
    public void handleMessage(Message message) {
        String locationAddress;
        switch (message.what) {
            case 1:
                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;
            default:
                locationAddress = null;
        }
        address1.setText(locationAddress);
    }
}
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "GoGoDriver");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("GoGoDriver", "Oops! Failed create "
                        + "GoGoDriver" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}







