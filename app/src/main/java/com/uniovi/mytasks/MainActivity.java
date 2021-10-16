package com.uniovi.mytasks;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mytasks.R;
import com.uniovi.mytasks.modelo.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static int GESTION_TAREA = 1;

    List<Task> listaTareas;
    /**
     * TODO: Pendiente de implementar vista para poder ver las tareas a modo de lista y pulsar en ellas
     * **/
    //RecyclerView listaTareasView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void crearNuevaTarea(){
        Intent intent = new Intent(MainActivity.this,FormularioActivity.class);
        startActivityForResult(intent,GESTION_TAREA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GESTION_TAREA){
            if(resultCode == RESULT_OK){

            }
        }else if(resultCode == RESULT_CANCELED)
            Log.d("MyTasks.MainActivity","FormularioActivity cancelada");
    }
}