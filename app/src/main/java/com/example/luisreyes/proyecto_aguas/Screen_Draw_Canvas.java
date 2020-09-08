package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by luis.reyes on 13/08/2019.
 */

public class Screen_Draw_Canvas extends Activity {

    myCanvas canvas;
    private Bitmap bitmap_firma;

    private static final int CANVAS_REQUEST_INC_SUMMARY = 3331;
    private static final int CANVAS_REQUEST_VALIDATE = 3333;
    private static final int CANVAS_REQUEST_EDIT_ITAC = 3339;
    private int caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new myCanvas(this, null);
        setContentView(canvas);

        caller = getIntent().getIntExtra("class_caller", 0);
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Salvar")
                .setMessage("Desea Salvar Firma")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent();
                        if(caller==CANVAS_REQUEST_EDIT_ITAC) {
                            resultIntent = new Intent(Screen_Draw_Canvas.this, Screen_Edit_ITAC.class);
                            bitmap_firma = (Bitmap) canvas.getDrawingCache();
                            if(bitmap_firma!= null) {
                                String path = saveBitmapImageFirmaItac(bitmap_firma, DBitacsController.foto_9);
                                resultIntent.putExtra("firma_cliente", path);
                            }else{
                                Log.e("Excepcion", "Error obteniedo firma_cliente");
                                resultIntent.putExtra("firma_cliente", "null");
                            }
                        }
                        else{
                            if (caller == CANVAS_REQUEST_VALIDATE) {
                                resultIntent = new Intent(Screen_Draw_Canvas.this, Screen_Validate.class);
                            } else if (caller == CANVAS_REQUEST_INC_SUMMARY) {
                                resultIntent = new Intent(Screen_Draw_Canvas.this, Screen_Incidence_Summary.class);
                            }
                            bitmap_firma = (Bitmap) canvas.getDrawingCache();
                            if (bitmap_firma != null) {
                                String nombre_abonado = null;
                                try {
                                    nombre_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace(" ", "_");
                                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.firma_cliente, nombre_abonado + "_firma.jpg");
                                    String path = saveBitmapImageFirma(bitmap_firma, nombre_abonado + "_firma");
                                    resultIntent.putExtra("firma_cliente", path);
                                } catch (JSONException e) {
                                    Log.e("Excepcion", "Error obteniedo nombre_cliente");
                                    resultIntent.putExtra("firma_cliente", "null");
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Error", "Error obteniedo firma_cliente null");
                                resultIntent.putExtra("firma_cliente", "null");
                            }
                        }

                        //resultIntent.putExtra("result", result);
                        setResult(RESULT_OK, resultIntent);
                        canvas.setDrawingCacheEnabled(false);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent();
                        if(caller==CANVAS_REQUEST_EDIT_ITAC) {
                            resultIntent = new Intent(Screen_Draw_Canvas.this, Screen_Edit_ITAC.class);
                        }
                        else if(caller==CANVAS_REQUEST_VALIDATE) {
                            resultIntent = new Intent(Screen_Draw_Canvas.this, Screen_Validate.class);
                        }
                        else if(caller==CANVAS_REQUEST_INC_SUMMARY) {
                            resultIntent = new Intent(Screen_Draw_Canvas.this, Screen_Incidence_Summary.class);
                        }
                        int result = 3;
                        bitmap_firma = null;
                        String img_compress="null";
                        resultIntent.putExtra("firma_cliente", img_compress);
                        //resultIntent.putExtra("result", result);
                        setResult(RESULT_OK, resultIntent);
                        canvas.setDrawingCacheEnabled(false);
                        finish();
                    }
                }).show();
    }

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap scaleBitmap(Bitmap bitmap){
        Size size = new Size(bitmap.getWidth(), bitmap.getHeight());
        double max_height = 1280;
        double max_width = 1280;
        double ratio;
        if(size.getWidth() > size.getHeight()){
            ratio = (double)(size.getHeight())/ (double)(size.getWidth());
            max_height = max_width * ratio;
        }else{
            ratio = (double)(size.getWidth())/ (double)(size.getHeight());
            max_width = max_height * ratio;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)max_width, (int)max_height, true);
        return bitmap;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String saveBitmapImageFirmaItac(Bitmap bitmap, String key){
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String saveBitmapImageFirma(Bitmap bitmap, String file_name){
        bitmap = scaleBitmap(bitmap);
        String numero_abonado = "";
        File myDir = null;
        String anomalia = "";
        try {
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }

            if(Screen_Login_Activity.checkStringVariable(numero_abonado)){

                myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                        Screen_Login_Activity.current_empresa +
                        "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);


                if(myDir!=null) {
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                        File storageDir2 =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                                Screen_Login_Activity.current_empresa +
                                "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);
                        if (!storageDir2.exists()) {
                            storageDir2.mkdirs();
                        }
                    } else {
                        File[] files = myDir.listFiles();
                        //ArrayList<String> names = new ArrayList<>();
                        for (int i = 0; i < files.length; i++) {
                            //names.add(files[i].getName());
                            if (files[i].getName().contains(file_name)) {
                                files[i].delete();
                            }
                        }
                        //Toast.makeText(Screen_User_Data.this, names.toString(), Toast.LENGTH_SHORT).show();
                    }

                    file_name += ".jpg";
                    File file = new File(myDir, file_name);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return file.getAbsolutePath();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
