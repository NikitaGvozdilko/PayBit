package com.justimaginethat.gogodriver.activity_landing_pkg.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.justimaginethat.gogodriver.APIModels.APILoginResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ExtendedView.ButtonLogin;
import com.justimaginethat.gogodriver.ExtendedView.EditTextSegoePrintFont;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_settings.activity_update_setting;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.History.activity_history;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Landing_GetPickUpPointAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.Payment.PaymentCardActivity;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_heading_to_pickup;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_on_the_way;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_pickup_selection_list;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_arrived_at_pickup;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_user_rating;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;
import com.special.ResideMenu.ResideMenu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Lion-1 on 3/4/2017.
 */

public class fragment_activity_landing_user extends FragmentActivity {


    public Context context;
    public ResideMenu resideMenu;
    public CircleImageView img_profile;
    public LinearLayout btnOrder;
    public LinearLayout btnHistory;
    public LinearLayout btnaddplace;
    Dialog dialog;
    public LinearLayout btnPayment;
    public LinearLayout btnLocation;
    public LinearLayout btnSetting;
    public LinearLayout btnLogout;
    public TextView txtnv_userName;
    public TextView txtnv_userName1;
    public static fragment_activity_landing_user fragment_activity_landing_user;
    public APILoginResponseModel apiLogResponseModel = new APILoginResponseModel();
    public FrameLayout contentView;
    public OrderDAO orderDAO;
    public DriverDAO driverDAO;
    public UserDAO userDAO;
    public List<SessionDCM> sessionDCM = new ArrayList<>();
    public SessionDAO sessionDAO;
    public String currentOrderStatus;

    public Button btnLocationSearch;

    public TextView title_text;
    public LinearLayout llImageWithTitle;
    //    public LinearLayout llTitleWithTagLine;
//    public FrameLayout flTitle;
    public ImageView imgBell;
    public TextView txtTitleImg;
    private LinearLayout llTitleWithTagLine;
    public TextView txtTitleOnly;
    public TextView txtTitle1;
    public TextView txtTitle2;
    private FrameLayout flTitle;
    private TextView txtTitle;
    private RelativeLayout btnAddress;
    private ProgressBar progressProfile;
    public boolean appInBackGround = false;
    public boolean callWorkOnAppResume = false;
    public boolean showRatingScreenForLastOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_landing_user);
        fragment_activity_landing_user = this;
        context = this;
        setUIView();
        setUpMenu();
        new Landing_GetPickUpPointAsync(this).execute();
//        try {
//            showLayout(FixLabels.OrderStatus.ReviewPending);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    public void setUIView() {
        llImageWithTitle = (LinearLayout) findViewById(R.id.llImageWithTitle);
        imgBell = (ImageView) findViewById(R.id.imgBell);
        txtTitleImg = (TextView) findViewById(R.id.txtTitleImg);
        txtTitleImg.setText("");
//        imgBell.setVisibility(View.GONE);

        llTitleWithTagLine = (LinearLayout) findViewById(R.id.llTitleWithTagLine);
        txtTitle1 = (TextView) findViewById(R.id.txtTitle1);
        txtTitle1.setText("");
        txtTitle2 = (TextView) findViewById(R.id.txtTitle2);
        txtTitle2.setText("");
