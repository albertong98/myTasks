package com.uniovi.mytasks.remote;

import com.uniovi.mytasks.modelo.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherApi {
    public static final String BASE_URL= "https://api.openweathermap.org/data/2.5/";

    @GET("weather?APPID="+ApiUtil.API_KEY+"&units=metric")
    Call<WeatherResponse> getWeatherForCity(@Query("q") String city);
}
