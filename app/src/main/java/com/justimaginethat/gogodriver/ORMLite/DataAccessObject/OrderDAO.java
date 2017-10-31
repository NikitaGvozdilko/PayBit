package com.justimaginethat.gogodriver.ORMLite.DataAccessObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.ORMLite.DatabaseHelper;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by LION1 on 03-03-2017.
 */
public class OrderDAO implements Serializable {


    private static final String TAG = OrderDAO.class.getSimpleName();
    Dao<OrderDCM, String> dao;
    private DatabaseHelper mDBHelper;

    public OrderDAO(DatabaseHelper db) {
        try {
            mDBHelper = db;
            dao = db.getOrderDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(OrderDCM OrderDCM) {
        try {
            return dao.create(OrderDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(final List<OrderDCM> orderList) {
        try {
            TransactionManager manager = new TransactionManager(
                    dao.getConnectionSource());
            manager.callInTransaction(new Callable() {
                @Override
                public Void call() throws Exception {
                    for (OrderDCM OrderDCM : orderList) {
                        dao.create(OrderDCM);
                    }
                    return null;
                }
            });

        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(OrderDCM OrderDCM) {
        try {
            return dao.update(OrderDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(OrderDCM OrderDCM) {
        try {

            return dao.delete(OrderDCM);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteAll() {
        int noOfRecordAffected = 0;
        SQLiteDatabase sqliteDB = mDBHelper.getWritableDatabase();
        noOfRecordAffected = sqliteDB.delete(Database.TABLE.Order.TableName, null, null);
        return noOfRecordAffected;
    }
    public List<OrderDCM> getAll() throws SQLException {

        List<OrderDCM> OrderDCMList = new ArrayList<>();
        SQLiteDatabase sDatabase = mDBHelper.getReadableDatabase();
        Cursor cursor = sDatabase.rawQuery("select * from " + Database.TABLE.Order.TableName, null);

        while (cursor.moveToNext()) {
            OrderDCM OrderDCM =new OrderDCM(
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.idOrderMaster)),
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.idUserCustomer)),
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.idDeliveryPickupPoints)),
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.idUserDriver)),

                    cursor.getDouble(cursor.getColumnIndex(Database.TABLE.Order.productQuantity)),
                    cursor.getDouble(cursor.getColumnIndex(Database.TABLE.Order.fixedRateAmount)),
                    cursor.getDouble(cursor.getColumnIndex(Database.TABLE.Order.orderAmount)),


                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.isCompleted))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.paid))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.isActive))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.isDeleted))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Order.recordBySystem))==0)?false:true,

                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.orderTitle)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.productName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.quantityType)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.orderDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.orderTime)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.entryDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.lastChangeDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.entryByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.changeByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.insertRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.updateRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.syncGUID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.orderStatus)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.deliveryAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.deliveryLongitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.deliveryLatitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.deliveryBuildingName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Order.deliveryFloorNumber))
            );
            OrderDCMList.add(OrderDCM);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return OrderDCMList;
    }

    public int getCount() {
        try {
            return (int) dao.countOf();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;

    }

    public OrderDCM getDetail(int idOrder) {
        try {
            QueryBuilder<OrderDCM, String> qb = dao.queryBuilder();
            qb.where().eq(Database.TABLE.Order.idOrderMaster, idOrder);

            PreparedQuery<OrderDCM> pq = qb.prepare();
            OrderDCM OrderDCM = dao.queryForFirst(pq);

            if (OrderDCM != null)
                return OrderDCM;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public List<OrderDCM> getStoriesByTypes(String Type) {
//        try {
//            QueryBuilder<OrderDCM, String> qb = dao.queryBuilder();
//            qb.where().eq(Database.TABLE.Order.storyType, Type);
//
//            PreparedQuery<OrderDCM> pq = qb.prepare();
//            List<OrderDCM> OrderDCMList = dao.query(pq);
//
//            if (OrderDCMList != null)
//                return OrderDCMList;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void clear() {
        if (dao != null) {
            dao.clearObjectCache();
        }
        dao = null;
        mDBHelper = null;
    }
}
