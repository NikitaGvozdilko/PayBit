package com.justimaginethat.gogodriver.APIModels;



import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 1/30/2017.
 */
public class APIRegistrationRequestModel extends BaseAPIErrorModel implements Serializable {

    public UserDCM UserDCM;
    public String gcmId;
    public String udId;

    public APIRegistrationRequestModel() {
        UserDCM = new UserDCM();
    }
}
