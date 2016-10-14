package com.bottlerocketstudios.continuitysample.core.ui;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created on 9/16/16.
 */
public interface LocationActivity {
    GoogleApiClient connectGoogleApiClient(LocationActivityListener locationActivityListener);
    void disconnectGoogleApiClient();
    void checkLocationSettings(LocationRequest locationRequest);
}
