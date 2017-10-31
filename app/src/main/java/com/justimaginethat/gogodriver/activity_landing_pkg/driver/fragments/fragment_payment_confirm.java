package com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.GetDriverOrderStatus;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_map_marker.OnMapAndViewReadyListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lion-1 on 3/6/2017.
 */

public class fragment_payment_confirm extends Fragment  implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener  {

    public fragment_activity_landing_driver fActivity;
    public FrameLayout frame_bottom_down_one;
    public ImageView action_image_up;
    public ImageView action_image_down;
    public FrameLayout frame_sub2;


    public PolylineOptions lineOptions = null;

    public SupportMapFragment mapView;
    public GoogleMap mGoogleMap;
    public GoogleApiClient mGoogleApiClient;
    public LatLng latLng;
    public FrameLayout pickerLayoutFL;
    public FrameLayout pickerLayoutConBL;
    public LayoutInflater inflater;
    public String duration="0 min";
    public String distance = "";
    public LatLng position;
    public Handler timer;
    public ProgressDialog progressDialog;
//    public LinearLayout cancel_action;//sp 9-5-2017
    public ImageView cancel_action;
    public PickupPointsDAO pickupPointDAO;
    public PickupPointDCM pickupPointDetails=new PickupPointDCM();
    public List<UserDCM> userDCMList =new ArrayList<>();
    public UserDCM userDCM =new UserDCM();
    public UserDAO userDAO;

    public Double toLat;
    public Double toLon;
    public Double fromLat;
    public Double fromLon;
    public View toMarkerView;
    public ImageView fromMarkerView;
    public LatLng toLatLng;
    public LatLng fromLatLng;

    public Marker markerTo;
    public MarkerOptions markerOptionsTo;
    public Marker markerFrom;
    public MarkerOptions markerOptionsFrom;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    public TextView userName;
    public CircleImageView imgUser;
    public TextView userRating;
//    public LinearLayout call_action;//sp 9-5-2017
    public ImageView call_action;

    public BitmapDescriptor iconMarker;
    public String url;
    public fragment_payment_confirm FLPayment;

    public boolean canGetOrderStatusFlag =true;
    public Context context;
    private Location mLastLocation;

    public fragment_payment_confirm(){
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
           View view = inflater.inflate(R.layout.fragment_payment_confirm, container, false);

        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapView.getMapAsync(this);
        this.inflater = inflater;
        FLPayment =this;
context = getContext();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fActivity = (fragment_activity_landing_driver) getActivity();
        userDAO= new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            userDCM = userDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pickupPointDAO= new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            pickupPointDetails = pickupPointDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_bike_gogo);

//        fromLat =Double.parseDouble(userDCM.defaultLatitude);
//        fromLon =Double.parseDouble(userDCM.defaultLongitude);
        toLat =Double.parseDouble(pickupPointDetails.pickuplatitude);
        toLon =Double.parseDouble(pickupPointDetails.pickupLongitude);
//        fromLatLng = new LatLng(fromLat, fromLon);
        toLatLng = new LatLng(toLat, toLon);
        ProgressBar pbCircular = (ProgressBar) view.findViewById(R.id.pbCircular);
        frame_bottom_down_one=(FrameLayout)view.findViewById(R.id.frame_bottom_down_one);
//        cancel_action=(LinearLayout)view.findViewById(R.id.cancel_action);sp 9-5-2017
        cancel_action=(ImageView)view.findViewById(R.id.cancel_action);


        userName =(TextView)view.findViewById(R.id.userName);
        imgUser =(CircleImageView)view.findViewById(R.id.imgUser);
//        imgUser=(ImageView)view.findViewById(R.id.imgUser);
        userRating =(TextView)view.findViewById(R.id.userRating);

