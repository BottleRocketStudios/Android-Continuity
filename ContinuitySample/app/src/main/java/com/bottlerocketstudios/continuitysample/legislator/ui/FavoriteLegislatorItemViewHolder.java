package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.bottlerocketstudios.continuitysample.BR;
import com.bottlerocketstudios.continuitysample.legislator.presenter.FavoriteLegislatorListPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * Created on 7/7/16.
 */
public class FavoriteLegislatorItemViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding mFavoriteLegislatorItemBinding;

    public FavoriteLegislatorItemViewHolder(ViewDataBinding favoriteLegislatorItemBinding) {
        super(favoriteLegislatorItemBinding.getRoot());
        mFavoriteLegislatorItemBinding = favoriteLegislatorItemBinding;
    }

    public void bind(LegislatorViewModel legislatorViewModel, FavoriteLegislatorListPresenter presenter) {
        mFavoriteLegislatorItemBinding.setVariable(BR.legislator, legislatorViewModel);
        mFavoriteLegislatorItemBinding.setVariable(BR.presenter, presenter);
    }
}