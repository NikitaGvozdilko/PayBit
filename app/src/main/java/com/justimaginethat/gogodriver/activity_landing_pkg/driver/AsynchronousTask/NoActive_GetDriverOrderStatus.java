package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIGetDriverOrderStatusRequestModel;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIGetDriverOrderStatusResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_no_active_order;

import java.sql.SQLException;
import java.util.HashMap;

public class NoActive_GetDriverOrderStatus extends AsyncTask<Void, Void, Boolean> {

    public Context context;
    public ProgressDialog progressDialog;
    public APIGetDriverOrderStatusRequestModel requestUpdateStatusModel = new APIGetDriverOrderStatusRequestModel();
    public BaseAPIResponseModel<APIGetDriverOrderStatusResponseModel> responseUpdateStatusDataModel = new BaseAPIResponseModel<APIGetDriverOrderStatusResponseModel>();
    public APIGetDriverOrderStatusResponseModel responseUpdateStatusModel = new APIGetDriverOrderStatusResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();


    public OrderDCM orderDCM = new OrderDCM();
    public OrderDAO orderDAO;

    public PickupPointDCM pickupPointDCM;
    public PickupPointsDAO pickupPointDAO;


    public fragment_no_active_order fragment;


    public NoActive_GetDriverOrderStatus(fragment_no_active_order fragment) {
        this.fragment = fragment;

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


        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            orderDCM = orderDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        requestUpdateStatusModel.idDriver = 0;

        requestUpdateStatusModel.address = sessionDCM.defaultAddress;
        requestUpdateStatusModel.longitude = String.valueOf(sessionDCM.longitude2);
        requestUpdateStatusModel.latitude = String.valueOf(sessionDCM.latitude2);
        requestUpdateStatusModel.idOrderMaster = orderDCM.idOrderMaster;

        fragment.canGetOrderStatusFlag = false;

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestUpdateStatusModel));
        String str = helperSP.requestHTTPResponse(APILinks.APIGetDriverOrderStatus, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseUpdateStatusDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseUpdateStatusDataModel.Data);
            responseUpdateStatusModel = gson.fromJson(str2, APIGetDriverOrderStatusResponseModel.class);
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fragment.canGetOrderStatusFlag = true;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            if (responseUpdateStatusModel != null) {
                if (responseUpdateStatusModel.errorLogId == 0) {


                    if (responseUpdateStatusModel.orderStatus != null&&responseUpdateStatusModel.idUser!=sessionDCM.idUser) {


                        if (!responseUpdateStatusModel.orderStatus.equals(FixLabels.OrderStatus.WaitingForDriver)) {
                            fragment.canGetOrderStatusFlag = false;


                            if (responseUpdateStatusModel.orderStatus.equals(FixLabels.OrderStatus.OrderCanceled)) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.context)
                                        .setTitle(FixLabels.alertDefaultTitle)
                                        .setCancelable(false)
                                        .setMessage("Sorry,Order was canceled by customer.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                fragment.pbCircular.setVisibility(View.VISIBLE);
                                                fragment.canGetNewOrder = true;
                                                fragment.flAcceptRejectDialog.setVisibility(View.INVISIBLE);
                                                if (fragment.timer2 != null) {
                                                    fragment.timer2.removeCallbacksAndMessages(null);
                                                }
                                                fragment.timer2 = null;
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert);
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.context)
                                        .setTitle(FixLabels.alertDefaultTitle)
                                        .setCancelable(false)
                                        .setMessage("Sorry,Order was assigned to someone else.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                fragment.pbCircular.setVisibility(View.VISIBLE);
                                                fragment.canGetNewOrder = true;
                                                fragment.flAcceptRejectDialog.setVisibility(View.INVISIBLE);
                                                if (fragment.timer2 != null) {
                                                    fragment.timer2.removeCallbacksAndMessages(null);
                                                }
                                                fragment.timer2 = null;
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert);
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            }

                            orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            orderDAO.deleteAll();

                        } else {
                            fragment.canGetOrderStatusFlag = true;
                        }

                    }
                    else
                    {
                        fragment.canGetOrderStatusFlag = true;
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + responseUpdateStatusModel.errorLogId + "\n errorURL :" + responseUpdateStatusModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    fragment.canGetOrderStatusFlag = false;
                                    String url = responseUpdateStatusModel.errorURL;
                                    Intent i = new Intent(context, activity_error_webview.class);
                                    i.putExtra("url", url);
                                    fragment.context.startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + responseUpdateStatusModel.errorLogId + "\n errorURL :" + responseUpdateStatusModel.errorURL);
                }
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragment.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fragment.getActivity().finish();
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