package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.animation.ObjectAnimator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderAmountRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderAmountResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.Payment.PaymentCardActivity;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by jayb6 on 08-04-2017.
 */

public class Confirm_GetPendingOrderAmountAsync extends AsyncTask<Void, Void, Boolean> {


    public fragment_layout_confirm fragmentLayoutConfirm;
    public boolean callURLFlag = false;
    private CustomProgressBar progressDialog;

    public Confirm_GetPendingOrderAmountAsync(fragment_layout_confirm fragmentLayoutConfirm, boolean callURLFlag) {
        this.fragmentLayoutConfirm = fragmentLayoutConfirm;
        this.callURLFlag = callURLFlag;
    }

    public APIGetOrderAmountRequestModel getOrderAmountRequest = new APIGetOrderAmountRequestModel();
    public BaseAPIResponseModel<APIGetOrderAmountResponseModel> getOrderAmountResponseDataModel = new BaseAPIResponseModel<APIGetOrderAmountResponseModel>();
    public APIGetOrderAmountResponseModel getOrderAmountResponse = new APIGetOrderAmountResponseModel();


    public SessionDAO sessionDAO;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        progressDialog = new ProgressDialog(fragmentLayoutConfirm.context);
//        progressDialog = ProgressDialog.show(fragmentLayoutConfirm.context, "", "", true, false);

        progressDialog = CustomProgressBar.show(fragmentLayoutConfirm.context, "", true, false, null);
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        int idUser = 0;
        try {
            idUser = sessionDAO.getAll().get(0).idUser;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getOrderAmountRequest.idOrderMaster = idUser;
        getOrderAmountRequest.idUser = idUser;
        if (callURLFlag == true) {

            CardsDAO cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
            try {
                if (fragmentLayoutConfirm.cardSelectionIndex > -1) {
                    CardsDCM dcm = cardsDAO.getAll().get(fragmentLayoutConfirm.cardSelectionIndex);
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
        String str = helperSP.requestHTTPResponse(APILinks.APIGetPendingOrderAmount, map, "POST", false);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConfirm.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        } else {
            if (getOrderAmountResponse != null) {


                if (getOrderAmountResponse.pendingAmount > 0 && callURLFlag == false) {
                    CardsDAO cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());

                    try {
                        if (cardsDAO.getAll().size() == 0) {

                            Intent i = new Intent(fragmentLayoutConfirm.context, PaymentCardActivity.class);
                            i.putExtra("selectionOnly", true);
                            fragmentLayoutConfirm.startActivityForResult(i, 101);

                        } else {


                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConfirm.context);
                            alertDialogBuilder.setMessage("You have due amount of canceled order,Due amount is : " + getOrderAmountResponse.pendingAmount + ",\n You will have to pay due amount in order to procceed with new order!");
                            alertDialogBuilder.setCancelable(false);

                            alertDialogBuilder.setPositiveButton("Continue",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            new Confirm_GetPendingOrderAmountAsync(fragmentLayoutConfirm, true).execute();
                                            fragmentLayoutConfirm.rlBtnConfirm.setVisibility(View.INVISIBLE);
                                            fragmentLayoutConfirm.btnCancelConfirm.setVisibility(View.INVISIBLE);
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (getOrderAmountResponse.pendingAmount > 0 && callURLFlag == true) {
                    fragmentLayoutConfirm.canGetOrderAmountRepeatFlag = true;
                    fragmentLayoutConfirm.frameWebView.setVisibility(View.VISIBLE);


                    fragmentLayoutConfirm.web_view.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            if (url.contains("receipt")) {


                                fragmentLayoutConfirm.fActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        fragmentLayoutConfirm.flLoadingWeb.setVisibility(View.VISIBLE);

                                        fragmentLayoutConfirm.frameWebView.setVisibility(View.GONE);
                                        ObjectAnimator processObjectAnimator = ObjectAnimator.ofFloat(fragmentLayoutConfirm.pbCircularWeb,
                                                "rotation", 0f, 360f);
                                        processObjectAnimator.setDuration(1000);
                                        processObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                                        processObjectAnimator.setInterpolator(new LinearInterpolator());
                                        processObjectAnimator.start();
                                        fragmentLayoutConfirm.frameWebView.setVisibility(View.GONE);


//


                                    }
                                });


                            }
                        }
                    });

                    fragmentLayoutConfirm.web_view.setWebChromeClient(new WebChromeClient());
                    fragmentLayoutConfirm.web_view.loadUrl(getOrderAmountResponse.url);

                } else {

//                        fragmentLayoutConfirm.callPathDrawAsync();

                }


            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConfirm.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                fragmentLayoutConfirm.canGetOrderStatusFlag =true;
                                fragmentLayoutConfirm.getActivity().finish();
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

