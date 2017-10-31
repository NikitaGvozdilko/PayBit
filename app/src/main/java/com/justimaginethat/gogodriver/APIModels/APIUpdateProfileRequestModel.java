package com.justimaginethat.gogodriver.APIModels;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 4/17/2017.
 */

public class APIUpdateProfileRequestModel extends BaseAPIErrorModel implements Serializable {

    public SessionDCM user=new SessionDCM();

    public APIUpdateProfileRequestModel() {
    }
}
