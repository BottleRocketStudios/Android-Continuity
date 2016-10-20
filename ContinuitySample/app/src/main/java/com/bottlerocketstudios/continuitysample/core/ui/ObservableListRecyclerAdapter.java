package com.bottlerocketstudios.continuitysample.core.ui;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Generic RecyclerAdapter that will automatically observe an ObservableList and notify the Recycler
 * of the appropriate updates as they map from ObservableList to RecyclerAdapter calls. This is ran
 * through a WeakObservableListWatcher because there isn't a clear way (AFAIK) to know when this
 * Adapter is no longer in use. The onDetachedFromRecyclerView callback is only on explicit detach.
 */
public abstract class ObservableListRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final WeakObservableListWatcher<T> mOnListChangedCallback;
    private LayoutInflater mLayoutInflator;
    protected ObservableList<T> mObservableList = new ObservableArrayList<>();

    public ObservableListRecyclerAdapter() {
        mOnListChangedCallback = new WeakObservableListWatcher<T>(this);
    }

    protected LayoutInflater getLayoutInflator(View view) {
        if (mLayoutInflator == null) {
            mLayoutInflator = LayoutInflater.from(view.getContext());
        }
        return mLayoutInflator;
    }

    public void swapList(ObservableList<T> observableList) {
        mObservableList.removeOnListChangedCallback(mOnListChangedCallback);
        mObservableList = observableList;
        mObservableList.addOnListChangedCallback(mOnListChangedCallback);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mObservableList.get(position);
    }

    @Override
    public int getItemCount() {
        return mObservableList.size();
    }
}
