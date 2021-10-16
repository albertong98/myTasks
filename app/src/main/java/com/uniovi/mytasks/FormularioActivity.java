package com.uniovi.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mytasks.R;
import com.uniovi.mytasks.modelo.Task;

import java.util.Date;

public class FormularioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Button buttonOk = findViewById(R.id.buttonOk);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        buttonOk.setOnClickListener(view -> {
            /** TODO: Obtener datos correctos de la tarea **/
            Task task = new Task("","",new Date(System.currentTimeMillis()));
            Intent intentResult = new Intent();
            intentResult.putExtra(MainActivity.TAREA_ADD,task);
            setResult(RESULT_OK,intentResult);
            finish();
        });

        buttonCancel.setOnClickListener(view ->{
                setResult(RESULT_CANCELED);
                finish();
        });
    }
}