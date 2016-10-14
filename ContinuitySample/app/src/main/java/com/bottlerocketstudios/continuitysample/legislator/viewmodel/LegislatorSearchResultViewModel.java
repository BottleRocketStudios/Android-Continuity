package com.bottlerocketstudios.continuitysample.legislator.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bottlerocketstudios.continuitysample.BR;

/**
 * Created on 9/16/16.
 */
public class LegislatorSearchResultViewModel extends BaseObservable {
    private boolean mSearchInProgress;
    private boolean mEmptyMessageVisible;
    private boolean mSearchStarted;

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

    @Bindable
    public boolean isSearchStarted() {
        return mSearchStarted;
    }

    public void setSearchStarted(boolean searchStarted) {
        mSearchStarted = searchStarted;
        notifyPropertyChanged(BR.searchStarted);
    }
}
