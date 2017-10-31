package com.justimaginethat.gogodriver.APIModels.UserWorkspace;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import java.io.Serializable;
/**
 * Created by Lion-1 on 3/25/2017.
 */

public class APIGetNearByDriverRequestModel  extends BaseAPIErrorModel implements Serializable {

    public int idUser= 0;
    public String address ="";
    public String longitude="";
    public String latitude="";

    public APIGetNearByDriverRequestModel(){

    }
}
