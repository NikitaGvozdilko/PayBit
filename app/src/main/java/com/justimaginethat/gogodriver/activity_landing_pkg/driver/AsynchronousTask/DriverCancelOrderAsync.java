package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APICancelOrderRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APICancelOrderResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_pickup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverCancelOrderAsync extends AsyncTask<Void, Void, Boolean> {
    public CustomProgressBar progressDialog;
    public APICancelOrderRequestModel requestCancelModel=new APICancelOrderRequestModel();
    public  BaseAPIResponseModel<APICancelOrderResponseModel> responseCancelDataModel = new BaseAPIResponseModel<APICancelOrderResponseModel>();
    public APICancelOrderResponseModel responseCancelOrderModel = new APICancelOrderResponseModel();
    List<DriverDCM> driverLists=new ArrayList<>();
    public fragment_goto_pickup FLGotoPick;

    public LatLng position;
    public LatLng positionDriver;

    ArrayList<LatLng> pointsDrivers = null;
    private MarkerOptions driverPosition;
    public Marker driverPositionMarker;
    private BitmapDescriptor iconMarker;

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();

    OrderDCM orderDCM=new OrderDCM();
    OrderDAO orderDAO;


    public DriverCancelOrderAsync(fragment_goto_pickup FLGotoPick) {
        this.FLGotoPick = FLGotoPick;
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


        requestCancelModel = new APICancelOrderRequestModel();

        requestCancelModel.idUser=sessionDCM.idUser;
        requestCancelModel.idOrderMaster=orderDCM.idOrderMaster;




//            canGetOrderStatusFlag=false;
//            progressDialog = ProgressDialog.show(context, "", "", true, false);
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestCancelModel));
            String str = helperSP.requestHTTPResponse(APILinks.APIDriverCancelOrder, map, "POST", false);
            if (str.equals("")) {
                return false;
            } else {
                responseCancelDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseCancelDataModel.Data);
                responseCancelOrderModel = gson.fromJson(str2, APICancelOrderResponseModel.class);
                return true;
            }
        }
        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if (response == false) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FLGotoPick.context)
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
                if (responseCancelOrderModel != null) {
                    if (responseCancelOrderModel.errorLogId == 0) {
                        if (responseCancelOrderModel.idOrderMaster>0) {
                            if(responseCancelOrderModel.orderStatus.equals(FixLabels.OrderStatus.OrderCanceled)){
                                DriverDAO driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                driverDAO.deleteAll();
                                UserDAO userDAO = new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                userDAO.deleteAll();
                                orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                orderDAO.deleteAll();
                                FLGotoPick.OrderStatus=FixLabels.OrderStatus.OrderCanceled;
                                FLGotoPick.fActivity.showLayout(FixLabels.OrderStatus.NoActiveOrder);
                            }
                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( FLGotoPick.context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("Either your email/mobile number and password do not match or your account is not active.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                      .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( FLGotoPick.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Server Error Log : " + responseCancelOrderModel.errorLogId + "\n errorURL :" + responseCancelOrderModel.errorURL)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = responseCancelOrderModel.errorURL;
                                        Intent i = new Intent( FLGotoPick.context, activity_error_webview.class);
                                        i.putExtra("url", url);
                                        FLGotoPick.startActivity(i);
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        Log.i("PayBit", "APILogin Failed from server Error Log : " + responseCancelOrderModel.errorLogId + "\n errorURL :" + responseCancelOrderModel.errorURL);
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( FLGotoPick.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FLGotoPick.getActivity().finish();
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