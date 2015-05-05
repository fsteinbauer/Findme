package com.acid.findme;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import com.acid.findme.exception.LocationException;

/**
 */
public class LocationUtil implements ConnectionCallbacks, OnConnectionFailedListener  {
    public static final String LOCATION_UTIL_TAG = "LocationUtil";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public LocationUtil(Context applicationContext){
        mGoogleApiClient = new GoogleApiClient.Builder(applicationContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);



    }

    @Override
    public void onConnectionSuspended(int i) {
        // todo

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // todo

    }

}
