package com.uniovi.mytasks.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * MyDHelper
 */
public class MyDBHelper extends SQLiteOpenHelper {

    /**
     * Nombre y version de la base de datos
     */
    private static final String DATABASE_NAME = "myTasks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLA_TAREAS = "tabla_tareas";

    public static final String COLUMNA_ID_TAREA = "id_tarea";
    public static final String COLUMNA_EMAIL = "email_tarea";
    public static final String COLUMNA_TITULO_TAREA = "titulo_tarea";
    public static final String COLUMNA_DESCRIPCION_TAREA = "descripcion_tarea";
    public static final String COLUMNA_FECHA_TAREA = "fecha_tarea";
    public static final String COLUMNA_HORA_TAREA = "hora_tarea";
    public static final String COLUMNA_UBICACION = "ubicacion_tarea";

    public static final String CREATE_TABLA_TAREAS =
            "create table if not exists " + TABLA_TAREAS + " (" +
            COLUMNA_ID_TAREA + " text primary key," +
            COLUMNA_TITULO_TAREA + " text not null," +
            COLUMNA_DESCRIPCION_TAREA  + " text," +
            COLUMNA_FECHA_TAREA + " text not null," +
            COLUMNA_HORA_TAREA + " integer not null," +
            COLUMNA_UBICACION  + " text," +
                    COLUMNA_EMAIL  + " text not null" +
            ");";

    /**
     * Script para borrar la base de datos (SQL)
     */
    public static final String DATABASE_DROP_TAREAS = "DROP TABLE IF EXISTS " + TABLA_TAREAS;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //invocamos execSQL pq no devuelve ningún tipo de dataset

        db.execSQL(CREATE_TABLA_TAREAS);

        Log.i("ONCREATE", "EJECUTO CREACION");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_TAREAS);
        this.onCreate(db);

    }
}