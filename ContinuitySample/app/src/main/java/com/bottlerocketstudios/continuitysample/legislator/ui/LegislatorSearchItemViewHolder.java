package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.support.v7.widget.RecyclerView;

import com.bottlerocketstudios.continuitysample.databinding.LegislatorSearchItemBinding;
import com.bottlerocketstudios.continuitysample.legislator.presenter.LegislatorSearchResultPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * Straightforward specific layout binding ViewHolder.
 */
public class LegislatorSearchItemViewHolder extends RecyclerView.ViewHolder {
    private final LegislatorSearchItemBinding mLegislatorItemBinding;

    public LegislatorSearchItemViewHolder(LegislatorSearchItemBinding legislatorItemBinding) {
        super(legislatorItemBinding.getRoot());
        mLegislatorItemBinding = legislatorItemBinding;
    }

    public void bind(LegislatorViewModel legislatorViewModel, LegislatorSearchResultPresenter presenter) {
        mLegislatorItemBinding.setLegislator(legislatorViewModel);
        mLegislatorItemBinding.setPresenter(presenter);
    }
}