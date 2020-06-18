package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Menu_Options extends AppCompatDialogFragment{

    private AlertDialog.Builder builder;

    private Button info_tarea;
    private Button tabla_tareas;
    private Button pant_principal;
    private Button savework, loadwork;
    private Button ayuda;

    Context context;
    FragmentManager fragmentManager;

    @Override
    public void onDestroyView() {
//        showing = false;
        Log.e("onDestroyView", "showing = false;");
        super.onDestroyView();
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_menu_options, null);
        builder.setView(view)
                .setTitle("Menu")
                .setCancelable(false)
//                .setIcon(getResources().getDrawable(R.drawable.ic_info_blue_black_24dp))
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        showing = false;
                    }
                });
        info_tarea = view.findViewById(R.id.button_tarea_info_layout_dialog);
        tabla_tareas = view.findViewById(R.id.button_tareas_layout_dialog);
        pant_principal = view.findViewById(R.id.button_pant_principal_layout_dialog);
        savework = view.findViewById(R.id.button_savework_layout_dialog);
        ayuda = view.findViewById(R.id.button_ayuda_layout_dialog);
        loadwork = view.findViewById(R.id.button_loadwork_layout_dialog);

        tabla_tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team_or_personal_task_selection_screen_Activity.from_team_or_personal =
                        team_or_personal_task_selection_screen_Activity.FROM_TEAM;
                Intent open_screen= new Intent(context, Screen_Table_Team.class);
                startActivity(open_screen);
                onDestroyView();
            }
        });
        pant_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_screen_team_or_task= new Intent(context, team_or_personal_task_selection_screen_Activity.class);
                startActivity(open_screen_team_or_task);
                onDestroyView();
            }
        });
        savework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(context);
                final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(Screen_Login_Activity.this,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent open_screen_team_or_task= new Intent(context, team_or_personal_task_selection_screen_Activity.class);
                        team_or_personal_task_selection_screen_Activity.salvar_trabajo = true;
                        startActivity(open_screen_team_or_task);
                        onDestroyView();
                    }
                });
                savework.startAnimation(myAnim);

            }
        });
        loadwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(context);
                final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(Screen_Login_Activity.this,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent open_screen_team_or_task= new Intent(context, team_or_personal_task_selection_screen_Activity.class);
                        team_or_personal_task_selection_screen_Activity.cargar_trabajo = true;
                        startActivity(open_screen_team_or_task);
                        onDestroyView();
                    }
                });
                loadwork.startAnimation(myAnim);
            }
        });
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessage("Contactar",
                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
                        "\n   Michel Morales: mraguas@gmail.com"
                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
                onDestroyView();
            }
        });
        info_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
                onDestroyView();
            }
        });
