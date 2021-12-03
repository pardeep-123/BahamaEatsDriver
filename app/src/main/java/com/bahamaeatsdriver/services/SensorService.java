package com.bahamaeatsdriver.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bahamaeats.constant.Constants;
import com.bahamaeatsdriver.R;
import com.bahamaeatsdriver.activity.Home_Page;
import com.bahamaeatsdriver.di.App;
import com.bahamaeatsdriver.receiver.LocationReceiver;
import com.bahamaeatsdriver.socket.SocketManager;
import com.bahamaeatsdriver.socket_response.SaveLocationResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;


public class SensorService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, SocketManager.Observer {
    public int counter = 0;
    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    Context context = this;
    //    public static final int LOCATION_INTERVAL = 10000;
//    public static final int FASTEST_LOCATION_INTERVAL = 5000;
    private static final long LOCATION_INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_LOCATION_INTERVAL = 1000 * 60 * 1; // 1 minute
    SocketManager socketManager;

    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private static final float SMALLEST_DISPLACEMENT = 0.15F; //quarter of a meter


    public SensorService(Context applicationContext) {
        super();
        Log.i("TAG", "here I am! in service");
    }

    public SensorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


        socketManager = App.Companion.getSocketManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            runAsForeground();
        } else {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
        socketManager.onRegister(this);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "ondestroy! service");
        Intent broadcastIntent = new Intent(this, LocationReceiver.class);
        broadcastIntent.setAction("restartservice");
        sendBroadcast(broadcastIntent);
        socketManager.unRegister(this);
        //stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        //initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        //timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.d("TAG", "in timer ++++  " + (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void runAsForeground() {
        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(getNotificationIcon()).setLargeIcon(icon1)
                .setContentTitle(getString(R.string.app_name)).setContentText("Fetching Location").
                        setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)).setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE).build();
        startForeground(101, notification);

    }

    @SuppressLint("ObsoleteSdkInt")
    private int getNotificationIcon() {

        int useWhiteIcon;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            useWhiteIcon = R.drawable.ic_noti_trans;
        } else {
            useWhiteIcon = R.mipmap.ic_launcher;
        }
        return useWhiteIcon;
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAG", "Location changed");


        if (location != null) {
            Log.d("TAG", "== location != null");

            //Send result to activities
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }

    }

    private void sendMessageToUI(String lat, String lng) {
        Log.d(TAG, "sendMessageToUI:" + "Sending info...");

        Log.d("TAG", "Sending info...");
        Intent intent = new Intent("restartservice");
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        /*{
          "receiverId": 7467---this will be it of other user to whom driver lat long will send,
          "receiverType": 1---- type 1 for user ,
          "latitude": 30.192414,
          "longitude": 76.122341
        }*/
        if (!Home_Page.Companion.getOtherUserId().isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("receiverId", Home_Page.Companion.getOtherUserId());
                jsonObject.put("receiverType", Constants.USER_TYPE);
                jsonObject.put("latitude", lat);
                jsonObject.put("longitude", lng);
                socketManager.saveVendorLocation(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d("TAG", "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("TAG", "Connection suspended");
    }


    //to get the location change


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("TAG", "Failed to connect to Google API");

    }

    @Override
    public void onError(String event, Object... args) {

    }

    @Override
    public void onResponse(String event, Object... args) {
        switch (event) {
            case SocketManager.UPDATE_LOCATION_LISTENER:
                try {
                    JSONObject data = (JSONObject) args[0];
                    SaveLocationResponse chatModel = new SaveLocationResponse();
                    chatModel.setLatitude(new JSONObject(data.getString("body")).getString("latitude"));
                    chatModel.setLongitude(new JSONObject(data.getString("body")).getString("longitude"));
                    Log.d(TAG, "onResponse: " + data.toString());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }
}