package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alejandro on 19/08/2019.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {

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
        String update_tarea_url;
        String test_conection_url;

        if(Screen_Login_Activity.isOnline){

            login_url = "https://server26194.webcindario.com/login_operarios.php";  //https://hosting.miarroba.com/webftp.php?id=1875467#!path=%2Fweb
            register_url = "https://server26194.webcindario.com/register_operario.php";
            change_foto_url = "https://server26194.webcindario.com/change_foto.php";
            get_operarios_url = "https://server26194.webcindario.com/get_operarios.php";
            get_user_data_url = "https://server26194.webcindario.com/get_one_operario.php";
            get_one_tarea_url = "https://server26194.webcindario.com/get_one_tarea.php";
            get_tareas_url = "https://server26194.webcindario.com/get_tareas.php";
            test_conection_url = "https://server26194.webcindario.com/test_database.php";
            update_tarea_url = "http://server26194.webcindario.com/probando_json.php";

//            login_url = "https://server26194.000webhostapp.com/login_operarios.php";  //https://files.000webhost.com/
//            register_url = "https://server26194.000webhostapp.com/register_operario.php";
//            change_foto_url = "https://server26194.000webhostapp.com/change_foto.php";
//            get_operarios_url = "https://server26194.000webhostapp.com/get_operarios.php";
//            get_user_data_url = "https://server26194.000webhostapp.com/get_one_operario.php";
//            get_one_tarea_url = "https://server26194.000webhostapp.com/get_one_tarea.php";
//            get_tareas_url = "https://server26194.000webhostapp.com/get_tareas.php";
//            update_tarea_url = "http://server26194.000webhostapp.com/probando_json.php";

//            test_conection_url = "https://server26194.000webhostapp.com/test_database.php";
        }
        else {
            login_url = "http://192.168.137.50/login_operarios.php";
            register_url = "http://192.168.137.50/register_operario.php";
            change_foto_url = "http://192.168.137.50/change_foto.php";
            get_operarios_url = "http://192.168.137.50/get_operarios.php";
            get_user_data_url = "http://192.168.137.50/get_one_operario.php";

            get_one_tarea_url = "http://192.168.137.50/get_one_tarea.php";
            get_tareas_url = "http://192.168.137.50/get_tareas.php";

            update_tarea_url = "http://192.168.137.50/probando_json.php";
        }

//        try {
//            operario.put("nombre", "Ale");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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
        else if(type.equals("update_tarea")){

            String tareas = String.valueOf(Screen_Login_Activity.tarea_JSON);
            URL url = null;
            try {
                url = new URL(update_tarea_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                OutputStream outputStream = null;

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(tareas);
                bufferedWriter.close();
                outputStream.close();

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
                keys.add("numero_serie_contador");
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
                for(int n =0 ; n < Screen_Table_Team.lista_tareas.size() ; n++) {
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
                for(int n =0 ; n < result.size() ; n++) {
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

                ArrayList<String> result = post_Output_Info(keys, values, get_operarios_url, false, true);

                String return_string = "";
                for(int n =0 ; n < result.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(result.get(n));
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