//
        flTitle = (FrameLayout) findViewById(R.id.flTitle);
        txtTitleOnly = (TextView) findViewById(R.id.txtTitleOnly);
        txtTitleOnly.setText("");


        btnAddress = (RelativeLayout) findViewById(R.id.btnAddress);
    }

    public void setUpMenu() {

        // attach to current activity;


        btnLocationSearch = (Button) findViewById(R.id.btnLocationSearch);
        contentView = (FrameLayout) findViewById(R.id.contentView);
        resideMenu = new ResideMenu(this, R.layout.layout_left_reside_menu, -1);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
//        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setUse3D(false);
        resideMenu.setBackground(R.drawable.icon_nbackground);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        btnOrder = (LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnOrder);
        txtnv_userName = (TextView) resideMenu.getLeftMenuView().findViewById(R.id.nv_userName);
//        txtnv_userName1 =(TextView) resideMenu.getLeftMenuView().findViewById(R.id.nv_userName1);

        btnHistory = (LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnHistory);
        btnPayment = (LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnPayment);
        btnaddplace = (LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnaddplace);

        btnLocation = (LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnLocation);
        btnSetting = (LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnSetting);
        btnLogout = (LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnLogout);
        progressProfile = (ProgressBar) resideMenu.getLeftMenuView().findViewById(R.id.progressProfile);

        img_profile = (CircleImageView) resideMenu.getLeftMenuView().findViewById(R.id.nv_userIMG);
        String newProfilePicture = "";


        try {
            sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
            sessionDCM = sessionDAO.getAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (sessionDCM.get(0).profilePicture == null || sessionDCM.get(0).profilePicture.equals("")) {
            img_profile.setImageResource(R.drawable.icon_profile_placeholder);
            progressProfile.setVisibility(View.GONE);
        } else {
            if (!sessionDCM.get(0).profilePicture.contains("http:") && sessionDCM.get(0).profilePicture != null && sessionDCM.get(0).profilePicture.length() > 0) {
                newProfilePicture = FixLabels.Server + sessionDCM.get(0).profilePicture;


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
                }).error(R.drawable.icon_profile_placeholder).into(img_profile);
            } else {
                Glide.with(this).load(sessionDCM.get(0).profilePicture)
                        .listener(new RequestListener<String, GlideDrawable>() {
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
                        }).error(R.drawable.icon_profile_placeholder).into(img_profile);
            }


        }


        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.closeMenu();
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(fragment_activity_landing_user.this, activity_history.class);
                startActivity(intent);
                resideMenu.closeMenu();
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PaymentCardActivity.class);
                startActivity(intent);
                resideMenu.closeMenu();

            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.closeMenu();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_update_setting.class);
                startActivity(intent);
                resideMenu.closeMenu();
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                resideMenu.closeMenu();
                finish();
            }
        });

        btnLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_default_address_setter.class);
                startActivityForResult(intent, 101);
            }
        });
        txtnv_userName.setText(sessionDCM.get(0).userName);
