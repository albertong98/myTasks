package com.uniovi.mytasks.datos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.uniovi.mytasks.modelo.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            MyDBHelper.COLUMNA_DESCRIPCION_TAREA,MyDBHelper.COLUMNA_FECHA_TAREA};

    public TareasDataSource(Context context) {
        //el último parámetro es la versión
        dbHelper = new MyDBHelper(context, null, null, 1);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(database);
    }

    public void close() {
        dbHelper.close();
    }

    public long createtask(Task taskToInsert) {
        this.open();
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_TAREA,taskToInsert.getId());
        values.put(MyDBHelper.COLUMNA_TITULO_TAREA,taskToInsert.getTitulo());
        values.put(MyDBHelper.COLUMNA_DESCRIPCION_TAREA,taskToInsert.getDescripcion());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String sFecha = formatter.format(taskToInsert.getFecha());
        values.put(MyDBHelper.COLUMNA_FECHA_TAREA,sFecha);

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_TAREAS, null, values);
        this.close();
        return insertId;
    }

    public boolean deleteOneTask(Task taskToDelete){
        this.open();
        boolean b = database.delete(MyDBHelper.TABLA_TAREAS,MyDBHelper.COLUMNA_ID_TAREA +" = "+taskToDelete.getId(),null) == 1;
        this.close();
        return b;
    }

    public boolean deleteTasksForDay(Date date){
        this.open();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String sFecha = formatter.format(date);
        boolean b = database.delete(MyDBHelper.TABLA_TAREAS,MyDBHelper.COLUMNA_FECHA_TAREA +" =?",new String[]{sFecha}) > 0;
        this.close();
        return b;
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
            task.setId(cursor.getInt(0));
            task.setTitulo(cursor.getString(1));
            task.setDescripcion(cursor.getString(2));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try{
                String s = cursor.getString(3);
                Date date = formatter.parse(s);
                task.setFecha(date);
            }catch (ParseException e){
                e.printStackTrace();
            }
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        this.close();
        return taskList;
    }
}
