package com.justimaginethat.gogodriver.activity_landing_pkg.driver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.justimaginethat.gogodriver.APIModels.APILoginResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Landing_GetPickUpPointAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.History.activity_history;
import com.justimaginethat.gogodriver.activity_settings.activity_update_setting;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.DriverActiveOrderAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_no_active_order;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_driver_conform;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_customer;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_pickup;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_payment_confirm;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_rate_DriverToCustomer;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_request_payment;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;
import com.special.ResideMenu.ResideMenu;

import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Lion-1 on 3/4/2017.
 */

public class fragment_activity_landing_driver extends FragmentActivity {


    public Context context;



    public TextView title_text;


    private ResideMenu resideMenu;
    private CircleImageView img_profile;
    private LinearLayout btnOrder;
    private LinearLayout btnHistory;
    private LinearLayout btnPayment;

    private LinearLayout btnLocation;
    private LinearLayout btnSetting;
    private LinearLayout btnLogout;
    private TextView txtnv_userName;
    private TextView txtnv_userName1;

//    public Button btnLocationSearch;



    public static fragment_activity_landing_driver fragment_activity_landing_driver;
    private APILoginResponseModel apiLogResponseModel = new APILoginResponseModel();
    public FrameLayout contentView;
    public OrderDAO orderDAO;
    public DriverDAO driverDAO;
    public UserDAO userDAO;


    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();
    public ProgressBar progressProfile;
    public boolean appInBackGround;
    public boolean callWorkOnAppResume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_landing_driver);
        fragment_activity_landing_driver=this;
        context = this;
        sessionDAO= new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        setUIView();
        setUpMenu();

        new DriverActiveOrderAsync(this).execute();
    }
    public void showLayout(String orderStatus) {

        Fragment currentAddedView = null;
        contentView.removeAllViews();

        if( FixLabels.OrderStatus.NoActiveOrder .equals( orderStatus)  || FixLabels.OrderStatus.OrderCanceled
                .equals( orderStatus))
        {
            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_no_active_order();//create the fragment instance for the top fragment
            ((fragment_no_active_order) currentAddedView).fActivity = this;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
            title_text.setText("WELCOME,"+sessionDCM.firstName);
        }



        else if( FixLabels.OrderStatus.OrderAcceptOrReject .equals(orderStatus)){

            {
                contentView.removeAllViews();
                if (currentAddedView != null) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().remove(currentAddedView);
                }
                currentAddedView = new fragment_driver_conform();//create the fragment instance for the top fragment
                ((fragment_driver_conform) currentAddedView).fActivity = this;
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
                title_text.setText("WELCOME,"+sessionDCM.firstName);
            }
        }
        else if( FixLabels.OrderStatus.DriverHeadingToPickUpPoint .equals(orderStatus) ){
            {
                contentView.removeAllViews();
                if (currentAddedView != null) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().remove(currentAddedView);
                }
                currentAddedView = new fragment_goto_pickup();//create the fragment instance for the top fragment
                ((fragment_goto_pickup) currentAddedView).fActivity = this;
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
                title_text.setText("GO TO PICKUP");

            }
        }
        else if( FixLabels.OrderStatus.DriverArrivedAtPickUpPoint.equals(orderStatus)){

            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_request_payment();//create the fragment instance for the top fragment
            ((fragment_request_payment) currentAddedView).fActivity = this;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
            title_text.setText("ENTER AMOUNT");
        }

