package com.justimaginethat.gogodriver.activity_settings.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIUpdateProfileRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIUpdateProfileResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_settings.updateModule.updateAddressProfile;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class UpdateAddressProfileAsync extends AsyncTask<Void, Void, Boolean> {

    public Context context;
    public CustomProgressBar progressDialog;
    public APIUpdateProfileRequestModel requestUpdateStatusModel=new APIUpdateProfileRequestModel();
    public BaseAPIResponseModel<APIUpdateProfileResponseModel> responseUpdateStatusDataModel = new BaseAPIResponseModel<APIUpdateProfileResponseModel>();
    public APIUpdateProfileResponseModel responseUpdateStatusModel = new APIUpdateProfileResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();



    public OrderDCM orderDCM=new OrderDCM();
    public OrderDAO orderDAO;

    public PickupPointDCM pickupPointDCM;
    public PickupPointsDAO pickupPointDAO;

    public updateAddressProfile fActivity;




    public UpdateAddressProfileAsync(updateAddressProfile fActivity) {
        this.fActivity=fActivity;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();

//        progressDialog = ProgressDialog.show(fActivity, "", "", true, false);

        progressDialog = CustomProgressBar.show(fActivity.context,"", true,false,null);


        sessionDAO= new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!sessionDCM.address1.equals(fActivity.messageAddress)){
            sessionDCM.address1=fActivity.messageAddress;
            sessionDCM.defaultAddress=fActivity.messageAddress;

            sessionDCM.buildingName1=fActivity.buildingName1;
            sessionDCM.defaultBuildingName=fActivity.buildingName1;


            sessionDCM.floorNumber1=fActivity.floorNumber1;
            sessionDCM.defaultFloorNumber=fActivity.floorNumber1;

            sessionDCM.latitude1=fActivity.latitude1;
            sessionDCM.longitude1=fActivity.longitude1;

            sessionDCM.defaultLatitude=fActivity.latitude1;
            sessionDCM.defaultLongitude=fActivity.longitude1;

            sessionDAO.update(sessionDCM);
            requestUpdateStatusModel.user=sessionDCM;
        }
        if(!sessionDCM.buildingName1.equals(fActivity.buildingName1)){
            sessionDCM.buildingName1=fActivity.buildingName1;
            sessionDCM.defaultBuildingName=fActivity.buildingName1;
            sessionDAO.update(sessionDCM);
            requestUpdateStatusModel.user=sessionDCM;
        }
        if(!sessionDCM.floorNumber1.equals(fActivity.floorNumber1)){
            sessionDCM.floorNumber1=fActivity.floorNumber1;
            sessionDCM.defaultFloorNumber=fActivity.floorNumber1;
            sessionDAO.update(sessionDCM);
            requestUpdateStatusModel.user=sessionDCM;
        }

        else{
            requestUpdateStatusModel.user=sessionDCM;
        }
//        requestModel.user.idUser=sessionDCM.idUser;
//        requestModel.user.firstName=fActivity.firstName;

        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestUpdateStatusModel));

            HashMap<String, String> mapFile = new HashMap<String, String>();
            if (new File(sessionDCM.profilePicture).exists()) {
                mapFile.put("File1", sessionDCM.profilePicture);
            }


            String str = null;
            try {
                str = helperSP.requestHTTPResponseWithFile(APILinks.APIUpdateProfile, map, mapFile, "POST", true);
                if (str.equals("")) {
                    return false;
                } else {
                    responseUpdateStatusDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                    String str2 = gson.toJson(responseUpdateStatusDataModel.Data);
                    responseUpdateStatusModel = gson.fromJson(str2, APIUpdateProfileResponseModel.class);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if (response == false) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                          .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            } else {
                if (responseUpdateStatusModel != null) {
                    if (responseUpdateStatusModel.errorLogId == 0) {
                        if (responseUpdateStatusModel.user.idUser >0) {
                            sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            sessionDCM = new SessionDCM();
//                            sessionDAO.deleteAll();
                            sessionDAO.update(responseUpdateStatusModel.user);

                            fActivity.finish();

                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Server Error Log : " + responseUpdateStatusModel.errorLogId + "\n errorURL :" + responseUpdateStatusModel.errorURL)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = responseUpdateStatusModel.errorURL;
                                        Intent i = new Intent( context, activity_error_webview.class);
                                        i.putExtra("url", url);
                                        fActivity.startActivity(i);
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        Log.i("PayBit", "APILogin Failed from server Error Log : " + responseUpdateStatusModel.errorLogId + "\n errorURL :" + responseUpdateStatusModel.errorURL);
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    fActivity.finish();
                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                }
            }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//                fActivity.finish();
//            }
        }
    }