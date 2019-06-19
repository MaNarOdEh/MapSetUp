package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean premission_given=false;
    private  static final String LOCATION_PREMISSION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PREMISSION=333;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView txt_gbs,txt_sessons,text_temp;
    String OPEN_WEATHER_MAP_API = "d32d64021d1cb92523a372dd36a23037";
    MarkerOptions options;
    Marker marker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        txt_sessons=findViewById(R.id.txt_sessons);
        txt_gbs=findViewById(R.id.txt_gbs);
        text_temp=findViewById(R.id.text_temp);
        txt_sessons=findViewById(R.id.txt_sessons);
        accessLocation();

    }
    private boolean isSucessServices(){
        int avaliable= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);
        if(avaliable== ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(avaliable)){
           //
            return false;
        }else{
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        premission_given=false;
        switch(requestCode){
            case MY_PREMISSION:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            premission_given=false;
                            return;
                        }
                    }
                    premission_given=true;
                    //initalize The Map
                    initTheMap();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }

            }
        }
    }

    private void accessLocation(){
        String []premission={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==
                    PackageManager.PERMISSION_GRANTED){
                premission_given=true;
                initTheMap();
            }
            else{
                ActivityCompat.requestPermissions(this,premission,MY_PREMISSION);
            }
        }else{
            ActivityCompat.requestPermissions(this,premission,MY_PREMISSION);
        }
    }
    private void getDeviceCurrentLocations(){
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        /*try{
            if(premission_given){
                final Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                          //  Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    14f);

                        }else{
                          //  Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (Exception e){

        }*/
    }
    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
    private void initTheMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
           // mMap = googleMap;
           // mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {


                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    }
                });

            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(32, 35);
         options = new MarkerOptions()
                .position(sydney)
                .title("I am here!");
       // marker= mMap.addMarker(new MarkerOptions().position(sydney).title("Palestine"));
        Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geo.getFromLocation(32, 35, 1);
            if (addresses.isEmpty()) {
            //    addres.setText("Waiting for Location");
            } else {
                if (addresses.size() > 0) {
                    marker= mMap.addMarker(new MarkerOptions().position(sydney).title(addresses.get(0).getAddressLine(0)));
                    options.title(addresses.get(0).getAddressLine(0));
                    //txt_sessons.setT
                    //addres.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                }
            }
        }catch(Exception e){

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(marker!=null){
                    marker.remove();
                }
                LatLng lat = new LatLng(latLng.latitude, latLng.longitude);
                 options = new MarkerOptions()
                        .position(lat)
                        .title("I am here!");
                Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geo.getFromLocation(32, 35, 1);
                    if (addresses.isEmpty()) {
                        //    addres.setText("Waiting for Location");
                    } else {
                        if (addresses.size() > 0) {
                            marker= mMap.addMarker(new MarkerOptions().position(lat).title(addresses.get(0).getAddressLine(0)));
                            options.title(addresses.get(0).getAddressLine(0));
                            //addres.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                        }
                    }
                }catch(Exception e){
                }
                marker=mMap.addMarker(options);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                txt_gbs.setText( Math.round(latLng.latitude)+","+Math.round(latLng.longitude)+"  ");
            }
        });
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        // and move the map's camera to the same location.
    }

    public static String excuteGet(String targetURL)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
