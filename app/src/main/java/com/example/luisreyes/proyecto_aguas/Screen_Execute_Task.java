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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    anomaly_button,
            button_guardar_datos;

    private Button button_validate_screen_exec_task,
            button_geolocalization_screen_exec_task;

    private ImageView imageView_edit_phone1_screen_exec_task,
            imageView_edit_phone2_screen_exec_task,
            imageView_edit_serial_number_screen_exec_task,
            imageView_edit_serial_number_module_screen_exec_task,
            button_codigo_geolocalization_screen_exec_task,
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
    private String lectura_string;

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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        images_files= new ArrayList<>();
        images_files_names =new ArrayList<>();

        imageView_edit_serial_number_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_serial_number_screen_exec_task);
        imageView_edit_serial_number_module_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_serial_number_module_screen_exec_task);
        imageView_edit_phone1_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_phone1_screen_exec_task);
        imageView_edit_phone2_screen_exec_task= (ImageView)findViewById(R.id.imageView_edit_phone2_screen_exec_task);

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

        button_scan_serial_number_screen_exec_task= (Button)findViewById(R.id.button_scan_serial_number_screen_exec_task);
        button_scan_module_screen_exec_task= (Button)findViewById(R.id.button_scan_module_screen_exec_task);
        button_validate_screen_exec_task          = (Button)findViewById(R.id.button_validate_screen_exec_task);
        button_guardar_datos = (Button)findViewById(R.id.button_guardar_datos_screen_exec_task);
        observaciones_button = (ImageView)findViewById(R.id.button_observations_screen_exec_task);
        anomaly_button = (Button)findViewById(R.id.button_anomalia_screen_exec_task);
        textView_observaciones_screen_exec_task = (TextView)findViewById(R.id.textView_observaciones_screen_exec_task);
        button_geolocalization_screen_exec_task= (Button)findViewById(R.id.button_geolocalization_screen_exec_task);

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
                    DBtareasController.observaciones));
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
                        openDialog("Numero Serie","Escriba aqui número serie");
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
                        openDialog("Número Serie","Escriba aqui número serie");
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
                        openDialog("Número Serie de Modulo","Escriba número serie de modulo");
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
                        openDialog("Código de geolocalización","...");
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
                        openDialog("Numero Serie de Modulo","Escriba número serie de modulo");
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
                        openDialog("Observaciones","Observación");
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
                openDialog("Telefono 1","547076...");
            }
        });
        imageView_edit_phone1_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 1","547076...");
            }
        });
        imageView_edit_phone2_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 2","547076...");
            }
        });
        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 1","547076...");
            }
        });
        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 2","547076...");
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
                        if(!TextUtils.isEmpty(lectura_editText.getText().toString())) {
                            lectura_introducida = lectura_editText.getText().toString();
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
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
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
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
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
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
                    intent_camera.putExtra("contador", numero_serie_viejo);
                    startActivityForResult(intent_camera, CAM_REQUEST_SN_PHOTO);
                }
            }
        });
        button_after_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
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
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_AFT_INT_PHOTO);
                }
            }
        });
    }

    public void onGuardar_Datos(){
        guardar_en_JSON_modificaciones();
        if(!(TextUtils.isEmpty(lectura_editText.getText()))) {
            if (!lectura_string.equals("null") && !lectura_string.equals("NULL") && !lectura_string.isEmpty()) {
                String lectura_actual = lectura_editText.getText().toString();
                if (Integer.parseInt(lectura_actual) >= Integer.parseInt(lectura_string)) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_string);
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_actual);
                        saveData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Screen_Execute_Task.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Screen_Execute_Task.this, "La lectura del contador debe ser mayor que la ultima registrada", Toast.LENGTH_LONG).show();
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
        if(checkConection()) {
            showRingDialog("Guardando Datos...");
            String type = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type);
        } else{
            if(!error) {
                Toast.makeText(this, "No hay conexion se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
                Intent intent_open_task_or_personal_screen = new Intent(Screen_Execute_Task.this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(intent_open_task_or_personal_screen);
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
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.observaciones_devueltas,observaciones);
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
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LECTOR_SNC) {//OJO preguntar sobre el numero de serie a cambiar
                String data_result = "";
                data_result = data.getStringExtra("result");
                if (!data_result.equals("null") && !data_result.isEmpty() && data_result!=null) {
                    textView_serial_number_result.setVisibility(View.VISIBLE);
                    textView_serial_number_result.setText(data_result);
                    contador = data_result;
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_contador_devuelto, data_result);
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
            else {
                if (requestCode == CAM_REQUEST_INST_PHOTO) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_foto_antes = data.getStringExtra("photo_path");
                        Bitmap bitmap_foto_antes_instalacion = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                        if (bitmap_foto_antes_instalacion != null) {
                            bitmap_foto_antes_instalacion = Bitmap.createScaledBitmap(bitmap_foto_antes_instalacion, 960, 1280, true);
                            instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                            instalation_photo_screen_exec_task.setImageBitmap(bitmap_foto_antes_instalacion);
                            lectura_editText.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_antes, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                if (requestCode == CAM_REQUEST_READ_PHOTO) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_foto_lectura = data.getStringExtra("photo_path");
                        Bitmap bitmap_foto_lectura = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                        if (bitmap_foto_lectura != null) {
                            bitmap_foto_lectura = Bitmap.createScaledBitmap(bitmap_foto_lectura, 960, 1280, true);
                            read_photo_screen_exec_task.setVisibility(View.VISIBLE);
                            read_photo_screen_exec_task.setImageBitmap(bitmap_foto_lectura);
                        }else{
                            Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_lectura, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                if (requestCode == CAM_REQUEST_SN_PHOTO) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_foto_serie = data.getStringExtra("photo_path");
                        Bitmap bitmap_foto_numero_serie = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                        if (bitmap_foto_numero_serie != null) {
                            bitmap_foto_numero_serie = Bitmap.createScaledBitmap(bitmap_foto_numero_serie, 960, 1280, true);
                            serial_number_photo_screen_exec_task.setVisibility(View.VISIBLE);
                            serial_number_photo_screen_exec_task.setImageBitmap(bitmap_foto_numero_serie);
                        }else{
                            Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_serie, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                if (requestCode == CAM_REQUEST_AFT_INT_PHOTO) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_foto_despues = data.getStringExtra("photo_path");
                        Bitmap bitmap_foto_despues_instalacion = getPhotoUserLocal(mCurrentPhotoPath_foto_despues);
                        if (bitmap_foto_despues_instalacion != null) {
                            bitmap_foto_despues_instalacion = Bitmap.createScaledBitmap(bitmap_foto_despues_instalacion, 960, 1280, true);
                            after_instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                            after_instalation_photo_screen_exec_task.setImageBitmap(bitmap_foto_despues_instalacion);
                        }else{
                            Toast.makeText(this,"No se encuentra foto: " +mCurrentPhotoPath_foto_despues, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

    }

    private String saveBitmapImage(Bitmap bitmap, String key){
        try {
            bitmap = Bitmap.createScaledBitmap(bitmap, 960, 1280, true);
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

            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"+numero_abonado);

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
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+wrote_text+"#");
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono1.setText(wrote_text);
            }
        }else if(tag.contains("Telefono 2")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono2, wrote_text);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+wrote_text+"#");
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono2.setText(wrote_text);
            }
        }else if(tag.contains("Observaciones")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.observaciones_devueltas, wrote_text);
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                textView_observaciones_screen_exec_task.setText(wrote_text);
            }
        }else if(tag.equals("Número Serie")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                if (!(TextUtils.isEmpty(wrote_text))) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_contador_devuelto, wrote_text);
                    textView_serial_number_result.setVisibility(View.VISIBLE);
                    textView_serial_number_result.setText(wrote_text);
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
        }
    }

    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Execute_Task.this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
            Intent intent_open_task_or_personal_screen = new Intent(Screen_Execute_Task.this, team_or_personal_task_selection_screen_Activity.class);
            startActivity(intent_open_task_or_personal_screen);
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
                            Intent intent_open_task_or_personal_screen = new Intent(Screen_Execute_Task.this, team_or_personal_task_selection_screen_Activity.class);
                            startActivity(intent_open_task_or_personal_screen);
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

