package com.justimaginethat.gogodriver.DomainModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;

/**
 * Created by Lion-1 on 2/21/2017.
 */

@DatabaseTable(tableName = Database.TABLE.DeliveryPickupPoint.TableName)

public class PickupPointDCM extends BaseAPIErrorModel implements Serializable {

    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.idDeliveryPickupPoints,id=true, generatedId = false )
    public int idDeliveryPickupPoints;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.entryByUserName)
    public  int entryByUserName;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.isActive)
    public  boolean isActive;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.title)
    public  String title;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.pickupAddress)
    public  String pickupAddress;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.pickupLongitude)
    public  String pickupLongitude;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.pickupLatitude)
    public  String pickuplatitude;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.entryDate)
    public  String entryDate;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.lastChangeDate)
    public  String lastChangeDate;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.changeByUserName)
    public  String changeByUserName;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.updateRoutePoint)
    public  String updateRoutePoint;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.syncGUID)
    public  String syncGUID;
    @DatabaseField(columnName = Database.TABLE.DeliveryPickupPoint.insertRoutePoint)
    public  String insertRoutePoint;

    public PickupPointDCM() {
    }

    public PickupPointDCM(
            int idDeliveryPickupPoints,
            int entryByUserName,
            Boolean isActive,
            String title,
            String pickupAddress,
            String pickupLongitude,
            String pickuplatitude,
            String entryDate,
            String lastChangeDate,
            String changeByUserName,
            String updateRoutePoint,
            String syncGUID,
            String insertRoutePoint
    ) {
        this.idDeliveryPickupPoints = idDeliveryPickupPoints;
        this.entryByUserName = entryByUserName;
        this.isActive = isActive;
        this.title = title;
        this.pickupAddress = pickupAddress;
        this.pickupLongitude = pickupLongitude;
        this.pickuplatitude = pickuplatitude;
        this.entryDate = entryDate;
        this.lastChangeDate = lastChangeDate;
        this.changeByUserName = changeByUserName;
        this.updateRoutePoint = updateRoutePoint;
        this.syncGUID = syncGUID;
        this.insertRoutePoint = insertRoutePoint;
    }
}
