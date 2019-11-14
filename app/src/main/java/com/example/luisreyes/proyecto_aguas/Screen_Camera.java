package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by luis.reyes on 14/08/2019.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Screen_Camera extends Activity {

    private static final int PHOTO_PREVIEW_REQUEST = 1212;
    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;
    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;
    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;
    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;
    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1700;
    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1000;

    private Button button_take_picture_screen_x;
    private Button flashButton, torchButton, selectButton, roteButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }
    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";
    private boolean isFlashSupported;
    private boolean isContinuousAutoFocusSupported;
    private boolean isTorchOn = false;
    private boolean isFlashOn = false;
    private boolean isVisibleOn = false;
    private boolean isFrontCameraOn = false;
    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private String photo_name = "";
    private String photo_path = "";
    private String photo_folder = "fotos_tareas";
    private String contador = "";

    public static int sensorOrientation;
    private OrientationEventListener orientationListener;

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice=null;
        }
    };
    private MediaPlayer mp;
    private int mState;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PHOTO_PREVIEW_REQUEST){
                String res = data.getStringExtra("result");
                Toast.makeText(Screen_Camera.this, "Resultado ok: " + res, Toast.LENGTH_SHORT).show();
                if(res.equals("save")){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("photo_path", photo_path);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }else if(res.equals("cancel")){
                    setupTorchButton();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_camera);

        String numero_interno = "";
        try {
            numero_interno = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno).trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        photo_name = getIntent().getStringExtra("photo_name");
        photo_folder = getIntent().getStringExtra("photo_folder")+"/"+numero_interno;
        contador = getIntent().getStringExtra("contador").trim().replace(" ","");



        orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI * 10) {
            public void onOrientationChanged(int orientation) {
//                Toast.makeText(Screen_Camera.this, "new : "+String.valueOf(orientation), Toast.LENGTH_LONG).show();
                sensorOrientation = orientation;
            }
        };
        orientationListener.enable();

        textureView = (TextureView)findViewById(R.id.textureView);

        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        button_take_picture_screen_x = (Button)findViewById(R.id.button_take_picture);
        flashButton = (Button)findViewById(R.id.button_flash_picture);
        torchButton = (Button)findViewById(R.id.button_torch_picture);
        roteButton = (Button)findViewById(R.id.button_rote_picture);
        selectButton = (Button)findViewById(R.id.button_select_picture);

        roteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCamera();
                if(isFrontCameraOn){
                    openCamera();
                    roteButton.setBackground(getDrawable(R.drawable.ic_camera_front_black_24dp));
                    isFrontCameraOn = false;
                }else{
                    openCameraFront();
                    roteButton.setBackground(getDrawable(R.drawable.ic_camera_rear_black_24dp));
                    isFrontCameraOn = true;
                }
            }
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchVisibilityButtonsLight();
            }
        });
        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlash();
            }
        });
        torchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTorch();
            }
        });
        button_take_picture_screen_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(photo_name != null && !TextUtils.isEmpty(photo_name)) {
                    takePicture();
                }
                else{
                    Toast.makeText(Screen_Camera.this, "No hay nombre de foto", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void switchVisibilityButtonsLight() {
        if(isVisibleOn){
            isVisibleOn=false;
            flashButton.setVisibility(View.GONE);
            torchButton.setVisibility(View.GONE);
        }else {
            isVisibleOn=true;
            flashButton.setVisibility(View.VISIBLE);
            torchButton.setVisibility(View.VISIBLE);
        }
    }

    private void takePicture(){
        if(cameraDevice == null){
            return;
        }
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null){
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
                int width = MAX_PREVIEW_WIDTH;
                int height = MAX_PREVIEW_HEIGHT;
                if(jpegSizes != null && jpegSizes.length > 0) {
                    for (int i = 0; i < jpegSizes.length; i++) {
                        if(jpegSizes[i].getWidth() <= MAX_PREVIEW_WIDTH && jpegSizes[i].getHeight() <= MAX_PREVIEW_HEIGHT) {
                            //Aqui tengo los tamaños
                            width = jpegSizes[i].getWidth();
                            height = jpegSizes[i].getHeight();
                            break;
                        }
                    }
                }

                final ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                List<Surface> outputSurface = new ArrayList<>(2);
                outputSurface.add(reader.getSurface());
                outputSurface.add(new Surface(textureView.getSurfaceTexture()));

                final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(cameraDevice.TEMPLATE_STILL_CAPTURE);
                captureBuilder.addTarget(reader.getSurface());
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                if(isTorchOn){
                    captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                }else if(isFlashOn){
                    captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                }else{
                    captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                }

                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

                File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+photo_folder);
                if(!myDir.exists()){
                    myDir.mkdirs();
                }
                else{
                    File[] files = myDir.listFiles();
                    if(!photo_name.contains("_foto_incidencia")) {
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].getName().contains(photo_name) || files[i].getName().contains(contador + "_foto_incidencia")) {
                                files[i].delete();
                            }
                        }
                    }else {
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].getName().contains(photo_name) || files[i].getName().contains(contador + "_foto_antes")
                                    || files[i].getName().contains(contador + "_foto_lectura")
                                    || files[i].getName().contains(contador + "_foto_numero")
                                    || files[i].getName().contains(contador + "_foto_despues")) {
                                files[i].delete();
                            }
                        }
                    }
                }
                photo_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+photo_folder+ "/"+ photo_name+".jpg";
                file= new File(photo_path);
                if(file.exists()){
                    file.delete();
                }
                ImageReader.OnImageAvailableListener readListener = new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader imageReader) {
                        Image image = null;
                        try{
                            image = reader.acquireLatestImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);
                            save(bytes);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        finally {
                            {
                                if(image != null){
                                    image.close();
                                }
                            }
                        }
                    }
                    private void save(byte[] bytes) throws IOException{
                        OutputStream outputStream = null;
                        try{
                            outputStream = new FileOutputStream(file);
                            outputStream.write(bytes);
                        }finally {
                            if(outputStream != null){
                                outputStream.close();
                            }
                        }
                    }
                };
                reader.setOnImageAvailableListener(readListener, mBackgroundHandler);
                final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        playOnOffSound();
                        //Toast.makeText(Screen_Camera.this, "Saved "+file, Toast.LENGTH_LONG).show();
                        Intent sendFileAddressIntent = new Intent(Screen_Camera.this, Screen_Zoom_Photo.class);
                        sendFileAddressIntent.putExtra("zooming_photo", photo_path);
                        startActivityForResult(sendFileAddressIntent, PHOTO_PREVIEW_REQUEST);
                        createCameraPreview();
                    }
                };

                cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                        try {
                            cameraCaptureSession.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        Toast.makeText(Screen_Camera.this, "Fallo la configuracion de la camara", Toast.LENGTH_LONG).show();
                    }
                }, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void transformImage(int width, int height){
        if(textureView == null){
            return;
        }
        Matrix matrix = new Matrix();
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        RectF textureRectF = new RectF(0,0, width, height);
        RectF previewRectF = new RectF(0,0, imageDimension.getWidth(), imageDimension.getHeight());
        float centerX = textureRectF.centerX();
        float centerY = textureRectF.centerY();
        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){

            previewRectF.offset(centerX - previewRectF.centerX(), centerY- previewRectF.centerY());
            matrix.setRectToRect(textureRectF, previewRectF, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float)width/imageDimension.getWidth(),
                    (float)height / imageDimension.getHeight());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90*(rotation-2), centerX, centerY);
            textureView.setTransform(matrix);
        }
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(cameraDevice == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(Screen_Camera.this, "Fallo la configuracion de la camara", Toast.LENGTH_LONG).show();
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updatePreview() {
        if(cameraDevice == null){
            Toast.makeText(Screen_Camera.this, "Error", Toast.LENGTH_LONG).show();
            return;
        }
        if(isTorchOn){
            captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
        }else if(isFlashOn){
            captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
        }else{
            captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        }
        captureRequBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        captureRequBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequBuilder.build(), null, mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void openCamera(){
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            isFlashSupported = available == null ? false : available;
            setupTorchButton();

            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CAMERA_PERMISSION);

                return;
            }
            manager.openCamera(cameraId, stateCallback, null);

        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void openCameraFront(){

        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraId = manager.getCameraIdList()[1];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            isFlashSupported = available == null ? false : available;
            setupTorchButton();

            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CAMERA_PERMISSION);

                return;
            }
            manager.openCamera(cameraId, stateCallback, null);

        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    public void switchTorch() {
        try {
            if (cameraId.equals(CAMERA_BACK)) {
                if (isFlashSupported) {
                    if (isTorchOn) {
                        captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                        cameraCaptureSessions.setRepeatingRequest(captureRequBuilder.build(), null, null);
                        torchButton.setBackground(getDrawable(R.drawable.ic_highlight_white_24dp));
                        selectButton.setBackground(getDrawable(R.drawable.ic_highlight_white_24dp));
                        isTorchOn = false;
                    } else {
                        if(isFlashOn){
                            switchFlash();
                        }
                        captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                        cameraCaptureSessions.setRepeatingRequest(captureRequBuilder.build(), null, null);
                        torchButton.setBackground(getDrawable(R.drawable.ic_highlight_blue_24dp));
                        selectButton.setBackground(getDrawable(R.drawable.ic_highlight_blue_24dp));
                        isTorchOn = true;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void switchFlash() {
        try {
            if (cameraId.equals(CAMERA_BACK)) {
                if (isFlashSupported) {
                    if (isFlashOn) {
                        captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                        cameraCaptureSessions.setRepeatingRequest(captureRequBuilder.build(), null, null);
                        flashButton.setBackground(getDrawable(R.drawable.ic_flash_off_black_24dp));
                        selectButton.setBackground(getDrawable(R.drawable.ic_flash_off_black_24dp));
                        isFlashOn = false;
                    } else {
                        if(isTorchOn){
                            switchTorch();
                        }
                        captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                        cameraCaptureSessions.setRepeatingRequest(captureRequBuilder.build(), null, null);
                        flashButton.setBackground(getDrawable(R.drawable.ic_flash_on_black_24dp));
                        selectButton.setBackground(getDrawable(R.drawable.ic_flash_on_black_24dp));
                        isFlashOn = true;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void setupTorchButton() {
        if (isFlashSupported) {
            flashButton.setVisibility(View.VISIBLE);
            torchButton.setVisibility(View.VISIBLE);
            if (isTorchOn) {
                torchButton.setBackground(getDrawable(R.drawable.ic_highlight_blue_24dp));
            } else {
                torchButton.setBackground(getDrawable(R.drawable.ic_highlight_white_24dp));
            }
            if (isFlashOn) {
                flashButton.setBackground(getDrawable(R.drawable.ic_flash_on_black_24dp));
            } else {
                flashButton.setBackground(getDrawable(R.drawable.ic_flash_off_black_24dp));
            }

        } else {
            flashButton.setVisibility(View.GONE);
            torchButton.setVisibility(View.GONE);
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    private void playOnOffSound(){

        mp = MediaPlayer.create(Screen_Camera.this, R.raw.camera_click);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
            transformImage(textureView.getWidth(), textureView.getHeight());
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Screen_Camera.this, "No puedes usar la camara sin permisos", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        orientationListener.enable();
        startBackgroundThread();
        if(textureView.isAvailable()){
            openCamera();
        }
        else{
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        orientationListener.disable();
        closeCamera();
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        orientationListener.disable();
        stopBackgroundThread();
        closeCamera();
        super.onPause();
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler= null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler= new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
