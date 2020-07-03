package com.example.luisreyes.proyecto_aguas;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SYSTEM on 26/11/2019.
 */

public class Tabla_de_Codigos {

    public static HashMap<String,String> mapaTiposDeEmplazamiento;
    public static HashMap<String,String> mapaTiposDeRestoEmplazamiento;
    public static HashMap<String,String> mapaTiposDeMarca;
    public static HashMap<String,String> mapaTiposDeClase;
    public static HashMap<String,String> mapaTiposDeTipoRadio;

    public static HashMap<String,String> emptyMap;

    public static ArrayList<String> lista_tipo_radio;
    public static ArrayList<String> lista_tipo_fluido;

    public Tabla_de_Codigos(){

        lista_tipo_radio= new ArrayList<>();
        lista_tipo_radio.add("NINGUNO");
        lista_tipo_radio.add("R3");
        lista_tipo_radio.add("R4");
        lista_tipo_radio.add("W4");

        lista_tipo_fluido= new ArrayList<>();
        lista_tipo_fluido.add("NINGUNO");
        lista_tipo_fluido.add("FRIA");
        lista_tipo_fluido.add("CALIENTE");
        lista_tipo_fluido.add("ENERGIA");
        lista_tipo_fluido.add("ELECTRONICO");
        lista_tipo_fluido.add("MBUS");
        lista_tipo_fluido.add("GAS");
        lista_tipo_fluido.add("FRIGORIAS");
        lista_tipo_fluido.add("GENERAL");

        mapaTiposDeEmplazamiento = new HashMap<>();
        mapaTiposDeEmplazamiento.put("AC", "Acera");
        mapaTiposDeEmplazamiento.put("AR", "Arqueta");
        mapaTiposDeEmplazamiento.put("BA", "Batería Cuarto de Contadores");
        mapaTiposDeEmplazamiento.put("BT", "Batería Escalera");
        mapaTiposDeEmplazamiento.put("CA", "Caldera");
        mapaTiposDeEmplazamiento.put("CB", "Combinado");
        mapaTiposDeEmplazamiento.put("CO", "Cocina");
        mapaTiposDeEmplazamiento.put("ES", "Escalera");
        mapaTiposDeEmplazamiento.put("FA", "Fachada");
        mapaTiposDeEmplazamiento.put("FR", "Fregadera");
        mapaTiposDeEmplazamiento.put("GA", "Garaje");
        mapaTiposDeEmplazamiento.put("KA", "Calle");
        mapaTiposDeEmplazamiento.put("LO", "Local");
        mapaTiposDeEmplazamiento.put("NI", "Nicho");
        mapaTiposDeEmplazamiento.put("PA", "Patio");
        mapaTiposDeEmplazamiento.put("SO", "Sotano");
        mapaTiposDeEmplazamiento.put("TE", "Terraza");
        mapaTiposDeEmplazamiento.put("TR", "Trastero");
        mapaTiposDeEmplazamiento.put("VE", "Ventana");
        mapaTiposDeEmplazamiento.put("WC", "W.C.");
        mapaTiposDeEmplazamiento.put("CS", "Caseta");
        mapaTiposDeEmplazamiento.put("CU", "Cuadra");
        mapaTiposDeEmplazamiento.put("HA", "Hall");
        mapaTiposDeEmplazamiento.put("TX", "Txoko");
        mapaTiposDeEmplazamiento.put("PR", "Pared");

        mapaTiposDeRestoEmplazamiento = new HashMap<>();
        mapaTiposDeRestoEmplazamiento.put("AC", "ERA");
        mapaTiposDeRestoEmplazamiento.put("AR", "QUETA");
        mapaTiposDeRestoEmplazamiento.put("BA", "Batería Cuarto de Contadores");
        mapaTiposDeRestoEmplazamiento.put("BT", "Batería Escalera"); //preguntar por este
        mapaTiposDeRestoEmplazamiento.put("CA", "LDERA");
        mapaTiposDeRestoEmplazamiento.put("CB", "INADO");
        mapaTiposDeRestoEmplazamiento.put("CO", "CINA");
        mapaTiposDeRestoEmplazamiento.put("ES", "CALERA");
        mapaTiposDeRestoEmplazamiento.put("FA", "CHADA");
        mapaTiposDeRestoEmplazamiento.put("FR", "EGADERA");
        mapaTiposDeRestoEmplazamiento.put("GA", "RAJE");
        mapaTiposDeRestoEmplazamiento.put("KA", "LLE");
        mapaTiposDeRestoEmplazamiento.put("LO", "CAL");
        mapaTiposDeRestoEmplazamiento.put("NI", "CHO");
        mapaTiposDeRestoEmplazamiento.put("PA", "TIO");
        mapaTiposDeRestoEmplazamiento.put("SO", "TANO");
        mapaTiposDeRestoEmplazamiento.put("TE", "RRAZA");
        mapaTiposDeRestoEmplazamiento.put("TR", "ASTERO");
        mapaTiposDeRestoEmplazamiento.put("VE", "NTANA");
        mapaTiposDeRestoEmplazamiento.put("WC", "W.C.");
        mapaTiposDeRestoEmplazamiento.put("CS", "SETA");
        mapaTiposDeRestoEmplazamiento.put("CU", "ADRA");
        mapaTiposDeRestoEmplazamiento.put("HA", "LL");
        mapaTiposDeRestoEmplazamiento.put("TX", "OKO");
        mapaTiposDeRestoEmplazamiento.put("PR", "ED");

        mapaTiposDeMarca = new HashMap<>();
        mapaTiposDeMarca.put("ELSTER - HELIX 4000", "001");
        mapaTiposDeMarca.put("ELSTER - DELAUNET C", "002");
        mapaTiposDeMarca.put("ELSTER - ZENIT S200", "003");
        mapaTiposDeMarca.put("ITRON - FLOSTAR M", "004"); //preguntar por este
        mapaTiposDeMarca.put("ITRON - MSD CYBLE", "005");
        mapaTiposDeMarca.put("ELSTER - COSMOS WPW", "006");
        mapaTiposDeMarca.put("SAPPEL - AQUILA V3 V4", "007");
        mapaTiposDeMarca.put("SAPPEL - ALTAIR V3 V4", "008");
        mapaTiposDeMarca.put("ITRON - MSD NO CYBLE", "009");
        mapaTiposDeMarca.put("ELSTER - S130", "010"); //preguntar por este
        mapaTiposDeMarca.put("ITRON - TR", "011");
        mapaTiposDeMarca.put("ITRON - MEDIS CYBLE", "012");
        mapaTiposDeMarca.put("ITRON - WOLTMANN", "013");
        mapaTiposDeMarca.put("ITRON - WOLTEX", "014");
        mapaTiposDeMarca.put("ITRON - CTWV", "015");
        mapaTiposDeMarca.put("ITRON - COMBINADO", "016"); //preguntar por este
        mapaTiposDeMarca.put("ELSTER - DELAUNET EXTRA", "017");
        mapaTiposDeMarca.put("ELSTER - S200 NO REED", "018");
        mapaTiposDeMarca.put("ELSTER - S100", "019");
        mapaTiposDeMarca.put("ELSTER - S150", "020");
        mapaTiposDeMarca.put("ELSTER - WOLTMANN NO REED", "021");
        mapaTiposDeMarca.put("ITRON - MEDIS UNIMAG", "022");
        mapaTiposDeMarca.put("ITRON - TA.3F", "023");
        mapaTiposDeMarca.put("SENSUS - CONTAGUA-DN_13_1", "024"); //preguntar por este
        mapaTiposDeMarca.put("ITRON - FLODIS", "025");
        mapaTiposDeMarca.put("EBRO - EBRO", "026");
        mapaTiposDeMarca.put("ITRON - NARVAL", "027");
        mapaTiposDeMarca.put("BRUNATA - BRUNATA", "028");
        mapaTiposDeMarca.put("WEHRLE - WEHRLE", "029");
        mapaTiposDeMarca.put("JANZ - CONTHIDRA", "030"); //preguntar por este
        mapaTiposDeMarca.put("SISMA - SISMA", "031");
        mapaTiposDeMarca.put("LORENZ - LORENZ", "032");
        mapaTiposDeMarca.put("?", "033");
        mapaTiposDeMarca.put("SENSUS - MEISTREAM", "034");
        mapaTiposDeMarca.put("SIEMENS - MAG 8000", "035");
        mapaTiposDeMarca.put("SAPPEL - AQUARIUS", "036"); //preguntar por este
        mapaTiposDeMarca.put("ITRON - IRRIMAG", "037");
        mapaTiposDeMarca.put("ISTA - DOMAQUA", "038");
        mapaTiposDeMarca.put("ELSTER - S220 STANDARD", "039");
        mapaTiposDeMarca.put("ELSTER - Y250RI", "040");
        mapaTiposDeMarca.put("ELSTER - S220_100", "041");
        mapaTiposDeMarca.put("ITRON - AQUADIS PLUS", "042");
        mapaTiposDeMarca.put("ELSTER -  WAI R1000 - R12", "043");
        mapaTiposDeMarca.put("SENSUS - 620C", "044"); //preguntar por este
        mapaTiposDeMarca.put("POWOGAZ - JS CLASE C", "045");
        mapaTiposDeMarca.put("POWOGAZ - WI INCENDIOS", "046");
        mapaTiposDeMarca.put("ARAD - OCTAVE", "047");
        mapaTiposDeMarca.put("ELSTER - H5000", "048");
        mapaTiposDeMarca.put("ELSTER - Y290", "049");
        mapaTiposDeMarca.put("ELSTER - V200", "050"); //preguntar por este
        mapaTiposDeMarca.put("ELSTER - Y250MRI", "051");
        mapaTiposDeMarca.put("SAPPEL - WP-MFD", "052");
        mapaTiposDeMarca.put("ITRON - INTELLIS", "053");
        mapaTiposDeMarca.put("SAPPEL - HYDRUS", "054");
        mapaTiposDeMarca.put("ELSTER - CSJ_S2000", "055");
        mapaTiposDeMarca.put("ELSTER - R 1200", "056"); //preguntar por este
        mapaTiposDeMarca.put("ELSTER - Y290M", "057");
        mapaTiposDeMarca.put("ELSTER - M120", "058");
        mapaTiposDeMarca.put("SAPPEL - WP G_ESFERA ESTANDAR", "059");
        mapaTiposDeMarca.put("GENERICO - DN:13-15-20 MM", "060");
        mapaTiposDeMarca.put("GENERICO - DN:25-30-40 MM", "061");
        mapaTiposDeMarca.put("GENERICO - DN:50-65-80-100", "062");
        mapaTiposDeMarca.put("GENERICO - DN:125-150-200 M", "063");
        mapaTiposDeMarca.put("SV_RTK - CRONOS", "070"); //preguntar por este
        mapaTiposDeMarca.put("DIEHL - ALTAIR V3 V4", "071");

        mapaTiposDeClase= new HashMap<>();
        mapaTiposDeClase.put("B", "Fijo en Boca de Riego");
        mapaTiposDeClase.put("C", "Combinado");
        mapaTiposDeClase.put("M", "Chorro Múltiple");
        mapaTiposDeClase.put("R", "Volumetrico con Radio Emisor");
        mapaTiposDeClase.put("S", "Chorro Multiple con Radio Emisor");
        mapaTiposDeClase.put("T", "Chorro Único con Radio Emisor");
        mapaTiposDeClase.put("U", "Chorro Único");
        mapaTiposDeClase.put("V", "Volumétrico sin Radio Emisor");
        mapaTiposDeClase.put("W", "Woltman");

        emptyMap = new HashMap<>();

}

