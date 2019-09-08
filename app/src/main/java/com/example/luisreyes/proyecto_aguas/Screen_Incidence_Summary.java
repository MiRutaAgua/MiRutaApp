package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Incidence_Summary extends Activity implements TaskCompleted{

    private ImageView firma_cliente, foto1, foto2, foto3, cerrar_tarea, compartir;
    private Intent intent_open_screen_client_sign;
    private static final int CANVAS_REQUEST = 3331;
    private Bitmap bitmap_firma_cliente;
    private TextView observaciones_incidence, nombre_y_tarea;
    private EditText lectura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_incidence_summary);

        intent_open_screen_client_sign = new Intent(this, Screen_Draw_Canvas.class);
        firma_cliente = (ImageView)findViewById(R.id.imageButton_firma_cliente_screen_validate);
        foto1 = (ImageView)findViewById(R.id.imageView_foto1_screen_incidence_summary);
        foto2 = (ImageView)findViewById(R.id.imageView_foto2_screen_incidence_summary);
        foto3 = (ImageView)findViewById(R.id.imageView_foto3_screen_incidence_summary);
        cerrar_tarea = (ImageView)findViewById(R.id.button_cerrar_tarea_screen_incidence_sumary);
        compartir = (ImageView)findViewById(R.id.button_compartir_screen_incidence_sumary);
        observaciones_incidence = (TextView)findViewById(R.id.textView_obsevaciones_screen_incidence_summary);
        nombre_y_tarea = (TextView)findViewById(R.id.textView_nombre_y_tarea_screen_incidence_summary);
        lectura = (EditText)findViewById(R.id.editText_lectura_de_contador_screen_incidence_summary);


        try {
            nombre_y_tarea.setText(Screen_Login_Activity.tarea_JSON.getString("nombre_cliente").replace("\n", "")+", "+Screen_Login_Activity.tarea_JSON.getString("calibre_toma"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener nombre de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            foto1.setImageBitmap(Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_1")));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener foto incidencia 1", Toast.LENGTH_LONG).show();
        }
        try {
            foto2.setImageBitmap(Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_2")));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener foto incidencia 2", Toast.LENGTH_LONG).show();
        }
        try {
            foto3.setImageBitmap(Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_3")));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener foto incidencia 3", Toast.LENGTH_LONG).show();
        }
        try {
            observaciones_incidence.setText(Screen_Login_Activity.tarea_JSON.getString("incidencia"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener texto incidencia de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            Screen_Login_Activity.tarea_JSON.put("observaciones", Screen_Login_Activity.tarea_JSON.getString("incidencia"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar observaciones de tarea", Toast.LENGTH_LONG).show();
        }

        firma_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent_open_screen_client_sign, CANVAS_REQUEST);
            }
        });
        cerrar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(TextUtils.isEmpty(lectura.getText()))) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put("lectura_actual", lectura.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
                    }
                }
                try {
                    String firma = Screen_Register_Operario.getStringImage(bitmap_firma_cliente);
                    Screen_Login_Activity.tarea_JSON.put("firma_cliente", firma);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar firma de cliente", Toast.LENGTH_LONG).show();
                }
                String type = "update_tarea";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Incidence_Summary.this);
                backgroundWorker.execute(type);
                Toast.makeText(Screen_Incidence_Summary.this, "Guardando Incidencias", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CANVAS_REQUEST){
            String firma = data.getStringExtra("firma_cliente");
            //int result = data.getIntExtra("result", 0);
            //String res = String.valueOf(result);
            bitmap_firma_cliente = getImageFromString(firma);
            firma_cliente.setImageBitmap(bitmap_firma_cliente);
            //Toast.makeText(Screen_Validate.this, "Resultado ok: " + res, Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap getImageFromString(String stringImage){
        byte[] decodeString = Base64.decode(stringImage, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeImage;
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "update_tarea") {
            if (!checkConection()) {
                Toast.makeText(Screen_Incidence_Summary.this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(Screen_Incidence_Summary.this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                } else {
                    if (result.contains("not success")) {
                        Toast.makeText(Screen_Incidence_Summary.this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Screen_Incidence_Summary.this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent_open_task_or_personal_screen = new Intent(Screen_Incidence_Summary.this, team_or_personal_task_selection_screen_Activity.class);
                        startActivity(intent_open_task_or_personal_screen);
                        Screen_Incidence_Summary.this.finish();
                    }
                }
            }
        }
    }

    public boolean checkConection(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
}
