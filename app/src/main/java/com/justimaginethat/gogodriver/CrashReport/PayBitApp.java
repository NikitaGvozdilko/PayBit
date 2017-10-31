package com.justimaginethat.gogodriver.CrashReport;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.justimaginethat.gogodriver.ORMLite.DatabaseHelper;
import com.justimaginethat.gogodriver.ORMLite.DatabaseManager;
import com.justimaginethat.gogodriver.R;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
@ReportsCrashes(
//        formUri = "http://www.backendofyourchoice.com/reportpath",
        mailTo = "jayb611@gmail.com,lionvisionsandeeppc1@gmail.com,INFO@JITOFFICIAL.COM",
//        mailTo = "lionvisionsandeeppc1@gmail.com",
        customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },
        mode = ReportingInteractionMode.TOAST,

        resToastText = R.string.generic_error
)

public class PayBitApp extends Application {



    private static PayBitApp appInstance;
    private DatabaseHelper mdbHelper;
    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        getDatabaseHelper();


    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // Create an ConfigurationBuilder. It is prepopulated with values specified via annotation.
        // Set any additional value of the builder and then use it to construct an ACRAConfiguration.
//        final ACRAConfiguration config = new ConfigurationBuilder(this)
//                .setFoo(foo)
//                .setBar(bar)
//                .build();

        // Initialise ACRA


        ACRA.init(this);
        MultiDex.install(this);
        // register it with ACRA.
//        ACRA.getErrorReporter().setReportSender(reportSender);
    }
    public static PayBitApp getAppInstance() {
        checkInstance();
        return appInstance;
    }

    public static void checkInstance() {
        if (appInstance == null)
            throw new IllegalStateException("Application not created yet!");
    }

    public DatabaseHelper getDatabaseHelper() {
        if (mdbHelper == null) {
            DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
            mdbHelper = manager.getHelper(this);
        }
        return mdbHelper;
    }


}
