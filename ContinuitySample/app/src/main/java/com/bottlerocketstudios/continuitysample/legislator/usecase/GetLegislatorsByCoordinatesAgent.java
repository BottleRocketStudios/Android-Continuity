package com.bottlerocketstudios.continuitysample.legislator.usecase;

import android.location.Location;

import com.bottlerocketstudios.continuitysample.core.usecase.BaseAgent;
import com.bottlerocketstudios.continuitysample.legislator.domain.LegislatorRepository;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;

import java.util.List;

/**
 * Created on 9/14/16.
 */
public class GetLegislatorsByCoordinatesAgent extends BaseAgent<List<Legislator>> {

    private final LegislatorRepository mLegislatorRepository;
    private final Location mLocation;

    public GetLegislatorsByCoordinatesAgent(LegislatorRepository legislatorRepository, Location location) {
        mLegislatorRepository = legislatorRepository;
        mLocation = location;
    }

    @Override
    public String createUniqueIdentifier() {
        return super.createUniqueIdentifier() + mLocation.toString();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void run() {
        notifyCompletion(mLegislatorRepository.fetchLegislatorListByCoordinates(mLocation));
    }
}
