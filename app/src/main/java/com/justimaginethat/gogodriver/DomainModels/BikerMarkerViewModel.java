package com.justimaginethat.gogodriver.DomainModels;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;

import java.io.Serializable;

/**
 * Created by Lion-1 on 4/1/2017.
 */

public class BikerMarkerViewModel extends BaseAPIErrorModel implements Serializable {

   public DriverDCM user;
    public ImageView marker;
    public boolean updated = false;
    public LatLng position;
//    public ImageView markerView;

}
