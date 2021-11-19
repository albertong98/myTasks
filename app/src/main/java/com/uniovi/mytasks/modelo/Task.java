package com.uniovi.mytasks.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Task implements Parcelable {

    private String id;
    private String titulo;
    private String descripcion;
    private Date fecha;
    private String ubicacion;



    public Task(){}

    public Task(String titulo, String descripcion, Date fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Task(String id, String titulo, String descripcion, Date fecha) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Task(String titulo, String descripcion, Date fecha, String ubicacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
    }



    public Task(Parcel in) {
        this.id = in.readString();
        this.titulo = in.readString();
        this.descripcion = in.readString();
        this.fecha = new Date(in.readLong());
        this.ubicacion = in.readString();
    }

    public String getId(){ return id; }

    public void setId(String id){ this.id = id; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.titulo);
        dest.writeString(this.descripcion);
        dest.writeLong(this.fecha.getTime());
        dest.writeString(this.ubicacion);
    }

    @Override
    public String toString() {
        return "Task{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
