package com.uniovi.mytasks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mytasks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniovi.mytasks.modelo.Task;
import com.uniovi.mytasks.util.Lector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static int GESTION_TAREA = 1;
    public final static String TAREA_ADD = "tarea_add";
    List<Task> listaTareas;
    /**
     * TODO: Pendiente de implementar vista para poder ver las tareas a modo de lista y pulsar en ellas
     * **/
    //RecyclerView listaTareasView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarTareas();

        FloatingActionButton fab = findViewById(R.id.botonAdd);
        fab.setOnClickListener(view ->{
            crearNuevaTarea();
        });
    }

    private void crearNuevaTarea(){
        Intent intent = new Intent(MainActivity.this,FormularioActivity.class);
        startActivityForResult(intent,GESTION_TAREA);
    }

    private void cargarTareas(){
        listaTareas = new ArrayList<Task>();
        String result = Lector.leerDeJson(getApplicationContext(),"tareas.json");
        try{
            JSONObject jsonTareas = new JSONObject(result);
            JSONArray tareas = jsonTareas.getJSONArray("tareas");
            for(int i=0;i<tareas.length();i++){
                JSONObject tarea = tareas.getJSONObject(i);
                String titulo = tarea.getString("titulo");
                String descripcion = tarea.getString("descripcion");
                String fecha = tarea.getString("fecha");

                listaTareas.add(new Task(titulo,descripcion,new SimpleDateFormat("dd/MM/yyyy").parse(fecha)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }catch(ParseException p){
            p.printStackTrace();
        }
    }
    //private void listaTareasAdapter(){}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GESTION_TAREA){
            if(resultCode == RESULT_OK){
                Task task = data.getParcelableExtra(TAREA_ADD);
                listaTareas.add(task);
                //listaTareasAdapter()
            }
        }else if(resultCode == RESULT_CANCELED)
            Log.d("MyTasks.MainActivity","FormularioActivity cancelada");
    }
}