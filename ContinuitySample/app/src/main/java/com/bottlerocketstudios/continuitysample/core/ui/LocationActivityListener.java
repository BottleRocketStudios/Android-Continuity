package com.bottlerocketstudios.continuitysample.core.ui;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Callbacks from a LocationActivity when various hurdles are cleared or slammed into with the whole
 * Google Play Services, Location Settings process.
 */
public interface LocationActivityListener extends GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    void onSettingsCheckSuccess();
    void onSettingsCheckFailure();
}
