package com.justimaginethat.gogodriver.APIModels;

import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/20/2017.
 */

public class APIUpdateAddressRequestModel  extends BaseAPIErrorModel implements Serializable {

    public int idUser = 0;
    public String address1 = "";
    public String address2 = "";
    public String buildingName1 = "";
    public String floorNumber1 = "";
    public String buildingName2 = "";
    public String floorNumber2 = "";
    public String longitude1 = "";
    public String latitude1 = "";
    public String longitude2 = "";
    public String latitude2 = "";
    public String streetName1 = "";
    public String streetName2= "";
    public String defaultAddress= "";
    public String defaultBuildingName = "";
    public String defaultFloorNumber = "";
    public String defaultLongitude = "";
    public String defaultLatitude = "";

    public APIUpdateAddressRequestModel(){

    }
    public APIUpdateAddressRequestModel(int idUser,
                                        String address1,
                                        String address2,
                                        String buildingName1,
                                        String floorNumber1,
                                        String buildingName2,
                                        String floorNumber2,
                                        String longitude1,
                                        String latitude1,
                                        String longitude2,
                                        String latitude2,
                                        String streetName1,
                                        String streetName2,
                                        String defaultAddress,
                                        String defaultBuildingName,
                                        String defaultFloorNumber,
                                        String defaultLongitude,
                                        String defaultLatitude) {

        this.idUser = idUser;
        this.address1 = address1;
        this.address2 = address2;
        this.buildingName1 = buildingName1;
        this.floorNumber1 = floorNumber1;
        this.buildingName2 = buildingName2;
        this.floorNumber2 = floorNumber2;
        this.latitude1 = latitude1;
        this.longitude2 = longitude2;
        this.latitude2 = latitude2;
        this.streetName1 = streetName1;
        this.streetName2 = streetName2;
        this.defaultAddress = defaultAddress;
        this.defaultBuildingName = defaultBuildingName;
        this.defaultFloorNumber = defaultFloorNumber;
        this.defaultLongitude = defaultLongitude;
        this.defaultLatitude = defaultLatitude;

    }
}
