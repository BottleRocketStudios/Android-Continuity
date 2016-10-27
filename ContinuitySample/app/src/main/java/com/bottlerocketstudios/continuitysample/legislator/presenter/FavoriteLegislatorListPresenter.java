package com.bottlerocketstudios.continuitysample.legislator.presenter;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.core.injection.ServiceInjector;
import com.bottlerocketstudios.continuitysample.core.model.ResponseContainer;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;
import com.bottlerocketstudios.continuitysample.core.util.ListReplacer;
import com.bottlerocketstudios.continuitysample.legislator.domain.LegislatorRepository;
import com.bottlerocketstudios.continuitysample.legislator.mapping.LegislatorMapper;
import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;
import com.bottlerocketstudios.continuitysample.legislator.usecase.GetFavoriteLegislatorsAgent;
import com.bottlerocketstudios.continuitysample.legislator.usecase.SetFavoriteLegislatorByBioguideId;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.FavoriteLegislatorViewModel;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;
import com.bottlerocketstudios.groundcontrol.convenience.GroundControl;
import com.bottlerocketstudios.groundcontrol.listener.FunctionalAgentListener;

import java.util.List;

/**
 * Presenter for a collection of Favorite Legislators.
 */
public class FavoriteLegislatorListPresenter implements ContinuousObject {

    //This observable ViewModel represents the meta state of the fragment.
    private final FavoriteLegislatorViewModel mFavoriteLegislatorViewModel = new FavoriteLegislatorViewModel();

    //This observable list will be bound to a RecyclerView.Adapter so it should not be replaced, only updated.
    private final ObservableList<LegislatorViewModel> mLegislatorViewModelList = new ObservableArrayList<>();

    private final LegislatorRepository mLegislatorRepository;
    private Listener mListener;
    private boolean mInitialized = false;

    public FavoriteLegislatorListPresenter() {
        mLegislatorRepository = ServiceInjector.resolve(LegislatorRepository.class);
    }

    /**
     * Call this method to set the listener for this Presenter and have the Presenter bind ViewModels via the Listener.
     */
    public void bindListener(Listener listener) {
        mListener = listener;
        mListener.bindLegislatorList(mLegislatorViewModelList);
        mListener.bindFavoriteLegislatorViewModel(mFavoriteLegislatorViewModel);
    }

    /**
     * Notify the presenter when the activity has been resumed to initialize the data.
     */
    public void onResume() {
        if (!mInitialized) {
            mInitialized = true;
            loadFavoriteLegislators();
        }
    }

    /**
     * User has tapped the search fab, navigate to search.
     */
    public void searchFabTapped() {
        if (mListener != null) {
            mListener.launchLegislatorSearch();
            //Clear the initialized flag because we need to refresh our data when the user returns.
            mInitialized = false;
        }
    }

    private void loadFavoriteLegislators() {
        //Use GroundControl to launch a background agent against the repository.
        GroundControl.uiAgent(this, new GetFavoriteLegislatorsAgent(mLegislatorRepository))
                .uiCallback(new FunctionalAgentListener<ResponseContainer<List<Legislator>>, Float>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<List<Legislator>> result) {
                        if (result.isSuccess()) {
                            //We have a new list of legislators, do a lightweight replace-as-needed on the ObservableList.
                            ListReplacer.selectiveReplace(result.getValue(), mLegislatorViewModelList, new ListReplacer.LrTransform<Legislator, LegislatorViewModel>() {
                                @Override
                                public boolean matches(Legislator source, LegislatorViewModel destination) {
                                    //Matching view model with model based on a unique id.
                                    return source.getBioguideId().equals(destination.getBioguideId());
                                }

                                @Override
                                public boolean shouldReplace(Legislator source, LegislatorViewModel destination) {
                                    //We only care about replacing a legislator if their favorite status has changed.
                                    return source.isFavorite() != destination.isFavorite();
                                }

                                @Override
                                public LegislatorViewModel transform(Legislator source) {
                                    //Create a new instance of the LegislatorViewModel to be added to the destination.
                                    return LegislatorMapper.INSTANCE.legislatorToViewModel(source);
                                }
                            });
                        } else if (mListener != null) {
                            //Don't show the error dialog from here. Tell the listener to do it.
                            mListener.showErrorDialog(result.getResponseStatus());
                        }
                        //Whatever the result, update empty state.
                        updateEmptyState();
                    }
                }).execute();
    }

    public void favoriteTapped(final LegislatorViewModel legislatorViewModel) {
        //Launch a background agent against the repository to remove the favorite legislator.
        GroundControl.uiAgent(this, new SetFavoriteLegislatorByBioguideId(mLegislatorRepository, legislatorViewModel.getBioguideId(), false))
                .uiCallback(new FunctionalAgentListener<ResponseContainer<Legislator>, Float>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<Legislator> result) {
                        if (!result.isSuccess()) {
                            //In the case of failure (favorite was not removed), re-add the legislator.
                            mLegislatorViewModelList.add(legislatorViewModel);
                            updateEmptyState();
                        }
                    }
                })
                .execute();
        //Preemptively update the UI in anticipation of success. Failure will correct UI state to match domain in the callback above.
        mLegislatorViewModelList.remove(legislatorViewModel);
        updateEmptyState();
    }

    public void legislatorTapped(View photo, LegislatorViewModel legislatorViewModel) {
        //Show legislator detail.
        if (mListener != null) {
            mListener.launchLegislatorDetail(photo, legislatorViewModel);
            mInitialized = false;
        }
    }

    private void updateEmptyState() {
        mFavoriteLegislatorViewModel.setEmptyMessageVisible(mLegislatorViewModelList.size() <= 0);
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

    public interface Listener {
        void bindFavoriteLegislatorViewModel(FavoriteLegislatorViewModel favoriteLegislatorViewModel);
        void bindLegislatorList(ObservableList<LegislatorViewModel> legislatorList);
        void launchLegislatorSearch();
        void launchLegislatorDetail(View photo, LegislatorViewModel legislatorViewModel);
        void showErrorDialog(ResponseStatus responseStatus);
    }
}
