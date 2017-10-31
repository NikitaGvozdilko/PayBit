package com.justimaginethat.gogodriver.activity_landing_pkg.user.AsynchronousTask;

import android.os.AsyncTask;
import android.util.Log;

import com.justimaginethat.gogodriver.activity_landing_pkg.user.fragments.fragment_layout_confirm;

public class Confirm_PickupToCustomerLocationPathCoordinatesAsync extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread

    public fragment_layout_confirm fragmentLayoutConform;
    public String urlLink;

    public Confirm_PickupToCustomerLocationPathCoordinatesAsync(fragment_layout_confirm fragmentLayoutConform, String urlLink) {
        this.fragmentLayoutConform = fragmentLayoutConform;
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

                data = fragmentLayoutConform.downloadUrl(urlLink[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
                  Confirm_LineDrawOnMapAsync parserTask = new Confirm_LineDrawOnMapAsync(fragmentLayoutConform);
                  parserTask.execute(result);

        }
    }