package com.kikkos.gps_location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnDataShow;
    private Button btnGPStoggle;
    private Button btnNWtoggle;
    private Button btnClear;
    private Button btnBothProvToggle;
    private TextView txtData;
    private LocationManager locationManager;
    private LocationListener listener;
    private Location currentLocation = null;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing buttons and text view variables.
        btnDataShow = (Button) findViewById(R.id.btnGPSshow);
        btnGPStoggle = (Button) findViewById(R.id.btnGPStoggle);
        btnNWtoggle = (Button) findViewById(R.id.btnNWpro);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnBothProvToggle = (Button) findViewById(R.id.btnBothProviders);
        txtData = (TextView) findViewById(R.id.txtData);



        // Location Listener initialization here.
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // when the location is captured i check it with the current saved location
                // by using the isBetterLocation method and provide the location to the app
                // via the currentLocation var.
                if (LocationTools.isBetterLocation(location, currentLocation)){
                    currentLocation = location;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                // If the Location setting is disabled i created an
                // Alert dialog so that will tell and redirect user to the Locations
                // settings area, so that he can enable Location.
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("EnableLocation")
                        .setMessage("Your Location Settings is set to 'Off'.\nPlease Enable Location to use this app")
                        .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(locationIntent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        };

        // on this button click i show the location info stored in currentLocation var by the listener to the text view.
        btnDataShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation != null){
                    // using the bellow counter and if statement in order to clear the screen so that i can view all Locations captured.
                    if (i>= 5){
                        i=0;
                        txtData.setText("SCREEN RESETTED\n");
                        txtData.append("\n" + i++ + " ============"+"\nPROVIDER: "+ currentLocation.getProvider()+
                                "\nlat: "+ currentLocation.getLatitude()+
                                "\nlon: "+ currentLocation.getLongitude()+
                                "\ncity: "+LocationTools.getCity(v.getContext(), currentLocation.getLatitude(), currentLocation.getLongitude())+
                                "\ncountry: "+LocationTools.getCountry(v.getContext(), currentLocation.getLatitude(), currentLocation.getLongitude()));
                    }
                    txtData.append("\n" + i++ + " ============"+"\nPROVIDER: "+ currentLocation.getProvider()+
                            "\nlat: "+ currentLocation.getLatitude()+
                            "\nlon: "+ currentLocation.getLongitude()+
                            "\ncity: "+LocationTools.getCity(v.getContext(), currentLocation.getLatitude(), currentLocation.getLongitude())+
                            "\ncountry: "+LocationTools.getCountry(v.getContext(), currentLocation.getLatitude(), currentLocation.getLongitude()));
                }
            }
        });

        // requesting location updates from the gps provider.
        // By checking if the locationManager is null or not i made this button as an On/Off toggle button
        // requesting/removing location updates.
        btnGPStoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager == null){
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.INTERNET
                            }, 10);
                        }
                        return;
                    }else {
                        txtData.append("\nGPS STARTED");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                    }
                }else {
                    locationManager.removeUpdates(listener);
                    locationManager = null;
                    currentLocation = null;
                    txtData.append("\nGPS STOPPED");
                }
            }
        });

        // requesting location updates from the network provider.
        // By checking if the locationManager is null or not i made this button as an On/Off toggle button
        // requesting/removing location updates.
        btnNWtoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager == null){
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.INTERNET
                            }, 20);
                        }
                        return;
                    }else {
                        txtData.append("\nGPS STARTED");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                    }
                }else {
                    locationManager.removeUpdates(listener);
                    locationManager = null;
                    currentLocation = null;
                    txtData.append("\nGPS STOPPED");
                }
            }
        });

        // Here i enabled both providers at the same time. Which means i will be able to get location updates from both providers and filter
        // the locations using the isBetterLocation method.
        btnBothProvToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager == null){
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.INTERNET
                            }, 30);
                        }
                        return;
                    }else {
                        txtData.append("\nGPS STARTED");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                    }
                }else {
                    locationManager.removeUpdates(listener);
                    locationManager = null;
                    currentLocation = null;
                    txtData.append("\nGPS STOPPED");
                }
            }
        });

        // Cancelling the location updates request, clearing all variables and text view.
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager != null){
                    locationManager.removeUpdates(listener);
                    locationManager = null;
                }
                txtData.setText("GPS INFO HERE");
                i = 0;
                currentLocation = null;
            }
        });

    }


    // Here i handle all the permission requests by the requestCode.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    txtData.append("\nGPS STARTED");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                }
                return;
            case 20:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    txtData.append("\nGPS STARTED");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                }
                return;
            case 30:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    txtData.append("\nGPS STARTED");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                }
                return;
        }
    }
}
