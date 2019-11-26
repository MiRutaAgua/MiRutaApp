package com.example.luisreyes.proyecto_aguas;

import java.util.HashMap;

/**
 * Created by SYSTEM on 26/11/2019.
 */

public class Tabla_de_Codigos {

    public static HashMap<String,String> mapaTiposDeResultados;
    public static HashMap<String,String> mapaTiposDeAnomalias;
    public static HashMap<String,String> mapaTiposDeEmplazamiento;
    public static HashMap<String,String> mapaTiposDeRestoEmplazamiento;
    public static HashMap<String,String> mapaTiposDeMarca;
    public static HashMap<String,String> mapaTiposDeClase;
    public static HashMap<String,String> mapaTiposDeTipoRadio;

    public static HashMap<String,String> mapaAnomaliasNCI;
    public static HashMap<String,String> mapaAnomaliasLFTD;
    public static HashMap<String,String> mapaAnomaliasTD;
    public static HashMap<String,String> mapaAnomaliasU;
    public static HashMap<String,String> mapaAnomaliasSI;
    public static HashMap<String,String> mapaAnomaliasT;
    public static HashMap<String,String> mapaAnomaliasCF;
    public static HashMap<String,String> mapaAnomaliasEL;
    public static HashMap<String,String> mapaAnomaliasI;
    public static HashMap<String,String> emptyMap;

