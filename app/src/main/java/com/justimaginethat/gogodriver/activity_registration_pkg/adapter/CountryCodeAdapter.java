package com.justimaginethat.gogodriver.activity_registration_pkg.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.activity_registration_pkg.models.Countries;


public class CountryCodeAdapter extends BaseAdapter {

    private final Context mContext;
    private final Countries malCountries;

    public CountryCodeAdapter(Context aContext, Countries aCountries) {
        mContext = aContext;
        malCountries = aCountries;
    }

    @Override
    public int getCount() {
        if(malCountries != null && malCountries.getCountries() != null) {
            return malCountries.getCountries().size();
        }
        return  0;
    }

    @Override
    public String getItem(int position) {
        return malCountries.getCountries().get(position).name + " (" +malCountries.getCountries().get(position).code+")";
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_generic_spinner_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtCode = (TextView) convertView.findViewById(R.id.txtTitle);
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/avenir_light.otf");
            viewHolder.txtCode.setTypeface(tf, 1);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtCode.setText(getItem(position).trim().replace(" ",""));

        return convertView;
    }

    class ViewHolder{
        TextView txtCode;
    }



}
