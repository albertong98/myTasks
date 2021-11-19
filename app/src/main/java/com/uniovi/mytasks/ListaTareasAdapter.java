package com.uniovi.mytasks;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniovi.mytasks.modelo.Task;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListaTareasAdapter extends RecyclerView.Adapter<ListaTareasAdapter.TareasViewholder> {

    public ListaTareasAdapter(List<Task> listaTareas, OnItemClickListener listener) {
        this.listaTareas = listaTareas;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Task item);
    }

    private List<Task> listaTareas;

    private final OnItemClickListener listener;

    @NonNull
    @Override
    public ListaTareasAdapter.TareasViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_recycler_view_tarea, parent, false);
        return new TareasViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaTareasAdapter.TareasViewholder holder, int position) {
        Task tarea = listaTareas.get(position);
        holder.bindUser(tarea, listener);
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public static class TareasViewholder extends RecyclerView.ViewHolder {

        private TextView titulo;
        private TextView fecha;
        private Switch tareaFinalizada;

        public TareasViewholder(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            tareaFinalizada = (Switch) itemView.findViewById(R.id.tareaFinalizada);
        }

        public void bindUser(final Task tarea, final OnItemClickListener listener) {
            titulo.setText(tarea.getTitulo());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            fecha.setText(df.format(tarea.getFecha()));
            if (!(tarea.getUbicacion() == null)){
                tareaFinalizada.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(tarea);
                }
            });
        }
    }
}
