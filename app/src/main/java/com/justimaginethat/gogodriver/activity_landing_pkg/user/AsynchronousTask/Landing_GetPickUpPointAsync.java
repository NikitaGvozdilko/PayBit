package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetPickUpPointsRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetPickUpPointsResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.adapter.PickupListAdapter;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Landing_GetPickUpPointAsync extends AsyncTask<Void, Void, Boolean> {

    public CustomProgressBar progressDialog;

    String str2;
    public fragment_activity_landing_user fActivity;
    public APIGetPickUpPointsRequestModel requestModel;
    String strGetPickup = "";
    String ListPicData = "";
    HashMap<String, String> map;
    private BaseAPIResponseModel responseDataModel;
    private ArrayList<PickupPointDCM> data;
    APIGetPickUpPointsResponseModel responseModelList;
    private OrderDCM orderDCM;
    private OrderDAO orderDAO;
    private DriverDCM orderDriver;
    private DriverDAO driverDAO;
    private PickupPointDCM orderPickupPoint;
    private PickupPointsDAO orderPickupPointDAO;

    public SessionDCM sessionDCM;
    public SessionDAO sessionDAO;


    private PickupListAdapter picListAdapter;
    fragment_layout_confirm conformLayout;

    public Landing_GetPickUpPointAsync(fragment_activity_landing_user fActivity) {
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
        requestModel = new APIGetPickUpPointsRequestModel();
        requestModel.gcmId = sessionDCM.gcmId;
        requestModel.mobileNumber = sessionDCM.mobileNumber;
        requestModel.emailAddress = sessionDCM.emailAddress;
        requestModel.idUser = sessionDCM.idUser;

//            progressDialog = new ProgressDialog(fActivity);
//            progressDialog = ProgressDialog.show(fActivity, "", "", true, false);
        progressDialog = CustomProgressBar.show(fActivity.context, "", true, false, null);


    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities jb = new LionUtilities();
        Gson gson = new Gson();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("JsonString", gson.toJson(requestModel));

        String str = jb.requestHTTPResponse(APILinks.APIPickupPoint, map, "POST", true);

        if (str.equals("")) {
            return false;
        } else {

            responseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            str2 = gson.toJson(responseDataModel.Data);
            responseModelList = gson.fromJson(str2, APIGetPickUpPointsResponseModel.class);
            Log.i("responseModelList ", String.valueOf(responseModelList));
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        conformLayout = new fragment_layout_confirm();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (response == false) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            fActivity.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {

            if (response != null) {
                if (responseModelList.errorLogId <= 0) {
                    orderDCM = new OrderDCM();
                    orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    orderDAO.deleteAll();


//                        sessionDCM=new SessionDCM();
//                        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
//                        sessionDAO.deleteAll();

                    FixLabels.pendingAmount = responseModelList.pendingAmount;
                    orderDriver = new DriverDCM();
                    driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    driverDAO.deleteAll();
                    orderPickupPoint = new PickupPointDCM();
                    orderPickupPointDAO = new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    orderPickupPointDAO.deleteAll();
                    if (responseModelList.activeOrder != null) {
                        orderDAO.create(responseModelList.activeOrder);
                        driverDAO.create(responseModelList.orderDriver);
                        orderPickupPointDAO.create(responseModelList.orderPickupPoint);
                        FixLabels.sessionDatabase.deliveryPickupPointsList = responseModelList.PickupPointList;
                        try {
                            fActivity.showLayout(responseModelList.activeOrder.orderStatus);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (responseModelList.PickupPointList != null) {
//                                orderPickupPointDAO.create(responseModelList.orderPickupPoint);
                            FixLabels.sessionDatabase.deliveryPickupPointsList = responseModelList.PickupPointList;
                            try {
                                fActivity.showLayout(FixLabels.OrderStatus.NoActiveOrder);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {
                    Log.i("Tesssssssttttttt", " Failed from server Error Log : " + responseModelList.errorLogId + "\n errorURL :" + responseModelList.errorURL);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(responseModelList.errorURL));
                    fActivity.startActivity(i);
                }
            } else {
                Log.i("Tesstsssssssgss", " Failed from server Error Log : " + responseModelList.errorLogId + "\n errorURL :" + responseModelList.errorURL);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(responseModelList.errorURL));
                fActivity.startActivity(i);
            }
        }


    }


}