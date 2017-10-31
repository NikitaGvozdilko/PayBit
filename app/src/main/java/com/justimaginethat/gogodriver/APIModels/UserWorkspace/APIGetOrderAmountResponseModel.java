package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/30/2017.
 */

public class APIGetOrderAmountResponseModel extends BaseAPIErrorModel implements Serializable {

    public double fixedRateAmount;
    public double orderAmount;
    public String orderStatus;
    public String url;
    public double pendingAmount;
    public APIGetOrderAmountResponseModel() {
    }
}
