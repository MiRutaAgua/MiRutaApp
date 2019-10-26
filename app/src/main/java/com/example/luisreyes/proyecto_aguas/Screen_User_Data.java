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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by luis.reyes on 30/08/2019.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Screen_User_Data extends AppCompatActivity implements TaskCompleted{

    Button button_continuar;
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

        button_continuar = (Button)findViewById(R.id.button_screen_user_data_continuar);
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
                    showRingDialog("Cargando información de operario...");
                    String type_script = "download_user_image";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_User_Data.this);
                    backgroundWorker.execute(type_script, usuario);
                } else {
                    if (Screen_Login_Activity.dBoperariosController.databasefileExists(this) && Screen_Login_Activity.dBoperariosController.checkForTableExists()) {
                        //Toast.makeText(Screen_User_Data.this,"Existe y no esta vacia", Toast.LENGTH_LONG).show();
                        String user = null;
                        try {
                            user = Screen_Login_Activity.dBoperariosController.get_one_operario_from_Database(usuario);
                            if(user!=null && !TextUtils.isEmpty(user)){
                                JSONObject jsonObject = new JSONObject(user);
                                String user_foto = jsonObject.getString("foto");
                                //Toast.makeText(Screen_User_Data.this, user_foto, Toast.LENGTH_LONG).show();
                                if (user_foto != null && !TextUtils.isEmpty(user_foto)) {
                                    String similar = getSimilarFile(user_foto);
                                    if(similar!=null && !similar.isEmpty()) {
                                        Bitmap foto = getPhotoUserLocal(similar);
                                        if (foto != null) {
                                            circlImageView_photo.setBackgroundColor(Color.TRANSPARENT);
                                            circlImageView_photo.setImageBitmap(foto);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_User_Data.this, R.anim.bounce);
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
                        onContinuer_button();
                    }
                });
                button_continuar.startAnimation(myAnim);
            }
        });
    }

    public void onContinuer_button(){
        Intent intent_open_next_screen = new Intent(Screen_User_Data.this, team_or_personal_task_selection_screen_Activity.class);
        startActivity(intent_open_next_screen);
        finish();
    }
    public String getSimilarFile(String file_name){
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_operarios");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
            File storageDir2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos_tareas");
            if(!storageDir2.exists()){
                storageDir2.mkdir();
            }
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

        if(requestCode == REQUEST_TAKE_PHOTO_FULL_SIZE){
            if (resultCode == RESULT_OK) {
                try {
                    mCurrentPhotoPath = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath), Screen_Login_Activity.operario_JSON.getString("usuario")+"_operario");
                    bitmap_user_photo = null;
                    bitmap_user_photo = getPhotoUserLocal(mCurrentPhotoPath);
                    if (bitmap_user_photo != null) {
                        circlImageView_photo.setBackgroundColor(Color.TRANSPARENT);
                        circlImageView_photo.setImageBitmap(bitmap_user_photo);

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
                    saveBitmapImage(Screen_Register_Operario.getImageFromString(result), usuario+"_operario");
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
        bitmap = Bitmap.createScaledBitmap(bitmap, 960, 1280, true);
        File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_operarios");
        if (!myDir.exists()) {
            myDir.mkdirs();
            File storageDir2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos_tareas");
            if(!storageDir2.exists()){
                storageDir2.mkdir();
            }
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
//        Toast.makeText(Screen_User_Data.this, file_name, Toast.LENGTH_SHORT).show();
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

    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = null;
        image =usuario.toString()+"_operario";
        File image_file=null;
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_operarios");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
            File storageDir2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos_tareas");
            if(!storageDir2.exists()){
                storageDir2.mkdir();
            }
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
            case R.id.Contactar:
//                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
                openMessage("Contactar",
                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
                        "\n   Michel Morales: mraguas@gmail.com"
                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.Ayuda:
                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.Configuracion:
                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
}
