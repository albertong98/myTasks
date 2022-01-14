package com.uniovi.mytasks.remote;

import com.uniovi.mytasks.modelo.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherApi {
    public static final String BASE_URL= "https://api.openweathermap.org/data/2.5/";

    @GET("weather?APPID="+ApiUtil.API_KEY+"&units=metric&lang="+ApiUtil.LANGUAGE)
    Call<WeatherResponse> getWeatherForCity(@Query("q") String city);

    @GET("weather")
    Call<WeatherResponse> getWeatherForLatAndLon(@Query("lat") String lat,
                                                 @Query("lon") String lon,
                                                 @Query("APPID") String appid,
                                                 @Query("units")String units,
                                                 @Query("lang") String lang);
}
