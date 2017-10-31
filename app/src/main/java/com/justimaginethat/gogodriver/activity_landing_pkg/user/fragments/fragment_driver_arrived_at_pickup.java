package com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.AtPickupPoint_GetOrderAmountAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.AtPickupPoint_GetOrderStatusAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Pay_CancelOrderAfterAcceptAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.Payment.PaymentCardActivity;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_map_marker.OnMapAndViewReadyListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Lion-1 on 3/6/2017.
 */

public class fragment_driver_arrived_at_pickup extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {
    public fragment_activity_landing_user fActivity;
    public FrameLayout frame_bottom_down_two;
    public FrameLayout frame_bottom_down_one;
    public ImageView action_image_up;
    public ImageView action_image_down;
    public LinearLayout action_pay;
    public LinearLayout action_done;
    public RelativeLayout frame_pay;
    public FrameLayout frame_done;
    public FrameLayout frame_main;
    public FrameLayout frame_sub;
    public FrameLayout frame_sub2;


    private SupportMapFragment mapView;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public FrameLayout pickerLayoutFL;
    public FrameLayout pickerLayoutConBL;

    public LayoutInflater inflater;

    public TextView driverName;
    public CircleImageView imgDriver;
    public TextView driverRating;

    public TextView driverRate;
    public TextView orderRate;
    public TextView totalRate;
    public fragment_driver_arrived_at_pickup rateAmount;
    public String URLtoCall = "http://www.google.com";
    public WebView web_view;
    public String str;
    public LinearLayout frameWebView;
    public List<DriverDCM> driverDCMList = new ArrayList<>();
    public DriverDAO driverDAO;
    private boolean callURLFlag = false;
    public String url = "";
    public RelativeLayout rlLoading;
    public ProgressBar pbCircle;
    public boolean canGetOrderStatusFlag = false;
    public Handler timer;
    public Handler timerLocal;
    public ProgressDialog progressLocal;
    public int counterWeb = 0;
    public Button btnRetry;
//    public LinearLayout call_action;//sp 9-5-2017
    public ImageView call_action;
    public Context context;
    private ImageView btnCancelConfirm;

    public fragment_driver_arrived_at_pickup() {
    }
//    totalRate,driverRate,orderRate
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_arrived_at_pickup, container, false);

        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        rateAmount = this;
        mapView.getMapAsync(this);
        context = getContext();
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);
        pbCircle = (ProgressBar) view.findViewById(R.id.pbCircular);
        rlLoading.setVisibility(View.INVISIBLE);
        frameWebView = (LinearLayout) view.findViewById(R.id.frameWebView);
        btnRetry = (Button) view.findViewById(R.id.btnRetry);
        action_pay = (LinearLayout) view.findViewById(R.id.action_pay);
        action_done = (LinearLayout) view.findViewById(R.id.action_done);

        frame_main = (FrameLayout) view.findViewById(R.id.frame_main);
        frame_sub = (FrameLayout) view.findViewById(R.id.frame_sub);

        frame_pay = (RelativeLayout) view.findViewById(R.id.frame_pay);
        frame_done = (FrameLayout) view.findViewById(R.id.frame_done);
        frame_sub2 = (FrameLayout) view.findViewById(R.id.frame_sub2);
        btnCancelConfirm = (ImageView) view.findViewById(R.id.btnCancelConfirm);

        frame_bottom_down_one = (FrameLayout) view.findViewById(R.id.frame_bottom_down_one);
        frame_bottom_down_two = (FrameLayout) view.findViewById(R.id.frame_bottom_down_two);
        action_image_up = (ImageView) view.findViewById(R.id.action_image_up);
        driverName = (TextView) view.findViewById(R.id.driverName);
