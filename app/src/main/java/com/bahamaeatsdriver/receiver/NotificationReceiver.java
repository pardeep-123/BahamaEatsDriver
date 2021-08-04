package com.bahamaeatsdriver.receiver;

import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class NotificationReceiver extends WakefulBroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            playNotificationSound(context);
        }
        public void playNotificationSound(Context context) {
        }
}
