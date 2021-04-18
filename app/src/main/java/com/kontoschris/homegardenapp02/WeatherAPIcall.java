//Inteface Που υλοποιεί την κλήση για το OpenWeather Μέσω Retrofit (περισσότερα στο word document της εργασίας)
//Retrfit documentantion https://square.github.io/retrofit/
//Παράδειγμα και χρήση https://www.journaldev.com/13639/retrofit-android-example-tutorial
package com.kontoschris.homegardenapp02;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPIcall {


    @GET("weather")

    //όπου q = πόλη
    //και appid το id Που δημιούργησα το OpenWeather
    Call<WeatherData> getWeatherData(@Query("q") String city, @Query("appid") String apiKEY); //Send to retrofit data for query


}
