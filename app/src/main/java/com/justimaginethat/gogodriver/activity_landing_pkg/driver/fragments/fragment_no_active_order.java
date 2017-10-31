package com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetNearByDriverRequestModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.ChangeWorkStatusAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.GetNewOrderAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.NoActive_GetDriverOrderStatus;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.RejectOrderAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_CancelOrderAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_CheckNearByDriverAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_GetOrderStatusAsync;
import com.skyfishjy.library.RippleBackground;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.deanwild.marqueetextview.MarqueeTextView;

import static android.content.Context.POWER_SERVICE;
import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.WINDOW_SERVICE;
import static com.justimaginethat.gogodriver.R.id.imgDriver;
import static com.justimaginethat.gogodriver.R.id.time;

/**
 * Created by Lion-1 on 3/7/2017.
 */

public class fragment_no_active_order extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public GoogleMap map;
    ArrayList<LatLng> markerPoints;
    public LatLng position;
    Polyline line;

    public LinearLayout llSwitchContainer;
    public LinearLayout btnLlOffline;
    public LinearLayout btnLlOnline;
    public ImageView btnImgOffline;
    public ImageView btnImgOnline;
    public ObjectAnimator processObjectAnimator;

    public int startTime;

    public Runnable updateTime;


    public GoogleMap mGoogleMap;
    Double yourLat;
    Double yourLon;


    public ProgressBar pbCircular;
    public SupportMapFragment mapView;
    private GoogleApiClient mGoogleApiClient;
    public Marker currentLocationMarker;

    public Boolean workStatus;
    public String oStatus;

    public Context context;
    public fragment_activity_landing_driver fActivity;
    public View view;

    public TextView display_customer;
    public TextView lblCustomerName;
    public MarqueeTextView lblPickupAddress;
    public TextView lblCustomerAddress;


    public ImageView btnImgAccept;
    public ImageView btnImgReject;
    public FrameLayout flAcceptRejectDialog;

    public TextView lblExpiryCounter;
    public Handler timer;
    public fragment_no_active_order fragment_no_active_order;
    public Handler timer2;
    public List<UserDCM> userDCMList = new ArrayList<>();
    public UserDAO userDAO;
    public Location mLastLocation;
    public String addressDetail;
    public double heading = 0;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    public double longitude;
    public double latitude;
    public TextView lblbearing;


    public float mSensorX;
    public float mSensorY;
    public Display mDisplay;
    public SensorManager sm;
    public PowerManager mPowerManager;
    public WindowManager mWindowManager;
    private ScrollView scrollerMap;
    private RelativeLayout frame_sub2;
    public boolean canGetOrderStatusFlag;


    public fragment_no_active_order() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {






        view = inflater.inflate(R.layout.fragment_driver, container, false);
        context = getContext();



//        sm = (SensorManager) context.getSystemService(SENSOR_SERVICE);
//        if(sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0){
//            Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
//            sm.registerListener(this,s, SensorManager.SENSOR_DELAY_NORMAL);
//        }

        // Get an instance of the PowerManager
        mPowerManager = (PowerManager)context.getSystemService(POWER_SERVICE);

        // Get an instance of the WindowManager
        mWindowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();



        markerPoints = new ArrayList<LatLng>();
        fragment_no_active_order = this;
        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapView.getMapAsync(this);
        mapView.getView().setClickable(false);

        setUI();
        clickEvent();

        return view;
    }


    public void setUI() {
        RippleBackground aniRipple = (RippleBackground) view.findViewById(R.id.aniRipple);
        aniRipple.startRippleAnimation();
        llSwitchContainer = (LinearLayout) view.findViewById(R.id.llSwitchContainer);
        btnLlOffline = (LinearLayout) view.findViewById(R.id.btnLlOffline);
        btnLlOnline = (LinearLayout) view.findViewById(R.id.btnLlOnline);
        btnImgOffline = (ImageView) view.findViewById(R.id.btnImgOffline);
        btnImgOnline = (ImageView) view.findViewById(R.id.btnImgOnline);
        lblCustomerName = (TextView) view.findViewById(R.id.lblCustomerName);
        lblbearing = (TextView) view.findViewById(R.id.lblbearing);
        lblPickupAddress = (MarqueeTextView) view.findViewById(R.id.lblPickupAddress);
        lblCustomerAddress = (TextView) view.findViewById(R.id.lblCustomerAddress);
        btnImgAccept = (ImageView) view.findViewById(R.id.btnImgAccept);
        btnImgReject = (ImageView) view.findViewById(R.id.btnImgReject);
        pbCircular = (ProgressBar) view.findViewById(R.id.pbCircular);
        flAcceptRejectDialog = (FrameLayout) view.findViewById(R.id.flAcceptRejectDialog);


        processObjectAnimator = ObjectAnimator.ofFloat(pbCircular,
                "rotation", 0f, 360f);
        processObjectAnimator.setDuration(1000);
        processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        processObjectAnimator.setInterpolator(new LinearInterpolator());
        processObjectAnimator.start();


        enableDisableViewGroup((ViewGroup) mapView.getView(),false);
        lblExpiryCounter = (TextView) view.findViewById(R.id.lblExpiryCounter);
        btnLlOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnlineUI();
                new ChangeWorkStatusAsync(fragment_no_active_order.this).execute();
            }
        });
        btnLlOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOfflineUI();
                new ChangeWorkStatusAsync(fragment_no_active_order.this).execute();
            }
        });
        btnImgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oStatus = "Accepted";
                canGetNewOrder = false;
                canGetOrderStatusFlag = false;
                if(timer != null) {
                    timer.removeCallbacksAndMessages(null);

                } timer = null;
                if(timer2 != null) {
                    timer2.removeCallbacksAndMessages(null);

                }timer2 = null;
                new RejectOrderAsync(fragment_no_active_order).execute();

            }
        });
        btnImgReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canGetOrderStatusFlag = false;
                oStatus = "Rejected";


                new RejectOrderAsync(fragment_no_active_order).execute();
            }
        });
        flAcceptRejectDialog.setVisibility(View.GONE);


    }
    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        // Request location updates
        if (ActivityCompat.checkSelfPermission(fActivity.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fActivity.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        getCurrentLocation();


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(fragment_no_active_order.context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }

    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (mLastLocation != null) {
            heading = mLastLocation.bearingTo(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));

        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            addressDetail = getCompleteAddressString(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
//                  mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);


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



//        if (ActivityCompat.checkSelfPermission(fActivity.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fActivity.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            if (mLastLocation != null) {
//                  heading = mLastLocation.bearingTo(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
//                  addressDetail =  getCompleteAddressString(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//
//                Toast.makeText(fActivity.context, heading + "", Toast.LENGTH_SHORT).show();
//              }

            if (mLastLocation != null) {
//                double bearing = mLastLocation.bearingTo(location);
////                heading = mLastLocation.getBearing();
//                heading = (360 + ((bearing + 360) % 360) - heading) % 360;
                mLastLocation = location;
//                lblbearing.setText(heading + "");
            }
//            Log.e("Location Update", "onLocationChanged: " + heading);
//            Toast.makeText(fActivity.context,heading + " ",Toast.LENGTH_LONG).show();








    }


          public void clickEvent(){
             setOnlineUI();
             new ChangeWorkStatusAsync(fragment_no_active_order.this).execute();
          }
          @Override
          public void onMapReady(GoogleMap googleMap) {
              mGoogleMap = googleMap;
              mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
              mGoogleMap.getUiSettings().setAllGesturesEnabled(false);
              mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);

              if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  mGoogleMap.setMyLocationEnabled(true);
                  return;
              }
              buildGoogleApiClient();
          }

          public boolean canGetNewOrder = false;
          public void callAsynchronousTask() {


              timer = new Handler();
              Runnable runnableCode = new Runnable() {
                  @Override
                  public void run() {
                      try {

                          if(canGetNewOrder ==true){
                              fActivity.runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      new GetNewOrderAsync(fragment_no_active_order).execute();
                                  }
                              });


                          }

                          if(canGetOrderStatusFlag == true)
                          {
                              fActivity.runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      new NoActive_GetDriverOrderStatus(fragment_no_active_order).execute();
                                  }
                              });
                          }

                          timer.postDelayed(this, 1000);
                      } catch (Exception e) {
                          // TODO Auto-generated catch block
                      }
                  }
              };
              timer.postDelayed(runnableCode, 1000);

          }



    public  int timerCount = 61;
          public void showAcceptRejectBox() {
//              timerCount = 61;
              if(timer2!=null)
              {
                  timer2.removeCallbacksAndMessages(null);
              }
              timer2 = null;
//              timerCount = 61;
              timer2 = new Handler();
              Runnable runnableCode = new Runnable() {
                  @Override
                  public void run() {
                      try {

                          if(timerCount >0 ){
                              canGetNewOrder = false;
                              flAcceptRejectDialog.setVisibility(View.VISIBLE);
                              timerCount = timerCount - 1 ;
                              lblExpiryCounter.setText("Job Expire in : " + timerCount+"s");
                          }
                          else
                          {

                              pbCircular.setVisibility(View.VISIBLE);
                              canGetOrderStatusFlag = false;
                              canGetNewOrder = true;
                              flAcceptRejectDialog.setVisibility(View.INVISIBLE);
                              timer2.removeCallbacksAndMessages(null);
                          }

                          timer2.postDelayed(this, 1000);
                      } catch (Exception e) {
                          // TODO Auto-generated catch block
                      }
                  }
              };
              timer.postDelayed(runnableCode, 1000);
          }




          public void  setOfflineUI()
          {



              btnLlOnline.setVisibility(View.INVISIBLE);
              btnImgOnline.setVisibility(View.INVISIBLE);

              btnLlOffline.setVisibility(View.VISIBLE);
              btnImgOffline.setVisibility(View.VISIBLE);

              llSwitchContainer.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_switch));

              pbCircular.setVisibility(View.GONE);
              processObjectAnimator.cancel();
              canGetNewOrder = true;
              workStatus=false;


          }
          public void  setOnlineUI()
          {

              pbCircular.setVisibility(View.VISIBLE);
              processObjectAnimator.start();
              btnLlOffline.setVisibility(View.INVISIBLE);
              btnImgOffline.setVisibility(View.INVISIBLE);
              flAcceptRejectDialog.setVisibility(View.INVISIBLE);
              btnLlOnline.setVisibility(View.VISIBLE);
              btnImgOnline.setVisibility(View.VISIBLE);
              if(timer2 != null)
              {
                  timer2.removeCallbacksAndMessages(null);
              }

              llSwitchContainer.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_swich_color));
              canGetNewOrder = true;
              workStatus=true;


          }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canGetNewOrder = false;
        canGetOrderStatusFlag = false;
        if(timer != null) {
            timer.removeCallbacksAndMessages(null);

        }timer = null;
        if(timer2 != null) {
            timer2.removeCallbacksAndMessages(null);

        }
        timer2 = null;
    }




//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if(sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0){
//            Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
//            sm.registerListener(this,s, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }


}
