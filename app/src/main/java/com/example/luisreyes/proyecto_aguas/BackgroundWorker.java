package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alejandro on 19/08/2019.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    public static boolean server_online_or_wamp = true;  //true internet, false wamp

    Context context;

    AlertDialog alertDialog;

    JSONObject operario = new JSONObject();
    JSONObject tarea = new JSONObject();

    private TaskCompleted mCallback;

    String type_script;

    boolean return_image = false;
    BackgroundWorker(Context ctx){
        context = ctx;
        this.mCallback = (TaskCompleted)ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        type_script = type;

        String login_url;
        String register_url;
        String change_foto_url;
        String get_empresas_url;
        String get_operarios_url;
        String get_user_data_url;
        String get_one_tarea_url;
        String get_tareas_url;
        String get_contadores_url;
        String create_tarea_url;
        String update_tarea_url;
        String update_contador_url;
        String update_operario_url;
        String test_conection_url;
        String upload_image_url;
        String upload_user_image_url;
        String download_image_url;
        String download_user_image_url;
        String save_work_url;
        String load_work_url;
        String get_piezas_url;
        String get_gestores_url;
        String get_causas_url;
        String get_tareas_amout_url;
        String get_tareas_with_limit_url;
        String get_contadores_amout_url;
        String get_contadores_with_limit_url;

//        String server =  "https://server26194.000webhostapp.com/php/";
//        String server =  "https://server26194.000webhostapp.com/php/yoyi/";
//        String server =  "https://server26194.000webhostapp.com/";  //Cambiar a este al enviarselo a Michel
//        String server =  "https://mywateroute.com/"; //servidor actual de michel de pago
        String server =  "https://mywateroute.com/Mi_Ruta/"; //servidor actual de michel de pago

        if(server_online_or_wamp){

//            ///Importante el https en vez de http
//            login_url = "https://server26194.webcindario.com/login_operarios.php";  //https://hosting.miarroba.com/webftp.php?id=1875467#!path=%2Fweb


            login_url = server+"login_operarios.php";  //https://files.000webhost.com/
            register_url = server+"register_operario.php";
            change_foto_url = server+"change_foto.php";
            get_empresas_url = server+"get_empresas.php";
            get_operarios_url = server+"get_operarios.php";
            get_user_data_url = server+"get_one_operario.php";
            get_one_tarea_url = server+"get_one_tarea.php";
            get_tareas_url = server+"get_tareas.php";
            get_contadores_url = server+"get_contadores.php";
            create_tarea_url = server+"create_task.php";
            update_tarea_url = server+"update_tarea.php";
            update_operario_url = server+"update_operario.php";
            update_contador_url = server+"update_contador.php";
            test_conection_url = server+"test_database.php";
            upload_image_url = server+"upload_image.php";
            download_image_url = server+"download_image.php";
            download_user_image_url = server+"download_user_image.php";
            upload_user_image_url = server+"upload_user_image.php";
            save_work_url = server+"save_work.php";
            load_work_url = server+"load_work.php";
            get_piezas_url = server+"get_piezas.php";
            get_gestores_url= server+"get_gestores.php";
            get_causas_url = server+"get_causas.php";
            get_tareas_amout_url = server+"get_tareas_amount.php";
            get_tareas_with_limit_url = server+"get_tareas_with_limit.php";
            get_contadores_amout_url = server+"get_contadores_amount.php";
            get_contadores_with_limit_url = server+"get_contadores_with_limit.php";
        }
        else {
            //Para PC de Trabjo ojo cambiar esto entre
            String prestring = "http://192.168.20.93";
            //Mi PC en casa
//            String prestring = "http://192.168.56.1";

            login_url = prestring + "/login_operarios.php";
            register_url = prestring + "/register_operario.php";
            change_foto_url = prestring + "/change_foto.php";
            get_empresas_url = prestring + "/get_empresas.php";
            get_operarios_url = prestring + "/get_operarios.php";
            get_user_data_url = prestring + "/get_one_operario.php";
            get_one_tarea_url = prestring + "/get_one_tarea.php";
            get_tareas_url = prestring + "/get_tareas.php";
            get_contadores_url = prestring + "/get_contadores.php";
            create_tarea_url = prestring + "/create_task.php";
            update_tarea_url = prestring + "/update_tarea.php";
            update_contador_url = prestring+"/update_contador.php";
            update_operario_url = prestring + "/update_operario.php";
            upload_image_url = prestring + "/upload_image.php";
            download_image_url = prestring + "/download_image.php";
            download_user_image_url = prestring + "/download_user_image.php";
            upload_user_image_url = prestring + "/upload_user_image.php";
            save_work_url = prestring+"/save_work.php";
            load_work_url = prestring+"/load_work.php";
            get_piezas_url = prestring+"/get_piezas.php";
            get_gestores_url = prestring+"/get_gestores.php";
            get_causas_url = prestring+"/get_causas.php";
            get_tareas_amout_url = prestring+"/get_tareas_amount.php";
            get_tareas_with_limit_url = prestring+"/get_tareas_with_limit.php";
            get_contadores_amout_url = prestring+"/get_contadores_amount.php";
            get_contadores_with_limit_url = prestring+"/get_contadores_with_limit.php";
        }


        if(type.equals("login")) try {
            return_image = false;
            ArrayList<String> keys = new ArrayList<String>();
            keys.add("user_name");
            keys.add("password");
            keys.add("empresa");
            ArrayList<String> values = new ArrayList<String>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(params[i+1]);
            }
            ArrayList<String> result = post_Output_Info(keys, values, login_url, true, true);
            if(result.size() >1){
                return result.get(1);
            }
            else{
                return "";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        else if(type.equals("upload_image")){
            try {
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("foto");
                keys.add("nombre");
                keys.add("gestor");
                keys.add("numero_abonado");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, upload_image_url, true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return result.get(0);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("upload_user_image")){
            try {
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("foto");
                keys.add("user_name");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, upload_user_image_url, true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return "";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("download_image")){
            try {
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("nombre");
                keys.add("gestor");
                keys.add("numero_abonado");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                    Log.e("parametro", params[i+1]);
                }

                ArrayList<String> result = post_Output_Info(keys, values, download_image_url,true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return result.get(0);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("download_user_image")){
            try {
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("user_name");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, download_user_image_url,true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return "";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("update_operario")){
            //////Pasarle los valores de los keys a esta operario (se hace en la clase que llama a esta)-----------------------------------------------------------------------------------------------------
//            String operario_post = String.valueOf(Screen_Login_Activity.operario_JSON);
            try {
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("json");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, update_operario_url,true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return "";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("create_tarea")){
//            String tarea_post = String.valueOf(Screen_Login_Activity.tarea_JSON);
            try {
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("json");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, create_tarea_url,true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return "";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("load_work")){
            try {
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("nombre");

                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, load_work_url,
                        true, true);
                if(!result.isEmpty()){
                    return result.get(0);
                }
                else{
                    return "";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("save_work")){
            try {
                String boundary = "*****", twoHyphens = "--", lineEnd ="\r\n";
                int bytesRead, byteAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1*1024*1024;
                String loadWorkDirString = params[1]; //le envio la direccion como parametro
                File sourceFile = new File(loadWorkDirString);

                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(save_work_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                DataOutputStream dos = null;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                httpURLConnection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary="+boundary);
                httpURLConnection.setRequestProperty("bill", loadWorkDirString);

                dos = new DataOutputStream(httpURLConnection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"bill\"; filename=\""
                        +loadWorkDirString + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                byteAvailable = fileInputStream.available();

                bufferSize = Math.min(byteAvailable, maxBufferSize);
                buffer= new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0){
                    dos.write(buffer, 0, bufferSize);
                    byteAvailable = fileInputStream.available();
                    bufferSize = Math.min(byteAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens+boundary+twoHyphens+lineEnd);

                int serverResponseCode = httpURLConnection.getResponseCode();

                String result=httpURLConnection.getResponseMessage();

                fileInputStream.close();
                dos.flush();
                dos.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("update_contador")){
//            String contador_post = String.valueOf(Screen_Login_Activity.contador_JSON);
            try {
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("json");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, update_contador_url,true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return "";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else if(type.equals("update_tarea")){
//            String tarea_post = String.valueOf(Screen_Login_Activity.tarea_JSON);
            try {
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("json");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, update_tarea_url,true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return "";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else if (type.equals("get_one_tarea")){

            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add(DBtareasController.principal_variable);
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, get_one_tarea_url, true, true);
                String return_string = "";
                for(int n =0 ; n < result.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(result.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            tarea  = jsonArray.getJSONObject(i);
                            return_string = tarea.toString();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (type.equals("get_contadores_amount")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }

                ArrayList<String> answer = post_Output_Info(keys, values, get_contadores_amout_url, true, true);

                String return_string = answer.toString();

                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("get_contadores_with_limit")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();
                keys.add("LIMIT");
                keys.add("OFFSET");
                keys.add("empresa");
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                Screen_Table_Team.lista_contadores_servidor = post_Output_Info(keys, values, get_contadores_with_limit_url, true, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_contadores_servidor.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_contadores_servidor.get(n));
                        if(jsonArray != null){
                            return_string = "download success";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Table_Team.lista_contadores_servidor.size() > 1){
                    String retorno = Screen_Table_Team.lista_contadores_servidor.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("get_tareas_amount")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }

                ArrayList<String> answer = post_Output_Info(keys, values, get_tareas_amout_url, true, true);

                String return_string = answer.toString();

                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("get_tareas_with_limit")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();
                keys.add("LIMIT");
                keys.add("OFFSET");
                keys.add("empresa");
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                Screen_Table_Team.lista_tareas = post_Output_Info(keys, values, get_tareas_with_limit_url, true, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_tareas.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        if(jsonArray != null){
                            return_string = "download success";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Table_Team.lista_tareas.size() > 1){
                    String retorno = Screen_Table_Team.lista_tareas.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("get_tareas")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();

                Screen_Table_Team.lista_tareas = post_Output_Info(keys, values, get_tareas_url, true, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_tareas.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        if(jsonArray != null){
                            return_string = "download success";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Table_Team.lista_tareas.size() > 1){
                    String retorno = Screen_Table_Team.lista_tareas.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("get_contadores")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                Screen_Table_Team.lista_contadores_servidor = post_Output_Info(keys, values, get_contadores_url, true, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_contadores_servidor.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_contadores_servidor.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            return_string += jsonObject.toString();
                            //return_string += jsonObject.getString("poblacion")+" "+jsonObject.getString("calle");
                            return_string += "\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Table_Team.lista_contadores_servidor.size() > 1){
                    String retorno = Screen_Table_Team.lista_contadores_servidor.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("get_gestores")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }

                Screen_Table_Team.lista_gestores_servidor = post_Output_Info(keys, values, get_gestores_url, true, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_gestores_servidor.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_gestores_servidor.get(n));
                        if(jsonArray!=null){
                            return_string = "download success";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Table_Team.lista_gestores_servidor.size() > 1){
                    String retorno = Screen_Table_Team.lista_gestores_servidor.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("get_piezas")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();

                Screen_Table_Team.lista_piezas_servidor = post_Output_Info(keys, values, get_piezas_url, false, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_piezas_servidor.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_piezas_servidor.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            return_string += jsonObject.toString();
                            //return_string += jsonObject.getString("poblacion")+" "+jsonObject.getString("calle");
                            return_string += "\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Table_Team.lista_piezas_servidor.size() > 1){
                    String retorno = Screen_Table_Team.lista_piezas_servidor.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("get_causas")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();

                Screen_Table_Team.lista_causas_servidor = post_Output_Info(keys, values, get_causas_url, false, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_causas_servidor.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_causas_servidor.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            return_string += jsonObject.toString();
                            //return_string += jsonObject.getString("poblacion")+" "+jsonObject.getString("calle");
                            return_string += "\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Table_Team.lista_causas_servidor.size() > 1){
                    String retorno = Screen_Table_Team.lista_causas_servidor.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("get_user_data")){
            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("user_name");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, get_user_data_url, true, true);
                String return_string = "";
                for(int n =1 ; n < result.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(result.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            operario = jsonArray.getJSONObject(i);
//                            return_string += operario.getString("nombre")+" "+operario.getString("apellidos");
//                            return_string += "\n";
                            return_string = operario.toString();
                        }
                    } catch (JSONException e) {
                        Log.e("JSONException","get_user_data -> "+e.toString());
                        e.printStackTrace();
                    }
                }

                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (type.equals("get_empresas")){

            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();

                Screen_Login_Activity.lista_empresas = post_Output_Info(keys, values, get_empresas_url, false, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Login_Activity.lista_empresas.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Login_Activity.lista_empresas.get(n).trim());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            return_string += jsonObject.getString(DBEmpresasController.empresa);
                            return_string += "\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Login_Activity.lista_empresas.size() > 1){
                    String retorno = Screen_Login_Activity.lista_empresas.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string.trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (type.equals("get_operarios")){

            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                Screen_Login_Activity.lista_operarios = post_Output_Info(keys, values, get_operarios_url, true, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Login_Activity.lista_operarios.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Login_Activity.lista_operarios.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            return_string += jsonObject.getString(DBoperariosController.nombre)+" "+jsonObject.getString(DBoperariosController.apellidos);
                            return_string += "\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(Screen_Login_Activity.lista_operarios.size() > 1){
                    String retorno = Screen_Login_Activity.lista_operarios.get(1);
                    if(retorno.contains("<b>Warning</b>:  mysqli_connect():")
                            && retorno.contains("Too many connections")){
                        return "Servidor caido, ahora no se puede sincronizar";
                    }
                }
                return return_string;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(type.equals("change_foto")){

            try {
                return_image = true;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("user_name");
                keys.add("password");
                keys.add("image");
                keys.add("date_time_modified");
                keys.add("empresa");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, change_foto_url, true, true);
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else  if(type.equals("register")){

            try {
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("name");
                keys.add("surname");
                keys.add("age");
                keys.add("phone");
                keys.add("user_name");
                keys.add("password");
                keys.add("image");
                keys.add("date_time_modified");
                keys.add("empresa");

                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }

                ArrayList<String> result = post_Output_Info(keys, values, register_url, true, true);
                if(result.size() >1){
                    return result.get(1);
                }
                else{
                    return "";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<String> post_Output_Info(ArrayList<String> keys, ArrayList<String> values, String url_string, boolean post, boolean read)
            throws IOException {

        URL url = new URL(url_string);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        ArrayList<String> result = new ArrayList<String>();

        if(post) {
            boolean first_iteration = true;
            String post_String="";

            for(int i=0; i< keys.size(); i++){
                if(!first_iteration){
                    post_String+="&";
                }
                first_iteration=false;
                post_String = post_String + URLEncoder.encode(keys.get(i), "UTF-8")+"="+URLEncoder.encode(values.get(i), "UTF-8");
            }
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(post_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
        }
        if(read) {
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return result;
    }
    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Connecting...");
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(s);
        try {
            mCallback.onTaskComplete(type_script, result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
