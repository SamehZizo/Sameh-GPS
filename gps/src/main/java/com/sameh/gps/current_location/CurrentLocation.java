package com.sameh.gps.current_location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class CurrentLocation {

    private final int REQUEST_CODE = 1950;
    Activity activity;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    private Completed isCompleted;

    public CurrentLocation(Activity activity) {
        this.activity = activity;
    }

    public void getCurrentLocation(Completed completed) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.isCompleted = completed;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE);
        }
        else {
            this.isCompleted = completed;
            getLocation(completed);
        }
    }

    public void onLocationRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE && permissions.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(isCompleted);
            }
        }
    }

    private void getLocation(Completed completed) {
        buildLocationRequest();
        buildLocationCallback(completed);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        Go();
    }

    private void Go() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
    public void close(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void buildLocationCallback(final Completed completed) {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()){
                    completed.onCompleted(location);
                }
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    public interface Completed {
        void onCompleted(Location location);
    }
}
