package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Screen_Edit_ITAC extends AppCompatActivity implements Dialog.DialogListener{

    private static final int CANVAS_REQUEST_EDIT_ITAC = 3339;

    private Button button_guardar_datos_screen_edit_itac,
            button_geolocalization_screen_edit_itac,
            button_editar_firma_cliente_screen_edit_itac,
            button_photo_1_screen_edit_itac,
            button_photo_2_screen_edit_itac,
            button_photo_3_screen_edit_itac,
            button_photo_4_screen_edit_itac,
            button_photo_5_screen_edit_itac,
            button_photo_6_screen_edit_itac,
            button_photo_7_screen_edit_itac,
            button_photo_8_screen_edit_itac;

    private Spinner spinner_tipo_contacto,
            spinner_gestor_screen_edit_itac;

    private LinearLayout layout_empresa_screen_edit_itac,
            layout_presidente_screen_edit_itac,
            layout_encargado_screen_edit_itac,
            linearLayout_gestor_screen_edit_itac;

    private ImageView imageView_photo_1_screen_edit_itac,
            imageView_photo_2_screen_edit_itac,
            imageView_photo_3_screen_edit_itac,
            imageView_photo_4_screen_edit_itac,
            imageView_photo_5_screen_edit_itac,
            imageView_photo_6_screen_edit_itac,
            imageView_photo_7_screen_edit_itac,
            imageView_photo_8_screen_edit_itac,
                    button_direccion_screen_edit_itac,
                    button_acceso_screen_edit_itac,
                    button_descripcion_screen_edit_itac,
            imageView_firma_screen_edit_itac,
            button_cod_emplazamiento_screen_edit_itac,
            imageView_edit_nombre_firmante_screen_edit_itac,
            imageView_edit_numero_carnet_firmante_screen_edit_itac;

    private EditText editText_descripcion_photo_1_screen_edit_itac,
            editText_descripcion_photo_2_screen_edit_itac,
            editText_descripcion_photo_3_screen_edit_itac,
            editText_descripcion_photo_4_screen_edit_itac,
            editText_descripcion_photo_5_screen_edit_itac,
            editText_descripcion_photo_6_screen_edit_itac,
            editText_descripcion_photo_7_screen_edit_itac,
            editText_descripcion_photo_8_screen_edit_itac,

            editText_nombre_empresa_screen_edit_itac,
            editText_direccion_oficina_screen_edit_itac,
            editText_nombre_responsable_screen_edit_itac,
            editText_telefono_fijo_screen_edit_itac,
            editText_telefono_movil_screen_edit_itac,
            editText_correo_screen_edit_itac,
                    editText_nombre_presidente_screen_edit_itac,
                    editText_telefono_fijo_presidente_screen_edit_itac,
                    editText_telefono_movil_presidente_screen_edit_itac,
                    editText_correo_presidente_screen_edit_itac,
            editText_nombre_encargado_screen_edit_itac,
            editText_telefono_fijo_encargado_screen_edit_itac,
            editText_telefono_movil_encargado_screen_edit_itac,
            editText_correo_encargado_screen_edit_itac;

    private TextView textView_acceso_screen_edit_itac,
            textView_descripcion_screen_edit_itac,
            textView_direccion_screen_edit_itac,
            textView_cod_emplazamiento_screen_edit_itac,
            textView_nombre_firmante_screen_edit_itac,
            textView_numero_carnet_firmante_screen_edit_itac;

    private static final int CAM_REQUEST_PHOTO_1 = 1311;
    private static final int CAM_REQUEST_PHOTO_2 = 1312;
    private static final int CAM_REQUEST_PHOTO_3 = 1313;
    private static final int CAM_REQUEST_PHOTO_4 = 1314;
    private static final int CAM_REQUEST_PHOTO_5 = 1315;
    private static final int CAM_REQUEST_PHOTO_6 = 1316;
    private static final int CAM_REQUEST_PHOTO_7 = 1317;
    private static final int CAM_REQUEST_PHOTO_8 = 1318;

    public static HashMap<Integer, String> foto_requests;
    public static HashMap<Integer, String> foto_requests_paths;
    private String tag="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_edit_itac);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        imageView_edit_nombre_firmante_screen_edit_itac = (ImageView) findViewById(R.id.imageView_edit_nombre_firmante_screen_edit_itac);
        imageView_edit_numero_carnet_firmante_screen_edit_itac = (ImageView) findViewById(R.id.imageView_edit_numero_carnet_firmante_screen_edit_itac);

        textView_nombre_firmante_screen_edit_itac= (TextView) findViewById(R.id.textView_nombre_firmante_screen_edit_itac);
        textView_numero_carnet_firmante_screen_edit_itac= (TextView) findViewById(R.id.textView_numero_carnet_firmante_screen_edit_itac);

        textView_acceso_screen_edit_itac = (TextView) findViewById(R.id.textView_acceso_screen_edit_itac);
        textView_descripcion_screen_edit_itac = (TextView) findViewById(R.id.textView_descripcion_screen_edit_itac);
        textView_direccion_screen_edit_itac = (TextView) findViewById(R.id.textView_direccion_screen_edit_itac);
        textView_cod_emplazamiento_screen_edit_itac  = (TextView) findViewById(R.id.textView_cod_emplazamiento_screen_edit_itac);

        editText_nombre_empresa_screen_edit_itac = (EditText) findViewById(R.id.editText_nombre_empresa_screen_edit_itac);
        editText_direccion_oficina_screen_edit_itac = (EditText) findViewById(R.id.editText_direccion_oficina_screen_edit_itac);
        editText_nombre_responsable_screen_edit_itac = (EditText) findViewById(R.id.editText_nombre_responsable_screen_edit_itac);
        editText_telefono_fijo_screen_edit_itac = (EditText) findViewById(R.id.editText_telefono_fijo_screen_edit_itac);
        editText_telefono_movil_screen_edit_itac = (EditText) findViewById(R.id.editText_telefono_movil_screen_edit_itac);
        editText_correo_screen_edit_itac = (EditText) findViewById(R.id.editText_correo_screen_edit_itac);
        editText_nombre_presidente_screen_edit_itac = (EditText) findViewById(R.id.editText_nombre_presidente_screen_edit_itac);
        editText_telefono_fijo_presidente_screen_edit_itac = (EditText) findViewById(R.id.editText_telefono_fijo_presidente_screen_edit_itac);
        editText_telefono_movil_presidente_screen_edit_itac = (EditText) findViewById(R.id.editText_telefono_movil_presidente_screen_edit_itac);
        editText_correo_presidente_screen_edit_itac = (EditText) findViewById(R.id.editText_correo_presidente_screen_edit_itac);
        editText_nombre_encargado_screen_edit_itac = (EditText) findViewById(R.id.editText_nombre_encargado_screen_edit_itac);
        editText_telefono_fijo_encargado_screen_edit_itac = (EditText) findViewById(R.id.editText_telefono_fijo_encargado_screen_edit_itac);
        editText_telefono_movil_encargado_screen_edit_itac = (EditText) findViewById(R.id.editText_telefono_movil_encargado_screen_edit_itac);
        editText_correo_encargado_screen_edit_itac = (EditText) findViewById(R.id.editText_correo_encargado_screen_edit_itac);

        button_direccion_screen_edit_itac = (ImageView) findViewById(R.id.button_direccion_screen_edit_itac);
        button_acceso_screen_edit_itac = (ImageView) findViewById(R.id.button_acceso_screen_edit_itac);
        button_descripcion_screen_edit_itac = (ImageView) findViewById(R.id.button_descripcion_screen_edit_itac);
        button_cod_emplazamiento_screen_edit_itac = (ImageView) findViewById(R.id.button_cod_emplazamiento_screen_edit_itac);

        imageView_photo_1_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_1_screen_edit_itac);
        imageView_photo_2_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_2_screen_edit_itac);
        imageView_photo_3_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_3_screen_edit_itac);
        imageView_photo_4_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_4_screen_edit_itac);
        imageView_photo_5_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_5_screen_edit_itac);
        imageView_photo_6_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_6_screen_edit_itac);
        imageView_photo_7_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_7_screen_edit_itac);
        imageView_photo_8_screen_edit_itac = (ImageView) findViewById(R.id.imageView_photo_8_screen_edit_itac);
        imageView_firma_screen_edit_itac = (ImageView) findViewById(R.id.imageView_firma_screen_edit_itac);

        button_editar_firma_cliente_screen_edit_itac = (Button) findViewById(R.id.button_editar_firma_cliente_screen_edit_itac);
        button_photo_1_screen_edit_itac = (Button) findViewById(R.id.button_photo_1_screen_edit_itac);
        button_photo_2_screen_edit_itac = (Button) findViewById(R.id.button_photo_2_screen_edit_itac);
        button_photo_3_screen_edit_itac = (Button) findViewById(R.id.button_photo_3_screen_edit_itac);
        button_photo_4_screen_edit_itac = (Button) findViewById(R.id.button_photo_4_screen_edit_itac);
        button_photo_5_screen_edit_itac = (Button) findViewById(R.id.button_photo_5_screen_edit_itac);
        button_photo_6_screen_edit_itac = (Button) findViewById(R.id.button_photo_6_screen_edit_itac);
        button_photo_7_screen_edit_itac = (Button) findViewById(R.id.button_photo_7_screen_edit_itac);
        button_photo_8_screen_edit_itac = (Button) findViewById(R.id.button_photo_8_screen_edit_itac);
        button_geolocalization_screen_edit_itac = (Button) findViewById(R.id.button_geolocalization_screen_edit_itac);

        editText_descripcion_photo_1_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_1_screen_edit_itac);
        editText_descripcion_photo_2_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_2_screen_edit_itac);
        editText_descripcion_photo_3_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_3_screen_edit_itac);
        editText_descripcion_photo_4_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_4_screen_edit_itac);
        editText_descripcion_photo_5_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_5_screen_edit_itac);
        editText_descripcion_photo_6_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_6_screen_edit_itac);
        editText_descripcion_photo_7_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_7_screen_edit_itac);
        editText_descripcion_photo_8_screen_edit_itac = (EditText) findViewById(R.id.editText_descripcion_photo_8_screen_edit_itac);

        
        button_guardar_datos_screen_edit_itac =(Button) findViewById(R.id.button_guardar_datos_screen_edit_itac);
        spinner_tipo_contacto =(Spinner) findViewById(R.id.spinner_tipo_contacto_screen_edit_itac);
        spinner_gestor_screen_edit_itac =(Spinner) findViewById(R.id.spinner_gestor_screen_edit_itac);
        layout_empresa_screen_edit_itac =(LinearLayout) findViewById(R.id.layout_empresa_screen_edit_itac);
        layout_presidente_screen_edit_itac =(LinearLayout) findViewById(R.id.layout_presidente_screen_edit_itac);
        layout_encargado_screen_edit_itac =(LinearLayout) findViewById(R.id.layout_encargado_screen_edit_itac);
        linearLayout_gestor_screen_edit_itac =(LinearLayout) findViewById(R.id.linearLayout_gestor_screen_edit_itac);

        foto_requests_paths = new HashMap<>();
        foto_requests = new HashMap<>();
        foto_requests.put(CAM_REQUEST_PHOTO_1, "foto_1");
        foto_requests.put(CAM_REQUEST_PHOTO_2, "foto_2");
        foto_requests.put(CAM_REQUEST_PHOTO_3, "foto_3");
        foto_requests.put(CAM_REQUEST_PHOTO_4, "foto_4");
        foto_requests.put(CAM_REQUEST_PHOTO_5, "foto_5");
        foto_requests.put(CAM_REQUEST_PHOTO_6, "foto_6");
        foto_requests.put(CAM_REQUEST_PHOTO_7, "foto_7");
        foto_requests.put(CAM_REQUEST_PHOTO_8, "foto_8");

        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("ADMINISTRACIÓN");
        lista_desplegable.add("PRESIDENTE O VECINO COLABORADOR");
        lista_desplegable.add("ENCARGADO O COSERJE");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable);
        spinner_tipo_contacto.setAdapter(arrayAdapter_spinner);

        try {
            int id = Screen_Login_Activity.itac_JSON.getInt(DBitacsController.id);
            if (id > 0) {
                button_cod_emplazamiento_screen_edit_itac.setVisibility(View.GONE);
                linearLayout_gestor_screen_edit_itac.setVisibility(View.GONE);
            }else{
                setGestores();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imageView_edit_numero_carnet_firmante_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("Identificación Firmante", "...");
                    }
                });
                imageView_edit_numero_carnet_firmante_screen_edit_itac.startAnimation(myAnim);
            }
        });
        imageView_edit_nombre_firmante_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("Nombre Firmante", "...");
                    }
                });
                imageView_edit_nombre_firmante_screen_edit_itac.startAnimation(myAnim);
            }
        });
        spinner_tipo_contacto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item_selected = spinner_tipo_contacto
                        .getSelectedItem().toString();
                if(!item_selected.isEmpty() && item_selected!=null) {
                    showCurrentContact(item_selected);
                    Log.e("Selecciona Tipo", item_selected);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        button_editar_firma_cliente_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        Intent intent_open_screen_client_sign = new Intent(Screen_Edit_ITAC.this, Screen_Draw_Canvas.class);
                        intent_open_screen_client_sign.putExtra("class_caller", CANVAS_REQUEST_EDIT_ITAC);
                        startActivityForResult(intent_open_screen_client_sign, CANVAS_REQUEST_EDIT_ITAC);
                    }
                });
                button_editar_firma_cliente_screen_edit_itac.startAnimation(myAnim);
            }
        });
        button_geolocalization_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Edit_ITAC.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent intent_open_screen_mapa= new Intent(Screen_Edit_ITAC.this, permission_itac.class);
                        startActivity(intent_open_screen_mapa);
                    }
                });
                button_geolocalization_screen_edit_itac.startAnimation(myAnim);
            }
        });
        button_guardar_datos_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Edit_ITAC.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        if(guardarDatos()) {
                            finishThisClass();
                        }
                    }
                });
                button_guardar_datos_screen_edit_itac.startAnimation(myAnim);
            }
        });

        button_photo_1_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());

                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo_2_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());

                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo_3_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());

                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo_4_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());

                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo_5_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());

                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_5);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo_6_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());

                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_6);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo_7_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());

                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_7);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo_8_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                try {
                    dispatchTakePictureIntent(CAM_REQUEST_PHOTO_8);
                } catch (JSONException e) {
                    e.printStackTrace();
                }                
            }
        });

        button_direccion_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Edit_ITAC.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        openDialog("Dirección",textView_direccion_screen_edit_itac.getText().toString());
                    }
                });
                button_direccion_screen_edit_itac.startAnimation(myAnim);
            }
        });
        button_acceso_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Edit_ITAC.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        openDialog("Acceso",textView_acceso_screen_edit_itac.getText().toString());
                    }
                });
                button_acceso_screen_edit_itac.startAnimation(myAnim);
            }
        });
        button_descripcion_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Edit_ITAC.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        openDialog("Descripción",textView_descripcion_screen_edit_itac.getText().toString());
                    }
                });
                button_descripcion_screen_edit_itac.startAnimation(myAnim);
            }
        });
        button_cod_emplazamiento_screen_edit_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Edit_ITAC.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        openDialog("Código de Emplazamiento",textView_cod_emplazamiento_screen_edit_itac.getText().toString());
                    }
                });
                button_cod_emplazamiento_screen_edit_itac.startAnimation(myAnim);
            }
        });
        populateView();
        addPhotoLocal();
    }


    private void setGestores() {
        if(team_or_personal_task_selection_screen_Activity.dBgestoresController!=null) {
            if (team_or_personal_task_selection_screen_Activity.dBgestoresController.databasefileExists(this)) {
                if (team_or_personal_task_selection_screen_Activity.dBgestoresController.checkForTableExists()) {
                    if (team_or_personal_task_selection_screen_Activity.dBgestoresController.countTableGestores() > 0) {
                        ArrayList<String> gestores = new ArrayList<>();
                        try {
                            ArrayList<String> json_gestores = team_or_personal_task_selection_screen_Activity.
                                    dBgestoresController.get_all_gestores_from_Database();
                            for (int i = 0; i < json_gestores.size(); i++) {
                                try {
                                    JSONObject jsonObject = new JSONObject(json_gestores.get(i));
                                    String gestor = "";
                                    try {
                                        gestor = jsonObject.getString(DBgestoresController.gestor).trim();
                                        if (Screen_Login_Activity.checkStringVariable(gestor)) {
                                            if (!gestores.contains(gestor)) {
                                                gestores.add(gestor);
                                            }
                                        }
                                    }catch (JSONException e) {
                                        Log.e("Excepcion gestor", "No se pudo obtener gestor\n" + e.toString());
                                        e.printStackTrace();
                                    }
                                }catch (JSONException e) {
                                    Log.e("Excepcion gestor", "No se pudo obtener gestor\n" + e.toString());
                                    e.printStackTrace();
                                }
                            }
                            ArrayAdapter arrayAdapter_gestores = new ArrayAdapter(this,
                                    R.layout.spinner_text_view, gestores);
                            spinner_gestor_screen_edit_itac.setAdapter(arrayAdapter_gestores);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void openDialog(String title, String hint){
        tag=title;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(title, hint);
        dialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void pasarTexto(String wrote_text) throws JSONException {
        if (tag.contains("Descripción")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion, wrote_text);
                textView_descripcion_screen_edit_itac.setText(wrote_text);
            }
        } else if (tag.contains("Acceso")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.itac_JSON.put(DBitacsController.acceso, wrote_text);
                textView_acceso_screen_edit_itac.setText(wrote_text);
            }
        }
        else if (tag.contains("Dirección")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.itac_JSON.put(DBitacsController.direccion, wrote_text);
                textView_direccion_screen_edit_itac.setText(wrote_text);
            }
        }else if (tag.contains("Código de Emplazamiento")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.itac_JSON.put(DBitacsController.codigo_itac, wrote_text);
                textView_cod_emplazamiento_screen_edit_itac.setText(wrote_text);
            }
        }else if (tag.contains("Identificación Firmante")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.itac_JSON.put(DBitacsController.carnet_firmante, wrote_text);
                textView_numero_carnet_firmante_screen_edit_itac.setText(wrote_text);
            }
        }else if (tag.contains("Nombre Firmante")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.itac_JSON.put(DBitacsController.nombre_firmante, wrote_text);
                textView_nombre_firmante_screen_edit_itac.setText(wrote_text);
            }
        }
    }
    public String lookForAllreadyTakenPhotos(String photo_name) throws JSONException {
        String cod_emplazamiento = null;
        String gestor = null;
        try {
            cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();
            gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            String dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                    Screen_Login_Activity.current_empresa + "/fotos_ITACs/" + gestor + "/"+ cod_emplazamiento+"/";
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
    private void addPhotoLocal() {
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_9).trim();//firma del cliente
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);

                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        imageView_firma_screen_edit_itac.setImageBitmap(bitmap);
                    }
                }else{
                    Log.e("no Existe: ", "foto firma");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_1).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_1, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_1_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_1_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_1_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_1_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto 1");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_2).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_2, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_2_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_2_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_2_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_2_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_3).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_3, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_3_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_3_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_3_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_3_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_4).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_4, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_4_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_4_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_4_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_4_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_5).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_5, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_5_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_5_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_5_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_5_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_6).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_6, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_6_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_6_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_6_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_6_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_7).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_7, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_7_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_7_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_7_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_7_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String photo = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_8).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    try {
                        foto_requests_paths.put(CAM_REQUEST_PHOTO_8, bitmap_dir);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap != null) {
                        editText_descripcion_photo_8_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_8_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_8_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_8_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                    }
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String saveBitmapImage(Bitmap bitmap, String key){
        bitmap = Screen_Login_Activity.scaleBitmap(bitmap);
        try {
            String cod_emplazamiento = null;
            cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();
            String file_full_name = cod_emplazamiento+"_"+key;
            String gestor = null;
            gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/"+
                    Screen_Login_Activity.current_empresa + "/fotos_ITACs/" + gestor + "/"+ cod_emplazamiento+"/");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            else{
                File[] files = myDir.listFiles();
                for(int i=0; i< files.length; i++){
                    if(files[i].getName().contains(file_full_name)){
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
            Screen_Login_Activity.itac_JSON.put(key, file_full_name);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CANVAS_REQUEST_EDIT_ITAC) {
                String firma = data.getStringExtra("firma_cliente");
                if (Screen_Login_Activity.checkStringVariable(firma)) {
                    Bitmap bitmap = getPhotoUserLocal(firma);
                    if(bitmap!=null) {
                        imageView_firma_screen_edit_itac.setImageBitmap(bitmap);
                        saveBitmapImage(bitmap, DBitacsController.foto_9);
//                        imageView_firma_screen_edit_itac.getLayoutParams().height = bitmap_firma_cliente.getHeight() + 300;
                    }
                }
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_1) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_1);
                String filename = saveBitmapImage(bitmap, foto_requests.get(CAM_REQUEST_PHOTO_1));
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                        if (bitmap != null) {
                            imageView_photo_1_screen_edit_itac.setVisibility(View.VISIBLE);
                            imageView_photo_1_screen_edit_itac.setImageBitmap(bitmap);
                            imageView_photo_1_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                            editText_descripcion_photo_1_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_2) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_2));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_2);
                String filename = saveBitmapImage(bitmap, foto_requests.get(CAM_REQUEST_PHOTO_2));
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                    if (bitmap != null) {
                        imageView_photo_2_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_2_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_2_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        editText_descripcion_photo_2_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_3) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_3));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_3);
                String filename = saveBitmapImage(bitmap, foto_requests.get(CAM_REQUEST_PHOTO_3));
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                    if (bitmap != null) {
                        imageView_photo_3_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_3_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_3_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        editText_descripcion_photo_3_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_4) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_4));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_4);
                String filename = saveBitmapImage(bitmap, foto_requests.get(CAM_REQUEST_PHOTO_4));
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                    if (bitmap != null) {
                        imageView_photo_4_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_4_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_4_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        editText_descripcion_photo_4_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_5) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_5));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_5);
                String filename = saveBitmapImage(bitmap, foto_requests.get(CAM_REQUEST_PHOTO_5));
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                    if (bitmap != null) {
                        imageView_photo_5_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_5_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_5_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        editText_descripcion_photo_5_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_6) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_6));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_6);
                String filename = saveBitmapImage(bitmap, foto_requests.get(CAM_REQUEST_PHOTO_6));
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                    if (bitmap != null) {
                        imageView_photo_6_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_6_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_6_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        editText_descripcion_photo_6_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_7) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_7));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_7);
                String filename = saveBitmapImage(bitmap, foto_requests.get(CAM_REQUEST_PHOTO_7));
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                    if (bitmap != null) {
                        imageView_photo_7_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_7_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_7_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        editText_descripcion_photo_7_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAM_REQUEST_PHOTO_8) {
            Bitmap bitmap = null;
            try {
                bitmap = getPhotoUserLocal(foto_requests_paths.get(CAM_REQUEST_PHOTO_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bitmap!=null) {
                String type_foto = foto_requests.get(CAM_REQUEST_PHOTO_8);
                String filename = saveBitmapImage(bitmap, type_foto);
                if (Screen_Login_Activity.checkStringVariable(filename)) {
                    bitmap = null;
                    bitmap = getPhotoUserLocal(filename);
                    if (bitmap != null) {
                        imageView_photo_8_screen_edit_itac.setVisibility(View.VISIBLE);
                        imageView_photo_8_screen_edit_itac.setImageBitmap(bitmap);
                        imageView_photo_8_screen_edit_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        editText_descripcion_photo_8_screen_edit_itac.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"No se encuentra foto luego de cambiar nombre: " +filename, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this,"No se encuentra archivo fotoe: " +filename, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"No se encuentra foto: ", Toast.LENGTH_LONG).show();
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
                if(foto_requests.containsKey(request)){
                    photoFile = createImageFile(request, foto_requests.get(request));
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
    private File createImageFile(int request, String foto_x) throws IOException, JSONException {
        // Create an image file name
        String image = "";
        String cod_emplazamiento = null;
        cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();

        image = cod_emplazamiento + "_" + foto_x;

        File image_file = null;
        String gestor = null;
        gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.GESTOR).trim();
        if(!Screen_Login_Activity.checkStringVariable(gestor)){
            if(Screen_Login_Activity.itac_JSON.getInt(DBitacsController.id) < 0){
                gestor = spinner_gestor_screen_edit_itac.getSelectedItem().toString();
                Screen_Login_Activity.itac_JSON.put(DBitacsController.GESTOR, gestor);
            }else {
                gestor = "Sin_Gestor";
            }
        }
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                Screen_Login_Activity.current_empresa + "/fotos_ITACs/" + gestor + "/"+ cod_emplazamiento+"/");
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
        try {
            foto_requests_paths.put(request, image_file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "Error insertanto en"+ foto_requests_paths);
        }

        //etname.setText(image);
        return image_file;
    }

    private void showCurrentContact(String item_selected) {
        if(item_selected.contains("ADMINISTRACIÓN")){
            layout_empresa_screen_edit_itac.setVisibility(View.VISIBLE);
            layout_presidente_screen_edit_itac.setVisibility(View.GONE);
            layout_encargado_screen_edit_itac.setVisibility(View.GONE);
        }else if(item_selected.contains("PRESIDENTE O VECINO COLABORADOR")){
            layout_empresa_screen_edit_itac.setVisibility(View.GONE);
            layout_presidente_screen_edit_itac.setVisibility(View.VISIBLE);
            layout_encargado_screen_edit_itac.setVisibility(View.GONE);
        }else if(item_selected.contains("ENCARGADO O COSERJE")){
            layout_empresa_screen_edit_itac.setVisibility(View.GONE);
            layout_presidente_screen_edit_itac.setVisibility(View.GONE);
            layout_encargado_screen_edit_itac.setVisibility(View.VISIBLE);
        }
    }

    public static String fieldData(String field){
        if(Screen_Login_Activity.checkStringVariable(field)){
            return field;
        }
        return "";
    }
    private void populateView(){
        try {
            textView_cod_emplazamiento_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac)));

            textView_direccion_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.itac)));

            textView_acceso_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.acceso)));
            textView_descripcion_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion)));

            editText_nombre_empresa_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.nombre_empresa_administracion)));
            editText_direccion_oficina_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.direccion_oficina_administracion)));
            editText_nombre_responsable_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.nombre_responsable_administracion)));
            editText_telefono_fijo_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.telefono_fijo_administracion)));
            editText_telefono_movil_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.telefono_movil_administracion)));
            editText_correo_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.correo_administracion)));

            editText_nombre_presidente_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.nombre_presidente)));
            editText_telefono_fijo_presidente_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.telefono_fijo_presidente)));
            editText_telefono_movil_presidente_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.telefono_movil_presidente)));
            editText_correo_presidente_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.correo_presidente)));

            editText_nombre_encargado_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.nombre_encargado)));
            editText_telefono_fijo_encargado_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.telefono_fijo_encargado)));
            editText_telefono_movil_encargado_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.telefono_movil_encargado)));
            editText_correo_encargado_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.correo_encargado)));

            editText_descripcion_photo_1_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_1)));
            editText_descripcion_photo_2_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_2)));
            editText_descripcion_photo_3_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_3)));
            editText_descripcion_photo_4_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_4)));
            editText_descripcion_photo_5_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_5)));
            editText_descripcion_photo_6_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_6)));
            editText_descripcion_photo_7_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_7)));
            editText_descripcion_photo_8_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion_foto_8)));

            textView_numero_carnet_firmante_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.carnet_firmante)));
            textView_nombre_firmante_screen_edit_itac.setText(fieldData(Screen_Login_Activity.itac_JSON.getString(DBitacsController.nombre_firmante)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private boolean guardarDatos() {
        try {
            Screen_Login_Activity.itac_JSON.put(DBitacsController.itac, textView_direccion_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.acceso, textView_acceso_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion, textView_descripcion_screen_edit_itac.getText().toString());

            Screen_Login_Activity.itac_JSON.put(DBitacsController.nombre_empresa_administracion, editText_nombre_empresa_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.direccion_oficina_administracion,editText_direccion_oficina_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.nombre_responsable_administracion, editText_nombre_responsable_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.telefono_fijo_administracion, editText_telefono_fijo_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.telefono_movil_administracion, editText_telefono_movil_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.correo_administracion, editText_correo_screen_edit_itac.getText().toString());

            Screen_Login_Activity.itac_JSON.put(DBitacsController.nombre_presidente, editText_nombre_presidente_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.telefono_fijo_presidente, editText_telefono_fijo_presidente_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.telefono_movil_presidente, editText_telefono_movil_presidente_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.correo_presidente, editText_correo_presidente_screen_edit_itac.getText().toString());

            Screen_Login_Activity.itac_JSON.put(DBitacsController.nombre_encargado, editText_nombre_encargado_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.telefono_fijo_encargado, editText_telefono_fijo_encargado_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.telefono_movil_encargado, editText_telefono_movil_encargado_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.correo_encargado, editText_correo_encargado_screen_edit_itac.getText().toString());

            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_1, editText_descripcion_photo_1_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_2, editText_descripcion_photo_2_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_3, editText_descripcion_photo_3_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_4, editText_descripcion_photo_4_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_5, editText_descripcion_photo_5_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_6, editText_descripcion_photo_6_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_7, editText_descripcion_photo_7_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.descripcion_foto_8, editText_descripcion_photo_8_screen_edit_itac.getText().toString());

            Screen_Login_Activity.itac_JSON.put(DBitacsController.nombre_firmante, textView_nombre_firmante_screen_edit_itac.getText().toString());
            Screen_Login_Activity.itac_JSON.put(DBitacsController.carnet_firmante, textView_numero_carnet_firmante_screen_edit_itac.getText().toString());

            int id =Screen_Login_Activity.itac_JSON.getInt(DBitacsController.id);
            if (id > 0) {
                if(!updateItac()){
                    openMessage("Error actualizando", "No se pudo actualiza ITAC");
                    return false;
                }
            }else {
                String cod = textView_cod_emplazamiento_screen_edit_itac.getText().toString();
                String gestor = spinner_gestor_screen_edit_itac.getSelectedItem().toString();
                if(!cod.isEmpty()) {
                    if(!createItac(cod, gestor)) {
                        openMessage("C.Emplazamiento Registrado", "No se pudo insertar. El Código de Emplazamiento ya existe. Puede que haya sido creado por otro equipo");
                        return false;
                    }
                }else {
                    openMessage("C.Emplazamiento Vacío", "Debe insertar el Código de Emplazamiento");
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
    public static boolean updateItac(){
        try {
            Screen_Login_Activity.itac_JSON.put(DBitacsController.date_time_modified, DBtareasController.getStringFromFechaHora(new Date()));

            String status = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.status_itac).trim();
            if(!Screen_Login_Activity.checkStringVariable(status)){
                status = "IDLE, TO_UPDATE";
            }else if(!status.contains("TO_UPDATE")){
                status+=", TO_UPDATE";
            }
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.status_itac, status);
            team_or_personal_task_selection_screen_Activity.
                    dBitacsController.updateItac(Screen_Login_Activity.itac_JSON);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean createItac(String cod , String gestor){
        try {
            if(!team_or_personal_task_selection_screen_Activity.
                    dBitacsController.checkIfItacExists(cod)) {

                Screen_Login_Activity.itac_JSON.put(DBitacsController.codigo_itac, cod);

                Screen_Login_Activity.itac_JSON.put(DBitacsController.GESTOR, gestor);
                Screen_Login_Activity.itac_JSON.put(DBitacsController.equipo,
                        Screen_Login_Activity.equipo_JSON.getString(DBequipo_operariosController.equipo_operario));

                String status = "IDLE, TO_UPDATE";
                Screen_Login_Activity.itac_JSON.put(DBitacsController.status_itac, status);
                Screen_Login_Activity.itac_JSON.put(DBitacsController.date_time_modified,
                        DBtareasController.getStringFromFechaHora(new Date()));

                team_or_personal_task_selection_screen_Activity.
                        dBitacsController.insertItac(Screen_Login_Activity.itac_JSON);
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBitacsController.get_one_itac_from_Database(cod));
                Screen_Login_Activity.itac_JSON.put(DBitacsController.id,
                        jsonObject.getInt(DBitacsController.id));
                return true;
            }else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void openMessage(String title, String hint){
        MessageDialog messageDialog = null;
        try {
            messageDialog = new MessageDialog();
            messageDialog.setTitleAndHint(title, hint);
            messageDialog.show(getSupportFragmentManager(), title);
        } catch (Exception e) {
            Log.e("Error abriendo mensaje", e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Saliendo de Edición")
                .setMessage("¿Desea salir sin guardar?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            int id = Screen_Login_Activity.itac_JSON.getInt(DBitacsController.id);
                            if (id > 0) {
                                finishThisClass();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Screen_Edit_ITAC.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
    public void finishThisClass(){
        Intent intent_open_screen_itac = new Intent(Screen_Edit_ITAC.this, Screen_Itac.class);
        startActivity(intent_open_screen_itac);
        Screen_Edit_ITAC.this.finish();
    }
}
