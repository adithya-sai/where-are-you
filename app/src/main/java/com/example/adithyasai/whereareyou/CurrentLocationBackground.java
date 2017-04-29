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

import org.json.JSONException;
import org.json.JSONObject;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    String user;
    String authKey;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    double longitude;

    public String getDestcoord() {
        return destcoord;
    }

    public void setDestcoord(String destcoord) {
        this.destcoord = destcoord;
    }

    String destcoord;

    IBinder binder= new MyBinder();
    protected LocationManager locationManager;
    boolean flag;
    String provider;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    String dest;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    String groupId;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Inside onCreate");
        flag=false;
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(),5,15000);
        intent = new Intent(str_receiver);
//        fn_getlocation();
    }

    @Override
    public void onLocationChanged(Location arg0) {
        location=arg0;
        try
        {
            Thread.sleep(10000);
        }catch(Exception e){

        }
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
            e.printStackTrace();
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


    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        setDest(bundle.getString("destination"));
        setAuthKey(bundle.getString("authKey"));
        setUser(bundle.getString("user"));
        setGroupId(bundle.getString("groupId"));
        return START_STICKY;
    }

    private void fn_update(Location location){
        try {
            intent.putExtra("latitude", location.getLatitude() + "");
            intent.putExtra("longitude", location.getLongitude() + "");
            setLongitude(longitude);
            setLatitude(latitude);
            System.out.println(location.getLatitude() + " " + location.getLongitude());
            String[] u = new String[2];
            intent.setAction("YES");
            if (!getDest().isEmpty()) {
                System.out.println("Inside getDest");
                u[0] = "Get ETA";
                u[1] = buildUrlDistance();
                String s = new AsyncHttpPost(this).execute(u).get();
                JSONObject jo1 = new JSONObject(s);
                u = new String[7];
                u[0] = "UpdateLocation";
                u[1] = location.getLatitude()+"";
                u[2]=location.getLongitude()+"";
                u[3]=jo1.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text").toString();
                u[4]=getUser();
                u[5]=getAuthKey();
                u[6]=getGroupId();
                String s2 = new AsyncHttpPost(this).execute(u).get();
                System.out.println(s2);
                intent.putExtra("json", s2);
            }
            sendBroadcast(intent);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
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

    public String buildUrlDistance()
    {
        try
        {
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=";
            url += getLatitude() + "," + getLongitude();
            String[] s=getDest().split(",");
            url+="&destinations="+s[0]+"%2C"+s[1];
            System.out.println(url);
            return url;
        }
        catch(Exception je)
        {
            je.printStackTrace();
        }
        return null;
    }


}
