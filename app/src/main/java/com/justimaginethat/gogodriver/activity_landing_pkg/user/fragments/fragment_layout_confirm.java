package com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
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
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APICreateNewOrderRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APICreateNewOrderResponseModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetNearByDriverRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetNearByDriverResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
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
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_CancelOrderAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_CheckNearByDriverAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_CreatedActiveOrderAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_GetOrderStatusAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_GetPendingOrderAmountAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_GetPendingOrderAmountRepeatAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.Confirm_PickupToCustomerLocationPathCoordinatesAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask.ToPickup_DriverHeadingToPickupPathCoordinateAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.CustomInfoWindow.CustomInfoWindow;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.adapter.PickupListAdapter;
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

import static android.app.Activity.RESULT_OK;


/**
 * Created by Lion-1 on 3/6/2017.
 */

public class fragment_layout_confirm extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    public FrameLayout custom_info_windowMain;
    public FrameLayout custom_info_windowArrow;
    public TextView lblDeliveryRate;


    public RelativeLayout rlBtnConfirm;
    public FrameLayout flConfirm;




    public RelativeLayout flLoadingWeb;
    public ImageView flBackgroundblackWeb;
    public ProgressBar pbCircularWeb;
    public TextView connectDriverWeb;


    public RelativeLayout flLoading;
    public FrameLayout pickupList_header;
    public TextView txtSetPickupLocation;
    public FrameLayout flMap;
    public fragment_activity_landing_user fActivity;
    public LatLng latLng;
    public Location mLastLocation;
    private LocationRequest mLocationRequest;
    public CustomInfoWindow customInfoWindow;
    private GoogleApiClient mGoogleApiClient;

    public SupportMapFragment mapView;
    public GoogleMap mGoogleMap;
    public LayoutInflater inflater;
    public fragment_layout_confirm fragment_layout_confirm;
    public List<BikerMarkerViewModel> driversAround = new ArrayList<>();


    public MarkerOptions driverPosition;
    public Marker driverPositionMarker;
    public Marker markerTo;
    public MarkerOptions markerOptionsTo;
    public Marker markerFrom;
    public MarkerOptions markerOptionsFrom;
    public PickupListAdapter picListAdapter;
    public PolylineOptions lineOptions = null;

    public double longitude;
    public double latitude;

    Double toLat;
    Double toLon;
    Double fromLat;
    Double fromLon;
    public View toMarkerView;
    public View fromMarkerView;
    public LatLng toLatLng;
    public LatLng fromLatLng;

    public LatLng position;
    public LocationManager locationManager;

