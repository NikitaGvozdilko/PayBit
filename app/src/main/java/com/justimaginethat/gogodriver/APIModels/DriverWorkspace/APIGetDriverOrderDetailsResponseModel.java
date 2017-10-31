package com.justimaginethat.gogodriver.APIModels.DriverWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/29/2017.
 */

public class APIGetDriverOrderDetailsResponseModel extends BaseAPIErrorModel implements Serializable {


    public OrderDCM order;
    public PickupPointDCM orderPickupPoint;
    public UserDCM UserDCM;


    public APIGetDriverOrderDetailsResponseModel() {
    }
}
