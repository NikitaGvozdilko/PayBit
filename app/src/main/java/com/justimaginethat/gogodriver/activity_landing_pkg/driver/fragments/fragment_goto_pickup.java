package com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.CameraPosition;
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
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.DriverCancelOrderAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.ToPickup_SetOrderStatusAsymc;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.ToPickup_CustomerHeadingToPickupPathCoordinateAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask.ToPickup_SetOrderStatusRepeateAsymc;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_map_marker.OnMapAndViewReadyListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lion-1 on 3/9/2017.
 */

public class fragment_goto_pickup  extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener{


    public Context context;
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
    public ImageView btnCancel;
    public PickupPointsDAO pickupPointDAO;
    public PickupPointDCM pickupPointDetails=new PickupPointDCM();
    public List<UserDCM> userDCMList=new ArrayList<>();
    public UserDCM userDCM=new UserDCM();
    public UserDAO userDAO;

    Double toLat;
    Double toLon;
    Double fromLat;
    Double fromLon;
    public View toMarkerView;
    public ImageView fromMarkerView;
    public LatLng toLatLng;
    public LatLng fromLatLng;

    public Marker markerTo;
    public MarkerOptions markerOptionsTo;
    public Marker markerFrom;
    public MarkerOptions markerOptionsFrom;


    public TextView userName;
    public CircleImageView imgUser;
    public TextView userRating;
//  public LinearLayout call_action;//sp 9-5-2017
    public ImageView call_action;
    public ImageView btnGogMap;

    public TextView lblPickupAddress;
    public LinearLayout arriveAtPickup;


    public BitmapDescriptor iconMarker;
    public String url;
    public fragment_goto_pickup DriverHeadingToPickup;

    public boolean canGetOrderStatusFlag =true;

    public String OrderStatus="";

    public List<PickupPointDCM> pickupDCMList=new ArrayList<>();
    public Location mLastLocation;
    public float heading;
    public LocationRequest mLocationRequest;
    public long UPDATE_INTERVAL = 1 * 1000;  /* 10 secs */
    public long FASTEST_INTERVAL = 2000; /* 2 sec */
    public String addressDetail;

    public fragment_goto_pickup() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goto_pickup, container, false);
        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapView.getMapAsync(this);
        this.inflater = inflater;
        context = getContext();
        DriverHeadingToPickup =this;
        OrderStatus=FixLabels.OrderStatus.DriverHeadingToPickUpPoint;
        canSetOrderStatus = true;
        userDAO = new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            userDCM = userDAO.getAll().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }


        pickupPointDAO = new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            pickupPointDetails = pickupPointDAO.getAll().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userDCMList = userDAO.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }


        iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_bike_gogo);
        btnGogMap=(ImageView)view.findViewById(R.id.btnGogMap);
        toLat =Double.parseDouble(pickupPointDetails.pickuplatitude);
        toLon =Double.parseDouble(pickupPointDetails.pickupLongitude);
        toLatLng = new LatLng(toLat, toLon);
        frame_bottom_down_one=(FrameLayout)view.findViewById(R.id.frame_bottom_down_one);
        btnCancel=(ImageView)view.findViewById(R.id.btnCancel);
        userName =(TextView)view.findViewById(R.id.userName);
        imgUser =(CircleImageView)view.findViewById(R.id.imgUser);
//      imgUser=(ImageView)view.findViewById(R.id.imgUser);
        userRating =(TextView)view.findViewById(R.id.userRating);
