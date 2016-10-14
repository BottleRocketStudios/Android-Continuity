package com.bottlerocketstudios.continuitysample.legislator.domain;

import android.location.Location;
import android.util.LruCache;

import com.bottlerocketstudios.continuitysample.core.api.NetworkExecutor;
import com.bottlerocketstudios.continuitysample.core.api.NetworkResponse;
import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;
import com.bottlerocketstudios.continuitysample.core.serialization.JsonObjectResponseHandler;
import com.bottlerocketstudios.continuitysample.legislator.api.GetLegislatorsByBioguideId;
import com.bottlerocketstudios.continuitysample.legislator.api.GetLegislatorsByCoordinates;
import com.bottlerocketstudios.continuitysample.legislator.api.GetLegislatorsByName;
import com.bottlerocketstudios.continuitysample.legislator.api.GetLegislatorsByZip;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;
import com.bottlerocketstudios.continuitysample.legislator.model.LegislatorResponse;
import com.bottlerocketstudios.continuitysample.legislator.serialization.GovtrackLegislatorImageUrlFormatter;
import com.bottlerocketstudios.continuitysample.legislator.serialization.LegislatorResponseSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * Created on 9/13/16.
 */
public class LegislatorRepository {

    private final OkHttpClient mOkHttpClient;
    private final LegislatorStorage mLegislatorStorage;
    private final List<Legislator> mFavoriteLegislatorList = new ArrayList<>();
    private final LruCache<String, Legislator> mLegislatorCache;

    public LegislatorRepository(LegislatorStorage legislatorStorage, LruCache<String, Legislator> legislatorCache, OkHttpClient okHttpClient) {
        mLegislatorStorage = legislatorStorage;
        mOkHttpClient = okHttpClient;
        mLegislatorCache = legislatorCache;
    }

    public LegislatorRepository(SharedPrefLegislatorStorage legislatorStorage, OkHttpClient okHttpClient) {
        this(legislatorStorage, new LruCache<String, Legislator>(100), okHttpClient);
    }

    public ResponseContainer<List<Legislator>> getFavoriteLegislators() {
        Set<String> favoriteLegislatorIds = getFavoriteLegislatorIds();

        if (favoriteLegislatorIds != null && favoriteLegislatorIds.size() != mFavoriteLegislatorList.size()) {
            return fetchFavoriteLegislators(favoriteLegislatorIds, false);
        }

        return new ResponseContainer<>(mFavoriteLegislatorList, ResponseStatus.SUCCESS);
    }

    public ResponseContainer<List<Legislator>> refreshFavoriteLegislators() {
        Set<String> favoriteLegislatorIds = getFavoriteLegislatorIds();

        if (favoriteLegislatorIds != null) {
            return fetchFavoriteLegislators(favoriteLegislatorIds, true);
        }

        return new ResponseContainer<>(mFavoriteLegislatorList, ResponseStatus.SUCCESS);
    }

    private ResponseContainer<List<Legislator>> fetchFavoriteLegislators(Set<String> favoriteLegislatorIds, boolean forceUpdate) {
        List<Legislator> legislatorList = new ArrayList<>();
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        if (favoriteLegislatorIds != null) {
            for (String legislatorId: favoriteLegislatorIds) {
                ResponseContainer<Legislator> responseContainer;
                if (forceUpdate) {
                    responseContainer = fetchLegislatorById(legislatorId);
                } else {
                    responseContainer = getLegislatorById(legislatorId);
                }
                if (responseContainer.isSuccess()) {
                    legislatorList.add(responseContainer.getValue());
                } else {
                    responseStatus = responseContainer.getResponseStatus();
                }
            }
            mFavoriteLegislatorList.clear();
            mFavoriteLegislatorList.addAll(legislatorList);
        }
        return new ResponseContainer<>(mFavoriteLegislatorList, responseStatus);
    }

    public ResponseContainer<Legislator> getLegislatorById(String bioguideId) {
        //Check for a cache hit first.
        Legislator legislator = getLegislatorFromCache(bioguideId);
        if (legislator != null) {
            return new ResponseContainer<>(legislator, ResponseStatus.SUCCESS);
        }

        //Fetch from web service.
        return fetchLegislatorById(bioguideId);
    }

    private Legislator getLegislatorFromCache(String bioguideId) {
        return mLegislatorCache.get(bioguideId);
    }

    private void putLegislatorInCache(Legislator legislator) {
        mLegislatorCache.put(legislator.getBioguideId(), legislator);
    }

    private void putLegislatorListInCache(List<Legislator> legislatorList) {
        for (Legislator legislator: legislatorList) {
            putLegislatorInCache(legislator);
        }
    }

    ResponseContainer<Legislator> fetchLegislatorById(String bioguideId) {
        NetworkResponse<LegislatorResponse> networkResponse = NetworkExecutor.execute(mOkHttpClient, new GetLegislatorsByBioguideId(bioguideId), new JsonObjectResponseHandler<LegislatorResponse>() {
            @Override
            public LegislatorResponse processJson(JSONObject jsonObject) throws JSONException {
                return LegislatorResponseSerializer.parseJsonObject(getJsonObject(), new GovtrackLegislatorImageUrlFormatter());
            }
        });

        if (networkResponse.isSuccess()) {
            LegislatorResponse legislatorResponse = networkResponse.getValue();
            if (legislatorResponse.getCount() == 1) {
                Legislator legislator = legislatorResponse.getResults().get(0);
                legislator.setFavorite(isFavoriteLegislator(legislator));
                putLegislatorInCache(legislator);
                return new ResponseContainer<>(legislator, ResponseStatus.SUCCESS);
            }
            return new ResponseContainer<>(null, ResponseStatus.INVALID_DATA);
        } else {
            return new ResponseContainer<>(null, ResponseStatus.translateNetworkStatus(networkResponse.getNetworkResponseStatus()));
        }
    }

