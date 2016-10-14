package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.ObservableListRecyclerAdapter;
import com.bottlerocketstudios.continuitysample.databinding.LegislatorSearchItemBinding;
import com.bottlerocketstudios.continuitysample.legislator.presenter.LegislatorSearchResultPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * Created on 7/7/16.
 */
public class LegislatorSearchRecyclerAdapter extends ObservableListRecyclerAdapter<LegislatorViewModel, LegislatorSearchItemViewHolder> {
    private static final String TAG = LegislatorSearchRecyclerAdapter.class.getSimpleName();

    private LegislatorSearchResultPresenter mPresenter;

    @Override
    public LegislatorSearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LegislatorSearchItemBinding itemBinding = DataBindingUtil.inflate(getLayoutInflator(parent), R.layout.legislator_search_item, parent, false);
        return new LegislatorSearchItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(LegislatorSearchItemViewHolder holder, int position) {
        holder.bind(getItem(position), mPresenter);
    }

    public void setPresenter(LegislatorSearchResultPresenter presenter) {
        mPresenter = presenter;
    }
}
