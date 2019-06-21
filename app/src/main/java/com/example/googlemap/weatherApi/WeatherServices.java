package com.example.googlemap.weatherApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//make the connection with API
public interface WeatherServices {
    // call the response
    //api key query parameters latitude and loglotude
    //generate class annoymous
    //easy to swap the eoor
    //unitTesting &&unit test
    @GET("/data/2.5/weather")
    Call<WeatherResponse> get(@Query("APPID") String token,@Query("lat") String latitude,
                              @Query("lon") String longitude);
}
