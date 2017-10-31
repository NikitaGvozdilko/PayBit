package com.justimaginethat.gogodriver.APIModels.Payment;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/30/2017.
 */

public class APIGetUserOrderDetailsResultCCM extends BaseAPIErrorModel implements Serializable {
    public double OrderAmount;
    public UserDCM UserDCM;
    public int OldOrders;
    public String countryName;


    public APIGetUserOrderDetailsResultCCM() {
    }
}
