package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */

public class APICreateNewOrderRequestModel extends BaseAPIErrorModel implements Serializable {

    public int idUser;
     public OrderDCM newOrder =new OrderDCM();

    public APICreateNewOrderRequestModel(){}
}
