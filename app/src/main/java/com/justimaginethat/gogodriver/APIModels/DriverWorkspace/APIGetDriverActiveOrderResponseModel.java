package com.justimaginethat.gogodriver.APIModels.DriverWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/29/2017.
 */

public class APIGetDriverActiveOrderResponseModel extends BaseAPIErrorModel implements Serializable {


    public boolean workStatus;
    public OrderDCM activeOrder;
    public UserDCM orderUser;
    public SessionDCM orderDriver;
    public PickupPointDCM orderPickupPoint;




    public APIGetDriverActiveOrderResponseModel() {
    }
}
