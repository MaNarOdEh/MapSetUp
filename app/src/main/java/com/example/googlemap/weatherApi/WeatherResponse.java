package com.example.googlemap.weatherApi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    //this key alloways mathecs to the response object
    @SerializedName("weather")
    public List<Weather> mWeather;

    @SerializedName("main")
    public WeatherMain mMain;

    public List<Weather> getmWeather() {
        return mWeather;
    }

    public WeatherMain getmMain() {
        return mMain;
    }

    public void setmMain(WeatherMain mMain) {
        this.mMain = mMain;
    }

    public void setmWeather(List<Weather> mWeather) {
        this.mWeather = mWeather;
    }
}
