package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean premission_given=false;
    private  static final String LOCATION_PREMISSION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PREMISSION=333;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        // and move the map's camera to the same location.


    }
}
