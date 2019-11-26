package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
        String get_operarios_url;
        String get_user_data_url;
        String get_one_tarea_url;
        String get_tareas_url;
        String create_tarea_url;
        String update_tarea_url;
        String update_operario_url;
        String test_conection_url;
        String upload_image_url;
        String upload_user_image_url;
        String download_image_url;
        String download_user_image_url;

        String server =  "https://server26194.000webhostapp.com/php/"; //Cambiar a este al enviarselo a Michel
//        String server =  "https://server26194.000webhostapp.com/php/yoyi/";
//        String server =  "https://server26194.000webhostapp.com/";

        if(server_online_or_wamp){

//            ///Importante el https en vez de http
//            login_url = "https://server26194.webcindario.com/login_operarios.php";  //https://hosting.miarroba.com/webftp.php?id=1875467#!path=%2Fweb
//            register_url = "https://server26194.webcindario.com/register_operario.php";
//            change_foto_url = "https://server26194.webcindario.com/change_foto.php";
//            get_operarios_url = "https://server26194.webcindario.com/get_operarios.php";
//            get_user_data_url = "https://server26194.webcindario.com/get_one_operario.php";
//            get_one_tarea_url = "https://server26194.webcindario.com/get_one_tarea.php";
//            get_tareas_url = "https://server26194.webcindario.com/get_tareas.php";
//            test_conection_url = "https://server26194.webcindario.com/test_database.php";
//            create_tarea_url = "https://server26194.webcindario.com/create_task.php";
//            update_tarea_url = "https://server26194.webcindario.com/update_tarea.php";
//            update_operario_url = "https://server26194.webcindario.com/update_operario.php";
//            upload_image_url = "https://server26194.webcindario.com/upload_image.php";
//            download_image_url = "https://server26194.webcindario.com/download_image.php";
//            download_user_image_url = "https://server26194.webcindario.com/download_user_image.php";
//            upload_user_image_url = "https://server26194.webcindario.com/upload_user_image.php";

            login_url = server+"login_operarios.php";  //https://files.000webhost.com/
            register_url = server+"register_operario.php";
            change_foto_url = server+"change_foto.php";
            get_operarios_url = server+"get_operarios.php";
            get_user_data_url = server+"get_one_operario.php";
            get_one_tarea_url = server+"get_one_tarea.php";
            get_tareas_url = server+"get_tareas.php";
            create_tarea_url = server+"create_task.php";
            update_tarea_url = server+"update_tarea.php";
            update_operario_url = server+"update_operario.php";
            test_conection_url = server+"test_database.php";
            upload_image_url = server+"upload_image.php";
            download_image_url = server+"download_image.php";
            download_user_image_url = server+"download_user_image.php";
            upload_user_image_url = server+"upload_user_image.php";
        }
        else {

            //Para PC de Trabjo ojo cambiar esto entre
//            login_url = "http://192.168.21.125/login_operarios.php";
//            register_url = "http://192.168.21.125/register_operario.php";
//            change_foto_url = "http://192.168.21.125/change_foto.php";
//            get_operarios_url = "http://192.168.21.125/get_operarios.php";
//            get_user_data_url = "http://192.168.21.125/get_one_operario.php";
//            get_one_tarea_url = "http://192.168.21.125/get_one_tarea.php";
//            get_tareas_url = "http://192.168.21.125/get_tareas.php";
//            create_tarea_url = "http://192.168.21.125/create_task.php";
//            update_tarea_url = "http://192.168.21.125/update_tarea.php";
//            update_operario_url = "http://192.168.21.125/update_operario.php";
//            upload_image_url = "http://192.168.21.125/upload_image.php";
//            download_image_url = "http://192.168.21.125/download_image.php";
//            download_user_image_url = "http://192.168.21.125/download_user_image.php";
//            upload_user_image_url = "http://192.168.21.125/upload_user_image.php";
            //Mi PC en casa
            login_url = "http://192.168.56.1/login_operarios.php";
            register_url = "http://192.168.56.1/register_operario.php";
            change_foto_url = "http://192.168.56.1/change_foto.php";
            get_operarios_url = "http://192.168.56.1/get_operarios.php";
            get_user_data_url = "http://192.168.56.1/get_one_operario.php";
            get_one_tarea_url = "http://192.168.56.1/get_one_tarea.php";
            get_tareas_url = "http://192.168.56.1/get_tareas.php";
            create_tarea_url = "http://192.168.56.1/create_task.php";
            update_tarea_url = "http://192.168.56.1/update_tarea.php";
            update_operario_url = "http://192.168.56.1/update_operario.php";
            upload_image_url = "http://192.168.56.1/upload_image.php";
            download_image_url = "http://192.168.56.1/download_image.php";
            download_user_image_url = "http://192.168.56.1/download_user_image.php";
            upload_user_image_url = "http://192.168.56.1/upload_user_image.php";
        }


        if(type.equals("login")) try {
            return_image = false;
            ArrayList<String> keys = new ArrayList<String>();
            keys.add("user_name");
            keys.add("password");
            ArrayList<String> values = new ArrayList<String>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(params[i+1]);
            }
            ArrayList<String> result = post_Output_Info(keys, values, login_url, true, true);
            return result.get(1);

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
                keys.add("numero_abonado");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, upload_image_url, true, true);
                return result.get(1);

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
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, upload_user_image_url, true, true);
                return result.get(1);

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
                keys.add("numero_abonado");
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, download_image_url,true, true);
                return result.get(1);

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
                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }
                ArrayList<String> result = post_Output_Info(keys, values, download_user_image_url,true, true);
                return result.get(1);

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
            String operario_post = String.valueOf(Screen_Login_Activity.operario_JSON);
            try {

                URL url = new URL(update_operario_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");

                Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                bufferedWriter.write(operario_post);
                // bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line;
                String result="";
                while ((line = bufferedReader.readLine()) != null) {
                    result+=(line);
                }
                bufferedReader.close();
                inputStream.close();
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
        }else if(type.equals("create_tarea")){

            String tarea_post = String.valueOf(Screen_Login_Activity.tarea_JSON);
            try {

                URL url = new URL(create_tarea_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");

                Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                bufferedWriter.write(tarea_post);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line;
                String result="";
                while ((line = bufferedReader.readLine()) != null) {
                    result+=(line);
                }
                bufferedReader.close();
                inputStream.close();
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
        else if(type.equals("update_tarea")){

            String tarea_post = String.valueOf(Screen_Login_Activity.tarea_JSON);
            try {

                URL url = new URL(update_tarea_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");

                Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                bufferedWriter.write(tarea_post);
                // bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line;
                String result="";
                while ((line = bufferedReader.readLine()) != null) {
                    result+=(line);
                }
                bufferedReader.close();
                inputStream.close();
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
        else if (type.equals("get_one_tarea")){

            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add(DBtareasController.numero_interno);
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

        }else if (type.equals("get_tareas")){

            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();

                Screen_Table_Team.lista_tareas = post_Output_Info(keys, values, get_tareas_url, false, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Table_Team.lista_tareas.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
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

        }else if (type.equals("get_operarios")){

            try{
                return_image = false;
                ArrayList<String> keys = new ArrayList<String>();
                ArrayList<String> values = new ArrayList<String>();

                Screen_Login_Activity.lista_operarios = post_Output_Info(keys, values, get_operarios_url, false, true);

                String return_string = "";
                for(int n =1 ; n < Screen_Login_Activity.lista_operarios.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Login_Activity.lista_operarios.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            return_string += jsonObject.getString("nombre")+" "+jsonObject.getString("apellidos");
                            return_string += "\n";
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

        }else if(type.equals("change_foto")){

            try {
                return_image = true;
                ArrayList<String> keys = new ArrayList<String>();
                keys.add("user_name");
                keys.add("password");
                keys.add("image");
                keys.add("date_time_modified");
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

                ArrayList<String> values = new ArrayList<String>();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(params[i+1]);
                }

                ArrayList<String> result = post_Output_Info(keys, values, register_url, true, true);
                return result.get(1);

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
