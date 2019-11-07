package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import org.json.JSONException;

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
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements TaskCompleted, OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private MaterialSearchBar materialSearchBar;
    private View mapView;

    Button btnOpciones,btnlocal;
    public int home = 1;
    public int pomp ;
    RadioGroup radioGroup;
    private int count = 0;
    private MarkerOptions markerHome = new MarkerOptions() ;
    private MarkerOptions markerPump = new MarkerOptions();
    private LatLng coordenates;
    private LatLng coordenates_pump;
    private boolean init = false;
    private boolean marker = false;


    private final float DEFAULT_ZOOM = 15;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_map);

        materialSearchBar = findViewById(R.id.searchBar);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        Places.initialize(MapActivity.this, getString(R.string.google_maps_api));
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setCountry("ES")
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i("etiqueta", "No se encontro ningun lugar");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("etiqueta", "Lugar no encontrado: " + place.getName());
                        LatLng latLngOfPlace = place.getLatLng();
                        if (latLngOfPlace != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                            MarkerOptions markerOptions = new MarkerOptions();


                            mMap.addMarker(new MarkerOptions().position(latLngOfPlace).title("Marker in Sydney"));

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                            Log.i("etiqueta", "Lugar no encontrado: " + e.getMessage());
                            Log.i("etiqueta", "estado: " + statusCode);
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        btnOpciones = (Button) findViewById(R.id.btnOpciones);

        btnOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapActivity.this,Maps_Box.class);
                startActivity(intent);
                finish();

            }});

        btnlocal = (Button) findViewById(R.id.btnlocal);

        btnlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (materialSearchBar.isSuggestionsVisible())
                    materialSearchBar.clearSuggestions();
                if (materialSearchBar.isSearchEnabled())
                    materialSearchBar.disableSearch();
                marker=false;
                getDeviceLocation();

            }});

        radioGroup = (RadioGroup)findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio1:
                        home = 1;
                        pomp = 0;
                        break;

                    case R.id.radio2:
                        home = 0;
                        pomp = 1;
                        break;

                }

            }
        });



    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);

//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
//            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            layoutParams.setMargins(0, 0, 40, 180);
//
//
//        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(MapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

//        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//
//                if (materialSearchBar.isSuggestionsVisible())
//                    materialSearchBar.clearSuggestions();
//                if (materialSearchBar.isSearchEnabled())
//                    materialSearchBar.disableSearch();
//                return false;
//            }
//        });

        try {
            String coor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.geolocalizacion).trim();
            //Toast.makeText(MapsActivity.this, coor, Toast.LENGTH_LONG).show();

            //Log.e(coor, "basedatos");

            if (coor.contains(",") && (!coor.contains("null")) && (!coor.contains("NULL"))){

                String[] parts = coor.split(",");
                String part1 = parts[0]; //obtiene: latitud
                String part2 = parts[1]; //obtiene: longitud
                double latitud_h, longitud_h;
                latitud_h = Double.parseDouble(part1);
                longitud_h = Double.parseDouble(part2);
                coordenates = new LatLng(latitud_h,longitud_h);

                if(markerHome.getPosition() != null){
                    mMap.clear();
                    if(markerPump.getPosition() != null){
                        mMap.addMarker(markerPump);}
                }

                String icon = "casa";
                //markerHome.position(coordenates).draggable(true).title("Marcador de casa").snippet("Posicion de casa").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 100, 100)));
                markerHome.position(coordenates).draggable(true).title("Marcador de casa").snippet("Posicion de casa").icon(bitmapDescriptorFromVector(this, R.drawable.ic_home_black_24dp));
                mMap.addMarker(markerHome);
                marker=true;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenates, DEFAULT_ZOOM));



                Toast.makeText(MapActivity.this, "Cargando Marcadores", Toast.LENGTH_LONG).show();}

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MapActivity.this, "No pudo Obtener marcador", Toast.LENGTH_LONG).show();
        }

        try {
            String coor1 = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.codigo_de_localizacion).trim();
            //Toast.makeText(MapsActivity.this, coor1, Toast.LENGTH_LONG).show();
            //Log.e(coor1, "basedatos");

            if (coor1.contains(",") && (!coor1.contains("null")) && (!coor1.contains("NULL")))  {
                String[] parts = coor1.split(",");
                String part3 = parts[0]; //obtiene: latitud
                String part4 = parts[1]; //obtiene: longitud
                double latitud_h, longitud_h;
                latitud_h = Double.parseDouble(part3);
                longitud_h = Double.parseDouble(part4);

                coordenates_pump = new LatLng(latitud_h,longitud_h);

                if(markerPump.getPosition() != null){
                    mMap.clear();
                    if(markerHome.getPosition() != null){
                        mMap.addMarker(markerHome);}
                }
                String icon = "mano";
                markerPump.position(coordenates_pump).draggable(true).title("Marcador de bateria").snippet("Posicion de bateria").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 100, 100)));
