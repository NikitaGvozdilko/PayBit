package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderAmountRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderAmountResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_arrived_at_pickup;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by jayb6 on 08-04-2017.
 */

public class AtPickupPoint_GetOrderAmountAsync extends AsyncTask<Void, Void, Boolean> {


    public fragment_driver_arrived_at_pickup arrivedAtPickup;
    public boolean callURLFlag = false;
    private CustomProgressBar progressDialog;

    public AtPickupPoint_GetOrderAmountAsync(fragment_driver_arrived_at_pickup arrivedAtPickup, boolean callURLFlag) {
        this.arrivedAtPickup = arrivedAtPickup;
        this.callURLFlag = callURLFlag;
    }

    public APIGetOrderAmountRequestModel getOrderAmountRequest = new APIGetOrderAmountRequestModel();
    public BaseAPIResponseModel<APIGetOrderAmountResponseModel> getOrderAmountResponseDataModel = new BaseAPIResponseModel<APIGetOrderAmountResponseModel>();
    public APIGetOrderAmountResponseModel getOrderAmountResponse = new APIGetOrderAmountResponseModel();

    public OrderDCM orderDetails = new OrderDCM();
    public OrderDAO orderDAO;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        progressDialog = new CustomProgressBar(arrivedAtPickup.context);
//        progressDialog = ProgressDialog.show(arrivedAtPickup.context, "", "", true, false);
        progressDialog = CustomProgressBar.show(arrivedAtPickup.context,"", true,false,null);
        orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            orderDetails = orderDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getOrderAmountRequest.idOrderMaster = orderDetails.idOrderMaster;
        getOrderAmountRequest.idUser = orderDetails.idUserCustomer;
        if (callURLFlag == true) {

            CardsDAO cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
            try {
                if (arrivedAtPickup.cardSelectionIndex > -1) {
                    CardsDCM dcm = cardsDAO.getAll().get(arrivedAtPickup.cardSelectionIndex);
                    getOrderAmountRequest.cvn = dcm.cvvNumber;
                    getOrderAmountRequest.number = dcm.number;
                    getOrderAmountRequest.expiryYear = dcm.expiryYear;
                    getOrderAmountRequest.expiryMonth = dcm.expiryMonth;
                    getOrderAmountRequest.token = dcm.paymentToken;
                } else {
                    for (CardsDCM dcm : cardsDAO.getAll()) {
                        if (dcm.isDefault == true) {
                            getOrderAmountRequest.cvn = dcm.cvvNumber;
                            getOrderAmountRequest.number = dcm.number;
                            getOrderAmountRequest.expiryYear = dcm.expiryYear;
                            getOrderAmountRequest.expiryMonth = dcm.expiryMonth;
                            getOrderAmountRequest.token = dcm.paymentToken;
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        getOrderAmountRequest.number=;
//        getOrderAmountRequest.expiryMonth= ;
//        getOrderAmountRequest.expiryYear=;
//        getOrderAmountRequest.cvn=;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(getOrderAmountRequest));
        String str = helperSP.requestHTTPResponse(APILinks.APIGetOrderAmount, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            getOrderAmountResponseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(getOrderAmountResponseDataModel.Data);
            getOrderAmountResponse = gson.fromJson(str2, APIGetOrderAmountResponseModel.class);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(arrivedAtPickup.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            if (getOrderAmountResponse != null) {



                    if (FixLabels.OrderStatus.PleaseConfirmPayment.equals(getOrderAmountResponse.orderStatus)) {
                        arrivedAtPickup.driverRate.setText(String.valueOf(getOrderAmountResponse.fixedRateAmount).toString());
                        arrivedAtPickup.orderRate.setText(String.valueOf(getOrderAmountResponse.orderAmount).toString());
                        double TotalAmount = (getOrderAmountResponse.fixedRateAmount + getOrderAmountResponse.orderAmount);
                        arrivedAtPickup.totalRate.setText(String.valueOf(TotalAmount).toString());
                        arrivedAtPickup.URLtoCall = getOrderAmountResponse.url;


                        if (callURLFlag == true) {
                            arrivedAtPickup.canGetOrderStatusFlag = false;
                            if (arrivedAtPickup.timer != null) {
                                arrivedAtPickup.timer.removeCallbacksAndMessages(null);
                                arrivedAtPickup.timer = null;
                            }


                            arrivedAtPickup.frame_pay.setVisibility(View.INVISIBLE);
                            arrivedAtPickup.frameWebView.setVisibility(View.VISIBLE);
                            arrivedAtPickup.rlLoading.setVisibility(View.VISIBLE);


                            arrivedAtPickup.web_view.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    super.onPageFinished(view, url);
                                    if (url.contains("receipt")) {


                                        arrivedAtPickup.counterWeb = 0;

                                        arrivedAtPickup.fActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                arrivedAtPickup.rlLoading.setVisibility(View.VISIBLE);


                                                ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(arrivedAtPickup.pbCircle,
                                                        "rotation", 0f, 360f);
                                                processObjectAnimator.setDuration(1000);
                                                processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                                                processObjectAnimator.setInterpolator(new LinearInterpolator());
                                                processObjectAnimator.start();
                                                arrivedAtPickup.frameWebView.setVisibility(View.GONE);

//                                                arrivedAtPickup.progressLocal = new ProgressDialog(arrivedAtPickup.context);
//                                                arrivedAtPickup.progressLocal = ProgressDialog.show(arrivedAtPickup.context, "", "Confirming Payment,Please wait.", true, false);
//                                                arrivedAtPickup.btnRetry.setEnabled(false);
//                                                arrivedAtPickup.btnRetry.setText("Confirming,Please wait..");
//


                                                new Pay_SetOrderStatusAsymc(arrivedAtPickup).execute();


                                            }
                                        });


//                                        arrivedAtPickup.timerLocal = new Handler();
//                                        Runnable runnableCode = new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                try {
//
//                                                    arrivedAtPickup.counterWeb = arrivedAtPickup.counterWeb + 1;
//                                                    arrivedAtPickup.timerLocal.postDelayed(this,1000);
//                                                } catch (Exception e) {
//                                                    Log.e("Error Handler", String.valueOf(e));
//                                                }
//                                            }
//                                        };
//                                        arrivedAtPickup.timerLocal.postDelayed(runnableCode,1000);

                                    }
                                }
                            });

                            arrivedAtPickup.web_view.setWebChromeClient(new WebChromeClient());


                            arrivedAtPickup.web_view.loadUrl(arrivedAtPickup.URLtoCall);

                            arrivedAtPickup.frame_done.setVisibility(View.VISIBLE);
                            arrivedAtPickup.action_done.setVisibility(View.VISIBLE);


                        } else {
                            arrivedAtPickup.frame_pay.setVisibility(View.VISIBLE);
                        }


                    } else if (FixLabels.OrderStatus.ConfirmingPayment.equals(getOrderAmountResponse.orderStatus)) {


                        if (arrivedAtPickup.timer != null) {
                            arrivedAtPickup.timer.removeCallbacksAndMessages(null);
                            arrivedAtPickup.timer = null;
                        }
                        arrivedAtPickup.canGetOrderStatusFlag = true;

                        arrivedAtPickup.frame_pay.setVisibility(View.INVISIBLE);
                        arrivedAtPickup.frameWebView.setVisibility(View.INVISIBLE);

                        arrivedAtPickup.frame_done.setVisibility(View.INVISIBLE);
                        arrivedAtPickup.action_done.setVisibility(View.INVISIBLE);

                        arrivedAtPickup.rlLoading.setVisibility(View.VISIBLE);


                        ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(arrivedAtPickup.pbCircle,
                                "rotation", 0f, 360f);
                        processObjectAnimator.setDuration(1000);
                        processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                        processObjectAnimator.setInterpolator(new LinearInterpolator());
                        processObjectAnimator.start();


                        arrivedAtPickup.callAsynchronousTask();


                    } else {
//                    arrivedAtPickup.canCheckNearbyDriverFlag =false;
//                    arrivedAtPickup.canGetOrderStatusFlag =true;
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(arrivedAtPickup.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Get Order is not active.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
//                                    arrivedAtPickup.canGetOrderStatusFlag =true;
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                    }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            }
            else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(arrivedAtPickup.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                arrivedAtPickup.canGetOrderStatusFlag =true;
                                arrivedAtPickup.getActivity().finish();
                            }
                        })
                          .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            }
        }
    }
}
