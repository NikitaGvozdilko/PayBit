package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */

public class APIRequestOrderDriverResponseModel extends BaseAPIErrorModel implements Serializable{

    public UserDCM driver;
    public String orderStatus;

    public APIRequestOrderDriverResponseModel(){

    }
}
