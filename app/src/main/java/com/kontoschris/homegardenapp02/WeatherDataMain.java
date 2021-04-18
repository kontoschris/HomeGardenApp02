//κλάση στην οποία φορτώνουμε τα δεδομενα του Section "MAIN" από το JSON αρχείο του OpenWeather API
//και συγκριμένα μόνο τα πεδία temp, feels_like & humidity
//"main": {
//        "temp": 291.53,
//        "feels_like": 290.68,
//        "temp_min": 290.37,
//        "temp_max": 292.59,
//        "pressure": 1019,
//        "humidity": 48
//        },
package com.kontoschris.homegardenapp02;

import com.google.gson.annotations.SerializedName;

public class WeatherDataMain {


    @SerializedName("temp")
    private Double  getTemperature;
    @SerializedName("feels_like")
    private Double getFeelsLike;
    @SerializedName("humidity")
    private Double getHumidity;

    public Double getGetTemperature() {
        return getTemperature;
    }

    public void setGetTemperature(Double getTemperature) {
        this.getTemperature = getTemperature;
    }

    public Double getGetFeelsLike() {
        return getFeelsLike;
    }

    public void setGetFeelsLike(Double getFeelsLike) {
        this.getFeelsLike = getFeelsLike;
    }

    public Double getGetHumidity() {
        return getHumidity;
    }

    public void setGetHumidity(Double getHumidity) {
        this.getHumidity = getHumidity;
    }
}
