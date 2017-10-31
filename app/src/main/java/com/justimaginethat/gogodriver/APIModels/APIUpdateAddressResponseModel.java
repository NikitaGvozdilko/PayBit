package com.justimaginethat.gogodriver.APIModels;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/20/2017.
 */

public class APIUpdateAddressResponseModel extends BaseAPIErrorModel implements Serializable {
    public int idUser;


    public APIUpdateAddressResponseModel() {
    }
}
