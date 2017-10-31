package com.justimaginethat.gogodriver.ORMLite.DataAccessObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.justimaginethat.gogodriver.DomainModels.TransactionDCM;
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

public class TransactionDAO  implements Serializable {
    private static final String TAG = TransactionDAO.class.getSimpleName();
    Dao<TransactionDCM, String> dao;
    private DatabaseHelper mDBHelper;
    public TransactionDAO(DatabaseHelper db) {
        try {
            mDBHelper = db;
            dao = db.getTransactionDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int create(TransactionDCM TransactionDCM) {
        try {
            return dao.create(TransactionDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(final List<TransactionDCM> TransactionDCMList) {
        try {
            TransactionManager manager = new TransactionManager(
                    dao.getConnectionSource());
            manager.callInTransaction(new Callable() {
                @Override
                public Void call() throws Exception {
                    for (TransactionDCM TransactionDCM : TransactionDCMList) {
                        dao.create(TransactionDCM);
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

    public int update(TransactionDCM TransactionDCM) {
        try {
            return dao.update(TransactionDCM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(TransactionDCM TransactionDCM) {
        try {

            return dao.delete(TransactionDCM);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteAll() {
        int noOfRecordAffected = 0;
        SQLiteDatabase sqliteDB = mDBHelper.getWritableDatabase();

        noOfRecordAffected = sqliteDB.delete(Database.TABLE.Transaction.TableName, null, null);
        // sqliteDB.close();
        return noOfRecordAffected;
    }

    public List<TransactionDCM> getAll() throws SQLException {

        List<TransactionDCM> TransactionDCMList = new ArrayList<>();
        SQLiteDatabase sDatabase = mDBHelper.getReadableDatabase();
        Cursor cursor = sDatabase.rawQuery("select * from " + Database.TABLE.Transaction.TableName, null);

        while (cursor.moveToNext()) {
            TransactionDCM TransactionDCM =new TransactionDCM(
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Transaction.idTransactionMaster)),
                    cursor.getInt(cursor.getColumnIndex(Database.TABLE.Transaction.idOrderMaster)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.utf8)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.invoiceNumber)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_card_number)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_locale)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.signature)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.auth_trans_ref_no)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_surname)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_address_city)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_card_expiry_date)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_address_postal_code)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_phone)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.reason_code)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.auth_amount)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.auth_response)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_forename)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_payment_method)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.request_token)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_device_fingerprint_id)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.auth_time)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_amount)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_email)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.transaction_id)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.auth_avs_code_raw)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_currency)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_card_type)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.decision)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.message)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.signed_field_names)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_transaction_uuid)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.auth_avs_code)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.auth_code)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_address_country)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_transaction_type)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_access_key)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_profile_id)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_reference_number)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_address_state)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.signed_date_time)),
                    cursor.getString(cursor.getColumnIndex(Database.TABLE.Transaction.req_bill_to_address_line1))
                    );
            TransactionDCMList.add(TransactionDCM);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return TransactionDCMList;
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

    public TransactionDCM getDetail(int idRegistration) {
        try {
            QueryBuilder<TransactionDCM, String> qb = dao.queryBuilder();
            qb.where().eq(Database.TABLE.Transaction.idTransactionMaster, idRegistration);//change to id number .................

            PreparedQuery<TransactionDCM> pq = qb.prepare();
            TransactionDCM TransactionDCM = dao.queryForFirst(pq);

            if (TransactionDCM != null)
                return TransactionDCM;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public List<TransactionDCM> getStoriesByTypes(String Type) {
//        try {
//            QueryBuilder<TransactionDCM, String> qb = dao.queryBuilder();
//            qb.where().eq(Database.TABLE.Transaction., Type);
//
//            PreparedQuery<TransactionDCM> pq = qb.prepare();
//            List<TransactionDCM> TransactionDCMList = dao.query(pq);
//
//            if (TransactionDCMList != null)
//                return TransactionDCMList;
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
