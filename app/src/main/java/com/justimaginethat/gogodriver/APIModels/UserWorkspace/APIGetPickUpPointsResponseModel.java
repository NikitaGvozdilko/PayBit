package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lion-1 on 2/21/2017.
 */

public class APIGetPickUpPointsResponseModel extends BaseAPIErrorModel implements Serializable {



    public OrderDCM activeOrder ;
    public DriverDCM orderDriver ;
    public List<PickupPointDCM> PickupPointList;
    public PickupPointDCM orderPickupPoint;
    public String orderStatus;
    public Double pendingAmount = 0.0;



    public APIGetPickUpPointsResponseModel() {
    }

}