//                markerPump.position(coordenates_pump).draggable(true).title("Marcador de bateria").snippet("Posicion de bateria").icon(bitmapDescriptorFromVector(this, R.drawable.ic_location_on_blue_24dp));

                mMap.addMarker(markerPump);

                Toast.makeText(MapActivity.this, "Cargando Marcador", Toast.LENGTH_LONG).show();}

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MapActivity.this, "No pudo Obtener Marcador", Toast.LENGTH_LONG).show();
        }

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
                                if (!marker){
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM)); }
                            } else {
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
                                        if (!marker){
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM)); }
                                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
//
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapActivity.this, "No se ha podido encontrar su localizacion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;}


    public void alert(){
        AlertDialog.Builder builder= new AlertDialog.Builder(MapActivity.this);
        builder.setMessage("Localizacion no encontrada")
                .setTitle("Vuelva a intentarlo mas tarde")
                .setCancelable(false)
                .setNegativeButton(" Salir", new DialogInterface.OnClickListener() {
                    @Override
                   public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void mostrar(LatLng latLng)
    {
        // con este tema personalizado evitamos los bordes por defecto
        Dialog customDialog = new Dialog(MapActivity.this,R.style.Theme_AppCompat_Light_Dialog);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //no obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(true);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_signin);



        ((Button) customDialog.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if(home==1){
                    //MarkerOptions markerHome = new MarkerOptions();

                    if(markerHome.getPosition() != null){
                        mMap.clear();
                        if(markerPump.getPosition() != null){
                            mMap.addMarker(markerPump);}
                   }
                    String icon = "casa";
                    //markerHome.position(latLng).draggable(true).title("Marcador de casa").snippet("Posicion de casa").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 100, 100)));
                    markerHome.position(latLng).draggable(true).title("Marcador de casa").snippet("Posicion de casa").icon(bitmapDescriptorFromVector(MapActivity.this, R.drawable.ic_home_black_24dp));
                    try {
                        //Screen_Login_Activity.tarea_JSON.put("geolocalizacion", latLng.toString());

                        String latLang_string_geolocalizacion = latLng.toString();
                        latLang_string_geolocalizacion = latLang_string_geolocalizacion.replace("lat/lng: (","").replace(")", "");
                        Screen_Login_Activity.tarea_JSON.put("geolocalizacion", latLang_string_geolocalizacion);

                        saveData();
                        //Toast.makeText(MapsActivity.this, latLang_string_geolocalizacion, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MapActivity.this, "No pudo guardar geolocalizacion", Toast.LENGTH_LONG).show();

                    }
                    mMap.addMarker(markerHome);}

                else if (pomp == 1 ){
                    //MarkerOptions markerPump = new MarkerOptions();

                    if(markerPump.getPosition() != null){
                        mMap.clear();
                        if(markerHome.getPosition() != null){
                            mMap.addMarker(markerHome);}

                    }
                    String icon = "mano";
                    markerPump.position(latLng).draggable(true).title("Marcador de contador").snippet("Posicion de contador").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 100, 100)));
//                    markerPump.position(latLng).draggable(true).title("Marcador de bateria").snippet("Posicion de bateria").icon(bitmapDescriptorFromVector(MapActivity.this, R.drawable.ic_location_on_blue_24dp));

                    try {
                        //Screen_Login_Activity.tarea_JSON.put("codigo_de_localizacion", latLng.toString());

                        String latLang_string_geolocalizacion = latLng.toString();
                        latLang_string_geolocalizacion = latLang_string_geolocalizacion.replace("lat/lng: (","").replace(")", "");

                        Screen_Login_Activity.tarea_JSON.put("codigo_de_localizacion", latLang_string_geolocalizacion);

                        saveData();
                        //Toast.makeText(MapsActivity.this, latLang_string_geolocalizacion, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MapActivity.this, "No pudo guardar geolocalizacion de contador", Toast.LENGTH_LONG).show();

                    }
                    mMap.addMarker(markerPump);
                }

                customDialog.dismiss();

            }
        });

        ((Button) customDialog.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                if(home==1){
                    //MarkerOptions markerHome = new MarkerOptions();
                    if(markerHome.getPosition() != null){
                        mMap.clear();
                        if(markerPump.getPosition() != null){
                            mMap.addMarker(markerPump);}

                    }


                    double latitude = mMap.getMyLocation().getLatitude();
                    double longitude = mMap.getMyLocation().getLongitude();
                    LatLng prueba = new LatLng(latitude,longitude);

                   try {
                        //Screen_Login_Activity.tarea_JSON.put("geolocalizacion", prueba.toString());
                        String latLang_string_geolocalizacion = prueba.toString();
                        latLang_string_geolocalizacion = latLang_string_geolocalizacion.replace("lat/lng: (","").replace(")", "");
                        Screen_Login_Activity.tarea_JSON.put("geolocalizacion", latLang_string_geolocalizacion);

                        saveData();
                        //Toast.makeText(MapsActivity.this, latLang_string_geolocalizacion, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MapActivity.this, "No pudo guardar geolocalizacion", Toast.LENGTH_LONG).show();

                    }
                    String icon = "casa";
                    //markerHome.position(prueba).draggable(true).title("Marcador de casa").snippet("Posicion de casa").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 100, 100)));
                    markerHome.position(prueba).draggable(true).title("Marcador de casa").snippet("Posicion de casa").icon(bitmapDescriptorFromVector(MapActivity.this, R.drawable.ic_home_black_24dp));
                    mMap.addMarker(markerHome);
                }
                else if(pomp == 1){
                    //MarkerOptions markerPomp = new MarkerOptions();
                    if(markerPump.getPosition() != null){
                        mMap.clear();

                        if(markerHome.getPosition() != null){
                           mMap.addMarker(markerHome);}

                    }


                    double latitude = mMap.getMyLocation().getLatitude();
                    double longitude = mMap.getMyLocation().getLongitude();
                    LatLng prueba = new LatLng(latitude,longitude);

                    try {
                        //Screen_Login_Activity.tarea_JSON.put("codigo_de_localizacion", prueba.toString());
                        String latLang_string_geolocalizacion = prueba.toString();
                        latLang_string_geolocalizacion = latLang_string_geolocalizacion.replace("lat/lng: (","").replace(")", "");

                        Screen_Login_Activity.tarea_JSON.put("codigo_de_localizacion", latLang_string_geolocalizacion);

                        saveData();
                        //Toast.makeText(MapsActivity.this, latLang_string_geolocalizacion, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MapActivity.this, "No pudo guardar geolocalizacion de contador", Toast.LENGTH_LONG).show();

                    }
                    String icon = "mano";
                    markerPump.position(prueba).draggable(true).title("Marcador de contador").snippet("Posicion de contador").icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, 100, 100)));
