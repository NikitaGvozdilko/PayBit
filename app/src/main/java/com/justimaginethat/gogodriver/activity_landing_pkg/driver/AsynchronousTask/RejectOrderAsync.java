package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIAcceptRejectOrdersRequestModel;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIAcceptRejectOrdersResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_no_active_order;

import java.sql.SQLException;
import java.util.HashMap;

public class RejectOrderAsync extends AsyncTask<Void, Void, Boolean> {

    //    public Context context;
    public CustomProgressBar progressDialog;
    public APIAcceptRejectOrdersRequestModel requestRejectModel = new APIAcceptRejectOrdersRequestModel();
    public BaseAPIResponseModel<APIAcceptRejectOrdersResponseModel> responseRejectDataModel = new BaseAPIResponseModel<APIAcceptRejectOrdersResponseModel>();
    public APIAcceptRejectOrdersResponseModel responseRejectOrderModel = new APIAcceptRejectOrdersResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();
    OrderDCM orderDCM = new OrderDCM();
    OrderDAO orderDAO;
    public fragment_no_active_order fragmentNoActiveOrder;


    public RejectOrderAsync(fragment_no_active_order fragmentNoActiveOrder) {
        this.fragmentNoActiveOrder = fragmentNoActiveOrder;
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


        requestRejectModel = new APIAcceptRejectOrdersRequestModel();
        requestRejectModel.idDriver = sessionDCM.idUser;
        requestRejectModel.idOrderMaster = orderDCM.idOrderMaster;
        requestRejectModel.orderStatus = fragmentNoActiveOrder.oStatus;

//        progressDialog = ProgressDialog.show(fragmentNoActiveOrder.context, "", "", true, false);
        progressDialog = CustomProgressBar.show(fragmentNoActiveOrder.context, "", true, false, null);
        if(fragmentNoActiveOrder.oStatus.equals("Rejected"))
        {
            orderDAO.deleteAll();
        }

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestRejectModel));
        String str = helperSP.requestHTTPResponse(APILinks.APIAcceptRejectOrders, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseRejectDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseRejectDataModel.Data);
            responseRejectOrderModel = gson.fromJson(str2, APIAcceptRejectOrdersResponseModel.class);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentNoActiveOrder.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentNoActiveOrder.fActivity.callWorkOnAppResume = true;

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            if (responseRejectOrderModel != null) {
                if (responseRejectOrderModel.errorLogId == 0) {
                    if (responseRejectOrderModel.idOrderMaster > 0) {

                        if (responseRejectOrderModel.orderStatus.equals(FixLabels.OrderStatus.DriverHeadingToPickUpPoint)) {

                            fragmentNoActiveOrder.setOfflineUI();
                            if (fragmentNoActiveOrder.fActivity.appInBackGround == false) {
                                try {
                                    fragmentNoActiveOrder.fActivity.showLayout(responseRejectOrderModel.orderStatus);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    fragmentNoActiveOrder.fActivity.finish();
                                }
                            } else {
                                fragmentNoActiveOrder.fActivity.callWorkOnAppResume = true;
                            }


                        } else if (responseRejectOrderModel.orderStatus.equals(FixLabels.OrderStatus.OrderCanceled)) {
                            fragmentNoActiveOrder.setOfflineUI();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentNoActiveOrder.context)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("Sorry,Order was rejected!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            orderDAO.deleteAll();
                                            fragmentNoActiveOrder.clickEvent();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();


                        } else if (responseRejectOrderModel.orderStatus.equals(FixLabels.OrderStatus.WaitingForDriver)) {
                            fragmentNoActiveOrder.clickEvent();
                        }


                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentNoActiveOrder.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + responseRejectOrderModel.errorLogId + "\n errorURL :" + responseRejectOrderModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = responseRejectOrderModel.errorURL;
                                    Intent i = new Intent(fragmentNoActiveOrder.context, activity_error_webview.class);
                                    i.putExtra("url", url);
                                    fragmentNoActiveOrder.context.startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + responseRejectOrderModel.errorLogId + "\n errorURL :" + responseRejectOrderModel.errorURL);
                }
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentNoActiveOrder.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                fragmentNoActiveOrder.getActivity().finish();


                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }

    }
}