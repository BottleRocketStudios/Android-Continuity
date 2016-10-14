package com.bottlerocketstudios.continuitysample.legislator.serialization;

import com.bottlerocketstudios.continuitysample.core.api.GovtrackRequest;

/**
 * Created on 10/3/16.
 */

public class GovtrackLegislatorImageUrlFormatter implements LegislatorImageUrlFormatter{

    @Override
    public String getImageUrl(String govtrackId) {
        return GovtrackRequest.getImageUrl(govtrackId);
    }
}
