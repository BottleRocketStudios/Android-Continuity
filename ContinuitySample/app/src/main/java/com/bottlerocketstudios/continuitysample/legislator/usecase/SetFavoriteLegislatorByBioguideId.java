package com.bottlerocketstudios.continuitysample.legislator.usecase;

import com.bottlerocketstudios.continuitysample.core.usecase.BaseAgent;
import com.bottlerocketstudios.continuitysample.legislator.domain.LegislatorRepository;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;

/**
 * Created on 9/14/16.
 */
public class SetFavoriteLegislatorByBioguideId extends BaseAgent<Legislator> {

    private final LegislatorRepository mLegislatorRepository;
    private final String mBioguideId;
    private final boolean mFavorite;

    public SetFavoriteLegislatorByBioguideId(LegislatorRepository legislatorRepository, String bioguideId, boolean favorite) {
        mLegislatorRepository = legislatorRepository;
        mBioguideId = bioguideId;
        mFavorite = favorite;
    }

    @Override
    public String createUniqueIdentifier() {
        return super.createUniqueIdentifier() + mBioguideId;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void run() {
        notifyCompletion(mLegislatorRepository.setLegislatorFavoriteById(mBioguideId, mFavorite));
    }
}
