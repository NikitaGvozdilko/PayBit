package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderStatusRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderStatusResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_arrived_at_pickup;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_arrived_at_pickup;

import java.sql.SQLException;
import java.util.HashMap;


public class AtPickupPoint_GetOrderStatusAsync extends AsyncTask<Void, Void, Boolean> {


    public fragment_driver_arrived_at_pickup arrivedAtPickup;



    public AtPickupPoint_GetOrderStatusAsync(fragment_driver_arrived_at_pickup arrivedAtPickup) {
        this.arrivedAtPickup = arrivedAtPickup;
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
        arrivedAtPickup.canGetOrderStatusFlag = false;
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
            new AlertDialog.Builder(arrivedAtPickup.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            arrivedAtPickup.canGetOrderStatusFlag = true;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            if (getOrderStatusResponse != null) {



                    Log.e("Order Status",getOrderStatusResponse.orderStatus);

                    if (FixLabels.OrderStatus.PaymentComplete.equals(getOrderStatusResponse.orderStatus)) {


                        CardsDAO cardsDAO= new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        try {
                            if(arrivedAtPickup.cardSelectionIndex > -1 )
                            {
                                CardsDCM dcm = cardsDAO.getAll().get(arrivedAtPickup.cardSelectionIndex);
                                dcm.paymentToken = getOrderStatusResponse.req_payment_token;
                                cardsDAO.update(dcm);
                            }
                            else {
                                for (CardsDCM dcm : cardsDAO.getAll()) {
                                    if (dcm.isDefault == true) {
                                        dcm.paymentToken = getOrderStatusResponse.req_payment_token;
                                        cardsDAO.update(dcm);
                                        break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        DriverDAO driverDAO=new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                        driverDAO.update(getOrderStatusResponse.user);

                        if( arrivedAtPickup.timer!=null) {
                            arrivedAtPickup.timer.removeCallbacksAndMessages(null);
                        }
                        if( arrivedAtPickup.timerLocal!=null) {
                            arrivedAtPickup.timerLocal.removeCallbacksAndMessages(null);
                        }
                        if(arrivedAtPickup.progressLocal != null)
                        {
                            arrivedAtPickup.progressLocal.dismiss();
                        }
                        arrivedAtPickup.timer = null;
                        arrivedAtPickup.timerLocal = null;
                        arrivedAtPickup.canGetOrderStatusFlag = false;
                        arrivedAtPickup.frame_pay.setVisibility(View.INVISIBLE);
                        arrivedAtPickup.rlLoading.setVisibility(View.INVISIBLE);
                        arrivedAtPickup.action_done.setVisibility(View.VISIBLE);
                        arrivedAtPickup.rlLoading.setVisibility(View.INVISIBLE);
                        arrivedAtPickup.frame_done.setVisibility(View.VISIBLE);
                        arrivedAtPickup.frameWebView.setVisibility(View.INVISIBLE);



//                        try {
//                            arrivedAtPickup.FRequestPayment.showLayout(getOrderStatusResponse.orderStatus);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }


                    }

                    else  {
                        arrivedAtPickup.canGetOrderStatusFlag = true;
                    }
                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(arrivedAtPickup.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("No response from server.Application will now close.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    arrivedAtPickup.canGetOrderStatusFlag = false;
                                    arrivedAtPickup.getActivity().finish();
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