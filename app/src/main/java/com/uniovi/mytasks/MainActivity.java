package com.uniovi.mytasks;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniovi.mytasks.datos.TareasDataSource;
import com.uniovi.mytasks.modelo.Task;
import com.uniovi.mytasks.util.Lector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAREA_SELECCIONADA = "tarea_seleccionada";
    public static final String TAREA_DELETE = "tarea_delete";
    public final static int GESTION_TAREA = 1;
    public final static String TAREA_ADD = "tarea_add";
    List<Task> listaTareas;

    RecyclerView listaTareaView;

    private FloatingActionButton fABAdd;
    private FloatingActionButton fABEventos;
    private FloatingActionButton fABTareas;

    //TODO Arreglar Animaciones
    //private final Animation rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
    //private final Animation rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
    //private final Animation fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
    //private final Animation toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

    private boolean clicked = false;


    TareasDataSource taskDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler_view);

        cargarTareasDB();

        listaTareaView = findViewById(R.id.recyclerView);
        listaTareaView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaTareaView.setLayoutManager(layoutManager);

        if(listaTareas != null && !listaTareas.isEmpty())
            introListaTareas();

        introListaTareas();


        fABAdd = findViewById(R.id.botonAdd);
        fABAdd.setOnClickListener(view ->{
            onAddButtonClicked();
        });
        fABEventos = findViewById(R.id.fABEventos);
        fABEventos.setOnClickListener(view ->{
            crearNuevoEvento();

        });
        fABTareas = findViewById(R.id.fABTareas);
        fABTareas.setOnClickListener(view ->{
            crearNuevaTarea();
        });
    }

    private void onAddButtonClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        if (!clicked)
            clicked = true;
        else
            clicked = false;
    }

    private void setAnimation(boolean clicked) {
        if(!clicked){
            //TODO Arreglar Animaciones
            //fABEventos.startAnimation(fromBottom);
            //fABEventos.startAnimation(fromBottom);
            //fABAdd.startAnimation(rotateOpen);
        }else{
            //fABEventos.startAnimation(toBottom);
            //fABEventos.startAnimation(toBottom);
            //fABAdd.startAnimation(rotateClose);
        }
    }

    private void setVisibility(boolean clicked) {
        if(!clicked){
            fABEventos.setVisibility(VISIBLE);
            fABTareas.setVisibility(VISIBLE);
        }else{
            fABEventos.setVisibility(INVISIBLE);
            fABTareas.setVisibility(INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clickOnItem(Task tarea) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(TAREA_SELECCIONADA, tarea);
        startActivityForResult(intent,MODIFICAR_TAREA);
    }

    private void crearNuevaTarea(){
        Intent intent = new Intent(MainActivity.this,FormularioActivity.class);
        startActivityForResult(intent,GESTION_TAREA);
    }

    private void crearNuevoEvento(){
        Intent intent = new Intent(MainActivity.this,FormularioEventos.class);
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

    private void cargarTareasDB(){
        taskDataSource = new TareasDataSource(getApplicationContext());
        listaTareas = taskDataSource.getAllValorations();
    }
    //private void listaTareasAdapter(){}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GESTION_TAREA){
            if(resultCode == RESULT_OK){
                Task task = data.getParcelableExtra(TAREA_ADD);
                addTarea(task);
                listaTareas.add(task);
                introListaTareas();
            }
        }else if(requestCode == MODIFICAR_TAREA){
            if(resultCode == RESULT_OK){
                Task task = data.getParcelableExtra(TAREA_DELETE);
                deleteTask(task);
            }
        }else if(resultCode == RESULT_CANCELED)
            Log.d("MyTasks.MainActivity","FormularioActivity cancelada");
    }

    private void introListaTareas(){
        ListaTareasAdapter ltAdapter = new ListaTareasAdapter(listaTareas, new ListaTareasAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(Task item) {
                clickOnItem(item);
            }
        });
        listaTareaView.setAdapter(ltAdapter);
    }

    private void addTarea(Task task){
        task.setId(listaTareas.size());
        taskDataSource = new TareasDataSource(getApplicationContext());
        taskDataSource.createtask(task);
    }

    private void deleteTask(Task task){
        Integer i = null;
        for(Task t : listaTareas)
            if(t.getTitulo().equals(task.getTitulo()) && t.getFecha().equals(task.getFecha()) && t.getDescripcion().equals(task.getDescripcion()))
                i = listaTareas.indexOf(t);
        if(i !=null)
            listaTareas.remove(i.intValue());

        introListaTareas();
    }
}