    public static String getResultadosPosiblesByAnomaly(String anomaly){
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String anomalia_en_json = jsonObject.getString(DBCausasController.codigo_causa_causas).trim();
                        if (anomalia_en_json.equals(anomaly)) {
                            String resultados = jsonObject.getString(DBCausasController.resultados_posibles_causas).trim();
                            return resultados;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getArealizarByAnomaly(String anomaly){
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String anomalia_en_json = jsonObject.getString(DBCausasController.codigo_causa_causas).trim();
                        if (anomalia_en_json.equals(anomaly)) {
                            String arealizar = jsonObject.getString(DBCausasController.arealizar_causas).trim();
                            return arealizar;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getIntervencionByAnomaly(String anomaly){
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String anomalia_en_json = jsonObject.getString(DBCausasController.codigo_causa_causas).trim();
                        if (anomalia_en_json.equals(anomaly)) {
                            String intervencion = jsonObject.getString(DBCausasController.causa_causas).trim();
                            return intervencion;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static ArrayList<String> getTipoByAccionOrdenada(String accion_ordenada){
        ArrayList<String> tipos_tareas = new ArrayList<>();
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String accion = jsonObject.getString(DBCausasController.accion_ordenada_causas).trim();
                        if (accion.equals(accion_ordenada)) {
                            String tipo_tarea = jsonObject.getString(DBCausasController.tipo_tarea_causas).trim();
                            if(!tipos_tareas.contains(tipo_tarea)){
                                tipos_tareas.add(tipo_tarea);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tipos_tareas;
    }
    public static String getTipoTareaByAnomaly(String anomaly){
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String anomalia_en_json = jsonObject.getString(DBCausasController.codigo_causa_causas).trim();
                        if (anomalia_en_json.equals(anomaly)) {
                            String tipo_tarea = jsonObject.getString(DBCausasController.tipo_tarea_causas).trim();
                            return tipo_tarea;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public static String getAccionOrdenadaByAnomaly(String anomaly){
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String anomalia_en_json = jsonObject.getString(DBCausasController.codigo_causa_causas).trim();
                        if (anomalia_en_json.equals(anomaly)) {
                            String accion_ordenada = jsonObject.getString(DBCausasController.accion_ordenada_causas).trim();
                            return accion_ordenada;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public static ArrayList<String> getAccionsOrdenadas(){
        ArrayList<String> acciones_odenadas = new ArrayList<>();
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String accion_ordenada = jsonObject.getString(DBCausasController.accion_ordenada_causas).trim();
                        if (Screen_Login_Activity.checkStringVariable(accion_ordenada) && !acciones_odenadas.contains(accion_ordenada)) {
                            acciones_odenadas.add(accion_ordenada);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return acciones_odenadas;
    }
    public static ArrayList<String> getAnomaliaseIntervencionesByAccionOrdenada(String accion_ordenada){
        ArrayList<String> anomalias_intervenciones = new ArrayList<>();
        if (team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas() > 0) {
            ArrayList<String> causas = new ArrayList<>();
            try {
                causas = team_or_personal_task_selection_screen_Activity.
                        dBcausasController.get_all_causas_from_Database();
                for (int i = 0; i < causas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(causas.get(i));
                        String accion = jsonObject.getString(DBCausasController.accion_ordenada_causas).trim();
                        if(accion.equals(accion_ordenada)) {
                            String anomalia = jsonObject.getString(DBCausasController.codigo_causa_causas).trim();
                            String intervencion = jsonObject.getString(DBCausasController.causa_causas).trim();
                            String anomalia_intervencion = anomalia + " - " + intervencion;
                            if (!anomalias_intervenciones.contains(anomalia_intervencion)) {
                                anomalias_intervenciones.add(anomalia_intervencion);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return anomalias_intervenciones;
    }

    public static String parse_tipo_tarea(String anomalia, String calibre, String marca)
    {
        if(!Screen_Login_Activity.checkStringVariable(calibre)){
            calibre = "?";
        }
        if(!Screen_Login_Activity.checkStringVariable(anomalia)){
            return "NCI " + calibre + "mm";
        }
        String tipo_t = getTipoTareaByAnomaly(anomalia).trim();
        calibre = calibre.trim();

        if(tipo_t == "NCI")
        {
            return "NCI " + calibre + "mm";
        }
        else if(tipo_t == "NCI + RC")
        {
            return "NCI " + calibre + "mm " + "+ RC";
        }
        else if(tipo_t == "LFTD")
        {
            return "LFTD " + calibre + "mm";
        }
        else if(tipo_t == "S.I.")
        {
            return "S.I. " + calibre + "mm";
        }
        else if(tipo_t == "U")
        {
            return "U " + calibre + "mm";
        }
        else if(tipo_t == "TD")
        {
            return "TD";
        }
        else if(tipo_t == "TD")
        {
            if(marca.toLowerCase().contains("Sappel")){
                return "TD";
            }
            return "TD/NCI " + calibre + "mm";
        }
        else if(tipo_t == "I+")
        {
            return "I+";
        }
        else if(tipo_t == "I+/NCI")
        {
            if(marca.toLowerCase().contains("sappel")){
                return "I+";
            }
            return "I+/NCI " + calibre + "mm";
        }
        else if(tipo_t == "LECT.R o RC")
        {
            return "LECT.R o RC";
        }
        else if(tipo_t == "RC")
        {
            return "RC";
        }
        else if(tipo_t == "RC NCI")
        {
            return "RC NCI " + calibre + "mm";
        }
        else if(tipo_t == "RCN")
        {
            return "RCN";
        }
        else if(tipo_t == "C. H. W4")
        {
            return "C. H. W4";
        }
        else if(tipo_t == "T"){
            String pre;
            if(calibre == "15")
                pre = "T3/4\" ";

            else if(calibre == "13")
                pre =  "T7/8\" ";

            else if(calibre == "20")
                pre =  "T1\" ";

            else if(calibre == "25")
                pre =  "T11/4\" ";

            else if(calibre == "30")
                pre =  "T11/2\" ";

            else if(calibre == "40")
                pre =  "T2\" ";

            else{
                pre =  "TBDN ";
            }
            return pre + calibre + "mm";
        }
        else if(tipo_t == "T + NCI"){
            String pre;
            if(calibre == "15")
                pre = "T3/4\" ";

            else if(calibre == "13")
                pre =  "T7/8\" ";

            else if(calibre == "20")
                pre =  "T1\" ";

            else if(calibre == "25")
                pre =  "T11/4\" ";

            else if(calibre == "30")
                pre =  "T11/2\" ";

            else if(calibre == "40")
                pre =  "T2\" ";

            else{
                pre =  "TBDN ";
            }
            return pre + "+ NCI " + calibre + "mm";
        }
        return tipo_t + " " + calibre + "mm";
    }
}
