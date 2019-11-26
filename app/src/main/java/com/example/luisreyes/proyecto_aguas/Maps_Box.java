package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class Maps_Box extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener , PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private static final int LOCATION_REQUEST = 500;
    private LocationComponent locationComponent;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "MainActiviy";
    DirectionsRoute currentRoute;
    private Point destinationPoint;
    private Point originPoint;
    private LatLng coordenates = new LatLng(0,0);
    private LatLng coordenates_pump = new LatLng(0,0);
    View view;
    private LatLng home;
    private boolean mapa = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.maps_box);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        view=findViewById(R.id.button);

    }

    public void startNavigationBtnClick(View v)
    {
        if (mapa){



            try {
                String coor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.geolocalizacion).trim();
                String coor1 = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.codigo_de_localizacion).trim();
                //Toast.makeText(Maps_Box.this, coor, Toast.LENGTH_LONG).show();
                if (coor.contains(",") && (!coor.contains("null")) && (!coor.contains("NULL"))    && (locationComponent.getLastKnownLocation() != null) ){
                    String[] parts = coor.split(",");
                    String part1 = parts[0]; //obtiene: latitud
                    String part2 = parts[1]; //obtiene: longitud
                    double latitud_h, longitud_h;
                    latitud_h = Double.parseDouble(part1);
                    longitud_h = Double.parseDouble(part2);
                    coordenates = new LatLng(latitud_h,longitud_h);
                    Toast.makeText(Maps_Box.this, "Cargando Ruta", Toast.LENGTH_LONG).show();
                    getcoor();}else {
                    if (coor1.contains(",") && (!coor1.contains("null")) && (!coor1.contains("NULL"))    && (locationComponent.getLastKnownLocation() != null) ){
                        String[] parts1 = coor1.split(",");
                        String part11 = parts1[0]; //obtiene: latitud
                        String part12 = parts1[1]; //obtiene: longitud
                        double latitud_h, longitud_h;
                        latitud_h = Double.parseDouble(part11);
                        longitud_h = Double.parseDouble(part12);
                        coordenates = new LatLng(latitud_h,longitud_h);
                        Toast.makeText(Maps_Box.this, "Cargando Ruta", Toast.LENGTH_LONG).show();
                        getcoor();
                    }
                }


                   // Toast.makeText(Maps_Box.this, coor, Toast.LENGTH_LONG).show();
                   // }

//                String coor1 = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.codigo_de_localizacion).trim();
//                //Toast.makeText(Maps_Box.this, coor, Toast.LENGTH_LONG).show();
//                if (coor1.contains(",") && (!coor1.contains("null")) && (!coor1.contains("NULL"))    && (locationComponent.getLastKnownLocation() != null) ){
//                    String[] parts = coor.split(",");
//                    String part1 = parts[0]; //obtiene: latitud
//                    String part2 = parts[1]; //obtiene: longitud
//                    double latitud_h, longitud_h;
//                    latitud_h = Double.parseDouble(part1);
//                    longitud_h = Double.parseDouble(part2);
//                    coordenates_pump = new LatLng(latitud_h,longitud_h);
//                    // Toast.makeText(Maps_Box.this, coor, Toast.LENGTH_LONG).show();
//                }
//
//                if (coordenates.getLatitude() != 0.0 && coordenates.getLongitude() !=0.0){
//                    Toast.makeText(Maps_Box.this, "Cargando Ruta", Toast.LENGTH_LONG).show();
//
//                    getcoor();
//                }else {
//                    if (coordenates_pump.getLatitude() != 0.0 && coordenates_pump.getLongitude() !=0.0){
//                        Toast.makeText(Maps_Box.this, "Cargando Ruta", Toast.LENGTH_LONG).show();
//                        coordenates = coordenates_pump;
//                        getcoor();
//
                    //}
//                }







            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Maps_Box.this, "No pudo Obtener geolocalizacion", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(Maps_Box.this, "Espere a q cargue el mapa", Toast.LENGTH_LONG).show();}

    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        //this.mapboxMap.setMinZoomPreference(15);

        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                addDestinationLayer(style);
                mapboxMap.addOnMapClickListener(Maps_Box.this);
                mapa = true;

            }
        });
    }

    private void addDestinationLayer(Style style) {

        style.addImage("destination-icon-id", BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));

        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        style.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id","destination-source-id");
        destinationSymbolLayer.withProperties(iconImage("destination-icon-id"),iconAllowOverlap(true),
                iconIgnorePlacement(true));

        style.addLayer(destinationSymbolLayer);

    }



    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationComponent = mapboxMap.getLocationComponent();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                return;
            }
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);

        }

        else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted){
            enableLocationComponent(mapboxMap.getStyle());
        }else{
            Toast.makeText(getApplicationContext(),"permission not granted",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return true;
    }

    public void getcoor(){

//        double latitud_h = 23.1330200 ;
//        double longitud_h = -82.3830400 ;
//        coordenates = new LatLng(latitud_h,longitud_h);
        destinationPoint =  Point.fromLngLat(coordenates.getLongitude(),coordenates.getLatitude());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        }
        originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");

        if(source!=null){
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        if (destinationPoint != null){
            getRoute(originPoint,destinationPoint,view);}
    }


    private void getRoute(Point origin, Point destination, View view){

        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null){
                            Log.e(TAG,"No routes founds, check right user and acces token");
                            return;
                        }else if (response.body().routes().size() == 0){
                            Log.e(TAG, "Ruta no encontrada, intentelo nuevamente");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        if(navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        }else{
                            navigationMapRoute = new NavigationMapRoute(null, mapView,mapboxMap);
                        }

                        navigationMapRoute.addRoute((currentRoute));
                        view.setBackgroundColor(getResources().getColor(R.color.mapboxBlue));
                        boolean simulateRoute = false;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();
                        NavigationLauncher.startNavigation(Maps_Box.this,options);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG,"Error:"+t.getMessage()+"intentelo nuevamente");
                    }
                });
    }



    @SuppressWarnings("MissingPermision")
    @Override
    protected void onStart() {
        super.onStart();
//        if(locationEngine != null){
//            locationEngine.requestLocationUpdates();
//        }
//        if (locationLayerPlugin != null)
//        { locationLayerPlugin.onStart();}
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(locationEngine != null){
//            locationEngine.removeLocationUpdates();
//        }
//        if (locationLayerPlugin != null)
//        { locationLayerPlugin.onStop();}
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(locationEngine != null){
//            locationEngine.deactivate();
//        }

        mapView.onDestroy();
    }


}
