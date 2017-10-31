package com.justimaginethat.gogodriver.activity_settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.SignOutAsync;
import com.justimaginethat.gogodriver.activity_settings.AsynchronousTask.UpdateImageProfileAsync;
import com.justimaginethat.gogodriver.activity_settings.updateModule.updateAddressProfile;
import com.justimaginethat.gogodriver.activity_settings.updateModule.UpdateEmailProfile;
import com.justimaginethat.gogodriver.activity_settings.updateModule.UpdateMobileNumberProfile;
import com.justimaginethat.gogodriver.activity_settings.updateModule.updateNameProfile;
import com.justimaginethat.gogodriver.activity_settings.updateModule.updateSecondaryAddressProfile;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_forgot_password_pkg.activity_forgot_password;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;
import com.justimaginethat.gogodriver.imagecompress.conversionImage;
import com.justimaginethat.gogodriver.zetbaitsu_compressor.Compressor;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lion-1 on 4/17/2017.
 */

public class activity_update_setting extends AppCompatActivity {


    public Context context;
    ImageView updateAddress2;
    ImageView updateAddress;
    ImageView updatePassword;
    ImageView updateMobile;
    ImageView updateEmail;
    ImageView updateName;


    public TextView FirstName;
    public TextView Email;
    public TextView CountryCode;
    public TextView PhoneNumber;
    public TextView PasswordPin;
    public TextView address1;
    public TextView edtAddressFieldS;
    public TextView edtBuildingNumber;
    public TextView edtFloorNumber;
    public LinearLayout action_image;
    public LinearLayout action_signout;
    public CircleImageView imgProfile;
    public ImageView imgBack;
    public TextView img_profileCamera;
    public TextView img_profileGallery;
    public List<SessionDCM> sessionDCMs = new ArrayList<>();
    public SessionDAO sessionDAO;


    public OrderDAO orderDAO;
    public DriverDAO driverDAO;
    public UserDAO userDAO;


    private int RESULT_GALLERY_LOAD_IMG = 11;
    private int RESULT_IMG_LOAD_UPLOAD = 12;
    private int RESULT_CAMERA_LOAD_IMG = 10;

    public int mRequestCodeName = 100;
    public int mRequestCodeEmail = 101;

    public int RESULT_UPDATE = 105;

    public String picturePath = "img";
    public String profilePicture = "";

    public String firstName = "";
    public String emailAddress = "";

    public static activity_update_setting updateSetting;
    public LinearLayout ledtBuilding;
    public LinearLayout ledtAddress;
    private ProgressBar progressProfile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_setting);
        context = this;
        updateSetting = this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_setting);

        progressProfile = (ProgressBar) findViewById(R.id.progressProfile);
        updateName = (ImageView) findViewById(R.id.updateName);
        updateEmail = (ImageView) findViewById(R.id.updateEmail);
        updateMobile = (ImageView) findViewById(R.id.updateMobile);
        updatePassword = (ImageView) findViewById(R.id.updatePassword);
        updateAddress = (ImageView) findViewById(R.id.updateAddress);
        updateAddress2 = (ImageView) findViewById(R.id.updateAddress2);


        FirstName = (TextView) findViewById(R.id.edtFName);
        Email = (TextView) findViewById(R.id.edtEmail);
        PasswordPin = (TextView) findViewById(R.id.edtPassword);
        CountryCode = (TextView) findViewById(R.id.edtCountryCode);
        PhoneNumber = (TextView) findViewById(R.id.edtPhoneNumber);
        address1 = (TextView) findViewById(R.id.edtAdress);
        edtAddressFieldS = (TextView) findViewById(R.id.edtAddressFieldS);

        img_profileCamera = (TextView) findViewById(R.id.img_profileCamera);
        img_profileGallery = (TextView) findViewById(R.id.img_profileGallery);
        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        action_signout = (LinearLayout) findViewById(R.id.action_done);

        ledtBuilding = (LinearLayout) findViewById(R.id.ledtBuilding);
        ledtAddress = (LinearLayout) findViewById(R.id.ledtAddress);

        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCMs = sessionDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FirstName.setText(sessionDCMs.get(0).firstName);
        Email.setText(sessionDCMs.get(0).emailAddress);
        PasswordPin.setText("********************");
//        PasswordPin.setText(sessionDCMs.get(0).password);
        PhoneNumber.setText(sessionDCMs.get(0).mobileNumber);
        address1.setText(sessionDCMs.get(0).address1);
        edtAddressFieldS.setText(sessionDCMs.get(0).address2);
