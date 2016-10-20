package com.bottlerocketstudios.continuitysample.legislator.domain;

import java.util.Set;

/**
 * Storage interface for LegislatorRepository
 */
public interface LegislatorStorage {
    void saveFavoriteLegislatorIdSet(Set<String> favoriteLegislators);
    Set<String> loadFavoriteLegislatorIdSet();
}