//        txtnv_userName1.setText(sessionDCM.get(0).emailAddress);


        btnaddplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }


    public void showCustomDialog() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.email_popupsss);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);


        final EditTextSegoePrintFont s_place = (EditTextSegoePrintFont) dialog.findViewById(R.id.s_place);
        ButtonLogin btnsubmit = (ButtonLogin) dialog.findViewById(R.id.btnsubmit);


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s_place.getText().toString().length() == 0) {
                    s_place.setError("Please enter valid details");
                    s_place.requestFocus();

                } else {


                    try {
                        String place = s_place.getText().toString();
                        String email = "info@jitofficial.com";
                        String subject = "GOGODRIVER: NEW LOCATION";
                        String body = "Username : " + sessionDCM.get(0).userName + System.getProperty("line.separator") + "Phone Number : " + sessionDCM.get(0).mobileNumber + System.getProperty("line.separator") + "Email : " + sessionDCM.get(0).emailAddress;
                        String msg = "Suggested Place : " + place;
                        String message = body + System.getProperty("line.separator") + msg;


                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");

                        intent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                new String[]{email});
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                subject);

                        intent
                                .putExtra(android.content.Intent.EXTRA_TEXT, message);


                        intent.setPackage("com.google.android.gm");
                        if (intent.resolveActivity(getPackageManager()) != null)
                            startActivity(intent);
                        else
                            Toast.makeText(context, "Gmail App is not installed", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    } catch (Throwable t) {
                        Toast.makeText(context,
                                "Request failed try again: " + t.toString(),
                                Toast.LENGTH_LONG).show();
                    }


                }
            }
        });


        dialog.show();


    }

    public void showLayout(String orderStatus) throws SQLException {
        currentOrderStatus = orderStatus;
        Fragment currentAddedView = null;
        contentView.removeAllViews();
        if (FixLabels.OrderStatus.ReviewPending == orderStatus || FixLabels.OrderStatus.OrderComplete == orderStatus || FixLabels.OrderStatus.DriverReachedYourLocation == orderStatus) {
            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_user_rating();//create the fragment instance for the top fragment
            ((fragment_user_rating) currentAddedView).fActivity = this;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
            llImageWithTitle.setVisibility(View.VISIBLE);
            llTitleWithTagLine.setVisibility(View.GONE);
            flTitle.setVisibility(View.VISIBLE);
            txtTitleOnly.setText("PICKUP COMPLETED");
            imgBell.setVisibility(View.GONE);
            txtTitleImg.setText("PICKUP COMPLETED");
            btnAddress.setVisibility(View.INVISIBLE);
            hideKeyboard(this);
        } else if (FixLabels.OrderStatus.DriverHeadingToYourLocation.equals(orderStatus) || FixLabels.OrderStatus.PaymentComplete.equals(orderStatus)) {
            contentView.removeAllViews();

            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_driver_on_the_way();//create the fragment instance for the top fragment
            ((fragment_driver_on_the_way) currentAddedView).fActivity = this;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();

            llImageWithTitle.setVisibility(View.VISIBLE);
            llTitleWithTagLine.setVisibility(View.GONE);
            flTitle.setVisibility(View.GONE);
            imgBell.setVisibility(View.VISIBLE);
            txtTitleImg.setText("DRIVER ON THE WAY.");
            btnAddress.setVisibility(View.INVISIBLE);
            hideKeyboard(this);
        } else if (FixLabels.OrderStatus.PleaseConfirmPayment.equals(orderStatus) || FixLabels.OrderStatus.ConfirmingPayment.equals(orderStatus)) {
            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_driver_arrived_at_pickup();//create the fragment instance for the top fragment
            ((fragment_driver_arrived_at_pickup) currentAddedView).fActivity = this;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();

            llImageWithTitle.setVisibility(View.VISIBLE);
            llTitleWithTagLine.setVisibility(View.GONE);
            flTitle.setVisibility(View.GONE);
            imgBell.setVisibility(View.VISIBLE);
            txtTitleImg.setText("DRIVER ARRIVED AT PICKUP POINT");
            btnAddress.setVisibility(View.INVISIBLE);
            hideKeyboard(this);

        } else if (FixLabels.OrderStatus.DriverHeadingToPickUpPoint.equals(orderStatus) || FixLabels.OrderStatus.DriverArrivedAtPickUpPoint.equals(orderStatus)) {
            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_driver_heading_to_pickup();//create the fragment instance for the top fragment
            ((fragment_driver_heading_to_pickup) currentAddedView).fActivity = this;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();

            llImageWithTitle.setVisibility(View.GONE);
            llTitleWithTagLine.setVisibility(View.GONE);
            flTitle.setVisibility(View.VISIBLE);
            imgBell.setVisibility(View.GONE);
            txtTitleOnly.setText("DRIVER HEADING TO PICKUP POINT");
            btnAddress.setVisibility(View.INVISIBLE);
            hideKeyboard(this);

        } else if (FixLabels.OrderStatus.PrepareNewOrder.equals(orderStatus) || FixLabels.OrderStatus.WaitingForDriver.equals(orderStatus)) {

            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_layout_confirm();//create the fragment instance for the top fragment
            ((fragment_layout_confirm) currentAddedView).fActivity = this;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
            llImageWithTitle.setVisibility(View.GONE);
            llTitleWithTagLine.setVisibility(View.VISIBLE);
            flTitle.setVisibility(View.GONE);

            btnAddress.setVisibility(View.INVISIBLE);
            SessionDAO sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
            SessionDCM sessionDCM = sessionDAO.getAll().get(0);
            txtTitle1.setText(sessionDCM.defaultAddress);
            txtTitle2.setText(sessionDCM.defaultBuildingName + ", " + sessionDCM.defaultFloorNumber);
            hideKeyboard(this);


//            SessionDAO sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
//            SessionDCM sessionDCM = sessionDAO.getAll().get(0);
//            txtTitleImg.setText(sessionDCM.defaultAddress+sessionDCM.defaultBuildingName + ", " + sessionDCM.defaultFloorNumber);
//
//
//            txtTitle.setText(sessionDCM.defaultAddress);
//            txtTitle2.setText(sessionDCM.defaultBuildingName + ", " + sessionDCM.defaultFloorNumber);

        }


//        else if( FixLabels.OrderStatus.Payment.equals(orderStatus)){
//            contentView.removeAllViews();
//            if (currentAddedView != null) {
//                FragmentManager manager = getSupportFragmentManager();
//                manager.beginTransaction().remove(currentAddedView);
//            }
//            currentAddedView = new fragment_payment();//create the fragment instance for the top fragment
//            ((fragment_payment) currentAddedView).FRequestPayment = this;
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
//
//            llImageWithTitle.setVisibility(View.GONE);
//            llTitleWithTagLine.setVisibility(View.GONE);
//            flTitle.setVisibility(View.VISIBLE);
//            txtTitleOnly.setText("DRIVER HEADING TO PICKUP POINT");
//
//        }
        else if (FixLabels.OrderStatus.NoActiveOrder.equals(orderStatus)) {
            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_layout_pickup_selection_list();//create the fragment instance for the top fragment
            ((fragment_layout_pickup_selection_list) currentAddedView).fActivity = this;

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();

            llImageWithTitle.setVisibility(View.GONE);
            llTitleWithTagLine.setVisibility(View.VISIBLE);
            flTitle.setVisibility(View.GONE);
            SessionDAO sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
            SessionDCM sessionDCM = sessionDAO.getAll().get(0);
            txtTitle1.setText(sessionDCM.defaultAddress);
            txtTitle2.setText(sessionDCM.defaultBuildingName + ", " + sessionDCM.defaultFloorNumber);
            if (sessionDCM.defaultAddress.equals(null) || sessionDCM.defaultAddress.equals("null") || sessionDCM.defaultAddress.equals("NULL") || sessionDCM.defaultAddress.equals("")) {
                sessionDCM.defaultAddress = "Please select your default address";
            }
            if (sessionDCM.defaultBuildingName.equals(null) || sessionDCM.defaultBuildingName.equals("null") || sessionDCM.defaultBuildingName.equals("NULL") || sessionDCM.defaultBuildingName.equals("")) {
                sessionDCM.defaultBuildingName = "";
            }

            if (sessionDCM.defaultFloorNumber.equals(null) || sessionDCM.defaultFloorNumber.equals("null") || sessionDCM.defaultFloorNumber.equals("NULL") || sessionDCM.defaultFloorNumber.equals("")) {
                sessionDCM.defaultFloorNumber = "";
            }

            txtTitleImg.setText(sessionDCM.defaultAddress + sessionDCM.defaultBuildingName + ", " + sessionDCM.defaultFloorNumber);

            btnAddress.setVisibility(View.VISIBLE);
            txtTitle1.setText(sessionDCM.defaultAddress);
            txtTitle2.setText(sessionDCM.defaultBuildingName + ", " + sessionDCM.defaultFloorNumber);

        }
    }

    public ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    public void logout() {

        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        sessionDAO.deleteAll();


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
        Intent i = new Intent(fragment_activity_landing_user.this, activity_login.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            showLayout(currentOrderStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
        } else {

            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        appInBackGround = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        appInBackGround = false;
        if (callWorkOnAppResume == true && showRatingScreenForLastOrder == false) {
            callWorkOnAppResume = false;
            new Landing_GetPickUpPointAsync(this).execute();
        } else if (callWorkOnAppResume == true && showRatingScreenForLastOrder == true) {
            try {
                showLayout(FixLabels.OrderStatus.OrderComplete);
                showRatingScreenForLastOrder = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}