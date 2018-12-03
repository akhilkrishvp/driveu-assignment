package com.driveu.driveutest.UI.Home;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.driveu.driveutest.Model.LocationModel;
import com.driveu.driveutest.R;
import com.driveu.driveutest.SharedPreferance.DriveUPref;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback,LocationView,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mMap;
    @BindView(R.id.fab)
    FloatingActionButton actionButton;
    Marker marker,currentLocMarker;
    MarkerOptions markerOptions,currentLocationMarkerOption;
    LocationPresenter locationPresenter ;
    Timer timer = new Timer();
    int count = 0;
    boolean isStarted = true;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String lat, lon;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initMap();
        initPresenter();
        hideOnscreenButtons();
        checkLocationPermission();

        if(DriveUPref.isServiceStarted()){
            updateMap();
            actionButton.setImageResource(R.drawable.stop);
        }
        else {
            actionButton.setImageResource(R.drawable.start);
        }
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStarted){
                    isStarted = false;
                    actionButton.setImageResource(R.drawable.stop);
                    DriveUPref.setStartService(true);
                    updateMap();

                }
                else {
                    isStarted = true;
                    actionButton.setImageResource(R.drawable.start);
                    DriveUPref.setStartService(false);
                    timer.cancel();
                }

            }
        });
    }
    public void initPresenter(){
        locationPresenter = new LocationPresenterImpl(this);
    }
    public void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void updateMap(){
        locationPresenter.getLatestLocation();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isNetworkAvailable()){
                    locationPresenter.getLatestLocation();
                }
            }
        },0,5000);
    }
    private void checkLocationPermission() {

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{mPermission}, REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            } else {
                buildGoogleApiClient();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED) {

                // Success Stuff here
                buildGoogleApiClient();
            } else {
                // Failure Stuff
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(12.8399,77.677);
        mMap = googleMap;
        marker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).position(latLng));
    }

    @Override
    public void onResponseSuccess(LocationModel locationModel) {
        LatLng latLng = new LatLng(locationModel.getLatitude(),locationModel.getLongitude());
        marker.setPosition(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));// Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void onResponseFailure(String msg) {

    }

    @Override
    public void retrofitError(String msg) {

    }
    public Boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every ten seconds

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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            setCurrentLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
      //  setCurrentLocation(latitude,longitude);
    }
    public void setCurrentLocation(double lat,double lon){
        LatLng latLng = new LatLng(lat,lon);
        currentLocationMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.loc));
        currentLocationMarkerOption.position(latLng);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        currentLocMarker = mMap.addMarker(currentLocationMarkerOption);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));// Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
    public void hideOnscreenButtons(){
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
