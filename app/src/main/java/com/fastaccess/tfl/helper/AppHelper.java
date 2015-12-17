package com.fastaccess.tfl.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by kosh20111 on 10/7/2015
 */
public class AppHelper {

    private static final int GPS_REQUEST_CODE = 2004;

    public static boolean isOnline(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (isM()) {
            Network networks = cm.getActiveNetwork();
            NetworkInfo netInfo = cm.getNetworkInfo(networks);
            haveConnectedWifi = netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
            haveConnectedMobile = netInfo.getType() == ConnectivityManager.TYPE_MOBILE && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
            return haveConnectedWifi || haveConnectedMobile;
        } else {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected())
                        haveConnectedMobile = true;
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }
    }

    public static boolean isM() {return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;}

    public static boolean isLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isBelowLollipop() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void turnGpsOn(Activity context) {
        context.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_REQUEST_CODE);
    }

    public static void start(Activity aciActivity, Class cl, View view, String transName) {
        Intent intent = new Intent(aciActivity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);
    }

    public static void start(Activity aciActivity, Intent intent, View view, String transName) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);

    }

    public static void startForResult(Activity aciActivity, Class cl, int code, View view, String transName) {
        Intent intent = new Intent(aciActivity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivityForResult(intent, code, options.toBundle());
        else
            aciActivity.startActivityForResult(intent, code);
    }

    public static void startForResult(Activity aciActivity, Intent cl, int code, View view, String transName) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivityForResult(cl, code, options.toBundle());
        else
            aciActivity.startActivityForResult(cl, code);
    }

    public static void startWithExtra(Activity aciActivity, Class cl, Bundle bundle, View view, String transName) {
        Intent intent = new Intent(aciActivity, cl);
        intent.putExtras(bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);
    }

    @SafeVarargs public static void start(Activity aciActivity, Class cl, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(aciActivity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, sharedElements);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);
    }

    @SafeVarargs public static void start(Activity aciActivity, Intent intent, Pair<View, String>... sharedElements) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, sharedElements);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);

    }

    public static void changeStatusBarColor(@NonNull Activity activity, @ColorInt int color) {
        if (color == 0) return;
        if (isLollipopOrHigher()) {
            float cl = 0.9f;
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= cl;
            int primaryDark = Color.HSVToColor(hsv);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(primaryDark);
        }
    }

    public static boolean isInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    public static String saveBitmap(Bitmap image) {
        try {
            File file = FileHelper.generateFile();
            OutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
            fOut.flush();
            fOut.close();
            Log.e("PAth", file.getPath());
            return file.getPath();
        } catch (Exception e) {
            return null;
        }
    }

}
