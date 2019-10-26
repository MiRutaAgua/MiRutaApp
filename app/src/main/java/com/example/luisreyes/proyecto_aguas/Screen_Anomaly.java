package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrador on 27/10/2019.
 */

public class Screen_Anomaly extends AppCompatActivity{


    private Spinner spinner_anomaly;
    private HashMap<String,String> mapaTiposDeAnomalias;
    private HashMap<String,String> mapaAnomaliasNCI;
    private HashMap<String,String> mapaAnomaliasLFTD;
    private HashMap<String,String> mapaAnomaliasTD;
    private HashMap<String,String> mapaAnomaliasU;
    private HashMap<String,String> mapaAnomaliasSI;
    private HashMap<String,String> mapaAnomaliasT;
    private HashMap<String,String> mapaAnomaliasCF;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_absent);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        mapaTiposDeAnomalias = new HashMap<>();
        mapaTiposDeAnomalias.put("", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeAnomalias.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeAnomalias.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeAnomalias.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeAnomalias.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeAnomalias.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
        mapaTiposDeAnomalias.put("D", "DATOS");
        mapaTiposDeAnomalias.put("TD", "TOMA DE DATOS");
        mapaTiposDeAnomalias.put("I", "INSPECCIÓN");
        mapaTiposDeAnomalias.put("CF", "COMPROBAR EMISOR");
        mapaTiposDeAnomalias.put("EL", "EMISOR LECTURA");
        mapaTiposDeAnomalias.put("SI", "SOLO INSTALAR");
        mapaTiposDeAnomalias.put("R", "REFORMA MAS CONTADOR");

        mapaAnomaliasNCI = new HashMap<>();
        mapaAnomaliasNCI.put("001", "CONTADOR DESTRUIDO");
        mapaAnomaliasNCI.put("002", "CUENTA AL REVES");
        mapaAnomaliasNCI.put("004", "AGUJAS SUELTAS O ROTAS");
        mapaAnomaliasNCI.put("005", "CRISTAL ROTO");
        mapaAnomaliasNCI.put("008", "NO FUNCIONA CORRECTAMENTE");
        mapaAnomaliasNCI.put("009", "FUGA EN EL CONTADOR");
        mapaAnomaliasNCI.put("021", "VERIFICACION CONTADOR CONSORCIO");
        mapaAnomaliasNCI.put("024", "NO DISPONE DE CONTADOR");
        mapaAnomaliasNCI.put("025", "MAS DE N AÑOS Y METROS 3 CONTADOS");
        mapaAnomaliasNCI.put("026", "MAS DE N AÑOS");
        mapaAnomaliasNCI.put("034", "CONTADOR PRECINTADO");
        mapaAnomaliasNCI.put("A32", "TOMA DE DATOS");
        mapaAnomaliasNCI.put("A33", "ALTA CONTADOR NUEVO");
        mapaAnomaliasNCI.put("C33", "ALTA LEGALIZACION");
        mapaAnomaliasNCI.put("CVF", "CONTADOR A VERIFICAR");
        mapaAnomaliasNCI.put("APC", "ANALISIS DE PARQUE DE CONTADORES");
        mapaAnomaliasNCI.put("NZ2", "SUSTITUIR CONTADOR EMISOR");
        mapaAnomaliasNCI.put("Z21", "CAMBIOS MASIVOS");

        mapaAnomaliasLFTD = new HashMap<>();
        mapaAnomaliasLFTD.put("023", "REPARACION URGENTE");

        mapaAnomaliasTD= new HashMap<>();
        mapaAnomaliasTD.put("027", "CORRECCION DE DATOS DE CONTADOR");
        mapaAnomaliasTD.put("A32", "ALTA CONTADOR INSTALADO");
        mapaAnomaliasTD.put("C32", "LEGALIZACION CONTADOR INSTALADO");
        mapaAnomaliasTD.put("Z21", "CAMBIOS MASIVOS");

        mapaAnomaliasU = new HashMap<>();
        mapaAnomaliasU.put("A31", "ALTA CONTADOR DE BAJA");

        mapaAnomaliasSI = new HashMap<>();
        mapaAnomaliasSI.put("010", "SALIDERO EN LO RACORES");
        mapaAnomaliasSI.put("023", "REPARACION URGENTE");
        mapaAnomaliasSI.put("A30", "ALTA SOLO INSTALAR");

        mapaAnomaliasT = new HashMap<>();
        mapaAnomaliasT.put("035", "BAJA PROVISIONAL DE OFICIO");
        mapaAnomaliasT.put("036", "BAJA DEFINITIVA");
        mapaAnomaliasT.put("037", "BAJA ADMINISTRATIVA DE OFICIO");
        mapaAnomaliasT.put("038", "BAJA ADMINISTRATIVA DE CONTADOR DEL ABONADO");
        mapaAnomaliasT.put("039", "BAJA ADMINISTRATIVA DE CONTADOR DEL CONSORCIO");

        mapaAnomaliasCF = new HashMap<>();
        mapaAnomaliasCF.put("X12", "ALARMA DE LA RADIO");
        mapaAnomaliasCF.put("X13", "EMISOR NO LEIDO (LEER A DISTANCIA)");
        mapaAnomaliasCF.put("X14", "EMISOR NO LEIDO (LEER DIRECTAMENTE AL CONTADOR)");
        mapaAnomaliasCF.put("X23", "EMISOR NO FUNCIONA");

        spinner_anomaly = (Spinner)findViewById(R.id.spinner_anomalias_screen_anomaly);

        ArrayList<String> lista_tipos_anomalia = new ArrayList<>();


    }
}
