package com.justimaginethat.gogodriver.APIModels;


import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

public class APILoginResponseModel extends BaseAPIErrorModel implements Serializable {
    public boolean isAuthorised;
    public int idUser;
    public String profilePicture;
    public String userName;
    public SessionDCM user;
    public boolean isCustomer;
    public boolean isDriver;
    public boolean isAdmin;
    public APILoginResponseModel() {
    }


}
