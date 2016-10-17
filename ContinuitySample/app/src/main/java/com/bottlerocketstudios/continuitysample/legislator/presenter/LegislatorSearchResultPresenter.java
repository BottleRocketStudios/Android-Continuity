package com.bottlerocketstudios.continuitysample.legislator.presenter;

import android.Manifest;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.core.injection.Injectable;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;
import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;
import com.bottlerocketstudios.continuitysample.core.ui.LocationActivity;
import com.bottlerocketstudios.continuitysample.core.ui.LocationActivityListener;
import com.bottlerocketstudios.continuitysample.core.util.ListReplacer;
import com.bottlerocketstudios.continuitysample.legislator.domain.LegislatorRepository;
import com.bottlerocketstudios.continuitysample.legislator.mapping.LegislatorMapper;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;
import com.bottlerocketstudios.continuitysample.legislator.usecase.GetLegislatorsByCoordinatesAgent;
import com.bottlerocketstudios.continuitysample.legislator.usecase.GetLegislatorsByNameAgent;
import com.bottlerocketstudios.continuitysample.legislator.usecase.GetLegislatorsByZipAgent;
import com.bottlerocketstudios.continuitysample.legislator.usecase.SetFavoriteLegislatorByBioguideId;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorSearchResultViewModel;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;
import com.bottlerocketstudios.groundcontrol.listener.FunctionalAgentListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Created on 9/16/16.
 */
public class LegislatorSearchResultPresenter implements ContinuousObject {
    private static final String TAG = LegislatorSearchResultPresenter.class.getSimpleName();

    private LegislatorRepository mLegislatorRepository;
    private Listener mListener;
    private long mLocationRequestTimestamp;
    private LocationActivity mLocationActivity;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private LegislatorSearchResultViewModel mLegislatorSearchResultViewModel = new LegislatorSearchResultViewModel();
    private ObservableList<LegislatorViewModel> mLegislatorViewModelList = new ObservableArrayList<>();
    private SearchMode mSearchMode;
    private String mQuery;

    public LegislatorSearchResultPresenter() {
        ServiceInjector.injectWithType(LegislatorRepository.class, new Injectable<LegislatorRepository>() {
            @Override
            public void receiveInjection(LegislatorRepository injection) {
                mLegislatorRepository = injection;
            }
        });
    }

    public void bindListener(Listener listener) {
        mListener = listener;
        mListener.bindLegislatorSearchViewModel(mLegislatorSearchResultViewModel);
        mListener.bindLegislatorList(mLegislatorViewModelList);
    }

    @Override
    public void onContinuityAnchorDestroyed(Object anchor) {
        mListener = null;
        mLocationActivity = null;
    }

    @Override
    public void onContinuityDiscard() {
        GroundControl.onDestroy(this);
    }

