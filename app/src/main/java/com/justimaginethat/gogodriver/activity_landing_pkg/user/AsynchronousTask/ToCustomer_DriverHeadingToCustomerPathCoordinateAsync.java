package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_heading_to_pickup;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_driver_on_the_way;

public class ToCustomer_DriverHeadingToCustomerPathCoordinateAsync extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread

    public fragment_driver_on_the_way fragmentDriverOnTheWay;
    public ProgressDialog progressDialog;
    public String urlLink;


    public ToCustomer_DriverHeadingToCustomerPathCoordinateAsync(fragment_driver_on_the_way fragmentDriverOnTheWay, String urlLink) {
        this.fragmentDriverOnTheWay = fragmentDriverOnTheWay;
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

                data = fragmentDriverOnTheWay.downloadUrl(urlLink[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ToCustomer_LineDrawOnMapAsync parserTask = new ToCustomer_LineDrawOnMapAsync(fragmentDriverOnTheWay);
                  parserTask.execute(result);

        }
    }