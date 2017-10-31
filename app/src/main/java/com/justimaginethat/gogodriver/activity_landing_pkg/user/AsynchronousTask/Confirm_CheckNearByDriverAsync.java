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
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Confirm_CheckNearByDriverAsync extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;
    public APIGetNearByDriverRequestModel requestModelNearbyD=new APIGetNearByDriverRequestModel();
    public  BaseAPIResponseModel<APIGetNearByDriverResponseModel> responseNearByDriverDataModel = new BaseAPIResponseModel<APIGetNearByDriverResponseModel>();
    public APIGetNearByDriverResponseModel responseNearDriverModel = new APIGetNearByDriverResponseModel();
    List<DriverDCM> driverLists=new ArrayList<>();
    public fragment_layout_confirm fragmentLayoutConform;

    public LatLng position;


    ArrayList<LatLng> pointsDrivers = null;
    private MarkerOptions driverPosition;
    private List<MarkerOptions> driverPositionList;
    public Marker driverPositionMarker;
    private BitmapDescriptor iconMarker;

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();


    public Confirm_CheckNearByDriverAsync(fragment_layout_confirm fragmentLayoutConform) {
        this.fragmentLayoutConform = fragmentLayoutConform;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();

        sessionDAO= new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());

        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        requestModelNearbyD = new APIGetNearByDriverRequestModel();

        requestModelNearbyD.idUser=sessionDCM.idUser;
        requestModelNearbyD.address="Pick UP Location Near driver";
        requestModelNearbyD.longitude=fragmentLayoutConform.pickupPointDetails.pickupLongitude;
        requestModelNearbyD.latitude=fragmentLayoutConform.pickupPointDetails.pickuplatitude ;



        fragmentLayoutConform.canCheckNearbyDriverFlag =false;
