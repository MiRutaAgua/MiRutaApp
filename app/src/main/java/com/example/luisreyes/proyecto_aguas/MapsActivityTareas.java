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
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivityTareas extends AppCompatActivity implements TaskCompleted, OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private static ProgressDialog progressDialog;
    private GoogleMap mMap;
    private static CameraPosition lastCameraPosition = null;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Spinner spinner_filtro_zonas_screen_mapas_cercania;
    private static String lastZonaFiltered = "";
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 18;
    private View mapView;
    private HashMap<String, Marker> tabla_marcadores = new HashMap<>();
    private HashMap<String, String> tabla_prioridades = new HashMap<>();
    private HashMap<String, Integer> tabla_tipos_tareas_resumen = new HashMap<>();
    LatLng lastClickedMarker =null;
    boolean first_time_spinner_fill = true; //cuando relleno el spinner se activa el evento de cambio de texto y quiero que lo haga cuando interactue con el spinner nadamas

    Button btnlocalizacion, btn_abrir_tarea, btn_resumen_tareas, btnfiltrar_mapas_cercania;
    EditText editText_radius_screen_mapas_cercania;
    LinearLayout linearLayout_perimeter_mapas_cercania;
    private ArrayList<String> lista_desplegable_zonas = new ArrayList<>();

    private static Circle circle = null;
    static boolean last_perimeter_filtering = false;
    private static final double RADIUS_OF_EARTH_METERS = 6371009;
    private boolean perimeter_filtering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_map_tareas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_tareas);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivityTareas.this);

        linearLayout_perimeter_mapas_cercania = (LinearLayout) findViewById(R.id.linearLayout_perimeter_mapas_cercania);
        editText_radius_screen_mapas_cercania = (EditText) findViewById(R.id.editText_radius_screen_mapas_cercania);
        spinner_filtro_zonas_screen_mapas_cercania = (Spinner)findViewById(R.id.spinner_filtro_zonas_screen_mapas_cercania);
        btnlocalizacion = (Button) findViewById(R.id.btnlocalizacion);
        btn_abrir_tarea = (Button) findViewById(R.id.btn_abrir_tarea);
        btn_resumen_tareas = (Button) findViewById(R.id.btn_resumen_tareas);
        btnfiltrar_mapas_cercania = (Button) findViewById(R.id.btnfiltrar_mapas_cercania);

        spinner_filtro_zonas_screen_mapas_cercania.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!first_time_spinner_fill) {
                    String zona_selected = spinner_filtro_zonas_screen_mapas_cercania
                            .getSelectedItem().toString();
                    if (Screen_Login_Activity.checkStringVariable(zona_selected)) {
                        if(last_perimeter_filtering && circle != null){
                            fillFilterGeolocalizacion(circle.getCenter(), circle.getRadius(), zona_selected, (lastCameraPosition == null));
                            last_perimeter_filtering = false;
                        }else {
                            fillFilterGeolocalizacion(zona_selected, (lastCameraPosition == null));
                        }
                    }
                }
                first_time_spinner_fill= false;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        btnfiltrar_mapas_cercania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillFilterGeolocalizacion(circle.getCenter(), circle.getRadius(),
                        spinner_filtro_zonas_screen_mapas_cercania.getSelectedItem().toString(), true);
                linearLayout_perimeter_mapas_cercania.setVisibility(View.GONE);
            }
        });
        btnlocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDeviceLocation();


            }
        });

        btn_resumen_tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResumen();
            }
        });

        btn_abrir_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRingDialog("Abriendo Tareas...");
                String coords = lastClickedMarker.toString().replace("lat/lng: (","").replace(")", "");
                String latLang = lastClickedMarker.toString();

                Log.e("Marker", coords);
                if(tabla_marcadores.containsKey(coords)) {
                    openTareas(coords);
                }
            }
        });

        team_or_personal_task_selection_screen_Activity.from_team_or_personal = team_or_personal_task_selection_screen_Activity.FROM_MAP_CERCANIA;

        btn_abrir_tarea.setVisibility(View.INVISIBLE);

        showRingDialog("Cargando Tareas...");
    }


    public void openTareas(String coordenadas){
        Intent open_Filter_Results = new Intent(this, Screen_Filter_Results.class);
        open_Filter_Results.putExtra("from", "FILTER_MAPAS");
        open_Filter_Results.putExtra("filter_type", "MAPAS_GEOLOCALIZACION");
        open_Filter_Results.putExtra("tipo_tarea", "");
        open_Filter_Results.putExtra("calibre", "");
        open_Filter_Results.putExtra("poblacion", "");
        open_Filter_Results.putExtra("calle", "");
        open_Filter_Results.putExtra("portales", "");
        open_Filter_Results.putExtra("geolocalizacion", "");
        open_Filter_Results.putExtra("limitar_a_operario", false);
        open_Filter_Results.putExtra("coordenadas", coordenadas);
        startActivity(open_Filter_Results);
        lastCameraPosition = mMap.getCameraPosition();
        lastZonaFiltered = spinner_filtro_zonas_screen_mapas_cercania.getSelectedItem().toString();
        last_perimeter_filtering = perimeter_filtering;
        this.finish();
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
        editText_radius_screen_mapas_cercania.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()) {
                    String text = null;
                    try {
                        text = charSequence.toString();
                        Integer radius = Integer.parseInt(text);
                        if(radius <= 3000000) {
                            if (radius != null) {
                                if (circle != null) {
                                    circle.setRadius(radius);
                                }
                            }
                        }else{
                            editText_radius_screen_mapas_cercania.setText("3000000");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
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
    public void onMapLongClick(LatLng center) {

        linearLayout_perimeter_mapas_cercania.setVisibility(View.VISIBLE);

        if(circle!=null){
            circle.remove();
        }
        double radiusMeters = 1000;

        circle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(radiusMeters)
                .strokeWidth((int)(radiusMeters / 100))//ancho borde
                .strokeColor(0xffffffff) //color borde
                .fillColor(0x47368dcE));//color de circulo
    }

    private static LatLng toRadiusLatLng(LatLng center, double radiusMeters) {
        double radiusAngle = Math.toDegrees(radiusMeters / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
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
    private void showRingDialog(String text){
        if(progressDialog==null) {
            progressDialog = ProgressDialog.show(this, "Espere", text, true);
            progressDialog.setCancelable(false);
        }else{
            try {
                progressDialog.setMessage(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void hideRingDialog(){
        try {
            if(progressDialog!=null) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hideRingDialog", e.toString());
        }
    }
    public static String getValidCoords(JSONObject jsonObject){
        try {
            String geolocalizacion = jsonObject.getString(DBtareasController.codigo_de_localizacion).trim();
            if(!Screen_Login_Activity.checkStringVariable(geolocalizacion)){
                geolocalizacion = jsonObject.getString(DBtareasController.geolocalizacion).trim();
            }
            return geolocalizacion;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void fillFilterGeolocalizacion() {
        perimeter_filtering = false;
        ArrayList<String> tareas;
        double max_distance = 0;
        String max_coords = "";
        tabla_marcadores.clear();
        tabla_prioridades.clear();
        lista_desplegable_zonas.clear();
        tabla_tipos_tareas_resumen.clear();

        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {

            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database_Valid_Coords();
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
                            String geolocalizacion = getValidCoords(jsonObject);
                            if(Screen_Login_Activity.checkStringVariable(geolocalizacion))
                            {
                                String principal_var = jsonObject.getString(DBtareasController.principal_variable).trim();
                                String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                                if(tabla_tipos_tareas_resumen.containsKey(tipo_tarea)){
                                    int cant = tabla_tipos_tareas_resumen.get(tipo_tarea);
                                    cant++;
                                    tabla_tipos_tareas_resumen.put(tipo_tarea, cant);
                                }else{
                                    tabla_tipos_tareas_resumen.put(tipo_tarea, 1);
                                }
                                String prioridad_l = jsonObject.getString(DBtareasController.prioridad).trim();
                                if(!Screen_Login_Activity.checkStringVariable(prioridad_l)){
                                    prioridad_l = "MEDIA";
                                }
                                String zona = null;
                                try {
                                    zona = jsonObject.getString(DBtareasController.zona).trim();
                                    if(!lista_desplegable_zonas.contains(zona) && Screen_Login_Activity.checkStringVariable(zona)){
                                        lista_desplegable_zonas.add(zona);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                double currentLatitud, currentLongitud;
                                currentLatitud = mLastKnownLocation.getLatitude();
                                currentLongitud = mLastKnownLocation.getLongitude();

                                Pair<Double, Double> coords = convertFromGeoField(geolocalizacion);
                                double latitud_h = coords.first;
                                double longitud_h = coords.second;

                                geolocalizacion = convertToGeoField(latitud_h, longitud_h);

                                if(!tabla_marcadores.containsKey(geolocalizacion)) {

                                    double current_distance = distanceInKmBetweenEarthCoordinates(latitud_h, longitud_h, currentLatitud, currentLongitud);
                                    if(current_distance > max_distance) {
                                        max_distance = current_distance;
                                        max_coords = geolocalizacion;
                                    }
                                    String dist = getDistanceString(latitud_h, longitud_h, currentLatitud, currentLongitud);

                                    Marker marker =  insertarMarcador(prioridad_l, tipo_tarea, dist,latitud_h,longitud_h);
                                    tabla_marcadores.put(geolocalizacion, marker);
                                    tabla_prioridades.put(geolocalizacion, prioridad_l);

                                }else{
                                    Marker marker = tabla_marcadores.get(geolocalizacion);
                                    String old_priority = tabla_prioridades.get(geolocalizacion);
                                    if(comparePriorities(old_priority, prioridad_l)){
                                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(
                                                prioridad_l+"_priority_marker"/*"mano"*/, 80, 100)));
                                    }
                                    String[] title_split = marker.getTitle().split(",");
                                    if(title_split.length > 1){
                                        for(int n= 0; n < title_split.length; n++){
                                            String tipo_completo = title_split[n].trim();
                                            if(tipo_completo.contains("->")) {
                                                String tipo = tipo_completo.split("->")[1].trim();
                                                String number = tipo_completo.split("->")[0].trim();
                                                if (Screen_Absent.checkOnlyNumbers(number)) {
                                                    if (tipo.trim().equals(tipo_tarea)) {
                                                        Integer integer = Integer.parseInt(number);
                                                        if (integer != null) {
                                                            integer++;
                                                            String cant = integer.toString();
                                                            String tittle = cant + " -> " + tipo.trim();
                                                            title_split[n] = tittle;
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                if(tipo_completo.trim().equals(tipo_tarea)) {
                                                    String tittle = "2 -> " + tipo_completo;
                                                    title_split[n] = tittle;
                                                }
                                            }
                                        }
                                        String tittle = TextUtils.join(", ", title_split);
                                        marker.setTitle(tittle);
                                        tabla_marcadores.put(geolocalizacion, marker);
                                    }
                                    else{
                                        String tipo_completo = title_split[0];
                                        String tittle = "";
                                        if(tipo_completo.contains("->")) {
                                            String tipo = title_split[0].split("->")[1].trim();
                                            String number = title_split[0].split("->")[0].trim();
                                            if (Screen_Absent.checkOnlyNumbers(number)) {
                                                if (tipo.trim().equals(tipo_tarea)) {
                                                    Integer integer = Integer.parseInt(number);
                                                    if (integer != null) {
                                                        integer++;
                                                        String cant = integer.toString();
                                                        tittle = cant + " -> " + tipo.trim();
                                                    }
                                                } else {
                                                    tittle = tipo_completo + ", " + tipo_tarea;
                                                }
                                            }
                                        }else {
                                            if(tipo_completo.trim().equals(tipo_tarea)) {
                                                tittle = "2 -> " + tipo_completo;
                                            }else{
                                                tittle = tipo_completo + ", " + tipo_tarea;
                                            }
                                        }
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

                Collections.sort(lista_desplegable_zonas);
                lista_desplegable_zonas.add(0,"TODAS");
                ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_zonas);
                spinner_filtro_zonas_screen_mapas_cercania.setAdapter(arrayAdapter_spinner);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("Minimun dist", String.valueOf(max_distance));
        Log.e("Minimun coords", max_coords);

        double zoom_d = log2((int)(40000 / (max_distance / 2)));
        int zoom_l = (int)(Math.floor(zoom_d));
        Log.e("zoom_d", String.valueOf(zoom_l));
        if(lastCameraPosition != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCameraPosition.target,  lastCameraPosition.zoom));
            if(!lastZonaFiltered.isEmpty()){
                int index = lista_desplegable_zonas.indexOf(lastZonaFiltered);
                first_time_spinner_fill= false;
                spinner_filtro_zonas_screen_mapas_cercania.setSelection(index);
            }
        }else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), zoom_l - 1));
        }
        hideRingDialog();
    }
    private void fillFilterGeolocalizacion(String zona, boolean moveCamera) {
        perimeter_filtering = false;
        mMap.clear();
        ArrayList<String> tareas;
        double max_distance = 80000;
        String max_coords = "";
        tabla_marcadores.clear();
        tabla_prioridades.clear();
        tabla_tipos_tareas_resumen.clear();

        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {

            try {
                if(zona.equals("TODAS")){
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database_Valid_Coords();
                }else{
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database(DBtareasController.zona, zona);
                }
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
                            String geolocalizacion = getValidCoords(jsonObject);
                            if(Screen_Login_Activity.checkStringVariable(geolocalizacion))
                            {
                                String principal_var = jsonObject.getString(DBtareasController.principal_variable).trim();
                                String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                                if(tabla_tipos_tareas_resumen.containsKey(tipo_tarea)){
                                    int cant = tabla_tipos_tareas_resumen.get(tipo_tarea);
                                    cant++;
                                    tabla_tipos_tareas_resumen.put(tipo_tarea, cant);
                                }else{
                                    tabla_tipos_tareas_resumen.put(tipo_tarea, 1);
                                }
                                String prioridad_l = jsonObject.getString(DBtareasController.prioridad).trim();
                                if(!Screen_Login_Activity.checkStringVariable(prioridad_l)){
                                    prioridad_l = "MEDIA";
                                }

                                double currentLatitud, currentLongitud;
                                currentLatitud = mLastKnownLocation.getLatitude();
                                currentLongitud = mLastKnownLocation.getLongitude();

                                Pair<Double, Double> coords = convertFromGeoField(geolocalizacion);
                                double latitud_h = coords.first;
                                double longitud_h = coords.second;

                                geolocalizacion = convertToGeoField(latitud_h, longitud_h);

                                if(!tabla_marcadores.containsKey(geolocalizacion)) {

                                    double current_distance = distanceInKmBetweenEarthCoordinates(latitud_h, longitud_h, currentLatitud, currentLongitud);
                                    if(current_distance > max_distance) {
                                        max_distance = current_distance;
                                        max_coords = geolocalizacion;
                                    }
                                    String dist = getDistanceString(latitud_h, longitud_h, currentLatitud, currentLongitud);

                                    Marker marker =  insertarMarcador(prioridad_l, tipo_tarea, dist,latitud_h,longitud_h);
                                    tabla_marcadores.put(geolocalizacion, marker);
                                    tabla_prioridades.put(geolocalizacion, prioridad_l);

                                }else{

                                    Marker marker = tabla_marcadores.get(geolocalizacion);
                                    String old_priority = tabla_prioridades.get(geolocalizacion);
                                    if(comparePriorities(old_priority, prioridad_l)){
                                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(
                                                prioridad_l+"_priority_marker"/*"mano"*/, 80, 100)));
                                    }
                                    String[] title_split = marker.getTitle().split(",");
                                    if(title_split.length > 1){
                                        for(int n= 0; n < title_split.length; n++){
                                            String tipo_completo = title_split[n].trim();
                                            if(tipo_completo.contains("->")) {
                                                String tipo = tipo_completo.split("->")[1].trim();
                                                String number = tipo_completo.split("->")[0].trim();
                                                if (Screen_Absent.checkOnlyNumbers(number)) {
                                                    if (tipo.trim().equals(tipo_tarea)) {
                                                        Integer integer = Integer.parseInt(number);
                                                        if (integer != null) {
                                                            integer++;
                                                            String cant = integer.toString();
                                                            String tittle = cant + " -> " + tipo.trim();
                                                            title_split[n] = tittle;
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                if(tipo_completo.trim().equals(tipo_tarea)) {
                                                    String tittle = "2 -> " + tipo_completo;
                                                    title_split[n] = tittle;
                                                }
                                            }
                                        }
                                        String tittle = TextUtils.join(", ", title_split);
                                        marker.setTitle(tittle);

                                        tabla_marcadores.put(geolocalizacion, marker);
                                    }
                                    else{
                                        String tipo_completo = title_split[0];
                                        String tittle = "";
                                        if(tipo_completo.contains("->")) {
                                            String tipo = title_split[0].split("->")[1].trim();
                                            String number = title_split[0].split("->")[0].trim();
                                            if (Screen_Absent.checkOnlyNumbers(number)) {
                                                if (tipo.trim().equals(tipo_tarea)) {
                                                    Integer integer = Integer.parseInt(number);
                                                    if (integer != null) {
                                                        integer++;
                                                        String cant = integer.toString();
                                                        tittle = cant + " -> " + tipo.trim();
                                                    }
                                                } else {
                                                    tittle = tipo_completo + ", " + tipo_tarea;
                                                }
                                            }
                                        }else {
                                            if(tipo_completo.trim().equals(tipo_tarea)) {
                                                tittle = "2 -> " + tipo_completo;
                                            }else{
                                                tittle = tipo_completo + ", " + tipo_tarea;
                                            }
                                        }
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

        if(moveCamera) {
            Log.e("Minimun dist", String.valueOf(max_distance));
            Log.e("Minimun coords", max_coords);

            double zoom_d = log2((int) (40000 / (max_distance / 2)));
            int zoom_l = (int) (Math.floor(zoom_d));
            Log.e("zoom_d", String.valueOf(zoom_l));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), zoom_l - 1));
        }else{
            lastCameraPosition = null;
            lastZonaFiltered = "";
        }
    }

    private void fillFilterGeolocalizacion(LatLng center, double radius, String zona, boolean moveCamera) {
        perimeter_filtering = true;
        mMap.clear();
        ArrayList<String> tareas;
        double max_distance = 0;
        String max_coords = "";
        tabla_marcadores.clear();
        tabla_prioridades.clear();
        tabla_tipos_tareas_resumen.clear();

        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {

            try {
                if(zona.equals("TODAS")){
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database_Valid_Coords();
                }else{
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database(DBtareasController.zona, zona);
                }
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
                            String geolocalizacion = getValidCoords(jsonObject);
                            if(Screen_Login_Activity.checkStringVariable(geolocalizacion))
                            {
                                double currentLatitud, currentLongitud;
                                currentLatitud = mLastKnownLocation.getLatitude();
                                currentLongitud = mLastKnownLocation.getLongitude();

                                Pair<Double, Double> coords = convertFromGeoField(geolocalizacion);
                                double latitud_h = coords.first;
                                double longitud_h = coords.second;

                                geolocalizacion = convertToGeoField(latitud_h, longitud_h);

                                double radius_geoCode = toRadiusMeters(center, new LatLng(latitud_h, longitud_h));

                                if(radius_geoCode > radius){
                                    continue;
                                }

                                String principal_var = jsonObject.getString(DBtareasController.principal_variable).trim();
                                String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                                if(tabla_tipos_tareas_resumen.containsKey(tipo_tarea)){
                                    int cant = tabla_tipos_tareas_resumen.get(tipo_tarea);
                                    cant++;
                                    tabla_tipos_tareas_resumen.put(tipo_tarea, cant);
                                }else{
                                    tabla_tipos_tareas_resumen.put(tipo_tarea, 1);
                                }
                                String prioridad_l = jsonObject.getString(DBtareasController.prioridad).trim();
                                if(!Screen_Login_Activity.checkStringVariable(prioridad_l)){
                                    prioridad_l = "MEDIA";
                                }



                                if(!tabla_marcadores.containsKey(geolocalizacion)) {

                                    double current_distance = distanceInKmBetweenEarthCoordinates(latitud_h, longitud_h, currentLatitud, currentLongitud);
                                    if(current_distance > max_distance) {
                                        max_distance = current_distance;
                                        max_coords = geolocalizacion;
                                    }
                                    String dist = getDistanceString(latitud_h, longitud_h, currentLatitud, currentLongitud);

                                    Marker marker =  insertarMarcador(prioridad_l, tipo_tarea, dist,latitud_h,longitud_h);
                                    tabla_marcadores.put(geolocalizacion, marker);
                                    tabla_prioridades.put(geolocalizacion, prioridad_l);

                                }else{

                                    Marker marker = tabla_marcadores.get(geolocalizacion);
                                    String old_priority = tabla_prioridades.get(geolocalizacion);
                                    if(comparePriorities(old_priority, prioridad_l)){
                                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(
                                                prioridad_l+"_priority_marker"/*"mano"*/, 80, 100)));
                                    }
                                    String[] title_split = marker.getTitle().split(",");
                                    if(title_split.length > 1){
                                        for(int n= 0; n < title_split.length; n++){
                                            String tipo_completo = title_split[n].trim();
                                            if(tipo_completo.contains("->")) {
                                                String tipo = tipo_completo.split("->")[1].trim();
                                                String number = tipo_completo.split("->")[0].trim();
                                                if (Screen_Absent.checkOnlyNumbers(number)) {
                                                    if (tipo.trim().equals(tipo_tarea)) {
                                                        Integer integer = Integer.parseInt(number);
                                                        if (integer != null) {
                                                            integer++;
                                                            String cant = integer.toString();
                                                            String tittle = cant + " -> " + tipo.trim();
                                                            title_split[n] = tittle;
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                if(tipo_completo.trim().equals(tipo_tarea)) {
                                                    String tittle = "2 -> " + tipo_completo;
                                                    title_split[n] = tittle;
                                                }
                                            }
                                        }
                                        String tittle = TextUtils.join(", ", title_split);
                                        marker.setTitle(tittle);

                                        tabla_marcadores.put(geolocalizacion, marker);
                                    }
                                    else{
                                        String tipo_completo = title_split[0];
                                        String tittle = "";
                                        if(tipo_completo.contains("->")) {
                                            String tipo = title_split[0].split("->")[1].trim();
                                            String number = title_split[0].split("->")[0].trim();
                                            if (Screen_Absent.checkOnlyNumbers(number)) {
                                                if (tipo.trim().equals(tipo_tarea)) {
                                                    Integer integer = Integer.parseInt(number);
                                                    if (integer != null) {
                                                        integer++;
                                                        String cant = integer.toString();
                                                        tittle = cant + " -> " + tipo.trim();
                                                    }
                                                } else {
                                                    tittle = tipo_completo + ", " + tipo_tarea;
                                                }
                                            }
                                        }else {
                                            if(tipo_completo.trim().equals(tipo_tarea)) {
                                                tittle = "2 -> " + tipo_completo;
                                            }else{
                                                tittle = tipo_completo + ", " + tipo_tarea;
                                            }
                                        }
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

        if(moveCamera) {
            Log.e("Minimun dist", String.valueOf(max_distance));
            Log.e("Minimun coords", max_coords);

            double zoom_d = log2((int) (40000 / (max_distance / 2)));
            int zoom_l = (int) (Math.floor(zoom_d));
            Log.e("zoom_d", String.valueOf(zoom_l));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), zoom_l - 1));
        }else{
            lastCameraPosition = null;
            lastZonaFiltered = "";
        }
    }

    public static String convertToGeoField(double latitud, double longitud){
        return String.valueOf(latitud) + "," + String.valueOf(longitud);
    }
    public static Pair<Double, Double> convertFromGeoField(String geolocalizacion){ //first latitud, second longitud
        double latitud = -1000, longitud = -1000;
        String[] parts = geolocalizacion.split(",");
        if(parts.length > 1) {
            String part1 = parts[0].trim(); //obtiene: latitud
            String part2 = parts[1].trim(); //obtiene: longitud
            latitud = Double.parseDouble(part1);
            longitud = Double.parseDouble(part2);
        }
        Pair<Double, Double> coords = new Pair<>(latitud, longitud);
        return coords;
    }
    public static boolean comparePriorities(String old_priority , String new_priority){ //true si la nueva es mas prioritaria
        HashMap<String, Integer> prioridades = new HashMap<>();
        prioridades.put("HIBERNAR", 1);
        prioridades.put("BAJA", 2);
        prioridades.put("MEDIA", 3);
        prioridades.put("ALTA", 4);
        if(prioridades.containsKey(old_priority) && prioridades.containsKey(new_priority)) {
            if (prioridades.get(old_priority) < prioridades.get(new_priority)) {
                return true;
            }
        }
        return false;
    }
    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

    private void showResumen(){
        String resumen = "";
        Iterator it = tabla_tipos_tareas_resumen.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            resumen+= (pair.getValue().toString() + " -> " + pair.getKey().toString()) + "\n";
        }
        openMessage("Resumen",resumen);
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
    private Marker insertarMarcador(String priority, String titulo, String snippet_parameter, double latitud, double longitud) {
        priority = priority.toLowerCase();
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitud, longitud))
                .title(titulo)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(priority+"_priority_marker"/*"mano"*/, 80, 100)))
                .snippet(snippet_parameter));
        return marker;
    }


    private Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = null;
        try {
            resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizedBitmap;
    }

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
        lastCameraPosition = null;
        finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        btn_abrir_tarea.setVisibility(View.INVISIBLE);
    }
}