    private boolean isFavoriteLegislator(Legislator legislator) {
        Set<String> favoriteLegislatorIdSet = getFavoriteLegislatorIds();
        if (favoriteLegislatorIdSet != null && getFavoriteLegislatorIds().contains(legislator.getBioguideId())) {
            return true;
        }
        return false;
    }

    public ResponseContainer<List<Legislator>> fetchLegislatorListByZip(String zip) {
        NetworkResponse<LegislatorResponse> networkResponse = NetworkExecutor.execute(mOkHttpClient, new GetLegislatorsByZip(zip), new JsonObjectResponseHandler<LegislatorResponse>() {
            @Override
            public LegislatorResponse processJson(JSONObject jsonObject) throws JSONException {
                return LegislatorResponseSerializer.parseJsonObject(getJsonObject(), new GovtrackLegislatorImageUrlFormatter());
            }
        });

        if (networkResponse.isSuccess()) {
            return new ResponseContainer<>(processServerLegislatorList(networkResponse), ResponseStatus.SUCCESS);
        } else {
            return new ResponseContainer<>(null, ResponseStatus.translateNetworkStatus(networkResponse.getNetworkResponseStatus()));
        }
    }

    public ResponseContainer<List<Legislator>> fetchLegislatorListByCoordinates(Location location) {
        NetworkResponse<LegislatorResponse> networkResponse = NetworkExecutor.execute(mOkHttpClient, new GetLegislatorsByCoordinates(location), new JsonObjectResponseHandler<LegislatorResponse>() {
            @Override
            public LegislatorResponse processJson(JSONObject jsonObject) throws JSONException {
                return LegislatorResponseSerializer.parseJsonObject(getJsonObject(), new GovtrackLegislatorImageUrlFormatter());
            }
        });

        if (networkResponse.isSuccess()) {
            return new ResponseContainer<>(processServerLegislatorList(networkResponse), ResponseStatus.SUCCESS);
        } else {
            return new ResponseContainer<>(null, ResponseStatus.translateNetworkStatus(networkResponse.getNetworkResponseStatus()));
        }
    }

    public ResponseContainer<List<Legislator>> fetchLegislatorListByName(String name) {
        NetworkResponse<LegislatorResponse> networkResponse = NetworkExecutor.execute(mOkHttpClient, new GetLegislatorsByName(name), new JsonObjectResponseHandler<LegislatorResponse>() {
            @Override
            public LegislatorResponse processJson(JSONObject jsonObject) throws JSONException {
                return LegislatorResponseSerializer.parseJsonObject(getJsonObject(), new GovtrackLegislatorImageUrlFormatter());
            }
        });

        if (networkResponse.isSuccess()) {
            return new ResponseContainer<>(processServerLegislatorList(networkResponse), ResponseStatus.SUCCESS);
        } else {
            return new ResponseContainer<>(null, ResponseStatus.translateNetworkStatus(networkResponse.getNetworkResponseStatus()));
        }
    }

    private List<Legislator> processServerLegislatorList(NetworkResponse<LegislatorResponse> networkResponse) {
        List<Legislator> legislatorList = flagFavoriteLegislators(networkResponse.getValue().getResults());
        putLegislatorListInCache(legislatorList);
        return legislatorList;
    }

    private List<Legislator> flagFavoriteLegislators(List<Legislator> legislatorList) {
        Set<String> favoriteLegislatorIdSet = getFavoriteLegislatorIds();
        if (favoriteLegislatorIdSet == null || favoriteLegislatorIdSet.size() == 0) {
            return legislatorList;
        }

        for (Legislator legislator: legislatorList) {
            if (favoriteLegislatorIdSet.contains(legislator.getBioguideId())) {
                legislator.setFavorite(true);
            }
        }
        return legislatorList;
    }

    void addFavoriteLegislator(Legislator legislator) {
        Set<String> favoriteLegislatorIds = getFavoriteLegislatorIds();
        if (favoriteLegislatorIds == null) favoriteLegislatorIds = new HashSet<>();
        favoriteLegislatorIds.add(legislator.getBioguideId());
        saveFavoriteLegislatorIds(favoriteLegislatorIds);
        legislator.setFavorite(true);

        mFavoriteLegislatorList.add(legislator);
    }

    void removeFavoriteLegislator(Legislator legislator) {
        Set<String> favoriteLegislatorIds = getFavoriteLegislatorIds();
        if (favoriteLegislatorIds != null) {
            favoriteLegislatorIds.remove(legislator.getBioguideId());
            saveFavoriteLegislatorIds(favoriteLegislatorIds);
        }
        legislator.setFavorite(false);

        mFavoriteLegislatorList.remove(legislator);
    }

    private Set<String> getFavoriteLegislatorIds() {
        return mLegislatorStorage.loadFavoriteLegislatorIdSet();
    }

    private void saveFavoriteLegislatorIds(Set<String> favoriteLegislators) {
        mLegislatorStorage.saveFavoriteLegislatorIdSet(favoriteLegislators);
    }

    public ResponseContainer<Legislator> setLegislatorFavoriteById(String bioguideId, boolean favorite) {
        ResponseContainer<Legislator> legislatorResponseContainer = getLegislatorById(bioguideId);
        if (legislatorResponseContainer.isSuccess()) {
            Legislator legislator = legislatorResponseContainer.getValue();
            if (favorite) {
                addFavoriteLegislator(legislator);
            } else {
                removeFavoriteLegislator(legislator);
            }
        }
        return legislatorResponseContainer;
    }
}
