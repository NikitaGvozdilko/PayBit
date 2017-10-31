package com.justimaginethat.gogodriver.FirebaseCloudMessagingServices;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIResponseModel;
import com.justimaginethat.gogodriver.FirebaseCloudMessagingServices.api_models.APIStep2UpdateFCMIDByUDIDResponseModel;
import com.justimaginethat.gogodriver.StartUp.FixLabels;


/**
 * Created by Jay Bhavsar on 05/08/2016.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private String TAG = "FCM==>";
    private String Token = "";


    BaseAPIResponseModel<APIStep2UpdateFCMIDByUDIDResponseModel> responseDataModel=new BaseAPIResponseModel<APIStep2UpdateFCMIDByUDIDResponseModel>();
    APIStep2UpdateFCMIDByUDIDResponseModel responseModel=new APIStep2UpdateFCMIDByUDIDResponseModel();
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FixLabels.gcmid =refreshedToken;
        Log.i(TAG, "Refreshed token: " + FixLabels.gcmid);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        new Thread(new Runnable() {
//            public void run() {
//                while(FixLabels.udid.equals("TESTDevice")||!Token.equals(null))
//                {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(!Token.equals(null)&&!Token.equals("")) {
//                    JBHelper jb = new JBHelper();
//                    Gson gson = new Gson();
//                    APIStep2UpdateFCMIDByUDIDRequestModel requestModelNearbyD = new APIStep2UpdateFCMIDByUDIDRequestModel();
//
//                    requestModelNearbyD.udid = FixLabels.udid;
//                    requestModelNearbyD.gcmid = Token;
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    map.put("JsonString", gson.toJson(requestModelNearbyD));
//                    String str = jb.requestHTTPResponse(APILinks.APIStep1CheckDeviceRegistration, map, "POST", false);
//
//                    if (str.equals("")) {
//                        Log.i(TAG, "APIStep2UpdateFCMIDByUDID failed due to server connection timeout.");
//                    } else {
//                        responseNearByDriverDataModel = gson.fromJson(str, BaseAPIResponseModel.class);
//                        String str2 = gson.toJson(responseNearByDriverDataModel.Data);
//                        responseNearDriverModel = gson.fromJson(str2, APIStep2UpdateFCMIDByUDIDResponseModel.class);
//                        if (responseNearDriverModel != null) {
//                            if (responseNearDriverModel.errorLogId < 0) {
//                                if (responseNearDriverModel.udid.equals(FixLabels.udid) && responseNearDriverModel.gcmid.equals(Token)) {
//                                    Log.i(TAG, "APIStep2UpdateFCMIDByUDID Updated");
//                                } else {
//                                    Log.i(TAG, "APIStep2UpdateFCMIDByUDID Update Failed");
//                                }
//                            } else {
//                                Log.i(TAG, "APIStep2UpdateFCMIDByUDID Update Failed from server Error Log : " + responseNearDriverModel.errorLogId + "\n errorURL :" + responseNearDriverModel.errorURL);
//                            }
//
//
//                        } else {
//                            Log.i(TAG, "APIStep2UpdateFCMIDByUDID Failed due to server returned no response on data");
//                        }
//                    }
//                }
//                else
//                {
//                    Log.i(TAG, "Token not generated retry");
//                }
//            }
//
//            }).start();
//
    }




}
