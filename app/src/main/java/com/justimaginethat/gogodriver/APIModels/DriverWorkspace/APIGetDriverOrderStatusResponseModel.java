package com.justimaginethat.gogodriver.APIModels.DriverWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */

public class APIGetDriverOrderStatusResponseModel extends BaseAPIErrorModel implements Serializable {

    public int idUser;
    public String orderStatus;
    public int idOrderMaster;
    public String req_payment_token;



    public APIGetDriverOrderStatusResponseModel(){}

}
