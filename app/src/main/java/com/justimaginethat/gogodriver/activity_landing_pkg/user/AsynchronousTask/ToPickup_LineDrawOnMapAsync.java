package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.Utility.MapPathAnimator;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_heading_to_pickup;
import com.justimaginethat.gogodriver.activity_map_marker.JsonParser.DirectionsJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToPickup_LineDrawOnMapAsync extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {


    public fragment_driver_heading_to_pickup fragmentPickupHandel;

    public ToPickup_LineDrawOnMapAsync(fragment_driver_heading_to_pickup fragmentPickupHandel) {
        this.fragmentPickupHandel = fragmentPickupHandel;
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
                    TextView tvDuration = (TextView) fragmentPickupHandel.toMarkerView.findViewById(R.id.tvDuration);
                    // Traversing through all the routes
                    fragmentPickupHandel.lineOptions = null;
                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<LatLng>();
                        fragmentPickupHandel.lineOptions = new PolylineOptions();
                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            if (j == 0) {    // Get distance from the list
                                fragmentPickupHandel.distance = (String) point.get("distance");
                                continue;
                            } else if (j == 1) { // Get duration from the list
                                fragmentPickupHandel.distance = (String) point.get("duration");
                                continue;
                            }

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            fragmentPickupHandel.position = new LatLng(lat, lng);
                            points.add(fragmentPickupHandel.position);
                        }
                        // Adding all the points in the route to LineOptions
                        fragmentPickupHandel.lineOptions.addAll(points);
                        fragmentPickupHandel.lineOptions.width(5);
                        fragmentPickupHandel.lineOptions.color(Color.BLACK);

                        fragmentPickupHandel.mGoogleMap.addPolyline(fragmentPickupHandel.lineOptions);
                        int padding=200;
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(fragmentPickupHandel.fromLatLng);
                        builder.include(fragmentPickupHandel.toLatLng);
                        LatLngBounds bounds = builder.build();
                        fragmentPickupHandel.mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,padding));
                        tvDuration.setText(fragmentPickupHandel.distance);
                    }



                } else {
                    fragmentPickupHandel.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(fragmentPickupHandel.getContext(), "No Path Found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                fragmentPickupHandel.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(fragmentPickupHandel.getContext(), "Error on path request", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
}