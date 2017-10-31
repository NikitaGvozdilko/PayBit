package com.justimaginethat.gogodriver.activity_settings.AsynchronousTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;
import com.justimaginethat.gogodriver.activity_settings.activity_update_setting;

import java.sql.SQLException;
import java.util.HashMap;

public class SignOutAsync extends AsyncTask<Void, Void, Boolean> {

//    public Context context;
//    public ProgressDialog progressDialog;
    public APIChangeWorkStatusRequestModel requestChangeWorkModel=new APIChangeWorkStatusRequestModel();
    public BaseAPIResponseModel<APIChangeWorkStatusResponseModel> responseChangeWorkDataModel = new BaseAPIResponseModel<APIChangeWorkStatusResponseModel>();
    public APIChangeWorkStatusResponseModel responseChangeWorkModel = new APIChangeWorkStatusResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();
    public activity_update_setting activity;

    public SignOutAsync(activity_update_setting activity) {
        this.activity=activity;

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
        requestChangeWorkModel = new APIChangeWorkStatusRequestModel();
        requestChangeWorkModel.idDriver=sessionDCM.idUser;
        requestChangeWorkModel.workStatus=false;
//        progressDialog = ProgressDialog.show(activity, "", "", true, false);
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
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            if (response == false) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity.context)
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
                if (responseChangeWorkModel != null) {
                    if (responseChangeWorkModel.errorLogId == 0) {
                        if (responseChangeWorkModel.idDriver == requestChangeWorkModel.idDriver && responseChangeWorkModel.workStatus == requestChangeWorkModel.workStatus) {





                            activity.logout();
                            activity.finish();





                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Server Error Log : " + responseChangeWorkModel.errorLogId + "\n errorURL :" + responseChangeWorkModel.errorURL)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = responseChangeWorkModel.errorURL;
                                        Intent i = new Intent( activity.context, activity_error_webview.class);
                                        i.putExtra("url", url);
                                        activity.context.startActivity(i);
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        Log.i("PayBit", "APILogin Failed from server Error Log : " + responseChangeWorkModel.errorLogId + "\n errorURL :" + responseChangeWorkModel.errorURL);
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( activity.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

//                                    DriverHeadingToPickup.finish();

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