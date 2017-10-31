package com.justimaginethat.gogodriver.APIModels.Payment;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/30/2017.
 */

public class APIGetUserOrderDetailsRequestCCM extends BaseAPIErrorModel implements Serializable {

    public int idOrderMaster;
    public int idUser;

    public APIGetUserOrderDetailsRequestCCM() {
    }
}
