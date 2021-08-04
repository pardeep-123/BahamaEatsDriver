package com.bahamaeatsdriver.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomToast extends AppCompatActivity {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static void makeText(Context mContext, String message, int time) {
        if (isNotificationEnabled(mContext)) {
            //Show Toast message
            Toast.makeText(mContext, message, time).show();
        } else {
            // Or show own custom alert dialog
            showCustomAlertDialog(mContext, message);
        }
    }

    private static boolean isNotificationEnabled(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager mAppOps = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = mContext.getApplicationInfo();
            String pkg = mContext.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass;
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod =
                        appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid,
                        pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                    | InvocationTargetException | IllegalAccessException ex) {
//                Utils.logExceptionCrashLytics(ex);
            }
            return false;
        } else {
            return false;
        }
    }

    private static void showCustomAlertDialog(Context mContext, String message) {
        if (!(mContext instanceof Activity && ((Activity)mContext).isFinishing())) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
            mBuilder.setMessage(message);
            mBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            mBuilder.setCancelable(true);
            AlertDialog alertDialog = mBuilder.create();
            alertDialog.show();
        }
    }
}
