package com.example.luisreyes.proyecto_aguas;

import java.util.Date;

/**
 * Created by luis.reyes on 16/09/2019.
 */

public class MyCounter implements Comparable<MyCounter> {

    private Date dateTime;
    private String contador;
    private String direccion;
    private String cita;
    private String abonado;
    private String numero_serie_contador;
    private String anno_contador;
    private String calibre;
    private String telefono1;
    private String telefono2;
    private String fecha_cita;

    public String getCita() {
        return cita;
    }
    public void setCita(String cita) {
        this.cita = cita;
    }
    public String getAbonado() {
        return abonado;
    }
    public void setAbonado(String abonado) {
        this.abonado = abonado;
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
        this.anno_contador = anno_contador;
    }
    public String getCalibre() {
        return calibre;
    }
    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }
    public String getTelefono1() {
        return telefono1;
    }
    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }
    public String getTelefono2() {
        return telefono2;
    }
    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getFecha_cita() {
        return fecha_cita;
    }

    public void setFecha_cita(String fecha_cita) {
        this.fecha_cita = fecha_cita;
    }
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContador() {
        return contador;
    }

    public void setContador(String counter) {
        this.contador = counter;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }

    @Override
    public int compareTo(MyCounter o) {
        if (getDateTime() == null || o.getDateTime() == null)
            return 0;
        return getDateTime().compareTo(o.getDateTime());
    }
}
