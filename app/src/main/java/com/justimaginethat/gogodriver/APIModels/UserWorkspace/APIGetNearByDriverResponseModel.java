package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lion-1 on 3/25/2017.
 */

public class APIGetNearByDriverResponseModel  extends BaseAPIErrorModel implements Serializable {

    public List<DriverDCM> driverList;

    public String orderStatus;



    public APIGetNearByDriverResponseModel() {
    }
}
