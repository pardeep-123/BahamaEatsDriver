package com.bahamaeatsdriver.helper.others;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonMethods {
    public static File myDir_images_sent = new File(Environment.getExternalStoragePublicDirectory("IDeliverIt"), "Images/Sent");
    public static File myDir_omorni = new File(Environment.getExternalStoragePublicDirectory("HairApp"), "Data");


    private static ProgressDialog mProgress;

    public static  double convertKmToMiles(double km){

        double milesOrinal=0.621371;

        /*1 km = 0.621371 miles*/
        double miles=0;

        miles=km*milesOrinal;

        return miles;
    }

    // check is the given email is in valid format.
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // check is the given phone is in valid format.
    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    // show the credit card type
   /* public static Bitmap returnCardType(String card_number, Context context) {
        CreditCard card = new CreditCard(card_number, 0, 0, null, null, null);
        return card.getCardType().imageBitmap(context);
    }*/


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static boolean checkStringNull(String string) {
        return string == null || string.equals("null") || string.isEmpty();
    }


//    public static void showSnackBar(View view, String message) {
//        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//        snackbar.show();
//    }


    public static String utcLongToNormal(long date, String format) {

        String newdate = "";

        if (String.valueOf(date).length() == 10) {
            date = date * 1000;
            //   newdate= String.valueOf(date);
        } else {

        }

        Calendar cal = Calendar.getInstance();
        // cal.timeInMillis = date;
        cal.setTimeInMillis(date);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat converted = new SimpleDateFormat(format, Locale.ENGLISH);
        converted.setTimeZone(TimeZone.getDefault());
        return converted.format(cal.getTime());
    }

    public static long time_to_timestamp(String str_date, String pattren) {
        long time_stamp = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattren);
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = (Date) formatter.parse(str_date);
            time_stamp = date.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        time_stamp = time_stamp / 1000;
        return time_stamp;
    }

    public static String convertTimeStampToDa(long timestamp) {
        Calendar calendar = Calendar.getInstance();

        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a | dd-MMM");
        /* SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");*/
        sdf.setTimeZone(tz);
        Date currenTimeZone = new Date(timestamp * 1000);
        return sdf.format(currenTimeZone);
    }
    public static String convertToNewFormat2(String dateStr) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("hh:mm a | dd-MMM | ");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = sourceFormat.parse(dateStr);
        return destFormat.format(convertedDate);
    }
    public static String convertToNewFormat5(String dateStr) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = sourceFormat.parse(dateStr);
        return destFormat.format(convertedDate);
    }
    public static String parseDateToddMMyyyy(String dateAndTime, String dateFormate) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(dateFormate);
        //ne line
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateAndTime);
            str = outputFormat.format(date);

            //new line
            outputFormat.setTimeZone(TimeZone.getDefault());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertTimeStampToDateTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        sdf.setTimeZone(tz);
        Date currenTimeZone = new Date(timestamp * 1000);
        return sdf.format(currenTimeZone);
    }

    public static String getNotificationTime(long time_stamp) {
        Date date = null;
        try {
            date = new Date(time_stamp * 1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("dateeee" + date.toString());

        String string_date = "";
        Date current = Calendar.getInstance().getTime();
        long diffInSeconds = (current.getTime() - date.getTime()) / 1000;
        long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30 : diffInSeconds;
        long weeks = days / 7;
        long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12 : diffInSeconds;
        long years = (diffInSeconds = (diffInSeconds / 12));

        if (years > 0) {
            if (years == 1) {
                string_date = "1 year";
            } else {
                string_date = years + " years";
            }
        } else if (months > 0) {
            if (months == 1) {
                //sb.append("a month");
                string_date = "1 month";
            } else {
                //sb.append(months + " months");
                string_date = months + " months";
            }
        } else if (weeks > 0) {
            if (weeks == 1) {
                //sb.append("a month");
                string_date = "1 week";
            } else {
                //sb.append(months + " months");
                string_date = weeks + " Weeks";
            }
        } else if (days > 0) {
            if (days == 1) {
                //sb.append("a day");
                string_date = "1 day";
            } else {
                // sb.append(days + " days");
                string_date = days + " days";

            }
        } else if (hrs > 0) {
            if (hrs == 1) {
                //sb.append("an hour");
                string_date = "1 hour";
            } else {
                //sb.append(hrs + " hours");
                string_date = hrs + " hours";
            }
        } else if (min > 0) {
            if (min == 1) {
                //sb.append("a minute");
                string_date = "1 minute";
            } else {
                //sb.append(min + " minutes");
                string_date = min + " minutes";
            }
        }

        string_date = string_date + " ago";
        return string_date;
    }

    public static void AlertErrorMessage(Activity activity, String message) {
        Alerter.create(activity)
                .setText(message)
                .setBackgroundColorInt(Color.RED) // or setBackgroundColorInt(Color.CYAN)
                .setDuration(2000)
                .hideIcon() // Optional - Removes white tint
                .setContentGravity(Gravity.CENTER)
                .show();
    }

    public static void AlertSuccessMessage(Activity activity, String message) {
        Alerter.create(activity)
                .setText(message)
                .setBackgroundColorInt(Color.GREEN) // or setBackgroundColorInt(Color.CYAN)
                .setDuration(2000)
                .hideIcon() // Optional - Removes white tint
                .setContentGravity(Gravity.CENTER)
                .show();
    }

    public static File writeSendFileToExternalDirectory(Bitmap bitmap, String file_name, String path, String message_type) {


        File file = null;
// if image
        if (message_type.equals("1")) {
            if (!myDir_images_sent.exists()) {
                myDir_images_sent.mkdirs();
            }
            file = new File(myDir_images_sent, file_name);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (message_type.equals("2")) {
// if file to be written is video
            if (!myDir_omorni.exists()) {
                myDir_omorni.mkdirs();
            }
            int length;
            try {
                File original_file = new File(path);
                file = new File(myDir_omorni, file_name);
                InputStream inStream = new FileInputStream(original_file);
                OutputStream outStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length);
                }

                inStream.close();
                outStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    public static String getAbsolutePath(Context activity, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "";
    }


/*
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

// DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
// ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

// TODO handle non-primary volumes
            }
// DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
// MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video1".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
// MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
// File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
*/


    // save the given bitmap on external storage and returns the path of the file.
/*
    public static String saveImageToExternalStorage(Bitmap bitmap) {

        // Calendar now = Calendar.getInstance();
        String ImageURi = null;
        String imageFileName = "image" + System.currentTimeMillis() + ".jpg";
        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    AppController.getInstance().getString(R.string.app_name));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //  String mPath = sdf.format(now.getTime()) + ".jpg";
            File imageFile = new File(dir, imageFileName);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 60;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            ImageURi = imageFile.getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ImageURi;
    }
*/

    /*Calender date to string convert*/
    public static long calender_date_to_timestamp(String str_date) {
        long time_stamp = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = (Date) formatter.parse(str_date);
            time_stamp = date.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        time_stamp = time_stamp / 1000;
        return time_stamp;
    }

    /*Calender date to string convert*/
    public static long calenderDateTime_to_timestamp(String str_date) {
        long time_stamp = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.ENGLISH);
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            // formatter.setTimeZone(TimeZone.getTimeZone("UTC"));             /*to Set UTC or GMT format */
            formatter.setTimeZone(TimeZone.getDefault());             /*to Set default time zone */
            Date date = (Date) formatter.parse(str_date);
            time_stamp = date.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        time_stamp = time_stamp / 1000;
        return time_stamp;
    }



    /* *//*Calender date to string convert*//*
    public static long time_to_timestamp(String str_time) {
        long time_stamp = 0;
        try {
          //  SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Time date = (Time) formatter.parse(str_time);
            time_stamp = date.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        time_stamp = time_stamp / 1000;
        return time_stamp;
    }
*/


    public static String convertTimeToTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(tz);
        Date currenTimeZone = new Date(timestamp * 1000);
        return sdf.format(currenTimeZone);
    }

/*
    public static long time_to_timestamp(String str_date, String pattren) {
        long time_stamp = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattren);
            //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            // formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = (Date) formatter.parse(str_date);
            time_stamp = date.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        time_stamp = time_stamp / 1000;
        return time_stamp;
    }
*/

/*
    public static void noInternetPopup(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.internet_check);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        dialog.show();
    }
*/


    /**
     * convert utc time to local time zone when date is in the form of timestamp long data type.
     *
     * @param date   : time in UTC in long data type.
     * @param format : specify format that you want in output
     * @return : time according to your local time in the format you specified.
     */
   /* public static String utcLongToNormal(long date, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat converted = new SimpleDateFormat(format, Locale.ENGLISH);
        converted.setTimeZone(TimeZone.getDefault());
        return converted.format(cal.getTime());
    }*/

    /**
     * convert local time to utc
     *
     * @param time : local time
     * @return : timeStamp in UTC
     * @throws ParseException
     */
    public static String localToUtcTimestampConverter(String time) throws ParseException {
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//        String formattedDate = df.format(Calendar.getInstance().getTime());
//        Log.i("Activity", "::" + formattedDate);
//        time = formattedDate + " " + time;
        if (!time.equals("")) {
            time = time + " " + "11:00 am";
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
            Date localDate = formatter.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(localDate);
            cal.setTimeZone(TimeZone.getDefault());
            SimpleDateFormat converted = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
            converted.setTimeZone(TimeZone.getTimeZone("UTC"));
            String utcDate = converted.format(cal.getTime());
            long utcTimeStamp = converted.parse(utcDate).getTime();
            return String.valueOf(utcTimeStamp);
        } else {
            return "";
        }
    }

    public static String convertTimeStampToDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(tz);
        Date currenTimeZone = new Date(timestamp * 1000);
        return sdf.format(currenTimeZone);
    }

    public static String convertTimeStampToDate2(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
        sdf.setTimeZone(tz);
        Date currenTimeZone = new Date(timestamp * 1000);
        return sdf.format(currenTimeZone);
    }


   /* public static String convertTimeStampToTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        sdf.setTimeZone(tz);
        Date currenTimeZone = new Date(timestamp * 1000);
        return sdf.format(currenTimeZone);
    }*/


    public static String convertTimeStampToTime(long timestamp) {
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timestamp),
                TimeUnit.MILLISECONDS.toMinutes(timestamp) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timestamp)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(timestamp) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timestamp)));

        return hms;

    }

    /**
     * Convert millisecond to minutes
     *
     * @param milliseconds
     * @return minute in string form.
     */
    public static String convertMillisToMinutes(int milliseconds) {
        String minutes = "";
        float sec = milliseconds / 1000;
        float min = sec / 60;
        if (min < 1) {
            if (Math.round(sec) < 10) {
                minutes = "0.0" + Math.round(sec);
            } else {
                minutes = "0." + Math.round(sec);
            }
        } else {
            minutes = String.format(Locale.ENGLISH, "%.2f", min);
        }
        return minutes;
    }


    // convert time utc to local timezone

    /*** time represent the value of time in timestamp  */
    public static String utcToLocalTimestampConverter(String time, String format) throws ParseException {

        DateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date timeStamp = formatter.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(timeStamp);
//        cal.setTimeZone(TimeZone.getTimeZone("UTC"));


        SimpleDateFormat converted = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        converted.setTimeZone(TimeZone.getTimeZone(timeZone));
        String utcDate = converted.format(cal.getTime());
        return utcDate.toUpperCase();
    }


    /**
     * change the default language of application
     *
     * @param code : code of language that you want to use.
     */
