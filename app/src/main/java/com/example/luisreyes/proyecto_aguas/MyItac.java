package com.example.luisreyes.proyecto_aguas;

import java.util.Date;

/**
 * Created by luis.reyes on 8/13/2020.
 */

public class MyItac implements Comparable<MyItac> {

    private String codigo_emplazamiento;
    private String direccion;
    private String geolocalizacion;
    private String acceso;
    private String descripcion;
    private String nombre_empresa;
    private String telefono_empresa;
    private String nombre_responsable;
    private String telefono_encargado;
    private String nombre_encargado;
    private String correo_encargado;

    public String getCodigo_emplazamiento() {
        return codigo_emplazamiento;
    }

    public void setCodigo_emplazamiento(String codigo_emplazamiento) {
        this.codigo_emplazamiento = codigo_emplazamiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGeolocalizacion() {
        return geolocalizacion;
    }

    public void setGeolocalizacion(String geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public String getTelefono_empresa() {
        return telefono_empresa;
    }

    public void setTelefono_empresa(String telefono_empresa) {
        this.telefono_empresa = telefono_empresa;
    }

    public String getNombre_responsable() {
        return nombre_responsable;
    }

    public void setNombre_responsable(String nombre_responsable) {
        this.nombre_responsable = nombre_responsable;
    }

    public String getTelefono_encargado() {
        return telefono_encargado;
    }

    public void setTelefono_encargado(String telefono_encargado) {
        this.telefono_encargado = telefono_encargado;
    }

    public String getNombre_encargado() {
        return nombre_encargado;
    }

    public void setNombre_encargado(String nombre_encargado) {
        this.nombre_encargado = nombre_encargado;
    }

    public String getCorreo_encargado() {
        return correo_encargado;
    }

    public void setCorreo_encargado(String correo_encargado) {
        this.correo_encargado = correo_encargado;
    }

    @Override
    public int compareTo(MyItac o) {
        if (getCodigo_emplazamiento() == null || o.getCodigo_emplazamiento() == null)
            return 0;
        return getCodigo_emplazamiento().compareTo(o.getCodigo_emplazamiento());
    }
}
