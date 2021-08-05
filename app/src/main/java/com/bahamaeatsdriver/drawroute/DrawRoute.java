package com.bahamaeatsdriver.drawroute;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DrawRoute extends AsyncTask<String, Void, String> {

    private GoogleMap mMap;
    LatLng origin;
    LatLng destination;
    public DrawRoute(LatLng origin, LatLng destination, GoogleMap mMap) {
        this.mMap = mMap;
         this.origin= origin;
          this.destination=destination;
    }

    @Override
    protected String doInBackground(String... url) {
        String data = "";
        try {
            data = getJsonRoutePoint(url[0]);
            Log.d("Background Task data", data);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        RouteDrawerTask routeDrawerTask = new RouteDrawerTask(origin,destination,mMap);
        routeDrawerTask.execute(result);
    }

    /**
     * A method to download json data from url
     */
    private String getJsonRoutePoint(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("getJsonRoutePoint", data);
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}