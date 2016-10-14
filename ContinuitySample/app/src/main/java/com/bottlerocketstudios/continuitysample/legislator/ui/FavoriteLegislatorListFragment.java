package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.model.ResponseStatus;
import com.bottlerocketstudios.continuitysample.core.ui.BaseFragment;
import com.bottlerocketstudios.continuitysample.core.ui.ResponseStatusTranslator;
import com.bottlerocketstudios.continuitysample.core.ui.dialog.JustDismissOnClickListener;
import com.bottlerocketstudios.continuitysample.core.ui.dialog.SampleDialogFragment;
import com.bottlerocketstudios.continuitysample.databinding.FavoriteLegislatorFragmentBinding;
import com.bottlerocketstudios.continuitysample.legislator.presenter.FavoriteLegislatorListPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.FavoriteLegislatorViewModel;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * Created on 9/13/16.
 */
public class FavoriteLegislatorListFragment extends BaseFragment {

    private static final int DIALOG_ID_ERROR = 200;
    private static final String DIALOG_TAG_ERROR = "errorDialog";

    private FavoriteLegislatorFragmentBinding mFavoriteLegislatorFragmentBinding;
    private FavoriteLegislatorListPresenter mFavoriteLegislatorPresenter;
    private FavoriteLegislatorRecyclerAdapter mFavoriteLegislatorRecyclerAdapter;

    public static FavoriteLegislatorListFragment newInstance() {
        return new FavoriteLegislatorListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFavoriteLegislatorFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.favorite_legislator_fragment, container, false);

        //Create or retrieve the presenter
        mFavoriteLegislatorPresenter = getPresenterRepository().with(this, FavoriteLegislatorListPresenter.class).build();
        mFavoriteLegislatorFragmentBinding.setPresenter(mFavoriteLegislatorPresenter);

        mFavoriteLegislatorFragmentBinding.flfRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFavoriteLegislatorRecyclerAdapter = new FavoriteLegislatorRecyclerAdapter();
        mFavoriteLegislatorRecyclerAdapter.setFavoriteLegislatorListPresenter(mFavoriteLegislatorPresenter);
        mFavoriteLegislatorFragmentBinding.flfRecyclerView.setAdapter(mFavoriteLegislatorRecyclerAdapter);

        mFavoriteLegislatorPresenter.bindListener(mListener);

        return mFavoriteLegislatorFragmentBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFavoriteLegislatorPresenter.onResume();
    }

    FavoriteLegislatorListPresenter.Listener mListener = new FavoriteLegislatorListPresenter.Listener() {
        @Override
        public void bindFavoriteLegislatorViewModel(FavoriteLegislatorViewModel favoriteLegislatorViewModel) {
            mFavoriteLegislatorFragmentBinding.setFavoriteLegislatorViewModel(favoriteLegislatorViewModel);
        }

        @Override
        public void bindLegislatorList(ObservableList<LegislatorViewModel> legislatorList) {
            mFavoriteLegislatorRecyclerAdapter.swapList(legislatorList);
        }

        @Override
        public void launchLegislatorSearch() {
            startActivity(LegislatorSearchActivity.newIntent(getActivity()));
        }

        @Override
        public void launchLegislatorDetail(View photo, LegislatorViewModel legislatorViewModel) {
            Intent intent = LegislatorDetailActivity.newIntent(getActivity(), legislatorViewModel);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), photo, getString(R.string.transition_legislator_detail));
                Bundle optionsBundle = options.toBundle();
                startActivity(intent, optionsBundle);
            } else {
                startActivity(intent);
            }
        }

        @Override
        public void showErrorDialog(ResponseStatus responseStatus) {
            new SampleDialogFragment.Builder()
                    .setDialogId(DIALOG_ID_ERROR)
                    .setTitle(getString(R.string.dialog_error_title))
                    .setMessage(ResponseStatusTranslator.getErrorString(getContext(), responseStatus))
                    .setPositiveText(getString(R.string.ok))
                    .setOnClickListener(new JustDismissOnClickListener())
                    .build()
                    .show(getChildFragmentManager(), DIALOG_TAG_ERROR);
        }
    };
}
