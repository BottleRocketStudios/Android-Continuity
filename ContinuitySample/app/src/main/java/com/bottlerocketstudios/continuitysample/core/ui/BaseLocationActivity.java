package com.bottlerocketstudios.continuitysample.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created on 9/16/16.
 */
public class BaseLocationActivity extends BaseActivity implements LocationActivity {

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 10254;
    private LocationActivityListener mLocationActivityListener;
    private GoogleApiClient mBaseGoogleApiClient;

    @Override
    public GoogleApiClient connectGoogleApiClient(LocationActivityListener locationActivityListener) {
        mLocationActivityListener = locationActivityListener;
        if (mBaseGoogleApiClient == null) {
            mBaseGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(mLocationActivityListener)
                    .enableAutoManage(this, mLocationActivityListener)
                    .build();
        }
        connectGoogleApi();
        return mBaseGoogleApiClient;
    }

    private void connectGoogleApi() {
        if (!mBaseGoogleApiClient.isConnected() && !mBaseGoogleApiClient.isConnecting()) {
            mBaseGoogleApiClient.connect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectGoogleApiClient();
    }

    @Override
    public void disconnectGoogleApiClient() {
        if (mBaseGoogleApiClient != null) {
            mBaseGoogleApiClient.disconnect();
        }
    }

    @Override
    public void checkLocationSettings(LocationRequest locationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mBaseGoogleApiClient, builder.build());
        result.setResultCallback(mLocationSettingsRequestResultCallback);
    }

    ResultCallback<LocationSettingsResult> mLocationSettingsRequestResultCallback = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(@NonNull LocationSettingsResult result) {
            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    mLocationActivityListener.onSettingsCheckSuccess();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        startResolvingSettingsProblem(status);
                    } catch (IntentSender.SendIntentException e) {
                        mLocationActivityListener.onSettingsCheckFailure();
                    }
                    break;
                default:
                    mLocationActivityListener.onSettingsCheckFailure();
                    break;
            }
        }
    };

    private void startResolvingSettingsProblem(Status status) throws IntentSender.SendIntentException {
        status.startResolutionForResult(this, REQUEST_CODE_LOCATION_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            onSettingResult(resultCode);
        }
    }

    public void onSettingResult(int resultCode) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                mLocationActivityListener.onSettingsCheckSuccess();
                break;
            default:
                mLocationActivityListener.onSettingsCheckFailure();
                break;
        }
    }
}
