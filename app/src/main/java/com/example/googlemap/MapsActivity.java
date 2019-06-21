package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//https://thecatapi.com/
import com.example.googlemap.weatherApi.Weather;
import com.example.googlemap.weatherApi.WeatherMain;
import com.example.googlemap.weatherApi.WeatherResponse;
import com.example.googlemap.weatherApi.WeatherServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean premission_given=false;
    private  static final String LOCATION_PREMISSION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int MY_PREMISSION=333;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView txt_gbs,txt_sessons,text_temp;
    private MarkerOptions options;
    private Marker marker;
    private String OPEN_WEATHER_MAP_API = "a21a79d3c32d92ebc8f8ee542782377f";
    public static final String BASE_URL = "https://api.openweathermap.org";
    private LinearLayout progress_bar,main_layout,linear_start_progress;
    private BottomNavigationView bottom_navigation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initializeAllWidget();
        makeNecessaryEvent();
        initTheMap();

    }
    private void initializeAllWidget() {
        txt_sessons=findViewById(R.id.txt_sessons);
        txt_gbs=findViewById(R.id.txt_gbs);
        text_temp=findViewById(R.id.text_temp);
        txt_sessons=findViewById(R.id.txt_sessons);
        progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        main_layout=findViewById(R.id.main_layout);
        linear_start_progress=findViewById(R.id.linear_start_progress);
        bottom_navigation=findViewById(R.id.bottom_navigation);
    }
    public void makeNecessaryEvent(){
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.map_hybrid:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case R.id.map_normal:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.map_stateLite:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case R.id.map_terrain:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
                menuItem.setChecked(true);

                return false;
            }
        });
    }

    private void fetchWeather(double lat,double lon) {
        show_progress_bar();
       if(checkInternetConnections()) {
           //Generate  the service && connect to the url
           Retrofit retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
           //Status Coden header
           //2
           //bad request
           //server error
           //scuseesful
           WeatherServices services = retrofit.create(WeatherServices.class);
           services.get(OPEN_WEATHER_MAP_API, lat + "", lon + "").enqueue(new Callback<WeatherResponse>() {
               @Override
               public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                   hide_progress_bar();
                   //handle error
                   if (!response.isSuccessful()) {
                       try {
                          // txt_sessons.setText(response.errorBody().string());
                           Snackbar snackbar = Snackbar
                                   .make(main_layout, response.message(), Snackbar.LENGTH_LONG);
                           snackbar.show();


                       } catch (Exception e) {

                       }
                       return;
                   }

                   if (response != null && response.body() != null) {
                       List<Weather> w = response.body().getmWeather();
                       if (w != null && w.size() >= 0) {
                           txt_sessons.setText(w.get(0).getmDescription() + "  ");
                           WeatherMain weatherMain = response.body().getmMain();
                           text_temp.setText(Math.round(weatherMain.getmTemp() - 273.15) + "  ");
                       }

                   }
               }

               @Override
               public void onFailure(Call<WeatherResponse> call, Throwable t) {
                   hide_progress_bar();
                   Snackbar snackbar = Snackbar
                           .make(main_layout, t.getMessage(), Snackbar.LENGTH_LONG);
                   snackbar.show();

               }
           });
       }else{

       }

    }
    private  void  hide_progress_bar(){
        progress_bar.setVisibility(View.GONE);
        main_layout.setVisibility(View.VISIBLE);
    }
    private  void show_progress_bar(){
        progress_bar.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
    }
    private boolean checkInternetConnections(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;

            // Do whatever
        }else{
            Snackbar snackbar = Snackbar
                    .make(main_layout, "You are not connected to the wifi", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return false;
    }

    private void initTheMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        linear_start_progress.setVisibility(View.GONE);
        mMap = googleMap;
        LatLng palestine = new LatLng(32, 35);
         options = new MarkerOptions()
                .position(palestine)
                .title("Palestine!");
         txt_gbs.setText("Palestine!");
        marker= mMap.addMarker(options);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(palestine));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker!=null){
                    marker.remove();
                }
                LatLng lat = new LatLng(latLng.latitude, latLng.longitude);
                fetchWeather(latLng.latitude,latLng.longitude);
                 options = new MarkerOptions()
                        .position(lat);

                Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geo.getFromLocation(lat.latitude, lat.longitude, 1);
                    fetchWeather(lat.latitude,lat.longitude);

                    if (addresses.isEmpty()) {
                    } else {
                        if (addresses.size() > 0) {
                            String cityName = addresses.get(0).getAddressLine(0);
                            String stateName = addresses.get(0).getAddressLine(1);
                            String countryName = addresses.get(0).getAddressLine(2);
                         //   Toast.makeText(MapsActivity.this, addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                             String ans=(countryName!=null?countryName+" ":"")+(stateName!=null?stateName+"  ":"")+(cityName!=null?cityName+"  ":"");
                             ans=ans.trim();
                             if(ans!=null&&!ans.isEmpty()) {
                                 txt_gbs.setText(ans);
                                 options.title(ans);
                             }else {
                                 options.title("Unkonwn");
                                 txt_gbs.setText("Unkonwn");
                             }
                            //options.title(addresses.get(0).getLocality());
                            //txt_gbs.setText(addresses.get(0).getLocality());
                        }
                    }
                }catch(Exception e){

                }
                marker= mMap.addMarker(options);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
    }


}
