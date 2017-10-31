package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderAmountRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGetOrderAmountResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_arrived_at_pickup;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by jayb6 on 08-04-2017.
 */

public class Confirm_GetPendingOrderAmountRepeatAsync extends AsyncTask<Void, Void, Boolean> {


    public fragment_layout_confirm fragmentLayoutConfirm;



    public Confirm_GetPendingOrderAmountRepeatAsync(fragment_layout_confirm fragmentLayoutConfirm) {
        this.fragmentLayoutConfirm = fragmentLayoutConfirm;
     
    }

    public APIGetOrderAmountRequestModel getOrderAmountRequest =new APIGetOrderAmountRequestModel();
    public BaseAPIResponseModel<APIGetOrderAmountResponseModel> getOrderAmountResponseDataModel = new BaseAPIResponseModel<APIGetOrderAmountResponseModel>();
    public APIGetOrderAmountResponseModel getOrderAmountResponse = new APIGetOrderAmountResponseModel();


    public SessionDAO sessionDAO;



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragmentLayoutConfirm.canGetOrderAmountRepeatFlag = false;


        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        int idUser = 0;
        try {
            idUser = sessionDAO.getAll().get(0).idUser;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getOrderAmountRequest.idOrderMaster = idUser;
        getOrderAmountRequest.idUser = idUser;


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

        if (response == false) {
            new AlertDialog.Builder(fragmentLayoutConfirm.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentLayoutConfirm.canGetOrderAmountRepeatFlag = true;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            if (getOrderAmountResponse != null) {



                    if(getOrderAmountResponse.pendingAmount == 0 ){

                        fragmentLayoutConfirm.canGetOrderAmountRepeatFlag = false;

                        fragmentLayoutConfirm.flLoadingWeb.setVisibility(View.GONE);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConfirm.context)
                                .setMessage("Payment Complete.")

                                .setCancelable(false)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        fragmentLayoutConfirm.canGetOrderAmountRepeatFlag = false;
                                        fragmentLayoutConfirm.flLoading.setVisibility(View.INVISIBLE);
                                        fragmentLayoutConfirm.flLoadingWeb.setVisibility(View.INVISIBLE);
                                        fragmentLayoutConfirm.btnCancelConfirm.setVisibility(View.VISIBLE);
                                        fragmentLayoutConfirm.rlBtnConfirm.setVisibility(View.VISIBLE);
                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();

//                        fragmentLayoutConfirm.callPathDrawAsync();

                    }
                    else
                    {

                        fragmentLayoutConfirm.canGetOrderAmountRepeatFlag = true;

                    }




            }
            else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(fragmentLayoutConfirm.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentLayoutConfirm.canGetOrderAmountRepeatFlag = false;
                                fragmentLayoutConfirm.getActivity().finish();
                            }
                        })
                          .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            }
        }

    }
}
