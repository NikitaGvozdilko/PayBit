package com.justimaginethat.gogodriver.APIModels.DriverWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/29/2017.
 */

public class APIAcceptRejectOrdersRequestModel  extends BaseAPIErrorModel implements Serializable {

    public int idOrderMaster;
    public int idDriver;
    public String orderStatus;

    public APIAcceptRejectOrdersRequestModel(){}
    }