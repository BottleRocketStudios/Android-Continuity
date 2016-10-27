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
import android.view.View;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;
import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;
import com.bottlerocketstudios.continuitysample.core.ui.LocationActivity;
import com.bottlerocketstudios.continuitysample.core.ui.LocationActivityListener;
import com.bottlerocketstudios.continuitysample.core.util.ListReplacer;
import com.bottlerocketstudios.continuitysample.core.util.LogIt;
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
 * Presenter to handle legislator search input.
 */
public class LegislatorSearchResultPresenter implements ContinuousObject {
    private static final String TAG = LegislatorSearchResultPresenter.class.getSimpleName();

    //meta-ViewModels are updated, never replaced for the life of the Presenter.
    private final LegislatorSearchResultViewModel mLegislatorSearchResultViewModel = new LegislatorSearchResultViewModel();
    private final ObservableList<LegislatorViewModel> mLegislatorViewModelList = new ObservableArrayList<>();
    private final LegislatorRepository mLegislatorRepository;

    private Listener mListener;
    private long mLocationRequestTimestamp;
    private LocationActivity mLocationActivity;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private SearchMode mSearchMode;
    private String mQuery;
    private boolean mSearchInitialized;

    public LegislatorSearchResultPresenter() {
        mLegislatorRepository = ServiceInjector.resolve(LegislatorRepository.class);
    }

    /**
     * Call this method to set the listener for this Presenter and have the Presenter bind ViewModels via the Listener.
     */
    public void bindListener(Listener listener) {
        mListener = listener;
        mListener.bindLegislatorSearchViewModel(mLegislatorSearchResultViewModel);
        mListener.bindLegislatorList(mLegislatorViewModelList);
    }

    @Override
    public void onContinuityAnchorDestroyed(Object anchor) {
        //Here we need to discard two different references to the UI.
        mListener = null;
        mLocationActivity = null;
    }

    @Override
    public void onContinuityDiscard() {
        //Notify GroundControl that we are no longer interested in any ongoing asynchronous work started here.
        GroundControl.onDestroy(this);
    }

    public void favoriteTapped(final LegislatorViewModel legislatorViewModel) {
        final boolean newFavoriteState = !legislatorViewModel.isFavorite();

        //Start async operation to update favorite state.
        GroundControl.uiAgent(this, new SetFavoriteLegislatorByBioguideId(mLegislatorRepository, legislatorViewModel.getBioguideId(), newFavoriteState))
                .uiCallback(new FunctionalAgentListener<ResponseContainer<Legislator>, Float>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<Legislator> result) {
                        if (result.isSuccess()) {
                            //Update UI to match domain model.
                            legislatorViewModel.setFavorite(result.getValue().isFavorite());
                        } else {
                            //Roll back the change on failure.
                            legislatorViewModel.setFavorite(!newFavoriteState);
                        }
                    }
                })
                .execute();

        //Preemptively update UI assuming happy path, change is rolled back on failure.
        legislatorViewModel.setFavorite(newFavoriteState);
    }

    public void legislatorTapped(View photo, LegislatorViewModel legislatorViewModel) {
        //Have listener launch detail view.
        if (mListener != null) {
            mListener.launchLegislatorDetail(photo, legislatorViewModel);
            //Assume the domain will be modified (favorite toggled) so we need to refresh on resume.
            mSearchInitialized = false;
        }
    }

    /**
     * Begin searching if we have not already.
     */
    public void onResume(SearchMode searchMode, String query) {
        mSearchMode = searchMode;
        mQuery = query;
        if (!mSearchInitialized) {
            mSearchInitialized = true;
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
        //Show or hide progress spinner (or however UI handles it, we don't care) via ViewModel update.
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
        //Because location has become a very complicated flow, first we check for permissions
        if (!mListener.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (mListener.shouldShowPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //We have been denied before, beg for permission.
                mListener.showPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);
            } else {
                //First time, the user tapped on search by location and knows what that entails... we hope.
                requestLocationPermission();
            }
        } else {
            //We have permission, on to phase 2.
            continueLocationSearch();
        }
    }

    /**
     * The user has agreed with our permission rationale dialog.
     */
    public void permissionRationaleAgreed() {
        requestLocationPermission();
    }

    /**
     * Have the listener request permission from OS.
     */
    private void requestLocationPermission() {
        //Keep the timestamp. The best indication of "never ask again" is an immediate response.
        mLocationRequestTimestamp = SystemClock.uptimeMillis();
        mListener.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * Permission request has completed.
     */
    public void handlePermissionResult(String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[i])) {
                handleLocationPermissionResponse(grantResults[i] == PermissionChecker.PERMISSION_GRANTED);
            }
        }
    }

    private void handleLocationPermissionResponse(boolean granted) {
        if (granted) {
            //Now we can move to phase 2.
            continueLocationSearch();
        } else {
            //They shut us down, notify the listener to show an error.
            if (SystemClock.uptimeMillis() - mLocationRequestTimestamp < 250) {
                //That was inhumanly fast, they've checked "never ask again" before.
                mListener.onPermanentLocationPermissionFailure();
            } else {
                //They manually denied us.
                mListener.onTemporaryLocationPermissionFailure();
            }
        }
    }

    private void continueLocationSearch() {
        //Phase 2 begins. Create a location request so that we can check to see if location is enabled.
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            //We have a google api client already, do the check.
            performLocationSettingCheck();
        } else {
            //Get the LocationActivty implementation to connect the GoogleApiClient
            mGoogleApiClient = mLocationActivity.connectGoogleApiClient(mLocationActivityListener);
            if (mGoogleApiClient.isConnected()) {
                //Already connected, continue phase 2 immediately as we will not get a callback.
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
            //Google Play Services is connected, continue phase 2 and see if location is enabled in settings.
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
            //Actually perform location setting check as we have a connected api client.
            mLocationActivity.checkLocationSettings(mLocationRequest);
        }
    }

    private void startActualLocationRequest() {
        //The stars have aligned. We have permission and location is enabled. Check permission again to make Lint happy.
        if (mListener.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            try {
                setSearchInProgress(true);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationCallback);
            } catch (SecurityException e) {
                LogIt.e(TAG, "Security Exception", e);
            }
        }
    }

    private LocationListener mLocationCallback = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //We got a location from Google Play Services.
            searchByLocation(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    };

    private void searchByLocation(Location location) {
        //Start async location search.
        GroundControl.uiAgent(this, new GetLegislatorsByCoordinatesAgent(mLegislatorRepository, location))
                .uiCallback(mSearchAgentListener)
                .execute();
    }

    /**
     * General listener for all search results as they all have the same format.
     */
    private final FunctionalAgentListener<ResponseContainer<List<Legislator>>, Float> mSearchAgentListener = new FunctionalAgentListener<ResponseContainer<List<Legislator>>, Float>() {
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
        //We have a new list of legislators, do a lightweight replace-as-needed on the ObservableList.
        ListReplacer.selectiveReplace(result.getValue(), mLegislatorViewModelList, new ListReplacer.LrTransform<Legislator, LegislatorViewModel>() {
            @Override
            public boolean matches(Legislator source, LegislatorViewModel destination) {
                //Match on bioguide id
                return source.getBioguideId().equals(destination.getBioguideId());
            }

            @Override
            public boolean shouldReplace(Legislator source, LegislatorViewModel destination) {
                //Only replace if favorite status has changed.
                return source.isFavorite() != destination.isFavorite();
            }

            @Override
            public LegislatorViewModel transform(Legislator source) {
                //Create new or replacement ViewModels for destination.
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
