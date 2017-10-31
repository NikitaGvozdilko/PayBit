package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.DomainModels.MasterModel;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lion-1 on 3/30/2017.
 */

public class APIGetUserOrderHistoryResponseModel extends BaseAPIErrorModel implements Serializable {

    public List<MasterModel> orderList;
    public APIGetUserOrderHistoryResponseModel() {
    }
}
