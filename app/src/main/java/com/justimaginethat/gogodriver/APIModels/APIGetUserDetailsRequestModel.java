package com.justimaginethat.gogodriver.APIModels;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 4/1/2017.
 */

public class APIGetUserDetailsRequestModel extends BaseAPIErrorModel implements Serializable {


    public UserDCM user;


    public int idUser;
    public APIGetUserDetailsRequestModel() {
    }
}
