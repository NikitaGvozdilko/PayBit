package com.justimaginethat.gogodriver.activity_landing_pkg.user.CustomInfoWindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.justimaginethat.gogodriver.R;

/**
 * Created by Lion-1 on 3/6/2017.
 */

public class CustomInfoWindowCircle implements GoogleMap.InfoWindowAdapter {

    public View mWindow;
    public View mContents;
    public Marker marker_of_window;
    public String txtWindowRenderTitle="";

    public FrameLayout custom_info_windowMain;
    public FrameLayout custom_info_windowArrow;
    public TextView titleUi;
    public TextView timeDurationUi;
    public GoogleMap mGoogleMap;
    public Marker currentLocationMarker;
    public Context mContext;



    public CustomInfoWindowCircle(LayoutInflater viewInflater) {
        mWindow =viewInflater.inflate(R.layout.custom_info_window_circel,null);
        mContents = viewInflater.inflate(R.layout.custom_info_contents, null);
    }
    @Override
    public View getInfoWindow(Marker marker) {
//        marker_of_window = marker;
//        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                marker_of_window = marker;
//                String[] items={"Cancel","Continue"};
//                AlertDialog.Builder itemDilog = AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
//                itemDilog.setTitle("Process");
//                itemDilog.setCancelable(false);
//                itemDilog.setItems(items, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch(which){
//                            case 0:{
//                                CancelButton();
//                            }break;
//                            case 1:{
//                                ContinueButton();
//                            }break;
//                        }
//                    }
//                });
//                itemDilog.show();
//            }
//        });

        return mWindow;
    }
    public void  CancelButton(){
//        pickerLayoutFL.setVisibility(View.VISIBLE);
//        pickerLayoutConBL.setVisibility(View.GONE);
//        custom_info_windowMain.setVisibility(View.GONE);
//        custom_info_windowArrow.setVisibility(View.GONE);
        marker_of_window.hideInfoWindow();
        titleUi.setText("");
        timeDurationUi.setText("0Min");
        marker_of_window.showInfoWindow();
    }

    public void ContinueButton(){
//        pickerLayoutFL.setVisibility(View.GONE);
//        pickerLayoutConBL.setVisibility(View.VISIBLE);
        custom_info_windowMain.setVisibility(View.VISIBLE);
        custom_info_windowArrow.setVisibility(View.VISIBLE);
        timeDurationUi.setText(timeDurationUi.getText());
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mContents;
    }

    public void render() {
        custom_info_windowMain=(FrameLayout)mWindow.findViewById(R.id.custom_info_windowMain);
        custom_info_windowArrow=(FrameLayout)mWindow.findViewById(R.id.custom_info_windowArrow);
        titleUi = ((TextView) mWindow.findViewById(R.id.title));
        titleUi.setText(txtWindowRenderTitle);

        custom_info_windowMain.setVisibility(View.VISIBLE);
        custom_info_windowArrow.setVisibility(View.VISIBLE);

        timeDurationUi = ((TextView) mWindow.findViewById(R.id.tvDuration));
        timeDurationUi.setText(timeDurationUi.getText().toString());

    }
}