public Context context;
    public String duration = "0 min";
    public String distance = "";
    private ImageView btnCancel;

    public ProgressBar pbCircular;

    public BitmapDescriptor iconMarker;

    public APICreateNewOrderRequestModel newOrderRequestModel;
    public BaseAPIResponseModel<APICreateNewOrderResponseModel> newOrderResponseDataModel = new BaseAPIResponseModel<APICreateNewOrderResponseModel>();
    public APICreateNewOrderResponseModel newOrderResponseModel = new APICreateNewOrderResponseModel();
    public OrderDCM orderDCM;
    OrderDAO orderDAO;

    public OrderDAO orderListDAO;
    public OrderDCM orderDetails;

    public APIGetNearByDriverRequestModel requestModelNearbyD;
    public BaseAPIResponseModel<APIGetNearByDriverResponseModel> responseNearByDriverDataModel = new BaseAPIResponseModel<APIGetNearByDriverResponseModel>();
    public APIGetNearByDriverResponseModel responseNearDriverModel = new APIGetNearByDriverResponseModel();

    List<DriverDCM> driverLists = new ArrayList<>();
    public PickupPointsDAO pickupPointDAO;
    public PickupPointDCM pickupPointDetails;

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM;

    public Handler timer = new Handler();


    public boolean canCheckNearbyDriverFlag = true;
    public boolean canGetOrderAmountRepeatFlag = true;
    public boolean createOrderFlag = true;
    public boolean canGetOrderStatusFlag = false;

    public boolean isConfirmClick = false;

    public String url;
    public View view;
    public ImageView flBackgroundblack;
    public ImageView btnCancelConfirm;
    public LinearLayout frameWebView;
    public WebView web_view;
    public Button btnRetry;

    public fragment_layout_confirm() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_layout_confirm, container, false);
        fragment_layout_confirm = this;

        mapView = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        mapView.getMapAsync(this);

        context = getContext();
        mGoogleApiClient = new GoogleApiClient.Builder(fragment_layout_confirm.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_bike_gogo);



        flLoadingWeb = (RelativeLayout) view.findViewById(R.id.flLoadingWeb);
        flBackgroundblackWeb = (ImageView) view.findViewById(R.id.flBackgroundblackWeb);
        pbCircularWeb=(ProgressBar) view.findViewById(R.id.pbCircularWeb);
        connectDriverWeb=(TextView)view.findViewById(R.id.connectDriverWeb);




        btnCancel = (ImageView) view.findViewById(R.id.btnCancel);
        rlBtnConfirm = (RelativeLayout) view.findViewById(R.id.rlBtnConfirm);
        lblDeliveryRate = (TextView) view.findViewById(R.id.lblDeliveryRate);
        flConfirm = (FrameLayout) view.findViewById(R.id.flConfirm);
        flMap = (FrameLayout) view.findViewById(R.id.flMap);
        flLoading = (RelativeLayout) view.findViewById(R.id.flLoading);




        btnCancelConfirm = (ImageView) view.findViewById(R.id.btnCancel);
        pbCircular = (ProgressBar) view.findViewById(R.id.pbCircular);
        flBackgroundblack = (ImageView) view.findViewById(R.id.flBackgroundblack);
        frameWebView = (LinearLayout) view.findViewById(R.id.frameWebView);
        btnRetry = (Button) view.findViewById(R.id.btnRetry);
        frameWebView.setVisibility(View.GONE);
        web_view = (WebView) view.findViewById(R.id.web_view);

        flLoading.setVisibility(View.INVISIBLE);
        pbCircular.setVisibility(View.INVISIBLE);
        btnCancelConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCancel.callOnClick();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FixLabels.WaitingPaymentConfrimationFromDriver = false;
                if (isConfirmClick == false) {
//                    Intent intent = new Intent(fragment_layout_confirm.getContext(), fragment_activity_landing_user.class);
//                    startActivity(intent);
                    try {
                        fActivity.showLayout(FixLabels.OrderStatus.NoActiveOrder);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment_layout_confirm.getContext());
                    alertDialogBuilder.setMessage("Are you sure you want to cancel your order?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    new Confirm_CancelOrderAsync(fragment_layout_confirm).execute();
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


            }
        });


        rlBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    timeCount  = 0;
                    isConfirmClick = false;
                    canCheckNearbyDriverFlag = false;
                    canGetOrderStatusFlag = false;
                    canGetOrderAmountRepeatFlag = false;
                FixLabels.WaitingPaymentConfrimationFromDriver = false;

                    new Confirm_CreatedActiveOrderAsync(fragment_layout_confirm).execute();


            }
        });
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pickupPointDAO = new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            pickupPointDetails = pickupPointDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            if (orderDAO.getAll().size() > 0) {
                orderDetails = orderDAO.getAll().get(0);
            } else {
                orderDetails = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (orderDetails != null) {

            isConfirmClick = true;

            canCheckNearbyDriverFlag = false;
            canGetOrderStatusFlag = true;
            updateViewGetForDriver();


        } else {
            isConfirmClick = false;

            canCheckNearbyDriverFlag = true;
            canGetOrderStatusFlag = false;
            updateViewCancelOrder();

        }
        canGetOrderAmountRepeatFlag = false;
        toLat = Double.parseDouble(pickupPointDetails.pickuplatitude);
        toLon = Double.parseDouble(pickupPointDetails.pickupLongitude);
        fromLat = Double.parseDouble(sessionDCM.defaultLatitude);
        fromLon = Double.parseDouble(sessionDCM.defaultLongitude);
        toLatLng = new LatLng(toLat, toLon);
        fromLatLng = new LatLng(fromLat, fromLon);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(toLatLng);
        builder.include(fromLatLng);
        LatLngBounds bounds = builder.build();


        mapView.newInstance(new GoogleMapOptions()
                .latLngBoundsForCameraTarget(bounds));

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frameWebView.setVisibility(View.INVISIBLE);
                Confirm_GetPendingOrderAmountAsync getPendingAmount = new Confirm_GetPendingOrderAmountAsync(fragment_layout_confirm, false);
                getPendingAmount.execute();
            }
        });
        return view;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // setMarkerAndCamera();
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
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;


        callAsynchronousTask();

        setMarkerAndCamera();
        String parameters = "origin=" + fromLatLng.latitude + "," + fromLatLng.longitude;
        parameters = parameters + "&destination=" + toLatLng.latitude + "," + toLatLng.longitude;
        parameters = parameters + "&sensor=false";
        parameters = parameters + "&key=" + FixLabels.map;
        url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        Confirm_PickupToCustomerLocationPathCoordinatesAsync fromServer=new Confirm_PickupToCustomerLocationPathCoordinatesAsync(fragment_layout_confirm,url);
        fromServer.execute(url);

        Confirm_GetPendingOrderAmountAsync getPendingAmount = new Confirm_GetPendingOrderAmountAsync(fragment_layout_confirm, false);
        getPendingAmount.execute();



    }

    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            LatLng latLng = new LatLng(latitude, longitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void blur(Bitmap bkg, View view) {
//
//        float radius = 20;
//        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
//                (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(overlay);
//        canvas.translate(-view.getLeft(), -view.getTop());
//        canvas.drawBitmap(bkg, 0, 0, null);
//        RenderScript rs = RenderScript.create(DriverHeadingToPickup.getContext());
//        Allocation overlayAlloc = Allocation.createFromBitmap(
//                rs, overlay);
//        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
//                rs, overlayAlloc.getElement());
//        blur.setInput(overlayAlloc);
//        blur.setRadius(radius);
//        blur.forEach(overlayAlloc);
//        overlayAlloc.copyTo(overlay);
//        view.setBackground(new BitmapDrawable(
//                getResources(), overlay));
//        rs.destroy();
//
//    }

    //    private void applyBlur() {
//        flBackgroundblack.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                flBackgroundblack.getViewTreeObserver().removeOnPreDrawListener(this);
//                flBackgroundblack.buildDrawingCache();
//                Bitmap bmp = flBackgroundblack.getDrawingCache();
//                blur(bmp, flMap);
//                return true;
//            }
//        });
//    }
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
            toMarkerView = inflater.inflate(R.layout.custom_info_window, viewGroup, false);
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


            fromMarkerView = inflater.inflate(R.layout.custom_info_window_user_location, viewGroup, false);
            TextView titleEnd = (TextView) fromMarkerView.findViewById(R.id.title);
            titleEnd.setText(titleEnd.getText());
            titleStart.setText(pickupPointDetails.title);
            tvDuration.setText(duration);
            fromMarkerView.setZ((float) 5);

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
            ((ViewGroup) viewmap).addView(toMarkerView);
            ((ViewGroup) viewmap).addView(fromMarkerView);

        }


        toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
        toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));

        fromMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (fromWidth / 2));
        fromMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - (fromMarkerView.getLayoutParams().height));


        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
