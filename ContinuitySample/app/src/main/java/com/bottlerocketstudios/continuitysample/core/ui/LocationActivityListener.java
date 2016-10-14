package com.bottlerocketstudios.continuitysample.core.ui;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created on 9/16/16.
 */
public interface LocationActivityListener extends GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    void onSettingsCheckSuccess();
    void onSettingsCheckFailure();
}
