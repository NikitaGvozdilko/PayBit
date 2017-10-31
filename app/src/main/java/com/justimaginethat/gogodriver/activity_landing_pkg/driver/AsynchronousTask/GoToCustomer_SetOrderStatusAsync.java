package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APISetOrderStatusRequestModel;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APISetOrderStatusResponseModel;
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
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_customer;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_pickup;

import java.sql.SQLException;
import java.util.HashMap;

public class GoToCustomer_SetOrderStatusAsync extends AsyncTask<Void, Void, Boolean> {

    public Context context;
    public CustomProgressBar progressDialog;
    public APISetOrderStatusRequestModel requestUpdateStatusModel=new APISetOrderStatusRequestModel();
    public BaseAPIResponseModel<APISetOrderStatusResponseModel> responseUpdateStatusDataModel = new BaseAPIResponseModel<APISetOrderStatusResponseModel>();
    public APISetOrderStatusResponseModel responseUpdateStatusModel = new APISetOrderStatusResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();

    public fragment_goto_customer FLGotoPick;



    public OrderDCM orderDCM=new OrderDCM();
    public OrderDAO orderDAO;
    public PickupPointDCM pickupPointDCM;
    public PickupPointsDAO pickupPointDAO;

    public GoToCustomer_SetOrderStatusAsync(fragment_goto_customer FLGotoPick) {
        this.FLGotoPick=FLGotoPick;
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

        orderDAO= new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            orderDCM = orderDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }




        requestUpdateStatusModel.idDriver=sessionDCM.idUser;
        requestUpdateStatusModel.address= FLGotoPick.addressDetail;

        requestUpdateStatusModel.longitude= String.valueOf(FLGotoPick.mLastLocation.getLongitude());
        requestUpdateStatusModel.latitude= String.valueOf(FLGotoPick.mLastLocation.getLatitude());

        requestUpdateStatusModel.heading= String.valueOf(FLGotoPick.heading);

        requestUpdateStatusModel.orderStatus=FLGotoPick.OrderStatus;

        requestUpdateStatusModel.heading=sessionDCM.heading;
        requestUpdateStatusModel.orderStatus=FLGotoPick.OrderStatus;
        requestUpdateStatusModel.idOrderMaster=orderDCM.idOrderMaster;
        FLGotoPick.canSetOrderStatus = false;
//        progressDialog = ProgressDialog.show(FLGotoPick.context, "", "", true, false);

        progressDialog = CustomProgressBar.show(FLGotoPick.context,"", true,false,null);


    }
        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestUpdateStatusModel));
            String str = helperSP.requestHTTPResponse(APILinks.APISetOrderStatus, map, "POST", false);
            if (str.equals("")) {
                return false;
            } else {
                responseUpdateStatusDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseUpdateStatusDataModel.Data);
                responseUpdateStatusModel = gson.fromJson(str2, APISetOrderStatusResponseModel.class);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FLGotoPick.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FLGotoPick.canSetOrderStatus = true;
                            }
                        })
                          .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            } else {
                if (responseUpdateStatusModel != null) {
                    if (responseUpdateStatusModel.errorLogId == 0) {



                            if(responseUpdateStatusModel.orderStatus != null) {

                                if (
                                        requestUpdateStatusModel.idDriver == requestUpdateStatusModel.idDriver
                                                && requestUpdateStatusModel.idOrderMaster == responseUpdateStatusModel.idOrderMaster
                                                && FLGotoPick.OrderStatus.equals(FixLabels.OrderStatus.OrderComplete)) {
                                    FLGotoPick.timer.removeCallbacksAndMessages(null);
                                    FLGotoPick.timer = null;
                                    FLGotoPick.canSetOrderStatus = false;
                                    FLGotoPick.fActivity.showLayout(FixLabels.OrderStatus.OrderComplete);
                                } else {
                                    FLGotoPick.canSetOrderStatus = true;
                                }
                            }
                        else
                            {
                                FLGotoPick.canSetOrderStatus = true;
                            }


                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FLGotoPick.context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("Server Error Log : " + responseUpdateStatusModel.errorLogId + "\n errorURL :" + responseUpdateStatusModel.errorURL)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String url = responseUpdateStatusModel.errorURL;
                                            Intent i = new Intent(context, activity_error_webview.class);
                                            i.putExtra("url", url);
                                            FLGotoPick.context.startActivity(i);
                                        }
                                    })
                                      .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                            Log.i("PayBit", "APILogin Failed from server Error Log : " + responseUpdateStatusModel.errorLogId + "\n errorURL :" + responseUpdateStatusModel.errorURL);
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FLGotoPick.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("No response from server.Application will now close.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FLGotoPick.fActivity.finish();
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                    }
                }

            }
    }