package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetNearByDriverRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetNearByDriverResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.BikerMarkerViewModel;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_pickup_selection_list;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Landing_CheckNearByDriverAsync extends AsyncTask<Void, Void, Boolean> {
    ProgressDialog progressDialog;
    public APIGetNearByDriverRequestModel requestModelNearbyD = new APIGetNearByDriverRequestModel();
    public BaseAPIResponseModel<APIGetNearByDriverResponseModel> responseNearByDriverDataModel = new BaseAPIResponseModel<APIGetNearByDriverResponseModel>();
    public APIGetNearByDriverResponseModel responseNearDriverModel = new APIGetNearByDriverResponseModel();
    List<DriverDCM> driverLists = new ArrayList<>();
    public fragment_layout_pickup_selection_list fragmentLayoutPickupSelectionList;

    public LatLng position;


    ArrayList<LatLng> pointsDrivers = null;
    private MarkerOptions driverPosition;
    private List<MarkerOptions> driverPositionList;
    public Marker driverPositionMarker;
    private BitmapDescriptor iconMarker;

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();

    public DriverDAO driverDAO;


    public Landing_CheckNearByDriverAsync(fragment_layout_pickup_selection_list fragmentLayoutPickupSelectionList) {
        this.fragmentLayoutPickupSelectionList = fragmentLayoutPickupSelectionList;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());

        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        requestModelNearbyD = new APIGetNearByDriverRequestModel();

        requestModelNearbyD.idUser = sessionDCM.idUser;
        requestModelNearbyD.address = sessionDCM.address1;
        requestModelNearbyD.longitude = String.valueOf(fragmentLayoutPickupSelectionList.latLng.longitude);
        requestModelNearbyD.latitude = String.valueOf(fragmentLayoutPickupSelectionList.latLng.latitude);
//        requestModelNearbyD.longitude=sessionDCM.defaultLongitude;
//        requestModelNearbyD.latitude=sessionDCM.defaultLatitude ;
        fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = false;

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestModelNearbyD));
        String str = helperSP.requestHTTPResponse(APILinks.APIGetNearByDriver, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseNearByDriverDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseNearByDriverDataModel.Data);
            responseNearDriverModel = gson.fromJson(str2, APIGetNearByDriverResponseModel.class);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {
            if (fragmentLayoutPickupSelectionList.context != null) {


                fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = true;


//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutPickupSelectionList.context)
//                            .setTitle(FixLabels.alertDefaultTitle)
//                            .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = true;
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
            } else {
                fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = true;
            }

        } else {
            if (responseNearDriverModel != null) {
                if (responseNearDriverModel.errorLogId == 0) {
                    if (responseNearDriverModel.driverList != null) {
                        driverLists = new ArrayList<DriverDCM>();
                        driverLists.addAll(responseNearDriverModel.driverList);


                        for (int i = 0; i < driverLists.size(); i++) {

                            int indOld = -1;
                            for (int j = 0; j < fragmentLayoutPickupSelectionList.driversAround.size(); j++) {

                                if (fragmentLayoutPickupSelectionList.driversAround.get(j).user.idUser == driverLists.get(i).idUser) {
                                    indOld = j;
                                    break;
                                }

                            }

                            if (indOld != -1) {
                                fragmentLayoutPickupSelectionList.driversAround.get(indOld).user = driverLists.get(i);
                                fragmentLayoutPickupSelectionList.driversAround.get(indOld).updated = true;
                            } else {
                                final BikerMarkerViewModel model = new BikerMarkerViewModel();
                                model.user = driverLists.get(i);
                                model.updated = true;
                                fragmentLayoutPickupSelectionList.driversAround.add(model);
                            }

                        }


                        if (driverLists.size() == 0) {
                            for (int j = 0; j < fragmentLayoutPickupSelectionList.driversAround.size(); j++) {
                                fragmentLayoutPickupSelectionList.driversAround.get(j).updated = false;
                            }
                        } else {

                            for (int i = 0; i < fragmentLayoutPickupSelectionList.driversAround.size(); i++) {

                                int indOld = -1;
                                for (int j = 0; j < driverLists.size(); j++) {

                                    if (fragmentLayoutPickupSelectionList.driversAround.get(i).user.idUser == driverLists.get(j).idUser) {
                                        indOld = j;
                                        break;
                                    }

                                }

                                if (indOld != -1) {
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).user = driverLists.get(indOld);
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).updated = true;
                                } else {
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).updated = false;
                                }


                            }
                        }

                        for (int j = 0; j < fragmentLayoutPickupSelectionList.driversAround.size(); j++) {
                            if (fragmentLayoutPickupSelectionList.driversAround.get(j).updated == false) {
                                View viewmap = fragmentLayoutPickupSelectionList.mapView.getView(); // returns base view of the fragment
                                ViewGroup viewGroup = (ViewGroup) viewmap;
                                if (((ViewGroup) viewmap) != null) {
                                    ((ViewGroup) viewmap).removeView(fragmentLayoutPickupSelectionList.driversAround.get(j).marker);
                                    fragmentLayoutPickupSelectionList.driversAround.remove(j);
                                }
                                j = j - 1;
                            }
                        }


                        iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_bike_gogo);


                        for (int i = 0; i < fragmentLayoutPickupSelectionList.driversAround.size(); i++) {
                            if (fragmentLayoutPickupSelectionList.driversAround.get(i).user.heading == null) {
                                fragmentLayoutPickupSelectionList.driversAround.get(i).user.heading = "0";
                            }

                            double lat = Double.parseDouble(fragmentLayoutPickupSelectionList.driversAround.get(i).user.latitude2);
                            double lng = Double.parseDouble(fragmentLayoutPickupSelectionList.driversAround.get(i).user.longitude2);
                            LatLng positionDriver = new LatLng(lat, lng);
                            View viewmap = fragmentLayoutPickupSelectionList.mapView.getView(); // returns base view of the fragment
                            if (viewmap != null) {
                                ViewGroup viewGroup = (ViewGroup) viewmap;
                                if (fragmentLayoutPickupSelectionList.driversAround.get(i).marker == null) {

                                    ImageView imgDriver = new ImageView(fragmentLayoutPickupSelectionList.context);
                                    imgDriver.setImageResource(R.drawable.icon_bike_gogo);

                                    ((ViewGroup) viewmap).addView(imgDriver);

                                    CameraPosition cameraPosition = fragmentLayoutPickupSelectionList.mGoogleMap.getCameraPosition();
                                    float zoomLevel = cameraPosition.zoom;
                                    int newSize = (int) ((zoomLevel * 70) / 14);
                                    imgDriver.animate().rotation(Float.parseFloat(fragmentLayoutPickupSelectionList.driversAround.get(i).user.heading)).setDuration(500);
                                    imgDriver.setLayoutParams(new FrameLayout.LayoutParams(newSize, newSize));
                                    imgDriver.animate().x((fragmentLayoutPickupSelectionList.mGoogleMap.getProjection().toScreenLocation(positionDriver).x) - (newSize / 2)).y((fragmentLayoutPickupSelectionList.mGoogleMap.getProjection().toScreenLocation(positionDriver).y) - newSize).setDuration(500);
                                    imgDriver.setZ((float) 1);
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).marker = imgDriver;
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).position = positionDriver;
                                } else {

                                    CameraPosition cameraPosition = fragmentLayoutPickupSelectionList.mGoogleMap.getCameraPosition();
                                    float zoomLevel = cameraPosition.zoom;
                                    int newSize = (int) ((zoomLevel * 70) / 14);
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).marker.animate().rotation(Float.parseFloat(fragmentLayoutPickupSelectionList.driversAround.get(i).user.heading)).setDuration(500);
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).marker.setLayoutParams(new FrameLayout.LayoutParams(newSize, newSize));
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).marker.animate().x((fragmentLayoutPickupSelectionList.mGoogleMap.getProjection().toScreenLocation(positionDriver).x) - (newSize / 2)).y((fragmentLayoutPickupSelectionList.mGoogleMap.getProjection().toScreenLocation(positionDriver).y) - newSize).setDuration(500);
                                    fragmentLayoutPickupSelectionList.driversAround.get(i).position = positionDriver;

                                }
                            }
                        }

                        fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = true;
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutPickupSelectionList.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("No Driver List Near to Your Location.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = true;

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutPickupSelectionList.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + responseNearDriverModel.errorLogId + "\n errorURL :" + responseNearDriverModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = responseNearDriverModel.errorURL;
                                    Intent i = new Intent(fragmentLayoutPickupSelectionList.context, activity_error_webview.class);
                                    fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = true;
                                    i.putExtra("url", url);
                                    fragmentLayoutPickupSelectionList.startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + responseNearDriverModel.errorLogId + "\n errorURL :" + responseNearDriverModel.errorURL);
                }
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutPickupSelectionList.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentLayoutPickupSelectionList.checkNearbyDriverFlag = false;
                                fragmentLayoutPickupSelectionList.getActivity().finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
    }
}