//        PasswordPin.setText(sessionDCMs.get(0).password);

        imgProfile.setImageResource(R.drawable.profile);
        String newProfilePicture = "";





//        if (sessionDCMs.get(0).profilePicture == null || sessionDCMs.get(0).profilePicture.equals("")) {
//            imgProfile.setImageResource(R.drawable.icon_profile_placeholder);
//        } else {
//
//            if (!sessionDCMs.get(0).profilePicture.contains("http:") && sessionDCMs.get(0).profilePicture != null && sessionDCMs.get(0).profilePicture.length() > 0) {
//                newProfilePicture = FixLabels.Server + sessionDCMs.get(0).profilePicture;
//
//
//                Glide.with(this).load(newProfilePicture).listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        progressProfile.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        progressProfile.setVisibility(View.GONE);
//
//                        return false;
//                    }
//                }).error(R.drawable.icon_profile_placeholder).into(imgProfile);
//            } else {
//                Glide.with(this).load(sessionDCMs.get(0).profilePicture)
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                progressProfile.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                progressProfile.setVisibility(View.GONE);
//                                return false;
//                            }
//                        }).error(R.drawable.icon_profile_placeholder).into(imgProfile);
//            }
//        }
//    }








        if (sessionDCMs.get(0).profilePicture == null || sessionDCMs.get(0).profilePicture.equals("")) {
            imgProfile.setImageResource(R.drawable.icon_profile_placeholder);
            progressProfile.setVisibility(View.GONE);
        } else {

            if (!sessionDCMs.get(0).profilePicture.contains("http:")&& sessionDCMs.get(0).profilePicture != null && sessionDCMs.get(0).profilePicture.length() > 0) {
                newProfilePicture = FixLabels.Server + sessionDCMs.get(0).profilePicture;

                Glide.with(this).load(newProfilePicture).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressProfile.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressProfile.setVisibility(View.GONE);
                        return false;
                    }
                }).error(R.drawable.icon_profile_placeholder).into(imgProfile);
            } else {
                Glide.with(this).load(sessionDCMs.get(0).profilePicture).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressProfile.setVisibility(View.GONE);
                        return false;
                    }



                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressProfile.setVisibility(View.GONE);
                        return false;
                    }
                }).error(R.drawable.icon_profile_placeholder).into(imgProfile);
            }

        }







        if(sessionDCMs.get(0).isDriver==true){




                ledtBuilding.setVisibility(View.GONE);
                ledtAddress.setVisibility(View.GONE);

//
//            Intent intent = new Intent(activity_update_setting.this, updateSecondaryAddressProfile.class);
//            startActivityForResult(intent, RESULT_UPDATE);







        }



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionDCMs.get(0).isDriver == true)
                {
                    Intent i = new Intent(activity_update_setting.this, fragment_activity_landing_driver.class);
                    startActivity(i);
                    finish();
                }
                else {

                    Intent i = new Intent(activity_update_setting.this, fragment_activity_landing_user.class);
                    startActivity(i);
                    finish();
                }

            }
        });


//        imgProfile.setImageBitmap(sessionDCMs.get(0).profilePicture);

//        Glide.with(this).load(sessionDCMs.get(0).profilePicture).into(imgProfile);

        img_profileCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LionUtilities.hasConnection(activity_update_setting.this)) {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, RESULT_IMG_LOAD_UPLOAD);

//                    new UpdateImageProfileAsync(updateSetting).execute();

                } else {
                    LionUtilities.makeToast(activity_update_setting.this,
                            "No INTERNET Connection ");
                }
            }
        });
        img_profileGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_GALLERY_LOAD_IMG);
//                new UpdateImageProfileAsync(updateSetting).execute();
            }
        });


        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_update_setting.this, updateNameProfile.class);
                startActivityForResult(intent, RESULT_UPDATE);
//                finish();
            }
        });


        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_update_setting.this, UpdateEmailProfile.class);
                startActivityForResult(intent, RESULT_UPDATE);
//                finish();

            }
        });

        updateMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(activity_update_setting.this, UpdateMobileNumberProfile.class);
//                startActivityForResult(intent, RESULT_UPDATE);
//                finish();

            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_update_setting.this, activity_forgot_password.class);
                intent.putExtra("CalledBy","Settings");
                startActivityForResult(intent, RESULT_UPDATE);
//                finish();

            }
        });

        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_update_setting.this, updateAddressProfile.class);
                startActivityForResult(intent, RESULT_UPDATE);
