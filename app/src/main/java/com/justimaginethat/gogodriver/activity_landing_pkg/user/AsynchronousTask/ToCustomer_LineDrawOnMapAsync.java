package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_heading_to_pickup;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_on_the_way;
import com.justimaginethat.gogodriver.activity_map_marker.JsonParser.DirectionsJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToCustomer_LineDrawOnMapAsync extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {


    public fragment_driver_on_the_way fragmentDriverOnTheWay;

    public ToCustomer_LineDrawOnMapAsync(fragment_driver_on_the_way fragmentDriverOnTheWay) {
        this.fragmentDriverOnTheWay = fragmentDriverOnTheWay;
    }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {




            if (result != null) {
                if (result.size() > 0) {
                    ArrayList<LatLng> points = null;
                    TextView tvDuration = (TextView) fragmentDriverOnTheWay.toMarkerView.findViewById(R.id.tvDuration);
                    // Traversing through all the routes
                    fragmentDriverOnTheWay.lineOptions = null;
                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<LatLng>();
                        fragmentDriverOnTheWay.lineOptions = new PolylineOptions();
                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            if (j == 0) {    // Get distance from the list
                                fragmentDriverOnTheWay.distance = (String) point.get("distance");
                                continue;
                            } else if (j == 1) { // Get duration from the list
                                fragmentDriverOnTheWay.distance = (String) point.get("duration");
                                continue;
                            }

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            fragmentDriverOnTheWay.position = new LatLng(lat, lng);
                            points.add(fragmentDriverOnTheWay.position);
                        }
                        // Adding all the points in the route to LineOptions
                        fragmentDriverOnTheWay.lineOptions.addAll(points);
                        fragmentDriverOnTheWay.lineOptions.width(5);
                        fragmentDriverOnTheWay.lineOptions.color(Color.BLACK);
                        fragmentDriverOnTheWay.mGoogleMap.clear();
                        fragmentDriverOnTheWay.mGoogleMap.addPolyline(fragmentDriverOnTheWay.lineOptions);
                        int padding=200;
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(fragmentDriverOnTheWay.fromLatLng);
                        builder.include(fragmentDriverOnTheWay.toLatLng);
                        LatLngBounds bounds = builder.build();
                        fragmentDriverOnTheWay.mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,padding));
                        tvDuration.setText(fragmentDriverOnTheWay.distance);
                    }



                } else {
                    fragmentDriverOnTheWay.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(fragmentDriverOnTheWay.getContext(), "No Path Found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                fragmentDriverOnTheWay.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(fragmentDriverOnTheWay.getContext(), "Error on path request", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
}