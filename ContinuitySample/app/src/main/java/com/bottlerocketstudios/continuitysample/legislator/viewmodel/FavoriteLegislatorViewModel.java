package com.bottlerocketstudios.continuitysample.legislator.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bottlerocketstudios.continuitysample.BR;

/**
 * Created on 9/14/16.
 */
public class FavoriteLegislatorViewModel extends BaseObservable {
    private boolean mEmptyMessageVisible;

    @Bindable
    public boolean isEmptyMessageVisible() {
        return mEmptyMessageVisible;
    }

    public void setEmptyMessageVisible(boolean emptyMessageVisible) {
        mEmptyMessageVisible = emptyMessageVisible;
        notifyPropertyChanged(BR.emptyMessageVisible);
    }
}
