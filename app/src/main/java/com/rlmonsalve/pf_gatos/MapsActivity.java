package com.rlmonsalve.pf_gatos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rlmonsalve.pf_gatos.data.Gato;
import com.rlmonsalve.pf_gatos.data.Objeto;
import com.rlmonsalve.pf_gatos.data.Sitio;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener{

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    TinyDB tinydb;

    Typeface typeface;
    Button Close;
    TextView title, text;
    ImageView sitePic, imgSite1, imgSite2;
    int catMarkerCounter;
    float anchor;
    double variationLat, variationLng;
    LatLng latLng, latLngCat, latLngSpot;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    private FloatingActionsMenu mMultipleActions;
    private View mCollection;
    private View mSettings;
    private View mGatopedia;
    ArrayList<Objeto> objList;
    ArrayList<Sitio> siteList;
    ArrayList<Gato> gatoList;
    Location currentLocation;
    Marker currLocationMarker, catMarker, spotMarker1, spotMarker2, spotMarker3, spotMarker4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tinydb = new TinyDB(this);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Muli-Bold.ttf");

        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);

        mMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mGatopedia = findViewById(R.id.action_gatopedia);
        mCollection = findViewById(R.id.action_collection);
        mSettings = findViewById(R.id.action_settings);
        mGatopedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultipleActions.collapse();
                startGatopediaActivity(v);
            }
        });
        mCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultipleActions.collapse();
                startCollectionActivity(v);
            }
        });
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMultipleActions.collapse();
                startSettingsActivity(v);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
        //mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        //mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMarkerClickListener(this);

        buildGoogleApiClient();

        mGoogleApiClient.connect();

    }

    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            currentLocation=mLastLocation;
            catMarkerCounter =0;
            anchor = (float) 0.5;
            addSpotMarker(mLastLocation);
            //addCatMarker(mLastLocation);
            //addSpecialCatMarker(mLastLocation);
            catMarkerCounter++;
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());


            currLocationMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Current Position")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_personal))
                    .flat(true)
                    .anchor(anchor,anchor));
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(4000); //5 seconds
        mLocationRequest.setFastestInterval(2000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        verifyFirstTime();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        float bear;
        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        if (calculateDistance(currentLocation,location)){
            bear = calculateBearing(currentLocation,location);
        }else{
            bear = mGoogleMap.getCameraPosition().bearing;
        }
        //Toast.makeText(MapsActivity.this, "Bearing: "+bear, Toast.LENGTH_SHORT).show();// display toast

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currLocationMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_personal))
                .flat(true)
                .rotation(bear)
                .anchor(anchor,anchor));

        //Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:

        float b = mGoogleMap.getCameraPosition().bearing;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(20).tilt(90).bearing(b).build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        currentLocation=location;
        Random rand = new Random();
        int n = rand.nextInt(100);


        if( n <= 10 && n>=0){
            if(catMarkerCounter < 4) {
                catMarkerCounter++;
                addCatMarker(location);
            }
        }

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    public void startCameraActivity(View view){
        Intent intent = new Intent(MapsActivity.this,
                CameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void startGatoActivity(){
        catMarker.remove();
        catMarkerCounter=0;
        Intent intent = new Intent(MapsActivity.this,
                GatoActivity.class);
        startActivity(intent);
        finish();
    }

    public void startProfileActivity(View view){
        Intent intent = new Intent(MapsActivity.this,
                ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void startGatopediaActivity(View view){
        Intent intent = new Intent(MapsActivity.this,
                GatopediaActivity.class);
        startActivity(intent);
        finish();
    }

    private void startCollectionActivity(View view){
        Intent intent = new Intent(MapsActivity.this,
                CollectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void startSettingsActivity(View view){
        Intent intent = new Intent(MapsActivity.this,
                SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void verifyFirstTime(){
        if (tinydb.getBoolean("firstrun_map")) {
            // Do first run stuff here then set 'firstrun' as false
            showTutorialPopup();
            initializeObjectData();
            initializeCatData();
            initializeSiteData();
            // using the following line to edit/commit prefs
            tinydb.putBoolean("firstrun_map", false);
        }else{
            objList=tinydb.getListObjectItem("Objetos_List",Objeto.class);
            siteList=tinydb.getListObjectSite("Sitios_List",Sitio.class);
            gatoList=tinydb.getListObject("Gatos_List",Gato.class);
        }
    }

    public void addCatMarker(Location location){
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        variationLat = Math.random() * 0.002;
        variationLng = Math.random() * 0.002;
        double catLat = 11.018 + variationLat;
        double carLng = -74.8517 + variationLng;
        latLngCat = new LatLng(catLat, carLng);
        catMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngCat)
                .title("Cat")
                .snippet("This is cat")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.silueta_gato_mapa)));
    }

    public void addSpecialCatMarker(Location location){
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        catMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Cat")
                .snippet("This is cat")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.silueta_gato_mapa)));
    }

    public void addSpotMarker(Location location){
        double spotLat = 11.019178;
        double spotLng = -74.849916;
        latLngSpot = new LatLng(spotLat, spotLng);
        spotMarker1 = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngSpot)
                .title("Bosque Seco Tropical")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_marker_azul)));

        spotLat = 11.019630;
        spotLng = -74.850753;
        latLngSpot = new LatLng(spotLat, spotLng);
        spotMarker2 = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngSpot)
                .title("Plaza Bloque Idiomas")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_marker_azul)));
        spotLat = 11.017976;
        spotLng = -74.850931;
        latLngSpot = new LatLng(spotLat, spotLng);
        spotMarker3 = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngSpot)
                .title("Roble Amarillo")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_marker_azul)));
        spotLat = 11.019822;
        spotLng = -74.851253;
        latLngSpot = new LatLng(spotLat, spotLng);
        spotMarker4 = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngSpot)
                .title("Plaza Bloque K")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_marker_azul)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (checkDistance(marker)){
            if(marker.getTitle().equals("Cat")){ // if marker source is clicked
                //Toast.makeText(MapsActivity.this, "Cat Event", Toast.LENGTH_SHORT).show();// display toast
                startGatoActivity();
                return true;
            }
            if(marker.getTitle().equals("Bosque Seco Tropical")){ // if marker source is clicked
                //Toast.makeText(MapsActivity.this, "BST event", Toast.LENGTH_SHORT).show();// display toast
                showPopup(marker.getTitle(),siteList.get(0));
                return true;
            }
            if(marker.getTitle().equals("Plaza Bloque Idiomas")){ // if marker source is clicked
                //Toast.makeText(MapsActivity.this, "PBI event", Toast.LENGTH_SHORT).show();// display toast
                showPopup(marker.getTitle(),siteList.get(1));
                return true;
            }
            if(marker.getTitle().equals("Roble Amarillo")){ // if marker source is clicked
                //Toast.makeText(MapsActivity.this, "PBI event", Toast.LENGTH_SHORT).show();// display toast
                showPopup(marker.getTitle(),siteList.get(2));
                return true;
            }
            if(marker.getTitle().equals("Plaza Bloque K")){ // if marker source is clicked
                //Toast.makeText(MapsActivity.this, "PBI event", Toast.LENGTH_SHORT).show();// display toast
                showPopup(marker.getTitle(),siteList.get(3));
                return true;
            }
        }else {
            Toast.makeText(MapsActivity.this, "Estas demasiado lejos!", Toast.LENGTH_SHORT).show();// display toast
            return true;
        }

        return false;


    }

    private float calculateBearing(Location loc1, Location loc2){
        double lat1 = loc1.getLatitude();
        double lng1 = loc1.getLongitude();
        double lat2 = loc2.getLatitude();
        double lng2 = loc2.getLongitude();

        double dLon = (lng2-lng1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
        float brng = (float) Math.toDegrees((Math.atan2(y, x)));
        brng = (360 - ((brng + 360) % 360));

        return brng;
    }

    private boolean calculateDistance(Location loc1, Location loc2){
        float[] results = new float[1];
        Location.distanceBetween( loc1.getLatitude(), loc1.getLongitude(),
                loc2.getLatitude(), loc2.getLongitude(),
                results);
        float distance = results[0];
        if (distance>1){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkDistance(Marker marker){
        float[] results = new float[1];
        Location.distanceBetween( currentLocation.getLatitude(), currentLocation.getLongitude(),
                marker.getPosition().latitude, marker.getPosition().longitude,
                results);
        float distance = results[0];
        if (distance<50){
            return true;
        }else{
            return false;
        }
    }

    private PopupWindow pw;
    private void showPopup(String pointTitle,Sitio site) {
        try {
// We need to get the instance of the LayoutInflater
            final Objeto obj1 = objList.get(site.getObjetcList().get(0).getId());
            final Objeto obj2 = objList.get(site.getObjetcList().get(1).getId());
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            LayoutInflater inflater = (LayoutInflater) MapsActivity.this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_punto_eco,
                    (ViewGroup) findViewById(R.id.popup_1));
            int mHeight = (int) ((displaymetrics.heightPixels)*0.75);
            int mWidth = (int) ((displaymetrics.widthPixels)*0.9);
            pw = new PopupWindow(layout, mWidth, mHeight, true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            sitePic = (ImageView) layout.findViewById(R.id.imageViewPunto);
            text = (TextView) layout.findViewById(R.id.textTextPunto);
            title = (TextView) layout.findViewById(R.id.textTitlePunto);
            imgSite1 = (ImageView) layout.findViewById(R.id.imageItem1);
            imgSite2 = (ImageView) layout.findViewById(R.id.imageItem2);
            Close = (Button) layout.findViewById(R.id.close_popup);

            text.setMovementMethod(new ScrollingMovementMethod());
            Close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int st1= obj1.getStock();
                    int st2= obj2.getStock();
                    st1++;
                    st2++;
                    obj1.setStock(st1);
                    obj2.setStock(st2);
                    savePrefs();
                    closePopup();
                }
            });

            sitePic.setImageResource(site.getImgId());
            title.setText(pointTitle);
            title.setTypeface(typeface);
            imgSite1.setImageResource(site.getObjetcList().get(0).getIconId());
            imgSite2.setImageResource(site.getObjetcList().get(1).getIconId());
            text.setText(site.getInfo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTutorialPopup(){
        final PopupWindowTutorial popupWindow = new PopupWindowTutorial(this,1);
        popupWindow.show(findViewById(R.id.activity_maps), 0, -100);
    }

    /*private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };*/

    private void closePopup(){
        pw.dismiss();
    }

    private void savePrefs(){
        tinydb.putListObjectItem("Objetos_List",objList);
    }

    private void initializeObjectData(){
        objList=new ArrayList<Objeto>();
        objList.add(new Objeto("Comida para Gatos",(R.drawable.catfood),0,5,true));
        objList.add(new Objeto("Comida Enlatada",(R.drawable.food_cans),1,5,true));
        objList.add(new Objeto("Acariciar",(R.drawable.mano_acariciar),2,-1,true));
        objList.add(new Objeto("Peine",(R.drawable.peine),3,-1,true));
        objList.add(new Objeto("Cascabel",(R.drawable.cascabel),4,3,true));
        objList.add(new Objeto("Ratón",(R.drawable.ratondejugete),5,3,true));
        objList.add(new Objeto("???",(R.drawable.interrogacion_gris),6,-1,false));
        objList.add(new Objeto("???",(R.drawable.interrogacion_gris),7,-1,false));
        objList.add(new Objeto("???",(R.drawable.interrogacion_gris),8,-1,false));
        tinydb.putListObjectItem("Objetos_List",objList);
    }

    private void initializeSiteData(){
        siteList=new ArrayList<Sitio>();
        ArrayList<Objeto> site1List = new ArrayList<Objeto>();
        site1List.add(objList.get(0));
        site1List.add(objList.get(4));
        ArrayList<Objeto> site2List = new ArrayList<Objeto>();
        site2List.add(objList.get(1));
        site2List.add(objList.get(5));
        ArrayList<Objeto> site3List = new ArrayList<Objeto>();
        site3List.add(objList.get(4));
        site3List.add(objList.get(5));
        ArrayList<Objeto> site4List = new ArrayList<Objeto>();
        site4List.add(objList.get(0));
        site4List.add(objList.get(1));
        double spotLat = 11.019178;
        double spotLng = -74.849916;
        LatLng latLngSpot1 = new LatLng(spotLat, spotLng);
        spotLat = 11.019630;
        spotLng = -74.850753;
        LatLng latLngSpot2 = new LatLng(spotLat, spotLng);
        spotLat = 11.017976;
        spotLng = -74.850931;
        LatLng latLngSpot3 = new LatLng(spotLat, spotLng);
        spotLat = 11.019822;
        spotLng = -74.851253;
        LatLng latLngSpot4 = new LatLng(spotLat, spotLng);
        siteList.add(new Sitio("Bosque Seco Tropical",latLngSpot1,site1List,R.drawable.eco_point_img,this.getResources().getString(R.string.frente_plaza_info)));
        siteList.add(new Sitio("Plaza Bloque Idiomas",latLngSpot2,site2List,R.drawable.bloquei,this.getResources().getString(R.string.plaza_idiomas)));
        siteList.add(new Sitio("Roble Amarillo",latLngSpot3,site3List,R.drawable.robleamarillo,this.getResources().getString(R.string.roble_amarillo_info)));
        siteList.add(new Sitio("Plaza Bloque K",latLngSpot4,site4List,R.drawable.plazoletak,this.getResources().getString(R.string.plaza_k_info)));
        tinydb.putListObjectSite("Sitios_List",siteList);
    }

    private void initializeCatData(){
        gatoList = new ArrayList<Gato>();
        gatoList.add(new Gato("Electrón",R.drawable.electron_symbol,R.drawable.cat_model_1,false,true,objList.get(2),objList.get(5)));
        gatoList.add(new Gato("Acuarela",R.drawable.acuarela_symbol,R.drawable.cat_model_3,false,true,objList.get(5),objList.get(3)));
        gatoList.add(new Gato("Malefico",R.drawable.malefico_symbol,R.drawable.cat_model_2,false,true,objList.get(0),objList.get(2)));
        gatoList.add(new Gato("???",R.drawable.silueta_gato_gatopedia,R.drawable.cat_model_1,false,false,objList.get(7),objList.get(7)));
        gatoList.add(new Gato("???",R.drawable.silueta_gato_gatopedia,R.drawable.cat_model_1,false,false,objList.get(7),objList.get(7)));
        gatoList.add(new Gato("???",R.drawable.silueta_gato_gatopedia,R.drawable.cat_model_1,false,false,objList.get(7),objList.get(7)));
        gatoList.add(new Gato("???",R.drawable.silueta_gato_gatopedia,R.drawable.cat_model_1,false,false,objList.get(7),objList.get(7)));
        tinydb.putListObject("Gatos_List",gatoList);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        pw.dismiss();
        finish();
    }
}
