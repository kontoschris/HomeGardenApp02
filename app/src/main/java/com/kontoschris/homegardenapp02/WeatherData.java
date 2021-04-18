//Κλάση στη οποία θα επισραφούν τα δεδομένα από την κλήση μέσω Rerofit στο OpenWeather
//Παρατήρηση: πέρνουμε μόνο το section "MAIN" του αρχείου JSON
////ΠΧ
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

public class WeatherData {
    //Από το JSON αρχειο του OpenWeather φορτώνουμε μονο το Main Section
    @SerializedName("main")
    private WeatherDataMain weatherDataMain;

    public WeatherDataMain getWeatherDataMain(){

        return  weatherDataMain;
    }
}
