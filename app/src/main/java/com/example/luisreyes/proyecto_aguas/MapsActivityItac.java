package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;


import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Dialog;
import android.content.DialogInterface;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivityItac extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 18;
    private View mapView;

    LatLng lastClickedMarker =null;


    Button btnlocalizacion, btn_abrir_tarea;
    private MarkerOptions markerItac = new MarkerOptions();
    private LatLng latLngItac = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_map_itac);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_itac);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivityItac.this);
        btnlocalizacion = (Button)findViewById(R.id.btnlocalizacion_mapa_itac);

        btnlocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivityItac.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivityItac.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(MapsActivityItac.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapsActivityItac.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        setearMarcador(latLng);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {


    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {

                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        if (latLngItac == null) {
                                            double latitud;
                                            double longitud;
                                            String geocode = null;
                                            try {
                                                geocode = Screen_Login_Activity.itac_JSON.getString(DBitacsController.geolocalizacion);
                                                if (Screen_Login_Activity.checkStringVariable(geocode)) {
                                                    setMarker(geocode);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }else {
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        }
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        } else {
                            Toast.makeText(MapsActivityItac.this, "No se ha podido encontrar su localizacion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setearMarcador(LatLng latLng)
    {
        // con este tema personalizado evitamos los bordes por defecto
        Dialog customDialog = new Dialog(this,R.style.Theme_AppCompat_Light_Dialog);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //no obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(true);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_marker_options);



        ((Button) customDialog.findViewById(R.id.button_marker_location)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if(markerItac.getPosition() != null){
                    mMap.clear();
                }
                String icon = "mano";
                markerItac.position(latLng).draggable(true).title("Marcador de casa").snippet("Posicion de casa").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 84, 100)));
                latLngItac= latLng;
                mMap.addMarker(markerItac);

                customDialog.dismiss();
            }
        });

        ((Button) customDialog.findViewById(R.id.button_current_location)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if(markerItac.getPosition() != null){
                    mMap.clear();
                }
                double latitude = mMap.getMyLocation().getLatitude();
                double longitude = mMap.getMyLocation().getLongitude();
                latLngItac = new LatLng(latitude,longitude);
                String icon = "mano";
                markerItac.position(latLngItac).draggable(true).title("Marcador de Itac").snippet("Posicion de Itac").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 84, 100)));
                mMap.addMarker(markerItac);

                customDialog.dismiss();
            }
        });
        customDialog.show();
    }
    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        latLngItac = latLng;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void saveMarker(){
        if (markerItac.getPosition() != null){
            try {
                if (latLngItac != null) {
                    String latLang_string_geolocalizacion = latLngItac.toString();
                    latLang_string_geolocalizacion = latLang_string_geolocalizacion.replace("lat/lng: (", "").replace(")", "");
                    Screen_Login_Activity.itac_JSON.put(DBitacsController.geolocalizacion, latLang_string_geolocalizacion);
                    updateGeoCodeTareas(latLang_string_geolocalizacion);
                    int id =Screen_Login_Activity.itac_JSON.getInt(DBitacsController.id);
                    if (id > 0) {
                        if(!Screen_Edit_ITAC.updateItac()){
                            Toast.makeText(this, "No pudo guardar geolocalizacion", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "No pudo guardar geolocalizacion", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void updateGeoCodeTareas(String geocode){
        try {
            String cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac);
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.canLookInsideTable(this)) {
                ArrayList<String> tareas = new ArrayList<>();
                try {
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database(DBtareasController.codigo_de_geolocalizacion, cod_emplazamiento);
                    for (int i = 0; i < tareas.size(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(tareas.get(i));
                            jsonObject.put(DBtareasController.codigo_de_localizacion, geocode);
                            jsonObject.put(DBtareasController.url_geolocalizacion, "https://maps.google.com/?q="+geocode);
                            team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setMarker(String coor1){
        if (coor1.contains(",")) {

            String[] parts = coor1.split(",");
            if (parts.length > 1) {
                String part3 = parts[0]; //obtiene: latitud
                String part4 = parts[1]; //obtiene: longitud
                double latitud_h, longitud_h;
                latitud_h = Double.parseDouble(part3);
                longitud_h = Double.parseDouble(part4);

                LatLng coordenates = new LatLng(latitud_h, longitud_h);
                latLngItac = coordenates;
                if (markerItac.getPosition() != null) {
                    mMap.clear();
                }
                String icon = "mano";
                markerItac.position(coordenates).draggable(true).title("Marcador de bateria").snippet("Posicion de bateria").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 80, 100)));
                mMap.addMarker(markerItac);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenates, DEFAULT_ZOOM));
                Toast.makeText(this, "Cargando Marcador", Toast.LENGTH_SHORT).show();
            }
        }        
    }


    private Marker insertarMarcador(String titulo, String snippet_parameter, double latitud, double longitud) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitud, longitud))
                .title(titulo)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("mano", 80, 100)))
                .snippet(snippet_parameter));
        return marker;
    }


    private Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;}

    @Override
    public boolean onMarkerClick(Marker marker) {
        lastClickedMarker = marker.getPosition();
        Log.e("LastMarkerClicked", lastClickedMarker.toString());
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        saveMarker();
        finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}



