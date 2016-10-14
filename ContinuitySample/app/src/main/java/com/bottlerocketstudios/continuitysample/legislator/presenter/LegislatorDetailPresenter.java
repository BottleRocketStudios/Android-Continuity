package com.bottlerocketstudios.continuitysample.legislator.presenter;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.core.injection.Injectable;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;
import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.continuitysample.core.util.SocialLinkFormatter;
import com.bottlerocketstudios.continuitysample.legislator.domain.LegislatorRepository;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;
import com.bottlerocketstudios.continuitysample.legislator.usecase.SetFavoriteLegislatorByBioguideId;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;
import com.bottlerocketstudios.groundcontrol.listener.FunctionalAgentListener;

/**
 * Created on 10/6/16.
 */

public class LegislatorDetailPresenter implements ContinuousObject {

    private Listener mListener;
    private LegislatorViewModel mLegislatorViewModel;
    private LegislatorRepository mLegislatorRepository;

    public LegislatorDetailPresenter () {
        ServiceInjector.injectWithType(LegislatorRepository.class, new Injectable<LegislatorRepository>() {
            @Override
            public void receiveInjection(LegislatorRepository injection) {
                mLegislatorRepository = injection;
            }
        });
    }

    public void bindListener(LegislatorDetailPresenter.Listener listener) {
        mListener = listener;
    }

    @Override
    public void onContinuityAnchorDestroyed(Object anchor) {
        mListener = null;
    }

    @Override
    public void onContinuityDiscard() {
        GroundControl.onDestroy(this);
    }

    public void setViewModel(LegislatorViewModel viewModel) {
        mLegislatorViewModel = viewModel;
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

    public void twitterTapped() {
        mListener.launchUrl(SocialLinkFormatter.formatTwitterUrl(mLegislatorViewModel.getTwitterId()));
    }

    public void facebookTapped() {
        mListener.launchUrl(SocialLinkFormatter.formatFacebookUrl(mLegislatorViewModel.getFacebookId()));
    }

    public void youTubeTapped() {
        mListener.launchUrl(SocialLinkFormatter.formatYouTubeUrl(mLegislatorViewModel.getYoutubeId()));
    }

    public void websiteTapped() {
        mListener.launchUrl(mLegislatorViewModel.getWebsite());
    }

    public void govtrackTapped() {
        mListener.launchUrl(SocialLinkFormatter.formatGovtrackUrl(mLegislatorViewModel.getGovtrackId()));
    }

    public interface Listener {
        void launchUrl(String url);
    }
}