    public Tabla_de_Codigos(){
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
        mapaTiposDeMarca.put("ELSTER-HELIX 4000", "001");
        mapaTiposDeMarca.put("ELSTER - DELAUNET C", "002");
        mapaTiposDeMarca.put("ELSTER - ZENIT S200", "003");
        mapaTiposDeMarca.put("ITRON - FLOSTAR M", "004"); //preguntar por este
        mapaTiposDeMarca.put("ITRON - MSD CYBLE", "005");
        mapaTiposDeMarca.put("ELSTER - COSMOS WPW", "006");
        mapaTiposDeMarca.put("SAPPEL -AQUILA V3 V4", "007");
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
        mapaTiposDeMarca.put("ITRON -TA.3F", "023");
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
        mapaTiposDeMarca.put("ARAD-OCTAVE", "047");
        mapaTiposDeMarca.put("ELSTER - H5000", "048");
        mapaTiposDeMarca.put("ELSTER - Y290", "049");
        mapaTiposDeMarca.put("ELSTER - V200", "050"); //preguntar por este
        mapaTiposDeMarca.put("ELSTER - Y250MRI", "051");
        mapaTiposDeMarca.put("SAPPEL - WP-MFD", "052");
        mapaTiposDeMarca.put("ITRON - INTELLIS", "053");
        mapaTiposDeMarca.put("SAPPEL - HYDRUS", "054");
        mapaTiposDeMarca.put("ELSTER - CSJ_S2000", "055");
        mapaTiposDeMarca.put("ELSTER - R 1200", "056"); //preguntar por este
        mapaTiposDeMarca.put("ELSTER-Y290M", "057");
        mapaTiposDeMarca.put("ELSTER-M120", "058");
        mapaTiposDeMarca.put("SAPPEL-WP G_ESFERA ESTANDAR", "059");
        mapaTiposDeMarca.put("GENERICO-DN:13-15-20 MM", "060");
        mapaTiposDeMarca.put("GENERICO-DN:25-30-40 MM", "061");
        mapaTiposDeMarca.put("GENERICO-DN:50-65-80-100", "062");
        mapaTiposDeMarca.put("GENERICO-DN:125-150-200 M", "063");
        mapaTiposDeMarca.put("SV_RTK-CRONOS", "070"); //preguntar por este

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

//        mapaTiposDeTipoRadio= new HashMap<>();
//        mapaTiposDeTipoRadio.put("027", "CORRECCION DE DATOS DE CONTADOR");
//        mapaTiposDeTipoRadio.put("A32", "ALTA CONTADOR INSTALADO");
//        mapaTiposDeTipoRadio.put("C32", "LEGALIZACION CONTADOR INSTALADO");
//        mapaTiposDeTipoRadio.put("Z21", "CAMBIOS MASIVOS");
//        mapaTiposDeTipoRadio.put("LHC", "LECTURA HISTORICA. WB-DB-RF");

    mapaTiposDeResultados = new HashMap<>();
        mapaTiposDeResultados.put("036", "012");
        mapaTiposDeResultados.put("037", "012");
        mapaTiposDeResultados.put("039", "012");
//        mapaTiposDeAnomalias.put("036", "11"); //preguntar por este  //añadir opciones a escoger
        mapaTiposDeResultados.put("A30", "002");
        mapaTiposDeResultados.put("A31", "002");
        mapaTiposDeResultados.put("A32", "002");
        mapaTiposDeResultados.put("A33", "002");
//        mapaTiposDeResultados.put("A32", "SR2"); //preguntar por este
        mapaTiposDeResultados.put("C32", "002");
        mapaTiposDeResultados.put("C33", "002");
        mapaTiposDeResultados.put("S01", "002");
        mapaTiposDeResultados.put("S02", "002");
        mapaTiposDeResultados.put("CVF", "002");
        mapaTiposDeResultados.put("021", "002");
        mapaTiposDeResultados.put("Z21", "002");
        mapaTiposDeResultados.put("NZ2", "002");
        mapaTiposDeResultados.put("NZ0", "002");
        mapaTiposDeResultados.put("023", "002");
        mapaTiposDeResultados.put("Z23", "002");
        mapaTiposDeResultados.put("APC", "002");
        mapaTiposDeResultados.put("X12", "996");
        mapaTiposDeResultados.put("X13", "990");
        mapaTiposDeResultados.put("X14", "990");
        mapaTiposDeResultados.put("X15", "996");
        mapaTiposDeResultados.put("NX2", "994");
        mapaTiposDeResultados.put("NX0", "994");
        mapaTiposDeResultados.put("LHC", "991");
        mapaTiposDeResultados.put("M21", "979-980-981-982");
        mapaTiposDeResultados.put("III", "DII");
        mapaTiposDeResultados.put("R30", "997");
        mapaTiposDeResultados.put("003", "903");

    mapaTiposDeAnomalias = new HashMap<>();
        mapaTiposDeAnomalias.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeAnomalias.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeAnomalias.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeAnomalias.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeAnomalias.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
    //mapaTiposDeAnomalias.put("D", "DATOS");
        mapaTiposDeAnomalias.put("TD", "TOMA DE DATOS");
        mapaTiposDeAnomalias.put("I", "INSPECCIÓN");
        mapaTiposDeAnomalias.put("CF", "COMPROBAR EMISOR");
        mapaTiposDeAnomalias.put("EL", "EMISOR LECTURA");
        mapaTiposDeAnomalias.put("SI", "SOLO INSTALAR");
    //mapaTiposDeAnomalias.put("R", "REFORMA MAS CONTADOR");

    emptyMap = new HashMap<>();

    mapaAnomaliasNCI = new HashMap<>();
        mapaAnomaliasNCI.put("S01", "SUMINISTRO POR CAMBIO DE CALIBRE");
        mapaAnomaliasNCI.put("S02", "SUMINISTRO POR IMPAGO DE CONTADOR");
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
        mapaAnomaliasNCI.put("NZ0", "CTD Y EMISORA RENOVADOS");
        mapaAnomaliasNCI.put("NZ2", "SUSTITUIR CONTADOR EMISOR");
        mapaAnomaliasNCI.put("Z21", "CAMBIOS MASIVOS");

    mapaAnomaliasLFTD = new HashMap<>();
        mapaAnomaliasLFTD.put("023", "REPARACION URGENTE");
        mapaAnomaliasLFTD.put("Z23", "REPARACION CTD Y EMISOR");

    mapaAnomaliasTD= new HashMap<>();
        mapaAnomaliasTD.put("027", "CORRECCION DE DATOS DE CONTADOR");
        mapaAnomaliasTD.put("A32", "ALTA CONTADOR INSTALADO");
        mapaAnomaliasTD.put("C32", "LEGALIZACION CONTADOR INSTALADO");
        mapaAnomaliasTD.put("Z21", "CAMBIOS MASIVOS");
        mapaAnomaliasTD.put("LHC", "LECTURA HISTORICA. WB-DB-RF");

    mapaAnomaliasU = new HashMap<>();
        mapaAnomaliasU.put("A31", "ALTA CONTADOR DE BAJA");

    mapaAnomaliasSI = new HashMap<>();
        mapaAnomaliasSI.put("010", "SALIDERO EN LO RACORES");
        mapaAnomaliasSI.put("023", "REPARACION URGENTE");
        mapaAnomaliasSI.put("A30", "ALTA SOLO INSTALAR");
        mapaAnomaliasSI.put("003", "INSTALADO AL REVÉS");
        mapaAnomaliasSI.put("R30", "REINSTALAR UBICACIÓN CORRECTA");

    mapaAnomaliasT = new HashMap<>();
        mapaAnomaliasT.put("035", "BAJA PROVISIONAL DE OFICIO");
        mapaAnomaliasT.put("036", "BAJA DEFINITIVA");
        mapaAnomaliasT.put("037", "BAJA ADMINISTRATIVA DE OFICIO");
        mapaAnomaliasT.put("038", "BAJA ADMINISTRATIVA DE CONTADOR DEL ABONADO");
        mapaAnomaliasT.put("039", "BAJA ADMINISTRATIVA DE CONTADOR DEL CONSORCIO");

    mapaAnomaliasCF = new HashMap<>();
        mapaAnomaliasCF.put("X12", "ALARMA DE LA RADIO");
        mapaAnomaliasCF.put("X15", "EMPAREJAR DATO RADIO Y MEC");
        mapaAnomaliasCF.put("NX2", "RENOVACIÓN DE EMISORA");
        mapaAnomaliasCF.put("NX0", "EMISORA RENOVADA. INFORMA CIA");

    mapaAnomaliasEL = new HashMap<>();
        mapaAnomaliasEL.put("X13", "RADIO NO RECIBIDA. VISITA P.");
        mapaAnomaliasEL.put("X14", "RADIO NO RECIBIDA. HAY LECT.");

    mapaAnomaliasI = new HashMap<>();
        mapaAnomaliasI.put("M21", "PREPARACIÓN RENOVACIÓN PERIÓDICA");
        mapaAnomaliasI.put("III", "INFORME INSTALACIÓN INTERIOR");
}

}
