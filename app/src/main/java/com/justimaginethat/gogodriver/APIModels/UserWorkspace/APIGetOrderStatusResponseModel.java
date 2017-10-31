package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */

public class APIGetOrderStatusResponseModel extends BaseAPIErrorModel implements Serializable {

    public int idDriver ;
    public int idOrderMaster ;
    public String address ;
    public String longitude ;
    public String latitude ;
    public String orderStatus ;
    public DriverDCM user;
    public String req_payment_token = "";


    public APIGetOrderStatusResponseModel(){}

}
