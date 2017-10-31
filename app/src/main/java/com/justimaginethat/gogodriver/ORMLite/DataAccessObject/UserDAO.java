package com.justimaginethat.gogodriver.ORMLite.DataAccessObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
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
public class UserDAO implements Serializable {
    private static final String TAG = UserDAO.class.getSimpleName();
    Dao<UserDCM, String> dao;
    private DatabaseHelper mDBHelper;

    public UserDAO(DatabaseHelper db) {
        try {
            mDBHelper = db;
            dao = db.getUserDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(UserDCM UserDCM) {
        try {
            return dao.create(UserDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(final List<UserDCM> userDCMList) {
        try {
            TransactionManager manager = new TransactionManager(
                    dao.getConnectionSource());
            manager.callInTransaction(new Callable() {
                @Override
                public Void call() throws Exception {
                    for (UserDCM UserDCM : userDCMList) {
                        dao.create(UserDCM);
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

    public int update(UserDCM UserDCM) {
        try {
            return dao.update(UserDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(UserDCM UserDCM) {
        try {

            return dao.delete(UserDCM);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteAll() {
        int noOfRecordAffected = 0;
        SQLiteDatabase sqliteDB = mDBHelper.getWritableDatabase();

        noOfRecordAffected = sqliteDB.delete(Database.TABLE.User.TableName, null, null);
        // sqliteDB.close();
        return noOfRecordAffected;
    }

    public List<UserDCM> getAll() throws SQLException {

        List<UserDCM> UserDCMList = new ArrayList<>();
        SQLiteDatabase sDatabase = mDBHelper.getReadableDatabase();
        Cursor cursor = sDatabase.rawQuery("select * from " + Database.TABLE.User.TableName, null);

        while (cursor.moveToNext()) {
            UserDCM UserDCM =new UserDCM(
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.idUser)),


                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.isCustomer))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.isDriver))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.isAdmin))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.workStatus))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.recordBySystem))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.isActive))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.User.isDeleted))==0)?false:true,


                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.attachment)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.type)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.address1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.countryCode)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.emailAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.firstName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.gcmId)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.mobileNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.password)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.profilePicture)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.udId)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.userName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.lastName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.passwordEncryptionKey)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.lastPasswordResetDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.otp)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.apnID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.address2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.longitude1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.latitude1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.longitude2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.latitude2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.streetName1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.streetName2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.sessionID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.sessionValue)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.sessionExpireyDateTime)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.sessionCreateTime)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.blockExpiry)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.userAccountStatus)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.lastChangeDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.entryByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.entryDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.changeByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.insertRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.updateRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.syncGUID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.buildingName1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.floorNumber1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.buildingName2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.floorNumber2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.defaultAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.defaultBuildingName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.defaultFloorNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.defaultLongitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.defaultLatitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.User.heading))
            );
            UserDCMList.add(UserDCM);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return UserDCMList;
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

    public UserDCM getDetail(int idRegistration) {
        try {
            QueryBuilder<UserDCM, String> qb = dao.queryBuilder();
            qb.where().eq(Database.TABLE.User.idUser, idRegistration);

            PreparedQuery<UserDCM> pq = qb.prepare();
            UserDCM UserDCM = dao.queryForFirst(pq);

            if (UserDCM != null)
                return UserDCM;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public List<UserDCM> getStoriesByTypes(String Type) {
//        try {
//            QueryBuilder<UserDCM, String> qb = dao.queryBuilder();
//            qb.where().eq(Database.TABLE.User.storyType, Type);
//
//            PreparedQuery<UserDCM> pq = qb.prepare();
//            List<UserDCM> UserDCMList = dao.query(pq);
//
//            if (UserDCMList != null)
//                return UserDCMList;
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
