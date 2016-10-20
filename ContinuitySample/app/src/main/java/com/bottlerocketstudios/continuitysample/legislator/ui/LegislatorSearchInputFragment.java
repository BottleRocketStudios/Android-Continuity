package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.BaseFragment;
import com.bottlerocketstudios.continuitysample.databinding.LegislatorSearchInputFragmentBinding;
import com.bottlerocketstudios.continuitysample.legislator.presenter.LegislatorSearchInputPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorSearchInputViewModel;

public class LegislatorSearchInputFragment extends BaseFragment {

    private Listener mFragmentListener;

    LegislatorSearchInputPresenter mPresenter;
    LegislatorSearchInputFragmentBinding mBinding;

    public static LegislatorSearchInputFragment newInstance() {
        return new LegislatorSearchInputFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentListener = activityCastOrThrow(context, Listener.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.legislator_search_input_fragment, container, false);

        mPresenter = getPresenterRepository().with(this, LegislatorSearchInputPresenter.class).build();
        mPresenter.bindListener(mPresenterListener);
        mBinding.setPresenter(mPresenter);

        return mBinding.getRoot();
    }

    private LegislatorSearchInputPresenter.Listener mPresenterListener = new LegislatorSearchInputPresenter.Listener() {
        @Override
        public void launchSearchForZip(String zip) {
            mFragmentListener.launchSearchByZip(zip);
        }

        @Override
        public void launchSearchForLocation() {
            mFragmentListener.launchSearchByLocation();
        }

        @Override
        public void launchSearchForName(String name) {
            mFragmentListener.launchSearchByName(name);
        }

        @Override
        public void bindViewModel(LegislatorSearchInputViewModel viewModel) {
            mBinding.setSearchInput(viewModel);
        }
    };

    /**
     * Plumbing back to host activity in case we want dual pane in the future.
     */
    public interface Listener {
        void launchSearchByName(String name);
        void launchSearchByZip(String zip);
        void launchSearchByLocation();
    }
}
