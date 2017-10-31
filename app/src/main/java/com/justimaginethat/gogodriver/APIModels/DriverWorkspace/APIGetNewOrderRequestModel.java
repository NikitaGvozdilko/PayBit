package com.justimaginethat.gogodriver.APIModels.DriverWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/29/2017.
 */

public class APIGetNewOrderRequestModel extends BaseAPIErrorModel implements Serializable {

    public int idDriver;
    public String address;
    public double longitude;
    public double latitude;
    public double heading;

    public APIGetNewOrderRequestModel() {
    }
}
