package com.example.lab5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.Entity.Actividad;

import java.util.ArrayList;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ActividadViewHolder> {

    private ArrayList<Actividad> listarActividades;
    private Context contenido;


    public Context getContenido() {
        return contenido;
    }

    public void setContenido(Context contenido) {
        this.contenido = contenido;
    }

    public ArrayList<Actividad> getListarActividades() {
        return listarActividades;
    }

    public void setListarActividades(ArrayList<Actividad> listarActividades) {
        this.listarActividades = listarActividades;
    }

    public class ActividadViewHolder extends RecyclerView.ViewHolder{
        Actividad actividad;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contenido).inflate(R.layout.item_rv,parent,false);
        return new ActividadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad acti = listarActividades.get(position);
        holder.actividad=acti;
        TextView textViewTitulo=holder.itemView.findViewById(R.id.Titulo);
        TextView textViewFechaInicio=holder.itemView.findViewById(R.id.FechaInicio);
        //TextView textViewFechaFin=holder.itemView.findViewById(R.id.FechaFin);
        TextView textViewHoraInicio=holder.itemView.findViewById(R.id.HoraInicio);
        TextView textViewHoraFin=holder.itemView.findViewById(R.id.HoraFin);

        textViewTitulo.setText(acti.getTitulo());
        //textViewFechaFin.setText(acti.getFecha());
        textViewFechaInicio.setText(acti.getFecha());
        textViewHoraInicio.setText(acti.getHoraInicio());
        textViewHoraFin.setText(acti.getHoraFin());
    }

    @Override
    public int getItemCount() {
        return listarActividades.size();
    }
}
