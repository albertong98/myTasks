package com.uniovi.mytasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.uniovi.mytasks.modelo.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DetailsActivity extends AppCompatActivity {
    private TextView titulo;
    private TextView descripcion;
    private TextView fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Task task = intent.getParcelableExtra(MainActivity.TAREA_SELECCIONADA);

        titulo = findViewById(R.id.tituloTarea);
        descripcion = findViewById(R.id.descripcion);
        fecha = findViewById(R.id.fechaTarea);

        if(task != null) rellenarDatos(task);

        ImageButton delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(view ->{
            deleteTask(task);
        });
    }

    private void rellenarDatos(Task task){
        titulo.setText(task.getTitulo());
        descripcion.setText(task.getDescripcion());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        fecha.setText(df.format(task.getFecha()));
    }

    private void deleteTask(Task task){
        Intent intentResult = new Intent();
        intentResult.putExtra(MainActivity.TAREA_DELETE, task);
        setResult(RESULT_OK,intentResult);
        finish();
    }
}