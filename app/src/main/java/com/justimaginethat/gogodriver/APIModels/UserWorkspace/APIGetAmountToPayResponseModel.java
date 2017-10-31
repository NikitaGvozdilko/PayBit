package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */

public class APIGetAmountToPayResponseModel extends BaseAPIErrorModel implements Serializable {

    public Double fixedRateAmount;
    public Double orderAmount;
    public String orderStatus;

    public APIGetAmountToPayResponseModel(){}
}
