package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
 * Created by luis.reyes on 30/08/2019.
 */

public class Screen_User_Data extends AppCompatActivity implements TaskCompleted{

    ImageView button_continuar;
    CircleImageView circlImageView_photo;
    TextView nombre, telefono;
    String nombre_operario;
    String clave;
    String usuario;
    String apellidos;
    String image;
    Bitmap bitmap_user_photo=null;

    private static final int CAM_REQUEST_USER_PHOTO = 1219;
    private static final int REQUEST_TAKE_PHOTO_FULL_SIZE = 1220;
    private ProgressDialog progressDialog;
    private String mCurrentPhotoPath;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_user_data);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        button_continuar = (ImageView)findViewById(R.id.button_screen_user_data_continuar);
        circlImageView_photo = (CircleImageView)findViewById(R.id.circleImageView_screen_user_data_photo);
        nombre = (TextView)findViewById(R.id.textView_screen_user_data_nombre);
        telefono = (TextView)findViewById(R.id.textView_screen_user_data_telefono);

        String json_usuario_string = getIntent().getStringExtra("usuario");
        if(json_usuario_string != null && !TextUtils.isEmpty(json_usuario_string)){
            try {
                JSONObject json_usuario = new JSONObject(json_usuario_string);
                Screen_Login_Activity.operario_JSON = json_usuario;
                //Bitmap bitmap = Screen_Validate.getImageFromString(foto_usuario);
                //circlImageView_photo.setImageBitmap(bitmap);/////////////////////**************************************************************************************
                nombre_operario = json_usuario.getString("nombre");
                apellidos = json_usuario.getString("apellidos");
                usuario = json_usuario.getString("usuario");
                clave = json_usuario.getString("clave");
                image = json_usuario.getString("foto");
                nombre.setText(nombre_operario + " " + apellidos);
                telefono.setText(json_usuario.getString("telefonos"));

                if (checkConection()) {
                    showRingDialog("Cargado informacion de operario...");
                    String type_script = "download_user_image";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_User_Data.this);
                    backgroundWorker.execute(type_script, usuario);
                } else {
                    if (Screen_Login_Activity.dBoperariosController.databasefileExists(this) && Screen_Login_Activity.dBoperariosController.checkForTableExists()) {
                        //Toast.makeText(Screen_User_Data.this,"Existe y no esta vacia", Toast.LENGTH_LONG).show();
                        String user = Screen_Login_Activity.dBoperariosController.get_one_operario_from_Database(usuario);
                        JSONObject jsonObject = new JSONObject(user);
                        String user_foto = jsonObject.getString("foto");
                        //Toast.makeText(Screen_User_Data.this, user_foto, Toast.LENGTH_LONG).show();
                        Bitmap foto = getPhotoUserLocal(getSimilarFile(user_foto));
                        if (foto != null) {
                            circlImageView_photo.setBackgroundColor(Color.TRANSPARENT);
                            circlImageView_photo.setImageBitmap(foto);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        circlImageView_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
//                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent_camera, CAM_REQUEST_USER_PHOTO);
            }
        });

        button_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_next_screen = new Intent(Screen_User_Data.this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(intent_open_next_screen);
                finish();
            }
        });
    }

    public String getSimilarFile(String file_name){
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_operarios");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File[] files = storageDir.listFiles();
        for(int i=0; i< files.length;i++) {
            if (files[i].getName().contains(file_name)) {
                return storageDir +"/" + files[i].getName();
            }
        }
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST_USER_PHOTO) {

            Bitmap foto_nueva = (Bitmap) data.getExtras().get("data");
            circlImageView_photo.setImageBitmap(foto_nueva);

            saveBitmapImage(foto_nueva, usuario);

            String image = Screen_Register_Operario.getStringImage(foto_nueva);
            String date_time_modified = DBoperariosController.getStringFromFechaHora(new Date());

            String type = "change_foto";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_User_Data.this);
            backgroundWorker.execute(type, usuario, clave, image, date_time_modified);
        }
        if(requestCode == REQUEST_TAKE_PHOTO_FULL_SIZE){
            if (resultCode == RESULT_OK) {
                try {
                    mCurrentPhotoPath = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath), "operario_"+Screen_Login_Activity.operario_JSON.getString("usuario"));
                    File file = new File(mCurrentPhotoPath);
                    bitmap_user_photo = null;
                    bitmap_user_photo = getPhotoUserLocal(mCurrentPhotoPath);
                    circlImageView_photo.setBackgroundColor(Color.TRANSPARENT);
                    circlImageView_photo.setImageBitmap(bitmap_user_photo);
                    if (bitmap_user_photo != null) {
                        showRingDialog("Cambiando foto...");
                        //Toast.makeText(this, "Imagen ok", Toast.LENGTH_LONG).show();
                        String type = "upload_user_image";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_User_Data.this);
                        backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap_user_photo), usuario);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "login"){
            if(result == null){
                Toast.makeText(Screen_User_Data.this,"No hay conexion a Internet, no se pudo loguear", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_User_Data.this, "Login Success ", Toast.LENGTH_SHORT).show();
            }
        }else if(type == "change_foto"){
            if(result == null){
                Toast.makeText(Screen_User_Data.this,"No se pudo acceder al servidor, no se pudo cambiar foto", Toast.LENGTH_LONG).show();
            }
            else if(result.contains("not success")){
                Toast.makeText(Screen_User_Data.this,"Error de script, no se pudo cambiar foto "+result, Toast.LENGTH_LONG).show();
            }
            else {
                //Toast.makeText(Screen_User_Data.this, "Cambiando foto", Toast.LENGTH_SHORT).show();
                String username = usuario;

                String type_script = "download_user_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_User_Data.this);
                backgroundWorker.execute(type_script, username);
            }
        }
        else if(type == "upload_user_image"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_User_Data.this, "Imagen "+ image+" subida", Toast.LENGTH_SHORT).show();
                //showRingDialog("Validando registro...");
            }
        }
        else if(type == "download_user_image") {
            hideRingDialog();
            if (result == null) {
                Toast.makeText(this, "No se puede acceder al servidor, no se obtuvo foto", Toast.LENGTH_LONG).show();
            }
            else if(result.contains("not success")){
                Toast.makeText(Screen_User_Data.this,"Error de script, no se pudo obtener foto "+result, Toast.LENGTH_LONG).show();
            }
            else {
                circlImageView_photo.setBackgroundColor(Color.TRANSPARENT);
                Bitmap bitmap = null;
                bitmap = Screen_Register_Operario.getImageFromString(result);
                if(bitmap != null){
                    circlImageView_photo.setImageBitmap(bitmap);
                    saveBitmapImage(Screen_Register_Operario.getImageFromString(result), usuario);
                }
            }
        }
    }

    private void setFotoUsuarioFromJSONString(String result) throws JSONException {
        JSONObject json_usuario = new JSONObject(result);

        String foto_usuario = json_usuario.getString("foto");
        Bitmap bitmap = Screen_Validate.getImageFromString(foto_usuario);
        circlImageView_photo.setBackgroundColor(Color.TRANSPARENT);
        circlImageView_photo.setImageBitmap(bitmap);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
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
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_FULL_SIZE);
            }
        }
    }

    private String saveBitmapImage(Bitmap bitmap, String file_name){
        File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_operarios");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        else{
            File[] files = myDir.listFiles();
            //ArrayList<String> names = new ArrayList<>();
            for(int i=0; i< files.length; i++){
                //names.add(files[i].getName());
                if(files[i].getName().contains(file_name)){
                    files[i].delete();
                }
            }
            //Toast.makeText(Screen_User_Data.this, names.toString(), Toast.LENGTH_SHORT).show();
        }
        file_name+=".jpg";
        File file = new File(myDir, file_name);
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
        return file.getAbsolutePath();
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = null;
        image = "operario_"+usuario.toString();
        File image_file=null;
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_operarios");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        image_file = File.createTempFile(
                image,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image_file.getAbsolutePath();
        //etname.setText(image);
        return image_file;
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
        progressDialog = ProgressDialog.show(Screen_User_Data.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
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
            case R.id.action_opcion1:
                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
                // User chose the "Settings" item, show the app settings UI...
                return true;

//            case R.id.action_favorite:
//                Toast.makeText(MainActivity.this, "Seleccionó la opción faovorito", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
