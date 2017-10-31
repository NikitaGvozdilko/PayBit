package com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.CustomerToDriverRatingAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.CustomInfoWindow.CustomInfoWindow;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_map_marker.OnMapAndViewReadyListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lion-1 on 3/7/2017.
 */

public class fragment_user_rating extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {


    private SupportMapFragment mapView;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LatLng latLng;
    private RecyclerView recyclerView;
    public FrameLayout pickerLayoutFL;
    public FrameLayout pickerLayoutConBL;
    public FrameLayout pickupList_header;
    public TextView txtSetPickupLocation;
    public FrameLayout pickupList_child;
    public AutoCompleteTextView search_name;
    public ImageView btnDown;
    public ImageView imgArrow_up;
    public Context context;
    public CustomInfoWindow customInfoWindow;
    public LayoutInflater inflater;

    public Marker currentLocationMarker;
    public MarkerOptions currentLocationMarkerOptions;
    RelativeLayout rate;
    RatingBar ratingBar;
    public static fragment_activity_landing_user fActivity;
    private PickupPointsDAO pickupPointDAO;
    private PickupPointDCM pickupPointDetails;
    public double rattingPoints = 0.0;


    Double toLat;
    Double toLon;
    public View toMarkerView;
    public LatLng toLatLng;
    public TextView driverName;

    public Marker markerTo;
    public MarkerOptions markerOptionsTo;
    public fragment_user_rating fragment_user_rating;

    public DriverDAO driverDAO;
   public List<DriverDCM> driverDCMs = new ArrayList<>();

   public SessionDAO sessionDAO;
    public List<SessionDCM> sessionDCMs = new ArrayList<>();


    public fragment_user_rating() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_completed, container, false);
        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapView.getMapAsync(this);

        this.inflater = inflater;
        this.fragment_user_rating = this;
        context = getContext();

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        driverName=(TextView)view.findViewById(R.id.driverName);


        driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            driverDCMs = driverDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (DriverDCM driverDCM : driverDCMs
                ) {
            driverName.setText(driverDCM.firstName);
        }



        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCMs = sessionDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (SessionDCM sessionDCM : sessionDCMs
                ) {
            toLat = Double.parseDouble(sessionDCM.defaultLatitude);
            toLon = Double.parseDouble(sessionDCM.defaultLongitude);
            toLatLng = new LatLng(toLat, toLon);
        }





        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int) starsf + 1;
                    ratingBar.setRating(stars);
                    rattingPoints = (double) stars;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setMessage("Are you sure you want to continue with ratings?")
                            .setCancelable(false)
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new CustomerToDriverRatingAsync(fragment_user_rating).execute();
                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }
        });


//
//        userDAO= new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
//        try {
//            userDCM = userDAO.getAll().get(0);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


        return view;


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
        }
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
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        mGoogleMap = gMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            return;
        }

        setMarkerAndCamera();


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
        if (toMarkerView == null) {
            View viewmap = mapView.getView(); // returns base view of the fragment
            ViewGroup viewGroup = (ViewGroup) viewmap;
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toMarkerView = inflater.inflate(R.layout.custom_info_window_user_location, viewGroup, false);
            TextView titleStart = (TextView) toMarkerView.findViewById(R.id.title);
            titleStart.setText("Order Complete");
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


}


