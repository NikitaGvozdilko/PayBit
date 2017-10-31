package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 2/21/2017.
 */

public class APIGetPickUpPointsRequestModel extends BaseAPIErrorModel implements Serializable {

    public int idUser;
    public String emailAddress;
    public String mobileNumber;
    public String gcmId;
    public String apnID ;
    public PickupPointDCM PickupPointDCM;


    public APIGetPickUpPointsRequestModel() {
    }


}
