package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */

public class APIPaidAmountRequestModel extends BaseAPIErrorModel implements Serializable {

    public int idUser;
    public int idOrderMaster;
    public Double amountPaid;
    public String transactionId;


    public APIPaidAmountRequestModel(){}
}
