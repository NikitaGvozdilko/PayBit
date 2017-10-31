package com.justimaginethat.gogodriver.DomainModels;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;

/**
 * Created by Lion-1 on 1/30/2017.
 */
@DatabaseTable(tableName = Database.TABLE.User.TableName)
public class UserDCM extends BaseAPIErrorModel implements Serializable {
    @DatabaseField(columnName = Database.TABLE.User.idUser,id=true, generatedId = false )
//    public int idUser;
    public int idUser ;
    @DatabaseField(columnName = Database.TABLE.User.isCustomer)
    public boolean isCustomer ;
    @DatabaseField(columnName = Database.TABLE.User.isDriver)
    public boolean isDriver ;
    @DatabaseField(columnName = Database.TABLE.User.isAdmin)
    public boolean isAdmin ;
    @DatabaseField(columnName = Database.TABLE.User.workStatus)
    public boolean workStatus ;
    @DatabaseField(columnName = Database.TABLE.User.recordBySystem)
    public boolean recordBySystem ;
    @DatabaseField(columnName = Database.TABLE.User.isActive)
    public boolean isActive ;
    @DatabaseField(columnName = Database.TABLE.User.isDeleted)
    public boolean isDeleted ;
    @DatabaseField(columnName = Database.TABLE.User.attachment)
    public String attachment;
    @DatabaseField(columnName = Database.TABLE.User.type)
    public String type;
    @DatabaseField(columnName = Database.TABLE.User.address1)
    public String address1 ;
    @DatabaseField(columnName = Database.TABLE.User.countryCode)
    public String countryCode ;
    @DatabaseField(columnName = Database.TABLE.User.emailAddress)
    public String emailAddress ;
    @DatabaseField(columnName = Database.TABLE.User.firstName)
    public String firstName ;
    @DatabaseField(columnName = Database.TABLE.User.gcmId)
    public String gcmId ;
    @DatabaseField(columnName = Database.TABLE.User.mobileNumber)
    public String mobileNumber ;
    @DatabaseField(columnName = Database.TABLE.User.password)
    public String password ;
    @DatabaseField(columnName = Database.TABLE.User.profilePicture)
    public String profilePicture ;
    @DatabaseField(columnName = Database.TABLE.User.udId)
    public String udId ;
    @DatabaseField(columnName = Database.TABLE.User.userName)
    public String userName ;
    @DatabaseField(columnName = Database.TABLE.User.lastName)
    public String lastName ;
    @DatabaseField(columnName = Database.TABLE.User.passwordEncryptionKey)
    public String passwordEncryptionKey ;
    @DatabaseField(columnName = Database.TABLE.User.lastPasswordResetDate)
    public String lastPasswordResetDate ;
    @DatabaseField(columnName = Database.TABLE.User.otp)
    public String otp ;
    @DatabaseField(columnName = Database.TABLE.User.apnID)
    public String apnID ;
    @DatabaseField(columnName = Database.TABLE.User.address2)
    public String address2 ;
    @DatabaseField(columnName = Database.TABLE.User.longitude1)
    public String longitude1 ;
    @DatabaseField(columnName = Database.TABLE.User.latitude1)
    public String latitude1 ;
    @DatabaseField(columnName = Database.TABLE.User.longitude2)
    public String longitude2 ;
    @DatabaseField(columnName = Database.TABLE.User.latitude2)
    public String latitude2 ;
    @DatabaseField(columnName = Database.TABLE.User.streetName1)
    public String streetName1 ;
    @DatabaseField(columnName = Database.TABLE.User.streetName2)
    public String streetName2 ;
    @DatabaseField(columnName = Database.TABLE.User.sessionID)
    public String sessionID ;
    @DatabaseField(columnName = Database.TABLE.User.sessionValue)
    public String sessionValue ;
    @DatabaseField(columnName = Database.TABLE.User.sessionExpireyDateTime)
    public String sessionExpireyDateTime ;
    @DatabaseField(columnName = Database.TABLE.User.sessionCreateTime)
    public String sessionCreateTime ;
    @DatabaseField(columnName = Database.TABLE.User.blockExpiry)
    public String blockExpiry ;
    @DatabaseField(columnName = Database.TABLE.User.userAccountStatus)
    public String userAccountStatus ;
    @DatabaseField(columnName = Database.TABLE.User.lastChangeDate)
    public String lastChangeDate ;
    @DatabaseField(columnName = Database.TABLE.User.entryByUserName)
    public String entryByUserName ;
    @DatabaseField(columnName = Database.TABLE.User.entryDate)
    public String entryDate ;
    @DatabaseField(columnName = Database.TABLE.User.changeByUserName)
    public String changeByUserName ;
    @DatabaseField(columnName = Database.TABLE.User.insertRoutePoint)
    public String insertRoutePoint ;
    @DatabaseField(columnName = Database.TABLE.User.updateRoutePoint)
    public String updateRoutePoint ;
    @DatabaseField(columnName = Database.TABLE.User.syncGUID)
    public String syncGUID ;
    @DatabaseField(columnName = Database.TABLE.User.buildingName1)
    public String buildingName1 ;
    @DatabaseField(columnName = Database.TABLE.User.floorNumber1)
    public String floorNumber1 ;
    @DatabaseField(columnName = Database.TABLE.User.buildingName2)
    public String buildingName2 ;
    @DatabaseField(columnName = Database.TABLE.User.floorNumber2)
    public String floorNumber2 ;
    @DatabaseField(columnName = Database.TABLE.User.defaultAddress)
    public String defaultAddress;
    @DatabaseField(columnName = Database.TABLE.User.defaultBuildingName)
    public String defaultBuildingName;
    @DatabaseField(columnName = Database.TABLE.User.defaultFloorNumber)
    public String defaultFloorNumber;
    @DatabaseField(columnName = Database.TABLE.User.defaultLongitude)
    public String defaultLongitude;
    @DatabaseField(columnName = Database.TABLE.User.defaultLatitude)
    public String defaultLatitude;
    @DatabaseField(columnName = Database.TABLE.User.heading)
    public String heading;
    //    public Collection<CattleDCM> cattle;
    public UserDCM() {
    }



    public UserDCM(
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
            String sessionExpireyDateTime,
            String sessionCreateTime,
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
        this.sessionExpireyDateTime = sessionExpireyDateTime;
        this.sessionCreateTime = sessionCreateTime;
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
