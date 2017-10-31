package com.justimaginethat.gogodriver.ORMLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.DomainModels.DriverDCM;
import com.justimaginethat.gogodriver.DomainModels.OrderDCM;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.DomainModels.TransactionDCM;
import com.justimaginethat.gogodriver.DomainModels.UserDCM;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


/**
 * Created by LION1 on 03-03-2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    //Step Two+DCM....


    private static final String DATABASE_NAME = Database.DatabaseName + ".db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static String DB_PATH = "";
    private Context mContext;

    private Dao<UserDCM, String> UserDao = null;
    private Dao<OrderDCM, String> OrderDao = null;
    private Dao<PickupPointDCM, String> DeliveryPickupPointDao = null;
    private Dao<SessionDCM, String> SessionDao = null;
    private Dao<DriverDCM, String> DriverDao = null;
    private Dao<CardsDCM, String> CardsDao = null;
    private Dao<TransactionDCM, String> TransactionDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.

            TableUtils.createTable(connectionSource, OrderDCM.class);
            TableUtils.createTable(connectionSource, PickupPointDCM.class);
            TableUtils.createTable(connectionSource, SessionDCM.class);
            TableUtils.createTable(connectionSource, DriverDCM.class);
            TableUtils.createTable(connectionSource, UserDCM.class);
            TableUtils.createTable(connectionSource, CardsDCM.class);
            TableUtils.createTable(connectionSource, TransactionDCM.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.
            backUPDb();
            TableUtils.dropTable(connectionSource, UserDCM.class, true);
            TableUtils.dropTable(connectionSource, OrderDCM.class, true);
            TableUtils.dropTable(connectionSource, PickupPointDCM.class, true);
            TableUtils.dropTable(connectionSource, SessionDCM.class, true);
            TableUtils.dropTable(connectionSource, DriverDCM.class, true);
            TableUtils.dropTable(connectionSource, CardsDCM.class, true);
            TableUtils.dropTable(connectionSource, TransactionDCM.class, true);

            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getWritableDatabase();
        }

    }

    private boolean checkDatabase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            return false;
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        try {
            backUPDb();
            onCreate(db);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void upgradeDatabase(SQLiteDatabase pDb, int pFromVersion,
                                 int pToVersion) {
        if (mContext != null) {
            InputStream inputStream = null;
            try {
                backUPDb();
                final String resourceName = String.format("raw/from_%d_to_%d",
                        pFromVersion, pToVersion);
                inputStream = mContext.getResources().openRawResource(
                        mContext.getResources().getIdentifier(resourceName,
                                "raw", mContext.getPackageName()));
                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    pDb.beginTransaction();
                    String query;

                    while ((query = bufferedReader.readLine()) != null) {
                        pDb.execSQL(query);
                    }
                    pDb.setTransactionSuccessful();
                    pDb.endTransaction();
                }
            } catch (Throwable e) {

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Dao<UserDCM, String> getUserDAO() throws SQLException {
        if (UserDao == null) {
            UserDao = DaoManager.createDao(getConnectionSource(),UserDCM.class);
        }
        return UserDao;
    }

    public Dao<PickupPointDCM, String> getDeliveryPickupPointDAO() throws SQLException {
        if (DeliveryPickupPointDao == null) {
            DeliveryPickupPointDao = DaoManager.createDao(getConnectionSource(),PickupPointDCM.class);
        }
        return DeliveryPickupPointDao;
    }
    public Dao<SessionDCM, String> getSessionDAO() throws SQLException {
        if (SessionDao == null) {
            SessionDao = DaoManager.createDao(getConnectionSource(),SessionDCM.class);
        }
        return SessionDao;
    }
    public Dao<DriverDCM, String> getDriverDAO() throws SQLException {
        if (DriverDao == null) {
            DriverDao = DaoManager.createDao(getConnectionSource(),DriverDCM.class);
        }
        return DriverDao;
    }


    public Dao<OrderDCM, String> getOrderDAO() throws SQLException {
        if (OrderDao == null) {
            OrderDao = DaoManager.createDao(getConnectionSource(),OrderDCM.class);
        }
        return OrderDao;
    }


    public Dao<CardsDCM, String> getCardsDAO() throws SQLException {
        if (CardsDao == null) {
            CardsDao = DaoManager.createDao(getConnectionSource(),CardsDCM.class);
        }
        return CardsDao;
    }


    public Dao<TransactionDCM, String> getTransactionDAO() throws SQLException {
        if (TransactionDao == null) {
            TransactionDao = DaoManager.createDao(getConnectionSource(),TransactionDCM.class);
        }
        return TransactionDao;
    }




    public void backUPDb() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new java.util.Date());
        final String inFileName = DB_PATH;
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory() + "/cattle_copy"+currentDateandTime+".db";

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }
    @Override
    public void close() {
        UserDao = null;
        OrderDao = null;
        DeliveryPickupPointDao = null;
        SessionDao = null;
        DriverDao = null;
        CardsDao=null;
        TransactionDao=null;
        super.close();
    }
}
