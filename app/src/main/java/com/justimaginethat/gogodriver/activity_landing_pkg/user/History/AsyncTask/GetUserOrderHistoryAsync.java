package com.justimaginethat.gogodriver.activity_landing_pkg.user.History.AsyncTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetUserOrderHistoryRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetUserOrderHistoryResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.MasterModel;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.History.activity_history;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetUserOrderHistoryAsync extends AsyncTask<Void, Void, Boolean> {
    public CustomProgressBar progressDialog;
    public APIGetUserOrderHistoryRequestModel requestHistoryModel = new APIGetUserOrderHistoryRequestModel();
    public BaseAPIResponseModel<APIGetUserOrderHistoryResponseModel> responseHistoryDataModel = new BaseAPIResponseModel<APIGetUserOrderHistoryResponseModel>();
    public APIGetUserOrderHistoryResponseModel responseHistoryOrderModel = new APIGetUserOrderHistoryResponseModel();

    public activity_history activityHistory;

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();

    public OrderDCM orderDCM = new OrderDCM();
    public OrderDAO orderDAO;
    public List<MasterModel> orderDCMList = new ArrayList<>();
    public String date;

    LionUtilities lionUtilities=new LionUtilities();


    public GetUserOrderHistoryAsync(activity_history activityHistory, String date) {
        this.activityHistory = activityHistory;
        this.date = date;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressDialog = ProgressDialog.show(activityHistory, "", "", true, false);

        progressDialog = CustomProgressBar.show(activityHistory.context,"", true,false,null);
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        requestHistoryModel = new APIGetUserOrderHistoryRequestModel();
        requestHistoryModel.idUser = sessionDCM.idUser;
        try {
            requestHistoryModel.orderDate=lionUtilities.getStringFromDate(lionUtilities.getDateFromString(date, FixLabels.dateFormat), FixLabels.serverDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        requestHistoryModel.isDriver = sessionDCM.isDriver;

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestHistoryModel));
        String str = helperSP.requestHTTPResponse(APILinks.APIGetUserOrderHistory, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseHistoryDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseHistoryDataModel.Data);
            responseHistoryOrderModel = gson.fromJson(str2, APIGetUserOrderHistoryResponseModel.class);
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {
            new AlertDialog.Builder(activityHistory)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            activityHistory.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            if (responseHistoryOrderModel != null) {
                if (responseHistoryOrderModel.errorLogId == 0) {


                    if (responseHistoryOrderModel.orderList != null) {

                        orderDCMList = new ArrayList<MasterModel>();
                        orderDCMList.addAll(responseHistoryOrderModel.orderList);
                        activityHistory.orderDCMList=new ArrayList<>();
                        activityHistory.orderDCMList.addAll(responseHistoryOrderModel.orderList);

                        activityHistory.recyclerView.setAdapter(activityHistory.orderListAdapter);
                        activityHistory.recyclerView.setLayoutManager(new LinearLayoutManager(activityHistory.context));
                        activityHistory.orderListAdapter.notifyDataSetChanged();

                        if(activityHistory.orderDCMList.size() == 0)
                        {
                            activityHistory.empty_view.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            activityHistory.empty_view.setVisibility(View.GONE);
                        }

//                        activityHistory.orderDCMLists.addAll(orderDCMList.get(0).order);
//                        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
//                        orderDAO.create(orderDCMList.get(0).order);
//


//                        try {
//                            orderDAO.getAll();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }


                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activityHistory)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Server Error Log : " + responseHistoryOrderModel.errorLogId + "\n errorURL :" + responseHistoryOrderModel.errorURL)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = responseHistoryOrderModel.errorURL;
                                        Intent i = new Intent(activityHistory, activity_error_webview.class);
                                        i.putExtra("url", url);
                                        activityHistory.startActivity(i);
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                        Log.i("PayBit", "APILogin Failed from server Error Log : " + responseHistoryOrderModel.errorLogId + "\n errorURL :" + responseHistoryOrderModel.errorURL);
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activityHistory)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    activityHistory.finish();
                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                }
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }
}