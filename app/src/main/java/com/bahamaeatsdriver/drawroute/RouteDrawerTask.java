package com.bahamaeatsdriver.drawroute;
import android.graphics.Color;
import android.os.AsyncTask;
 import android.util.Log;

import androidx.core.content.ContextCompat;

import com.bahamaeatsdriver.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private PolylineOptions lineOptions;
   // private PolylineOptions lineOptionsNoRoute;
    private GoogleMap mMap;
    private int routeColor;
   // private int routeColorNoRoute;
   LatLng origin;
    LatLng destination;
    public RouteDrawerTask(LatLng origin, LatLng destination, GoogleMap mMap) {
        this.mMap = mMap;
        this.origin= origin;
        this.destination=destination;
    }

    LatLng startRoute;
    LatLng endRoute;

    String  first="";
    String  last="";
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("RouteDrawerTask", jsonData[0]);
            DataRouteParser parser = new DataRouteParser();
            Log.d("RouteDrawerTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("RouteDrawerTask", "Executing routes");
            Log.d("RouteDrawerTask", routes.toString());

        } catch (Exception e) {
            Log.d("RouteDrawerTask", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        if (result != null)
            drawPolyLine(result);
    }

    private void drawPolyLine(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
      //  ArrayList<LatLng> pointsNoRoute;
        lineOptions = null;
        //lineOptionsNoRoute = null;

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
          //  pointsNoRoute = new ArrayList<>();
            lineOptions = new PolylineOptions();
           // lineOptionsNoRoute = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
                if (first.isEmpty()){
                    first="1";
                    startRoute=new LatLng(lat,lng);
                //    pointsNoRoute.add(orignFromWalkingNoRoute);

                }
                endRoute=new LatLng(lat,lng);

            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(6);
            routeColor = ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.colorPrimaryDark);
            if (routeColor == 0)
            {
                lineOptions.color(0xFF0A8F08);

            }
            else{
                lineOptions.color(routeColor);

            }

          /*  // no route
            lineOptionsNoRoute.addAll(pointsNoRoute);
            lineOptionsNoRoute.width(15);
            routeColorNoRoute = ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.colorBlack);
            if (routeColorNoRoute == 0)
            {
                lineOptionsNoRoute.color(0xFF0A8F08);

            }
            else{
                lineOptionsNoRoute.color(routeColorNoRoute);

            }*/


        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null && mMap != null) {
            mMap.addPolyline(lineOptions);

           // LatLng sydney1 = new LatLng(30.382699,130.382699);

            showCurvedPolyline(startRoute,origin, 0.5);
            showCurvedPolyline(endRoute,destination, 0.5);
          //  mMap.addPolyline(lineOptionsNoRoute);
        } else {
            Log.d("onPostExecute", "without Polylines draw");
        }
    }

    private void showCurvedPolyline (LatLng p1, LatLng p2, double k) {
        //Calculate distance and heading between two points
        double d = SphericalUtil.computeDistanceBetween(p1,p2);
        double h = SphericalUtil.computeHeading(p1, p2);

        //Midpoint position
        LatLng p = SphericalUtil.computeOffset(p1, d*0.5, h);

        //Apply some mathematics to calculate position of the circle center
        double x = (1-k*k)*d*0.5/(2*k);
        double r = (1+k*k)*d*0.5/(2*k);

        LatLng c = SphericalUtil.computeOffset(p, x, h + 90.0);

        //Polyline options
        PolylineOptions options = new PolylineOptions();
        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));

        //Calculate heading between circle center and two points
        double h1 = SphericalUtil.computeHeading(c, p1);
        double h2 = SphericalUtil.computeHeading(c, p2);

        //Calculate positions of points on circle border and add them to polyline options
        int numpoints = 100;
        double step = (h2 -h1) / numpoints;

        for (int i=0; i < numpoints; i++) {
            LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
            options.add(pi);
        }

        //Draw polyline
        mMap.addPolyline(options.width(10).color(Color.MAGENTA).geodesic(false).pattern(pattern));
    }

}
