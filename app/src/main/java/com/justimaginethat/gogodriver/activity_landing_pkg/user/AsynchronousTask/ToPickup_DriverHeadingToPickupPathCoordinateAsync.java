package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_heading_to_pickup;

public class ToPickup_DriverHeadingToPickupPathCoordinateAsync extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread

    public fragment_driver_heading_to_pickup headingToPickup;
    public ProgressDialog progressDialog;
    public String urlLink;


    public ToPickup_DriverHeadingToPickupPathCoordinateAsync(fragment_driver_heading_to_pickup headingToPickup, String urlLink) {
        this.headingToPickup = headingToPickup;
        this.urlLink=urlLink;

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
        protected String doInBackground(String... urlLink) {
            String data = "";
            try{

                data = headingToPickup.downloadUrl(urlLink[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ToPickup_LineDrawOnMapAsync parserTask = new ToPickup_LineDrawOnMapAsync(headingToPickup);
                  parserTask.execute(result);

        }
    }