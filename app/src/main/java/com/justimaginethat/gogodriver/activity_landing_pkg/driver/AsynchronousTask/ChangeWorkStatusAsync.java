package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIChangeWorkStatusRequestModel;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIChangeWorkStatusResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_no_active_order;

import java.sql.SQLException;
import java.util.HashMap;

public class ChangeWorkStatusAsync extends AsyncTask<Void, Void, Boolean> {


    public CustomProgressBar progressDialog;
    public APIChangeWorkStatusRequestModel requestChangeWorkModel = new APIChangeWorkStatusRequestModel();
    public BaseAPIResponseModel<APIChangeWorkStatusResponseModel> responseChangeWorkDataModel = new BaseAPIResponseModel<APIChangeWorkStatusResponseModel>();
    public APIChangeWorkStatusResponseModel responseChangeWorkModel = new APIChangeWorkStatusResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();
    public fragment_no_active_order fragmentDriver;

    public ChangeWorkStatusAsync(fragment_no_active_order fragmentDriver) {

        this.fragmentDriver = fragmentDriver;

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
        requestChangeWorkModel = new APIChangeWorkStatusRequestModel();
        requestChangeWorkModel.idDriver = sessionDCM.idUser;
        requestChangeWorkModel.workStatus = fragmentDriver.workStatus;
//        progressDialog = ProgressDialog.show(fragmentDriver.context, "", "", true, false);
        progressDialog = CustomProgressBar.show(fragmentDriver.context, "", true, false, null);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestChangeWorkModel));
        String str = helperSP.requestHTTPResponse(APILinks.APIChangeWorkStatus, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseChangeWorkDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseChangeWorkDataModel.Data);
            responseChangeWorkModel = gson.fromJson(str2, APIChangeWorkStatusResponseModel.class);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentDriver.context)
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
            if (responseChangeWorkModel != null) {
                if (responseChangeWorkModel.errorLogId == 0) {
                    if (responseChangeWorkModel.idDriver == requestChangeWorkModel.idDriver && responseChangeWorkModel.workStatus == requestChangeWorkModel.workStatus) {
                        if (responseChangeWorkModel.workStatus == true) {
                            fragmentDriver.setOnlineUI();
                            fragmentDriver.callAsynchronousTask();
                        } else {
                            fragmentDriver.setOfflineUI();
                            fragmentDriver.timer.removeCallbacksAndMessages(null);

                        }

                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentDriver.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + responseChangeWorkModel.errorLogId + "\n errorURL :" + responseChangeWorkModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = responseChangeWorkModel.errorURL;
                                    Intent i = new Intent(fragmentDriver.context, activity_error_webview.class);
                                    i.putExtra("url", url);
                                    fragmentDriver.startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + responseChangeWorkModel.errorLogId + "\n errorURL :" + responseChangeWorkModel.errorURL);
                }
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentDriver.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

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