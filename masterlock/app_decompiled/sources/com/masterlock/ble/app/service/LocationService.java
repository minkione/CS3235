package com.masterlock.ble.app.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.p000v4.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.masterlock.ble.app.MasterLockSharedPreferences;

public class LocationService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private MasterLockSharedPreferences mPreferences;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void onConnectionSuspended(int i) {
    }

    public void onDestroy() {
        super.onDestroy();
        GoogleApiClient googleApiClient = this.mGoogleApiClient;
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    public boolean isGpsEnabled() {
        return ((LocationManager) getApplicationContext().getSystemService("location")).isProviderEnabled("gps");
    }

    public boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") == 0;
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
    }

    public void onConnected(Bundle bundle) {
        if (isLocationPermissionGranted() && isGpsEnabled()) {
            startLocationUpdates();
        }
    }

    public void onLocationChanged(Location location) {
        this.mPreferences.putLatitude(String.valueOf(location.getLatitude()));
        this.mPreferences.putLongitude(String.valueOf(location.getLongitude()));
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (this.mGoogleApiClient == null) {
            this.mGoogleApiClient = new Builder(getApplicationContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
        this.mLocationRequest = LocationRequest.create().setPriority(100).setInterval(10000).setFastestInterval(1000);
        this.mGoogleApiClient.connect();
        this.mPreferences = MasterLockSharedPreferences.getInstance();
        return 0;
    }
}
