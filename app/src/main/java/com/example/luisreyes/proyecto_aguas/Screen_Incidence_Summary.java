package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Incidence_Summary extends AppCompatActivity implements TaskCompleted{

    private ImageView button_compartir_screen_incidence_summary,firma_cliente, foto1, foto2, foto3, cerrar_tarea;

    private static final int CANVAS_REQUEST_INC_SUMMARY = 3331;
    private Bitmap bitmap_firma_cliente = null;
    private TextView observaciones_incidence, nombre_y_tarea;
    private EditText lectura;
    private ProgressDialog progressDialog;
    private ArrayList<String> images_files;
    private LinearLayout llScroll;
    private LinearLayout llScroll_2;
    private LinearLayout llScroll_3;
    private LinearLayout llScroll_4;
    boolean bitmap1_no_nulo = false, bitmap2_no_nulo = false,bitmap3_no_nulo = false,bitmap4_no_nulo = true;
    private Bitmap bitmap = null,  bitmap2 = null,bitmap3 = null,bitmap4 = null;
    private static final String pdfName = "pdf_validar_incidencia";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_incidence_summary);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        llScroll = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary);
        llScroll_2 = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary_2);
        llScroll_3 = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary_3);
        llScroll_4 = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary_4);
        images_files = new ArrayList<>();
        button_compartir_screen_incidence_summary = (ImageView)findViewById(R.id.button_compartir_screen_incidence_summary);
        firma_cliente = (ImageView)findViewById(R.id.imageButton_firma_cliente_screen_validate);
        foto1 = (ImageView)findViewById(R.id.imageView_foto1_screen_incidence_summary);
        foto2 = (ImageView)findViewById(R.id.imageView_foto2_screen_incidence_summary);
        foto3 = (ImageView)findViewById(R.id.imageView_foto3_screen_incidence_summary);
        cerrar_tarea = (ImageView)findViewById(R.id.button_cerrar_tarea_screen_incidence_sumary);
        observaciones_incidence = (TextView)findViewById(R.id.textView_obsevaciones_screen_incidence_summary);
        nombre_y_tarea = (TextView)findViewById(R.id.textView_nombre_y_tarea_screen_incidence_summary);
        lectura = (EditText)findViewById(R.id.editText_lectura_de_contador_screen_incidence_summary);

        try {
            nombre_y_tarea.setText(Screen_Login_Activity.tarea_JSON.getString("nombre_cliente").replace("\n", "")+", "+Screen_Login_Activity.tarea_JSON.getString("calibre_toma"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener nombre de cliente", Toast.LENGTH_LONG).show();
        }
//        try {
//            Toast.makeText(Screen_Incidence_Summary.this, Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_1"), Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Bitmap bitmapf = getPhotoUserLocal(Screen_Incidence.mCurrentPhotoPath_incidencia_1);
        if(bitmapf!=null){
            bitmap1_no_nulo =true;
            foto1.setVisibility(View.VISIBLE);
            foto1.setImageBitmap(bitmapf);
        }
        bitmapf = getPhotoUserLocal(Screen_Incidence.mCurrentPhotoPath_incidencia_2);
        if(bitmapf!=null){
            bitmap2_no_nulo =true;
            foto2.setVisibility(View.VISIBLE);
            foto2.setImageBitmap(bitmapf);
        }
        bitmapf = getPhotoUserLocal(Screen_Incidence.mCurrentPhotoPath_incidencia_3);
        if(bitmapf!=null){
            bitmap3_no_nulo =true;
            foto3.setVisibility(View.VISIBLE);
            foto3.setImageBitmap(bitmapf);
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
        try {
            firma_cliente.setImageBitmap(Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("firma_cliente")));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener firma cliente de tarea", Toast.LENGTH_LONG).show();
        }
        try {
            String lectura_string = Screen_Login_Activity.tarea_JSON.getString("lectura_actual");
            if(!TextUtils.isEmpty(lectura_string)){
                lectura.setHint(lectura_string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener actual lectura de contador", Toast.LENGTH_LONG).show();
        }

        firma_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_screen_client_sign = new Intent(Screen_Incidence_Summary.this, Screen_Draw_Canvas.class);
                intent_open_screen_client_sign.putExtra("class_caller", CANVAS_REQUEST_INC_SUMMARY);
                startActivityForResult(intent_open_screen_client_sign, CANVAS_REQUEST_INC_SUMMARY);
            }
        });

        button_compartir_screen_incidence_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRingDialog("Creando PDF...");
                if(bitmap1_no_nulo)
                    bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
                if(bitmap2_no_nulo)
                    bitmap2 = loadBitmapFromView(llScroll_2, llScroll_2.getWidth(), llScroll_2.getHeight());
                if(bitmap3_no_nulo)
                    bitmap3 = loadBitmapFromView(llScroll_3, llScroll_3.getWidth(), llScroll_3.getHeight());
                bitmap4 = loadBitmapFromView(llScroll_4, llScroll_4.getWidth(), llScroll_4.getHeight());
                createPdf();
            }
        });
        cerrar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Screen_Login_Activity.tarea_JSON.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!(TextUtils.isEmpty(lectura.getText()))) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put("lectura_actual", lectura.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
                    }
                }
                if(bitmap_firma_cliente!=null) {
                    try {
                        String firma = Screen_Register_Operario.getStringImage(bitmap_firma_cliente);
                        Screen_Login_Activity.tarea_JSON.put("firma_cliente", firma);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar firma de cliente", Toast.LENGTH_LONG).show();
                    }
                }
                showRingDialog("Guardando Incidencias...");
                if(team_or_personal_task_selection_screen_Activity.dBtareasController != null){
                    try {
                        team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(checkConection()) {
                    String type = "update_tarea";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Incidence_Summary.this);
                    backgroundWorker.execute(type);
                } else{
                    Toast.makeText(Screen_Incidence_Summary.this, "No hay conexion se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        v.setBackgroundColor(Color.WHITE);
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }
    private PdfDocument setContentPDF(PdfDocument document, Bitmap bitmap, int w, int h, int page_count){
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(w, h, page_count).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);
        return document;
    }
    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

        int page_count = 0;
//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        if(bitmap1_no_nulo)
            document = setContentPDF(document, bitmap, convertWidth, convertHighet, ++page_count);
        if(bitmap2_no_nulo)
            document = setContentPDF(document, bitmap2, convertWidth, convertHighet, ++page_count);
        if(bitmap3_no_nulo)
            document = setContentPDF(document, bitmap3, convertWidth, convertHighet, ++page_count);
        document = setContentPDF(document, bitmap4, convertWidth, convertHighet, ++page_count);
        //////////////////////////////////////////////////////////////////////////////////////////////
        // write the document content
        File myDir = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)));
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String targetPdf = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+pdfName+".pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No pudo crearse: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();

        hideRingDialog();
        if(filePath.exists()) {
            Toast.makeText(this, "PDF creado correctamente " + targetPdf, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SEND ,Uri.parse("mailto: mraguascontadores@gmail.com")); // it's not ACTION_SEND
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "PDF validar");
            intent.putExtra(Intent.EXTRA_TEXT, "");
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:"+filePath.getAbsolutePath()));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "PDF no creado", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CANVAS_REQUEST_INC_SUMMARY){
            String firma = data.getStringExtra("firma_cliente");
            //int result = data.getIntExtra("result", 0);
            //String res = String.valueOf(result);
            if(!firma.equals("null")) {
                bitmap_firma_cliente = getImageFromString(firma);
                firma_cliente.setImageBitmap(bitmap_firma_cliente);
            }
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
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(Screen_Incidence_Summary.this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(Screen_Incidence_Summary.this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                } else {
                    if (result.contains("not success")) {
                        Toast.makeText(Screen_Incidence_Summary.this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();

                    } else {
                        images_files.clear();
                        images_files.add(Screen_Incidence.mCurrentPhotoPath_incidencia_1);
                        images_files.add(Screen_Incidence.mCurrentPhotoPath_incidencia_2);
                        images_files.add(Screen_Incidence.mCurrentPhotoPath_incidencia_3);

                        showRingDialog("Subiendo foto...");
                        uploadPhotos();
                    }
                }
            }
        }else if(type == "upload_image"){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Incidence_Summary.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                uploadPhotos();
                //showRingDialog("Validando registro...");
            }
        }
    }


    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Incidence_Summary.this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
            Intent intent_open_task_or_personal_screen = new Intent(Screen_Incidence_Summary.this, team_or_personal_task_selection_screen_Activity.class);
            startActivity(intent_open_task_or_personal_screen);
            Screen_Incidence_Summary.this.finish();
            return;
        }
        else {
            String file_name = null, image_file;

            try {
                file_name = Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_"+String.valueOf(images_files.size()));
                image_file = images_files.get(images_files.size() - 1);
                images_files.remove(images_files.size() - 1);
                String type = "upload_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Incidence_Summary.this);
                backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(getPhotoUserLocal(image_file)), file_name);
            } catch (JSONException e) {
                e.printStackTrace();
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
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Incidence_Summary.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }
}
