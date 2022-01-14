package com.uniovi.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uniovi.mytasks.modelo.Task;
import com.uniovi.mytasks.ui.tareas.MainFragmentTareas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormularioActivity extends AppCompatActivity {

    private int day, month, year, hour, min;

    EditText titulo;
    EditText detalles;

    //datos del datepicker
    EditText txtDate;
    EditText txtHour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Button buttonOk = findViewById(R.id.buttonOk);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        ImageButton btnDate = findViewById(R.id.imgBtnDate);
        ImageButton btnTime = findViewById(R.id.imgBtnTime);

        titulo = (EditText) findViewById(R.id.txtTituloTareaAñadir);
        detalles = (EditText) findViewById(R.id.txtDetallesAñadir);
        txtDate = (EditText) findViewById(R.id.txtFecha);
        txtHour = (EditText) findViewById(R.id.txtHora);


        buttonOk.setOnClickListener(view -> {
            if(titulo.getText().toString().isEmpty() || detalles.getText().toString().isEmpty() || txtDate.getText().toString().isEmpty() || txtHour.getText().toString().isEmpty()) {
                Toast emptyFields =
                        Toast.makeText(getApplicationContext(),
                                "Ninguno de los campos puede estar vacío", Toast.LENGTH_SHORT);

                emptyFields.show();

            }else{
                Task task = null;
                try {
                    task = new Task(titulo.getText().toString(),detalles.getText().toString(),new SimpleDateFormat("dd/MM/yyyy").parse(txtDate.getText().toString()), new SimpleDateFormat("hh:mm").parse(txtHour.getText().toString()),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intentResult = new Intent();
                intentResult.putExtra(MainActivity.TAREA_ADD,task);
                setResult(RESULT_OK,intentResult);
                finish();
            }
        });

        buttonCancel.setOnClickListener(view ->{
                setResult(RESULT_CANCELED);
                finish();
        });

        btnDate.setOnClickListener(view -> {
                final Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int año, int monthOfYear, int dayOfMonth) {

                        year = año;
                        day = dayOfMonth;
                        month = monthOfYear;
                        txtDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

                    }
                }
                        , year, month, day);
                datePickerDialog.show();

        });

        btnTime.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            min = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                    hour = hourOfDay;
                    min = minute;
                    txtHour.setText(hourOfDay+":"+minute);

                }
            }
                    , hour, min, true);
            timePickerDialog.show();
        });
    }

    public void Agregar(View view){

        Calendar calendar = Calendar.getInstance();

        boolean aux = false;
        Intent intent = null;
        while(aux == false){
            try{
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);

                intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());

                intent.putExtra(CalendarContract.Events.TITLE, titulo.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, detalles.getText().toString());

                startActivity(intent);
                aux = true;
            } catch (Exception e) {
                txtDate.setText("");
                Toast.makeText(getApplicationContext(), "Fecha Inválida", Toast.LENGTH_SHORT).show();
            }
        }
    }

}