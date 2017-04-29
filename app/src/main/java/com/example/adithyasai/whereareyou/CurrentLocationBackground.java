package com.example.adithyasai.whereareyou;
import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gautham on 4/21/17.
 */


public class CurrentLocationBackground extends Service implements LocationListener
{

    private final Context context;

    Intent intent;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private Timer mTimer = null;
    private Handler mHandler = new Handler();
    Location location;
    public static String str_receiver = "servicetutorial.service.receiver";
    double latitude;
    double longitude;
    IBinder binder= new MyBinder();
    protected LocationManager locationManager;
    boolean flag;
    String provider;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1;

    public CurrentLocationBackground()
    {
        context=null;
    }

    public CurrentLocationBackground(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        flag=false;
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(),5,400);
        intent = new Intent(str_receiver);
//        fn_getlocation();
    }

    @Override
    public void onLocationChanged(Location arg0) {
        location=arg0;
        fn_update(location);
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    public class MyBinder extends Binder {

        public CurrentLocationBackground getService() {
            return CurrentLocationBackground.this;
        }
    }

    private void fn_getlocation(){
        if (PermissionChecker.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //return;
            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        locationManager = (LocationManager) getBaseContext()
                .getSystemService(LOCATION_SERVICE);
        try {
            if (flag == false) {



                // Getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);


                if (!isGPSEnabled) {
                    // No network provider is enabled
                } else {


                    // If GPS enabled, get latitude/longitude using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
                flag=true;
            }
            else {
                Criteria criteria = new Criteria();
                provider = LocationManager.GPS_PROVIDER; //Specifying usage of GPS Provider
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationManager.requestLocationUpdates(provider, 400, 1, this);
            }
        }catch(Exception e)
        {

        }

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }


    private void fn_update(Location location){

        intent.putExtra("latitude",location.getLatitude()+"");
        intent.putExtra("longitude",location.getLongitude()+"");
        sendBroadcast(intent);
    }

    private boolean getGPSStatus()
    {
        String allowedLocationProviders =
                Settings.System.getString(getContentResolver(),
                        Settings.System.LOCATION_PROVIDERS_ALLOWED);

        if (allowedLocationProviders == null) {
            allowedLocationProviders = "";
        }

        return allowedLocationProviders.contains(LocationManager.GPS_PROVIDER);
    }



}
