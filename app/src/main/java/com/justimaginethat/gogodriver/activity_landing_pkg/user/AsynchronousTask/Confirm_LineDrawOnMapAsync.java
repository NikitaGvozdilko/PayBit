package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.Utility.MapPathAnimator;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;
import com.justimaginethat.gogodriver.activity_map_marker.JsonParser.DirectionsJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Confirm_LineDrawOnMapAsync extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    ProgressDialog progressDialog;


    public fragment_layout_confirm fragmentLayoutConfirm;

    public Confirm_LineDrawOnMapAsync(fragment_layout_confirm fragmentLayoutConfirm) {
        this.fragmentLayoutConfirm = fragmentLayoutConfirm;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;
        try {

            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();
            // Starts parsing data
            routes = parser.parse(jObject);
        } catch (Exception e) {
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
                TextView tvDuration = (TextView) fragmentLayoutConfirm.toMarkerView.findViewById(R.id.tvDuration);
                // Traversing through all the routes
                fragmentLayoutConfirm.lineOptions = null;
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    fragmentLayoutConfirm.lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) {    // Get distance from the list
                            fragmentLayoutConfirm.distance = (String) point.get("distance");
                            continue;
                        } else if (j == 1) { // Get duration from the list
                            fragmentLayoutConfirm.distance = (String) point.get("duration");
                            continue;
                        }

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        fragmentLayoutConfirm.position = new LatLng(lat, lng);
                        points.add(fragmentLayoutConfirm.position);
                    }
                    // Adding all the points in the route to LineOptions
                    fragmentLayoutConfirm.lineOptions.addAll(points);
                    fragmentLayoutConfirm.mGoogleMap.clear();
                    MapPathAnimator ani = new MapPathAnimator();
                    ani.animateRoute(fragmentLayoutConfirm.mGoogleMap,points);
                    tvDuration.setText(fragmentLayoutConfirm.distance);
                }

            } else {
                fragmentLayoutConfirm.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(fragmentLayoutConfirm.getContext(), "No Path Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            fragmentLayoutConfirm.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(fragmentLayoutConfirm.getContext(), "Error on path request", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}