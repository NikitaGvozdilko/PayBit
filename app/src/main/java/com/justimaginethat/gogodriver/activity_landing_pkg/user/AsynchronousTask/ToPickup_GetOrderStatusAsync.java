package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderStatusRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderStatusResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_heading_to_pickup;

import java.sql.SQLException;
import java.util.HashMap;


public class ToPickup_GetOrderStatusAsync extends AsyncTask<Void, Void, Boolean> {


    public fragment_driver_heading_to_pickup fragmentDriverHandel;


    public ToPickup_GetOrderStatusAsync(fragment_driver_heading_to_pickup fragmentDriverHandel) {
        this.fragmentDriverHandel = fragmentDriverHandel;
    }


    public APIGetOrderStatusRequestModel getOrderStatusRequest = new APIGetOrderStatusRequestModel();
    public BaseAPIResponseModel<APIGetOrderStatusResponseModel> getOrderStatusResponseDataModel = new BaseAPIResponseModel<APIGetOrderStatusResponseModel>();
    public APIGetOrderStatusResponseModel getOrderStatusResponse = new APIGetOrderStatusResponseModel();

    public OrderDCM orderDetails = new OrderDCM();
    public OrderDAO orderDAO;


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
    protected void onPreExecute() {
        super.onPreExecute();
        fragmentDriverHandel.canGetOrderStatusFlag = false;
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
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {
            new AlertDialog.Builder(fragmentDriverHandel.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            fragmentDriverHandel.canGetOrderStatusFlag = true;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        } else {
            if (getOrderStatusResponse != null) {


                Log.e("Order Status", getOrderStatusResponse.orderStatus);

                if (FixLabels.OrderStatus.DriverHeadingToPickUpPoint.equals(getOrderStatusResponse.orderStatus)) {
                    DriverDAO driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    driverDAO.deleteAll();
                    driverDAO.create(getOrderStatusResponse.user);
                    fragmentDriverHandel.driverDCM = getOrderStatusResponse.user;
//                        fragmentDriverHandel.timer.removeCallbacksAndMessages(null);
                    fragmentDriverHandel.canGetOrderStatusFlag = true;
                } else if (FixLabels.OrderStatus.PaymentComplete.equals(getOrderStatusResponse.orderStatus)) {
                    DriverDAO driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    driverDAO.deleteAll();
                    driverDAO.create(getOrderStatusResponse.user);
                    fragmentDriverHandel.driverDCM = getOrderStatusResponse.user;
                    fragmentDriverHandel.timer.removeCallbacksAndMessages(null);
                    fragmentDriverHandel.canGetOrderStatusFlag = false;



                    if (fragmentDriverHandel.fActivity.appInBackGround == false) {
                        try {
                            fragmentDriverHandel.fActivity.showLayout(getOrderStatusResponse.orderStatus);
                        } catch (Exception e) {
                            e.printStackTrace();
                            fragmentDriverHandel.fActivity.finish();
                        }
                    } else {
                        fragmentDriverHandel.fActivity.callWorkOnAppResume = true;
                    }


                } else if (FixLabels.OrderStatus.PleaseConfirmPayment.equals(getOrderStatusResponse.orderStatus)) {
                    DriverDAO driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    driverDAO.deleteAll();
                    driverDAO.create(getOrderStatusResponse.user);
                    fragmentDriverHandel.driverDCM = getOrderStatusResponse.user;
                    fragmentDriverHandel.timer.removeCallbacksAndMessages(null);
                    fragmentDriverHandel.canGetOrderStatusFlag = false;
                    if (fragmentDriverHandel.fActivity.appInBackGround == false) {
                        try {
                            fragmentDriverHandel.fActivity.showLayout(FixLabels.OrderStatus.PleaseConfirmPayment);
                        } catch (Exception e) {
                            e.printStackTrace();
                            fragmentDriverHandel.fActivity.finish();
                        }
                    } else {
                        fragmentDriverHandel.fActivity.callWorkOnAppResume = true;
                    }


                } else if (FixLabels.OrderStatus.OrderCanceled.equals(getOrderStatusResponse.orderStatus)) {
                    if(fragmentDriverHandel.cancelClicked == false) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentDriverHandel.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Order was cancelled by driver!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DriverDAO driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                        driverDAO.deleteAll();
                                        UserDAO userDAO = new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                        userDAO.deleteAll();
                                        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                        orderDAO.deleteAll();

                                        fragmentDriverHandel.canGetOrderStatusFlag = false;
                                        try {
                                            fragmentDriverHandel.fActivity.showLayout(FixLabels.OrderStatus.NoActiveOrder);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                    }
                } else {
                    fragmentDriverHandel.canGetOrderStatusFlag = true;
                    fragmentDriverHandel.orederStatus = getOrderStatusResponse.orderStatus;
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentDriverHandel.context)
//                                .setTitle("Get Order failed")
//                                .setMessage("Get Order is not active.")
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        fragmentDriverHandel.canGetOrderStatusFlag = true;
//                                    }
//                                })
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
                }
            } else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentDriverHandel.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentDriverHandel.canGetOrderStatusFlag = false;
                                fragmentDriverHandel.getActivity().finish();
                            }
                        })
                          .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
        }
    }
}