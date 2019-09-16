package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;

import me.dm7.barcodescanner.core.ZXingScannerView;


public class lector extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    Boolean encendida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector);
        ZXingScannerView zscanner = (ZXingScannerView) findViewById(R.id.zxscan);
        mScannerView = new ZXingScannerView(this);

        //setContentView(mScannerView);                // Set the scanner view as the content view

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(lector.this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        zscanner.addView(mScannerView);
        final Button linterna = (Button)  findViewById(R.id.button_flash);

        linterna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (encendida){
                    encendida = false;
                    linterna.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_linterna),null,null);
                    linterna.setText("Encender");
                    mScannerView.setFlash(false);
                } else {
                    encendida = true;
                    linterna.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_linterna_encendida),null,null);
                    linterna.setText("Apagar");
                    mScannerView.setFlash(true);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

       // Log.i("QR_response>>",rawResult.getText());

        Intent intent_open_screen_exec_task =new Intent(lector.this,Screen_Execute_Task.class);
        intent_open_screen_exec_task.putExtra("result", rawResult.getText().toString());
        setResult(RESULT_OK, intent_open_screen_exec_task);
        finish();

    }
}
