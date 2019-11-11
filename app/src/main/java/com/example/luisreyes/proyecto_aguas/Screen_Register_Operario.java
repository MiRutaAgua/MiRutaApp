package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luis.reyes on 29/08/2019.
 */

public class Screen_Register_Operario extends AppCompatActivity implements TaskCompleted{

    EditText etname, etapellidos, etedad, etTelefonos, etuser_name, etclave;
    ImageView  button_foto;
    Button button_register;

    String name;
    String surname;
    String age;
    String telefonos;
    String password_reg;
    String username_reg;
    String mCurrentPhotoPath="";

    private static final int CAM_REQUEST_PHOTO = 1212;
    private static final int REQUEST_TAKE_PHOTO_FULL_SIZE = 1112;
    Bitmap bitmap_foto;
    boolean photo_taken = false;

    ImageView capture_Photo;
    private String image;
    private ProgressDialog progressDialog = null;
    boolean registrando = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_register_operario);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        button_register = (Button)findViewById(R.id.button_screen_register_operario_register);

        etname = (EditText)findViewById(R.id.editText_screen_register_operario_etNombre);
        etapellidos = (EditText)findViewById(R.id.editText_screen_register_operario_etApellidos);
        etedad = (EditText)findViewById(R.id.editText_screen_register_operario_etEdad);
        etTelefonos = (EditText)findViewById(R.id.editText_screen_register_operario_etTelefono);
        etuser_name = (EditText)findViewById(R.id.editText_screen_register_operario_etUsuario);
        etclave= (EditText)findViewById(R.id.editText_screen_register_operario_etClave);
        //button_foto = (ImageView) findViewById(R.id.button_screen_register_operario_foto);
        capture_Photo = (ImageView)findViewById(R.id.imageView_screen_register_operario_foto);

        capture_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(TextUtils.isEmpty(etuser_name.getText().toString()))) {
                    dispatchTakePictureIntent();
                }
                else{
                    Toast.makeText(Screen_Register_Operario.this, "Inserte nombre de usuario", Toast.LENGTH_LONG).show();
                }
//                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent_camera, CAM_REQUEST_PHOTO);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Register_Operario.this, R.anim.bounce);
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
                        onRegister_Button();
                    }
                });

                button_register.startAnimation(myAnim);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onRegister_Button(){
        if (!registrando) {
            registrando = true;
            if (checkConection()) {
                if (photo_taken && !(TextUtils.isEmpty(etname.getText().toString()))
                        && !(TextUtils.isEmpty(etapellidos.getText().toString()))
                        && !(TextUtils.isEmpty(etedad.getText().toString()))
                        && !(TextUtils.isEmpty(etTelefonos.getText().toString()))
                        && !(TextUtils.isEmpty(etuser_name.getText().toString()))
                        && !(TextUtils.isEmpty(etclave.getText().toString()))) {

                    username_reg = etuser_name.getText().toString();
                    if (!Screen_Login_Activity.lista_usuarios.contains(username_reg)) {

                        name = etname.getText().toString();
                        surname = etapellidos.getText().toString();
                        age = etedad.getText().toString();
                        telefonos = etTelefonos.getText().toString();
                        password_reg = etclave.getText().toString();
                        String fecha_hora = DBoperariosController.getStringFromFechaHora(new Date());
                        //image = getStringImage(bitmap_foto);
                        capture_Photo.setImageDrawable(getDrawable(R.drawable.screen_exec_task_imagen));
                        //etname.setText(image);

                        showRingDialog("Registrando operario...");

                        String type = "register";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Register_Operario.this);
                        backgroundWorker.execute(type, name, surname, age, telefonos, username_reg, password_reg, image, fecha_hora);
                    } else {
                        registrando = false;
                        Toast.makeText(Screen_Register_Operario.this, "El usuario " + username_reg + " ya está registrado", Toast.LENGTH_LONG).show();
                    }
                } else {
                    registrando = false;
                    if (!photo_taken) {
                        Toast.makeText(Screen_Register_Operario.this, "Debe tomarse una foto, presione el icono del camara", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Screen_Register_Operario.this, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                registrando = false;
                Toast.makeText(Screen_Register_Operario.this, "No hay conexion a internet", Toast.LENGTH_LONG).show();
            }
        }
    }
    public static Bitmap compressImage(Bitmap bmp, int quality){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return bmp;
    }

    public static Bitmap getImageFromString(String stringImage){
        byte[] decodeString = Base64.decode(stringImage, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeImage;
    }
    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST_PHOTO){
            bitmap_foto = (Bitmap)data.getExtras().get("data");
            capture_Photo.setImageBitmap(bitmap_foto);
            photo_taken= true;
        }
        if(requestCode == REQUEST_TAKE_PHOTO_FULL_SIZE){
            if (resultCode == RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                bitmap_foto = null;
                bitmap_foto = getPhotoUserLocal(mCurrentPhotoPath);
                capture_Photo.setImageBitmap(bitmap_foto);
                if (bitmap_foto != null) {
                    Toast.makeText(this, "Imagen ok", Toast.LENGTH_LONG).show();
                    photo_taken= true;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setFotoUsuarioFromJSONString(String result) throws JSONException {
        JSONObject json_usuario = new JSONObject(result);

        String foto_usuario = json_usuario.getString("foto");
        Bitmap bitmap = Screen_Validate.getImageFromString(foto_usuario);
        capture_Photo.setImageBitmap(bitmap);
    }
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "register"){
            registrando = false;
            hideRingDialog();
            if(result == null){
                Toast.makeText(Screen_Register_Operario.this,"No se puede acceder al servidor, no se pudo registrar", Toast.LENGTH_LONG).show();
            }
            else {
                if(result.contains("Insert Successful")) {
                    //Toast.makeText(Screen_Register_Operario.this, "Validando registro...", Toast.LENGTH_SHORT).show();
                    if (bitmap_foto != null) {
                        showRingDialog("Subiendo foto de operario");

                        String foto_string = getStringImage(bitmap_foto);
                        String type_script = "upload_user_image";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Register_Operario.this);
                        backgroundWorker.execute(type_script, foto_string, image);
                    } else {
                        Toast.makeText(this, "Error subiendo foto, imagen nula", Toast.LENGTH_LONG).show();
                    }
                }else if(result.contains("Usuario ya registrado")){
                    Toast.makeText(this, "No se ha registrado porque el usuario ya existe", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Error registrando operario " + result, Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(type == "upload_user_image"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Register_Operario.this, "Imagen "+ image+" subida", Toast.LENGTH_SHORT).show();
                showRingDialog("Validando registro...");

                String type_script = "download_user_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Register_Operario.this);
                backgroundWorker.execute(type_script, username_reg);
            }
        }
        else if(type == "download_user_image"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se valido registro", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Register_Operario.this, "Usuario "+ username_reg +" registrado", Toast.LENGTH_SHORT).show();
                capture_Photo.setImageBitmap(getImageFromString(result));
            }
        }
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

    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = null;
        image = etuser_name.getText().toString();
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
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media
                    .getBitmap(this.getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            return bitmap;
        }
        else {
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
        progressDialog = ProgressDialog.show(Screen_Register_Operario.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        if(progressDialog!=null) {
            progressDialog.dismiss();
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

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.Configuracion:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}