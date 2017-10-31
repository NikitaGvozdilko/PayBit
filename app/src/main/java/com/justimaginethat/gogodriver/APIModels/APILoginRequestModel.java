package com.justimaginethat.gogodriver.APIModels;


import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

public class APILoginRequestModel extends BaseAPIErrorModel implements Serializable {
    public String emailAddress;
    public String mobileNumber;
    public String password;
    public String gcmId;
    public String udid;
}
