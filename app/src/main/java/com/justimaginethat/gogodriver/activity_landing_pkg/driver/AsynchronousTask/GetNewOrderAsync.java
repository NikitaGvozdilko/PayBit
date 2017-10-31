package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIGetNewOrderRequestModel;
import com.justimaginethat.gogodriver.APIModels.DriverWorkspace.APIGetNewOrderResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_error_webview_pkg.activity_error_webview;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_no_active_order;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetNewOrderAsync extends AsyncTask<Void, Void, Boolean> {

//    public Context context;
    public ProgressDialog progressDialog;
    public APIGetNewOrderRequestModel requestGetNewOrderModel = new APIGetNewOrderRequestModel();
    public BaseAPIResponseModel<APIGetNewOrderResponseModel> responseGetNewOrderDataModel = new BaseAPIResponseModel<APIGetNewOrderResponseModel>();
    public APIGetNewOrderResponseModel responseGetNewOrderModel = new APIGetNewOrderResponseModel();

    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();

    public UserDCM userDCM = new UserDCM();
    public UserDAO userDAO;


    public OrderDCM orderDCM = new OrderDCM();
    public OrderDAO orderDAO;

    public List<PickupPointDCM> pickupPointList = new ArrayList<>();
    public PickupPointsDAO pickupPointDAO;
    public PickupPointDCM pickupPointDCM;


    public fragment_no_active_order fActivity;
    private List<UserDCM> userDCMList = new ArrayList<>();


    public GetNewOrderAsync(fragment_no_active_order fActivity) {
        this.fActivity = fActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fActivity.canGetNewOrder = false;
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        requestGetNewOrderModel = new APIGetNewOrderRequestModel();
        requestGetNewOrderModel.idDriver = sessionDCM.idUser;
        requestGetNewOrderModel.latitude = fActivity.mLastLocation.getLatitude();
        requestGetNewOrderModel.longitude = fActivity.mLastLocation.getLongitude();
        requestGetNewOrderModel.address = fActivity.addressDetail;
        requestGetNewOrderModel.heading = fActivity.heading;


        // float bearing = fActivity.mLastLocation.bearingTo(fActivity.mLastLocation);    // -180 to 180


//        progressDialog = ProgressDialog.show(FRequestPayment.getContext(), "", "", true, false);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(requestGetNewOrderModel));
        String str = helperSP.requestHTTPResponse(APILinks.APIGetNewOrder, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            responseGetNewOrderDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(responseGetNewOrderDataModel.Data);
            responseGetNewOrderModel = gson.fromJson(str2, APIGetNewOrderResponseModel.class);
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {

            fActivity.canGetNewOrder = true;
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
//                    .setTitle(FixLabels.alertDefaultTitle)
//                    .setCancelable(false)
//                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
//                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert);
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setCanceledOnTouchOutside(false);
//            alertDialog.setCancelable(false);
//            alertDialog.show();
            Log.d(FixLabels.alertDefaultTitle,"No Response From Server");
        } else {
            if (responseGetNewOrderModel != null) {
                if (responseGetNewOrderModel.errorLogId == 0) {
                    if (responseGetNewOrderModel.order != null) {
                        userDAO = new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        userDCM = new UserDCM();
                        userDCM = responseGetNewOrderModel.user;
                        userDAO.deleteAll();
                        userDAO.create(userDCM);
                        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        orderDCM = new OrderDCM();
                        orderDCM = responseGetNewOrderModel.order;
                        orderDAO.deleteAll();
                        orderDAO.create(orderDCM);
                        pickupPointDAO = new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        pickupPointDCM = new PickupPointDCM();
                        pickupPointDCM = responseGetNewOrderModel.orderPickupPoint;
                        pickupPointDAO.deleteAll();
                        pickupPointDAO.create(pickupPointDCM);
//                          FRequestPayment.FRequestPayment.showLayout(responseGetNewOrderModel.order.orderStatus);
                        try {
                            userDCMList = userDAO.getAll();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        for (UserDCM userDCMs : userDCMList
                                ) {
                            fActivity.lblCustomerName.setText(userDCMs.firstName);
                            fActivity.lblCustomerAddress.setText(userDCMs.defaultAddress);
                        }
                        try {
                            pickupPointList = pickupPointDAO.getAll();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        for (PickupPointDCM pickupPointDCMs : pickupPointList) {
                            String city="";
                            try {
                                 city=pickupPointDCMs.pickupAddress.split(",")[1];
                            } catch (Exception e) {
                                city=pickupPointDCMs.pickupAddress;
                                e.printStackTrace();
                            }
                            try {
                                fActivity.lblPickupAddress.setText("Pickup Location: "+pickupPointDCMs.title+" "+city);
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                        fActivity.pbCircular.setVisibility(View.GONE);
                        if (fActivity.timer2 != null) {
                            fActivity.timer2.removeCallbacksAndMessages(null);

                        }
                        fActivity.timer2 = null;

                        LionUtilities util = new LionUtilities();
                        Date d = new Date();
                        long startDate = 0;
                        try {
                            d = util.getDateFromString(orderDCM.entryDate,FixLabels.serverDateFormat);
                            startDate =  (d.getTime() + 1000 * 180);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long diffInMs = startDate - new Date().getTime() ;
                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

                        if(diffInSec >= 61)
                        {
                            fActivity.timerCount = 61;
                        }
                        else
                        {
                           fActivity.timerCount = (int) diffInSec;
                        }

                        fActivity.showAcceptRejectBox();
                        fActivity.canGetNewOrder = false;
                        fActivity.canGetOrderStatusFlag = true;
                    } else {
                        fActivity.canGetOrderStatusFlag = false;
                        fActivity.canGetNewOrder = true;
                    }


                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + responseGetNewOrderModel.errorLogId + "\n errorURL :" + responseGetNewOrderModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = responseGetNewOrderModel.errorURL;
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
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + responseGetNewOrderModel.errorLogId + "\n errorURL :" + responseGetNewOrderModel.errorURL);
                }
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fActivity.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fActivity.getActivity().finish();


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