//                finish();

            }
        });

        updateAddress2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionDCMs.get(0).isDriver == false) {
                    Intent intent = new Intent(activity_update_setting.this, updateSecondaryAddressProfile.class);
                    startActivityForResult(intent, RESULT_UPDATE);
                }
            }
        });


        action_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("Are You sure you want to Sign out ?")
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                new SignOutAsync(updateSetting).execute();
//                                logout();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            if (resultCode == RESULT_OK && requestCode == RESULT_UPDATE) {


                sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                try {
                    sessionDCMs = sessionDAO.getAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                FirstName.setText(sessionDCMs.get(0).firstName);
                Email.setText(sessionDCMs.get(0).emailAddress);
                PasswordPin.setText(sessionDCMs.get(0).password);
                PhoneNumber.setText(sessionDCMs.get(0).mobileNumber);
                address1.setText(sessionDCMs.get(0).address1);
                edtAddressFieldS.setText(sessionDCMs.get(0).address2);
                PasswordPin.setText(sessionDCMs.get(0).password);
                String newProfilePicture = "";



                if(sessionDCMs.get(0).profilePicture==null || sessionDCMs.get(0).profilePicture.equals("")){
                    imgProfile.setImageResource(R.drawable.icon_profile_placeholder);
                }else {
                    if (!sessionDCMs.get(0).profilePicture.contains("http:") && sessionDCMs.get(0).profilePicture != null && sessionDCMs.get(0).profilePicture.length() > 0) {
                        newProfilePicture = FixLabels.Server + sessionDCMs.get(0).profilePicture;
                        Glide.with(this).load(newProfilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgProfile);
                    } else {
                        Glide.with(this).load(sessionDCMs.get(0).profilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgProfile);
                    }

                }




//                if (!sessionDCMs.get(0).profilePicture.contains("http:")) {
//                    newProfilePicture = FixLabels.Server + sessionDCMs.get(0).profilePicture;
//                    Glide.with(this).load(newProfilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgProfile);
//                } else {
//                    Glide.with(this).load(sessionDCMs.get(0).profilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgProfile);
//                }
//
//
            }


            if (requestCode == RESULT_CAMERA_LOAD_IMG) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                picturePath = cursor.getString(column_index_data);
                Bitmap bitmapImage = Compressor.getDefault(this).compressToBitmap(new File(picturePath));
//                    ivShow.setImageBitmap(bitmapImage);
                File imgFile = new File(picturePath);
                if (imgFile.exists()) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imgProfile.setImageBitmap(photo);
                }
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

                Bitmap bitmapImage = Compressor.getDefault(this).compressToBitmap(new File(profilePicture));
                int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                imgProfile.setImageBitmap(scaled);
                new UpdateImageProfileAsync(updateSetting).execute();
            }
            if (requestCode == RESULT_IMG_LOAD_UPLOAD && resultCode == Activity.RESULT_OK) {
                profilePicture = "";
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();
                profilePicture = cursor.getString(column_index_data);
//                profilePicture = new conversionImage().compressImage(profilePicture, this, 864.0f, 1152.0f, 100);

                File imgFile = new File(profilePicture);
                if (imgFile.exists()) {
                    Bitmap bmp =Compressor.getDefault(this).compressToBitmap(new File(profilePicture));
                    imgProfile.setImageBitmap(bmp);


                    new UpdateImageProfileAsync(updateSetting).execute();
                    setResult(RESULT_OK);



                }
               // Toast.makeText(context, profilePicture + "", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void logout() {

        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        sessionDAO.deleteAll();
        sessionDCMs = null;

        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        orderDAO.deleteAll();

        driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        driverDAO.deleteAll();

        userDAO = new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        userDAO.deleteAll();
//        FixLabels.sessionDatabase.user = null;
        FixLabels.sessionDatabase.pickupPointId = 0;
        FixLabels.sessionDatabase.pickupAddress = "";
        FixLabels.sessionDatabase.pickupAddress = "";
        FixLabels.sessionDatabase.pickupLatitude = "";
        FixLabels.sessionDatabase.pickupLongitude = "";
        FixLabels.sessionDatabase.pickupTitle = "";
        Intent i = new Intent(activity_update_setting.this, activity_login.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
        if(sessionDCMs.get(0).isDriver == true)
        {
            Intent i = new Intent(activity_update_setting.this, fragment_activity_landing_driver.class);
            startActivity(i);
            finish();
        }
        else {

            Intent i = new Intent(activity_update_setting.this, fragment_activity_landing_user.class);
            startActivity(i);
            finish();
        }

    }

}
