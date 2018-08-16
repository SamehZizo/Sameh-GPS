package com.sameh.samehgps;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sameh.gps.current_location.CurrentLocation;

public class MainActivity extends AppCompatActivity {

    CurrentLocation currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLocation = new CurrentLocation(this);
        currentLocation.getCurrentLocation(new CurrentLocation.Completed() {
            @Override
            public void onCompleted(Location location) {
                Log.d("Sameh",location.getLatitude()+"/"+location.getLongitude());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        currentLocation.onLocationRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
