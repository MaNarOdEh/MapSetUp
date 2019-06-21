package com.example.googlemap.weatherApi;

import com.google.gson.annotations.SerializedName;

public  class Weather {

    @SerializedName("main")
    public String mMain;//prefix instance variable with m

    @SerializedName("description")
    public String mDescription;
    public Weather(){

    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmMain() {
        return mMain;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmMain(String mMain) {
        this.mMain = mMain;
    }
}
