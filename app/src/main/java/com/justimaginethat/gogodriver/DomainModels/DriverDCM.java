package com.justimaginethat.gogodriver.DomainModels;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;

/**
 * Created by Lion-1 on 1/30/2017.
 */
@DatabaseTable(tableName = Database.TABLE.Driver.TableName)
public class DriverDCM extends BaseAPIErrorModel implements Serializable {
    @DatabaseField(columnName = Database.TABLE.Driver.idUser,id=true, generatedId = false )
    public int idUser;
//    public int idUser ;
    @DatabaseField(columnName = Database.TABLE.Driver.isCustomer)
    public boolean isCustomer ;
    @DatabaseField(columnName = Database.TABLE.Driver.isDriver)
    public boolean isDriver ;
    @DatabaseField(columnName = Database.TABLE.Driver.isAdmin)
    public boolean isAdmin ;
    @DatabaseField(columnName = Database.TABLE.Driver.workStatus)
    public boolean workStatus ;
    @DatabaseField(columnName = Database.TABLE.Driver.recordBySystem)
    public boolean recordBySystem ;
    @DatabaseField(columnName = Database.TABLE.Driver.isActive)
    public boolean isActive ;
    @DatabaseField(columnName = Database.TABLE.Driver.isDeleted)
    public boolean isDeleted ;
    @DatabaseField(columnName = Database.TABLE.Driver.attachment)
    public String attachment;
    @DatabaseField(columnName = Database.TABLE.Driver.type)
    public String type;
    @DatabaseField(columnName = Database.TABLE.Driver.address1)
    public String address1 ;
    @DatabaseField(columnName = Database.TABLE.Driver.countryCode)
    public String countryCode ;
    @DatabaseField(columnName = Database.TABLE.Driver.emailAddress)
    public String emailAddress ;
    @DatabaseField(columnName = Database.TABLE.Driver.firstName)
    public String firstName ;
    @DatabaseField(columnName = Database.TABLE.Driver.gcmId)
    public String gcmId ;
    @DatabaseField(columnName = Database.TABLE.Driver.mobileNumber)
    public String mobileNumber ;
    @DatabaseField(columnName = Database.TABLE.Driver.password)
    public String password ;
    @DatabaseField(columnName = Database.TABLE.Driver.profilePicture)
    public String profilePicture ;
    @DatabaseField(columnName = Database.TABLE.Driver.udId)
    public String udId ;
    @DatabaseField(columnName = Database.TABLE.Driver.userName)
    public String userName ;
    @DatabaseField(columnName = Database.TABLE.Driver.lastName)
    public String lastName ;
    @DatabaseField(columnName = Database.TABLE.Driver.passwordEncryptionKey)
    public String passwordEncryptionKey ;
    @DatabaseField(columnName = Database.TABLE.Driver.lastPasswordResetDate)
    public String lastPasswordResetDate ;
    @DatabaseField(columnName = Database.TABLE.Driver.otp)
    public String otp ;
    @DatabaseField(columnName = Database.TABLE.Driver.apnID)
    public String apnID ;
    @DatabaseField(columnName = Database.TABLE.Driver.address2)
    public String address2 ;
    @DatabaseField(columnName = Database.TABLE.Driver.longitude1)
    public String longitude1 ;
    @DatabaseField(columnName = Database.TABLE.Driver.latitude1)
    public String latitude1 ;
    @DatabaseField(columnName = Database.TABLE.Driver.longitude2)
    public String longitude2 ;
    @DatabaseField(columnName = Database.TABLE.Driver.latitude2)
    public String latitude2 ;
    @DatabaseField(columnName = Database.TABLE.Driver.streetName1)
    public String streetName1 ;
    @DatabaseField(columnName = Database.TABLE.Driver.streetName2)
    public String streetName2 ;
    @DatabaseField(columnName = Database.TABLE.Driver.sessionID)
    public String sessionID;
    @DatabaseField(columnName = Database.TABLE.Driver.sessionValue)
    public String sessionValue;
    @DatabaseField(columnName = Database.TABLE.Driver.sessionExpireyDateTime)
    public String sessionExpireyDateTime;
    @DatabaseField(columnName = Database.TABLE.Driver.sessionCreateTime)
    public String sessionCreateTime;
    @DatabaseField(columnName = Database.TABLE.Driver.blockExpiry)
    public String blockExpiry ;
    @DatabaseField(columnName = Database.TABLE.Driver.userAccountStatus)
    public String userAccountStatus ;
    @DatabaseField(columnName = Database.TABLE.Driver.lastChangeDate)
    public String lastChangeDate ;
    @DatabaseField(columnName = Database.TABLE.Driver.entryByUserName)
    public String entryByUserName ;
    @DatabaseField(columnName = Database.TABLE.Driver.entryDate)
    public String entryDate ;
    @DatabaseField(columnName = Database.TABLE.Driver.changeByUserName)
    public String changeByUserName ;
    @DatabaseField(columnName = Database.TABLE.Driver.insertRoutePoint)
    public String insertRoutePoint ;
    @DatabaseField(columnName = Database.TABLE.Driver.updateRoutePoint)
    public String updateRoutePoint ;
    @DatabaseField(columnName = Database.TABLE.Driver.syncGUID)
    public String syncGUID ;
    @DatabaseField(columnName = Database.TABLE.Driver.buildingName1)
    public String buildingName1 ;
    @DatabaseField(columnName = Database.TABLE.Driver.floorNumber1)
    public String floorNumber1 ;
    @DatabaseField(columnName = Database.TABLE.Driver.buildingName2)
    public String buildingName2 ;
    @DatabaseField(columnName = Database.TABLE.Driver.floorNumber2)
    public String floorNumber2 ;


