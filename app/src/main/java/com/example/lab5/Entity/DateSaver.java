package com.example.lab5.Entity;

import android.util.Log;

public class DateSaver {



    String titulo;
    String fecha;
    String inicio;
    String fin;
    int inicioMinutos;
    int finMinutos;

    public DateSaver(String titulo,String fecha, String inicio, String fin) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.inicio = inicio;
        this.fin = fin;
        this.inicioMinutos = minutesInDay(inicio);
        this.finMinutos = minutesInDay(this.fin);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public static int minutesInDay(String horaFormato){
        int hora =  Integer.parseInt(horaFormato.substring(0,2));
        if( horaFormato.endsWith("PM")){
            hora += 12;
        }
        return hora*60+ Integer.parseInt(horaFormato.substring(3,5));
    }



    public boolean conflict(String inicio, String fin){

        int inicioMinutos = minutesInDay(inicio);
        int finMinutos = minutesInDay(fin);

        if((inicioMinutos>=this.inicioMinutos && inicioMinutos<this.finMinutos) ||
                (finMinutos>this.inicioMinutos && finMinutos<=this.finMinutos) ||
                (inicioMinutos<=this.inicioMinutos && finMinutos>=this.finMinutos)){
            return true;
        }
        return false;
    }

}