/*
    public static void changeLanguage(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        AppController.getInstance().getBaseContext().getResources().updateConfiguration(config,
                AppController.getInstance().getBaseContext().getResources().getDisplayMetrics());
    }
*/


//    public static void showCommonDialog(final DialogType type, String title, String subtitle, final YesNoDialogListener yesNoDialogListener, Context context) {
//        final Dialog mDialog = new Dialog(context, R.style.MyDialog);
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Window window = mDialog.getWindow();
//        window.setGravity(Gravity.CENTER);
//        window.setGravity(Gravity.CENTER_VERTICAL);
//        window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_image)));
//        mDialog.setCancelable(false);
//        mDialog.setContentView(R.layout.dialog_layout);
//        TextView tvTitle = (TextView) mDialog.findViewById(R.id.tv_title);
//        TextView tvSubTitle = (TextView) mDialog.findViewById(R.id.tv_sub_title);
//        TextView tvYes = (TextView) mDialog.findViewById(R.id.tv_yes);
//        TextView tvNo = (TextView) mDialog.findViewById(R.id.tv_no);
//        TextView tvOk = (TextView) mDialog.findViewById(R.id.tv_ok);
//        ImageView ivCancel = (ImageView) mDialog.findViewById(R.id.iv_cancel);
//        ImageView ivTick = (ImageView) mDialog.findViewById(R.id.iv_tick);
//
//        LinearLayout llSingleBtn = (LinearLayout) mDialog.findViewById(R.id.ll_single_btn);
//        LinearLayout llDoubleBtn = (LinearLayout) mDialog.findViewById(R.id.ll_double_btn);
//        switch (type){
//            case LOGOUT:
//                break;
//            case REVIEW_SENT:
//                ivTick.setVisibility(View.VISIBLE);
//                llDoubleBtn.setVisibility(View.GONE);
//                llSingleBtn.setVisibility(View.VISIBLE);
//                break;
//            case HALL_UNAVAILABLE:
//                ivTick.setVisibility(View.GONE);
//                llDoubleBtn.setVisibility(View.GONE);
//                llSingleBtn.setVisibility(View.VISIBLE);
//                break;
//        }
//
//        tvTitle.setText(title);
//        tvSubTitle.setText(subtitle);
//
//
//        tvYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDialog.dismiss();
//                yesNoDialogListener.yesClicked(type);
//            }
//        });
//
//        tvNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDialog.dismiss();
//                yesNoDialogListener.noClicked(type);
//            }
//        });
//
//        tvOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                yesNoDialogListener.okClicked(type);
//            }
//        });
//
//        ivCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        mDialog.show();
//    }
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
// Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }


}
