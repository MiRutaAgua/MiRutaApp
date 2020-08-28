package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Execute_Task extends AppCompatActivity implements Dialog.DialogListener, TaskCompleted{

    private  TextView textView_observaciones_screen_exec_task,
            textView_serial_number_result,
            textView_serial_number_module_result,
            telefonos, telefono1, telefono2,
            textView_anomalia,
            textView_codigo_geolocalization_screen_exec_task;

    private String tag="";
    private Button button_scan_serial_number_screen_exec_task;

    private Button button_scan_module_screen_exec_task,
    anomaly_button, imageView_itac_screen_exec_task,
            button_guardar_datos;

    private Button button_validate_screen_exec_task,
            button_geolocalization_screen_exec_task;

    private ImageView imageView_edit_phone1_screen_exec_task,
            imageView_edit_phone2_screen_exec_task,
            imageView_edit_serial_number_screen_exec_task,
            imageView_edit_serial_number_module_screen_exec_task,
            button_codigo_geolocalization_screen_exec_task,
            imageView_call_phone1_exec_task,
    imageView_call_phone2_exec_task, imageview_edit_lectura_screen_exec_task,
            observaciones_button;

    private Button button_instalation_photo_screen_exec_task;
    private ImageView instalation_photo_screen_exec_task;
    private Button button_read_photo_screen_exec_task;
    private ImageView read_photo_screen_exec_task;
    private Button button_serial_number_photo_screen_exec_task;
    private ImageView serial_number_photo_screen_exec_task;
    private Button button_after_instalation_photo_screen_exec_task;
    private ImageView after_instalation_photo_screen_exec_task;
    private String contador = "";
    public static String lectura_introducida = "";
    public static String numero_serie_viejo = "";

    private EditText lectura_editText;
    private String lectura_string = "";

    private static final int CAM_REQUEST_INST_PHOTO = 1313;
    private static final int CAM_REQUEST_READ_PHOTO = 1314;
    private static final int CAM_REQUEST_SN_PHOTO = 1315;
    private static final int CAM_REQUEST_AFT_INT_PHOTO = 1316;
    private static final int REQUEST_LECTOR_SNC = 1317;
    private static final int REQUEST_LECTOR_SNM = 1318;
    private static final int REQUEST_ANOMALY = 1319;

    private Intent intent_open_scan_screen_lector;

    public static ProgressDialog progressDialog;
    public static String mCurrentPhotoPath_foto_antes = "";
    public static String mCurrentPhotoPath_foto_lectura= "";
    public static String mCurrentPhotoPath_foto_despues = "";
    public static String mCurrentPhotoPath_foto_serie = "";
    private ArrayList<String> images_files;
    private ArrayList<String> images_files_names;
    private boolean showing;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( //Para esconder el teclado
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.screen_execute_task);

        mCurrentPhotoPath_foto_antes = "";
        mCurrentPhotoPath_foto_lectura= "";
        mCurrentPhotoPath_foto_despues = "";
        mCurrentPhotoPath_foto_serie = "";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        images_files= new ArrayList<>();
        images_files_names =new ArrayList<>();

        imageview_edit_lectura_screen_exec_task = (ImageView)findViewById(R.id.imageview_edit_lectura_screen_exec_task);
        imageView_edit_serial_number_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_serial_number_screen_exec_task);
        imageView_edit_serial_number_module_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_serial_number_module_screen_exec_task);
        imageView_edit_phone1_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_phone1_screen_exec_task);
        imageView_edit_phone2_screen_exec_task= (ImageView)findViewById(R.id.imageView_edit_phone2_screen_exec_task);
        imageView_call_phone2_exec_task= (ImageView)findViewById(R.id.imageView_edit_phone2_exec_task);
        imageView_call_phone1_exec_task= (ImageView)findViewById(R.id.imageView_edit_phone1_exec_task);

        lectura_editText = (EditText)findViewById(R.id.editText_lectura_screen_exec_task);
        textView_serial_number_result = (TextView)findViewById(R.id.textView_serial_number_screen_exec_task);
        textView_serial_number_module_result = (TextView)findViewById(R.id.textView_module_number_screen_exec_task);
        textView_codigo_geolocalization_screen_exec_task = (TextView)findViewById(R.id.textView_codigo_geolocalization_screen_exec_task);

        textView_anomalia = (TextView) findViewById(R.id.textView_anomalia_screen_exec_task);
        telefonos = (TextView) findViewById(R.id.textView_phones_screen_exec_task);
        telefono1 = (TextView) findViewById(R.id.textView_phone1_screen_exec_task);
        telefono2 = (TextView) findViewById(R.id.textView_phone2_screen_exec_task);
        button_instalation_photo_screen_exec_task = (Button)findViewById(R.id.button_instalation_photo_screen_exec_task);
        button_read_photo_screen_exec_task = (Button)findViewById(R.id.button_read_photo_screen_exec_task);
        button_serial_number_photo_screen_exec_task = (Button)findViewById(R.id.button_serial_number_photo_screen_exec_task);
        button_after_instalation_photo_screen_exec_task = (Button)findViewById(R.id.button_final_instalation_photo_screen_exec_task);
        instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.instalation_photo_screen_exec_task);
        read_photo_screen_exec_task = (ImageView)findViewById(R.id.read_photo_screen_exec_task);
        serial_number_photo_screen_exec_task = (ImageView)findViewById(R.id.serial_number_photo_screen_exec_task);
        after_instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.final_instalation_photo_screen_exec_task);
        button_codigo_geolocalization_screen_exec_task = (ImageView)findViewById(R.id.button_codigo_geolocalization_screen_exec_task);

        imageView_itac_screen_exec_task = (Button)findViewById(R.id.imageView_itac_screen_exec_task);
        button_scan_serial_number_screen_exec_task= (Button)findViewById(R.id.button_scan_serial_number_screen_exec_task);
        button_scan_module_screen_exec_task= (Button)findViewById(R.id.button_scan_module_screen_exec_task);
        button_validate_screen_exec_task          = (Button)findViewById(R.id.button_validate_screen_exec_task);
        button_guardar_datos = (Button)findViewById(R.id.button_guardar_datos_screen_exec_task);
        observaciones_button = (ImageView)findViewById(R.id.button_observations_screen_exec_task);
        anomaly_button = (Button)findViewById(R.id.button_anomalia_screen_exec_task);
        textView_observaciones_screen_exec_task = (TextView)findViewById(R.id.textView_observaciones_screen_exec_task);
        button_geolocalization_screen_exec_task= (Button)findViewById(R.id.button_geolocalization_screen_exec_task);

        String photo = "";
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion).trim();
            Log.e("photo: ", photo);
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                Log.e("bitmap_dir: ", bitmap_dir);
                if(!bitmap_dir.isEmpty()){
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap!=null) {
                        Log.e("Bitmap ok: ", bitmap_dir);
                        mCurrentPhotoPath_foto_antes = bitmap_dir;
                        instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                        instalation_photo_screen_exec_task.setImageBitmap(bitmap);
                        instalation_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                        lectura_editText.setVisibility(View.VISIBLE);
                    }else{
                        Log.e("Bitmap nulo: ", "foto_antes_instalacion");
                    }
                }else{
                    Log.e("no Existe: ", "foto_antes_instalacion");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_lectura).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    Bitmap bitmap =getPhotoUserLocal(bitmap_dir);
                    mCurrentPhotoPath_foto_lectura = bitmap_dir;
                    read_photo_screen_exec_task.setVisibility(View.VISIBLE);
                    read_photo_screen_exec_task.setImageBitmap(bitmap);
                    read_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                }
                else{
                    Log.e("no Existe: ", "foto_lectura");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_numero_serie).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    Bitmap bitmap =getPhotoUserLocal(bitmap_dir);
                    mCurrentPhotoPath_foto_serie = bitmap_dir;
                    serial_number_photo_screen_exec_task.setVisibility(View.VISIBLE);
                    serial_number_photo_screen_exec_task.setImageBitmap(bitmap);
                    serial_number_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_despues_instalacion).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    Bitmap bitmap =getPhotoUserLocal(bitmap_dir);
                    mCurrentPhotoPath_foto_despues = bitmap_dir;
                    after_instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                    after_instalation_photo_screen_exec_task.setImageBitmap(bitmap);
                    after_instalation_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                }else{
                    Log.e("no Existe: ", "foto_despues_instalacion");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String contadorDV = Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.numero_serie_contador_devuelto)
                    .trim().replace(" ", "");
            if(Screen_Login_Activity.checkStringVariable(contadorDV)) {
                textView_serial_number_result.setText(contadorDV);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
        }

        try {
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
            .trim().replace(" ", "");
            numero_serie_viejo = contador;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
        }
        try {
            String codigo = Screen_Login_Activity.tarea_JSON.getString(
                    DBtareasController.codigo_de_geolocalizacion).trim();
            if(!codigo.isEmpty() && !codigo.equals("NULL") && !codigo.equals("null"))
                textView_codigo_geolocalization_screen_exec_task.setText(codigo);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener codigo_de_geolocalizacion de tarea", Toast.LENGTH_LONG).show();
        }
        try {
            textView_observaciones_screen_exec_task.setText(Screen_Login_Activity.tarea_JSON.getString(
                    DBtareasController.MENSAJE_LIBRE));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener observaciones de tarea", Toast.LENGTH_LONG).show();
        }
        try {
            String telefono1_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono1);
            if(!telefono1_string.equals("null") && !telefono1_string.isEmpty() && telefono1_string!=null) {
                telefono1.setText(telefono1_string);
            }else{
                telefono1.setText("Añadir");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener telefono 1 de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            String telefono2_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono2);
            if(!telefono2_string.equals("null") && !telefono2_string.isEmpty() && telefono2_string!=null) {
                telefono2.setText(telefono2_string);
            }else{
                telefono2.setText("Añadir");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener telefono 2 de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            lectura_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual);
            if(!TextUtils.isEmpty(lectura_string) && !lectura_string.equals("null")){
                lectura_editText.setHint(lectura_string);
            }else{
                lectura_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_ultima);
                if(!TextUtils.isEmpty(lectura_string) && !lectura_string.equals("null")){
                    lectura_editText.setHint(lectura_string);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener actual lectura de contador", Toast.LENGTH_LONG).show();
        }

        lectura_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()) {
                    Log.e("onTextChanged", charSequence.toString());
                    Log.e("lectura_string", lectura_string);
                    lectura_introducida = charSequence.toString();
                    Log.e("lectura_introducida", lectura_introducida);
                    Integer lect = Integer.parseInt(charSequence.toString());
                    if (!lectura_string.isEmpty() ) {
                        Integer last = Integer.parseInt(lectura_string.trim());
                        Log.e("last", last.toString());
                        if (last > 0) {
                            Log.e("last", "mayor 0");
                            if (lect - last >= 100) {
                                Log.e("lect - last", "mayor");
                                if (!MessageDialog.isShowing()) {
                                    openMessage("Advertencia", "La diferencia de lectura es mayor que 100");
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        imageView_itac_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        String cod_emplazamiento = textView_codigo_geolocalization_screen_exec_task.
                                getText().toString();
                        if(!cod_emplazamiento.isEmpty()) {
                            onButtonITAC(cod_emplazamiento);
                        }
                    }
                });
                imageView_itac_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_call_phone2_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        if(Screen_Absent.checkIfOnlyNumbers(telefono2.getText().toString().trim())){
                            Screen_Absent.callNumber(Screen_Execute_Task.this,telefono2.getText().toString().trim());
                        }
                    }
                });
                imageView_call_phone2_exec_task.startAnimation(myAnim);
            }
        });
        imageView_call_phone1_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        if(Screen_Absent.checkIfOnlyNumbers(telefono1.getText().toString().trim())){
                            Screen_Absent.callNumber(Screen_Execute_Task.this,telefono1.getText().toString().trim());
                        }
                    }
                });
                imageView_call_phone1_exec_task.startAnimation(myAnim);
            }
        });
        button_geolocalization_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
                        intent.putExtra("INSERTANDO", false);
                        startActivity(intent);
                    }
                });
                button_geolocalization_screen_exec_task.startAnimation(myAnim);
            }
        });

        imageview_edit_lectura_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        openDialog("Lectura",lectura_editText.getText().toString());
                    }
                });
                imageview_edit_lectura_screen_exec_task.startAnimation(myAnim);
            }

        });
        textView_serial_number_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        openDialog("Numero Serie",textView_serial_number_result.getText().toString());
                    }
                });
                textView_serial_number_result.startAnimation(myAnim);
            }

        });
        imageView_edit_serial_number_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        openDialog("Número Serie",textView_serial_number_result.getText().toString());
                    }
                });
                imageView_edit_serial_number_screen_exec_task.startAnimation(myAnim);

            }
        });
        imageView_edit_serial_number_module_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        openDialog("Número Serie de Modulo",textView_serial_number_module_result.getText().toString());
                    }
                });
                imageView_edit_serial_number_module_screen_exec_task.startAnimation(myAnim);
            }
        });

        button_codigo_geolocalization_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        openDialog("Código de geolocalización",textView_codigo_geolocalization_screen_exec_task.getText().toString());
                    }
                });
                button_codigo_geolocalization_screen_exec_task.startAnimation(myAnim);
            }
        });
        textView_serial_number_module_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        openDialog("Numero Serie de Modulo",textView_serial_number_module_result.getText().toString());
                    }
                });
                textView_serial_number_module_result.startAnimation(myAnim);

            }

        });
        button_guardar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        onGuardar_Datos();
                    }
                });
                button_guardar_datos.startAnimation(myAnim);
            }
        });
        observaciones_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        openDialog("Mensaje libre",textView_observaciones_screen_exec_task.getText().toString());
                    }
                });
                observaciones_button.startAnimation(myAnim);
            }
        });

        anomaly_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        Intent open_anomaly_screen = new Intent(Screen_Execute_Task.this, Screen_Anomaly.class);
                        startActivityForResult(open_anomaly_screen, REQUEST_ANOMALY);
                    }
                });
                anomaly_button.startAnimation(myAnim);
            }
        });

        telefonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 1",Screen_Absent.checkIfAgregar(telefono1.getText().toString()));
            }
        });
        imageView_edit_phone1_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 1",Screen_Absent.checkIfAgregar(telefono1.getText().toString()));
            }
        });
        imageView_edit_phone2_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 2",Screen_Absent.checkIfAgregar(telefono2.getText().toString()));
            }
        });
        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 1",Screen_Absent.checkIfAgregar(telefono1.getText().toString()));
            }
        });
        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 2",Screen_Absent.checkIfAgregar(telefono2.getText().toString()));
            }
        });

        button_validate_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        guardar_en_JSON_modificaciones();
                        String lect = lectura_editText.getText().toString();
                        if(Screen_Absent.checkOnlyNumbers(lect)) {
                            lectura_introducida = lect;
                            Log.e("lectura_ ", "Llena");

                        }else{
                            lectura_introducida="";
                            Log.e("lectura_ ", "Vacia");
                        }
                        if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
                            Toast.makeText(getApplicationContext(), "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
                        }
                        showRingDialog("Validando...");
                        Intent intent_open_screen_validate = new Intent(Screen_Execute_Task.this, Screen_Validate.class);
                        startActivity(intent_open_screen_validate);
                    }
                });
                button_validate_screen_exec_task.startAnimation(myAnim);
            }
        });

        button_scan_serial_number_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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

                        intent_open_scan_screen_lector = new Intent(Screen_Execute_Task.this, lector.class);
                        startActivityForResult(intent_open_scan_screen_lector, REQUEST_LECTOR_SNC);
                        textView_serial_number_result.setVisibility(View.VISIBLE);
                    }
                });
                button_scan_serial_number_screen_exec_task.startAnimation(myAnim);
            }
        });
        button_scan_module_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Execute_Task.this, R.anim.bounce);
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
                        intent_open_scan_screen_lector = new Intent(Screen_Execute_Task.this, lector.class);
                        startActivityForResult(intent_open_scan_screen_lector, REQUEST_LECTOR_SNM);
                        textView_serial_number_module_result.setVisibility(View.VISIBLE);
                    }
                });
                button_scan_module_screen_exec_task.startAnimation(myAnim);
            }
        });

        button_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_INST_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", numero_serie_viejo + "_foto_antes_instalacion");
                    intent_camera.putExtra("photo_folder", Screen_Login_Activity.current_empresa + "/fotos_tareas");
                    intent_camera.putExtra("contador", numero_serie_viejo);
                    startActivityForResult(intent_camera, CAM_REQUEST_INST_PHOTO);
                }
            }
        });
        button_read_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_READ_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", numero_serie_viejo + "_foto_lectura");
                    intent_camera.putExtra("photo_folder", Screen_Login_Activity.current_empresa + "/fotos_tareas");
                    intent_camera.putExtra("contador", numero_serie_viejo);
                    startActivityForResult(intent_camera, CAM_REQUEST_READ_PHOTO);
                }
            }
        });
        button_serial_number_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_SN_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", numero_serie_viejo + "_foto_numero_serie");
                    intent_camera.putExtra("photo_folder", Screen_Login_Activity.current_empresa + "/fotos_tareas");
                    intent_camera.putExtra("contador", numero_serie_viejo);
                    startActivityForResult(intent_camera, CAM_REQUEST_SN_PHOTO);
                }
            }
        });
        button_after_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                String numero_serie_devuelto = "";
                try {
                    numero_serie_devuelto = Screen_Login_Activity.tarea_JSON.
                            getString(DBtareasController.numero_serie_contador_devuelto).trim();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(numero_serie_devuelto.isEmpty() || numero_serie_devuelto.equals("null") || numero_serie_devuelto.equals("NULL")){
                    Toast.makeText(Screen_Execute_Task.this, "Inserte o escanee el número de serie nuevo", Toast.LENGTH_LONG).show();
                    return;
                }
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_AFT_INT_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador + "_foto_despues_instalacion");
                    intent_camera.putExtra("photo_folder", Screen_Login_Activity.current_empresa + "/fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_AFT_INT_PHOTO);
                }
            }
        });


        String anomaly_Order="";
        try {
            anomaly_Order = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.AREALIZAR_devuelta);
            if(!Screen_Login_Activity.checkStringVariable(anomaly_Order)){
                anomaly_Order = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA);
                llenarInformacionDeAnomalia(anomaly_Order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        llenarInformacionDeEmplazamiento();

    }

    private void onButtonITAC(String cod_emplazamiento) {
        if (team_or_personal_task_selection_screen_Activity.dBitacsController != null) {
            if (team_or_personal_task_selection_screen_Activity.dBitacsController.databasefileExists(this)) {
                if (team_or_personal_task_selection_screen_Activity.dBitacsController.checkForTableExists()) {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                        ArrayList<String> itacs = new ArrayList<>();
                        try {
                            itacs = team_or_personal_task_selection_screen_Activity.
                                    dBitacsController.get_all_itacs_from_Database();
                            for (int i = 0; i < itacs.size(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(itacs.get(i));
                                    if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                                        continue;
                                    }
                                    String c_emplazamiento = jsonObject.getString(
                                            DBitacsController.codigo_itac).trim();

                                    if(c_emplazamiento.equals(cod_emplazamiento)){
                                        if (jsonObject != null) {
                                            Screen_Login_Activity.itac_JSON = jsonObject;
                                            try {
                                                if(Screen_Login_Activity.itac_JSON!=null) {
                                                    Intent intent = new Intent(this, Screen_Itac.class);
                                                    startActivity(intent);
                                                    return;
                                                }else{
                                                    Toast.makeText(this, "Itac nula", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(this, "No pudo acceder a itac Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(this, "JSON nulo, se delvio de la tabla elemento nulo", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                new AlertDialog.Builder(this)
                        .setTitle("No exite ITAC")
                        .setMessage("¿Desea crear un nuevo itac?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                JSONObject jsonObject = new JSONObject();
                                Screen_Login_Activity.itac_JSON = DBitacsController.setEmptyJSON(jsonObject);
                                try {
                                    Screen_Login_Activity.itac_JSON.put(
                                            DBitacsController.codigo_itac, cod_emplazamiento);
                                    Intent intent_open_screen_edit_itac = new Intent(
                                            Screen_Execute_Task.this, Screen_Edit_ITAC.class);
                                    startActivity(intent_open_screen_edit_itac);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        }
    }

    private void llenarInformacionDeEmplazamiento() {
        String emplazamiento="";
        try {
            emplazamiento = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.emplazamiento_devuelto).trim();
            if(!Screen_Login_Activity.checkStringVariable(emplazamiento)){
                emplazamiento = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.emplazamiento).trim();
                if(emplazamiento.contains("BA-") || emplazamiento.contains("BT-")){
                    String resto= "";
                    if(emplazamiento.contains("BA")){
                        resto = emplazamiento.replace("BA", "");
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.emplazamiento_devuelto, "BA");
                    }
                    if(emplazamiento.contains("BT")){
                        resto = emplazamiento.replace("BT", "");
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.emplazamiento_devuelto, "BT");
                    }
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.RESTO_EM, resto);
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.ubicacion_en_bateria, resto);
                }else {
                    Iterator it = Tabla_de_Codigos.mapaTiposDeEmplazamiento.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        //System.out.println(pair.getKey() + " = " + pair.getValue());
                        if (pair.getValue().toString().toLowerCase().equals(emplazamiento.toLowerCase())) {
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.emplazamiento_devuelto, pair.getKey().toString());
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.RESTO_EM, Tabla_de_Codigos.mapaTiposDeRestoEmplazamiento.get(pair.getKey().toString()));
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void llenarInformacionDeAnomalia(String anomalia) {
        String resultados = Tabla_de_Codigos.getResultadosPosiblesByAnomaly(anomalia);
        String intervencion = Tabla_de_Codigos.getIntervencionByAnomaly(anomalia);
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.resultado, resultados);
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.AREALIZAR_devuelta, //causa destino
                    anomalia + " - " + intervencion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String lookForAllreadyTakenPhotos(String photo_name) throws JSONException {
        String numero_abonado = null;
        String gestor = null;
        try {
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            String dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa + "/fotos_tareas/" + gestor + "/" +numero_abonado;
            File myDir = new File(dir);
            if(myDir.exists()){
                String file_full_name = dir+"/"+photo_name;
                File photo = new File(file_full_name);
                if(photo.exists()){
                    return photo.getAbsolutePath();
                }
            }
        } catch (JSONException e) {
            Log.e("JSONException", "lookForAllreadyTakenPhotos "+e.toString());
            e.printStackTrace();
        }
        return "";
    }

    public void onGuardar_Datos(){
        guardar_en_JSON_modificaciones();
        if(!(TextUtils.isEmpty(lectura_editText.getText()))) {
            Log.e("onGuardar_Datos", "Solo numeros");
            Log.e("lectura_editText", lectura_editText.getText().toString());
            if (!lectura_string.equals("null") && !lectura_string.equals("NULL") && !lectura_string.isEmpty()) {
                Log.e("lectura_string", lectura_string);
                String lectura_actual = lectura_editText.getText().toString();
                Integer lectura_actual_int = Integer.parseInt(lectura_actual);
                Integer lectura_last_int = Integer.parseInt(lectura_string);

                if (lectura_actual_int >= lectura_last_int) {
                    if(lectura_actual_int - lectura_last_int >= 100){
                        new AlertDialog.Builder(this)
                                .setTitle("Lectura muy grande")
                                .setMessage("La diferencia de lecturas es mayor que 100m3\n¿Desea guardar con esta lectura?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        try {
//                                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_string);
                                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_actual);
                                            saveData();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(Screen_Execute_Task.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
                    else {
                        try {
//                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_string);
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_actual);
                            saveData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Screen_Execute_Task.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Lectura menor")
                            .setMessage("La lectura insertada es menor a la última registrada\n¿Desea guardar con esta lectura menor?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
//                                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_string);
                                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_actual);
                                        saveData();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Screen_Execute_Task.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
//                    Toast.makeText(Screen_Execute_Task.this, "La lectura del contador debe ser mayor que la ultima registrada", Toast.LENGTH_LONG).show();
                }
            } else {
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_editText.getText().toString());
                    saveData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Execute_Task.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            new AlertDialog.Builder(Screen_Execute_Task.this)
                    .setTitle("Lectura Faltante")
                    .setMessage("No ha insertado lectura\n¿Desea guardar esta tarea?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveData();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
//            Toast.makeText(Screen_Execute_Task.this, "Inserte la lectura del contador", Toast.LENGTH_LONG).show();
        }
    }
    public void saveData() {
        boolean error=false;
        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            try {
                String status = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.status_tarea) ;
                if(!status.contains("TO_UPDATE")){
                    status = status + ", TO_UPDATE";
                }
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.f_instnew, "ANDROID "
                        + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario));
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, status);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.Estado, "NORMAL");
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            } catch (JSONException e) {
                Toast.makeText(this, "No se pudo guardar tarea local " + e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
                error = true;
            }
        }else{
            error = true;
            Toast.makeText(this, "No hay tabla donde guardar", Toast.LENGTH_LONG).show();
        }
        if(checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica) {
            showRingDialog("Guardando Datos...");
            String type = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type);
        } else{
            if(!error) {
                Toast.makeText(this, "Se guardaron los datos en el teléfono", Toast.LENGTH_LONG).show();
                finishesThisClass();
            }
        }
    }

    private void guardar_en_JSON_modificaciones() {
        try {
            String fecha = DBtareasController.getStringFromFechaHora(new Date());
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified, fecha);
            if(!DBtareasController.tabla_model) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.F_INST, fecha);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.fecha_de_cambio, fecha);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(textView_observaciones_screen_exec_task.getText().toString())){
            String observaciones = textView_observaciones_screen_exec_task.getText().toString();
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE,observaciones);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar observaciones", Toast.LENGTH_LONG).show();
            }
        }
        if(!TextUtils.isEmpty(telefono1.getText().toString()) && !telefono1.getText().toString().contains("Añadir")){
            String tel1 = telefono1.getText().toString();
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono1,tel1);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar telefono1", Toast.LENGTH_LONG).show();
            }
        }
        if(!TextUtils.isEmpty(telefono2.getText().toString()) && !telefono2.getText().toString().contains("Añadir")){
            String tel2 = telefono2.getText().toString();
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono2,tel2);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar telefono2", Toast.LENGTH_LONG).show();
            }
        }

        String contador=null;
        try {
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador_devuelto)
            .trim().replace(" ", "");
