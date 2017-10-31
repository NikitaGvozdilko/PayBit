package com.justimaginethat.gogodriver.DomainModels;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/25/2017.
 */

public class MasterModel extends BaseAPIErrorModel implements Serializable {
    public UserDCM user;
    public    OrderDCM order;
    public UserDCM driver;
    public PickupPointDCM pickup;
    public TransactionDCM transaction;


}
