package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.ObservableListRecyclerAdapter;
import com.bottlerocketstudios.continuitysample.databinding.FavoriteLegislatorItemBinding;
import com.bottlerocketstudios.continuitysample.legislator.presenter.FavoriteLegislatorListPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * Created on 7/7/16.
 */
public class FavoriteLegislatorRecyclerAdapter extends ObservableListRecyclerAdapter<LegislatorViewModel, FavoriteLegislatorItemViewHolder> {
    private static final String TAG = FavoriteLegislatorRecyclerAdapter.class.getSimpleName();

    private FavoriteLegislatorListPresenter mFavoriteLegislatorListPresenter;

    @Override
    public FavoriteLegislatorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FavoriteLegislatorItemBinding itemBinding = DataBindingUtil.inflate(getLayoutInflator(parent), R.layout.favorite_legislator_item, parent, false);
        return new FavoriteLegislatorItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(FavoriteLegislatorItemViewHolder holder, int position) {
        holder.bind(getItem(position), mFavoriteLegislatorListPresenter);
    }

    public void setFavoriteLegislatorListPresenter(FavoriteLegislatorListPresenter favoriteLegislatorListPresenter) {
        mFavoriteLegislatorListPresenter = favoriteLegislatorListPresenter;
    }
}
