package com.justimaginethat.gogodriver.DomainModels;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;

/**
 * Created by Lion-1 on 1/30/2017.
 */
@DatabaseTable(tableName = Database.TABLE.Session.TableName)
public class SessionDCM extends BaseAPIErrorModel implements Serializable {


    @DatabaseField(columnName = Database.TABLE.Session.idUser,id=true, generatedId = false )
//    public int idUser;
    public int idUser ;
    @DatabaseField(columnName = Database.TABLE.Session.isCustomer)
    public boolean isCustomer ;
    @DatabaseField(columnName = Database.TABLE.Session.isDriver)
    public boolean isDriver ;
    @DatabaseField(columnName = Database.TABLE.Session.isAdmin)
    public boolean isAdmin ;
    @DatabaseField(columnName = Database.TABLE.Session.workStatus)
    public boolean workStatus ;
    @DatabaseField(columnName = Database.TABLE.Session.recordBySystem)
    public boolean recordBySystem ;
    @DatabaseField(columnName = Database.TABLE.Session.isActive)
    public boolean isActive ;
    @DatabaseField(columnName = Database.TABLE.Session.isDeleted)
    public boolean isDeleted ;
    @DatabaseField(columnName = Database.TABLE.Session.attachment)
    public String attachment;
    @DatabaseField(columnName = Database.TABLE.Session.type)
    public String type;
    @DatabaseField(columnName = Database.TABLE.Session.address1)
    public String address1 ;
    @DatabaseField(columnName = Database.TABLE.Session.countryCode)
    public String countryCode ;
    @DatabaseField(columnName = Database.TABLE.Session.emailAddress)
    public String emailAddress ;
    @DatabaseField(columnName = Database.TABLE.Session.firstName)
    public String firstName ;
    @DatabaseField(columnName = Database.TABLE.Session.gcmId)
    public String gcmId ;
    @DatabaseField(columnName = Database.TABLE.Session.mobileNumber)
    public String mobileNumber ;
    @DatabaseField(columnName = Database.TABLE.Session.password)
    public String password ;
    @DatabaseField(columnName = Database.TABLE.Session.profilePicture)
    public String profilePicture ;
    @DatabaseField(columnName = Database.TABLE.Session.udId)
    public String udId ;
    @DatabaseField(columnName = Database.TABLE.Session.userName)
    public String userName ;
    @DatabaseField(columnName = Database.TABLE.Session.lastName)
    public String lastName ;
    @DatabaseField(columnName = Database.TABLE.Session.passwordEncryptionKey)
    public String passwordEncryptionKey ;
    @DatabaseField(columnName = Database.TABLE.Session.lastPasswordResetDate)
    public String lastPasswordResetDate ;
    @DatabaseField(columnName = Database.TABLE.Session.otp)
    public String otp ;
    @DatabaseField(columnName = Database.TABLE.Session.apnID)
    public String apnID ;
    @DatabaseField(columnName = Database.TABLE.Session.address2)
    public String address2 ;
    @DatabaseField(columnName = Database.TABLE.Session.longitude1)
    public String longitude1 ;
    @DatabaseField(columnName = Database.TABLE.Session.latitude1)
    public String latitude1 ;
    @DatabaseField(columnName = Database.TABLE.Session.longitude2)
    public String longitude2 ;
    @DatabaseField(columnName = Database.TABLE.Session.latitude2)
    public String latitude2 ;
    @DatabaseField(columnName = Database.TABLE.Session.streetName1)
    public String streetName1 ;
    @DatabaseField(columnName = Database.TABLE.Session.streetName2)
    public String streetName2 ;
    @DatabaseField(columnName = Database.TABLE.Session.sessionID)
    public String sessionID ;
    @DatabaseField(columnName = Database.TABLE.Session.sessionValue)
    public String sessionValue ;
    @DatabaseField(columnName = Database.TABLE.Session.sessionExpireyDateTime)
    public String sessionExpireyDateTime ;
    @DatabaseField(columnName = Database.TABLE.Session.sessionCreateTime)
    public String sessionCreateTime ;
    @DatabaseField(columnName = Database.TABLE.Session.blockExpiry)
    public String blockExpiry ;
    @DatabaseField(columnName = Database.TABLE.Session.userAccountStatus)
    public String userAccountStatus ;
    @DatabaseField(columnName = Database.TABLE.Session.lastChangeDate)
    public String lastChangeDate ;
    @DatabaseField(columnName = Database.TABLE.Session.entryByUserName)
    public String entryByUserName ;
    @DatabaseField(columnName = Database.TABLE.Session.entryDate)
    public String entryDate ;
    @DatabaseField(columnName = Database.TABLE.Session.changeByUserName)
    public String changeByUserName ;
    @DatabaseField(columnName = Database.TABLE.Session.insertRoutePoint)
    public String insertRoutePoint ;
    @DatabaseField(columnName = Database.TABLE.Session.updateRoutePoint)
    public String updateRoutePoint ;
    @DatabaseField(columnName = Database.TABLE.Session.syncGUID)
    public String syncGUID ;
    @DatabaseField(columnName = Database.TABLE.Session.buildingName1)
    public String buildingName1 ;
    @DatabaseField(columnName = Database.TABLE.Session.floorNumber1)
    public String floorNumber1 ;
    @DatabaseField(columnName = Database.TABLE.Session.buildingName2)
    public String buildingName2 ;
    @DatabaseField(columnName = Database.TABLE.Session.floorNumber2)
    public String floorNumber2 ;

