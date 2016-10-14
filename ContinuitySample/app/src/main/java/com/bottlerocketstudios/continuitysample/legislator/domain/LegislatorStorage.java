package com.bottlerocketstudios.continuitysample.legislator.domain;

import java.util.Set;

/**
 * Created on 9/14/16.
 */
public interface LegislatorStorage {
    void saveFavoriteLegislatorIdSet(Set<String> favoriteLegislators);
    Set<String> loadFavoriteLegislatorIdSet();
}
