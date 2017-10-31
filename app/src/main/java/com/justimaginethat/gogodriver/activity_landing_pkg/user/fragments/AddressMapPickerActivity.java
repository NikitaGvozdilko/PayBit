package com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.Marker;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.activity_default_address_setter;

import java.util.List;
import java.util.Locale;


/**
 * Created by Lion-1 on 3/17/2017.
 */


public class AddressMapPickerActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        LocationListener {

    public static final String TAG = "AddressMapPickerActivity";
    public GoogleMap mMap;
    public double longitude;
    public double latitude;
    public GoogleApiClient googleApiClient;
    public TextView txtAddress;

    Context context;
    public View mCustomView;
    public ImageView imgBack;
    public ImageView imgBackRight;
    public String messageAddress;

    public String latitude1;
    public String longitude1;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_user_address);

context = this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_address);
        mCustomView = getSupportActionBar().getCustomView();


        imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
        imgBackRight = (ImageView) mCustomView.findViewById(R.id.imgBackRight);


        txtAddress = (TextView) findViewById(R.id.txtAddress);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passdataactivity();
                finish();
            }
        });

        int locationMode = 0;
        boolean mode = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(),Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            mode = (locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY ); //check location mode


        }else{
            String locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                mode =  !TextUtils.isEmpty(locationProviders);
        }
    if(mode == false) {
        new android.app.AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setCancelable(false)
                .setTitle(FixLabels.alertDefaultTitle)
                .setMessage("Change GPS To High Accuracy.")
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 105);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {

                            }
                        }).show();

    }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);




        // Add a marker in Sydney and move the camera
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                longitude = mMap.getCameraPosition().target.longitude;
                latitude = mMap.getCameraPosition().target.latitude;
                getAddressMap();
            }

        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

/*

        LatLng india = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(india).title("Marker in India"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

*/



    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

         LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                mLocationRequest,this);
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            getAddressMap();
            //moving the map to location
            moveMap();
        }
    }

    private void moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        mMap.clear();
        LatLng latLng = new LatLng(latitude, longitude);
      /*  mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Marker in India"));*/

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.getUiSettings().setZoomControlsEnabled(true);



    }

    @Override
    public void onClick(View view) {

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




    public void onMarkerDragEnd(Marker marker) {
        // getting the Co-ordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        getAddressMap();
        //move to current position
        moveMap();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }




    void getAddressMap(){


        try {
            Geocoder geo = new Geocoder(AddressMapPickerActivity.this.getApplicationContext(), Locale.getDefault());


            latitude1=String.valueOf(latitude);
            longitude1=String.valueOf(longitude);
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                txtAddress.setText("Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {



                    String str = "";
                    if(addresses.get(0).getAddressLine(1)!= null)
                    {
                        if(str.length() > 0)
                        {
                            str += ",";
                        }
                        str += addresses.get(0).getAddressLine(1);
                    }

                    if(addresses.get(0).getAddressLine(2)!= null)
                    {
                        if(str.length() > 0)
                        {
                            str += ",";
                        }
                        str += addresses.get(0).getAddressLine(2);
                    }

                    if(addresses.get(0).getAddressLine(3)!= null)
                    {
                        if(str.length() > 0)
                        {
                            str += ",";
                        }
                        str += addresses.get(0).getAddressLine(3);
                    }

                    if(addresses.get(0).getFeatureName()!= null)
                    {
                        if(str.length() > 0)
                        {
                            str += ",";
                        }
                        str += addresses.get(0).getFeatureName();
                    }

                    if(addresses.get(0).getLocality()!= null)
                    {
                        if(str.length() > 0)
                        {
                            str += ",";
                        }
                        str += addresses.get(0).getLocality();
                    }

                    if(addresses.get(0).getAdminArea()!= null)
                    {
                        if(str.length() > 0)
                        {
                            str += ",";
                        }
                        str += addresses.get(0).getAdminArea();
                    }
                    if(addresses.get(0).getCountryName()!= null)
                    {
                        if(str.length() > 0)
                        {
                            str += ",";
                        }
                        str += addresses.get(0).getCountryName();
                    }

                    txtAddress.setText(str);
                    messageAddress=txtAddress.getText().toString();
                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
       /* CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(13.05241, 80.25082)).zoom(2)
                .build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(13.05241, 80.25082)).title(
                "RAT");
        marker.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_bike));
        googleMap.addMarker(marker);*/

    }



    public void passdataactivity()
    {
        Intent intent = new Intent(this, activity_default_address_setter.class);
        intent.putExtra("messageAddress", messageAddress);
        intent.putExtra("latitude1", latitude1);
        intent.putExtra("longitude1", longitude1);
        setResult(RESULT_OK, intent);
    }


    @Override
    public void onLocationChanged(Location location) {

    }
}