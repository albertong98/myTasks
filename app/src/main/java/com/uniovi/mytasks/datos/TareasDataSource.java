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
import java.util.UUID;

public class TareasDataSource {
    private SQLiteDatabase database;
    /**
     * Referencia al helper que se encarga de crear y actualizar la base de datos.
     */
    private MyDBHelper dbHelper;
    /**
     * Columnas de la tabla
     */
    private final String[] allColumns = {MyDBHelper.COLUMNA_ID_TAREA,MyDBHelper.COLUMNA_TITULO_TAREA,
            MyDBHelper.COLUMNA_DESCRIPCION_TAREA,MyDBHelper.COLUMNA_FECHA_TAREA, MyDBHelper.COLUMNA_UBICACION, MyDBHelper.COLUMNA_EMAIL};

    public TareasDataSource(Context context) {
        //el último parámetro es la versión
        dbHelper = new MyDBHelper(context, null, null, 1);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createtask(Task taskToInsert) {
       this.open();
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_TAREA, UUID.randomUUID().toString());
        values.put(MyDBHelper.COLUMNA_TITULO_TAREA,taskToInsert.getTitulo());
        values.put(MyDBHelper.COLUMNA_DESCRIPCION_TAREA,taskToInsert.getDescripcion());
        values.put(MyDBHelper.COLUMNA_FECHA_TAREA,taskToInsert.getFecha().getTime());
        values.put(MyDBHelper.COLUMNA_HORA_TAREA,taskToInsert.getHora().getTime());
        values.put(MyDBHelper.COLUMNA_UBICACION,taskToInsert.getUbicacion());
        values.put(MyDBHelper.COLUMNA_EMAIL,taskToInsert.getEmail());

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
            task.setId(cursor.getString(0));
            task.setTitulo(cursor.getString(1));
            task.setDescripcion(cursor.getString(2));
            task.setFecha(new Date(cursor.getInt(3)));
            task.setUbicacion(cursor.getString(4));
            task.setEmail(cursor.getString(5));
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        this.close();
        return taskList;
    }

    public List<Task> getTasksByUser(String email) {
        this.open();
        List<Task> taskList = new ArrayList<Task>();

        Cursor cursor = database.rawQuery("Select * " +
                " FROM " + MyDBHelper.TABLA_TAREAS +
                " WHERE " + MyDBHelper.TABLA_TAREAS + "." + MyDBHelper.COLUMNA_EMAIL + " = \'" + email+"\'", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Task task= new Task();
            cursor.getInt(0);
            task.setId(cursor.getString(0));
            task.setTitulo(cursor.getString(1));
            task.setDescripcion(cursor.getString(2));
            task.setFecha(new Date(cursor.getInt(3)));
            task.setHora(new Date(cursor.getInt(4)));
            task.setUbicacion(cursor.getString(5));
            task.setEmail(cursor.getString(6));
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.

        return taskList;
    }

    public void deleteItem(String getID) {
        database.execSQL("DELETE from "+ MyDBHelper.TABLA_TAREAS +"  WHERE "+ MyDBHelper.COLUMNA_ID_TAREA+" = '" +getID+ "'");
    }
}
