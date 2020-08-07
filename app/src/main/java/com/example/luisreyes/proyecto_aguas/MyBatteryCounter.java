package com.example.luisreyes.proyecto_aguas;


/**
 * Created by luis.reyes on 11/12/2019.
 */

public class MyBatteryCounter implements Comparable<MyBatteryCounter>  {

    private String contador;
    private String direccion;
    private String tipo_tarea;
    private String principal_variable = null;
    private String abonado;
    private String numero_serie_contador;
    private String anno_contador;
    private String calibre;
    private String telefono1;
    private String telefono2;
    private String numero_abonado;
    private String ubicacion_bateria;

    public String getTipo_tarea() {
        return tipo_tarea;
    }

    public void setTipo_tarea(String tipo_tarea) {
        if(!tipo_tarea.contains("NULL") && !tipo_tarea.contains("null")) {
            this.tipo_tarea = tipo_tarea;
        }else{
            this.tipo_tarea = "";
        }
    }

    public String getPrincipal_variable() {
        return principal_variable;
    }
    public void setPrincipal_variable(String principal_variable) {
        if(!principal_variable.isEmpty())
            this.principal_variable = principal_variable;
    }

    public String getNumero_abonado() {
        return numero_abonado;
    }
    public void setNumero_abonado(String numero_abonado) {
        this.numero_abonado = numero_abonado;
    }

    public String getAbonado() {
        return abonado;
    }
    public void setAbonado(String abonado) {
        this.abonado = abonado.replace("null", "");
    }

    public String getNumero_serie_contador() {
        return numero_serie_contador;
    }
    public void setNumero_serie_contador(String numero_serie_contador) {
        this.numero_serie_contador = numero_serie_contador;
    }

    public String getAnno_contador() {
        return anno_contador;
    }
    public void setAnno_contador(String anno_contador) {
        this.anno_contador = anno_contador.replace("null", "");
    }

    public String getCalibre() {
        return calibre;
    }
    public void setCalibre(String calibre) {
        if(!calibre.contains("NULL") && !calibre.contains("null")) {
            this.calibre = calibre.trim()+ "mm";
            if(tipo_tarea.replace("NULL", "").replace("null", "")
                    .trim().isEmpty()){
                tipo_tarea = "NCI";
            }
        }else{
            this.calibre = "-";
            if(tipo_tarea.replace("NULL", "").replace("null", "")
                    .trim().isEmpty()){
                tipo_tarea = "-";
            }
        }
    }

    public String getTelefono1() {
        return telefono1;
    }
    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1.replace("null", "");
    }

    public String getTelefono2() {
        return telefono2;
    }
    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2.replace("null", "");
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion.replace("null", "");
    }

    public String getContador() {
        return contador;
    }
    public void setContador(String counter) {
        this.contador = counter;
    }

    public String getUbicacion_bateria() {
        String previo;
        if(!Screen_Login_Activity.checkStringVariable(ubicacion_bateria) || !ubicacion_bateria.contains("-")){
            previo="BA-?";
            return previo;
        }
        if(!ubicacion_bateria.contains("BA") && !ubicacion_bateria.contains("BT")){
            return "BA"+ubicacion_bateria;
        }
        return ubicacion_bateria;
    }
    public void setUbicacion_bateria(String ubicacion) {
        this.ubicacion_bateria = ubicacion.trim();
    }

    @Override
    public int compareTo(MyBatteryCounter o) {
        if (getUbicacion_bateria() == null || o.getUbicacion_bateria() == null)
            return 0;
        return getUbicacion_bateria().compareTo(o.getUbicacion_bateria());
    }
}
