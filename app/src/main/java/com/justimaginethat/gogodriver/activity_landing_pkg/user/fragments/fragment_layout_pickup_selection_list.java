package com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetNearByDriverRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetPickUpPointsRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetPickUpPointsResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.BikerMarkerViewModel;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Landing_CheckNearByDriverAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.CustomInfoWindow.CustomInfoWindow;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.adapter.PickupListAdapter;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_map_marker.OnMapAndViewReadyListener;
import com.skyfishjy.library.RippleBackground;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lion-1 on 3/4/2017.
 */

public class fragment_layout_pickup_selection_list extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    public fragment_activity_landing_user fActivity;
    public APIGetPickUpPointsRequestModel requestModel;

    List<APIGetPickUpPointsResponseModel> pickUpPointsResponseModelList = new ArrayList<>();

    public SupportMapFragment mapFragment;
    public boolean checkNearbyDriverFlag = true;

    public SupportMapFragment mapView;
    public GoogleMap mGoogleMap;
    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    public LocationRequest mLocationRequest;
    public LatLng latLng;
    public RecyclerView recyclerView;
    public FrameLayout pickerLayoutFL;
    public FrameLayout pickerLayoutConBL;
    public FrameLayout pickupList_header;
    public TextView txtSetPickupLocation;
    public FrameLayout pickupList_child;
    public FrameLayout frame_bottom;
    public EditText search_name;
    public ScrollView scrollerMap;
    public ArrayAdapter adapter;
    public ImageView btnDown;
    public ImageView imgArrow_up;

    public CustomInfoWindow customInfoWindow;
    public LayoutInflater inflater;
    public PickupPointsDAO pickupPointDAO;
    public Marker currentLocationMarker;
    public MarkerOptions currentLocationMarkerOptions;

    public fragment_layout_pickup_selection_list fragment_layout_pickup_selection_list;
    public LatLng latLngNew;


    APIGetPickUpPointsResponseModel responseModelList;
    public ArrayList<PickupPointDCM> masterList = new ArrayList<>();
    public PickupListAdapter picListAdapter;

    public ArrayList<PickupPointDCM> dataSearch = new ArrayList<PickupPointDCM>();
    ArrayList<PickupPointDCM> deliveryPickupPoint;
    public OrderDCM orderDCM;
    public OrderDAO orderDAO;

    public DriverDCM orderDriver;
    public DriverDAO driverDAO;
    public List<BikerMarkerViewModel> driversAround = new ArrayList<>();
    public PickupPointDCM orderPickupPoint;
    public PickupPointsDAO orderPickupPointDAO;
    public   Context context;
    Gson gson;

    public int deff = 0;
    public Handler timer;


    public fragment_layout_pickup_selection_list() {

    }

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO Auto-generated method stub

        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_layout_pickup_selection_list, container, false);
        fragment_layout_pickup_selection_list = this;
        recyclerView = (RecyclerView) view.findViewById(R.id.recList);
        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        mapView.getMapAsync(this);
        context = getContext();
        buildGoogleApiClient();
        gson = new Gson();
        pickupPointDAO = new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        RippleBackground aniRipple = (RippleBackground) view.findViewById(R.id.aniRipple);
        aniRipple.startRippleAnimation();
        pickerLayoutFL = (FrameLayout) view.findViewById(R.id.FM);
        pickerLayoutConBL = (FrameLayout) view.findViewById(R.id.pickerLayoutConBL);
        pickupList_header = (FrameLayout) view.findViewById(R.id.pickupList_header);
        txtSetPickupLocation = (TextView) view.findViewById(R.id.txtSetPickupLocation);
        pickupList_child = (FrameLayout) view.findViewById(R.id.pickupList_child);
        search_name = (EditText) view.findViewById(R.id.search_name);
        scrollerMap = (ScrollView) view.findViewById(R.id.scrollerMap);
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        pickerLayoutFL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                pickerLayoutFL.getWindowVisibleDisplayFrame(r);

                int screenHeight = pickerLayoutFL.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d("asdasd", "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    int totalHeight = scrollerMap.getChildAt(0).getHeight();
                    scrollerMap.setScrollY(150);
                } else {
                    scrollerMap.setScrollY(0);
                }
            }
        });


        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (search_name.getText().length() > 0) {

                    String searchTest = search_name.getText().toString().toLowerCase();

                    String masterTest = "";
                    dataSearch.clear();
                    for (int i = 0; i < masterList.size(); i++) {
                        masterTest = masterList.get(i).title.toLowerCase();
                        if (masterTest.contains(searchTest)) {
                            dataSearch.add(masterList.get(i));
                        }
                    }
                    picListAdapter.notifyDataSetChanged();

                } else {
                    dataSearch.clear();
                    for (int i = 0; i < masterList.size(); i++) {
                        dataSearch.add(masterList.get(i));
                    }

                    picListAdapter.notifyDataSetChanged();
                }
            }
        });
        btnDown = (ImageView) view.findViewById(R.id.btnDown);
        imgArrow_up = (ImageView) view.findViewById(R.id.imgArrow_up);
        pickupList_child.setVisibility(View.GONE);
        pickupList_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickupList_child.getVisibility() == View.GONE) {
                    imgArrow_up.setImageDrawable(getResources().getDrawable(R.drawable.up_arrow));


                    pickupList_child.setVisibility(View.VISIBLE);
                } else {
                    pickupList_child.setVisibility(View.GONE);
                    imgArrow_up.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));

                }
            }
        });
        pickupList_child.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickupList_child.setVisibility(View.GONE);
                imgArrow_up.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
            }
        });
        requestModel = new APIGetPickUpPointsRequestModel();
        requestModel.gcmId = sessionDCM.gcmId;
        requestModel.mobileNumber = sessionDCM.mobileNumber;
        requestModel.emailAddress = sessionDCM.emailAddress;
        requestModel.idUser = sessionDCM.idUser;


        for (PickupPointDCM item : FixLabels.sessionDatabase.deliveryPickupPointsList) {
            String delivryListStr = gson.toJson(item, PickupPointDCM.class);
            PickupPointDCM temp = new PickupPointDCM();
            temp = gson.fromJson(delivryListStr, PickupPointDCM.class);
            masterList.add(temp);
            dataSearch.add(temp);
        }
        picListAdapter = new PickupListAdapter(fragment_layout_pickup_selection_list.getContext(), fragment_layout_pickup_selection_list);
        recyclerView.setAdapter(picListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fActivity.context));
        return view;
    }

    public void callAsynchronousTask() {
        timer = new Handler();
        Runnable runnableCode = new Runnable() {
            public APIGetNearByDriverRequestModel requestModelNearbyD;
            @Override
            public void run() {
                try {
                    if (checkNearbyDriverFlag == true) {
                        fActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                new Landing_CheckNearByDriverAsync(fragment_layout_pickup_selection_list).execute();
                            }
                        });


                    }
                    timer.postDelayed(this,1000);
                } catch (Exception e) {
                    Log.e("Gogo",e.toString());
                }
            }
        };
        timer.postDelayed(runnableCode,1000);

    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            return;
        }
        getCurrentLocation();
        callAsynchronousTask();


    }

    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        try
        {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {

                LatLng latLng = new LatLng( mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        checkNearbyDriverFlag = false;
        if(timer!=null) {
            timer.removeCallbacksAndMessages(null);
            timer = null;
        }
    }
}