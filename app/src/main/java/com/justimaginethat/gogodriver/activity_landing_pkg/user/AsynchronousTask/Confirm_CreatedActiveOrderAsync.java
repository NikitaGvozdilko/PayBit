package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APICreateNewOrderRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APICreateNewOrderResponseModel;
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
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;

import java.sql.SQLException;
import java.util.HashMap;

public class Confirm_CreatedActiveOrderAsync extends AsyncTask<Void, Void, Boolean> {
    public CustomProgressBar progressDialog;
    public APICreateNewOrderRequestModel newOrderRequestModel = new APICreateNewOrderRequestModel();
    public BaseAPIResponseModel<APICreateNewOrderResponseModel> newOrderResponseDataModel = new BaseAPIResponseModel<APICreateNewOrderResponseModel>();
    public APICreateNewOrderResponseModel newOrderResponseModel = new APICreateNewOrderResponseModel();
    public OrderDCM orderDCM = new OrderDCM();
    private OrderDAO orderDAO;
    PickupPointDCM pickupPointDetails = new PickupPointDCM();
    PickupPointsDAO pickupPointDAO;
    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();

    public Confirm_CreatedActiveOrderAsync(fragment_layout_confirm fragmentLayoutConform) {
        this.fragmentLayoutConform = fragmentLayoutConform;
    }

    public fragment_layout_confirm fragmentLayoutConform;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        pickupPointDAO = new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            pickupPointDetails = pickupPointDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        newOrderRequestModel = new APICreateNewOrderRequestModel();
        newOrderRequestModel.newOrder.idUserCustomer = sessionDCM.idUser;


        newOrderRequestModel.newOrder.idDeliveryPickupPoints = pickupPointDetails.idDeliveryPickupPoints;
        newOrderRequestModel.newOrder.isActive = true;
        newOrderRequestModel.newOrder.isDeleted = false;
        newOrderRequestModel.newOrder.orderStatus = FixLabels.OrderStatus.WaitingForDriver;
        newOrderRequestModel.newOrder.deliveryAddress = sessionDCM.defaultAddress;
        newOrderRequestModel.newOrder.deliveryLatitude = sessionDCM.defaultLatitude;
        newOrderRequestModel.newOrder.deliveryLongitude = sessionDCM.defaultLongitude;
        newOrderRequestModel.newOrder.deliveryBuildingName = sessionDCM.defaultBuildingName;
        newOrderRequestModel.newOrder.deliveryFloorNumber = sessionDCM.defaultFloorNumber;
        newOrderRequestModel.idUser = sessionDCM.idUser;


//            progressDialog = ProgressDialog.show(fragmentLayoutConform.context, "", "", true, false);

        progressDialog = CustomProgressBar.show(fragmentLayoutConform.context, "", true, false, null);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(newOrderRequestModel));
        String str = helperSP.requestHTTPResponse(APILinks.APICreateNewOrder, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            newOrderResponseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(newOrderResponseDataModel.Data);
            newOrderResponseModel = gson.fromJson(str2, APICreateNewOrderResponseModel.class);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            fragmentLayoutConform.canCheckNearbyDriverFlag = true;
                            fragmentLayoutConform.canGetOrderStatusFlag = false;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            if (newOrderResponseModel != null) {
                if (newOrderResponseModel.errorLogId == 0) {

                    if (newOrderResponseModel.createdActiveOrder != null) {
                        ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(fragmentLayoutConform.pbCircular,
                                "rotation", 0f, 360f);
                        processObjectAnimator.setDuration(1000);
                        processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                        processObjectAnimator.setInterpolator(new LinearInterpolator());
                        processObjectAnimator.start();

                        orderDCM = new OrderDCM();
                        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        orderDAO.deleteAll();
                        orderDAO.create(newOrderResponseModel.createdActiveOrder);


                        fragmentLayoutConform.isConfirmClick = true;
                        fragmentLayoutConform.canCheckNearbyDriverFlag = false;
                        fragmentLayoutConform.canGetOrderStatusFlag = true;
                        fragmentLayoutConform.canGetOrderAmountRepeatFlag = false;
                        fragmentLayoutConform.updateViewGetForDriver();

                    } else {
                        fragmentLayoutConform.canCheckNearbyDriverFlag = true;
                        fragmentLayoutConform.canGetOrderStatusFlag = false;


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)

                                .setMessage("Create Order failed!")
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
                    fragmentLayoutConform.canCheckNearbyDriverFlag = true;
                    fragmentLayoutConform.canGetOrderStatusFlag = false;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Server Error Log : " + newOrderResponseModel.errorLogId + "\n errorURL :" + newOrderResponseModel.errorURL)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = newOrderResponseModel.errorURL;
                                    Intent i = new Intent(fragmentLayoutConform.context, activity_error_webview.class);
                                    i.putExtra("url", url);
                                    fragmentLayoutConform.startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    Log.i("PayBit", "APILogin Failed from server Error Log : " + newOrderResponseModel.errorLogId + "\n errorURL :" + newOrderResponseModel.errorURL);
                }
            } else {
                fragmentLayoutConform.canCheckNearbyDriverFlag = true;
                fragmentLayoutConform.canGetOrderStatusFlag = false;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentLayoutConform.getActivity().finish();
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