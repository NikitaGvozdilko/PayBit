package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_pickup;


public class ToPickup_CustomerHeadingToPickupPathCoordinateAsync extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread

    public fragment_goto_pickup headingToPickup;
    public ProgressDialog progressDialog;
    public String urlLink;


    public ToPickup_CustomerHeadingToPickupPathCoordinateAsync(fragment_goto_pickup headingToPickup, String urlLink) {
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
            ToPickup_LineDrawOnMapDAsync parserTask = new ToPickup_LineDrawOnMapDAsync(headingToPickup);
                  parserTask.execute(result);

        }
    }