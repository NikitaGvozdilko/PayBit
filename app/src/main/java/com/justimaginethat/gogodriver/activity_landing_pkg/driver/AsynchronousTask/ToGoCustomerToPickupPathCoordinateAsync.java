package com.justimaginethat.gogodriver.activity_landing_pkg.driver.AsynchronousTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_customer;
import com.justimaginethat.gogodriver.activity_landing_pkg.driver.fragments.fragment_goto_pickup;


public class ToGoCustomerToPickupPathCoordinateAsync extends AsyncTask<String, Void, String> {

    public fragment_goto_customer headingToPickup;
    public ProgressDialog progressDialog;
    public String urlLink;


    public ToGoCustomerToPickupPathCoordinateAsync(fragment_goto_customer headingToPickup, String urlLink) {
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
            ToCustomer_LineDrawOnMapDAsync parserTask = new ToCustomer_LineDrawOnMapDAsync(headingToPickup);
                  parserTask.execute(result);

        }
    }