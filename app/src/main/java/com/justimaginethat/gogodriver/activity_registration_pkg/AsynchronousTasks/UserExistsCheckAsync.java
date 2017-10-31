package com.justimaginethat.gogodriver.activity_registration_pkg.AsynchronousTasks;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIUserExistsCheckRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIUserExistsCheckResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_registration_pkg.RegistrationActivity;
import com.justimaginethat.gogodriver.activity_registration_pkg.RegistrationActivityOne;

import java.sql.SQLException;
import java.util.HashMap;


public class UserExistsCheckAsync extends AsyncTask<Void, Void, Boolean> {


    public RegistrationActivityOne registrationActivity;
    public String identifyField;


    public UserExistsCheckAsync(RegistrationActivityOne registrationActivity, String identifyField) {
        this.registrationActivity = registrationActivity;
        this.identifyField = identifyField;
    }


    public APIUserExistsCheckRequestModel getUserExitsCheckRequest = new APIUserExistsCheckRequestModel();
    public BaseAPIResponseModel<APIUserExistsCheckResponseModel> getUserExitsCheckResponseDataModel = new BaseAPIResponseModel<APIUserExistsCheckResponseModel>();
    public APIUserExistsCheckResponseModel getUserExitsCheckResponse = new APIUserExistsCheckResponseModel();

    public OrderDCM orderDetails = new OrderDCM();
    public OrderDAO orderDAO;


    @Override
    protected Boolean doInBackground(Void... voids) {
        LionUtilities helperSP = new LionUtilities();
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("JsonString", gson.toJson(getUserExitsCheckRequest));
        String str = helperSP.requestHTTPResponse(APILinks.APIUserExistsCheck, map, "POST", false);
        if (str.equals("")) {
            return false;
        } else {
            getUserExitsCheckResponseDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
            String str2 = gson.toJson(getUserExitsCheckResponseDataModel.Data);
            getUserExitsCheckResponse = gson.fromJson(str2, APIUserExistsCheckResponseModel.class);
            return true;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        getUserExitsCheckRequest.identifyField = identifyField;


    }

    @Override
    protected void onPostExecute(Boolean response) {
        super.onPostExecute(response);
        if (response == false) {
            new AlertDialog.Builder(registrationActivity.context)
                    .setTitle(FixLabels.alertDefaultTitle)
                    .setCancelable(false)
                    .setMessage("No Internet Connection!!, No response from server!! , Possible server under maintenance.contact developer. Have a nice day.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            if (getUserExitsCheckResponse != null) {




                 if (getUserExitsCheckResponse.userExits) {

                    if(getUserExitsCheckRequest.identifyField.contains("@"))
                    {
                        registrationActivity.Email.setError("This email address is already registered!");
                        registrationActivity.valid = false;
//                        registrationActivity.Email.requestFocus();
                    }
                     else
                    {
                        registrationActivity.PhoneNumber.setError("This contact number is already registered!");
                        registrationActivity.valid = false;
//                        registrationActivity.PhoneNumber.requestFocus();
                    }
                } else {
                     if(getUserExitsCheckRequest.identifyField.contains("@"))
                     {
                         registrationActivity.Email.setError(null);
                         registrationActivity.valid = true;
                     }
                     else
                     {
                         registrationActivity.PhoneNumber.setError(null);
                         registrationActivity.valid = true;
                     }
                 }


            } else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(registrationActivity.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("No response from server.Application will now close.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

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