package com.uniovi.mytasks;

import static com.uniovi.mytasks.remote.ApiUtil.createWeatherApi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.uniovi.mytasks.modelo.WeatherResponse;
import com.uniovi.mytasks.remote.weatherApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    TextView temperatura;
    TextView descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        temperatura = findViewById(R.id.temperatura);
        descripcion = findViewById(R.id.valorDescripcion);

        weatherApi weatherApi = createWeatherApi();

        findViewById(R.id.buttonCity).setOnClickListener(v -> {
            peticionTiempo(weatherApi);
        });
    }

    private void peticionTiempo(weatherApi api){
        EditText city = findViewById(R.id.cityName);
        if(!city.getText().toString().trim().isEmpty() && city.getText().length() != 0) {
            Call<WeatherResponse> call = api.getWeatherForCity(city.getText().toString());

            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    temperatura.setText(response.body().main.getTemp()+" ºC");
                    descripcion.setText(response.body().weather.get(0).description);
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {

                }
            });
        }
    }
}