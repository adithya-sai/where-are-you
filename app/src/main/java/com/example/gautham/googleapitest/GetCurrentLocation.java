package com.example.gautham.googleapitest;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by gautham on 4/8/17.
 */

public class GetCurrentLocation implements LocationListener{
        private Context context;
        String lat,lon;
        GetCurrentLocation(Context context)
        {
            this.context=context;
        }

        @Override
        public void onLocationChanged(Location loc) {

            Toast.makeText(context, "Location changed: Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();
            lon=longitude;
            lat=latitude;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
        public String[] getLoc()
            {
            System.out.println("Inside getLoc");
            String[] coordinates= new String[2];
            coordinates[0]=lat;
            coordinates[1]=lon;
            return coordinates;
            }

}