//
//        else if( FixLabels.OrderStatus.PleaseConfirmPayment .equals( orderStatus)){
//            contentView.removeAllViews();
//            if (currentAddedView != null) {
//                FragmentManager manager = getSupportFragmentManager();
//                manager.beginTransaction().remove(currentAddedView);
//            }
//            currentAddedView = new fragment_success_fully_paid();//create the fragment instance for the top fragment
//            ((fragment_success_fully_paid) currentAddedView).fActivity = this;
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
//            title_text.setText("CONFIRM PAYMENT");
//        }




        else if( FixLabels.OrderStatus.PleaseConfirmPayment .equals( orderStatus) || FixLabels.OrderStatus.ConfirmingPayment.equals( orderStatus)){
            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_payment_confirm();//create the fragment instance for the top fragment
            ((fragment_payment_confirm) currentAddedView).fActivity = this;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
            title_text.setText("CONFIRM PAYMENT");
            hideKeyboard(this);

        }







        else if( FixLabels.OrderStatus.DriverHeadingToYourLocation .equals( orderStatus)||FixLabels.OrderStatus.PaymentComplete .equals( orderStatus)){
            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_goto_customer();//create the fragment instance for the top fragment
            ((fragment_goto_customer) currentAddedView).fActivity = this;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
            title_text.setText("GO TO CUSTOMER");
            hideKeyboard(this);

        }

        else if( FixLabels.OrderStatus.DriverReachedYourLocation .equals( orderStatus) || FixLabels.OrderStatus.OrderComplete .equals( orderStatus)){

            contentView.removeAllViews();
            if (currentAddedView != null) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(currentAddedView);
            }
            currentAddedView = new fragment_rate_DriverToCustomer();//create the fragment instance for the top fragment
            ((fragment_rate_DriverToCustomer) currentAddedView).fActivity = this;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, currentAddedView).commitAllowingStateLoss();
            title_text.setText("RATE CUSTOMER");
            hideKeyboard(this);
        }
    }


    public void setUIView(){
        title_text=(TextView)findViewById(R.id.title_text);
        title_text.setText("");
//        btnLocationSearch=(Button)findViewById(R.id.btnLocationSearch);
    }
    public void setUpMenu() {
        contentView=(FrameLayout)findViewById(R.id.contentView);
        resideMenu = new ResideMenu(this,R.layout.driver_left_reside_menu,-1);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
//        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        resideMenu.setUse3D(false);
        resideMenu.setBackground(R.drawable.icon_nbackground);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        img_profile =(CircleImageView) resideMenu.getLeftMenuView().findViewById(R.id.nv_userIMG);
        progressProfile = (ProgressBar) resideMenu.getLeftMenuView().findViewById(R.id.progressProfile);
        String newProfilePicture="";


        if(sessionDCM.profilePicture==null || sessionDCM.profilePicture.equals("")){
            img_profile.setImageResource(R.drawable.icon_profile_placeholder);
            progressProfile.setVisibility(View.GONE);
        }else {
            if (!sessionDCM.profilePicture.contains("http:") && sessionDCM.profilePicture != null && sessionDCM.profilePicture.length() > 0) {

                newProfilePicture=FixLabels.Server+ sessionDCM.profilePicture;
                Glide.with(this).load(newProfilePicture) .listener(new RequestListener<String, GlideDrawable>() {
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
            }else {
                Glide.with(this).load(sessionDCM.profilePicture) .listener(new RequestListener<String, GlideDrawable>() {
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

        txtnv_userName =(TextView) resideMenu.getLeftMenuView().findViewById(R.id.nv_userName);
//        txtnv_userName1 =(TextView) resideMenu.getLeftMenuView().findViewById(R.id.nv_userName1);

        txtnv_userName.setText(sessionDCM.userName);
//        txtnv_userName1.setText(sessionDCM.emailAddress);
        btnOrder =(LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.closeMenu();
            }
        });
        btnHistory =(LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(fragment_activity_landing_driver.this ,activity_history.class);
                startActivity(intent);
                resideMenu.closeMenu();
                finish();
            }
        });

        btnPayment =(LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.closeMenu();
            }
        });
        btnLocation =(LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.closeMenu();
            }
        });
        btnSetting =(LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context,activity_update_setting.class);
                startActivity(intent);
                resideMenu.closeMenu();
                finish();
            }
        });

//        btnLocationSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context,fragment_address_driver.class);
//                startActivity(intent);
//            }
//        });




        btnLogout =(LinearLayout) resideMenu.getLeftMenuView().findViewById(R.id.btnLogout);
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logout();
//                resideMenu.closeMenu();
//                finish();
//            }
//        });

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//                hideKeyboard(resideMenu.this);
            }
        });
    }
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };
    public void logout(){

        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        sessionDAO.deleteAll();
        sessionDCM = null;

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
        Intent i = new Intent(fragment_activity_landing_driver.this, activity_login.class);
            startActivity(i);

    }
    public void hideKeyboard(Activity activity)
    {

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
        if(resideMenu.isOpened()){
            resideMenu.closeMenu();
        }else {

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
        if(callWorkOnAppResume == true)
        {
            callWorkOnAppResume = false;
            new DriverActiveOrderAsync(this).execute();
        }
    }
}