package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIMarkOrderCompletedRequestModel;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIMarkOrderCompletedResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;

import java.sql.SQLException;
import java.util.HashMap;

public class MarkOrderCompletedAsync extends AsyncTask<Void, Void, Boolean> {

    public Context context;
    public CustomProgressBar progressDialog;
    public APIMarkOrderCompletedRequestModel requestUpdateStatusModel=new APIMarkOrderCompletedRequestModel();
    public BaseAPIResponseModel<APIMarkOrderCompletedResponseModel> responseUpdateStatusDataModel = new BaseAPIResponseModel<APIMarkOrderCompletedResponseModel>();
    public APIMarkOrderCompletedResponseModel responseUpdateStatusModel = new APIMarkOrderCompletedResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM=new SessionDCM();

  

    public OrderDCM orderDCM=new OrderDCM();
    public OrderDAO orderDAO;

    public PickupPointDCM pickupPointDCM;
    public PickupPointsDAO pickupPointDAO;

    public fragment_activity_landing_driver fActivity;




    public MarkOrderCompletedAsync() {
        
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

        requestUpdateStatusModel = new APIMarkOrderCompletedRequestModel();
        requestUpdateStatusModel.idUser=sessionDCM.idUser;
//        progressDialog = ProgressDialog.show(fActivity, "", "", true, false);

        progressDialog = CustomProgressBar.show(fActivity.context,"", true,false,null);

    }
        @Override
        protected Boolean doInBackground(Void... voids) {
            LionUtilities helperSP = new LionUtilities();
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("JsonString", gson.toJson(requestUpdateStatusModel));
            String str = helperSP.requestHTTPResponse(APILinks.APIMarkOrderCompleted, map, "POST", false);
            if (str.equals("")) {
                return false;
            } else {
                responseUpdateStatusDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
                String str2 = gson.toJson(responseUpdateStatusDataModel.Data);
                responseUpdateStatusModel = gson.fromJson(str2, APIMarkOrderCompletedResponseModel.class);
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
                if (responseUpdateStatusModel != null) {
                    if (responseUpdateStatusModel.errorLogId == 0) {



                        if (responseUpdateStatusModel.EmailID!=null ) {


                            sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                            sessionDCM = new SessionDCM();
                            sessionDAO.deleteAll();
//                            sessionDAO.create(responseModel.);
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
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