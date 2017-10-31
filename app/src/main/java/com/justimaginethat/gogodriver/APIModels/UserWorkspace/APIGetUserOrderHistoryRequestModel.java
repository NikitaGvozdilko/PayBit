package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/30/2017.
 */

public class APIGetUserOrderHistoryRequestModel extends BaseAPIErrorModel implements Serializable {

    public int idUser;
    public String orderDate;
    public Boolean isDriver;

    public APIGetUserOrderHistoryRequestModel() {
    }


}
