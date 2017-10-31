package com.justimaginethat.gogodriver.APIModels.UserWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */

public class APIGiveRatingsResponseModel extends BaseAPIErrorModel implements Serializable {

    public int idUser ;
    public int idOrderMaster ;
    public int idrating;
    public int idDriver;
    public int idCustomer;
    public Double ratings ;
    public Boolean isCustomerRating ;
    public String orderStatus;

    public APIGiveRatingsResponseModel(){}

}
