package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderStatusRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderStatusResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;

import java.sql.SQLException;
import java.util.HashMap;


public class Confirm_GetOrderStatusAsync extends AsyncTask<Void, Void, Boolean> {


    public fragment_layout_confirm fragmentLayoutConform;


    public Confirm_GetOrderStatusAsync(fragment_layout_confirm fragmentLayoutConform) {
        this.fragmentLayoutConform = fragmentLayoutConform;
    }

    public APIGetOrderStatusRequestModel getOrderStatusRequest = new APIGetOrderStatusRequestModel();
    public BaseAPIResponseModel<APIGetOrderStatusResponseModel> getOrderStatusResponseDataModel = new BaseAPIResponseModel<APIGetOrderStatusResponseModel>();
    public APIGetOrderStatusResponseModel getOrderStatusResponse = new APIGetOrderStatusResponseModel();

    public OrderDCM orderDetails = new OrderDCM();
    public OrderDAO orderDAO;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragmentLayoutConform.canCheckNearbyDriverFlag = false;
        fragmentLayoutConform.canGetOrderStatusFlag = false;
        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            orderDetails = orderDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getOrderStatusRequest.idOrderMaster = orderDetails.idOrderMaster;
        getOrderStatusRequest.idUser = orderDetails.idUserCustomer;


    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(getOrderStatusRequest));
        String str = helperSP.requestHTTPResponse(APILinks.APIGetOrderStatus, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            getOrderStatusResponseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(getOrderStatusResponseDataModel.Data);
            getOrderStatusResponse = gson.fromJson(str2, APIGetOrderStatusResponseModel.class);
            return true;
        }
    }


    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            fragmentLayoutConform.canCheckNearbyDriverFlag = false;
                            fragmentLayoutConform.canGetOrderStatusFlag = true;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            if (getOrderStatusResponse != null) {


                if (FixLabels.OrderStatus.DriverHeadingToPickUpPoint.equals(getOrderStatusResponse.orderStatus)) {

                    fragmentLayoutConform.canCheckNearbyDriverFlag = false;
                    fragmentLayoutConform.canGetOrderStatusFlag = false;


                    DriverDAO driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    DriverDCM driverDCM = new DriverDCM();
                    driverDAO.deleteAll();
                    driverDAO.create(getOrderStatusResponse.user);
                    fragmentLayoutConform.timer.removeCallbacksAndMessages(null);
                    if (fragmentLayoutConform.fActivity.appInBackGround == false) {
                        try {
                            fragmentLayoutConform.fActivity.showLayout(FixLabels.OrderStatus.DriverHeadingToPickUpPoint);
                        } catch (Exception e) {
                            e.printStackTrace();
                            fragmentLayoutConform.fActivity.finish();
                        }
                    } else {
                        fragmentLayoutConform.fActivity.callWorkOnAppResume = true;
                    }
                } else {
                    fragmentLayoutConform.canCheckNearbyDriverFlag = false;
                    fragmentLayoutConform.canGetOrderStatusFlag = true;
                }

            } else {
                fragmentLayoutConform.canCheckNearbyDriverFlag = false;
                fragmentLayoutConform.canGetOrderStatusFlag = true;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConform.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentLayoutConform.canGetOrderStatusFlag = true;
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
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
    }


}