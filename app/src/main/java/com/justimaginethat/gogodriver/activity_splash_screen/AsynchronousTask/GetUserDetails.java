package com.justimaginethat.gogodriver.activity_splash_screen.AsynchronousTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIGetUserDetailsRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIGetUserDetailsResponseModel;
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
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_splash_screen.SplashScreenActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetUserDetails extends AsyncTask<Void, Void, Boolean> {

    //    public Context context;
    public CustomProgressBar progressDialog;
    public APIGetUserDetailsRequestModel requestGetUserDetailsModel = new APIGetUserDetailsRequestModel();
    public BaseAPIResponseModel<APIGetUserDetailsResponseModel> responseGetUserDetailsDataModel = new BaseAPIResponseModel<APIGetUserDetailsResponseModel>();
    public APIGetUserDetailsResponseModel responseGetUserDetailsModel = new APIGetUserDetailsResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();


    public OrderDCM orderDCM = new OrderDCM();
    public OrderDAO orderDAO;

    public PickupPointDCM pickupPointDCM;
    public PickupPointsDAO pickupPointDAO;
    private List<SessionDCM> sessionDCMs = new ArrayList<SessionDCM>();


    public SplashScreenActivity fActivity;


    public GetUserDetails(SplashScreenActivity fActivity) {
        this.fActivity = fActivity;
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

        requestGetUserDetailsModel = new APIGetUserDetailsRequestModel();
        requestGetUserDetailsModel.idUser = sessionDCM.idUser;
//      progressDialog = ProgressDialog.show(fActivity, "", "", true, false);
        progressDialog = CustomProgressBar.show(fActivity.context, "", true, false, null);

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestGetUserDetailsModel));
        String str = helperSP.requestHTTPResponse(APILinks.APIGetUserDetails, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseGetUserDetailsDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseGetUserDetailsDataModel.Data);
            responseGetUserDetailsModel = gson.fromJson(str2, APIGetUserDetailsResponseModel.class);
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
                            fActivity.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            if (responseGetUserDetailsModel != null) {
                if (responseGetUserDetailsModel.errorLogId == 0) {


                    if (responseGetUserDetailsModel.user != null) {
                        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        sessionDCM = new SessionDCM();
                        sessionDAO.deleteAll();
                        sessionDAO.create(responseGetUserDetailsModel.user);
                        sessionDCMs = new ArrayList<>();
                        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        try {
                            sessionDCMs = sessionDAO.getAll();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        sessionDAO.create(sessionDCMs);
                        if (sessionDCMs.get(0).isDriver) {

                            Intent intent_home_screen = new Intent(fActivity.context, fragment_activity_landing_driver.class);
                            fActivity.startActivity(intent_home_screen);
                            fActivity.finish();
                        } else if (sessionDCMs.get(0).isCustomer) {
                            Intent intent_home_screen = new Intent(fActivity.context, fragment_activity_landing_user.class);
                            fActivity.startActivity(intent_home_screen);
                            fActivity.finish();
                        }
//                        else {
//                            Intent intent_home_screen = new Intent(cardActivity, activity_login.class);
//                            cardActivity.startActivity(intent_home_screen);
//                            cardActivity.finish();
//                        }


                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setMessage("Not active.")
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + responseGetUserDetailsModel.errorLogId + "\n errorURL :" + responseGetUserDetailsModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = responseGetUserDetailsModel.errorURL;
                                    Intent i = new Intent(fActivity.context, activity_error_webview.class);
                                    i.putExtra("url", url);
                                    fActivity.startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + responseGetUserDetailsModel.errorLogId + "\n errorURL :" + responseGetUserDetailsModel.errorURL);
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
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
    }

}