package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.bottlerocketstudios.continuitysample.BR;
import com.bottlerocketstudios.continuitysample.legislator.presenter.FavoriteLegislatorListPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * General ViewDataBinding based ViewHolder.
 */
public class FavoriteLegislatorItemViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding mFavoriteLegislatorItemBinding;

    /**
     * Use a generic ViewDataBinding to bind two different layouts.
     */
    public FavoriteLegislatorItemViewHolder(ViewDataBinding favoriteLegislatorItemBinding) {
        super(favoriteLegislatorItemBinding.getRoot());
        mFavoriteLegislatorItemBinding = favoriteLegislatorItemBinding;
    }

    public void bind(LegislatorViewModel legislatorViewModel, FavoriteLegislatorListPresenter presenter) {
        //Demonstration of using a generic ViewDataBinding if different layouts were inflated that
        //accept the same values.
        mFavoriteLegislatorItemBinding.setVariable(BR.legislator, legislatorViewModel);
        mFavoriteLegislatorItemBinding.setVariable(BR.presenter, presenter);
    }
}