        //        call_action = (LinearLayout) view.findViewById(R.id.call_action);//sp 9-5-2017
        call_action = (ImageView) view.findViewById(R.id.call_action);//sp 9-5-2017
        action_image_up=(ImageView)view.findViewById(R.id.action_image_up);
        action_image_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_bottom_down_one.setVisibility(View.VISIBLE);
                action_image_up.setImageResource(R.drawable.icon_marker);

            }
        });
        action_image_down=(ImageView)view.findViewById(R.id.action_image_down);
        action_image_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame_bottom_down_one.setVisibility(View.INVISIBLE);
                action_image_up.setImageResource(R.drawable.icon_up_arrow);
            }
        });

        ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(pbCircular,
                "rotation", 0f, 360f);
        processObjectAnimator.setDuration(1000);
        processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        processObjectAnimator.setInterpolator(new LinearInterpolator());
        processObjectAnimator.start();



        pickupPointDAO= new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            pickupPointDetails = pickupPointDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            userDCMList = userDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (UserDCM userDCMs : userDCMList
                ) {
            String newProfilePicture="";




            if(userDCMs.profilePicture==null || userDCMs.profilePicture.equals("")){
                imgUser.setImageResource(R.drawable.icon_profile_placeholder);
            }else {
                if (!userDCMs.profilePicture.contains("http:") && userDCMs.profilePicture != null && userDCMs.profilePicture.length() > 0) {
                    newProfilePicture = FixLabels.Server + userDCMs.profilePicture;
                    Glide.with(this).load(newProfilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgUser);
                } else {
                    Glide.with(this).load(userDCMs.profilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgUser);
                }

            }





//            if(!userDCMs.profilePicture.contains("http:")){
//                newProfilePicture= FixLabels.Server+ userDCMs.profilePicture;
//                Glide.with(this).load(newProfilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgUser);
//            }else {
//                Glide.with(this).load(userDCMs.profilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgUser);
//            }
            userName.setText(userDCMs.firstName);
            if(userDCMs.syncGUID==null||userDCMs.syncGUID==""||userDCMs.syncGUID.equals("")||userDCMs.syncGUID.equals(null)){
                userRating.setText("5"+"/5");
            }else {
                userRating.setText(userDCMs.syncGUID + "/5");
            }
//            userRating.setText(userDCMs.syncGUID + "/5");//sp 9-5-2017
        }

        call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setCancelable(false)
                        .setTitle(userDCMList.get(0).mobileNumber)
                        .setMessage("Are you sure Make call?")
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                    MakeCall(userDCMList.get(0).mobileNumber);
                            }
                        })
                        .setIcon(android.R.drawable.sym_call_outgoing)
                        .show();



            }
        });
        canGetOrderStatusFlag = true;

//        setMarkerAndCamera();
        return view;
    }
    public void MakeCall(String callNumber){




        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(("tel:" + callNumber)));

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }






    public void getCurrentLocation() {
        mGoogleMap.clear();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            fromLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            int padding = 100;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(fromLatLng);
            builder.include(toLatLng);
            LatLngBounds bounds = builder.build();

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(18));

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
        markerOptionsTo = new MarkerOptions()
                .position(toLatLng)
                .visible(true)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_bike_gogo", 100, 100)));

        markerTo = mGoogleMap.addMarker(markerOptionsTo);


        callAsynchronousTask();
    }

    @Override
    public void onConnectionSuspended(int i) {

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLastLocation != null) {
            mLastLocation = location;
            fromLatLng =new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {


        mGoogleMap = gMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            return;
        }

        buildGoogleApiClient();



    }




    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getContext().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }




    public int toWidth = 0;
    public int fromWidth = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setMarkerAndCamera() {

        if (markerTo == null) {
            markerOptionsTo = new MarkerOptions();
            markerOptionsTo.position(toLatLng);
            markerOptionsTo.icon(iconMarker);
            markerOptionsTo.visible(true);
            markerTo = mGoogleMap.addMarker(markerOptionsTo);
        }
        if (toMarkerView == null) {
            View viewmap = mapView.getView(); // returns base view of the fragment
            ViewGroup viewGroup = (ViewGroup) viewmap;
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            toMarkerView = inflater.inflate(R.layout.custom_info_window_user_location, viewGroup, false);
            TextView titleStart = (TextView) toMarkerView.findViewById(R.id.title);
            titleStart.setText("Confirm Payment.");

            toMarkerView.setZ((float) 5);
            toMarkerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    toWidth = i2;
                    toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
                    toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));
                }
            });
            ((ViewGroup) viewmap).addView(toMarkerView);
            int padding = 200;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(toLatLng);
            LatLngBounds bounds = builder.build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        }
        toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
        toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));
        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
                toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));
            }
        });
    }



    public void callAsynchronousTask() {
        timer = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                try {
                    if(canGetOrderStatusFlag == true) {

                        new GetDriverOrderStatus(FLPayment).execute();
                    }

                    timer.postDelayed(this, 1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        };
        timer.postDelayed(runnableCode, 1000);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canGetOrderStatusFlag =  false;
        if(timer != null) {
            timer.removeCallbacksAndMessages(null);

        }
        timer = null;
    }
}





