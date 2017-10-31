package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_on_the_way;

import java.sql.SQLException;
import java.util.HashMap;


public class ToCustomer_GetOrderStatusAsync extends AsyncTask<Void, Void, Boolean> {


    public fragment_driver_on_the_way fragmentDriverOnTheWay;



    public ToCustomer_GetOrderStatusAsync(fragment_driver_on_the_way fragmentDriverOnTheWay) {
        this.fragmentDriverOnTheWay = fragmentDriverOnTheWay;
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
        fragmentDriverOnTheWay.canGetOrderStatusFlag = false;
        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            orderDetails = orderDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getOrderStatusRequest.idOrderMaster = orderDetails.idOrderMaster;
        getOrderStatusRequest.idUser = orderDetails.idUserCustomer;


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {
            new AlertDialog.Builder(fragmentDriverOnTheWay.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentDriverOnTheWay.canGetOrderStatusFlag = true;
//                            fragmentDriverOnTheWay.fActivity.showRatingScreenForLastOrder = true;
//                            fragmentDriverOnTheWay.fActivity.callWorkOnAppResume = true;

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        } else {
            if (getOrderStatusResponse != null) {
                if (getOrderStatusResponse != null) {
                    Log.e("Order Status",getOrderStatusResponse.orderStatus);
                    if (FixLabels.OrderStatus.OrderComplete.equals(getOrderStatusResponse.orderStatus)) {
                        if (fragmentDriverOnTheWay.fActivity.appInBackGround == false) {
                            try {
                                fragmentDriverOnTheWay.fActivity.showLayout(FixLabels.OrderStatus.OrderComplete);
                            } catch (Exception e) {
                                e.printStackTrace();
                                fragmentDriverOnTheWay.fActivity.finish();
                            }
                        } else {
                            fragmentDriverOnTheWay.fActivity.showRatingScreenForLastOrder = true;
                            fragmentDriverOnTheWay.fActivity.callWorkOnAppResume = true;
                        }

                    }
                    else {


                        DriverDAO driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        try {
                            fragmentDriverOnTheWay.driverDCM = driverDAO.getAll().get(0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        fragmentDriverOnTheWay.driverDCM.address2 = getOrderStatusResponse.address;
                        fragmentDriverOnTheWay.driverDCM.latitude2 = getOrderStatusResponse.latitude;
                        fragmentDriverOnTheWay.driverDCM.longitude2 = getOrderStatusResponse.longitude;
                        fragmentDriverOnTheWay.driverDCM.heading  = getOrderStatusResponse.user.heading;

                        driverDAO.update(fragmentDriverOnTheWay.driverDCM);

                            fragmentDriverOnTheWay.canGetOrderStatusFlag = true;
                            fragmentDriverOnTheWay.fromLat =Double.parseDouble(getOrderStatusResponse.latitude);
                             fragmentDriverOnTheWay.fromLon =Double.parseDouble(getOrderStatusResponse.longitude);
                            fragmentDriverOnTheWay.fromLatLng = new LatLng( fragmentDriverOnTheWay.fromLat, fragmentDriverOnTheWay.fromLon);

                            fragmentDriverOnTheWay.setMarkerAndCamera();
                    }
                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentDriverOnTheWay.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    fragmentDriverOnTheWay.canGetOrderStatusFlag = false;
                                    fragmentDriverOnTheWay.getActivity().finish();
                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                }
            }
        }
    }
}