//            canGetOrderStatusFlag=false;
//            progressDialog = ProgressDialog.show(context, "", "", true, false);
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
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.fragment_layout_confirm.context)
//                        .setTitle(FixLabels.alertDefaultTitle)
//                        .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();

                if(fragmentLayoutConform.context != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    fragmentLayoutConform.canCheckNearbyDriverFlag =true;
                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                }
                else
                {
                    fragmentLayoutConform.canCheckNearbyDriverFlag =true;
                }
            } else {
                if (responseNearDriverModel != null) {
                    if (responseNearDriverModel.errorLogId == 0) {
                        if (responseNearDriverModel.driverList!=null) {
                            driverLists=new ArrayList<DriverDCM>();
                            driverLists.addAll(responseNearDriverModel.driverList);

                            DriverDAO driverDAO=new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            driverDAO.create(responseNearDriverModel.driverList);
                            try {
                                driverDAO.getAll();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                            for(int i=0;i<driverLists.size();i++)
                            {

                                int indOld = -1;
                                for(int j=0;j<fragmentLayoutConform.driversAround.size();j++)
                                {

                                    if(fragmentLayoutConform.driversAround.get(j).user.idUser == driverLists.get(i).idUser)
                                    {
                                        indOld = j;
                                        break;
                                    }

                                }

                                if(indOld != -1)
                                {
                                    fragmentLayoutConform.driversAround.get(indOld).user = driverLists.get(i);
                                    fragmentLayoutConform.driversAround.get(indOld).updated = true;
                                }
                                else
                                {
                                    final BikerMarkerViewModel model = new BikerMarkerViewModel();
                                    model.user  = driverLists.get(i);
                                    model.updated = true;
                                    fragmentLayoutConform.driversAround.add(model);
                                }

                            }

                            if(driverLists.size() == 0)
                            {
                                for(int j=0;j<fragmentLayoutConform.driversAround.size();j++) {
                                    fragmentLayoutConform.driversAround.get(j).updated = false;
                                }
                            }
                            else
                            {

                                for(int i=0;i<fragmentLayoutConform.driversAround.size() ;i++)
                                {

                                    int indOld = -1;
                                    for(int j=0;j<driverLists.size();j++)
                                    {

                                        if(fragmentLayoutConform.driversAround.get(i).user.idUser == driverLists.get(j).idUser)
                                        {
                                            indOld = j;
                                            break;
                                        }

                                    }

                                    if(indOld != -1)
                                    {
                                        fragmentLayoutConform.driversAround.get(i).user = driverLists.get(indOld);
                                        fragmentLayoutConform.driversAround.get(i).updated = true;
                                    }
                                    else
                                    {
                                        fragmentLayoutConform.driversAround.get(i).updated = false;
                                    }


                                }
                            }

                            for(int j=0;j<fragmentLayoutConform.driversAround.size();j++) {
                                if(fragmentLayoutConform.driversAround.get(j).updated == false)
                                {
                                    View viewmap = fragmentLayoutConform.mapView.getView(); // returns base view of the fragment
                                    ViewGroup viewGroup = (ViewGroup) viewmap;
                                    ((ViewGroup)viewmap).removeView(fragmentLayoutConform.driversAround.get(j).marker);
                                    fragmentLayoutConform.driversAround.remove(j);
                                    j = j - 1;
                                }
                            }


                            iconMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_bike_gogo);


                            for (int i=0;i<fragmentLayoutConform.driversAround.size();i++) {

                                double lat = Double.parseDouble(fragmentLayoutConform.driversAround.get(i).user.latitude2);
                                double lng = Double.parseDouble(fragmentLayoutConform.driversAround.get(i).user.longitude2);
                                LatLng positionDriver = new LatLng(lat, lng);
                                View viewmap = fragmentLayoutConform.mapView.getView(); // returns base view of the fragment
                                ViewGroup viewGroup = (ViewGroup) viewmap;
                                if(fragmentLayoutConform.driversAround.get(i).marker == null)
                                {

                                    ImageView imgDriver = new ImageView(fragmentLayoutConform.context);
                                    imgDriver.setImageResource(R.drawable.icon_bike_gogo);

                                    ((ViewGroup)viewmap).addView(imgDriver);

                                    CameraPosition cameraPosition = fragmentLayoutConform.mGoogleMap.getCameraPosition();
                                    float zoomLevel  = cameraPosition.zoom;
                                    int newSize = (int)((zoomLevel * 70) / 14);
                                    imgDriver.animate().rotation(Float.parseFloat(fragmentLayoutConform.driversAround.get(i).user.heading)).setDuration(500);
                                    imgDriver.setLayoutParams(new FrameLayout.LayoutParams(newSize,newSize));
                                    imgDriver.animate().x((fragmentLayoutConform.mGoogleMap.getProjection().toScreenLocation(positionDriver).x) - (newSize/2)).y((fragmentLayoutConform.mGoogleMap.getProjection().toScreenLocation(positionDriver).y) - newSize).setDuration(500);
                                    imgDriver.setZ((float)1);
                                    fragmentLayoutConform.driversAround.get(i).marker = imgDriver;
                                    fragmentLayoutConform.driversAround.get(i).position = positionDriver;
                                }
                                else
                                {

                                    CameraPosition cameraPosition = fragmentLayoutConform.mGoogleMap.getCameraPosition();
                                    float zoomLevel  = cameraPosition.zoom;
                                    int newSize = (int)((zoomLevel * 70) / 14);
                                    fragmentLayoutConform.driversAround.get(i).marker.animate().rotation(Float.parseFloat(fragmentLayoutConform.driversAround.get(i).user.heading)).setDuration(500);
                                    fragmentLayoutConform.driversAround.get(i).marker.setLayoutParams(new FrameLayout.LayoutParams(newSize,newSize));
                                    fragmentLayoutConform.driversAround.get(i).marker.animate().x((fragmentLayoutConform.mGoogleMap.getProjection().toScreenLocation(positionDriver).x) - (newSize/2)).y((fragmentLayoutConform.mGoogleMap.getProjection().toScreenLocation(positionDriver).y) - newSize).setDuration(500);
                                    fragmentLayoutConform.driversAround.get(i).position = positionDriver;

                                }
                            }

                            fragmentLayoutConform.canCheckNearbyDriverFlag =true;
                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( fragmentLayoutConform.fragment_layout_confirm.context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("No Driver List Near to Your Location.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            fragmentLayoutConform.canCheckNearbyDriverFlag =true;

                                        }
                                    })
                                      .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( fragmentLayoutConform.fragment_layout_confirm.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Server Error Log : " + responseNearDriverModel.errorLogId + "\n errorURL :" + responseNearDriverModel.errorURL)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = responseNearDriverModel.errorURL;
                                        Intent i = new Intent( fragmentLayoutConform.context, activity_error_webview.class);
                                        fragmentLayoutConform.canCheckNearbyDriverFlag =true;
                                        i.putExtra("url", url);
                                        fragmentLayoutConform.startActivity(i);
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        Log.i("PayBit", "APILogin Failed from server Error Log : " + responseNearDriverModel.errorLogId + "\n errorURL :" + responseNearDriverModel.errorURL);
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( fragmentLayoutConform.fragment_layout_confirm.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    fragmentLayoutConform.canCheckNearbyDriverFlag =false;
                                    fragmentLayoutConform.getActivity().finish();
                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                }
            }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
        }
    }