//        textView_mensaje.setHint(hint);
        return  builder.create();
    }

    public static boolean loadWorkinFile(String dirToLoad){
        String loadWorkDirString = dirToLoad;
        File loadWorkFile = new File(loadWorkDirString);
        FileInputStream fis = null;
        if(loadWorkFile.exists()){
            Log.e("Existe", "loadWorkFile");
            try {
                Log.e("Opening", "...");
                fis = new FileInputStream(loadWorkFile);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("IOException Opening", e.toString());
                return false;
            }
            try {
                if(fis != null) {
                    String toRead = "";
                    if (fis != null) {
                        String prueba = "prueba";
                        Log.e("Reading", "...");
                        byte[] b = new byte[fis.available()];
                        fis.read(b);
                        toRead = new String(b, "UTF-8");
                        try {
                            JSONArray jsonArray = new JSONArray(toRead);
//                    Log.e("jsonArray", jsonArray.get(0).toString());
                            if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = fixJsonObjectFormat(jsonArray.getJSONObject(i));
                                        insertOrUpdateTareas(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOException Reading", e.toString());
                return false;
            }
            try {
                if(fis != null) {
                    Log.e("Closing", "...");
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOException Closing", e.toString());
                return false;
            }
        }
        else{
            return false;
        }
        return true;
    }
    public static JSONObject fixJsonObjectFormat(JSONObject jsonObjectoFix) throws JSONException {
        JSONObject jsonObject =new JSONObject();
        jsonObject = DBtareasController.setEmptyJSON(jsonObject);
        Iterator it = jsonObject.keys();
        while (it.hasNext()) {
            String key = (String)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            String value = "";
            try {
                value = jsonObjectoFix.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonObject.put(key, value);
        }
        return  jsonObject;
    }
    public static void insertOrUpdateTareas(JSONObject jsonObjectInFile) throws JSONException {
        if(!Screen_Filter_Tareas.checkIfIsDone(jsonObjectInFile)) {
            String numeroInternoFile = jsonObjectInFile.
                    getString(DBtareasController.principal_variable);
            if (team_or_personal_task_selection_screen_Activity.
                    dBtareasController.checkIfTareaExists(numeroInternoFile)) {

                JSONObject jsonObject_local = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(numeroInternoFile));

                if(compareTwoTareasByDate(jsonObject_local, jsonObjectInFile)){
                    team_or_personal_task_selection_screen_Activity.
                            dBtareasController.updateTarea(jsonObjectInFile,
                            DBtareasController.principal_variable);
                    Log.e("insertOrUpdateTareas", "updateTarea");
                }else {
                    Log.e("insertOrUpdateTareas", "no updateTarea");
                }
            } else {
                team_or_personal_task_selection_screen_Activity.
                        dBtareasController.insertTarea(jsonObjectInFile);
                Log.e("insertOrUpdateTareas", "insertTarea");
            }
        }
    }
    //Retorna true si la tarea nueva es mas actual (fecha de modificacion mas reciente)
    public static boolean compareTwoTareasByDate(JSONObject jsonObject_old, JSONObject jsonObject_new) throws JSONException {
        Date date_old = DBtareasController.getFechaHoraFromString(
                jsonObject_old.getString(DBtareasController.date_time_modified));
        Date date_new = DBtareasController.getFechaHoraFromString(
                jsonObject_new.getString(DBtareasController.date_time_modified));
        if(date_new.after(date_old)){
            Log.e("compareTwoTareasByDate", "true");
            return true;
        }else {
            Log.e("compareTwoTareasByDate", "false");
            return false;
        }
    }
    public static String writeWorkinFile(Context context, String file_stringData) throws JSONException {
        String saveWorkNameFile = "Trabajo_a_Cargar"+ DBtareasController.getStringFromFechaHora(new Date())
                .replace(" ","_").replace(":","_").replace("-","_");
        String savedWorkDirString = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                +"/Trabajo a Cargar/"+saveWorkNameFile+".txt";
        File saveWorkFile = new File(savedWorkDirString);
        FileOutputStream fos = null;
        try {
            Log.e("Creating", "...");
            fos = new FileOutputStream(saveWorkFile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IOException Creating", e.toString());
        }
        if (fos != null) {
            try {
                fos.write(file_stringData.getBytes());
                Log.e("Writing", "...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Log.e("Closing", "...");
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOException Closing", e.toString());
            }
        }
        return savedWorkDirString;
    }
    public static String savedWorkinFile(Context context){
        String saveWorkNameFile = "Trabajo_Salvado_"+ DBtareasController.getStringFromFechaHora(new Date())
                .replace(" ","_").replace(":","_").replace("-","_");
        String savedWorkDirString = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/Trabajo Salvado/"+saveWorkNameFile+".txt";
        File saveWorkFile = new File(savedWorkDirString);
        FileOutputStream fos = null;
        try {
            Log.e("Creating", "...");
            fos = new FileOutputStream(saveWorkFile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IOException Creating", e.toString());
        }
        try {
            String toWrite="[";
            if(fos != null) {
                String prueba = "prueba";
                Log.e("Writing", "...");
                if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                        ArrayList<String> tareas = new ArrayList<>();
                        try {
                            tareas = team_or_personal_task_selection_screen_Activity.
                                    dBtareasController.get_all_tareas_from_Database();
                            for (int i = 0; i < tareas.size(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(tareas.get(i));
                                    toWrite += jsonObject.toString() + ",";
                                } catch (JSONException e) {
                                    Log.e("JSONException", "Error en Elemento i = " + String.valueOf(i));
                                    e.printStackTrace();
                                }
                            }
                            toWrite = toWrite.substring(0, toWrite.length()-1); //quitando la ultima coma (,)
                            toWrite += "]";
                            fos.write(toWrite.getBytes());
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException Writing", e.toString());
        }
        try {
            Log.e("Closing", "...");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOException Closing", e.toString());
        }
        return savedWorkDirString;
    }

    public void setContent(Context ctx){
        context = ctx;
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(Menu_Options.this.getFragmentManager(), title);
    }

//    @Override
//    public void onTaskComplete(String type, String result) throws JSONException {
////        if(type == "save_work") {
////            if (result == null) {
////                Log.e("result", "nulo");
////            }
////            else if(result.isEmpty()){
////                Log.e("result", "vacio");
////            }
////            else if (result.equals("upload not success")) {
////                Log.e("result", "upload not success");
////            }else{
////                Log.e("result", "upload success");
////            }
////        }else{
////            Log.e("type", type);
////        }
//    }
}
