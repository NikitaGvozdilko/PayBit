package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIUpdateAddressRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIUpdateAddressResponseModel;
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
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.activity_default_address_setter;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UpdateAddressAsync extends AsyncTask<Void, Void, Boolean> {

    public CustomProgressBar progressDialog;
    public APIUpdateAddressRequestModel requestModel =new APIUpdateAddressRequestModel();
    public BaseAPIResponseModel<APIUpdateAddressResponseModel> responseUpdateStatusDataModel = new BaseAPIResponseModel<APIUpdateAddressResponseModel>();
    public APIUpdateAddressResponseModel responseModel = new APIUpdateAddressResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();



    public OrderDCM orderDCM=new OrderDCM();
    public OrderDAO orderDAO;

    public PickupPointDCM pickupPointDCM;
    public PickupPointsDAO pickupPointDAO;

    public activity_default_address_setter fActivity;




    public UpdateAddressAsync(activity_default_address_setter fActivity,SessionDCM sessionDCM) {
        this.fActivity=fActivity;
        this.sessionDCM = sessionDCM;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();

//        progressDialog = ProgressDialog.show(fActivity, "", "", true, false);
        progressDialog = CustomProgressBar.show(fActivity.context,"", true,false,null);
        requestModel.address1=sessionDCM.address1;
        requestModel.address2=sessionDCM.address2;
        requestModel.defaultAddress=sessionDCM.defaultAddress;
        requestModel.buildingName1=sessionDCM.buildingName1;
        requestModel.buildingName2=sessionDCM.buildingName2;
        requestModel.defaultBuildingName=sessionDCM.defaultBuildingName;
        requestModel.floorNumber1=sessionDCM.floorNumber1;
        requestModel.floorNumber2=sessionDCM.floorNumber2;
        requestModel.defaultFloorNumber=sessionDCM.defaultFloorNumber;
        requestModel.latitude1=sessionDCM.latitude1;
        requestModel.latitude2=sessionDCM.latitude2;
        requestModel.defaultLatitude=sessionDCM.defaultLatitude;
        requestModel.longitude1=sessionDCM.longitude1;
        requestModel.longitude2=sessionDCM.longitude2;
        requestModel.defaultLongitude=sessionDCM.defaultLongitude;

        requestModel.idUser = sessionDCM.idUser;



        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestModel));

            HashMap<String, String> mapFile = new HashMap<String, String>();
            if (new File(sessionDCM.profilePicture).exists()) {
                mapFile.put("File1", sessionDCM.profilePicture);
            }


            String str = null;
            str = helperSP.requestHTTPResponse(APILinks.APIUpdateAddress, map, "POST", true);
            if (str.equals("")) {
                return false;
            } else {
                responseUpdateStatusDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseUpdateStatusDataModel.Data);
                responseModel = gson.fromJson(str2, APIUpdateAddressResponseModel.class);
                return true;
            }

        }
        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if (progressDialog != null) {
                progressDialog.dismiss();

            }
            if (response == false) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
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
                if (responseModel != null) {
                    if (responseModel.errorLogId == 0) {
                        if (responseModel.idUser >0) {
                            sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            sessionDAO.update(sessionDCM);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("Address saved.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
//                                            Intent intentBack = new Intent(fActivity, fragment_activity_landing_user.class);
//                                            fActivity.startActivity(intentBack);
                                            fActivity.finish();
                                        }
                                    })
                                      .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();



                        }
                        else
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("Address was not saved due to error!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                      .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = responseModel.errorURL;
                                        Intent i = new Intent( fActivity.context, activity_error_webview.class);
                                        i.putExtra("url", url);
                                        fActivity.startActivity(i);
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        Log.i("PayBit", "APILogin Failed from server Error Log : " + responseModel.errorLogId + "\n errorURL :" + responseModel.errorURL);
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
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

        }
    }