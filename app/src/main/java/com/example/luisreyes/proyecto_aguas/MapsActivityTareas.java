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
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
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

public class MapsActivityTareas extends AppCompatActivity implements TaskCompleted, OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 18;
    private View mapView;
    private HashMap<String, Marker> tabla_marcadores = new HashMap<>();
    private HashMap<String, String> tabla_principal_variable = new HashMap<>();
    LatLng lastClickedMarker =null;


    Button btnlocalizacion, btn_abrir_tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_map_tareas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_tareas);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivityTareas.this);

        btnlocalizacion = (Button) findViewById(R.id.btnlocalizacion);
        btn_abrir_tarea = (Button) findViewById(R.id.btn_abrir_tarea);

        btnlocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               getDeviceLocation();


            }});

        btn_abrir_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coords = lastClickedMarker.toString().replace("lat/lng: (","").replace(")", "");
                String latLang = lastClickedMarker.toString();

                Log.e("Marker", coords);
                if(tabla_principal_variable.containsKey(coords)) {
                    try {
                        String princ_var = tabla_principal_variable.get(coords);
                        if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists() && coords != null) {
                            if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                                String tarea = team_or_personal_task_selection_screen_Activity.
                                        dBtareasController.get_one_tarea_from_Database(princ_var);
                                if (Screen_Login_Activity.checkStringVariable(tarea)) {
                                    JSONObject jsonObject = new JSONObject(tarea);
                                    if (jsonObject != null) {
                                        openTarea(jsonObject);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        team_or_personal_task_selection_screen_Activity.from_team_or_personal = team_or_personal_task_selection_screen_Activity.FROM_MAP_CERCANIA;

        btn_abrir_tarea.setVisibility(View.INVISIBLE);
    }



    public void openTarea(JSONObject jsonObject){
        Screen_Login_Activity.tarea_JSON = jsonObject;
        try {
            if (Screen_Login_Activity.tarea_JSON != null) {
                if (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.operario).trim().contains(
                        Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario).trim())) {
                    Screen_Table_Team.acceder_a_Tarea(MapsActivityTareas.this, team_or_personal_task_selection_screen_Activity.FROM_MAP_CERCANIA);//revisar esto
                    MapsActivityTareas.this.finish();
                } else {
                    new AlertDialog.Builder(MapsActivityTareas.this)
                            .setTitle("Cambiar Operario")
                            .setMessage("Esta tarea corresponde a otro operario\n¿Desea asignarse esta tarea?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.operario,
                                                Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario).trim());
                                    } catch (JSONException e) {
                                        Toast.makeText(MapsActivityTareas.this, "Error -> No pudo asignarse tarea a este operario", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                        return;
                                    }
                                    Screen_Table_Team.acceder_a_Tarea(MapsActivityTareas.this, team_or_personal_task_selection_screen_Activity.FROM_MAP_CERCANIA);
                                    MapsActivityTareas.this.finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
            } else {
                Toast.makeText(MapsActivityTareas.this, "Tarea nula", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MapsActivityTareas.this, "No pudo acceder a tarea Error -> " + e.toString(), Toast.LENGTH_SHORT).show();
        }
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

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivityTareas.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivityTareas.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(MapsActivityTareas.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapsActivityTareas.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
//        insertarMarcador("viva",43.2708400000000,-2.9437300000000);
     }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    @Override
    public void onMarkerDragStart(Marker marker) {


    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

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
//                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);//
                                        fillFilterGeolocalizacion();
//                                        Log.e("entre","entre");
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapsActivityTareas.this, "No se ha podido encontrar su localizacion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fillFilterGeolocalizacion() {
       ArrayList<String> tareas;
        double minimun_distance = 999999;
        String minimun_coords = "";
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {

            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject ;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            continue;
                        }
                        if (team_or_personal_task_selection_screen_Activity.from_team_or_personal
                                ==team_or_personal_task_selection_screen_Activity.FROM_PERSONAL) {
                            if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }

                        if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                            String geolocalizacion = jsonObject.getString(DBtareasController.geolocalizacion).trim();
                            String principal_var = jsonObject.getString(DBtareasController.principal_variable).trim();
                            String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                            double currentLatitud, currentLongitud;
                            currentLatitud = mLastKnownLocation.getLatitude();
                            currentLongitud = mLastKnownLocation.getLongitude();
                            if (!Screen_Login_Activity.checkStringVariable(geolocalizacion)) {
                                geolocalizacion = jsonObject.getString(DBtareasController.codigo_de_localizacion).trim();
                            }
                            if(Screen_Login_Activity.checkStringVariable(geolocalizacion))
                            {
                                String[] parts = geolocalizacion.split(",");
                                String part1 = parts[0].trim(); //obtiene: latitud
                                String part2 = parts[1].trim(); //obtiene: longitud
                                double latitud_h, longitud_h;
                                latitud_h = Double.parseDouble(part1);
                                longitud_h = Double.parseDouble(part2);

                                geolocalizacion = String.valueOf(latitud_h) + "," + String.valueOf(longitud_h);

                                if(!tabla_marcadores.containsKey(geolocalizacion)) {

                                    double current_distance = distanceInKmBetweenEarthCoordinates(latitud_h, longitud_h, currentLatitud, currentLongitud);
                                    if(current_distance < minimun_distance) {
                                        minimun_distance = current_distance;
                                        minimun_coords = geolocalizacion;
                                    }
                                    String dist = getDistanceString(latitud_h, longitud_h, currentLatitud, currentLongitud);

                                    Marker marker =  insertarMarcador(tipo_tarea, dist,latitud_h,longitud_h);
                                    tabla_marcadores.put(geolocalizacion, marker);
                                    tabla_principal_variable.put(geolocalizacion, principal_var);

                                }else{
                                    Marker marker = tabla_marcadores.get(geolocalizacion);
                                    String title_split = marker.getTitle().substring(0,1);

                                    if(Screen_Absent.checkOnlyNumbers(title_split)){
                                        Integer integer = Integer.parseInt(title_split);
                                        if(integer != null){
                                            integer++;
                                            String cant = integer.toString();
                                            String tittle =  cant + marker.getTitle().substring(1, marker.getTitle().length());
                                            marker.setTitle(tittle);
                                            tabla_marcadores.put(geolocalizacion, marker);
                                        }
                                    }else{
                                        String tittle =  "2 " + marker.getTitle();
                                        marker.setTitle(tittle);
                                        tabla_marcadores.put(geolocalizacion, marker);
                                    }
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("Minimun dist", String.valueOf(minimun_distance));
        Log.e("Minimun coords", minimun_coords);

        double zoom_d = log2((int)(40000 / (minimun_distance / 2)));
        int zoom_l = (int)(Math.floor(zoom_d));
        Log.e("zoom_d", String.valueOf(zoom_l));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), zoom_l-1));
    }

    public static int log2(int n){
        if(n <= 0) throw new IllegalArgumentException("TAREAS NO CARGADAS!");
        return 31 - Integer.numberOfLeadingZeros(n);
    }
    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double distanceInKmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
    private String getDistanceString(double lat1, double lon1, double lat2, double lon2){
        double distance = distanceInKmBetweenEarthCoordinates( lat1,  lon1,  lat2,  lon2);
        String dist;
        if(distance >= 1){
            dist = String.format("%.2f", distance ) + " Km";
        }else{
            dist = String.format("%.2f", distance * 1000) + " m";
        }
        return dist;
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
        btn_abrir_tarea.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = null;
        if(team_or_personal_task_selection_screen_Activity.from_screen == team_or_personal_task_selection_screen_Activity.FROM_TEAM) {
            intent = new Intent(this,team_task_screen_Activity.class);
        }else if(team_or_personal_task_selection_screen_Activity.from_screen == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL){
            intent = new Intent(this,personal_task_screen_Activity.class);
        }
        if(intent != null) {
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        btn_abrir_tarea.setVisibility(View.INVISIBLE);
    }
}