    @DatabaseField(columnName = Database.TABLE.Session.defaultAddress)
    public String defaultAddress;
    @DatabaseField(columnName = Database.TABLE.Session.defaultBuildingName)
    public String defaultBuildingName;
    @DatabaseField(columnName = Database.TABLE.Session.defaultFloorNumber)
    public String defaultFloorNumber;
    @DatabaseField(columnName = Database.TABLE.Session.defaultLongitude)
    public String defaultLongitude;
    @DatabaseField(columnName = Database.TABLE.Session.defaultLatitude)
    public String defaultLatitude;
    @DatabaseField(columnName = Database.TABLE.Session.heading)
    public String heading;



    //    public Collection<CattleDCM> cattle;
    public SessionDCM() {

        isCustomer = false;
        isDriver = false;
        isAdmin = false;
        workStatus = false;
        recordBySystem = false;
        isActive = false;
        isDeleted = false;
        attachment = "" ;
        type = "" ;
        address1  = "" ;
        countryCode  = "" ;
        emailAddress  = "" ;
        firstName  = "" ;
        gcmId  = "" ;
        mobileNumber  = "" ;
        password  = "" ;
        profilePicture  = "" ;
        udId  = "" ;
        userName  = "" ;
        lastName  = "" ;
        passwordEncryptionKey  = "" ;
        lastPasswordResetDate  = "" ;
        otp  = "" ;
        apnID  = "" ;
        address2  = "" ;
        longitude1  = "" ;
        latitude1  = "" ;
        longitude2  = "" ;
        latitude2  = "" ;
        streetName1  = "" ;
        streetName2  = "" ;
        sessionID  = "" ;
        sessionValue  = "" ;
        sessionExpireyDateTime  = "" ;
        sessionCreateTime  = "" ;
        blockExpiry  = "" ;
        userAccountStatus  = "" ;
        lastChangeDate  = "" ;
        entryByUserName  = "" ;
        entryDate  = "" ;
        changeByUserName  = "" ;
        insertRoutePoint  = "" ;
        updateRoutePoint  = "" ;
        syncGUID  = "" ;
        buildingName1  = "" ;
        floorNumber1  = "" ;
        buildingName2  = "" ;
        floorNumber2  = "" ;

        defaultAddress = "" ;
        defaultBuildingName = "" ;
        defaultFloorNumber = "" ;
        defaultLongitude = "" ;
        defaultLatitude = "" ;
        heading = "" ;



    }



    public SessionDCM(
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
//        this.idUser = idUser;
      this.idUser = idUser;
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
