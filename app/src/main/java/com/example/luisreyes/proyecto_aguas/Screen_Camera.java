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
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
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
import android.text.TextUtils;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

public class Screen_Camera extends Activity {
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

    private Button button_cancel_picture_screen_x, button_save_picture_screen_x;
    private Button button_take_picture_screen_x;
    private Button flashButton, torchButton;
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
    private boolean isTorchOn;
    private boolean isFlashOn;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_camera);

        photo_name = getIntent().getStringExtra("photo_name");
        photo_folder = getIntent().getStringExtra("photo_folder");
        contador = getIntent().getStringExtra("contador");

        textureView = (TextureView)findViewById(R.id.textureView);

        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        button_take_picture_screen_x = (Button)findViewById(R.id.button_take_picture);
        button_save_picture_screen_x = (Button)findViewById(R.id.button_save_picture);
        button_cancel_picture_screen_x = (Button)findViewById(R.id.button_cancel_picture);
        flashButton = (Button)findViewById(R.id.button_flash_picture);
        torchButton = (Button)findViewById(R.id.button_torch_picture);

        torchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                switchTorch();
            }
        });
        button_save_picture_screen_x.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("photo_path", photo_path);
                setResult(RESULT_OK, resultIntent);
                finish();
                closeCamera();
            }
        });

        button_cancel_picture_screen_x.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                button_save_picture_screen_x.setVisibility(View.GONE);
                button_cancel_picture_screen_x.setVisibility(View.GONE);
                torchButton.setVisibility(View.VISIBLE);
                flashButton.setVisibility(View.VISIBLE);
                button_take_picture_screen_x.setVisibility(View.VISIBLE);
                createCameraPreview();
            }
        });
        button_take_picture_screen_x.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                if(photo_name != null && !TextUtils.isEmpty(photo_name)) {
                    button_save_picture_screen_x.setVisibility(View.VISIBLE);
                    button_cancel_picture_screen_x.setVisibility(View.VISIBLE);
                    torchButton.setVisibility(View.GONE);
                    flashButton.setVisibility(View.GONE);
                    button_take_picture_screen_x.setVisibility(View.GONE);
                    playOnOffSound();
                    takePicture();
                }
                else{
                    Toast.makeText(Screen_Camera.this, "No hay nombre de foto", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                            Toast.makeText(Screen_Camera.this, "Width: "+width+"    "+"Height: "+height, Toast.LENGTH_LONG).show();
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
                }else{
                    captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                }
//                if(isFlashOn){
//                    captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
//                }else{
//                    captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
//                }

                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

                File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+photo_folder);
                if(!myDir.exists()){
                    myDir.mkdir();
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
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        //Toast.makeText(Screen_Camera.this, "Saved "+file, Toast.LENGTH_LONG).show();
                        //createCameraPreview();
                    }
                };

                cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {

                        try {
                            captureRequBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                                    CameraMetadata.CONTROL_AF_TRIGGER_START);
                            mState = STATE_WAITING_LOCK;
                            cameraCaptureSession.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                    }
                }, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                    Toast.makeText(Screen_Camera.this, "Changed", Toast.LENGTH_LONG).show();
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void switchTorch() {
        try {
//            if (cameraId.equals(CAMERA_BACK)) {
            if (isFlashSupported) {
                if (isTorchOn) {
//                        captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
//                        cameraCaptureSessions.setRepeatingRequest(captureRequBuilder.build(), null, null);
                    torchButton.setBackground(getDrawable(R.drawable.ic_highlight_white_24dp));
                    isTorchOn = false;
                } else {
                    captureRequBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
//                        //captureRequBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//                        //captureRequBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
//                        //captureRequBuilder.set(CaptureRequest.AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                    cameraCaptureSessions.setRepeatingRequest(captureRequBuilder.build(), null, null);
                    torchButton.setBackground(getDrawable(R.drawable.ic_highlight_blue_24dp));
                    isTorchOn = true;
                }
            }
//            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupTorchButton() {
        if (isFlashSupported) {
            torchButton.setVisibility(View.VISIBLE);

            if (isTorchOn) {
                torchButton.setBackground(getDrawable(R.drawable.ic_highlight_blue_24dp));
            } else {
                torchButton.setBackground(getDrawable(R.drawable.ic_highlight_white_24dp));
            }

        } else {
            torchButton.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        if(isTorchOn){
            switchTorch();
        }
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        stopBackgroundThread();
        if(isTorchOn){
            switchTorch();
        }
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
}
