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
 * Presenter for a single legislator detail.
 */
public class LegislatorDetailPresenter implements ContinuousObject {

    private Listener mListener;
    private LegislatorViewModel mLegislatorViewModel;
    private LegislatorRepository mLegislatorRepository;

    public LegislatorDetailPresenter () {
        //Grab the Legislator Repository here so that it can be passed to use cases.
        ServiceInjector.injectWithType(LegislatorRepository.class, new Injectable<LegislatorRepository>() {
            @Override
            public void receiveInjection(LegislatorRepository injection) {
                mLegislatorRepository = injection;
            }
        });
    }

    /**
     * Call this method to set the listener for this Presenter
     */
    public void bindListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onContinuityAnchorDestroyed(Object anchor) {
        //The UI has gone away, remove the reference to it.
        mListener = null;
    }

    @Override
    public void onContinuityDiscard() {
        //Notify GroundControl that we are no longer interested in any ongoing asynchronous work started here.
        GroundControl.onDestroy(this);
    }

    /**
     * Provide a LegislatorViewModel passed in as an argument to the Fragment/Activity.
     */
    public void setViewModel(LegislatorViewModel viewModel) {
        mLegislatorViewModel = viewModel;
    }

    public void favoriteTapped(final LegislatorViewModel legislatorViewModel) {
        //Perform the command based on the state the user clicked i.e. the state of the ViewModel.
        final boolean newFavoriteState = !legislatorViewModel.isFavorite();

        //Start background operation to change favorite to new state.
        GroundControl.uiAgent(this, new SetFavoriteLegislatorByBioguideId(mLegislatorRepository, legislatorViewModel.getBioguideId(), newFavoriteState))
                .uiCallback(new FunctionalAgentListener<ResponseContainer<Legislator>, Float>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<Legislator> result) {
                        if (result.isSuccess()) {
                            //The operation worked, update the view model. In this case we know what the changed field was.
                            //In some cases you could just call LegislatorMapper.INSTANCE.updateLegislatorViewModel() and update everything.
                            legislatorViewModel.setFavorite(result.getValue().isFavorite());
                        } else {
                            //Update failed, swap UI back to previous state.
                            legislatorViewModel.setFavorite(!newFavoriteState);
                        }
                    }
                })
                .execute();

        //Assume the update works and update the UI. Async callback will revert the change if it fails.
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
