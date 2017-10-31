package com.justimaginethat.gogodriver.APIModels.DriverWorkspace;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/29/2017.
 */

public class APIChangeWorkStatusResponseModel extends BaseAPIErrorModel implements Serializable {


    public int idDriver;
    public boolean workStatus;
    public String apnID;
    public String gcmID;
    public APIChangeWorkStatusResponseModel() {
    }
}
