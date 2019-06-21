package com.example.googlemap.weatherApi;

import com.google.gson.annotations.SerializedName;

public class WeatherMain {

    @SerializedName("temp")
    public double mTemp;

    @SerializedName("temp_min")
    public double mTempMain;

    @SerializedName("temp_max")
    public double mTempMax;

    public WeatherMain() {

    }

    public double getmTempMax() {
        return mTempMax;
    }

    public double getmTempMain() {
        return mTempMain;
    }

    public double getmTemp() {
        return mTemp;
    }

    public void setmTempMax(double mTempMax) {
        this.mTempMax = mTempMax;
    }

    public void setmTempMain(double mTempMain) {
        this.mTempMain = mTempMain;
    }

    public void setmTemp(double mTemp) {
        this.mTemp = mTemp;
    }
}
