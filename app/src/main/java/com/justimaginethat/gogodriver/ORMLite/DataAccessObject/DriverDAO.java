package com.justimaginethat.gogodriver.ORMLite.DataAccessObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
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
public class DriverDAO implements Serializable {
    private static final String TAG = DriverDAO.class.getSimpleName();
    Dao<DriverDCM, String> dao;
    private DatabaseHelper mDBHelper;
    public DriverDAO(DatabaseHelper db) {
        try {
            mDBHelper = db;
            dao = db.getDriverDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(DriverDCM DriverDCM) {
        try {
            return dao.create(DriverDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(final List<DriverDCM> DriverDCMList) {
        try {
            TransactionManager manager = new TransactionManager(
                    dao.getConnectionSource());
            manager.callInTransaction(new Callable() {
                @Override
                public Void call() throws Exception {
                    for (DriverDCM DriverDCM : DriverDCMList) {
                        dao.create(DriverDCM);
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

    public int update(DriverDCM DriverDCM) {
        try {
            return dao.update(DriverDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(DriverDCM DriverDCM) {
        try {

            return dao.delete(DriverDCM);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteAll() {
        int noOfRecordAffected = 0;
        SQLiteDatabase sqliteDB = mDBHelper.getWritableDatabase();

        noOfRecordAffected = sqliteDB.delete(Database.TABLE.Driver.TableName, null, null);
        // sqliteDB.close();
        return noOfRecordAffected;
    }

    public List<DriverDCM> getAll() throws SQLException {

        List<DriverDCM> DriverDCMList = new ArrayList<>();
        SQLiteDatabase sDatabase = mDBHelper.getReadableDatabase();
        Cursor cursor = sDatabase.rawQuery("select * from " + Database.TABLE.Driver.TableName, null);

        while (cursor.moveToNext()) {
            DriverDCM DriverDCM =new DriverDCM(

                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.idUser)),


                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.isCustomer))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.isDriver))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.isAdmin))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.workStatus))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.recordBySystem))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.isActive))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Driver.isDeleted))==0)?false:true,


                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.attachment)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.type)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.address1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.countryCode)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.emailAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.firstName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.gcmId)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.mobileNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.password)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.profilePicture)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.udId)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.userName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.lastName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.passwordEncryptionKey)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.lastPasswordResetDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.otp)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.apnID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.address2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.longitude1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.latitude1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.longitude2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.latitude2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.streetName1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.streetName2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.sessionID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.sessionValue)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.sessionExpireyDateTime)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.sessionCreateTime)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.blockExpiry)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.userAccountStatus)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.lastChangeDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.entryByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.entryDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.changeByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.insertRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.updateRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.syncGUID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.buildingName1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.floorNumber1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.buildingName2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.floorNumber2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.defaultAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.defaultBuildingName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.defaultFloorNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.defaultLongitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.defaultLatitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Driver.heading))
            );
            DriverDCMList.add(DriverDCM);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return DriverDCMList;
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

    public DriverDCM getDetail(int idRegistration) {
        try {
            QueryBuilder<DriverDCM, String> qb = dao.queryBuilder();
            qb.where().eq(Database.TABLE.Driver.idUser, idRegistration);

            PreparedQuery<DriverDCM> pq = qb.prepare();
            DriverDCM DriverDCM = dao.queryForFirst(pq);

            if (DriverDCM != null)
                return DriverDCM;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public List<DriverDCM> getStoriesByTypes(String Type) {
//        try {
//            QueryBuilder<DriverDCM, String> qb = dao.queryBuilder();
//            qb.where().eq(Database.TABLE.User.storyType, Type);
//
//            PreparedQuery<DriverDCM> pq = qb.prepare();
//            List<DriverDCM> DriverDCMList = dao.query(pq);
//
//            if (DriverDCMList != null)
//                return DriverDCMList;
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
