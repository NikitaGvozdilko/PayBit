package com.justimaginethat.gogodriver.FirebaseCloudMessagingServices.api_models;


import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Jay Bhavsar on 02/08/2016.
 */
public class APIStep2UpdateFCMIDByUDIDRequestModel extends BaseAPIErrorModel implements Serializable {

    public String udid;
    public String fcmid;

}