//            Toast.makeText(Screen_Execute_Task.this, "Contador"+contador, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(numero_serie_viejo != null  && !TextUtils.isEmpty(mCurrentPhotoPath_foto_antes)
                && ((new File(mCurrentPhotoPath_foto_antes)).exists())){
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_antes_instalacion,
                        numero_serie_viejo +"_foto_antes_instalacion.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_antes_instalacion", Toast.LENGTH_LONG).show();
            }
        }
        if(numero_serie_viejo != null && !TextUtils.isEmpty(mCurrentPhotoPath_foto_lectura) && ((new File(mCurrentPhotoPath_foto_lectura)).exists()) ){
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_lectura,
                        numero_serie_viejo +"_foto_lectura.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_lectura", Toast.LENGTH_LONG).show();
            }
        }
        if(numero_serie_viejo != null && !TextUtils.isEmpty(mCurrentPhotoPath_foto_serie)
                && ((new File(mCurrentPhotoPath_foto_serie)).exists())){
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_numero_serie,
                        numero_serie_viejo +"_foto_numero_serie.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_numero_serie", Toast.LENGTH_LONG).show();
            }
        }
        if(contador != null && !TextUtils.isEmpty(mCurrentPhotoPath_foto_despues)
                && ((new File(mCurrentPhotoPath_foto_despues)).exists())){
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_despues_instalacion,
                        contador +"_foto_despues_instalacion.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_despues_instalacion", Toast.LENGTH_LONG).show();
            }
        }
