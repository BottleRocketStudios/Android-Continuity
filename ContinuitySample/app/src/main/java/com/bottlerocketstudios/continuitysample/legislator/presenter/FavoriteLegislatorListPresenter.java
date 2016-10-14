package com.bottlerocketstudios.continuitysample.legislator.presenter;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.bottlerocketstudios.continuity.ContinuousObject;
import com.bottlerocketstudios.continuitysample.core.injection.Injectable;
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
 * Created on 9/13/16.
 */
public class FavoriteLegislatorListPresenter implements ContinuousObject {

    private LegislatorRepository mLegislatorRepository;
    private Listener mListener;
    private FavoriteLegislatorViewModel mFavoriteLegislatorViewModel = new FavoriteLegislatorViewModel();
    private ObservableList<LegislatorViewModel> mLegislatorViewModelList = new ObservableArrayList<>();
    private boolean mInitialized = false;

    public FavoriteLegislatorListPresenter() {
        ServiceInjector.injectWithType(LegislatorRepository.class, new Injectable<LegislatorRepository>() {
            @Override
            public void receiveInjection(LegislatorRepository injection) {
                mLegislatorRepository = injection;
            }
        });
    }

    public void bindListener(Listener listener) {
        mListener = listener;
        mListener.bindLegislatorList(mLegislatorViewModelList);
        mListener.bindFavoriteLegislatorViewModel(mFavoriteLegislatorViewModel);
    }

    public void onResume() {
        if (!mInitialized) {
            mInitialized = true;
            loadFavoriteLegislators();
        }
    }

    public void searchFabTapped() {
        if (mListener != null) {
            mListener.launchLegislatorSearch();
            mInitialized = false;
        }
    }

    public void loadFavoriteLegislators() {
        GroundControl.uiAgent(this, new GetFavoriteLegislatorsAgent(mLegislatorRepository))
                .uiCallback(new FunctionalAgentListener<ResponseContainer<List<Legislator>>, Void>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<List<Legislator>> result) {
                        if (result.isSuccess()) {
                            ListReplacer.selectiveReplace(result.getValue(), mLegislatorViewModelList, new ListReplacer.LrTransform<Legislator, LegislatorViewModel>() {
                                @Override
                                public boolean matches(Legislator source, LegislatorViewModel destination) {
                                    return source.getBioguideId().equals(destination.getBioguideId());
                                }

                                @Override
                                public boolean shouldReplace(Legislator source, LegislatorViewModel destination) {
                                    return source.isFavorite() != destination.isFavorite();
                                }

                                @Override
                                public LegislatorViewModel transform(Legislator source) {
                                    return LegislatorMapper.INSTANCE.legislatorToViewModel(source);
                                }
                            });
                        } else {
                            mListener.showErrorDialog(result.getResponseStatus());
                        }
                        updateEmptyState();
                    }
                }).execute();
    }

    public void favoriteTapped(final LegislatorViewModel legislatorViewModel) {
        GroundControl.uiAgent(this, new SetFavoriteLegislatorByBioguideId(mLegislatorRepository, legislatorViewModel.getBioguideId(), false))
                .uiCallback(new FunctionalAgentListener<ResponseContainer<Legislator>, Void>() {
                    @Override
                    public void onCompletion(String agentIdentifier, ResponseContainer<Legislator> result) {
                        if (!result.isSuccess()) {
                            mLegislatorViewModelList.add(legislatorViewModel);
                            updateEmptyState();
                        }
                    }
                })
                .execute();
        mLegislatorViewModelList.remove(legislatorViewModel);
        updateEmptyState();
    }

    public void legislatorTapped(View photo, LegislatorViewModel legislatorViewModel) {
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
        mListener = null;
    }

    @Override
    public void onContinuityDiscard() {
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
