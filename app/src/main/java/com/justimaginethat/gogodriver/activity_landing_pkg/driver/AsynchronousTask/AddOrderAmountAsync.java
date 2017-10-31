package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIAddOrderAmountRequestModel;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIAddOrderAmountResponseModel;
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
import com.justimaginethat.gogodriver.activity_forgot_password_pkg.activity_forgot_password;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_request_payment;

import java.sql.SQLException;
import java.util.HashMap;

public class AddOrderAmountAsync extends AsyncTask<Void, Void, Boolean> {

    public Context context;
    public CustomProgressBar progressDialog;
    public APIAddOrderAmountRequestModel requestAmountModel = new APIAddOrderAmountRequestModel();
    public BaseAPIResponseModel<APIAddOrderAmountResponseModel> responseAmountDataModel = new BaseAPIResponseModel<APIAddOrderAmountResponseModel>();
    public APIAddOrderAmountResponseModel responseAmountOrderModel = new APIAddOrderAmountResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();
    OrderDCM orderDCM = new OrderDCM();
    OrderDAO orderDAO;
    public fragment_request_payment FRequestPayment;

    public AddOrderAmountAsync(fragment_request_payment FRequestPayment) {
        this.FRequestPayment = FRequestPayment;
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

        requestAmountModel = new APIAddOrderAmountRequestModel();
        requestAmountModel.idDriver = sessionDCM.idUser;
        requestAmountModel.idOrderMaster = orderDCM.idOrderMaster;
        requestAmountModel.orderAmount = Double.parseDouble(FRequestPayment.paymentRate);
        progressDialog = CustomProgressBar.show(FRequestPayment.fActivity, "", true, true, null);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestAmountModel));
        String str = helperSP.requestHTTPResponse(APILinks.APIAddOrderAmount, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseAmountDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseAmountDataModel.Data);
            responseAmountOrderModel = gson.fromJson(str2, APIAddOrderAmountResponseModel.class);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FRequestPayment.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            if (responseAmountOrderModel != null) {
                if (responseAmountOrderModel.errorLogId == 0) {
                    if (responseAmountOrderModel.idOrderMaster > 0) {

                        if (responseAmountOrderModel.orderStatus.equals(FixLabels.OrderStatus.PleaseConfirmPayment)) {
                            FRequestPayment.fActivity.showLayout(responseAmountOrderModel.orderStatus);
                            FRequestPayment.hideKeyboard();
                        } else if (responseAmountOrderModel.orderStatus.equals(FixLabels.OrderStatus.OrderCanceled)) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FRequestPayment.fActivity)
                                    .setTitle(FixLabels.alertDefaultTitle)
                                    .setCancelable(false)
                                    .setMessage("Sorry,Customer Canceled Order!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            FRequestPayment.hideKeyboard();
                                            FRequestPayment.fActivity.showLayout(responseAmountOrderModel.orderStatus);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();

                        }


                        //code here...
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FRequestPayment.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + responseAmountOrderModel.errorLogId + "\n errorURL :" + responseAmountOrderModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = responseAmountOrderModel.errorURL;
                                    Intent i = new Intent(context, activity_error_webview.class);
                                    i.putExtra("url", url);
                                    FRequestPayment.fActivity.startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + responseAmountOrderModel.errorLogId + "\n errorURL :" + responseAmountOrderModel.errorURL);
                }
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FRequestPayment.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                FRequestPayment.fActivity.finish();
//                                    DriverHeadingToPickup.finish();


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