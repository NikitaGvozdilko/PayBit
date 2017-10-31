package com.justimaginethat.gogodriver.activity_settings.AsynchronousTask;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;
import com.justimaginethat.gogodriver.APIModels.APIUserExistsCheckRequestModel;
import com.justimaginethat.gogodriver.APIModels.APIUserExistsCheckResponseModel;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.StartUp.APILinks;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_settings.updateModule.UpdateMobileNumberProfile;
import com.justimaginethat.gogodriver.Utility.LionUtilities;

import java.util.HashMap;


public class UserExistsCheckSettingsMobileAsync extends AsyncTask<Void, Void, Boolean> {


    public UpdateMobileNumberProfile  updateMobileProfile ;
    public String identifyField;


    public UserExistsCheckSettingsMobileAsync(UpdateMobileNumberProfile updateMobileProfile , String identifyField) {
        this.updateMobileProfile  = updateMobileProfile ;
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
            new AlertDialog.Builder(updateMobileProfile.context)
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



                     updateMobileProfile.edtPhoneNumber.setError("This contact number is already registered!");
                     updateMobileProfile.valid = false;
                     updateMobileProfile.edtPhoneNumber.requestFocus();
                } else {
                     updateMobileProfile.edtPhoneNumber.setError(null);
                     updateMobileProfile.valid = true;
                 }


            } else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(updateMobileProfile.context)
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