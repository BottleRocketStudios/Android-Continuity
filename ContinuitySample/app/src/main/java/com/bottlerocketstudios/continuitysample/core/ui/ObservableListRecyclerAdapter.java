package com.bottlerocketstudios.continuitysample.core.ui;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created on 7/8/16.
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