//        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null){
//            try {
//                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public static String eliminarCaracteresAlFinal(String string){
        for(int n = string.length()-1; n >= 0; n--) {
            if(Character.isLetter(string.charAt(n))){
                string = string.substring(0,
                        string.length()-1);
            }else{
                break;
            }
        }
        return string;
    }
    private void llenarInformacionDeContador(String numero_serie_nuevo_string) {
        numero_serie_nuevo_string = eliminarCaracteresAlFinal(numero_serie_nuevo_string);
        try {
            if (team_or_personal_task_selection_screen_Activity.
                    dBcontadoresController.checkForTableExists()) {
                if (team_or_personal_task_selection_screen_Activity.
                        dBcontadoresController.checkIfContadorExists(numero_serie_nuevo_string)) {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                            dBcontadoresController.get_one_contador_from_Database(numero_serie_nuevo_string));

                    Log.e("Contador", jsonObject.toString());

                    String codigo_clase = jsonObject.getString(DBcontadoresController.codigo_clase).trim();
                    String clase = jsonObject.getString(DBcontadoresController.clase).trim();
                    String tipo_fluido = jsonObject.getString(DBcontadoresController.tipo_fluido).trim();
                    String tipo_radio = jsonObject.getString(DBcontadoresController.tipo_radio).trim();

                    String marca = "";
                    marca = marca + jsonObject.getString(DBcontadoresController.codigo_marca).trim() + " - ";
                    marca = marca + jsonObject.getString(DBcontadoresController.marca).trim() + " - ";
                    marca = marca + jsonObject.getString(DBcontadoresController.modelo).trim();
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.marca_devuelta, marca);
//                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.modelo_devuelto, jsonObject.getString(DBcontadoresController.marca || DBcontadoresController.modelo).trim());
                    String clase_y_codigo_clase = "";
                    clase_y_codigo_clase = clase_y_codigo_clase + codigo_clase + " - ";
                    clase_y_codigo_clase = clase_y_codigo_clase + clase;
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.TIPO_devuelto, clase_y_codigo_clase);
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.calibre_real, jsonObject.getString(DBcontadoresController.calibre_contador).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.largo_devuelto, jsonObject.getString(DBcontadoresController.longitud).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.TIPOFLUIDO_devuelto, jsonObject.getString(DBcontadoresController.tipo_fluido).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.tipoRadio_devuelto, jsonObject.getString(DBcontadoresController.tipo_radio).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.RUEDASDV, jsonObject.getString(DBcontadoresController.ruedas).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.CONTADOR_Prefijo_anno_devuelto, jsonObject.getString(DBcontadoresController.anno_o_prefijo).trim());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LECTOR_SNC) {//OJO preguntar sobre el numero de serie a cambiar
                String data_result = "";
                String contadorEscaneado ="";
                data_result = data.getStringExtra("result");
                if (!data_result.equals("null") && !data_result.isEmpty() && data_result!=null) {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                        ArrayList<String> tareas = new ArrayList<>();
                        try {
                            tareas = team_or_personal_task_selection_screen_Activity.
                                    dBtareasController.get_all_tareas_from_Database();
                            String numIn = Screen_Login_Activity.tarea_JSON.
                                    getString(DBtareasController.numero_interno).trim();
                            for (int i = 0; i < tareas.size(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(tareas.get(i));

                                    String numSerieDevuelto = jsonObject.getString(DBtareasController.
                                            numero_serie_contador_devuelto).trim();
                                    numSerieDevuelto = eliminarCaracteresAlFinal(numSerieDevuelto);

                                    contadorEscaneado = data_result;
                                    contadorEscaneado = eliminarCaracteresAlFinal(contadorEscaneado);

                                    String numInterno = jsonObject.
                                            getString(DBtareasController.numero_interno).trim();
                                    if (numSerieDevuelto.equals(contadorEscaneado) &&
                                            !numIn.equals(numInterno)) {
                                        openMessage("Advertencia", "Este contador " +
                                                "ya esta asignado " +
                                                "en otra instalacion realizada");
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    textView_serial_number_result.setVisibility(View.VISIBLE);
                    textView_serial_number_result.setText(contadorEscaneado);
                    contador = contadorEscaneado;
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.
                                numero_serie_contador_devuelto, contadorEscaneado);
                        llenarInformacionDeContador(contadorEscaneado);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "No pudo insertarse numero serie: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (requestCode == REQUEST_LECTOR_SNM) {
                String data_result = "";
                data_result = data.getStringExtra("result");
                if (!data_result.equals("null") && !data_result.isEmpty() && data_result != null) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_modulo, data_result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "No pudo insertarse numero serie de modulo: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                    textView_serial_number_module_result.setVisibility(View.VISIBLE);
                    textView_serial_number_module_result.setText(data_result);
                }
            }
            if (requestCode == REQUEST_ANOMALY) {
//                Toast.makeText(this, "Anomalia devuelta: ", Toast.LENGTH_LONG).show();
                String anomaly_code = "";
                String anomaly_string = "";
                anomaly_code = data.getStringExtra("anomaly_code");
                anomaly_string = data.getStringExtra("anomaly_string");
                if (!anomaly_code.equals("null") &&  !anomaly_code.equals("NULL") && !anomaly_code.isEmpty() && anomaly_code != null) {
                    if(!DBtareasController.tabla_model) {
                        try {
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.AREALIZAR_devuelta, anomaly_code);//esta es la anomalia devuelta
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.intervencion_devuelta, anomaly_string);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "No pudo insertarse numero serie de modulo: " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    if(anomaly_code.equals("null") &&  anomaly_code.equals("NULL") && anomaly_code.isEmpty() && anomaly_code == null){
                        anomaly_string = "";
                    }
                    textView_anomalia.setVisibility(View.VISIBLE);
                    textView_anomalia.setText(anomaly_code + " - " + anomaly_string);
                }
            }

            if(Screen_Login_Activity.movileModel){
                if (requestCode == CAM_REQUEST_INST_PHOTO) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                    if(bitmap!=null) {
                        String filename = saveBitmapImage(bitmap, "foto_antes_instalacion");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_foto_antes = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                            if (bitmap != null) {
                                instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                                instalation_photo_screen_exec_task.setImageBitmap(bitmap);
                                instalation_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                                lectura_editText.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +mCurrentPhotoPath_foto_antes, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_antes, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_READ_PHOTO) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                    if(bitmap!=null) {
                        String filename = saveBitmapImage(bitmap, "foto_lectura");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_foto_lectura = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                            if (bitmap != null) {
                                read_photo_screen_exec_task.setVisibility(View.VISIBLE);
                                read_photo_screen_exec_task.setImageBitmap(bitmap);
                                read_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                                lectura_editText.setVisibility(View.VISIBLE);
                                Log.e("Foto", "Lectura----************-------");
                            }else{
                                Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +mCurrentPhotoPath_foto_lectura, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_lectura, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_SN_PHOTO) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                    if(bitmap!=null) {
                        String filename = saveBitmapImage(bitmap, "foto_numero_serie");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_foto_serie = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                            if (bitmap != null) {
                                serial_number_photo_screen_exec_task.setVisibility(View.VISIBLE);
                                serial_number_photo_screen_exec_task.setImageBitmap(bitmap);
                                serial_number_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                            }else{
                                Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +mCurrentPhotoPath_foto_serie, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_serie, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_AFT_INT_PHOTO) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_despues);
                    if(bitmap!=null) {
                        String filename = saveBitmapImage(bitmap, "foto_despues_instalacion");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_foto_despues = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_despues);
                            if (bitmap != null) {
                                after_instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                                after_instalation_photo_screen_exec_task.setImageBitmap(bitmap);
                                after_instalation_photo_screen_exec_task.getLayoutParams().height = bitmap.getHeight() + 300;
                            }else{
                                Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +mCurrentPhotoPath_foto_despues, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_despues, Toast.LENGTH_LONG).show();
                    }
                }
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String saveBitmapImage(Bitmap bitmap, String key){
        try {
            bitmap = Screen_Login_Activity.scaleBitmap(bitmap);
            String numero_serie="";
            if(key.contains("despues")){
                 numero_serie = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador_devuelto)
                        .trim().replace(" ","");
            }
            else {
                 numero_serie = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                        .trim().replace(" ", "");
            }
            String file_full_name = numero_serie+"_"+key;
            //Toast.makeText(Screen_Incidence.this,"archivo: "+file_full_name, Toast.LENGTH_LONG).show();

            String numero_abonado = null;

            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();

            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa + "/fotos_tareas/"+ gestor + "/" +numero_abonado);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            else{
                File[] files = myDir.listFiles();
                for(int i=0; i< files.length; i++){
                    if(files[i].getName().contains(file_full_name) || files[i].getName().contains(numero_serie+"_foto_incidencia")){
                        files[i].delete();
                    }
                }
            }
            file_full_name+=".jpg";
            File file = new File(myDir, file_full_name);
            if (file.exists())
                file.delete();
            try {

                FileOutputStream out = new FileOutputStream(file);

                ExifInterface ei = new ExifInterface(file.getAbsolutePath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

//                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bitmap = rotateImage(bitmap, 90);
                        Log.e("Orientation", "90");
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bitmap = rotateImage(bitmap, 180);
                        Log.e("Orientation", "180");
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bitmap = rotateImage(bitmap, 270);
                        Log.e("Orientation", "180");
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        bitmap = bitmap;
//                        Log.e("Orientation", "normal");
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, MainActivity.COMPRESS_QUALITY, out);

                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Screen_Login_Activity.tarea_JSON.put(key, file_full_name);

            return file.getAbsolutePath();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public void openDialog(String title, String hint){
        tag=title;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(title, hint);
        dialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void pasarTexto(String wrote_text) throws JSONException {
        if(tag.contains("Telefono 1")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono1, wrote_text);
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+wrote_text+"#");
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono1.setText(wrote_text);
            }
        }else if(tag.contains("Telefono 2")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono2, wrote_text);
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+wrote_text+"#");
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono2.setText(wrote_text);
            }
        }else if(tag.contains("Mensaje libre")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, wrote_text);
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.observaciones_devueltas, wrote_text);
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.observaciones, wrote_text);
                textView_observaciones_screen_exec_task.setText(wrote_text);
            }
        }else if(tag.equals("Número Serie")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                if (!(TextUtils.isEmpty(wrote_text))) {
                    if (!wrote_text.equals("null") && !wrote_text.isEmpty() && wrote_text != null) {
                        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                            ArrayList<String> tareas = new ArrayList<>();
                            try {
                                tareas = team_or_personal_task_selection_screen_Activity.
                                        dBtareasController.get_all_tareas_from_Database();
                                String numIn = Screen_Login_Activity.tarea_JSON.
                                        getString(DBtareasController.numero_interno).trim();
                                for (int i = 0; i < tareas.size(); i++) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(tareas.get(i));
                                        String numSerieDevuelto = jsonObject.getString(DBtareasController.
                                                numero_serie_contador_devuelto).trim();
                                        numSerieDevuelto = eliminarCaracteresAlFinal(numSerieDevuelto);
                                        wrote_text = eliminarCaracteresAlFinal(wrote_text);

                                        String numInterno = jsonObject.getString
                                                (DBtareasController.numero_interno).trim();
                                        if (numSerieDevuelto.equals(wrote_text) &&
                                                !numIn.equals(numInterno)) {
                                            openMessage("Advertencia", "Este contador " +
                                                    "ya esta asignado " +
                                                    "en otra instalacion realizada");
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.
                                numero_serie_contador_devuelto, wrote_text);
                        llenarInformacionDeContador(wrote_text);
                        textView_serial_number_result.setVisibility(View.VISIBLE);
                        textView_serial_number_result.setText(wrote_text);
                    }
                }
            }
        }else if(tag.equals("Número Serie de Modulo")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                if (!(TextUtils.isEmpty(wrote_text))) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_modulo, wrote_text);
                    textView_serial_number_module_result.setVisibility(View.VISIBLE);
                    textView_serial_number_module_result.setText(wrote_text);
                }
            }
        }else if(tag.equals("Código de geolocalización")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                if (!(TextUtils.isEmpty(wrote_text))) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.codigo_de_geolocalizacion, wrote_text);
                    textView_codigo_geolocalization_screen_exec_task.setVisibility(View.VISIBLE);
                    textView_codigo_geolocalization_screen_exec_task.setText(wrote_text);
                }
            }
        }else if(tag.equals("Lectura")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                if (!(TextUtils.isEmpty(wrote_text))) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, wrote_text);
                    lectura_editText.setVisibility(View.VISIBLE);
                    lectura_editText.setText(wrote_text);
                }
            }
        }
    }

    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Execute_Task.this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
            finishesThisClass();
            return;
        }
        else {
            String numero_abonado = "";
            try {
                numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();

                String file_name = null, image_file;

                file_name = images_files_names.get(images_files.size() - 1);
                images_files_names.remove(images_files.size() - 1);
                image_file = images_files.get(images_files.size() - 1);
                images_files.remove(images_files.size() - 1);
                String type = "upload_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(getPhotoUserLocal(image_file)), file_name, numero_abonado);

            } catch (JSONException e) {
                images_files.clear();
                e.printStackTrace();
                Toast.makeText(this, "Error obteniendo numero_abonado\n"+ e.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
    }
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "update_tarea") {
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(Screen_Execute_Task.this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(Screen_Execute_Task.this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                } else {
                    if (result.contains("not success")) {
                        Toast.makeText(Screen_Execute_Task.this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();

                    } else {

                        if(result.contains("success ok")) {
                            Toast.makeText(this, "Datos guardados correctamente en el servidor", Toast.LENGTH_LONG).show();
                        }
                        String contador=null;
                        try {
                            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_antes)  && ((new File(mCurrentPhotoPath_foto_antes)).exists())) {
                            images_files.add(mCurrentPhotoPath_foto_antes);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(numero_serie_viejo.trim().replace(" ", "")+"_foto_antes_instalacion.jpg");
                            }
                        }
                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_lectura) && ((new File(mCurrentPhotoPath_foto_lectura)).exists())) {
                            images_files.add(mCurrentPhotoPath_foto_lectura);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(numero_serie_viejo.trim().replace(" ", "")+"_foto_lectura.jpg");
                            }
                        }
                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_serie) && ((new File(mCurrentPhotoPath_foto_serie)).exists())) {
                            images_files.add(mCurrentPhotoPath_foto_serie);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(numero_serie_viejo.trim().replace(" ", "")+"_foto_numero_serie.jpg");
                            }
                        }
                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_despues) && ((new File(mCurrentPhotoPath_foto_despues)).exists())) {
                            images_files.add(mCurrentPhotoPath_foto_despues);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(contador.trim().replace(" ", "")+"_foto_despues_instalacion.jpg");
                            }
                        }
                        if(!images_files_names.isEmpty() && !images_files.isEmpty()) {
                            showRingDialog("Subiedo fotos");
                            uploadPhotos();
                        }else{
                            finishesThisClass();
                        }
                    }
                }
            }
        }
        else if(type == "upload_image"){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Execute_Task.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                uploadPhotos();
                //showRingDialog("Validando registro...");
            }
        }
    }


    private void dispatchTakePictureIntent(int request) throws JSONException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                if(request == CAM_REQUEST_INST_PHOTO){
                    photoFile = createImageFile("foto_antes_instalacion");
                }
                else if(request == CAM_REQUEST_SN_PHOTO){
                    photoFile = createImageFile("foto_numero_serie");
                }
                else if(request == CAM_REQUEST_READ_PHOTO){
                    photoFile = createImageFile("foto_lectura");
                }
                else if(request == CAM_REQUEST_AFT_INT_PHOTO){
                    photoFile = createImageFile("foto_despues_instalacion");
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "No se pudo crear el archivo", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.luisreyes.proyecto_aguas.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, request);
            }
        }
    }

    private File createImageFile(String foto_x) throws IOException, JSONException {
        // Create an image file name

        String imageFileName = null;
        String image = "";

        if(foto_x.contains("despues")){
            image = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador_devuelto)
                    .trim().replace(" ", "") + "_" + foto_x;
        }else {
            image = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    .trim().replace(" ", "") + "_" + foto_x;
        }

        String numero_abonado = null;
        numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();

        File image_file=null;
        String gestor = null;
        gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
        if(!Screen_Login_Activity.checkStringVariable(gestor)){
            gestor = "Sin_Gestor";
        }
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa + "/fotos_tareas/"+ gestor + "/" +numero_abonado);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File[] files = storageDir.listFiles();
        for(int i=0; i< files.length;i++){
            if(files[i].getName().contains(image)){
                files[i].delete();
            }
        }
        image_file = File.createTempFile(
                image,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if(foto_x.contains("antes")){
            mCurrentPhotoPath_foto_antes = image_file.getAbsolutePath();
        }
        else if(foto_x.contains("lectura")){
            mCurrentPhotoPath_foto_lectura = image_file.getAbsolutePath();
        }
        else if(foto_x.contains("serie")){
            mCurrentPhotoPath_foto_serie= image_file.getAbsolutePath();
        }
        else if(foto_x.contains("despues")){
            mCurrentPhotoPath_foto_despues= image_file.getAbsolutePath();
        }
        //etname.setText(image);
        return image_file;
    }

    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
