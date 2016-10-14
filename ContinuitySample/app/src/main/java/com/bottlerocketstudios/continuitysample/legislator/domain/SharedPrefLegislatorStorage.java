package com.bottlerocketstudios.continuitysample.legislator.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created on 9/14/16.
 */
public class SharedPrefLegislatorStorage implements LegislatorStorage {

    private static final String PREF_FAVORITE_LEGISLATORS = "favoriteLegislators";
    private static final String PREF_UPDATE_TIME = "updateTime";

    private final Context mContext;

    public SharedPrefLegislatorStorage(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void saveFavoriteLegislatorIdSet(Set<String> favoriteLegislators) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putStringSet(PREF_FAVORITE_LEGISLATORS, favoriteLegislators);
        //Write the update time so that the Set actually gets saved. Issue 27801.
        editor.putLong(PREF_UPDATE_TIME, System.currentTimeMillis());
        editor.apply();
    }

    @Override
    public Set<String> loadFavoriteLegislatorIdSet() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getStringSet(PREF_FAVORITE_LEGISLATORS, null);
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
