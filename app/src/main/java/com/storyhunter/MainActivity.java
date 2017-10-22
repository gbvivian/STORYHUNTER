package com.storyhunter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.storyhunter.api.MarvelAPI;
import com.storyhunter.model.EventResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vivianlam on 2017-10-22.
 */

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MarvelAPI marvelAPI;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "Creating");
        super.onCreate(savedInstanceState);

        marvelAPI = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl("http://10.18.236.24:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarvelAPI.class);

        marvelAPI.event().enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.i("HTTP Request", response.body().description);
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
             Log.d("onCreate", "No location permissions");
            return;
        }
        System.out.println("about to go mLocationManager");
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, mLocationListener);
        // Log.i("onCreate", "Setup location handler");

        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        System.out.println("Entering onmapsready");

        googleMap = map;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        System.out.println("about to check permissions");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        System.out.println("coming out");
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        System.out.println("going into addmarker");

        map.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Marker"));

        Log.i("Our location is", String.valueOf(location.getLatitude()));
        /*

        googleMap = map;
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        */
        System.out.println("coming out of addmarker");
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            // Log.i("LocationListener", "Location changed");
            System.out.println("Entering priv fn location listener");
            Log.i("Our new location is", String.valueOf(location.getLatitude()));
            googleMap.clear();

            MarkerOptions mp = new MarkerOptions();

            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));

            mp.title("my position");

            googleMap.addMarker(mp);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 16));
        }

        @Override
        public void onStatusChanged (String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled (String provider) {

        }

        @Override
        public void onProviderDisabled (String provider) {

        }
    };

}