    public void favoriteTapped(final LegislatorViewModel legislatorViewModel) {
        boolean newFavoriteState = !legislatorViewModel.isFavorite();
        GroundControl.uiAgent(this, new SetFavoriteLegislatorByBioguideId(mLegislatorRepository, legislatorViewModel.getBioguideId(), !legislatorViewModel.isFavorite()))
                .uiCallback(new FunctionalAgentListener<ResponseContainer<Legislator>, Void>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<Legislator> result) {
                        if (result.isSuccess()) {
                            legislatorViewModel.setFavorite(result.getValue().isFavorite());
                        }
                    }
                })
                .execute();
        legislatorViewModel.setFavorite(newFavoriteState);
    }

    public void legislatorTapped(View photo, LegislatorViewModel legislatorViewModel) {
        if (mListener != null) {
            mListener.launchLegislatorDetail(photo, legislatorViewModel);
            mLegislatorSearchResultViewModel.setSearchStarted(false);
        }
    }

    public void onResume(SearchMode searchMode, String query) {
        mSearchMode = searchMode;
        mQuery = query;
        if (!mLegislatorSearchResultViewModel.isSearchStarted()) {
            mLegislatorSearchResultViewModel.setSearchStarted(true);
            switch (mSearchMode) {
                case LOCATION:
                    startLocationSearch();
                    break;
                case NAME:
                    startNameSearch();
                    break;
                case ZIP_CODE:
                    startZipCodeSearch();
                    break;
            }
        }
    }

    private void setSearchInProgress(boolean searchInProgress) {
        mLegislatorSearchResultViewModel.setSearchInProgress(searchInProgress);
    }

    private void startZipCodeSearch() {
        setSearchInProgress(true);
        GroundControl.uiAgent(this, new GetLegislatorsByZipAgent(mLegislatorRepository, mQuery))
                .uiCallback(mSearchAgentListener)
                .execute();
    }

    private void startNameSearch() {
        setSearchInProgress(true);
        GroundControl.uiAgent(this, new GetLegislatorsByNameAgent(mLegislatorRepository, mQuery))
                .uiCallback(mSearchAgentListener)
                .execute();
    }

    private void startLocationSearch() {
        if (!mListener.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (mListener.shouldShowPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                mListener.showPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);
            } else {
                requestLocationPermission();
            }
        } else {
            continueLocationSearch();
        }
    }

    public void permissionRationaleAgreed() {
        requestLocationPermission();
    }

    private void requestLocationPermission() {
        mLocationRequestTimestamp = SystemClock.uptimeMillis();
        mListener.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public void handlePermissionResult(String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[i])) {
                handleLocationPermissionResponse(grantResults[i] == PermissionChecker.PERMISSION_GRANTED);
            }
        }
    }

    private void handleLocationPermissionResponse(boolean granted) {
        if (granted) {
            continueLocationSearch();
        } else {
            if (SystemClock.uptimeMillis() - mLocationRequestTimestamp < 250) {
                mListener.onPermanentLocationPermissionFailure();
            } else {
                mListener.onTemporaryLocationPermissionFailure();
            }
        }
    }

    private void continueLocationSearch() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            performLocationSettingCheck();
        } else {
            mGoogleApiClient = mLocationActivity.connectGoogleApiClient(mLocationActivityListener);
            if (mGoogleApiClient.isConnected()) {
                performLocationSettingCheck();
            }
        }
    }

    private LocationActivityListener mLocationActivityListener = new LocationActivityListener() {
        @Override
        public void onConnectionSuspended(int reason) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            //The client was set to autoresolve. If this method gets called, abandon all hope.
            mListener.onPermanentGooglePlayServicesFailure();
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            //Google Play Services is connected, see if location is enabled in settings.
            performLocationSettingCheck();
        }

        @Override
        public void onSettingsCheckSuccess() {
            startActualLocationRequest();
        }

        @Override
        public void onSettingsCheckFailure() {
            mListener.onLocationSettingFailure();
        }
    };

    private void performLocationSettingCheck() {
        if (mLocationActivity != null) {
            mLocationActivity.checkLocationSettings(mLocationRequest);
        }
    }

    private void startActualLocationRequest() {
        if (mListener.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            try {
                setSearchInProgress(true);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationCallback);
            } catch (SecurityException e) {
                Log.e(TAG, "Security Exception", e);
            }
        }
    }

    private LocationListener mLocationCallback = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            searchByLocation(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    };

    private void searchByLocation(Location location) {
        GroundControl.uiAgent(this, new GetLegislatorsByCoordinatesAgent(mLegislatorRepository, location))
                .uiCallback(mSearchAgentListener)
                .execute();
    }

    private final FunctionalAgentListener<ResponseContainer<List<Legislator>>, Void> mSearchAgentListener = new FunctionalAgentListener<ResponseContainer<List<Legislator>>, Void>() {
        @Override
        public void onCompletion(String agentIdentifier, ResponseContainer<List<Legislator>> result) {
            setSearchInProgress(false);
            if (result.isSuccess()) {
                updateResultList(result);
            } else {
                if (mListener != null) mListener.showNetworkError(result.getResponseStatus());
            }
        }
    };

    private void updateResultList(ResponseContainer<List<Legislator>> result) {
        ListReplacer.selectiveReplace(result.getValue(), mLegislatorViewModelList, new ListReplacer.LrTransform<Legislator, LegislatorViewModel>() {
            @Override
            public boolean matches(Legislator source, LegislatorViewModel destination) {
                return source.getBioguideId().equals(destination.getBioguideId());
            }

            @Override
            public boolean shouldReplace(Legislator source, LegislatorViewModel destination) {
                return source.isFavorite() != destination.isFavorite();
            }

            @Override
            public LegislatorViewModel transform(Legislator source) {
                return LegislatorMapper.INSTANCE.legislatorToViewModel(source);
            }
        });
        mLegislatorSearchResultViewModel.setEmptyMessageVisible(mLegislatorViewModelList.size() <= 0);
    }

    public void setLocationActivity(LocationActivity locationActivity) {
        mLocationActivity = locationActivity;
    }

    public interface Listener {
        void bindLegislatorSearchViewModel(LegislatorSearchResultViewModel legislatorSearchResultViewModel);
        void bindLegislatorList(ObservableList<LegislatorViewModel> legislatorList);
        boolean hasPermission(String permission);
        boolean shouldShowPermissionRationale(String permission);
        void showPermissionRationale(String permission);
        void requestPermission(String permission);
        void onPermanentLocationPermissionFailure();
        void onTemporaryLocationPermissionFailure();
        void onLocationSettingFailure();
        void onPermanentGooglePlayServicesFailure();
        void showNetworkError(ResponseStatus responseStatus);
        void launchLegislatorDetail(View photo, LegislatorViewModel legislatorViewModel);
    }

    public enum SearchMode {
        ZIP_CODE,
        NAME,
        LOCATION
    }
}
