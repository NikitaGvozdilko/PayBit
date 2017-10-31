package com.justimaginethat.gogodriver.ORMLite.DataAccessObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
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
public class SessionDAO implements Serializable {
    private static final String TAG = SessionDAO.class.getSimpleName();
    Dao<SessionDCM, String> dao;
    private DatabaseHelper mDBHelper;

    public SessionDAO(DatabaseHelper db) {
        try {
            mDBHelper = db;
            dao = db.getSessionDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(SessionDCM SessionDCM) {
        try {
            return dao.create(SessionDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(final List<SessionDCM> SessionDCMList) {
        try {
            TransactionManager manager = new TransactionManager(
                    dao.getConnectionSource());
            manager.callInTransaction(new Callable() {
                @Override
                public Void call() throws Exception {
                    for (SessionDCM SessionDCM : SessionDCMList) {
                        dao.create(SessionDCM);
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

    public int update(SessionDCM SessionDCM) {
        try {
            return dao.update(SessionDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(SessionDCM SessionDCM) {
        try {

            return dao.delete(SessionDCM);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteAll() {
        int noOfRecordAffected = 0;
        SQLiteDatabase sqliteDB = mDBHelper.getWritableDatabase();

        noOfRecordAffected = sqliteDB.delete(Database.TABLE.Session.TableName, null, null);
        // sqliteDB.close();
        return noOfRecordAffected;
    }

    public List<SessionDCM> getAll() throws SQLException {

        List<SessionDCM> SessionDCMList = new ArrayList<>();
        SQLiteDatabase sDatabase = mDBHelper.getReadableDatabase();
        Cursor cursor = sDatabase.rawQuery("select * from " + Database.TABLE.Session.TableName, null);

        while (cursor.moveToNext()) {
            SessionDCM SessionDCM =new SessionDCM(
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.idUser)),


                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.isCustomer))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.isDriver))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.isAdmin))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.workStatus))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.recordBySystem))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.isActive))==0)?false:true,
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Session.isDeleted))==0)?false:true,


                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.attachment)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.type)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.address1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.countryCode)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.emailAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.firstName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.gcmId)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.mobileNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.password)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.profilePicture)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.udId)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.userName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.lastName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.passwordEncryptionKey)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.lastPasswordResetDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.otp)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.apnID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.address2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.longitude1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.latitude1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.longitude2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.latitude2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.streetName1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.streetName2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.sessionID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.sessionValue)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.sessionExpireyDateTime)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.sessionCreateTime)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.blockExpiry)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.userAccountStatus)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.lastChangeDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.entryByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.entryDate)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.changeByUserName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.insertRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.updateRoutePoint)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.syncGUID)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.buildingName1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.floorNumber1)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.buildingName2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.floorNumber2)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.defaultAddress)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.defaultBuildingName)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.defaultFloorNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.defaultLongitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.defaultLatitude)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Session.heading))


            );
            SessionDCMList.add(SessionDCM);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return SessionDCMList;
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

    public SessionDCM getDetail(int idRegistration) {
        try {
            QueryBuilder<SessionDCM, String> qb = dao.queryBuilder();
            qb.where().eq(Database.TABLE.Session.idUser, idRegistration);

            PreparedQuery<SessionDCM> pq = qb.prepare();
            SessionDCM SessionDCM = dao.queryForFirst(pq);

            if (SessionDCM != null)
                return SessionDCM;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public List<SessionDCM> getStoriesByTypes(String Type) {
//        try {
//            QueryBuilder<SessionDCM, String> qb = dao.queryBuilder();
//            qb.where().eq(Database.TABLE.User.storyType, Type);
//
//            PreparedQuery<SessionDCM> pq = qb.prepare();
//            List<SessionDCM> SessionDCMList = dao.query(pq);
//
//            if (SessionDCMList != null)
//                return SessionDCMList;
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
