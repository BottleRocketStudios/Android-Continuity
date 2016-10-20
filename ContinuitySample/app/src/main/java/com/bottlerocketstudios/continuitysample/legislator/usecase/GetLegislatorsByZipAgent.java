package com.bottlerocketstudios.continuitysample.legislator.usecase;

import com.bottlerocketstudios.continuitysample.core.usecase.BaseAgent;
import com.bottlerocketstudios.continuitysample.legislator.domain.LegislatorRepository;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;

import java.util.List;

/**
 * Asynchronously execute a fetch of legislators for a zip code.
 */
public class GetLegislatorsByZipAgent extends BaseAgent<List<Legislator>> {

    private final LegislatorRepository mLegislatorRepository;
    private final String mZip;

    public GetLegislatorsByZipAgent(LegislatorRepository legislatorRepository, String zip) {
        mLegislatorRepository = legislatorRepository;
        mZip = zip;
    }

    @Override
    public String createUniqueIdentifier() {
        return super.createUniqueIdentifier() + mZip;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void run() {
        notifyCompletion(mLegislatorRepository.fetchLegislatorListByZip(mZip));
    }
}
