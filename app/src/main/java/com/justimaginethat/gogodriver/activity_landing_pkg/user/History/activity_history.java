package com.justimaginethat.gogodriver.activity_landing_pkg.user.History;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.MasterModel;
import com.justimaginethat.gogodriver.DomainModels.SessionDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragment_activity_landing_driver;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.History.AsyncTask.GetUserOrderHistoryAsync;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.History.adapter.OrderListAdapter;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_settings.activity_update_setting;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lion-1 on 4/22/2017.
 */

public class activity_history extends AppCompatActivity {
    public TextView txtDate;
    public LinearLayout btnLeft;
    public LinearLayout btnRight;
    public Button btnAdd;

    public activity_history activityHistory;
    public List<MasterModel> orderDCMList=new ArrayList<>();

    public View mCustomView;
    public ImageView imgBack;




    public Calendar cal = Calendar.getInstance();

    public int dayofyear = cal.get(Calendar.DAY_OF_YEAR);
    public int year = cal.get(Calendar.YEAR);
    public int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
    public int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
    public int dayMonth=cal.get(Calendar.MONTH);


    public RecyclerView recyclerView;
    public OrderListAdapter orderListAdapter;
    public String datePass;
    public String date;
    public SimpleDateFormat df;
    public Date current_date;


    public SessionDAO sessionDAO;
    public SessionDCM sessionDCM = new SessionDCM();

    public AlertDialog.Builder builder;
    public Context context;
    public DatePicker dpStartDate;
    public TextView empty_view;

//    public List<MasterModel> orderDCMLists =new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_history);
        mCustomView = getSupportActionBar().getCustomView();
        context=this;
        activityHistory=this;


        builder = new AlertDialog.Builder(context);

        df= new SimpleDateFormat("MMMM/dd/yyyy");





        current_date= Calendar.getInstance().getTime();
        recyclerView = (RecyclerView)findViewById(R.id.recList);
        orderListAdapter = new OrderListAdapter(context,activityHistory);

        txtDate=(TextView)findViewById(R.id.txtDate);
        btnLeft=(LinearLayout)findViewById(R.id.btnLeft);
        btnRight=(LinearLayout)findViewById(R.id.btnRight);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        empty_view=(TextView)findViewById(R.id.empty_view);
        sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
        try {
            sessionDCM = sessionDAO.getAll().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(sessionDCM.isDriver==true){
            btnAdd.setVisibility(View.GONE);
        }


        imgBack = (ImageView) mCustomView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(sessionDCM.isCustomer==true){

                    Intent intentBack = new Intent(activity_history.this, fragment_activity_landing_user.class);
                    startActivity(intentBack);
                    finish();
                }else {

                    Intent intentBack = new Intent(activity_history.this, fragment_activity_landing_driver.class);
                    startActivity(intentBack);
                    finish();

                }


            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionDCM.isCustomer==true){

                    Intent intentBack = new Intent(activity_history.this, fragment_activity_landing_user.class);
                    startActivity(intentBack);
                    finish();
                }else {

                    Intent intentBack = new Intent(activity_history.this, fragment_activity_landing_driver.class);
                    startActivity(intentBack);
                    finish();

                }
            }
        });

//        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
//        String formattedDate =

        date=df.format(new Date().getTime());
        txtDate.setText(date);
//        getlongtoago(Long.parseLong(date, 30));
        datePass = date;

        new GetUserOrderHistoryAsync(activityHistory,datePass).execute();
        orderListAdapter.notifyDataSetChanged();

            txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                View view = inflater.inflate(R.layout.inflate_datepicker, null);
                dpStartDate = (DatePicker) view.findViewById(R.id.datePicker);
                dpStartDate.setMaxDate(System.currentTimeMillis());//sp 9-5-2017
                // item.addView(view);

                builder.setView(view); // Set the view of the dialog to your custom layout
                builder.setTitle("Select Date");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar newDate = Calendar.getInstance();
//                        dpStartDate.setMaxDate(System.currentTimeMillis());//sp 9-5-2017
                        newDate.set(dpStartDate.getYear(), dpStartDate.getMonth(), dpStartDate.getDayOfMonth());

                        try {
                            txtDate.setText(LionUtilities.getStringFromDate(newDate.getTime(), FixLabels.dateFormat));
                            datePass=LionUtilities.getStringFromDate(newDate.getTime(), FixLabels.dateFormat);
                            new GetUserOrderHistoryAsync(activityHistory,datePass).execute();
                            orderListAdapter.notifyDataSetChanged();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                date = ""+cal.get(Calendar.DATE)+"/"+(cal.get(Calendar.MONTH)-1)+"/"+cal.get(Calendar.YEAR);
//                txtDate.setText(date);
                cal.add(Calendar.DATE, -1);
                date = df.format(cal.getTime());
                txtDate.setText(date);
                datePass=date;
                new GetUserOrderHistoryAsync(activityHistory,datePass).execute();
                orderListAdapter.notifyDataSetChanged();
                btnRight.setVisibility(View.VISIBLE);
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long d = new Date().getTime();
                Calendar c = (Calendar) cal.clone();
                c.add(Calendar.DATE, 1);

                long next = c.getTimeInMillis();

                long diff = d - next;
                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diff);
                if(diffInSec > 0) {
                    btnRight.setVisibility(View.VISIBLE);
                    cal.add(Calendar.DATE, 1);
                    date = df.format(cal.getTime());
                    txtDate.setText(date);
                    datePass = date;
                    new GetUserOrderHistoryAsync(activityHistory,datePass).execute();
                    orderListAdapter.notifyDataSetChanged();
                }
                else
                {
                    btnRight.setVisibility(View.INVISIBLE);
//                    c.add(Calendar.DATE, -1);
                }




            }
        });
    }



    public static String getlongtoago(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
        Date date = null;
        date = new Date(createdAt);
        String crdate1 = dateFormatNeeded.format(date);

        // Date Calculation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        crdate1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + "day ago ";
            } else {
                time = diffDays + "days ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + "hr ago";
                } else {
                    time = diffHours + "hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + "min ago";
                    } else {
                        time = diffMinutes + "mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + "secs ago";
                    }
                }

            }

        }
        return time;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
        if(sessionDCM.isDriver == true)
        {
            Intent i = new Intent(activity_history.this, fragment_activity_landing_driver.class);
            startActivity(i);
            finish();
        }
        else {

            Intent i = new Intent(activity_history.this, fragment_activity_landing_user.class);
            startActivity(i);
            finish();
        }

    }
}
