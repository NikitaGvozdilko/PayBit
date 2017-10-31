package com.justimaginethat.gogodriver.ORMLite.DataAccessObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.ORMLite.DatabaseHelper;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Lion-1 on 3/25/2017.
 */

public class CardsDAO implements Serializable {
    private static final String TAG = CardsDAO.class.getSimpleName();
    Dao<CardsDCM, String> dao;
    private DatabaseHelper mDBHelper;
    public CardsDAO(DatabaseHelper db) {
        try {
            mDBHelper = db;
            dao = db.getCardsDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(CardsDCM CardsDCM) {
        try {
            return dao.create(CardsDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(final List<CardsDCM> CardsDCMList) {
        try {
            TransactionManager manager = new TransactionManager(
                    dao.getConnectionSource());
            manager.callInTransaction(new Callable() {
                @Override
                public Void call() throws Exception {
                    for (CardsDCM CardsDCM : CardsDCMList) {
                        dao.create(CardsDCM);
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

    public int update(CardsDCM CardsDCM) {
        try {
            return dao.update(CardsDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(CardsDCM CardsDCM) {
        try {

            return dao.delete(CardsDCM);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteAll() {
        int noOfRecordAffected = 0;
        SQLiteDatabase sqliteDB = mDBHelper.getWritableDatabase();

        noOfRecordAffected = sqliteDB.delete(Database.TABLE.Cards.TableName, null, null);
        // sqliteDB.close();
        return noOfRecordAffected;
    }

    public List<CardsDCM> getAll() throws SQLException {

        List<CardsDCM> CardsDCMList = new ArrayList<>();
        SQLiteDatabase sDatabase = mDBHelper.getReadableDatabase();
        Cursor cursor = sDatabase.rawQuery("select * from " + Database.TABLE.Cards.TableName, null);

        while (cursor.moveToNext()) {
            CardsDCM CardsDCM =new CardsDCM(
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Cards.id))),
                    (cursor.getInt(cursor.getColumnIndex(Database.TABLE.Cards.isDefault))==0)?false:true,
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Cards.number)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Cards.expiryMonth)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Cards.expiryYear)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Cards.cvvNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Cards.type)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Cards.paymentToken))
            );
            CardsDCMList.add(CardsDCM);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return CardsDCMList;
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

    public CardsDCM getDetail(int idRegistration) {
        try {
            QueryBuilder<CardsDCM, String> qb = dao.queryBuilder();
            qb.where().eq(Database.TABLE.Cards.number, idRegistration);//change to id number .................

            PreparedQuery<CardsDCM> pq = qb.prepare();
            CardsDCM CardsDCM = dao.queryForFirst(pq);

            if (CardsDCM != null)
                return CardsDCM;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public List<CardsDCM> getStoriesByTypes(String Type) {
//        try {
//            QueryBuilder<CardsDCM, String> qb = dao.queryBuilder();
//            qb.where().eq(Database.TABLE.Cards.storyType, Type);
//
//            PreparedQuery<CardsDCM> pq = qb.prepare();
//            List<CardsDCM> CardsDCMList = dao.query(pq);
//
//            if (CardsDCMList != null)
//                return CardsDCMList;
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
