package com.bottlerocketstudios.continuitysample.legislator.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.bottlerocketstudios.continuitysample.R;
import com.bottlerocketstudios.continuitysample.core.ui.ObservableListRecyclerAdapter;
import com.bottlerocketstudios.continuitysample.legislator.presenter.FavoriteLegislatorListPresenter;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

/**
 * Recycler adapter for favorite legislators that uses two different layouts to demonstrate that
 * capability of DataBinding.
 */
public class FavoriteLegislatorRecyclerAdapter extends ObservableListRecyclerAdapter<LegislatorViewModel, FavoriteLegislatorItemViewHolder> {
    private static final String TAG = FavoriteLegislatorRecyclerAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_A = 1;
    private static final int VIEW_TYPE_B = 2;

    private FavoriteLegislatorListPresenter mFavoriteLegislatorListPresenter;

    @Override
    public FavoriteLegislatorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Use two different layouts by using a generic ViewDataBinding instead of the generated binding specific to the layout.
        int layoutId = viewType == VIEW_TYPE_A ? R.layout.favorite_legislator_item_a : R.layout.favorite_legislator_item_b;
        ViewDataBinding itemBinding = DataBindingUtil.inflate(getLayoutInflator(parent), layoutId, parent, false);
        return new FavoriteLegislatorItemViewHolder(itemBinding);
    }

    @Override
    public int getItemViewType(int position) {
        //Switch layout odd-even.
        return position % 2 == 0 ? VIEW_TYPE_A : VIEW_TYPE_B;
    }

    @Override
    public void onBindViewHolder(FavoriteLegislatorItemViewHolder holder, int position) {
        holder.bind(getItem(position), mFavoriteLegislatorListPresenter);
    }

    public void setFavoriteLegislatorListPresenter(FavoriteLegislatorListPresenter favoriteLegislatorListPresenter) {
        mFavoriteLegislatorListPresenter = favoriteLegislatorListPresenter;
    }
}
