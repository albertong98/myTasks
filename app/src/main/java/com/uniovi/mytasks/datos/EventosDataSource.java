package com.uniovi.mytasks.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.uniovi.mytasks.modelo.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventosDataSource {
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;
    /**
     * Columnas de la tabla
     */
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_TAREA,MyDBHelper.COLUMNA_TITULO_TAREA,
            MyDBHelper.COLUMNA_DESCRIPCION_TAREA,MyDBHelper.COLUMNA_FECHA_TAREA};

    public EventosDataSource(Context context) {
        //el último parámetro es la versión
        dbHelper = new MyDBHelper(context, null, null, 1);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createevento(Task taskToInsert) {
        this.open();
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_TAREA,taskToInsert.getId());
        values.put(MyDBHelper.COLUMNA_TITULO_TAREA,taskToInsert.getTitulo());
        values.put(MyDBHelper.COLUMNA_DESCRIPCION_TAREA,taskToInsert.getDescripcion());
        values.put(MyDBHelper.COLUMNA_FECHA_TAREA,taskToInsert.getFecha().getTime());

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_TAREAS, null, values);
        this.close();
        return insertId;
    }

    public List<Task> getAllValorations() {
        this.open();
        // Lista que almacenara el resultado
        List<Task> taskList = new ArrayList<Task>();
        //hacemos una query porque queremos devolver un cursor

        Cursor cursor = database.query(MyDBHelper.TABLA_TAREAS, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Task task= new Task();
            cursor.getInt(0);

            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        this.close();
        return taskList;
    }
}
