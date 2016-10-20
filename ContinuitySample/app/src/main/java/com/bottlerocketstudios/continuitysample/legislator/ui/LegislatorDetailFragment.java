package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.BaseFragment;
import com.bottlerocketstudios.continuitysample.databinding.LegislatorDetailFragmentBinding;
import com.bottlerocketstudios.continuitysample.legislator.presenter.LegislatorDetailPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

public class LegislatorDetailFragment extends BaseFragment {

    private static final String ARG_LEGISLATOR = "legislator";

    LegislatorDetailPresenter mPresenter;
    LegislatorDetailFragmentBinding mBinding;
    private LegislatorViewModel mLegislatorViewModel;

    public static LegislatorDetailFragment newInstance(LegislatorViewModel legislatorViewModel) {
        LegislatorDetailFragment fragment = new LegislatorDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LEGISLATOR, legislatorViewModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This time the ViewModel is supplied upon creation of the fragment by the Activity.
        mLegislatorViewModel = getArguments().getParcelable(ARG_LEGISLATOR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.legislator_detail_fragment, container, false);

        mPresenter = getPresenterRepository().with(this, LegislatorDetailPresenter.class).tag(mLegislatorViewModel.getBioguideId()).build();
        mPresenter.bindListener(mPresenterListener);
        mPresenter.setViewModel(mLegislatorViewModel);

        mBinding.setPresenter(mPresenter);
        mBinding.setLegislator(mLegislatorViewModel);

        return mBinding.getRoot();
    }

    private final LegislatorDetailPresenter.Listener mPresenterListener = new LegislatorDetailPresenter.Listener() {
        @Override
        public void launchUrl(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    };

}