//                canGetOrderStatusFlag = false;

                toMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(toLatLng).x) - (toWidth / 2));
                toMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(toLatLng).y) - (toMarkerView.getLayoutParams().height));

                fromMarkerView.setX((mGoogleMap.getProjection().toScreenLocation(fromLatLng).x) - (fromWidth / 2));
                fromMarkerView.setY((mGoogleMap.getProjection().toScreenLocation(fromLatLng).y) - (fromMarkerView.getLayoutParams().height));


                CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
                float zoomLevel = cameraPosition.zoom;
                for (BikerMarkerViewModel model : driversAround) {
                    int newSize = (int) ((zoomLevel * 70) / 14);
                    model.marker.setLayoutParams(new FrameLayout.LayoutParams(newSize, newSize));
                    model.marker.animate().x((mGoogleMap.getProjection().toScreenLocation(model.position).x) - (newSize / 2)).y((mGoogleMap.getProjection().toScreenLocation(model.position).y) - newSize).setDuration(0);
                }

            }
        });

        //Avoid vibration of marker on mover
//        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                if(isConfirmClick == true   ) {
//                    canGetOrderStatusFlag = true;
//                }
//            }
//        });
    }

    public int timeCount = 0;

    public void callAsynchronousTask() {
        timeCount = 0;
        timer = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                try {

                    if(canGetOrderAmountRepeatFlag == true)
                    {
                        fragment_layout_confirm.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("TEST", "Called");
                                new Confirm_GetPendingOrderAmountRepeatAsync(fragment_layout_confirm).execute();
                            }
                        });
                    }

                    if (canCheckNearbyDriverFlag == true) {



                        fragment_layout_confirm.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Confirm_CheckNearByDriverAsync(fragment_layout_confirm).execute();
                            }
                        });

                    }

                    timeCount = timeCount + 1;
                    if (canGetOrderStatusFlag == true) {


                        if (timeCount < 180) {
                            fragment_layout_confirm.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Confirm_GetOrderStatusAsync orderStatusAsync = new Confirm_GetOrderStatusAsync(fragment_layout_confirm);
                                    orderStatusAsync.execute();
                                }
                            });
                        } else {

                            canGetOrderStatusFlag = false;

                            fragment_layout_confirm.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment_layout_confirm.getContext());
                                    alertDialogBuilder.setMessage("No driver found. Please try again later.");
                                    alertDialogBuilder.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    new Confirm_CancelOrderAsync(fragment_layout_confirm).execute();
                                                }
                                            });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            });
                        }
                    }

                    timer.postDelayed(this, 1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        };
        timer.postDelayed(runnableCode, 1000);
    }

    public void updateViewGetForDriver() {

        rlBtnConfirm.setVisibility(View.INVISIBLE);
        flLoading.setVisibility(View.VISIBLE);
        pbCircular.setVisibility(View.VISIBLE);
        lblDeliveryRate.setVisibility(View.INVISIBLE);


        ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(pbCircular,
                "rotation", 0f, 360f);
        processObjectAnimator.setDuration(1000);
        processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        processObjectAnimator.setInterpolator(new LinearInterpolator());
        processObjectAnimator.start();

    }

    public void updateViewCancelOrder() {
        rlBtnConfirm.setVisibility(View.VISIBLE);
        flLoading.setVisibility(View.INVISIBLE);
        pbCircular.setVisibility(View.INVISIBLE);
        lblDeliveryRate.setVisibility(View.VISIBLE);


//        applyBlur();
    }

    /**
     * A method to download json data from url
     */
    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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
                new Confirm_GetPendingOrderAmountAsync(fragment_layout_confirm, false).execute();

            }
        }

    }

    public void moveNextScreen()
    {

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canCheckNearbyDriverFlag = false;
        canGetOrderAmountRepeatFlag = false;
        canGetOrderStatusFlag = false;
        if(timer!=null) {
            timer.removeCallbacksAndMessages(null);
            timer = null;
        }

    }
}