//                    markerPump.position(prueba).draggable(true).title("Marcador de contador").snippet("Posicion de contador").icon(bitmapDescriptorFromVector(MapActivity.this, R.drawable.ic_location_on_blue_24dp));

                    mMap.addMarker(markerPump);}
                customDialog.dismiss();
           }
        });

        customDialog.show();
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mostrar(latLng);
    }

    public void saveData() {
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified, DBtareasController.getStringFromFechaHora(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean error=false;
        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            try {
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            } catch (JSONException e) {
                Toast.makeText(this, "No se pudo guardar tarea local " + e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
                error = true;
            }
        }else{
            error = true;
            Toast.makeText(this, "No hay tabla donde guardar", Toast.LENGTH_LONG).show();
        }
        if(checkConection()) {
            showRingDialog("Guardando Datos...");
            String type = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type);
        } else{
            if(!error)
                Toast.makeText(this, "No hay conexion se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "update_tarea") {
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(this, "No hay conexion a Internet, no se pudo guardar geolocalizacion. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                } else {
                    if (result.contains("not success")) {
                        Toast.makeText(this, "No se pudo guardar geolocalizacion correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();

                    } else {
                        if(result.contains("success ok")) {
                            Toast.makeText(this, "Geolocalizacion guardada correctamente en el servidor", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
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
        progressDialog = ProgressDialog.show(MapActivity.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_location);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(35, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 35);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {



    }

    @Override
    public void onMarkerDrag(Marker marker) {



    }

    @Override
    public void onMarkerDragEnd(Marker marker) {



        if ((marker.getTitle()).equals("Marcador de casa")) {

            LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

            try {


                String latLang_string_geolocalizacion = latLng.toString();
                latLang_string_geolocalizacion = latLang_string_geolocalizacion.replace("lat/lng: (","").replace(")", "");
                Screen_Login_Activity.tarea_JSON.put("geolocalizacion", latLang_string_geolocalizacion);
                saveData();
                Toast.makeText(MapActivity.this, "Guardando geolocalizacion", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MapActivity.this, "No pudo guardar geolocalizacion", Toast.LENGTH_LONG).show();

            }}


        else if ((marker.getTitle()).equals("Marcador de contador"))

        {


             LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

            try {



                String latLang_string_geolocalizacion = latLng.toString();
                latLang_string_geolocalizacion = latLang_string_geolocalizacion.replace("lat/lng: (","").replace(")", "");

                Screen_Login_Activity.tarea_JSON.put("codigo_de_localizacion", latLang_string_geolocalizacion);
                saveData();
                Toast.makeText(MapActivity.this, "Guardando geolocalizacion de contador", Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MapActivity.this, "No pudo guardar geolocalizacion de contador", Toast.LENGTH_LONG).show();

            }




        }

    }


}