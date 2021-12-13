package com.uniovi.mytasks.remote;

import retrofit2.Retrofit;

public class ApiUtil {
    public static final String LANGUAGE = "es-ES";
    public static final String API_KEY = "f27e388816e3e7ace91eb06f36818262";

    public static weatherApi createWeatherApi() {
        Retrofit retrofit = RetrofitClient.getClient(weatherApi.BASE_URL);

        return retrofit.create(weatherApi.class);
    }
}
