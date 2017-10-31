package com.justimaginethat.gogodriver.activity_map_marker.Modules;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public LatLng startLocation;
    public LatLng endLocation;
    public String startAddress;
    public String endAddress;

    public List<LatLng> points;
}
