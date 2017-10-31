package com.justimaginethat.gogodriver.APIModels;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 4/1/2017.
 */

public class APIGetUserDetailsResponseModel extends BaseAPIErrorModel implements Serializable {
    public SessionDCM user;

    public APIGetUserDetailsResponseModel() {
    }
}
