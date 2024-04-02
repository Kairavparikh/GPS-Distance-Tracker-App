package com.example.gpsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.os.SystemClock;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String KEY_STRING = "abcd";
    private static final String KEY_STRING1 = "abcd";
    int count = 0;

    List<GPS> currentAddress;
    TextView textViewLat,textViewLon,textViewAddress,textViewDistance;

    private LocationManager locationManager;
    private LocationListener locationListener;
    Location priorLocation;
    double lon;
    ListView listView;
    double lat;
    double totTime;
    double distance;
    double totalDistance;
    String address1;
    List<Address> address;
    List<String> currentSpot;
    List<String> currentPoint;
    Geocoder geocoder;
    long timeSpent;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(KEY_STRING, distance);
        outState.putSerializable(KEY_STRING1, (Serializable) currentAddress);
        locationManager.removeUpdates(locationListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentAddress = new ArrayList<GPS>();
        currentPoint = new ArrayList<String>();
        currentSpot = new ArrayList<String>();
        textViewLat = findViewById(R.id.textView123);
        textViewLon = findViewById(R.id.textView2);
        textViewAddress = findViewById(R.id.textViewAdress);
        textViewDistance = findViewById(R.id.textView4);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.US);
        listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, R.layout.adapter_layout, currentAddress);
        listView.setAdapter(adapter);
        if (savedInstanceState != null) {
            currentAddress = (ArrayList<GPS>) savedInstanceState.getSerializable(KEY_STRING1);
            distance = savedInstanceState.getDouble(KEY_STRING);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                textViewLat.setText("Latitude: " + lat);
                textViewLon.setText("Longitude: " + lon);
                try {
                    address = geocoder.getFromLocation(lat, lon, 1);
                    address1 = address.get(0).getAddressLine(0);
                    textViewAddress.setText("" + address1);
                    currentPoint.add(String.valueOf(address1));
                    Log.d("Address", String.valueOf(address1));
                    Log.d("CurrentPoint", String.valueOf(currentPoint));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                currentSpot.add(address1);
                priorLocation = location;
                Log.d("Location", "Latitude: " + lat+ ", Longitude: " + lon);
                if(currentAddress.size()  != 0) {
                    if (!(currentAddress.get(currentAddress.size()-1).getAddress().equals(address1))) {
                        //for(int i = 0; i < currentPoint.size(); i++)
                        currentAddress.add(new GPS(currentPoint.get(currentPoint.size()-1), "CURRENTLY HERE", location));
                        if(currentAddress.size() > 1){
                            long time = ((SystemClock.elapsedRealtime()- timeSpent) / 1000);
                                long seconds = time % 60;
                                currentAddress.get(currentAddress.size()-1).setTimeSpent(seconds+ "seconds");
                            //timeSpent = SystemClock.elapsedRealtime();
                            Log.d("Total Time12345", String.valueOf(time));
                        }
                        adapter.notifyDataSetChanged();
                        distance += (currentAddress.get(0).getLocation().distanceTo(currentAddress.get(1).getLocation()));
                        textViewDistance.setText(distance + " meters");
                    }

                }
                else{
                    //priorLocation = location;
                    textViewDistance.setText((distance+"meters"));
                    currentAddress.add(new GPS(currentPoint.get(currentPoint.size()-1), "STARTED HERE", location));
                    timeSpent = SystemClock.elapsedRealtime();
                    adapter.notifyDataSetChanged();
                }

                Log.d("REAL", String.valueOf(currentAddress));
                Log.d("Current Address", String.valueOf(currentAddress));
                //Location priorLocation = new Location(location);
                Log.d("Prior Location", String.valueOf(priorLocation));
                Log.d("part time", String.valueOf(priorLocation.getTime()));
                Log.d("Total Time", String.valueOf(timeSpent));

                if(priorLocation == null){
                    Log.d("ERROR", " ");
                }
                /*if(priorLocation != null) {
                    distance = priorLocation.distanceTo(location);
                    totalDistance += distance;
                }
                 */
                Log.d("Distance", String.valueOf(distance));

                Log.d("Total Distance", String.valueOf(totalDistance));
            }

        };

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            textViewAddress.setText(address1);
            textViewLat.setText(""+lat);
            textViewLon.setText(""+lon);
            textViewDistance.setText(""+distance);
        } else {
            textViewAddress.setText(address1);
            textViewLat.setText(""+lat);
            textViewLon.setText(""+lon);
            textViewDistance.setText(""+distance);

            //count++;
            //removeOnConfigurationChangedListener((Consumer<Configuration>) locationListener);

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        else {
            UpdateLocation();
        }
        }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                UpdateLocation();
            } else {
                Log.e("Permission", "Denied.");
            }
        }
    }

    private void UpdateLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        }
    }
}

