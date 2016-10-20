package com.bottlerocketstudios.continuitysample.core.ui;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Methods that are required to communicate with an Activity that is trying to connect to Google
 * Play Services for a Location update.
 */
public interface LocationActivity {
    GoogleApiClient connectGoogleApiClient(LocationActivityListener locationActivityListener);
    void disconnectGoogleApiClient();
    void checkLocationSettings(LocationRequest locationRequest);
}