//      call_action = (LinearLayout) view.findViewById(R.id.call_action);//sp 9-5-2017
        call_action = (ImageView) view.findViewById(R.id.call_action);//sp 9-5-2017
        lblPickupAddress=(TextView)view.findViewById(R.id.lblPickupAddress);
        arriveAtPickup=(LinearLayout)view.findViewById(R.id.arriveAtPickup);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DriverHeadingToPickup.getContext());
                alertDialogBuilder.setMessage("Are you sure you want to cancel your order?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                                canSetOrderStatus = false;
                                if(timer != null) {
                                    timer.removeCallbacksAndMessages(null);

                                }  timer = null;


                                new DriverCancelOrderAsync(DriverHeadingToPickup).execute();

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
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

        pickupPointDAO= new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            pickupPointDetails = pickupPointDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            userDCMList= userDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (UserDCM userDCMs :userDCMList
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
            if(userDCMs.syncGUID == null)
            {
                userDCMs.syncGUID = "0";
            }


            if(userDCMs.syncGUID==null||userDCMs.syncGUID==""||userDCMs.syncGUID.equals("")||userDCMs.syncGUID.equals(null)){
                userRating.setText("5"+"/5");
            }else {
                userRating.setText(userDCMs.syncGUID + "/5");
            }



//            userRating.setText(userDCMs.syncGUID+"/5");//sp 9-5-2017
        }
        String city="";
        try {
            city=pickupPointDetails.pickupAddress.split(",")[1];
        } catch (Exception e) {
            city=pickupPointDetails.pickupAddress;
            e.printStackTrace();
        }

        lblPickupAddress.setText(pickupPointDetails.title+" "+city);

//        lblPickupAddress.setText(pickupPointDetails.title+" , "+pickupPointDetails.pickupAddress);
        btnGogMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent( Intent.ACTION_VIEW,
                        Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                                "&saddr=" + fromLatLng.latitude + "," + fromLatLng.longitude + "&daddr=" + pickupPointDetails.pickuplatitude + "," + pickupPointDetails.pickupLongitude + "&hl=zh&t=m&dirflg=d"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK&Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

            }
        });
        call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle(userDCMList.get(0).mobileNumber)
                        .setMessage("Are you sure Make call?")
                        .setCancelable(false)
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

        arriveAtPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you have arrive at pickup point ?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                canSetOrderStatus = false;




                                OrderStatus=FixLabels.OrderStatus.DriverArrivedAtPickUpPoint;
                                new ToPickup_SetOrderStatusAsymc(DriverHeadingToPickup).execute();


                            }
                        })
                        .show();







            }
        });



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






    public void showMap(String PLACE_LONGITUDE,String PLACE_LATITUDE) {

    }




    public boolean canSetOrderStatus=false;
    public void callAsynchronousTask() {


        timer = new Handler();
        Runnable runnableCode = new Runnable() {


            @Override
            public void run() {
                try {

                    if(canSetOrderStatus ==true){
                        fActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("GoGoDriver","Called");
                                new ToPickup_SetOrderStatusRepeateAsymc(DriverHeadingToPickup).execute();
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


    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(DriverHeadingToPickup.getContext(), Locale.getDefault());
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setSmallestDisplacement(1)
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
        canSetOrderStatus = true;
        callAsynchronousTask();


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (mLastLocation != null) {
            heading = mLastLocation.bearingTo(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));

        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {

                fromLatLng = new LatLng( mLastLocation.getLatitude(), mLastLocation.getLongitude());
                addressDetail = getCompleteAddressString(fromLatLng.latitude, fromLatLng.longitude);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(fromLatLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                setMarkerAndCamera();

                String parameters = "origin=" + fromLatLng.latitude + "," + fromLatLng.longitude;
                parameters = parameters + "&destination=" + toLatLng.latitude + "," + toLatLng.longitude;
                parameters = parameters + "&sensor=false";
                parameters = parameters + "&key=" + FixLabels.map;
                url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
                ToPickup_CustomerHeadingToPickupPathCoordinateAsync fromServer = new ToPickup_CustomerHeadingToPickupPathCoordinateAsync(DriverHeadingToPickup, url);
                fromServer.execute(url);

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
        if (mLastLocation != null) {
//                double bearing = mLastLocation.bearingTo(location);
////                heading = mLastLocation.getBearing();
//                heading = (360 + ((bearing + 360) % 360) - heading) % 360;
            fromLatLng = new LatLng( mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                lblbearing.setText(heading + "");
        }
//            mLastLocation = location;

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
    /** A method to download json data from url */
    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        }catch(Exception e){
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public int toWidth = 0;
    public int fromWidth = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setMarkerAndCamera() {

        if (markerTo == null) {
            markerOptionsTo = new MarkerOptions();
            markerOptionsTo.position(toLatLng);
            markerOptionsTo.visible(false);
            markerTo = mGoogleMap.addMarker(markerOptionsTo);

        }

        if (markerFrom == null) {
            markerOptionsFrom = new MarkerOptions();
            markerOptionsFrom.position(fromLatLng);
            markerOptionsFrom.icon(iconMarker);
            markerOptionsFrom.title(fromLatLng.toString());
            markerOptionsFrom.visible(false);
            markerFrom = mGoogleMap.addMarker(markerOptionsFrom);
        }


        if (toMarkerView == null && fromMarkerView == null) {
            View viewmap = mapView.getView(); // returns base view of the fragment
            ViewGroup viewGroup = (ViewGroup) viewmap;
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toMarkerView = inflater.inflate(R.layout.custom_info_window_circel, viewGroup, false);
            TextView titleStart = (TextView) toMarkerView.findViewById(R.id.title);
            TextView tvDuration = (TextView) toMarkerView.findViewById(R.id.tvDuration);
            ProgressBar pb = (ProgressBar) toMarkerView.findViewById(R.id.progressbar);
            toMarkerView.setZ((float) 5);

            ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(pb,
                    "rotation", 0f, 360f);
            processObjectAnimator.setDuration(1000);
            processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            processObjectAnimator.setInterpolator(new LinearInterpolator());
            processObjectAnimator.start();



            toMarkerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    toWidth = i2;

                    toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
                    toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));
                }
            });

            ((ViewGroup) viewmap).addView(toMarkerView);








            viewmap = mapView.getView(); // returns base view of the fragment
            viewGroup = (ViewGroup) viewmap;
            fromMarkerView = new ImageView(getContext());
            fromMarkerView.setImageResource(R.drawable.icon_bike_gogo);
            viewGroup.addView(fromMarkerView);
            CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
            float zoomLevel  = cameraPosition.zoom;
            int newSize = (int)((zoomLevel * 70) / 14);
//            if( userDCM.heading==null||userDCM.heading=="")
//            {
//                userDCM.heading  = "0";
//            }
//            fromMarkerView.animate().rotation(Float.parseFloat(userDCM.heading)).setDuration(500);
            fromMarkerView.setLayoutParams(new FrameLayout.LayoutParams(newSize,newSize));
            fromMarkerView.animate().x((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (newSize/2)).y((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - newSize).setDuration(500);
            fromMarkerView.setZ((float)5);

            fromMarkerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    fromWidth = i2;
                    fromMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (fromWidth / 2));
                    fromMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - (fromMarkerView.getLayoutParams().height));

                }
            });


            int padding = 200;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(toLatLng);
            builder.include(fromLatLng);
            LatLngBounds bounds = builder.build();

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));



        }

        CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
        float zoomLevel  = cameraPosition.zoom;
        int newSize = (int)((zoomLevel * 70) / 14);
//        fromMarkerView.animate().rotation(Float.parseFloat(userDCM.heading)).setDuration(500);
        fromMarkerView.setLayoutParams(new FrameLayout.LayoutParams(newSize,newSize));
        fromMarkerView.animate().x((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (newSize/2)).y((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - newSize).setDuration(500);


        toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
        toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));


        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {



                toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
                toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));

                CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
                float zoomLevel  = cameraPosition.zoom;
                int newSize = (int)((zoomLevel * 70) / 14);
//                fromMarkerView.animate().rotation(Float.parseFloat(userDCM.heading)).setDuration(500);
                fromMarkerView.setLayoutParams(new FrameLayout.LayoutParams(newSize,newSize));
                fromMarkerView.animate().x((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (newSize/2)).y((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - newSize).setDuration(500);

            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canSetOrderStatus = false;
        if(timer != null) {
            timer.removeCallbacksAndMessages(null);

        }  timer = null;
    }
}