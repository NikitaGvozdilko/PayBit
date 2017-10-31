package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/30/2017.
 */

public class APIGetOrderAmountRequestModel extends BaseAPIErrorModel implements Serializable {

    public int idOrderMaster;
    public int idUser;
    public String number;
    public String expiryMonth;
    public String expiryYear;
    public String cvn;
    public String token;


    public APIGetOrderAmountRequestModel() {
    }
}
