package com.justimaginethat.gogodriver.APIModels;


import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Jay Bhavsar on 02/08/2016.
 */
public class APIOTPVerifyResponseModel extends BaseAPIErrorModel implements Serializable {

    public boolean isOTPValid ;
    public String emailAddress ;

}