//                bitmap =Bitmap.createScaledBitmap(MediaStore.Images.Media
//                        .getBitmap(this.getContentResolver(), Uri.fromFile(file)), 512, 512, true);
                bitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                return bitmap;
            } else {
                return null;
            }
        }else{
            return null;
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

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Execute_Task.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    public static void hideRingDialog(){
        try {
            if(progressDialog!=null) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hideRingDialog", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Contactar:
//                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
                openMessage("Contactar",
                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
                        "\n   Michel Morales: mraguas@gmail.com"
                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.Principal:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent open_screen= new Intent(Screen_Execute_Task.this,
                        team_or_personal_task_selection_screen_Activity.class);
                startActivity(open_screen);
                clearvariables();
                finish();
                return true;

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                team_or_personal_task_selection_screen_Activity.from_team_or_personal =
                        team_or_personal_task_selection_screen_Activity.FROM_TEAM;
//                Intent open_screen= new Intent(Screen_Execute_Task.this, Screen_Table_Team.class);
//                startActivity(open_screen);
                finishesThisClass();
                return true;

            case R.id.Configuracion:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
                return true;
            case R.id.Info_Tarea:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void onBackPressed() {
        if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
            Toast.makeText(getApplicationContext(), "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
        }
        clearvariables();
        Intent intent_open_screen_info_counter=null;
        if (team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_BATTERY) {
            intent_open_screen_info_counter= new Intent(this, Screen_Battery_counter.class);
        } else if(team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_UNITY){
            intent_open_screen_info_counter= new Intent(this, Screen_Unity_Counter.class);
        }
        if(intent_open_screen_info_counter!=null) {
            startActivity(intent_open_screen_info_counter);
        }
        Screen_Execute_Task.this.finish();
        super.onBackPressed();
    }

    public void finishesThisClass(){

        Intent openTableActivity = null;
        if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_TEAM) {
            openTableActivity = new Intent(this, Screen_Table_Team.class);
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL){
            openTableActivity = new Intent(this, Screen_Table_Personal.class);
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT){
            openTableActivity = new Intent(this, Screen_Filter_Results.class);
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS){
            openTableActivity = new Intent(this, Screen_Filter_Tareas.class);
        }
        if(openTableActivity!= null) {
            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivityIfNeeded(openTableActivity, 0);
        }
        clearvariables();
        finish();
    }
    public void clearvariables(){
        mCurrentPhotoPath_foto_antes = "";
        mCurrentPhotoPath_foto_lectura= "";
        mCurrentPhotoPath_foto_despues = "";
        mCurrentPhotoPath_foto_serie = "";
    }
}