    @DatabaseField(columnName = Database.TABLE.Driver.defaultAddress)
    public String defaultAddress;
    @DatabaseField(columnName = Database.TABLE.Driver.defaultBuildingName)
    public String defaultBuildingName;
    @DatabaseField(columnName = Database.TABLE.Driver.defaultFloorNumber)
    public String defaultFloorNumber;
    @DatabaseField(columnName = Database.TABLE.Driver.defaultLongitude)
    public String defaultLongitude;
    @DatabaseField(columnName = Database.TABLE.Driver.defaultLatitude)
    public String defaultLatitude;
    @DatabaseField(columnName = Database.TABLE.Driver.heading)
    public String heading;

    //    public Collection<CattleDCM> cattle;
    public DriverDCM() {
    }



    public DriverDCM(
            int idUser,
//          int idUser1,
            boolean isCustomer,
            boolean isDriver,
            boolean isAdmin,
            boolean workStatus,
            boolean recordBySystem,
            boolean isActive,
            boolean isDeleted,
            String attachment,
            String type,
            String address1,
            String countryCode,
            String emailAddress,
            String firstName,
            String gcmId,
            String mobileNumber,
            String password,
            String profilePicture,
            String udId,
            String userName,
            String lastName,
            String passwordEncryptionKey,
            String lastPasswordResetDate,
            String otp,
            String apnID,
            String address2,
            String longitude1,
            String latitude1,
            String longitude2,
            String latitude2,
            String streetName1,
            String streetName2,
            String sessionID,
            String sessionValue,
            String DriverExpireyDateTime,
            String DriverCreateTime,
            String blockExpiry,
            String userAccountStatus,
            String lastChangeDate,
            String entryByUserName,
            String entryDate,
            String changeByUserName,
            String insertRoutePoint,
            String updateRoutePoint,
            String syncGUID,
            String buildingName1,
            String floorNumber1,
            String buildingName2,
            String floorNumber2,

            String defaultAddress,
            String defaultBuildingName,
            String defaultFloorNumber,
            String defaultLongitude,
            String defaultLatitude,
            String heading
    ) {
        this.idUser = idUser;
//      this.idUser = idUser1;
        this.isCustomer = isCustomer;
        this.isDriver = isDriver;
        this.isAdmin = isAdmin;
        this.workStatus = workStatus;
        this.recordBySystem = recordBySystem;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.attachment = attachment;
        this.type = type;
        this.address1 = address1;
        this.countryCode = countryCode;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.gcmId = gcmId;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.profilePicture = profilePicture;
        this.udId = udId;
        this.userName = userName;
        this.lastName = lastName;
        this.passwordEncryptionKey = passwordEncryptionKey;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.otp = otp;
        this.apnID = apnID;
        this.address2 = address2;
        this.longitude1 = longitude1;
        this.latitude1 = latitude1;
        this.longitude2 = longitude2;
        this.latitude2 = latitude2;
        this.streetName1 = streetName1;
        this.streetName2 = streetName2;
        this.sessionID = sessionID;
        this.sessionValue = sessionValue;
        this.sessionExpireyDateTime = DriverExpireyDateTime;
        this.sessionCreateTime = DriverCreateTime;
        this.blockExpiry = blockExpiry;
        this.userAccountStatus = userAccountStatus;
        this.lastChangeDate = lastChangeDate;
        this.entryByUserName = entryByUserName;
        this.entryDate = entryDate;
        this.changeByUserName = changeByUserName;
        this.insertRoutePoint = insertRoutePoint;
        this.updateRoutePoint = updateRoutePoint;
        this.syncGUID = syncGUID;
        this.buildingName1 = buildingName1;
        this.floorNumber1 = floorNumber1;
        this.buildingName2 = buildingName2;
        this.floorNumber2 = floorNumber2;

        this.defaultAddress = defaultAddress;
        this.defaultBuildingName = defaultBuildingName;
        this.defaultFloorNumber = defaultFloorNumber;
        this.defaultLongitude = defaultLongitude;
        this.defaultLatitude = defaultLatitude;
        this.heading = heading;

    }
}
