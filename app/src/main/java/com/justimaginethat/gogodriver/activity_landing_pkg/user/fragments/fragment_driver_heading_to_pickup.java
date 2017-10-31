package com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.ToPickup_CancelOrderAfterAcceptAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.ToPickup_DriverHeadingToPickupPathCoordinateAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.ToPickup_GetOrderStatusAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.CustomInfoWindow.CustomInfoWindowCircle;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lion-1 on 3/6/2017.
 */

public class fragment_driver_heading_to_pickup extends Fragment  implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener  {

    public fragment_activity_landing_user fActivity;
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
    public List<DriverDCM> driverDCMList=new ArrayList<>();
    public DriverDCM driverDCM=new DriverDCM();
    public DriverDAO driverDAO;

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


    public TextView driverName;
    public CircleImageView imgDriver;
    public TextView driverRating;
    public ImageView call_action;
//    public LinearLayout call_action;//sp 9-5-2017


    public BitmapDescriptor iconMarker;
    public String url;
    public fragment_driver_heading_to_pickup fragment_driver_heading_to_pickup;

    public boolean canGetOrderStatusFlag =true;
    public String orederStatus;

    public boolean cancelClicked = false;
    public Context context;

    public fragment_driver_heading_to_pickup(){
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
           View view = inflater.inflate(R.layout.fragment_driver_handel_to_pickup, container, false);
        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapView.getMapAsync(this);
        this.inflater = inflater;
        fragment_driver_heading_to_pickup =this;
        context =getContext();
        driverDAO= new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            driverDCM = driverDAO.getAll().get(0);
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

        fromLat =Double.parseDouble(driverDCM.latitude2);
        fromLon =Double.parseDouble(driverDCM.longitude2);
        toLat =Double.parseDouble(pickupPointDetails.pickuplatitude);
        toLon =Double.parseDouble(pickupPointDetails.pickupLongitude);
        fromLatLng = new LatLng(fromLat, fromLon);
        toLatLng = new LatLng(toLat, toLon);

        frame_bottom_down_one=(FrameLayout)view.findViewById(R.id.frame_bottom_down_one);

        //        cancel_action=(LinearLayout)view.findViewById(R.id.cancel_action);sp 9-5-2017
        cancel_action=(ImageView)view.findViewById(R.id.cancel_action);



        driverName=(TextView)view.findViewById(R.id.driverName);
        imgDriver=(CircleImageView)view.findViewById(R.id.imgDriver);
//        imgUser=(ImageView)view.findViewById(R.id.imgUser);
        driverRating=(TextView)view.findViewById(R.id.driverRating);

        //        call_action = (LinearLayout) view.findViewById(R.id.call_action);//sp 9-5-2017
        call_action = (ImageView) view.findViewById(R.id.call_action);//sp 9-5-2017




        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FixLabels.OrderStatus.DriverArrivedAtPickUpPoint.equals(orederStatus))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment_driver_heading_to_pickup.getContext());
                    alertDialogBuilder.setMessage("If you cancel this order you will be charged a $2 cancellation fee.");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    cancelClicked = true ;

                                    canGetOrderStatusFlag =false;
                                    if(timer != null)
                                    {
                                        timer.removeCallbacksAndMessages(null);
                                    }
                                    timer = null;
                                    new ToPickup_CancelOrderAfterAcceptAsync(fragment_driver_heading_to_pickup).execute();
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
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment_driver_heading_to_pickup.getContext());
                    alertDialogBuilder.setMessage("Are you sure you want to cancel your order?");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    cancelClicked = true ;
                                    canGetOrderStatusFlag =false;
                                    new ToPickup_CancelOrderAfterAcceptAsync(fragment_driver_heading_to_pickup).execute();
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
            driverDCMList=driverDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (DriverDCM driverDCMs:driverDCMList
                ) {
            String newProfilePicture="";



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






//            if(!driverDCMs.profilePicture.contains("http:")){
//                newProfilePicture= FixLabels.Server+ driverDCMs.profilePicture;
//                Glide.with(this).load(newProfilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgDriver);
//            }else {
//                Glide.with(this).load(driverDCMs.profilePicture).placeholder(R.drawable.icon_profile_placeholder).error(R.drawable.icon_profile_placeholder).into(imgDriver);
//            }
            driverName.setText(driverDCMs.firstName);


            if(driverDCMs.syncGUID==null||driverDCMs.syncGUID==""||driverDCMs.syncGUID.equals("")||driverDCMs.syncGUID.equals(null)){
                driverRating.setText("5"+"/5");
            }else {
                driverRating.setText(driverDCMs.syncGUID + "/5");
            }



//            driverRating.setText(driverDCMs.syncGUID+"/5");//sp 9-5-2017
        }

        call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle(driverDCMList.get(0).mobileNumber)
                        .setMessage("Are you sure Make call?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                    MakeCall(driverDCMList.get(0).mobileNumber);
                            }
                        })
                        .setIcon(android.R.drawable.sym_call_outgoing)
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








    @Override
    public void onConnected(@Nullable Bundle bundle) {
       getCurrentLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {  BitmapDescriptor iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_bike);
    }


    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            return;
        }


        setMarkerAndCamera();
        String parameters = "origin=" + fromLatLng.latitude + "," + fromLatLng.longitude;
        parameters = parameters + "&destination=" + toLatLng.latitude + "," + toLatLng.longitude;
        parameters = parameters + "&sensor=false";
        parameters = parameters + "&key=" + FixLabels.map;
        url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        ToPickup_DriverHeadingToPickupPathCoordinateAsync fromServer=new ToPickup_DriverHeadingToPickupPathCoordinateAsync(fragment_driver_heading_to_pickup,url);
        fromServer.execute(url);
        callAsynchronousTask();








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



    public void getCurrentLocation() {
        mGoogleMap.clear();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            LatLng latLng = new LatLng(toLat, toLon);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
//            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }
    public void callAsynchronousTask() {

        timer = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                try {
                    if(canGetOrderStatusFlag==true) {
                        fragment_driver_heading_to_pickup.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("Called","Caall");
                                new ToPickup_GetOrderStatusAsync(fragment_driver_heading_to_pickup).execute();
                            }
                        });

                    }
                    timer.postDelayed(this,1000);
                } catch (Exception e) {
                   Log.e("Error Handler", String.valueOf(e));
                }
            }
        };
        timer.postDelayed(runnableCode,1000);

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
//            fromMarkerView.animate().rotation(Float.parseFloat(driverDCM.heading)).setDuration(500);
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
//        fromMarkerView.animate().rotation(Float.parseFloat(driverDCM.heading)).setDuration(500);
        fromMarkerView.setLayoutParams(new FrameLayout.LayoutParams(newSize,newSize));
        fromMarkerView.animate().x((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (newSize/2)).y((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - newSize).setDuration(500);


        toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
        toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));


        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

//Avoid vibration of marker on mover


                toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
                toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));

                CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
                float zoomLevel  = cameraPosition.zoom;
                int newSize = (int)((zoomLevel * 70) / 14);
//                fromMarkerView.animate().rotation(Float.parseFloat(driverDCM.heading)).setDuration(500);
                fromMarkerView.setLayoutParams(new FrameLayout.LayoutParams(newSize,newSize));
                fromMarkerView.animate().x((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (newSize/2)).y((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - newSize).setDuration(500);

            }
        });



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canGetOrderStatusFlag =false;
        if(timer!=null) {
            timer.removeCallbacksAndMessages(null);
            timer = null;
        }
    }
}
