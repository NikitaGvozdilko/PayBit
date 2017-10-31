package com.justimaginethat.gogodriver.activity_landing_pkg.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.PickupPointDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.PickupPointsDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.Utility.LionUtilities;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragment_activity_landing_user;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_pickup_selection_list;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PickupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public Context context;
    private LayoutInflater inflater;
    List<PickupPointDCM> data= Collections.emptyList();
    public fragment_activity_landing_user fActivity;
    PickupPointDCM current;
    int currentPos=0;


    public fragment_layout_confirm fragActCon;



    fragment_layout_pickup_selection_list fragAct;
    fragment_activity_landing_user fragment_activity_landing_user;
    LionUtilities jb = new LionUtilities();

    public PickupListAdapter(Context  context,fragment_layout_pickup_selection_list fragAct) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.fragAct=fragAct;
    }

    public PickupListAdapter(Context context,fragment_layout_confirm fragment_layout_confirm) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.fragActCon= fragment_layout_confirm;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.pickup_data_list_adapter, parent,false);
        PickUPHolder holder=new PickUPHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PickUPHolder pickUPHolder = (PickUPHolder) holder;



        final PickupPointDCM currentData =fragAct.dataSearch.get(position);
        pickUPHolder.title.setText(currentData.title);
        pickUPHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragAct.checkNearbyDriverFlag = false;
                fragAct.timer.removeCallbacksAndMessages(null);

                PickupPointDCM pointDCM=new PickupPointDCM();
                PickupPointsDAO pointDAO = new PickupPointsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
                pointDAO.deleteAll();
                pointDCM.idDeliveryPickupPoints=currentData.idDeliveryPickupPoints;
                pointDCM.entryByUserName=currentData.entryByUserName;
                pointDCM.isActive=currentData.isActive;
                pointDCM.title=currentData.title;
                pointDCM.pickupAddress=currentData.pickupAddress;
                pointDCM.pickuplatitude=currentData.pickuplatitude;
                pointDCM.pickupLongitude=currentData.pickupLongitude;
                pointDCM.entryDate=currentData.entryDate;
                pointDCM.lastChangeDate=currentData.lastChangeDate;
                pointDCM.changeByUserName=currentData.changeByUserName;
                pointDCM.updateRoutePoint=currentData.updateRoutePoint;
                pointDCM.syncGUID=currentData.syncGUID;
                pointDCM.insertRoutePoint=currentData.insertRoutePoint;
                pointDAO.create(pointDCM);

                fragAct.pickerLayoutFL.setVisibility(View.GONE);

                fragment_activity_landing_user=(fragment_activity_landing_user)context;
                try {
                    fragment_activity_landing_user.showLayout(FixLabels.OrderStatus.PrepareNewOrder);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return fragAct.dataSearch.size();
    }

    public static class PickUPHolder extends RecyclerView.ViewHolder {
       public TextView title;
        public PickUPHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
        }
    }
}



