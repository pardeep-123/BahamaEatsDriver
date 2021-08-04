package com.bahamaeatsdriver.drawroute;

import com.bahamaeatsdriver.R;
import com.bahamaeatsdriver.di.App;
import com.google.android.gms.maps.model.LatLng;

public class FetchUrl {
    public static String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String api = "key="+ App.application.getString(R.string.google_maps_key);
        String parameters = str_origin + "&" + str_dest + "&" + sensor+"&"+mode+"&"+api;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
}
