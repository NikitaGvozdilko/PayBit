package com.justimaginethat.gogodriver.activity_landing_pkg.user.History.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.MasterModel;

import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.SessionDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.History.activity_history;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public Context context;
    private LayoutInflater inflater;
    public List<MasterModel> data = Collections.emptyList();
    public String date;
    public activity_history fActivity;
    public MasterModel current;
    public MasterModel orderDCM;
    int currentPos = 0;
    public String datePass;

    public LionUtilities lionUtilities = new LionUtilities();

    public long diff;

    public long diffDays = diff / (24 * 60 * 60 * 1000);
    private String time;


    public OrderListAdapter(Context context, activity_history fActivity) {
        this.context = context;
        this.fActivity = fActivity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_history_list_adapter, parent, false);
        OrderHistoryHolder holder = new OrderHistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderHistoryHolder orderHistoryHolder = (OrderHistoryHolder) holder;
        final MasterModel currentData = fActivity.orderDCMList.get(position);


        try {
            Date old = lionUtilities.getDateFromString(currentData.order.orderDate, FixLabels.serverDateFormat);

            diff = lionUtilities.getDateDiffInDays(new Date(), old);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (currentData.order.orderStatus.equalsIgnoreCase(FixLabels.OrderStatus.OrderCanceled)) {
            orderHistoryHolder.title.setText(currentData.pickup.title + " " + "(Cancelled)");

            try {
                datePass = lionUtilities.getStringFromDate(lionUtilities.getDateFromString(fActivity.date, FixLabels.dateFormat), FixLabels.serverDateFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }


                 time = "";
                if (diff < 1) {
                    SessionDAO sessionDAO = new SessionDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                    try {
                        boolean isDriver =  sessionDAO.getAll().get(0).isDriver;

                        if(isDriver)
                        {
                            time=currentData.order.orderTime;


                        }
                        else
                        {
                            time = "Today";
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        time = "Err";
                    }


                } else {
                    time=currentData.order.orderTime;

//                    time = diff + "days ago ";
                }



            orderHistoryHolder.txtTime.setText(time);

            orderHistoryHolder.llPay.setVisibility(View.INVISIBLE);
            orderHistoryHolder.llDriverName.setVisibility(View.INVISIBLE);

            orderHistoryHolder.txtPay.setVisibility(View.INVISIBLE);
            orderHistoryHolder.txtDriverName.setVisibility(View.INVISIBLE);


        } else

        {

            orderHistoryHolder.txtTime.setText(time);
            orderHistoryHolder.title.setText(currentData.pickup.title);

            orderHistoryHolder.txtPay.setText(String.valueOf(currentData.order.orderAmount));
            orderHistoryHolder.txtDriverName.setText(currentData.user.firstName);
        }
    }

    @Override
    public int getItemCount() {
        return fActivity.orderDCMList.size();
    }

    public static class OrderHistoryHolder extends RecyclerView.ViewHolder {

        public LinearLayout llMain;
        public LinearLayout llChild1;
        public LinearLayout llChild2;

        public LinearLayout llTime;
        public LinearLayout llPay;
        public LinearLayout llDriverName;

        public ImageView imgTime;
        public ImageView imgPay;
        public ImageView imgDriverName;

        public TextView title;
        public TextView txtTime;
        public TextView txtPay;
        public TextView txtDriverName;

        public OrderHistoryHolder(View v) {
            super(v);


            llMain = (LinearLayout) v.findViewById(R.id.llMain);
            llChild1 = (LinearLayout) v.findViewById(R.id.llChild1);
            llChild2 = (LinearLayout) v.findViewById(R.id.llChild2);


            llTime = (LinearLayout) v.findViewById(R.id.llTime);
            llPay = (LinearLayout) v.findViewById(R.id.llPay);
            llDriverName = (LinearLayout) v.findViewById(R.id.llDriverName);


            imgTime = (ImageView) v.findViewById(R.id.imgTime);
            imgPay = (ImageView) v.findViewById(R.id.imgPay);
            imgDriverName = (ImageView) v.findViewById(R.id.imgDriverName);

            title = (TextView) v.findViewById(R.id.title);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            txtPay = (TextView) v.findViewById(R.id.txtPay);
            txtDriverName = (TextView) v.findViewById(R.id.txtDriverName);
        }
    }
}