//    private void dispatchTakePictureIntent(int request) throws JSONException {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                if(request == CAM_REQUEST_INST_PHOTO){
//                    photoFile = createImageFile("foto_antes_instalacion");
//                }
//                else if(request == CAM_REQUEST_SN_PHOTO){
//                    photoFile = createImageFile("foto_numero_serie");
//                }
//                else if(request == CAM_REQUEST_READ_PHOTO){
//                    photoFile = createImageFile("foto_lectura");
//                }
//                else if(request == CAM_REQUEST_AFT_INT_PHOTO){
//                    photoFile = createImageFile("foto_despues_instalacion");
//                }
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Toast.makeText(this, "No se pudo crear el archivo", Toast.LENGTH_LONG).show();
//            }
//            // Continue only if the File was successfully created
//            Camera camera = Camera.open();
//            Camera.Parameters params = camera.getParameters();
//            List<Camera.Size> sizes = params.getSupportedPictureSizes();
//
//            Toast.makeText(this, String.valueOf(sizes.get(sizes.size()-3).height) + "  " + String.valueOf(sizes.get(sizes.size()-3).width), Toast.LENGTH_LONG).show();
//
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.luisreyes.proyecto_aguas.fileprovider",
//                        photoFile);
//                //takePictureIntent.setType("image/*");
//                takePictureIntent.putExtra("crop", true);
//                takePictureIntent.putExtra("outputX", 240);
//                takePictureIntent.putExtra("outputY", 320);
////                takePictureIntent.putExtra("aspectX", 1);
////                takePictureIntent.putExtra("aspectY", 1);
//                takePictureIntent.putExtra("scale", true);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                takePictureIntent.putExtra("outputFormat",
//                        Bitmap.CompressFormat.JPEG.toString());
//                startActivityForResult(takePictureIntent, request);
//            }
//        //}
//    }

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
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"+numero_abonado);
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
        if(progressDialog!=null)
        progressDialog.dismiss();
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

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
        finishesThisClass();
        super.onBackPressed();
    }

    public void finishesThisClass(){
        mCurrentPhotoPath_foto_antes = "";
        mCurrentPhotoPath_foto_lectura= "";
        mCurrentPhotoPath_foto_despues = "";
        mCurrentPhotoPath_foto_serie = "";
        finish();
    }
}
