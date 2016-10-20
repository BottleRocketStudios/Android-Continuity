package com.bottlerocketstudios.continuitysample.legislator.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bottlerocketstudios.continuitysample.BR;

/**
 * Meta UI state for a legislator search presenter.
 */
public class LegislatorSearchResultViewModel extends BaseObservable {
    private boolean mSearchInProgress;
    private boolean mEmptyMessageVisible;

    @Bindable
    public boolean isSearchInProgress() {
        return mSearchInProgress;
    }

    public void setSearchInProgress(boolean searchInProgress) {
        mSearchInProgress = searchInProgress;
        notifyPropertyChanged(BR.searchInProgress);
    }

    @Bindable
    public boolean isEmptyMessageVisible() {
        return mEmptyMessageVisible;
    }

    public void setEmptyMessageVisible(boolean emptyMessageVisible) {
        mEmptyMessageVisible = emptyMessageVisible;
        notifyPropertyChanged(BR.emptyMessageVisible);
    }
}
