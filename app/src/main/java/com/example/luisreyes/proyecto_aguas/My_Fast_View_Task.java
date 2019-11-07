package com.example.luisreyes.proyecto_aguas;

/**
 * Created by Alejandro on 27/09/2019.
 */

public class My_Fast_View_Task implements Comparable<My_Fast_View_Task> {

    public String getCalibre() {
        return calibre;
    }
    public int getCalibreInt() {
        int res=0;
        res = Integer.parseInt(calibre);
        return res;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre.replace("\n","").trim();
        all_string += " "+ this.calibre + " mm";
    }
    public void setCalibreByInt(int calibre) {
        this.calibre = String.valueOf(calibre);
        all_string += " "+ this.calibre + " mm";
    }

    public String getTipo_tarea() {
        return tipo_tarea;
    }

    public void setTipo_tarea(String tipo_tarea) {
        this.tipo_tarea = tipo_tarea.replace("\n","").trim();
        all_string += tipo_tarea;
    }

    private String calibre="";
    private String tipo_tarea="";
    private String all_string="";

    public String getAll_string() {
        return all_string;
    }

    public void setAll_string(String all_string) {
        this.all_string = all_string;
    }

    public boolean compareToOther(My_Fast_View_Task other){
        if(other.getCalibre().equals(calibre) && other.getTipo_tarea().equals(tipo_tarea)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int compareTo(My_Fast_View_Task o) {
        if (getAll_string() == null || o.getAll_string() == null)
            return 0;
        return getAll_string().compareTo(o.getAll_string());
    }
}
