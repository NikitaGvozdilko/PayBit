package com.justimaginethat.gogodriver.ORMLite.DataAccessObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
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
public class PickupPointsDAO implements Serializable {
    private static final String TAG = PickupPointsDAO.class.getSimpleName();
    Dao<PickupPointDCM, String> dao;
    private DatabaseHelper mDBHelper;

    public PickupPointsDAO(DatabaseHelper db) {
        try {
            mDBHelper = db;
            dao = db.getDeliveryPickupPointDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(PickupPointDCM PickupPointDCM) {
        try {
            return dao.create(PickupPointDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(final List<PickupPointDCM> pickupPointDCMList) {
        try {
            TransactionManager manager = new TransactionManager(
                    dao.getConnectionSource());
            manager.callInTransaction(new Callable() {
                @Override
                public Void call() throws Exception {
                    for (PickupPointDCM PickupPointDCM : pickupPointDCMList) {
                        dao.create(PickupPointDCM);
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

    public int update(PickupPointDCM PickupPointDCM) {
        try {
            return dao.update(PickupPointDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(PickupPointDCM PickupPointDCM) {
        try {

            return dao.delete(PickupPointDCM);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteAll() {
        int noOfRecordAffected = 0;
        SQLiteDatabase sqliteDB = mDBHelper.getWritableDatabase();

        noOfRecordAffected = sqliteDB.delete(Database.TABLE.DeliveryPickupPoint.TableName, null, null);
        // sqliteDB.close();
        return noOfRecordAffected;
    }

    public List<PickupPointDCM> getAll() throws SQLException {

        List<PickupPointDCM> pickupPointDCMList = new ArrayList<>();
        SQLiteDatabase sDatabase = mDBHelper.getReadableDatabase();
        Cursor cursor = sDatabase.rawQuery("select * from " + Database.TABLE.DeliveryPickupPoint.TableName, null);

        while (cursor.moveToNext()) {
            PickupPointDCM PickupPointDCM =new PickupPointDCM(

                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.idDeliveryPickupPoints)),
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.entryByUserName)),

                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.isActive))==0)?false:true,

                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.title)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.pickupAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.pickupLongitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.pickupLatitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.entryDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.lastChangeDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.changeByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.updateRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.syncGUID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.DeliveryPickupPoint.insertRoutePoint))
            );
            pickupPointDCMList.add(PickupPointDCM);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return pickupPointDCMList;
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

    public PickupPointDCM getDetail(int idDelivery) {
        try {
            QueryBuilder<PickupPointDCM, String> qb = dao.queryBuilder();
            qb.where().eq(Database.TABLE.DeliveryPickupPoint.idDeliveryPickupPoints, idDelivery);

            PreparedQuery<PickupPointDCM> pq = qb.prepare();
            PickupPointDCM PickupPointDCM = dao.queryForFirst(pq);

            if (PickupPointDCM != null)
                return PickupPointDCM;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public List<PickupPointDCM> getStoriesByTypes(String Type) {
//        try {
//            QueryBuilder<PickupPointDCM, String> qb = dao.queryBuilder();
//            qb.where().eq(Database.TABLE.DeliveryPickupPoint.storyType, Type);
//
//            PreparedQuery<PickupPointDCM> pq = qb.prepare();
//            List<PickupPointDCM> DeliveryPickupPointDCMList = dao.query(pq);
//
//            if (DeliveryPickupPointDCMList != null)
//                return DeliveryPickupPointDCMList;
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
