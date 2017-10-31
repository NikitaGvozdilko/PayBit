package com.justimaginethat.gogodriver.DomainModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;

/**
 * Created by Lion-1 on 3/3/2017.
 */
@DatabaseTable(tableName = Database.TABLE.Order.TableName)
public class OrderDCM extends BaseAPIErrorModel implements Serializable {
    @DatabaseField(columnName = Database.TABLE.Order.idOrderMaster,id=true, generatedId = false )
    public int idOrderMaster;
    @DatabaseField(columnName = Database.TABLE.Order.idUserCustomer)
    public int idUserCustomer;
    @DatabaseField(columnName = Database.TABLE.Order.idDeliveryPickupPoints)
    public int idDeliveryPickupPoints;
    @DatabaseField(columnName = Database.TABLE.Order.idUserDriver)
    public int idUserDriver;
    @DatabaseField(columnName = Database.TABLE.Order.productQuantity)
    public Double productQuantity;
    @DatabaseField(columnName = Database.TABLE.Order.fixedRateAmount)
    public Double fixedRateAmount;
    @DatabaseField(columnName = Database.TABLE.Order.orderAmount)
    public Double orderAmount;
    @DatabaseField(columnName = Database.TABLE.Order.isCompleted)
    public boolean isCompleted;
    @DatabaseField(columnName = Database.TABLE.Order.paid)
    public boolean paid;
    @DatabaseField(columnName = Database.TABLE.Order.isActive)
    public boolean isActive;
    @DatabaseField(columnName = Database.TABLE.Order.isDeleted)
    public boolean isDeleted;
    @DatabaseField(columnName = Database.TABLE.Order.recordBySystem)
    public boolean recordBySystem;
    @DatabaseField(columnName = Database.TABLE.Order.orderTitle)
    public String orderTitle;
    @DatabaseField(columnName = Database.TABLE.Order.productName)
    public String productName;
    @DatabaseField(columnName = Database.TABLE.Order.quantityType)
    public String quantityType;
    @DatabaseField(columnName = Database.TABLE.Order.orderDate)
    public String orderDate;
    @DatabaseField(columnName = Database.TABLE.Order.orderTime)
    public String orderTime;
    @DatabaseField(columnName = Database.TABLE.Order.entryDate)
    public String entryDate;
    @DatabaseField(columnName = Database.TABLE.Order.lastChangeDate)
    public String lastChangeDate;
    @DatabaseField(columnName = Database.TABLE.Order.entryByUserName)
    public String entryByUserName;
    @DatabaseField(columnName = Database.TABLE.Order.changeByUserName)
    public String changeByUserName;
    @DatabaseField(columnName = Database.TABLE.Order.insertRoutePoint)
    public String insertRoutePoint;
    @DatabaseField(columnName = Database.TABLE.Order.updateRoutePoint)
    public String updateRoutePoint;
    @DatabaseField(columnName = Database.TABLE.Order.syncGUID)
    public String syncGUID;
    @DatabaseField(columnName = Database.TABLE.Order.orderStatus)
    public String orderStatus;
    @DatabaseField(columnName = Database.TABLE.Order.deliveryAddress)
    public String deliveryAddress;
    @DatabaseField(columnName = Database.TABLE.Order.deliveryLongitude)
    public String deliveryLongitude;
    @DatabaseField(columnName = Database.TABLE.Order.deliveryLatitude)
    public String deliveryLatitude;
    @DatabaseField(columnName = Database.TABLE.Order.deliveryBuildingName)
    public String deliveryBuildingName;
    @DatabaseField(columnName = Database.TABLE.Order.deliveryFloorNumber)
    public String deliveryFloorNumber;


    public OrderDCM() {
    }

    public OrderDCM(
            int idOrderMaster,
            int idUserCustomer,
            int idDeliveryPickupPoints,
            int idUserDriver,
            Double productQuantity,
            Double fixedRateAmount,
            Double orderAmount,
            boolean isCompleted,
            boolean paid,
            boolean isActive,
            boolean isDeleted,
            boolean recordBySystem,
            String orderTitle,
            String productName,
            String quantityType,
            String orderDate,
            String orderTime,
            String entryDate,
            String lastChangeDate,
            String entryByUserName,
            String changeByUserName,
            String insertRoutePoint,
            String updateRoutePoint,
            String syncGUID,
            String orderStatus,
            String deliveryAddress,
            String deliveryLongitude,
            String deliveryLatitude,
            String deliveryBuildingName,
            String deliveryFloorNumber
    ) {
        this.idOrderMaster = idOrderMaster;
        this.idUserCustomer = idUserCustomer;
        this.idDeliveryPickupPoints = idDeliveryPickupPoints;
        this.idUserDriver = idUserDriver;
        this.productQuantity = productQuantity;
        this.fixedRateAmount = fixedRateAmount;
        this.orderAmount = orderAmount;
        this.isCompleted = isCompleted;
        this.paid = paid;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.recordBySystem = recordBySystem;
        this.orderTitle = orderTitle;
        this.productName = productName;
        this.quantityType = quantityType;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.entryDate = entryDate;
        this.lastChangeDate = lastChangeDate;
        this.entryByUserName = entryByUserName;
        this.changeByUserName = changeByUserName;
        this.insertRoutePoint = insertRoutePoint;
        this.updateRoutePoint = updateRoutePoint;
        this.syncGUID = syncGUID;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
        this.deliveryLongitude = deliveryLongitude;
        this.deliveryLatitude = deliveryLatitude;
        this.deliveryBuildingName = deliveryBuildingName;
        this.deliveryFloorNumber = deliveryFloorNumber;
    }
}