package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGiveRatingsRequestModel;
import com.justimaginethat.gogodriver.APIModels.UserWorkspace.APIGiveRatingsResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ProcessBar.CustomProgressBar;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_user_rating;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by jayb6 on 08-04-2017.
 */

public class CustomerToDriverRatingAsync extends AsyncTask<Void, Void, Boolean> {
    public fragment_user_rating pickupComplete;
    public APIGiveRatingsRequestModel getRattingRequest =new APIGiveRatingsRequestModel();

    public BaseAPIResponseModel<APIGiveRatingsResponseModel> getRattingResponseDataModel = new BaseAPIResponseModel<APIGiveRatingsResponseModel>();
    public APIGiveRatingsResponseModel giveRattingResponse = new APIGiveRatingsResponseModel();
    public OrderDCM orderDetails=new OrderDCM();
    public OrderDAO orderDAO;

    public SessionDCM sessionDCM =new SessionDCM();
    public SessionDAO sessionDAO;


    public DriverDCM driverDCM =new DriverDCM();
    public DriverDAO driverDAO;



    public CustomerToDriverRatingAsync(fragment_user_rating pickupComplete) {
        this.pickupComplete = pickupComplete;
    }

    public CustomProgressBar progressDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressDialog = ProgressDialog.show(pickupComplete.context, "", "", true, false);
        progressDialog = CustomProgressBar.show(pickupComplete.context,"", true,false,null);
        orderDAO= new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            orderDetails = orderDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        driverDAO= new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            driverDCM = driverDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        sessionDAO= new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getRattingRequest.idOrderMaster= orderDetails.idOrderMaster;
        getRattingRequest.idUser=sessionDCM.idUser;
        getRattingRequest.idrating=0;
        getRattingRequest.idDriver=driverDCM.idUser;
        getRattingRequest.idCustomer= sessionDCM.idUser;
        getRattingRequest.ratings=pickupComplete.rattingPoints;
        getRattingRequest.isCustomerRating=false;
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(getRattingRequest));
        String str = helperSP.requestHTTPResponse(APILinks.APIGiveRatings, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            getRattingResponseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(getRattingResponseDataModel.Data);
            giveRattingResponse = gson.fromJson(str2, APIGiveRatingsResponseModel.class);
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
            new AlertDialog.Builder(pickupComplete.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
        } else {

            if (giveRattingResponse != null) {


                if (giveRattingResponse.idrating > 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(pickupComplete.context);
                    alertDialogBuilder.setMessage("Thank You For Rating.");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    orderDAO.deleteAll();
                                    PickupPointsDAO pickupPointsDAO=new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                                    pickupPointsDAO.deleteAll();
                                    FixLabels.sessionDatabase.pickupPointId = 0;
                                    FixLabels.sessionDatabase.pickupAddress = "";
                                    FixLabels.sessionDatabase.pickupAddress = "";
                                    FixLabels.sessionDatabase.pickupLatitude = "";
                                    FixLabels.sessionDatabase.pickupLongitude = "";
                                    FixLabels.sessionDatabase.pickupTitle = "";
                                    try {
                                        pickupComplete.fActivity.showLayout(FixLabels.OrderStatus.NoActiveOrder);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(pickupComplete.context)
                            .setTitle(FixLabels.alertDefaultTitle)
                            .setCancelable(false)
                            .setMessage("Failed to give rating!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
//                                    pickupComplete.canGetOrderStatusFlag =true;
                                }
                            })
                              .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                }
            }
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(pickupComplete.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                pickupComplete.getActivity().finish();
                            }
                        })
                          .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
            }
        }

    }
}
