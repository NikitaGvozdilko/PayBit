package com.justimaginethat.gogodriver.APIModels;


import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Jay Bhavsar on 02/08/2016.
 */
public class APIRegistrationResponseModel extends BaseAPIErrorModel implements Serializable {
    public int idUser;
    public SessionDCM user;
}