//        imgUser=(ImageView)view.findViewById(R.id.imgUser);
        imgDriver = (CircleImageView) view.findViewById(R.id.imgDriver);
        driverRating = (TextView) view.findViewById(R.id.driverRating);
        driverRate = (TextView) view.findViewById(R.id.driverRate);
        orderRate = (TextView) view.findViewById(R.id.orderRate);
        totalRate = (TextView) view.findViewById(R.id.totalRate);

        //        call_action = (LinearLayout) view.findViewById(R.id.call_action);//sp 9-5-2017
        call_action = (ImageView) view.findViewById(R.id.call_action);//sp 9-5-2017

        action_image_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_bottom_down_one.setVisibility(View.VISIBLE);
                action_image_up.setImageResource(R.drawable.icon_marker);
            }
        });
        action_image_down = (ImageView) view.findViewById(R.id.action_image_down);
        action_image_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_bottom_down_one.setVisibility(View.INVISIBLE);
                action_image_up.setImageResource(R.drawable.icon_up_arrow);
            }
        });
        frameWebView.setVisibility(View.GONE);

        driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());

        try {
            driverDCMList = driverDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (DriverDCM driverDCMs : driverDCMList
                ) {
            String newProfilePicture = "";



            if(driverDCMs.profilePicture==null || driverDCMs.profilePicture.equals("")){
                imgDriver.setImageResource(R.drawable.icon_profile_placeholder);
            }else {
                if (!driverDCMs.profilePicture.contains("http:") && driverDCMs.profilePicture != null && driverDCMs.profilePicture.length() > 0) {
                    newProfilePicture = FixLabels.Server + driverDCMs.profilePicture;
                    Glide.with(this).load(newProfilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgDriver);
                } else {
                    Glide.with(this).load(driverDCMs.profilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgDriver);
                }

            }





//            if (!driverDCMs.profilePicture.contains("http:")) {
//                newProfilePicture = FixLabels.Server + driverDCMs.profilePicture;
//                Glide.with(this).load(newProfilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgDriver);
//            } else {
//                Glide.with(this).load(driverDCMs.profilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgDriver);
//            }


            driverName.setText(driverDCMs.firstName);



            if(driverDCMs.syncGUID==null||driverDCMs.syncGUID==""||driverDCMs.syncGUID.equals("")||driverDCMs.syncGUID.equals(null)){
                driverRating.setText("5"+"/5");
            }else {
                driverRating.setText(driverDCMs.syncGUID + "/5");
            }


//            driverRating.setText(driverDCMs.syncGUID + "/5");sp 9-5-2017
        }


        call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle(driverDCMList.get(0).mobileNumber)
                        .setMessage("Are you sure Make call?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MakeCall(driverDCMList.get(0).mobileNumber);
                            }
                        })
                        .setIcon(android.R.drawable.sym_call_outgoing)
                        .show();


            }
        });


        action_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View views) {

                boolean foundDefault = false;
                CardsDAO cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());

                try {
                    if (cardsDAO.getAll().size() == 0) {
                        Intent i = new Intent(getContext(), PaymentCardActivity.class);
                        i.putExtra("selectionOnly", true);
                        startActivityForResult(i, 101);
                    } else {

                        new AtPickupPoint_GetOrderAmountAsync(rateAmount, true).execute();

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        action_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    fActivity.showLayout(FixLabels.OrderStatus.DriverHeadingToYourLocation);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//
//                frameWebView.setVisibility(View.GONE);
//                frame_pay.removeAllViews();
//                callAsynchronousTask();
//                frame_done.removeAllViews();
//                frame_main.removeAllViews();
//                frame_sub2.removeAllViews();

            }
        });
        web_view = (WebView) view.findViewById(R.id.web_view);

        btnCancelConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("If you cancel this order you will be charged a $2 cancellation fee.");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                                canGetOrderStatusFlag = false;
                                if(timer != null)
                                {
                                    timer.removeCallbacksAndMessages(null);
                                }
                                timer = null;
                                new Pay_CancelOrderAfterAcceptAsync(rateAmount).execute();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_pay.setVisibility(View.VISIBLE);
                frame_done.setVisibility(View.INVISIBLE);
                rlLoading.setVisibility(View.INVISIBLE);
                frameWebView.setVisibility(View.INVISIBLE);
            }
        });
        frame_pay.setVisibility(View.INVISIBLE);


        return view;
    }

    public void MakeCall(String callNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(("tel:" + callNumber)));
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    public void callAsynchronousTask() {
        timer = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                try {
                    if (canGetOrderStatusFlag == true) {
                        rateAmount.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                new AtPickupPoint_GetOrderStatusAsync(rateAmount).execute();
                            }
                        });
                    }
                    timer.postDelayed(this, 1000);
                } catch (Exception e) {
                    Log.e("Error Handler", String.valueOf(e));
                }
            }
        };
        timer.postDelayed(runnableCode, 1000);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;
        buildGoogleApiClient();


    }

    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(fActivity.getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
        new AtPickupPoint_GetOrderAmountAsync(rateAmount, false).execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public int cardSelectionIndex = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    cardSelectionIndex = Integer.parseInt(data.getData().toString());
                }
                action_pay.callOnClick();

            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canGetOrderStatusFlag = false;
        if (timer != null) {
            timer.removeCallbacksAndMessages(null);
            timer = null;
        }
        if (timerLocal != null) {
            timerLocal.removeCallbacksAndMessages(null);
            timerLocal = null;
        }

    }
}
