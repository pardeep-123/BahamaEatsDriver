package com.bahamaeatsdriver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.bahamaeatsdriver.services.LocationMonitoringService;


public class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Broadcast Listened", "Service tried to stop");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, LocationMonitoringService.class));
            } else {
                context.startService(new Intent(context, LocationMonitoringService.class));
            }
        }

}
