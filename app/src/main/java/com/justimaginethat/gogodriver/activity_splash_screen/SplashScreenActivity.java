package com.justimaginethat.gogodriver.activity_splash_screen;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.DriverDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.OrderDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.UserDAO;
import com.justimaginethat.gogodriver.ORMLite.DatabaseHelper;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_login_pkg.activity_login;
import com.justimaginethat.gogodriver.activity_splash_screen.AsynchronousTask.GetUserDetails;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class SplashScreenActivity extends Activity {

	// Note: Your consumer key and secret should be obfuscated in your source code before shipping.

	public DatabaseHelper mydb ;
	private static final long TIMEINMILLIS = 3000;
	LionUtilities helperSP = new LionUtilities();
	private static final int REQUEST_WRITE_STORAGE = 101;
	public Context context;

	private List<SessionDCM> sessionDCMs = new ArrayList<SessionDCM>();

	public Cursor cursor;
	private int progress = 0;
	private boolean isRun = true;

	public SessionDAO sessionDAO;
	public SessionDCM sessionDCM;

	public OrderDAO orderDAO;
	public DriverDAO driverDAO;
	public UserDAO userDAO;


	public SplashScreenActivity() {
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case REQUEST_WRITE_STORAGE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED
						&& grantResults[1] == PackageManager.PERMISSION_GRANTED
						&& grantResults[2] == PackageManager.PERMISSION_GRANTED
						&& grantResults[3] == PackageManager.PERMISSION_GRANTED
						&& grantResults[4] == PackageManager.PERMISSION_GRANTED
						&& grantResults[5] == PackageManager.PERMISSION_GRANTED
						&& grantResults[6] == PackageManager.PERMISSION_GRANTED
						&& grantResults[7] == PackageManager.PERMISSION_GRANTED
						&& grantResults[8] == PackageManager.PERMISSION_GRANTED
						&& grantResults[9] == PackageManager.PERMISSION_GRANTED
						&& grantResults[10] == PackageManager.PERMISSION_GRANTED
						) {
					AskUserToTurnONGPS();


				} else {


					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					Toast t = Toast.makeText(context, "You must need to allow all permission to use app!", Toast.LENGTH_SHORT);
					t.show();
					this.finish();
				}
				return;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen_runp);
		context = this;


//		AskUserToTurnONGPS();



		if (helperSP.isGooglePlayServicesAvailable(this) == 1) {
			if (Build.VERSION.SDK_INT >= 23) {
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED

						|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
						|| ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED

						) {
					ActivityCompat.requestPermissions(this,
							new String[]{android.Manifest.permission.INTERNET,
									Manifest.permission.ACCESS_COARSE_LOCATION,
									Manifest.permission.ACCESS_FINE_LOCATION,
									Manifest.permission.WRITE_EXTERNAL_STORAGE,
									Manifest.permission.READ_EXTERNAL_STORAGE,
									Manifest.permission.VIBRATE,
									Manifest.permission.CAMERA,
									Manifest.permission.ACCESS_NETWORK_STATE,
									Manifest.permission.READ_CONTACTS,
									Manifest.permission.READ_PHONE_STATE,
									Manifest.permission.CALL_PHONE,
							},
							REQUEST_WRITE_STORAGE);
				} else {
					if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
							&& ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
							) {
						AskUserToTurnONGPS();
					} else {
						AskUserToTurnONGPS();
//						finish();
// continueExecution();
					}
				}

			} else {
				AskUserToTurnONGPS();
			}
		} else if (helperSP.isGooglePlayServicesAvailable(this) == 0) {
			new AlertDialog.Builder(context)
					.setTitle(FixLabels.alertDefaultTitle)
					.setCancelable(false)
					.setMessage("Google play services is not available on this device,Application can not run without google play services.Exiting application!! ")
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// continue with delete
							finish();
						}
					})
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}
	}
	public void AskUserToTurnONGPS() {
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (statusOfGPS == false) {
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				new AlertDialog.Builder(this, R.style.AlertDialogCustom).
						setTitle("GPS Offline").

						setMessage("To track location we need gps enabled,Please turn on gps.").setCancelable(false).
						setPositiveButton("Settings",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface d, int id) {


										startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 105);
									}
								})
						.setNegativeButton("Exit",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface d, int id) {
										finish();
									}
								}).show();
//			}
		}
		else
		{
			continueExecution();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

			AskUserToTurnONGPS();

	}

	private void continueExecution() {

		int regid = 0;
		String profilePicture="";
		String userName="";
		sessionDCM = null;
//		if(sessionDCM.idUser>0) {
			try {
				sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
				sessionDCMs = new ArrayList<>();
				sessionDCMs = sessionDAO.getAll();

				if (sessionDCMs.size() > 0) {
					sessionDCM = sessionDCMs.get(0);
					sessionDAO.create(sessionDCMs);
				} else {

				}
				driverDAO = new DriverDAO(PayBitApp.getAppInstance().getDatabaseHelper());
				driverDAO.deleteAll();

				userDAO = new UserDAO(PayBitApp.getAppInstance().getDatabaseHelper());
				userDAO.deleteAll();
//				FixLabels.sessionDatabase.user = null;
				FixLabels.sessionDatabase.pickupPointId = 0;
				FixLabels.sessionDatabase.pickupAddress = "";
				FixLabels.sessionDatabase.pickupAddress = "";
				FixLabels.sessionDatabase.pickupLatitude = "";
				FixLabels.sessionDatabase.pickupLongitude = "";
				FixLabels.sessionDatabase.pickupTitle = "";
				orderDAO = new OrderDAO(PayBitApp.getAppInstance().getDatabaseHelper());
				orderDAO.deleteAll();



			} catch (SQLException e) {
				e.printStackTrace();
			}


//		}else {
//			Intent intent_home_screen = new Intent(SplashScreenActivity.this, activity_login.class);
//			startActivity(intent_home_screen);
//			finish();
//		}
		new Handler().postDelayed(new Runnable() {

/*
* Showing splash screen with a timer. This will be useful when you
* want to show case your app logo / company
*/

            @Override
            public void run() {
//				if(FixLabels.sessionDatabase.session!=null) {

//
				if(sessionDCM!=null){
					new GetUserDetails(SplashScreenActivity.this).execute();
				}else {
					Intent intent_home_screen = new Intent(SplashScreenActivity.this, activity_login.class);
					startActivity(intent_home_screen);
					finish();
				}
            }
        }, TIMEINMILLIS);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}

