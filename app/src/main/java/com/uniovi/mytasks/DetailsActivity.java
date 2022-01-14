package com.uniovi.mytasks;

import static com.uniovi.mytasks.remote.ApiUtil.API_KEY;
import static com.uniovi.mytasks.remote.ApiUtil.LANGUAGE;
import static com.uniovi.mytasks.remote.ApiUtil.createWeatherApi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uniovi.mytasks.modelo.Task;
import com.uniovi.mytasks.modelo.WeatherResponse;
import com.uniovi.mytasks.remote.weatherApi;
import com.uniovi.mytasks.ui.tareas.MainFragmentTareas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private TextView titulo;
    private TextView descripcion;
    private TextView fecha;
    private TextView temperatura;
    private TextView descipcionTexto;

    private ImageView termo;
    private ImageView descripcionTiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Task task = intent.getParcelableExtra(MainFragmentTareas.TAREA_SELECCIONADA);

        titulo = findViewById(R.id.tituloTarea);
        descripcion = findViewById(R.id.descripcion);
        fecha = findViewById(R.id.fechaTarea);
        temperatura = findViewById(R.id.textTemperatura);
        descipcionTexto = findViewById(R.id.descripcionTexto);

        termo = findViewById(R.id.termometro);
        descripcionTiempo = findViewById(R.id.descripciontiempo);

        termo.setVisibility(View.INVISIBLE);
        weatherApi weatherApi = createWeatherApi();
        if(task != null) {
            rellenarDatos(task);
            if(task.getUbicacion() != null && !task.getUbicacion().isEmpty() && task.getUbicacion().length() != 0)
                peticionTiempo(weatherApi,task);
        }
    }

    private void rellenarDatos(Task task){
        titulo.setText(task.getTitulo());
        descripcion.setText(task.getDescripcion());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        fecha.setText(df.format(task.getFecha()));
    }

    private void peticionTiempo(weatherApi api,Task task){
        termo.setVisibility(View.VISIBLE);

        String[] latlon = task.getUbicacion().split(",");
        String lat = latlon[0];
        String lon = latlon[1].trim();
        if(!lat.trim().isEmpty() && lat.length() != 0 && !lon.trim().isEmpty() && lon.length() != 0) {
            Call<WeatherResponse> call = api.getWeatherForLatAndLon(lat,lon,API_KEY,"metric",LANGUAGE);

            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    descipcionTexto.setText(response.body().weather.get(0).description);
                    temperatura.setText(response.body().main.getTemp()+" ºC");
                    Picasso.get().load("http://openweathermap.org/img/wn/"+response.body().weather.get(0).icon+"@2x.png").into(descripcionTiempo);
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {

                }
            });
        }
    }
}